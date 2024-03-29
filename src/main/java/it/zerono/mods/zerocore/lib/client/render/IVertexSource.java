package it.zerono.mods.zerocore.lib.client.render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public interface IVertexSource {

    void uploadVertexData(@Nonnull final BufferBuilder buffer);
}