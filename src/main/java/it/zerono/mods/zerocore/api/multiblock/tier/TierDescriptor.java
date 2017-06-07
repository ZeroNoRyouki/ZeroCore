package it.zerono.mods.zerocore.api.multiblock.tier;

import it.zerono.mods.zerocore.lib.block.properties.IPropertyValue;
import javax.annotation.Nonnull;

public abstract class TierDescriptor<Tier extends Enum<Tier> & IPropertyValue> {

    public final Tier Tier;
    public final int MaxSizeX;
    public final int MaxSizeY;
    public final int MaxSizeZ;

    protected TierDescriptor(@Nonnull final Tier tier, final int maxX, final int maxY, final int maxZ) {

        this.Tier = tier;
        this.MaxSizeX = maxX;
        this.MaxSizeY = maxY;
        this.MaxSizeZ = maxZ;
    }
}