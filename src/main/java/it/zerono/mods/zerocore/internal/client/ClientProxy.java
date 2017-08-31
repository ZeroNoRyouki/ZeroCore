package it.zerono.mods.zerocore.internal.client;

import it.zerono.mods.zerocore.internal.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

public class ClientProxy extends CommonProxy {

    @Override
    public void sendPlayerStatusMessage(@Nonnull final EntityPlayer player, @Nonnull final ITextComponent message) {

        if (player instanceof EntityPlayerSP)
            Minecraft.getMinecraft().ingameGUI.setRecordPlayingMessage(message.getUnformattedText());
    }
}