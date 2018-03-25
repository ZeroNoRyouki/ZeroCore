package it.zerono.mods.zerocore.lib.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public abstract class CachedRender {

    public void paint(final double x, final double y, final double z) {

        if (null == this._list) {

            this._list = new DisplayList();
            this._list.beginList();
            this.buildRender();
            this._list.endList();
        }

        this.onBeginPainting(x, y, z);
        ModRenderHelper.bindTexture(this.getTexture());
        this._list.play();
        this.onEndPainting(x, y, z);
    }

    public void invalidate() {
        this._list = null;
    }

    protected abstract ResourceLocation getTexture();

    protected abstract void buildRender();

    protected void onBeginPainting(final double x, final double y, final double z) {

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
    }

    protected void onEndPainting(final double x, final double y, final double z) {

        GlStateManager.translate(-x, -y, -z);
        GlStateManager.popMatrix();
    }

    private DisplayList _list;
}