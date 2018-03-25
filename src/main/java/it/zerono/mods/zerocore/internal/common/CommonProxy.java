package it.zerono.mods.zerocore.internal.common;

import it.zerono.mods.zerocore.api.multiblock.IMultiblockRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CommonProxy {

    public IMultiblockRegistry initMultiblockRegistry() {

        if (null == s_multiblockHandler)
            MinecraftForge.EVENT_BUS.register(s_multiblockHandler = new MultiblockEventHandler());

        return MultiblockRegistry.INSTANCE;
    }

    public void sendPlayerStatusMessage(@Nonnull final EntityPlayer player, @Nonnull final ITextComponent message) {

        if (player instanceof EntityPlayerMP)
            ((EntityPlayerMP)player).connection.sendPacket(new SPacketChat(message, ChatType.GAME_INFO));
    }

    public IThreadListener getClientThreadListener() {
        return null;
    }

    public IThreadListener getServerThreadListener() {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
    }

    private static MultiblockEventHandler s_multiblockHandler = null;
}
