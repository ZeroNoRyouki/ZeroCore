package it.zerono.mods.zerocore.lib.client.render;

import it.zerono.mods.zerocore.lib.math.Colour;
import it.zerono.mods.zerocore.lib.math.LightMap;
import it.zerono.mods.zerocore.lib.math.UV;
import it.zerono.mods.zerocore.lib.math.Vector3d;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class Vertex implements IVertexSource {

    public final Vector3d POSITION;
    public final Vector3d NORMAL;
    public final UV UV;
    public final Colour COLOUR;
    public final LightMap LIGHT_MAP;

    public Vertex(@Nonnull final Vector3d position, @Nonnull final Colour colour) {

        this.POSITION = position;
        this.NORMAL = null;
        this.UV = null;
        this.COLOUR = colour;
        this.LIGHT_MAP = null;
    }

    public Vertex(@Nonnull final Vector3d position, @Nonnull final UV uv) {

        this.POSITION = position;
        this.NORMAL = null;
        this.UV = uv;
        this.COLOUR = null;
        this.LIGHT_MAP = null;
    }

    public Vertex(@Nonnull final Vector3d position, @Nullable final Vector3d normal,
                  @Nullable final UV uv, @Nullable final Colour colour, @Nullable final LightMap lightMap) {

        this.POSITION = position;
        this.NORMAL = normal;
        this.UV = uv;
        this.COLOUR = colour;
        this.LIGHT_MAP = lightMap;
    }

    @Override
    public void uploadVertexData(@Nonnull BufferBuilder buffer) {

        for (final VertexFormatElement element : buffer.getVertexFormat().getElements()) {

            switch (element.getUsage()) {

                case COLOR:

                    if (null != this.COLOUR) {
                        buffer.color(this.COLOUR.R, this.COLOUR.G, this.COLOUR.B, this.COLOUR.A);
                    }

                    break;

                case UV:

                    if (0 == element.getIndex()) {

                        // UV
                        if (null != this.UV) {
                            buffer.tex(this.UV.U, this.UV.V);
                        }

                    } else {

                        // light map
                        if (null != this.LIGHT_MAP) {
                            buffer.lightmap(this.LIGHT_MAP.SKY_LIGHT, this.LIGHT_MAP.BLOCK_LIGHT);
                        }
                    }

                    break;

                case NORMAL:

                    if (null != this.NORMAL) {
                        buffer.normal((float)this.NORMAL.X, (float)this.NORMAL.Y, (float)this.NORMAL.Z);
                    }

                    break;

                case POSITION:
                    buffer.pos(this.POSITION.X, this.POSITION.Y, this.POSITION.Z);
                    break;
            }
        }

        buffer.endVertex();
    }
}