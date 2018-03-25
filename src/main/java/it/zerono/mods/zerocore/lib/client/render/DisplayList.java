package it.zerono.mods.zerocore.lib.client.render;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public final class DisplayList {

    public DisplayList() {
        this._id = GlStateManager.glGenLists(1);
    }

    @Override
    public void finalize() {
        GlStateManager.glDeleteLists(this._id, 1);
    }

    public void beginList() {
        GlStateManager.glNewList(this._id, GL11.GL_COMPILE);
    }

    public void endList() {
        GlStateManager.glEndList();
    }

    public void play() {
        GlStateManager.callList(this._id);
    }

    private final int _id;
}