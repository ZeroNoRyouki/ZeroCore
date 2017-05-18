package it.zerono.mods.zerocore.api.multiblock.tier;

import it.zerono.mods.zerocore.lib.block.properties.IPropertyValue;
import net.minecraft.block.properties.PropertyEnum;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.HashMap;

public abstract class MultiblockDescriptor<Tier extends Enum<Tier> & IPropertyValue> {

    @Nullable
    public TierDescriptor getTierDescriptor(@Nonnull final Tier tier) {
        return _tierData.get(tier);
    }

    @Nonnull
    public EnumSet<Tier> getValidTiers() {
        return this._validTiers;
    }

    @Nonnull
    public EnumSet<Tier> getActiveTiers() {
        return this._activeTiers;
    }

    @Nonnull
    public Tier getTierFromMeta(final int meta) {

        final Tier tier = this._tierMetaMap.get(meta);

        return null != tier ? tier : this._defaultTier;
    }

    @Nonnull
    public Tier getDefaultTier() {
        return this._defaultTier;
    }

    @Nonnull
    public PropertyEnum<Tier> getTierProperty() {

        if (null == this._tierProperty)
            throw new RuntimeException("Tier blockstate property not yet build!");

        return this._tierProperty;
    }

    protected MultiblockDescriptor(@Nonnull final Tier defaultTier, @Nonnull final Class<Tier> tierClass) {

        this._tierClass = tierClass;
        this._validTiers = EnumSet.noneOf(tierClass);
        this._activeTiers = EnumSet.noneOf(tierClass);
        this._tierMetaMap = new HashMap<>();
        this._defaultTier = defaultTier;
        this._tierData = new HashMap<>();
    }

    protected void addTier(final boolean active, @Nonnull final Tier tier) {

        this._validTiers.add(tier);
        this._tierMetaMap.put(tier.toMeta(), tier);

        if (active)
            this._activeTiers.add(tier);
    }

    protected void buildProperties() {
        this._tierProperty = PropertyEnum.create("tier", this._tierClass, this._activeTiers);
    }

    protected void addTierDescriptor(@Nonnull final TierDescriptor<Tier> descriptor) {
        this._tierData.put(descriptor.Tier, descriptor);
    }

    private final Class<Tier> _tierClass;
    private final EnumSet<Tier> _validTiers;
    private final EnumSet<Tier> _activeTiers;
    private final HashMap<Integer, Tier> _tierMetaMap;
    private final Tier _defaultTier;
    private final HashMap<Tier, TierDescriptor> _tierData;
    private PropertyEnum<Tier> _tierProperty;
}
