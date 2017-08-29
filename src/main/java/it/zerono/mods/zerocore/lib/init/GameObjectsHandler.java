package it.zerono.mods.zerocore.lib.init;

import com.google.common.collect.*;
import it.zerono.mods.zerocore.lib.IModInitializationHandler;
import it.zerono.mods.zerocore.lib.config.ConfigHandler;
import it.zerono.mods.zerocore.util.CodeHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GameObjectsHandler implements IModInitializationHandler {

    protected GameObjectsHandler(ConfigHandler... configs) {

        this._modId = CodeHelper.getModIdFromActiveModContainer();
        this._configHandlers = null != configs ? ImmutableList.copyOf(configs) : ImmutableList.of();

        this.addBlockRemapper(new LowerCaseMapper<>());
        this.addItemRemapper(new LowerCaseMapper<>());

        MinecraftForge.EVENT_BUS.register(this);
        this.syncConfigHandlers();
    }

    /**
     * Create all the blocks instances for this mod and register them
     * Override in your subclass to create your blocks instances and register them with the provided registry
     * @param registry the block registry
     */
    protected void onRegisterBlocks(@Nonnull final IForgeRegistry<Block> registry) {
    }

    /**
     * Register all the tile entities for this mod
     */
    protected void onRegisterTileEntities() {
    }

    /**
     * Create all the items instances for this mod and register them
     * Override in your subclass to create your items instances and register them with the provided registry
     * @param registry the item registry
     */
    protected void onRegisterItems(@Nonnull final IForgeRegistry<Item> registry) {
    }

    /**
     * Register all custom Ore Dictionary entries for your blocks and items
     * Override in your subclass to register your Ore Dictionary entries
     */
    protected void onRegisterOreDictionaryEntries() {
    }

    /**
     * Register all the recipes for the blocks and items of this mod
     * Override in your subclass to register your recipes
     */
    protected void onRegisterRecipes() {
    }

    /*
    @Deprecated
    @Nonnull
    public <I extends Item & IGameObject> I registerItem(@Nonnull final String name, @Nonnull final I item) {

        item.setRegistryName(name);
        item.setUnlocalizedName(item.getRegistryName().toString());
        this._items.add(item);

        final I result = ZeroCore.getProxy().registerGameObject(item);

        this.addRemapEntry(result);
        return result;
    }

    @Deprecated
    @Nonnull
    public <B extends Block & IGameObject, I extends ItemBlock & IGameObject> B registerBlock(@Nonnull final String name,
                                                                                              @Nonnull final B block,
                                                                                              @Nonnull final I itemBlock) {

        block.setRegistryName(name);
        block.setUnlocalizedName(block.getRegistryName().toString());
        this._blocks.add(block);

        this.registerItem(name, itemBlock);




        final B result = ZeroCore.getProxy().registerGameObject(block);

        this.addRemapEntry(result);
        return result;
    }
    */

    protected void addBlockRemapper(@Nonnull final IGameObjectMapper<Block> remapper) {
        this._blocksMappers.add(remapper);
    }

    protected void addItemRemapper(@Nonnull final IGameObjectMapper<Item> remapper) {
        this._itemsMappers.add(remapper);
    }

    protected void registerTileEntity(@Nonnull final Class<? extends TileEntity> tileEntityClass, @Nonnull final String prefix) {
        GameRegistry.registerTileEntity(tileEntityClass, prefix + tileEntityClass.getSimpleName());
    }

    /**
     * Handles Forge Register event for Blocks
     * Raise the onRegisterBlocks and onRegisterTileEntities events and gather all the newly registered blocks
     * @param event
     */
    @SubscribeEvent
    protected void onRegisterBlocks(RegistryEvent.Register<Block> event) {

        final TrackingForgeRegistry<Block> tracker = new TrackingForgeRegistry<>(event.getRegistry());

        this.onRegisterBlocks(tracker);
        this._blocks = tracker.getTrackedObjects();
        this._blocksMappers.stream().forEach(mapper -> mapper.linkObjectsMap(this._blocks));

        this.onRegisterTileEntities();
    }

    /**
     * Handles Forge Register event for Items
     * Raise the onRegisterItems event and gather all the newly registered items and then ask all the previously
     * registered blocks to register their ItemBlocks. Next, Ore Dictionary entries are registered
     * @param event
     */
    @SubscribeEvent
    protected void onRegisterItems(RegistryEvent.Register<Item> event) {

        final IForgeRegistry<Item> registry = event.getRegistry();
        final TrackingForgeRegistry<Item> tracker = new TrackingForgeRegistry<>(registry);

        this.onRegisterItems(tracker);
        this._items = tracker.getTrackedObjects();

        raiseRegisterItemBlocks(this._blocks.values(), registry);

        this._itemsMappers.stream().forEach(mapper -> mapper.linkObjectsMap(this._items));

        // register Ore Dictionary entries

        this.onRegisterOreDictionaryEntries();
        raiseRegisterOreDictionaryEntries(this._blocks.values());
        raiseRegisterOreDictionaryEntries(this._items.values());
    }

    /**
     * Handles client-side models registration
     * @param event
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    protected void onRegisterModels(ModelRegistryEvent event) {

        raiseRegisterModels(this._blocks.values());
        raiseRegisterModels(this._items.values());
    }

    @Mod.EventHandler
    public void onMissinMappings(FMLMissingMappingsEvent event) {

        for (final FMLMissingMappingsEvent.MissingMapping mapping : event.get()) {

            switch (mapping.type) {

                case ITEM:
                    remapObjects(this._itemsMappers, mapping);
                    break;

                case BLOCK:
                    remapObjects(this._blocksMappers, mapping);
                    break;
            }
        }
    }

    @SubscribeEvent
    protected void onConfigChangedFromGUI(ConfigChangedEvent.OnConfigChangedEvent event) {

        if (this._modId.equalsIgnoreCase(event.getModID())) {

            this.syncConfigHandlers();
            this.notifyConfigListeners();
        }
    }

    @Override
    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
    }

    @Override
    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {

        this.notifyConfigListeners();

        this.onRegisterRecipes();
        raiseRegisterRecipes(this._blocks.values());
        raiseRegisterRecipes(this._items.values());
    }

    @Override
    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
    }

    private static <T extends IForgeRegistryEntry<T>> void raiseRegisterItemBlocks(
            @Nonnull final ImmutableCollection<T> objects, @Nonnull final IForgeRegistry<Item> registry) {

        for (T object : objects)
            if (object instanceof IGameObject)
                ((IGameObject)object).onRegisterItemBlocks(registry);
    }

    private static <T extends IForgeRegistryEntry<T>> void raiseRegisterOreDictionaryEntries(
            @Nonnull final ImmutableCollection<T> objects) {

        for (T object : objects)
            if (object instanceof IGameObject)
                ((IGameObject)object).onRegisterOreDictionaryEntries();
    }

    private static <T extends IForgeRegistryEntry<T>> void raiseRegisterRecipes(
            @Nonnull final ImmutableCollection<T> objects) {

        for (T object : objects)
            if (object instanceof IGameObject)
                ((IGameObject)object).onRegisterRecipes();
    }

    @SideOnly(Side.CLIENT)
    private static <T extends IForgeRegistryEntry<T>> void raiseRegisterModels(
            @Nonnull final ImmutableCollection<T> objects) {

        for (T object : objects)
            if (object instanceof IGameObject)
                ((IGameObject)object).onRegisterModels();
    }

    private static <T extends IForgeRegistryEntry<T>> void remapObjects(
            @Nonnull final List<IGameObjectMapper<T>> mappers,
            @Nonnull final FMLMissingMappingsEvent.MissingMapping mapping) {

        for (final IGameObjectMapper<T> mapper : mappers) {

            mapper.remap(mapping);

            if (FMLMissingMappingsEvent.Action.DEFAULT != mapping.getAction())
                break;
        }
    }

    private void syncConfigHandlers() {

        for (final ConfigHandler handler : this._configHandlers)
            handler.sync();
    }

    private void notifyConfigListeners() {

        for (final ConfigHandler handler : this._configHandlers)
            handler.notifyListeners();
    }

    private static class TrackingForgeRegistry<T extends IForgeRegistryEntry<T>>
            implements IForgeRegistry<T> {

        public TrackingForgeRegistry(@Nonnull final IForgeRegistry<T> registry) {

            this._registry = registry;
            this._trackedObjects = Maps.newHashMap();
        }

        @Nonnull
        public ImmutableMap<String, T> getTrackedObjects() {
            return ImmutableMap.copyOf(this._trackedObjects);
        }

        @Override
        public Class<T> getRegistrySuperType() {
            return this._registry.getRegistrySuperType();
        }

        @Override
        public void register(T value) {

            this._registry.register(value);
            this._trackedObjects.put(value.getRegistryName().getResourcePath(), value);
        }

        @Override
        public void registerAll(T[] values) {

            for (T value : values)
                this.register(value);
        }

        @Override
        public boolean containsKey(ResourceLocation key) {
            return this._registry.containsKey(key);
        }

        @Override
        public boolean containsValue(T value) {
            return this._registry.containsValue(value);
        }

        @Nullable
        @Override
        public T getValue(ResourceLocation key) {
            return this._registry.getValue(key);
        }

        @Nullable
        @Override
        public ResourceLocation getKey(T value) {
            return this._registry.getKey(value);
        }

        @Nonnull
        @Override
        public Set<ResourceLocation> getKeys() {
            return this._registry.getKeys();
        }

        @Nonnull
        @Override
        public List<T> getValues() {
            return this._registry.getValues();
        }

        @Nonnull
        @Override
        public Set<Map.Entry<ResourceLocation, T>> getEntries() {
            return this._registry.getEntries();
        }

        /**
         * Retrieve the slave map of type T from the registry.
         * Slave maps are maps which are dependent on registry content in some way.
         *
         * @param slaveMapName The name of the slavemap
         * @param type         The type
         * @return The slavemap if present
         */
        @Override
        public <T> T getSlaveMap(ResourceLocation slaveMapName, Class<T> type) {
            return this._registry.getSlaveMap(slaveMapName, type);
        }

        @Override
        public Iterator<T> iterator() {
            return this._registry.iterator();
        }

        private final IForgeRegistry<T> _registry;
        private final Map<String, T> _trackedObjects;
    }

    private static class LowerCaseMapper<T extends IForgeRegistryEntry<T>> implements IGameObjectMapper<T> {

        /**
         * Link the currently registered objects to this mapper
         * This method is called after the objects are registered in the game
         *
         * @param map the registered objects
         */
        @Override
        public void linkObjectsMap(@Nonnull ImmutableMap<String, T> map) {
            this._map = map;
        }

        /**
         * Process a missing mapping
         * The mapping will be sent to all registered IGameObjectMappers for the particular type of object until one
         * of them remap the object
         *
         * @param mapping the object to remap
         */
        @Override
        public void remap(@Nonnull FMLMissingMappingsEvent.MissingMapping mapping) {

            final String candidateName = mapping.resourceLocation.getResourcePath().toLowerCase();

            if (this._map.containsKey(candidateName)) {

                final T replacement = this._map.get(candidateName);

                if (replacement instanceof Block)
                    mapping.remap((Block)replacement);

                else if (replacement instanceof Item)
                    mapping.remap((Item)replacement);
            }
        }

        private ImmutableMap<String, T> _map;
    }

    private final String _modId;
    private final ImmutableList<ConfigHandler> _configHandlers;
    private ImmutableMap<String, Block> _blocks;
    private ImmutableMap<String, Item> _items;
    private final List<IGameObjectMapper<Block>> _blocksMappers = Lists.newArrayList();
    private final List<IGameObjectMapper<Item>> _itemsMappers = Lists.newArrayList();
}