package it.zerono.mods.zerocore.lib.block;

import it.zerono.mods.zerocore.api.multiblock.tier.MultiblockDescriptor;

import javax.annotation.Nonnull;

public interface IMultiblockTieredPartType extends IMultiblockPartType {

    @Nonnull
    MultiblockDescriptor getMultiblockDescriptor();
}
