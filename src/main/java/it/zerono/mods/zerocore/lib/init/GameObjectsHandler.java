package it.zerono.mods.zerocore.lib.init;

import com.google.common.collect.*;
import it.zerono.mods.zerocore.lib.IModInitializationHandler;
import it.zerono.mods.zerocore.lib.config.ConfigHandler;
import it.zerono.mods.zerocore.lib.data.fixer.GameObjectWalker;
import it.zerono.mods.zerocore.lib.data.fixer.IGameObjectDataWalker;
import it.zerono.mods.zerocore.util.CodeHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.IFixableData;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GameObjectsHandler implements IModInitializationHandler {

    protected GameObjectsHandler(final int dataVersion, ConfigHandler... configs) {

        this._dataVersion = dataVersion;
        this._modId = CodeHelper.getModIdFromActiveModContainer();
        this._configHandlers = null != configs ? ImmutableList.copyOf(configs) : ImmutableList.of();
        this._tilesWalker = null;

        this.addBlockRemapper(new LowerCaseMapper<>());
        this.addItemRemapper(new LowerCaseMapper<>());

        MinecraftForge.EVENT_BUS.register(this);
        this.syncConfigHandlers();
    }

    @Nonnull
    protected String getModId() {
        return this._modId;
    }

    protected int getDataVersion() {
        return this._dataVersion;
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
     * Override in your subclass to register your recipes with the provided registry
     * @param registry the recipes registry
     */
    protected void onRegisterRecipes(@Nonnull final IForgeRegistry<IRecipe> registry) {
    }

    /**
     * Register all your IDataFixer and IDataWalker
     */
    protected void onRegisterDataFixers() {

        if (null != this._tilesWalker) {
            this.registerDataWalker(FixTypes.BLOCK_ENTITY, this._tilesWalker);
        }
    }

    protected void addBlockRemapper(@Nonnull final IGameObjectMapper<Block> remapper) {
        this._blocksMappers.add(remapper);
    }

    protected void addItemRemapper(@Nonnull final IGameObjectMapper<Item> remapper) {
        this._itemsMappers.add(remapper);
    }

    protected void registerTileEntity(@Nonnull final Class<? extends TileEntity> tileEntityClass) {
        this.registerTileEntity(tileEntityClass, null);
    }

    protected void registerTileEntity(@Nonnull final Class<? extends TileEntity> tileEntityClass,
                                      @Nullable final IGameObjectDataWalker walker) {

        final ResourceLocation id = new ResourceLocation(this.getModId(), tileEntityClass.getSimpleName());

        GameRegistry.registerTileEntity(tileEntityClass, id);

        if (null != walker) {

            if (null == this._tilesWalker) {
                this._tilesWalker = new GameObjectWalker();
            }

            this._tilesWalker.addObjectWalker(id, walker);
        }
    }

    protected void registerDataFixer(@Nonnull final FixTypes type, @Nonnull final IFixableData fixer) {
        this.getModFixs().registerFix(type, fixer);
    }

    protected void registerDataWalker(@Nonnull final FixTypes type, @Nonnull final IDataWalker walker) {
        FMLCommonHandler.instance().getDataFixer().registerWalker(type, walker);
    }

    @Nullable
    protected Block getTrackedBlock(@Nonnull final String name) {
        return this._blocks.get(name);
    }

    @Nullable
    protected Item getTrackedItem(@Nonnull final String name) {
        return this._items.get(name);
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
     * Handles Forge Register event for Recipes
     * Raise the onRegisterRecipes event and gather all the newly registered items and then ask all the previously
     * registered blocks to register their ItemBlocks. Next, Ore Dictionary entries are registered
     * @param event
     */
    @SubscribeEvent
    protected void onRegisterRecipes(RegistryEvent.Register<IRecipe> event) {

        final IForgeRegistry<IRecipe> registry = event.getRegistry();

        this.onRegisterRecipes(registry);
        raiseRegisterRecipes(this._blocks.values(), registry);
        raiseRegisterRecipes(this._items.values(), registry);
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

    @SubscribeEvent
    protected void onMissinBlockMappings(RegistryEvent.MissingMappings<Block> event) {
        remapObjects(this._blocksMappers, event.getMappings());
    }

    @SubscribeEvent
    protected void onMissingItemMapping(RegistryEvent.MissingMappings<Item> event) {
        remapObjects(this._itemsMappers, event.getMappings());
    }

    @SubscribeEvent
    protected void onConfigChangedFromGUI(ConfigChangedEvent.OnConfigChangedEvent event) {

        if (this._modId.equalsIgnoreCase(event.getModID())) {

            this.syncConfigHandlers();
            this.notifyConfigListeners();
        }
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
    }

    @Override
    public void onInit(FMLInitializationEvent event) {

        this.notifyConfigListeners();
        this.onRegisterDataFixers();
    }

    @Override
    public void onPostInit(FMLPostInitializationEvent event) {
    }

    //region internals

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
            @Nonnull final ImmutableCollection<T> objects, @Nonnull final IForgeRegistry<IRecipe> registry) {

        for (T object : objects)
            if (object instanceof IGameObject)
                ((IGameObject)object).onRegisterRecipes(registry);
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
            @Nonnull final ImmutableList<RegistryEvent.MissingMappings.Mapping<T>> mappings) {

        for (final RegistryEvent.MissingMappings.Mapping<T> mapping : mappings) {
            for (final IGameObjectMapper<T> mapper : mappers) {

                mapper.remap(mapping);

                if (RegistryEvent.MissingMappings.Action.DEFAULT != mapping.getAction())
                    break;
            }
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

    @Nonnull
    private ModFixs getModFixs() {

        if (null == this._modFixs) {
            this._modFixs = FMLCommonHandler.instance().getDataFixer().init(this.getModId(), this.getDataVersion());
        }

        return this._modFixs;
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
            this._trackedObjects.put(value.getRegistryName().getPath(), value);
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
        public void remap(@Nonnull RegistryEvent.MissingMappings.Mapping<T> mapping) {

            final String candidateName = mapping.key.getPath().toLowerCase();

            if (this._map.containsKey(candidateName)) {

                final T replacement = this._map.get(candidateName);

                mapping.remap(replacement);
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

    // Data fixers
    private final int _dataVersion;
    private ModFixs _modFixs;
    private GameObjectWalker _tilesWalker;
}