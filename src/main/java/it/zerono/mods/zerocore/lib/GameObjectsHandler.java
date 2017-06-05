package it.zerono.mods.zerocore.lib;

import it.zerono.mods.zerocore.internal.ZeroCore;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameObjectsHandler implements IModInitializationHandler {

    private GameObjectsHandler() {

        this._objects = new ArrayList<>();
        this._remapBlocks = new LowerCaseRemapper<>();
        this._remapItems = new LowerCaseRemapper<>();
    }

    @Nonnull
    public <I extends Item & IGameObject> I register(@Nonnull final I item) {

        this._objects.add(item);

        final I result = ZeroCore.getProxy().registerGameObject(item);

        this.addRemapEntry(result);
        return result;
    }

    @Nonnull
    public <B extends Block & IGameObject> B register(@Nonnull final B block) {

        this._objects.add(block);

        final B result = ZeroCore.getProxy().registerGameObject(block);

        this.addRemapEntry(result);
        return result;
    }

    public void register(@Nonnull final Class<? extends TileEntity> tileEntityClass, @Nonnull final String prefix) {
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

    public void onMissingMapping(FMLMissingMappingsEvent event) {

        for (FMLMissingMappingsEvent.MissingMapping mapping : event.get()) {

            switch (mapping.type) {

                case ITEM:
                    this._remapItems.remap(mapping);
                    break;

                case BLOCK:
                    this._remapBlocks.remap(mapping);
                    break;
            }
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

        void remap(final FMLMissingMappingsEvent.MissingMapping mapping) {

            String candidateName = mapping.resourceLocation.getResourcePath().toLowerCase();

            if (this._map.containsKey(candidateName)) {

                T replacement = this._map.get(candidateName);

                if (GameRegistry.Type.BLOCK == mapping.type && replacement instanceof Block)
                    mapping.remap((Block)replacement);

                else if (GameRegistry.Type.ITEM == mapping.type && replacement instanceof Item)
                    mapping.remap((Item) replacement);
            }
        }

        private Map<String, T> _map;
    }

    private List<IGameObject> _objects;
    private final LowerCaseRemapper<Block> _remapBlocks;
    private final LowerCaseRemapper<Item> _remapItems;
}