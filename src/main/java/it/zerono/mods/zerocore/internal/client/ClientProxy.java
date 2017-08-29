package it.zerono.mods.zerocore.internal.client;

import it.zerono.mods.zerocore.internal.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

public class ClientProxy extends CommonProxy {

    /*
    @Override
    public void onPreInit(FMLPreInitializationEvent event) {

        super.onPreInit(event);

        MinecraftForge.EVENT_BUS.register(VersionChecker.class);
        //VersionChecker.scheduleCheck("http://ws.zerono.it/check.ashx");
    }*/

    @Override
    public void sendPlayerStatusMessage(@Nonnull final EntityPlayer player, @Nonnull final ITextComponent message) {

        if (player instanceof EntityPlayerSP)
            Minecraft.getMinecraft().ingameGUI.setRecordPlayingMessage(message.getUnformattedText());
    }
}