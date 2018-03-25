package it.zerono.mods.zerocore.lib.client.render.builder;

import it.zerono.mods.zerocore.lib.client.render.Shape;
import it.zerono.mods.zerocore.lib.client.render.Vertex;
import it.zerono.mods.zerocore.lib.math.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class QuadBuilder extends AbstractShapeBuilder {

    public final static int VERTICES_COUNT = 4;

    @Nonnull
    public static QuadBuilder getDefaultBuilder() {

        if (null == s_defaultBuilder) {
            s_defaultBuilder = new QuadBuilder(false);
        }

        return s_defaultBuilder;
    }

    public QuadBuilder(final boolean autoReset) {

        super(autoReset);
        this._faceData = new PolygonalFaceData(VERTICES_COUNT);
    }

    @Nonnull
    @Override
    public Shape build() {

        if (null == this._face) {
            throw new IllegalStateException("No face was provided!");
        }

        final Shape shape = new Shape(VERTICES_COUNT);
        final VertexBuilder vertexBuilder = new VertexBuilder(true);

        for (int vertexIndex = 0; vertexIndex < VERTICES_COUNT; ++vertexIndex) {
            shape.addVertex(buildSingleVertex(vertexIndex, vertexBuilder, this._face, this._faceData));
        }

        if (this.autoReset()) {
            this.reset();
        }

        return shape;
    }

    static Vertex buildSingleVertex(final int vertexIndex, @Nonnull final VertexBuilder vertexBuilder,
                                    @Nonnull final Cuboid.Face face, @Nonnull final PolygonalFaceData faceData) {

        final UV uv = faceData.getUvAt(vertexIndex);
        final Colour colour = faceData.getColourlAt(vertexIndex);
        final LightMap lightMap = faceData.getLightMapAt(vertexIndex);
        final Vector3d normal = faceData.getNormalAt(vertexIndex);

        vertexBuilder.setPosition(face.getVertexByIndex(vertexIndex));

        if (null != uv) {
            vertexBuilder.setTexture(uv);
        }

        if (null != colour) {
            vertexBuilder.setColour(colour);
        }

        if (null != lightMap) {
            vertexBuilder.setLightMap(lightMap);
        }

        if (null != normal) {
            vertexBuilder.setNormal(normal);
        }

        return vertexBuilder.build();
    }

    @Override
    public void reset() {

        this._face = null;
        this._faceData.reset();
    }

    // Face

    @Nonnull
    public QuadBuilder setFace(@Nonnull final Cuboid.Face face) {

        this._face = face;
        return this;
    }

    @Nonnull
    public QuadBuilder setFace(@Nonnull final Cuboid cuboid, @Nonnull final EnumFacing facing) {
        return this.setFace(cuboid.getFace(facing));
    }

    // Color

    @Nonnull
    public QuadBuilder setColour(@Nonnull final Colour colour) {

        this._faceData.setColour(colour);
        return this;
    }

    @Nonnull
    public QuadBuilder setColour(final int vertexIndex, @Nonnull final Colour colour) {

        this._faceData.setColour(vertexIndex, colour);
        return this;
    }

    // Texture

    @Nonnull
    public QuadBuilder setTexture(@Nonnull final UV a, @Nonnull final UV b, @Nonnull final UV c, @Nonnull final UV d) {

        this._faceData.setTexture(0, a);
        this._faceData.setTexture(1, b);
        this._faceData.setTexture(2, c);
        this._faceData.setTexture(3, d);
        return this;
    }

    @Nonnull
    public QuadBuilder setTexture(final int vertexIndex, @Nonnull final UV uv) {

        this._faceData.setTexture(vertexIndex, uv);
        return this;
    }

    @Nonnull
    public QuadBuilder setTexture(@Nonnull final TextureAtlasSprite sprite) {

        this._faceData.setTexture(sprite);
        return this;
    }

    // Light-map

    @Nonnull
    public QuadBuilder setLightMap(final LightMap lightMap) {

        this._faceData.setLightMap(lightMap);
        return this;
    }

    @Nonnull
    public QuadBuilder setLightMap(final int vertexIndex, final LightMap lightMap) {

        this._faceData.setLightMap(vertexIndex, lightMap);
        return this;
    }

    private Cuboid.Face _face;
    private final PolygonalFaceData _faceData;

    private static QuadBuilder s_defaultBuilder = null;
}