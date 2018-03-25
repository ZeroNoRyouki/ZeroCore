package it.zerono.mods.zerocore.lib.client.render.builder;

import it.zerono.mods.zerocore.lib.client.render.Vertex;
import it.zerono.mods.zerocore.lib.math.Colour;
import it.zerono.mods.zerocore.lib.math.LightMap;
import it.zerono.mods.zerocore.lib.math.UV;
import it.zerono.mods.zerocore.lib.math.Vector3d;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class VertexBuilder {

    @Nonnull
    public static VertexBuilder getDefaultBuilder() {

        if (null == s_defaultBuilder) {
            s_defaultBuilder = new VertexBuilder(false);
        }

        return s_defaultBuilder;
    }

    public VertexBuilder(final boolean autoReset) {
        this._autoReset = autoReset;
    }

    @Nonnull
    public Vertex build() {

        final Vertex vertex = new Vertex(this._position, this._normal, this._uv, this._colour, this._lightMap);

        if (this._autoReset) {
            this.reset();
        }

        return vertex;
    }

    public void reset() {

        this._position = this._normal = null;
        this._uv = null;
        this._colour = null;
        this._lightMap = null;
    }

    // Texture

    @Nonnull
    public VertexBuilder setTexture(@Nonnull final UV uv) {

        this._uv = uv;
        return this;
    }

    @Nonnull
    public VertexBuilder setTexture(final double u, final double v) {
        return this.setTexture(new UV(u, v));
    }

    @Nonnull
    public VertexBuilder setTexture(@Nonnull final TextureAtlasSprite sprite) {
        return this.setTexture(new UV(sprite.getMinU(), sprite.getMinV()));
    }

    // Light-map

    @Nonnull
    public VertexBuilder setLightMap(@Nonnull final LightMap lightMap) {

        this._lightMap = lightMap;
        return this;
    }

    @Nonnull
    public VertexBuilder setLightMap(final int skyLight, final int blockLight) {
        return this.setLightMap(new LightMap(skyLight, blockLight));
    }

    // Color

    @Nonnull
    public VertexBuilder setColour(@Nonnull final Colour colour) {

        this._colour = colour;
        return this;
    }

    @Nonnull
    public VertexBuilder setColour(final int red, final int green, final int blue, final int alpha) {
        return this.setColour(new Colour(red, green, blue, alpha));
    }

    @Nonnull
    public VertexBuilder setColour(final double red, final double green, final double blue, final double alpha) {
        return this.setColour(new Colour(red, green, blue, alpha));
    }

    // Position

    @Nonnull
    public VertexBuilder setPosition(@Nonnull final Vector3d position) {

        this._position = position;
        return this;
    }

    @Nonnull
    public VertexBuilder setPosition(final double x, final double y, final double z) {
        return this.setPosition(new Vector3d(x, y, z));
    }

    // Normal

    @Nonnull
    public VertexBuilder setNormal(@Nonnull final Vector3d normal) {

        this._normal = normal;
        return this;
    }

    @Nonnull
    public VertexBuilder setNormal(final double x, final double y, final double z) {
        return this.setNormal(new Vector3d(x, y, z));
    }

    private final boolean _autoReset;
    private Vector3d _position;
    private Vector3d _normal;
    private UV _uv;
    private Colour _colour;
    private LightMap _lightMap;

    private static VertexBuilder s_defaultBuilder = null;
}