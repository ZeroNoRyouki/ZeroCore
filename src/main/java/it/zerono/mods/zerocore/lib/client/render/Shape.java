package it.zerono.mods.zerocore.lib.client.render;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

@SideOnly(Side.CLIENT)
public class Shape implements IVertexSource {

    public Shape() {
        this(4);
    }

    public Shape(final int initialSize) {
        this._vertices = Lists.newArrayListWithCapacity(initialSize);
    }

    public void addVertex(@Nonnull final Vertex vertex) {
        this._vertices.add(vertex);
    }

    @Nonnull
    public ImmutableList<Vertex> getVertices() {
        return ImmutableList.copyOf(this._vertices);
    }

    public int getVerticesCount() {
        return this._vertices.size();
    }

    @Override
    public void uploadVertexData(@Nonnull final VertexBuffer buffer) {

        for (final Vertex vertex : this._vertices) {
            vertex.uploadVertexData(buffer);
        }
    }

    private final List<Vertex> _vertices;
}