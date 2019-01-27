package it.zerono.mods.zerocore.lib.init.fixer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataFixer;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface IGameObjectDataWalker {

    @Nonnull
    NBTTagCompound walkObject(@Nonnull final IDataFixer fixer, @Nonnull final NBTTagCompound compound, final int version);
}
