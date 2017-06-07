package it.zerono.mods.zerocore.api.multiblock.tier;

import it.zerono.mods.zerocore.api.multiblock.MultiblockControllerBase;
import it.zerono.mods.zerocore.lib.block.properties.IPropertyValue;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.EnumSet;
import java.util.HashMap;

public abstract class MultiblockDescriptor<Tier extends Enum<Tier> & IPropertyValue> {

    @Nonnull
    public Class<? extends MultiblockControllerBase> getMultiblockControllerType() {
        return this._controllerClass;
    }

    @Nonnull
    public MultiblockControllerBase createMultiblockController(@Nonnull final World world) {

        try {
            Constructor<?> constructor = this._controllerClass.getConstructor(World.class);

            return (MultiblockControllerBase)constructor.newInstance(world);

        } catch (NoSuchMethodException ex) {

            FMLLog.severe("No suitable constructor the found in Multiblock Controller class %s", this._controllerClass.getName());
            throw new RuntimeException("Multiblock Controller creation failed", ex);

        } catch (InstantiationException ex) {

            FMLLog.severe("Failed to instantiate the Multiblock Controller class %s", this._controllerClass.getName());
            throw new RuntimeException("Multiblock Controller creation failed", ex);

        } catch (IllegalAccessException ex) {

            FMLLog.severe("Unable to access the constructor of the Multiblock Controller class %s", this._controllerClass.getName());
            throw new RuntimeException("Multiblock Controller creation failed", ex);

        } catch (InvocationTargetException ex) {

            FMLLog.severe("Error caught while constructing the Multiblock Controller class %s", this._controllerClass.getName());
            throw new RuntimeException("Multiblock Controller creation failed", ex);
        }
    }

    @Nonnull
    public TierDescriptor<Tier> getTierDescriptor(@Nonnull final Tier tier) {

        final TierDescriptor<Tier> descriptor = this._tierData.get(tier);

        if (null == descriptor)
            throw new RuntimeException(String.format("No descriptor exist for tier %s", tier.getName()));

        return descriptor;
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

    protected MultiblockDescriptor(@Nonnull final Class<? extends MultiblockControllerBase> controllerClass,
                                   @Nonnull final Tier defaultTier, @Nonnull final Class<Tier> tierClass) {

        this._controllerClass = controllerClass;
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

    private final Class<? extends MultiblockControllerBase> _controllerClass;
    private final Class<Tier> _tierClass;
    private final EnumSet<Tier> _validTiers;
    private final EnumSet<Tier> _activeTiers;
    private final HashMap<Integer, Tier> _tierMetaMap;
    private final Tier _defaultTier;
    private final HashMap<Tier, TierDescriptor<Tier>> _tierData;
    private PropertyEnum<Tier> _tierProperty;
}
