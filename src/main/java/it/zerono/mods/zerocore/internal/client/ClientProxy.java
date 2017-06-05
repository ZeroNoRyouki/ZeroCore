package it.zerono.mods.zerocore.internal.client;

import it.zerono.mods.zerocore.internal.common.CommonProxy;
import it.zerono.mods.zerocore.lib.IGameObject;
import it.zerono.mods.zerocore.lib.client.VersionChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nonnull;

public class ClientProxy extends CommonProxy {

    @Override
    public <T extends Item & IGameObject> T register(T item) {

        super.register(item);
        item.onPostClientRegister();
        return item;
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {

        super.onPreInit(event);

        MinecraftForge.EVENT_BUS.register(VersionChecker.class);
        //VersionChecker.scheduleCheck("http://ws.zerono.it/check.ashx");
    }

    @Override
    public void sendPlayerStatusMessage(@Nonnull final EntityPlayer player, @Nonnull final ITextComponent message) {

        if (player instanceof EntityPlayerSP)
            Minecraft.getMinecraft().ingameGUI.setRecordPlayingMessage(message.getUnformattedText());
    }
}
