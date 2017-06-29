package it.zerono.mods.zerocore.lib;

import it.zerono.mods.zerocore.internal.ZeroCore;
import it.zerono.mods.zerocore.lib.block.ModBlock;
import it.zerono.mods.zerocore.lib.item.ModItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameObjectsHandler implements IModInitializationHandler {

    public GameObjectsHandler() {

        this._blocks = NonNullList.create();
        this._items = NonNullList.create();

        //this._objects = new ArrayList<>();
        this._remapBlocks = new LowerCaseRemapper<>();
        this._remapItems = new LowerCaseRemapper<>();
    }

    @Nonnull
    public <I extends Item & IGameObject> I registerItem(@Nonnull final String name, @Nonnull final I item) {

        item.setRegistryName(name);
        item.setUnlocalizedName(item.getRegistryName().toString());
        this._items.add(item);

        final I result = ZeroCore.getProxy().registerGameObject(item);

        this.addRemapEntry(result);
        return result;
    }

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

    public void registerTileEntity(@Nonnull final Class<? extends TileEntity> tileEntityClass, @Nonnull final String prefix) {
        GameRegistry.registerTileEntity(tileEntityClass, prefix + tileEntityClass.getSimpleName());
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
    }

    @Override
    public void onInit(FMLInitializationEvent event) {

        // Register ore dict entries and recipes

        for (IGameObject obj: this._objects)
            obj.registerOreDictionaryEntries();

        for (IGameObject obj: this._objects)
            obj.registerRecipes();
    }

    @Override
    public void onPostInit(FMLPostInitializationEvent event) {

        this._objects.clear();
        this._objects = null;
    }

    public void onMissinBlockMappings(RegistryEvent.MissingMappings<Block> event) {

        for (RegistryEvent.MissingMappings.Mapping<Block> mapping : event.getMappings())
            this._remapBlocks.remap(mapping);
    }

    public void onMissingItemMapping(RegistryEvent.MissingMappings<Item> event) {

        for (RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getMappings())
            this._remapItems.remap(mapping);
    }

    @SubscribeEvent
    private void onRegisterBlocks(RegistryEvent.Register<Block> event) {

        final IForgeRegistry<Block> registry = event.getRegistry();

        for (final Block block : this._blocks) {

            registry.register(block);
        }
    }

    @SubscribeEvent
    private void onRegisterItems(RegistryEvent.Register<Item> event) {

        final IForgeRegistry<Item> registry = event.getRegistry();

        for (final Item item : this._items) {

            registry.register(item);
        }
    }


    private void addRemapEntry(@Nonnull final Block block) {

        this._remapBlocks.add(block);

        final Item itemBlock = Item.REGISTRY.getObject(block.getRegistryName());

        if (null != itemBlock)
            this.addRemapEntry(itemBlock);
    }

    private void addRemapEntry(@Nonnull final Item item) {
        this._remapItems.add(item);
    }

    private static class LowerCaseRemapper<T extends IForgeRegistryEntry<T>> {

        LowerCaseRemapper() {
            this._map = new HashMap<>();
        }

        public void add(final T entry) {
            this._map.put(entry.getRegistryName().getResourcePath(), entry);
        }

        void remap(final RegistryEvent.MissingMappings.Mapping<T> mapping) {

            String candidateName = mapping.key.getResourcePath().toLowerCase();

            if (this._map.containsKey(candidateName)) {

                T replacement = this._map.get(candidateName);

                mapping.remap(replacement);
            }
        }
        /*
        void remap(final RegistryEvent.MissingMappings<T>.Mapping mapping) {

            String candidateName = mapping.key.getResourcePath().toLowerCase();

            if (this._map.containsKey(candidateName)) {

                T replacement = this._map.get(candidateName);

                mapping.remap(replacement);


                if (GameRegistry.Type.BLOCK == mapping.type && replacement instanceof Block)
                    mapping.remap((Block)replacement);

                else if (GameRegistry.Type.ITEM == mapping.type && replacement instanceof Item)
                    mapping.remap((Item) replacement);
            }
        }*/

        private Map<String, T> _map;
    }

    private final NonNullList<Block> _blocks;
    private final NonNullList<Item> _items;


    //private List<IGameObject> _objects;
    private final LowerCaseRemapper<Block> _remapBlocks;
    private final LowerCaseRemapper<Item> _remapItems;
}