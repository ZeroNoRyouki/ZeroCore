package it.zerono.mods.zerocore.internal.common;

import it.zerono.mods.zerocore.api.multiblock.IMultiblockRegistry;
import it.zerono.mods.zerocore.internal.common.init.ZeroItems;
import it.zerono.mods.zerocore.lib.IGameObject;
import it.zerono.mods.zerocore.lib.IModInitializationHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;

public class CommonProxy implements IModInitializationHandler {

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

    @Nonnull
    public <I extends Item & IGameObject> I registerGameObject(@Nonnull I item) {

        GameRegistry.register(item);
        item.onPostRegister();
        return item;
    }

    @Nonnull
    public <B extends Block & IGameObject> B registerGameObject(@Nonnull B block) {

        GameRegistry.register(block);
        block.onPostRegister();
        return block;
    }

    public void sendPlayerStatusMessage(@Nonnull final EntityPlayer player, @Nonnull final ITextComponent message) {

        if (player instanceof EntityPlayerMP)
            ((EntityPlayerMP)player).connection.sendPacket(new SPacketChat(message, (byte)2));
    }

    private static MultiblockEventHandler s_multiblockHandler = null;
}
