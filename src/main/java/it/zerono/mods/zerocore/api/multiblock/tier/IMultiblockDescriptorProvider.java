package it.zerono.mods.zerocore.api.multiblock.tier;

import it.zerono.mods.zerocore.lib.block.properties.IPropertyValue;

import javax.annotation.Nonnull;

public interface IMultiblockDescriptorProvider<Tier extends Enum<Tier> & IPropertyValue,
        Descriptor extends MultiblockDescriptor<Tier>> {

    @Nonnull
    Descriptor getMultiblockDescriptor();
}
