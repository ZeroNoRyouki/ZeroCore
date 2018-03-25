package it.zerono.mods.zerocore.lib.client.render.builder;

import it.zerono.mods.zerocore.lib.BlockFacings;
import it.zerono.mods.zerocore.lib.client.render.Shape;
import it.zerono.mods.zerocore.lib.math.Colour;
import it.zerono.mods.zerocore.lib.math.Cuboid;
import it.zerono.mods.zerocore.lib.math.LightMap;
import it.zerono.mods.zerocore.lib.math.UV;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class CuboidBuilder extends AbstractShapeBuilder {

    public final static int VERTICES_COUNT = 8;
    public final static int FACES_COUNT = EnumFacing.VALUES.length;

    @Nonnull
    public static CuboidBuilder getDefaultBuilder() {

        if (null == s_defaultBuilder) {
            s_defaultBuilder = new CuboidBuilder(false);
        }

        return s_defaultBuilder;
    }

    public CuboidBuilder(final boolean autoReset) {

        super(autoReset);
        this._cuboidData = new PolygonalFaceData[FACES_COUNT];
        this._facesToBeRendered = BlockFacings.ALL;

        for (int faceIndex = 0; faceIndex < this._cuboidData.length; ++faceIndex) {
            this._cuboidData[faceIndex] = new PolygonalFaceData(QuadBuilder.VERTICES_COUNT);
        }
    }

    @Nonnull
    @Override
    public Shape build() {

        if (null == this._cuboid) {
            throw new IllegalStateException("No cuboid was provided!");
        }

        final Shape shape = new Shape(VERTICES_COUNT);
        final VertexBuilder vertexBuilder = new VertexBuilder(true);

        for (final EnumFacing facing : EnumFacing.VALUES) {

            if (this._facesToBeRendered.isSet(facing)) {

                final PolygonalFaceData data = this._cuboidData[facing.getIndex()];

                for (int vertexIndex = 0; vertexIndex < data.VERTICES_COUNT; ++vertexIndex) {
                    shape.addVertex(QuadBuilder.buildSingleVertex(vertexIndex, vertexBuilder, this._cuboid.getFace(facing), data));
                }
            }
        }

        if (this.autoReset()) {
            this.reset();
        }

        return shape;
    }

    @Override
    public void reset() {

        this._cuboid = null;
        this._facesToBeRendered = BlockFacings.ALL;

        for (int idx = 0; idx < this._cuboidData.length; ++idx) {
            this._cuboidData[idx].reset();
        }
    }

    // Cuboid to be rendered

    @Nonnull
    public CuboidBuilder setCuboid(@Nonnull final Cuboid cuboid) {

        this._cuboid = cuboid;
        return this;
    }

    // Faces to be rendered

    @Nonnull
    public CuboidBuilder setVisibleFaces(@Nonnull final BlockFacings visibleFaces) {

        this._facesToBeRendered = visibleFaces;
        return this;
    }

    @Nonnull
    public CuboidBuilder setFaceVisibility(@Nonnull final EnumFacing face, final boolean visible) {

        this._facesToBeRendered = this._facesToBeRendered.set(face, visible);
        return this;
    }

    // Color

    /***
     * Set the colour for ALL the faces of the cuboid to be rendered
     * See {@link CuboidBuilder#setVisibleFaces} or {@link CuboidBuilder#setFaceVisibility}
     * @param colour    the color
     * @return the builder
     */
    @Nonnull
    public CuboidBuilder setColour(@Nonnull final Colour colour) {

        for (int idx = 0; idx < this._cuboidData.length; ++idx) {
            this._cuboidData[idx].setColour(colour);
        }

        return this;
    }

    /***
     * Set the colour for the given face of the cuboid to be rendered
     * @param facing    the face
     * @param colour    the color
     * @return the builder
     */
    @Nonnull
    public CuboidBuilder setColour(@Nonnull final EnumFacing facing, @Nonnull final Colour colour) {

        this._cuboidData[facing.getIndex()].setColour(colour);
        return this;
    }

    /***
     * Set the light map for a vertex of the given face of the cuboid to be rendered
     * @param facing        the face
     * @param vertexIndex   the vertex to change
     * @param colour        the color
     * @return the builder
     */
    @Nonnull
    public CuboidBuilder setColour(@Nonnull final EnumFacing facing, final int vertexIndex, @Nonnull final Colour colour) {

        this._cuboidData[facing.getIndex()].setColour(vertexIndex, colour);
        return this;
    }

    // Texture

    /***
     * Set the texture for ALL the faces of the cuboid to be rendered
     * See {@link CuboidBuilder#setVisibleFaces} or {@link CuboidBuilder#setFaceVisibility}
     * @param a the texture map for the first vertex of a face
     * @param b the texture map for the second vertex of a face
     * @param c the texture map for the third vertex of a face
     * @param d the texture map for the forth vertex of a face
     * @return the builder
     */
    @Nonnull
    public CuboidBuilder setTexture(@Nonnull final UV a, @Nonnull final UV b, @Nonnull final UV c, @Nonnull final UV d) {

        for (int faceIdx = 0; faceIdx < this._cuboidData.length; ++faceIdx) {

            final PolygonalFaceData data = this._cuboidData[faceIdx];

            data.setTexture(0, a);
            data.setTexture(1, b);
            data.setTexture(2, c);
            data.setTexture(3, d);
        }

        return this;
    }

    /***
     * Set the texture for the given face of the cuboid to be rendered.
     * See {@link CuboidBuilder#setVisibleFaces} or {@link CuboidBuilder#setFaceVisibility}
     * @param facing    the face
     * @param a         the texture map for the first vertex of a face
     * @param b         the texture map for the second vertex of a face
     * @param c         the texture map for the third vertex of a face
     * @param d         the texture map for the forth vertex of a face
     * @return the builder
     */
    @Nonnull
    public CuboidBuilder setTexture(@Nonnull final EnumFacing facing, @Nonnull final UV a, @Nonnull final UV b,
                                    @Nonnull final UV c, @Nonnull final UV d) {

        final PolygonalFaceData data = this._cuboidData[facing.getIndex()];

        data.setTexture(0, a);
        data.setTexture(1, b);
        data.setTexture(2, c);
        data.setTexture(3, d);
        return this;
    }

    @Nonnull
    public CuboidBuilder setTexture(@Nonnull final EnumFacing facing, final int vertexIndex, @Nonnull final UV uv) {

        final PolygonalFaceData data = this._cuboidData[facing.getIndex()];

        data.setTexture(vertexIndex, uv);
        return this;
    }

    @Nonnull
    public CuboidBuilder setTexture(@Nonnull final TextureAtlasSprite sprite) {

        for (int faceIdx = 0; faceIdx < this._cuboidData.length; ++faceIdx) {
            this._cuboidData[faceIdx].setTexture(sprite);
        }

        return this;
    }

    @Nonnull
    public CuboidBuilder setTexture(@Nonnull final EnumFacing facing, @Nonnull final TextureAtlasSprite sprite) {

        this._cuboidData[facing.getIndex()].setTexture(sprite);
        return this;
    }

    // Light-map

    /***
     * Set the light map for all the face of the cuboid
     * @param lightMap  the light map
     * @return the builder
     */
    @Nonnull
    public CuboidBuilder setLightMap(@Nonnull final LightMap lightMap) {

        for (int idx = 0; idx < this._cuboidData.length; ++idx) {
            this._cuboidData[idx].setLightMap(lightMap);
        }

        return this;
    }

    /***
     * Set the light map for the given face of the cuboid
     * @param facing    the face to change
     * @param lightMap  the light map
     * @return the builder
     */
    @Nonnull
    public CuboidBuilder setLightMap(@Nonnull final EnumFacing facing, @Nonnull final LightMap lightMap) {

        this._cuboidData[facing.getIndex()].setLightMap(lightMap);
        return this;
    }

    /***
     * Set the light map for a vertex of the given face of the cuboid
     * @param facing        the face to change
     * @param vertexIndex   the vertex to change
     * @param lightMap      the light map
     * @return the builder
     */
    @Nonnull
    public CuboidBuilder setLightMap(@Nonnull final EnumFacing facing, final int vertexIndex, @Nonnull final LightMap lightMap) {

        this._cuboidData[facing.getIndex()].setLightMap(vertexIndex, lightMap);
        return this;
    }

    private final PolygonalFaceData[] _cuboidData;
    private Cuboid _cuboid;
    private BlockFacings _facesToBeRendered;

    private static CuboidBuilder s_defaultBuilder = null;
}