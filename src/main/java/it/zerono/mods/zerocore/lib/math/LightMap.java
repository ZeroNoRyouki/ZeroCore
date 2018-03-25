package it.zerono.mods.zerocore.lib.math;

import javax.annotation.Nonnull;

public class LightMap {

    public int SKY_LIGHT;
    public int BLOCK_LIGHT;

    public LightMap(final int skyLight, final int blockLight) {
        this.set(skyLight, blockLight);
    }

    public LightMap(@Nonnull final LightMap other) {
        this.set(other);
    }

    @Nonnull
    public static LightMap fromCombinedLight(final int combinedLight) {
        return new LightMap(getSkyLightFromCombined(combinedLight), getBlockLightFromCombined(combinedLight));
    }

    public static int getSkyLightFromCombined(final int combinedLight) {
        return combinedLight >> 16 & 0xffff;
    }

    public static int getBlockLightFromCombined(final int combinedLight) {
        return combinedLight & 0xffff;
    }

    public static int getCombinedLight(final int skyLight, final int blockLight) {
        return skyLight << 20 | blockLight << 4;
    }

    @Nonnull
    public LightMap set(final int skyLight, final int blockLight) {

        this.SKY_LIGHT = skyLight;
        this.BLOCK_LIGHT = blockLight;
        return this;
    }

    @Nonnull
    public LightMap set(@Nonnull final LightMap other) {
        return set(other.SKY_LIGHT, other.BLOCK_LIGHT);
    }

    @Nonnull
    public LightMap set(final int combinedLight) {
        return set(getSkyLightFromCombined(combinedLight), getBlockLightFromCombined(combinedLight));
    }

    @Override
    public boolean equals(Object other) {

        if (other instanceof LightMap) {

            LightMap map = (LightMap)other;

            return this.SKY_LIGHT == map.SKY_LIGHT && this.BLOCK_LIGHT == map.BLOCK_LIGHT;
        }

        return false;
    }

    @Override
    public String toString() {
        return String.format("LightMap (0x%08x, 0x%08x)", this.SKY_LIGHT, this.BLOCK_LIGHT);
    }

    private LightMap() {
    }
}