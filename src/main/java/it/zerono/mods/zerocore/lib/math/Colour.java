package it.zerono.mods.zerocore.lib.math;

import javax.annotation.Nonnull;

public class Colour {

    public static final Colour WHITE = new Colour(0xff, 0xff, 0xff, 0xff);

    public byte R;
    public byte G;
    public byte B;
    public byte A;

    public Colour(final int r, final int g, final int b, final int a) {
        this.set(r, g, b, a);
    }

    public Colour(final double r, final double g, final double b, final double a) {
        this.set(r, g, b, a);
    }

    public Colour(@Nonnull final Colour other) {
        this.set(other);
    }

    @Nonnull
    public static Colour fromARGB(final int packedARGB) {
        return new Colour((packedARGB >> 16) & 0xFF, (packedARGB >> 8) & 0xFF, packedARGB & 0xFF, (packedARGB >> 24) & 0xFF);
    }

    @Nonnull
    public static Colour fromRGBA(final int packedRGBA) {
        return new Colour((packedRGBA >> 24) & 0xFF, (packedRGBA >> 16) & 0xFF, (packedRGBA >> 8) & 0xFF, packedRGBA & 0xFF);
    }

    public int toRGB() {
        return RGB(this.R, this.G, this.B);
    }

    public int toARGB() {
        return ARGB(this.R, this.G, this.B, this.A);
    }

    public int toRGBA() {
        return RGBA(this.R, this.G, this.B, this.A);
    }

    public static int RGB(final int r, final int g, final int b) {
        return r << 16 | g << 8 | b;
    }

    public static int RGB(final byte r, final byte g, final byte b) {
        return RGB(r & 0xff, g & 0xff, b & 0xff);
    }

    public static int RGB(final double r, final double g, final double b) {
        return RGB((int)(r * 255), (int)(g * 255), (int)(b * 255));
    }

    public static int RGBA(final int r, final int g, final int b, final int a) {
        return r << 24 | g << 16 | b << 8 | a;
    }

    public static int RGBA(final byte r, final byte g, final byte b, final byte a) {
        return RGBA(r & 0xff, g & 0xff, b & 0xff, a & 0xff);
    }

    public static int RGBA(final double r, final double g, final double b, final double a) {
        return RGBA((int)(r * 255), (int)(g * 255), (int)(b * 255), (int)(a * 255));
    }

    public static int ARGB(final int r, final int g, final int b, final int a) {
        return a << 24 | r << 16 | g << 8 | b;
    }

    public static int ARGB(final byte r, final byte g, final byte b, final byte a) {
        return ARGB(a & 0xff, r & 0xff, g & 0xff, b & 0xff);
    }

    public static int ARGB(double r, double g, double b, double a) {
        return ARGB((int)(a * 255), (int)(r * 255), (int)(g * 255), (int)(b * 255));
    }

    @Nonnull
    public Colour set(final int r, final int g, final int b, final int a) {

        this.R = (byte)r;
        this.G = (byte)g;
        this.B = (byte)b;
        this.A = (byte)a;
        return this;
    }

    @Nonnull
    public Colour set(final double r, final double g, final double b, final double a) {
        return this.set((int)(r * 255), (int)(g * 255), (int)(b * 255), (int)(a * 255));
    }

    @Nonnull
    public Colour set(@Nonnull final Colour other) {
        return this.set(other.R, other.G, other.B, other.A);
    }

    @Override
    public boolean equals(Object other) {

        if (other instanceof Colour) {

            Colour c = (Colour)other;

            return this.R == c.R && this.G == c.G && this.B == c.B && this.A == c.A;
        }

        return false;
    }

    @Override
    public String toString() {
        return String.format("Colour R 0x%02x, G 0x%02x, B 0x%02x, A 0x%02x", this.R, this.G, this.B, this.A);
    }

    private Colour() {
    }
}