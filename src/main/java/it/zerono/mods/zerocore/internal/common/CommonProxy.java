package it.zerono.mods.zerocore.internal.common;

import it.zerono.mods.zerocore.api.multiblock.IMultiblockRegistry;
import it.zerono.mods.zerocore.internal.common.init.ZeroItems;
import it.zerono.mods.zerocore.lib.IGameObject;
import it.zerono.mods.zerocore.lib.IModInitializationHandler;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy implements IModInitializationHandler {

    public <T extends Item & IGameObject> T register(T item) {

        GameRegistry.register(item);
        item.onPostRegister();
        return item;
    }

    public IMultiblockRegistry initMultiblockRegistry() {

        if (null == s_multiblockHandler)
            MinecraftForge.EVENT_BUS.register(s_multiblockHandler = new MultiblockEventHandler());

        return MultiblockRegistry.INSTANCE;
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        ZeroItems.initialize();
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        ZeroItems.debugTool.registerRecipes();
    }

    @Override
    public void onPostInit(FMLPostInitializationEvent event) {
    }

    private static MultiblockEventHandler s_multiblockHandler = null;
}
