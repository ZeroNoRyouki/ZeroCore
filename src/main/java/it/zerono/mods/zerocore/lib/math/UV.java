package it.zerono.mods.zerocore.lib.math;

import javax.annotation.Nonnull;

public class UV {

    public double U;
    public double V;

    public UV(final double u, final double v) {
        this.set(u, v);
    }

    public UV(@Nonnull final UV other) {
        this.set(other);
    }

    @Nonnull
    public UV set(final double u, final double v) {

        this.U = u;
        this.V = v;
        return this;
    }

    @Nonnull
    public UV set(@Nonnull final UV other) {
        return set(other.U, other.V);
    }

    @Nonnull
    public UV multiply(final double factor) {

        this.U *= factor;
        this.V *= factor;
        return this;
    }

    @Override
    public boolean equals(Object other) {

        if (other instanceof UV) {

            UV uv = (UV)other;

            return this.U == uv.U && this.V == uv.V;
        }

        return false;
    }

    @Override
    public String toString() {
        return String.format("UV (%f, %f)", this.U, this.V);
    }

    private UV() {
    }
}