package it.zerono.mods.zerocore.lib.block;

import it.zerono.mods.zerocore.lib.block.properties.IPropertyValue;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IMultiblockPartType extends IPropertyValue {

    @Nullable
    TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state);
}
