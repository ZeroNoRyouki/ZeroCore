package it.zerono.mods.zerocore.lib.client.render.builder;

import it.zerono.mods.zerocore.lib.client.render.Shape;
import it.zerono.mods.zerocore.lib.math.Colour;
import it.zerono.mods.zerocore.lib.math.LightMap;
import it.zerono.mods.zerocore.lib.math.UV;
import it.zerono.mods.zerocore.lib.math.Vector3d;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;

@SideOnly(Side.CLIENT)
public abstract class AbstractShapeBuilder {

    public abstract Shape build();

    public abstract void reset();

    protected AbstractShapeBuilder(final boolean autoReset) {
        this._autoReset = autoReset;
    }

    protected boolean autoReset() {
        return this._autoReset;
    }

    private final boolean _autoReset;


    protected static class PolygonalFaceData {

        public final int VERTICES_COUNT;

        public final Vector3d[] NORMALS;
        public final UV[] UV_MAP;
        public final Colour[] COLOURS;
        public final LightMap[] LIGHT_MAPS;

        public PolygonalFaceData(final int vertexCount) {

            this.VERTICES_COUNT = vertexCount;
            this.NORMALS = new Vector3d[vertexCount];
            this.UV_MAP = new UV[vertexCount];
            this.COLOURS = new Colour[vertexCount];
            this.LIGHT_MAPS = new LightMap[vertexCount];

            this._filledElements = EnumSet.noneOf(VertexElementType.class);
        }

        public void reset() {

            this._filledElements.clear();

            for (int idx = 0; idx < this.VERTICES_COUNT; ++idx) {

                this.NORMALS[idx] = null;
                this.UV_MAP[idx] = null;
                this.COLOURS[idx] = null;
                this.LIGHT_MAPS[idx] = null;
            }
        }

        @Nullable
        public Vector3d getNormalAt(final int vertexIndex) {
            return this.checkElement(VertexElementType.Normal) ? this.NORMALS[vertexIndex] : null;
        }

        @Nullable
        public UV getUvAt(final int vertexIndex) {
            return this.checkElement(VertexElementType.Texture) ? this.UV_MAP[vertexIndex] : null;
        }

        @Nullable
        public Colour getColourlAt(final int vertexIndex) {
            return this.checkElement(VertexElementType.Colour) ? this.COLOURS[vertexIndex] : null;
        }

        @Nullable
        public LightMap getLightMapAt(final int vertexIndex) {
            return this.checkElement(VertexElementType.LightMap) ? this.LIGHT_MAPS[vertexIndex] : null;
        }

        @Nonnull
        public PolygonalFaceData setNormal(@Nonnull final Vector3d normal) {

            for (int vertexIndex = 0; vertexIndex < this.NORMALS.length; ++vertexIndex) {
                this.NORMALS[vertexIndex] = normal;
            }

            this.addElement(VertexElementType.Normal);
            return this;
        }

        @Nonnull
        public PolygonalFaceData setNormal(final int vertexIndex, @Nonnull final Vector3d normal) {

            this.NORMALS[vertexIndex] = normal;
            this.addElement(VertexElementType.Normal);
            return this;
        }

        @Nonnull
        public PolygonalFaceData setTexture(@Nonnull final UV texture) {

            for (int vertexIndex = 0; vertexIndex < this.UV_MAP.length; ++vertexIndex) {
                this.UV_MAP[vertexIndex] = texture;
            }

            this.addElement(VertexElementType.Texture);
            return this;
        }

        @Nonnull
        public PolygonalFaceData setTexture(final int vertexIndex, @Nonnull final UV texture) {

            this.UV_MAP[vertexIndex] = texture;
            this.addElement(VertexElementType.Texture);
            return this;
        }

        @Nonnull
        public PolygonalFaceData setTexture(@Nonnull final TextureAtlasSprite sprite) {

            if (4 != this.VERTICES_COUNT) {
                throw new IllegalArgumentException("This polygonal face does not have 4 vertices");
            }

            final UV a = new UV(sprite.getMinU(), sprite.getMinV());
            final UV b = new UV(sprite.getMinU(), sprite.getMaxV());
            final UV c = new UV(sprite.getMaxU(), sprite.getMaxV());
            final UV d = new UV(sprite.getMaxU(), sprite.getMinV());

            this.setTexture(0, a);
            this.setTexture(1, b);
            this.setTexture(2, c);
            this.setTexture(3, d);

            return this;
        }

        @Nonnull
        public PolygonalFaceData setColour(@Nonnull final Colour colour) {

            for (int vertexIndex = 0; vertexIndex < this.COLOURS.length; ++vertexIndex) {
                this.COLOURS[vertexIndex] = colour;
            }

            this.addElement(VertexElementType.Colour);
            return this;
        }

        @Nonnull
        public PolygonalFaceData setColour(final int vertexIndex, @Nonnull final Colour colour) {

            this.COLOURS[vertexIndex] = colour;
            this.addElement(VertexElementType.Colour);
            return this;
        }

        @Nonnull
        public PolygonalFaceData setLightMap(@Nonnull final LightMap lightMap) {

            for (int vertexIndex = 0; vertexIndex < this.LIGHT_MAPS.length; ++vertexIndex) {
                this.LIGHT_MAPS[vertexIndex] = lightMap;
            }

            this.addElement(VertexElementType.LightMap);
            return this;
        }

        @Nonnull
        public PolygonalFaceData setLightMap(final int vertexIndex, @Nonnull final LightMap lightMap) {

            this.LIGHT_MAPS[vertexIndex] = lightMap;
            this.addElement(VertexElementType.LightMap);
            return this;
        }

        protected boolean checkElement(@Nonnull final VertexElementType element) {
            return this._filledElements.contains(element);
        }

        protected void addElement(@Nonnull final VertexElementType element) {
            this._filledElements.add(element);
        }

        private final EnumSet<VertexElementType> _filledElements;
    }
}