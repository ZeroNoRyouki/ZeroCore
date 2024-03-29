package it.zerono.mods.zerocore.api.multiblock.rectangular;

/*
 * A multiblock library for making irregularly-shaped multiblock machines
 *
 * Original author: Erogenous Beef
 * https://github.com/erogenousbeef/BeefCore
 *
 * Ported to Minecraft 1.9+ and maintained by ZeroNoRyouki
 * https://github.com/ZeroNoRyouki/ZeroCore
 */

import it.zerono.mods.zerocore.api.multiblock.MultiblockControllerBase;
import it.zerono.mods.zerocore.api.multiblock.validation.IMultiblockValidator;
import it.zerono.mods.zerocore.api.multiblock.validation.ValidationError;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class RectangularMultiblockControllerBase extends MultiblockControllerBase {

	protected RectangularMultiblockControllerBase(World world) {
		super(world);
	}

	/**
	 * @return True if the machine is "whole" and should be assembled. False otherwise.
	 */
	@Override
	protected boolean isMachineWhole(IMultiblockValidator validatorCallback) {

		if(connectedParts.size() < getMinimumNumberOfBlocksForAssembledMachine()) {
			validatorCallback.setLastError(ValidationError.VALIDATION_ERROR_TOO_FEW_PARTS);
			return false;
		}
		
		BlockPos maximumCoord = this.getMaximumCoord();
		BlockPos minimumCoord = this.getMinimumCoord();

		int minX = minimumCoord.getX();
		int minY = minimumCoord.getY();
		int minZ = minimumCoord.getZ();
		int maxX = maximumCoord.getX();
		int maxY = maximumCoord.getY();
		int maxZ = maximumCoord.getZ();
		
		// Quickly check for exceeded dimensions
		int deltaX = maxX - minX + 1;
		int deltaY = maxY - minY + 1;
		int deltaZ = maxZ - minZ + 1;
		
		int maxXSize = this.getMaximumXSize();
		int maxYSize = this.getMaximumYSize();
		int maxZSize = this.getMaximumZSize();
		int minXSize = this.getMinimumXSize();
		int minYSize = this.getMinimumYSize();
		int minZSize = this.getMinimumZSize();
		
		if (maxXSize > 0 && deltaX > maxXSize) { validatorCallback.setLastError("zerocore:api.multiblock.validation.machine_too_large", maxXSize, "X"); return false; }
		if (maxYSize > 0 && deltaY > maxYSize) { validatorCallback.setLastError("zerocore:api.multiblock.validation.machine_too_large", maxYSize, "Y"); return false; }
		if (maxZSize > 0 && deltaZ > maxZSize) { validatorCallback.setLastError("zerocore:api.multiblock.validation.machine_too_large", maxZSize, "Z"); return false; }
		if (deltaX < minXSize) { validatorCallback.setLastError("zerocore:zerocore:api.multiblock.validation.machine_too_small", minXSize, "X"); return false; }
		if (deltaY < minYSize) { validatorCallback.setLastError("zerocore:zerocore:api.multiblock.validation.machine_too_small", minYSize, "Y"); return false; }
		if (deltaZ < minZSize) { validatorCallback.setLastError("zerocore:zerocore:api.multiblock.validation.machine_too_small", minZSize, "Z"); return false; }

		// Now we run a simple check on each block within that volume.
		// Any block deviating = NO DEAL SIR
		TileEntity te;
		RectangularMultiblockTileEntityBase part;
		Class<? extends RectangularMultiblockControllerBase> myClass = this.getClass();
		int extremes;
		boolean isPartValid;

		for(int x = minX; x <= maxX; x++) {
			for(int y = minY; y <= maxY; y++) {
				for(int z = minZ; z <= maxZ; z++) {
					// Okay, figure out what sort of block this should be.
					
					te = this.getTile(new BlockPos(x, y, z));
					if(te instanceof RectangularMultiblockTileEntityBase) {
						part = (RectangularMultiblockTileEntityBase)te;
						
						// Ensure this part should actually be allowed within a cube of this controller's type
						if(!myClass.equals(part.getMultiblockControllerType())) {

							validatorCallback.setLastError("zerocore:api.multiblock.validation.invalid_part", x, y, z);
							return false;
						}

						if (!this.doesPartBelong(part)) {

							validatorCallback.setLastError("zerocore:api.multiblock.validation.invalid_foreign_part", x, y, z);
							return false;
						}
					}
					else {
						// This is permitted so that we can incorporate certain non-multiblock parts inside interiors
						part = null;
					}
					
					// Validate block type against both part-level and material-level validators.
					extremes = 0;

					if(x == minX) { extremes++; }
					if(y == minY) { extremes++; }
					if(z == minZ) { extremes++; }
					
					if(x == maxX) { extremes++; }
					if(y == maxY) { extremes++; }
					if(z == maxZ) { extremes++; }

					if(extremes >= 2) {

						isPartValid = part != null ? part.isGoodForFrame(validatorCallback) : this.isBlockGoodForFrame(this.WORLD, x, y, z, validatorCallback);

						if (!isPartValid) {

							if (null == validatorCallback.getLastError())
								validatorCallback.setLastError("zerocore:api.multiblock.validation.invalid_part_for_frame", x, y, z);

							return false;
						}
					}
					else if(extremes == 1) {
						if(y == maxY) {

							isPartValid = part != null ? part.isGoodForTop(validatorCallback) : this.isBlockGoodForTop(this.WORLD, x, y, z, validatorCallback);

							if (!isPartValid) {

								if (null == validatorCallback.getLastError())
									validatorCallback.setLastError("zerocore:api.multiblock.validation.invalid_part_for_top", x, y, z);

								return false;
							}
						}
						else if(y == minY) {

							isPartValid = part != null ? part.isGoodForBottom(validatorCallback) : this.isBlockGoodForBottom(this.WORLD, x, y, z, validatorCallback);

							if (!isPartValid) {

								if (null == validatorCallback.getLastError())
									validatorCallback.setLastError("zerocore:api.multiblock.validation.invalid_part_for_bottom", x, y, z);

								return false;
							}
						}
						else {
							// Side
							isPartValid = part != null ? part.isGoodForSides(validatorCallback) : this.isBlockGoodForSides(this.WORLD, x, y, z, validatorCallback);

							if (!isPartValid) {

								if (null == validatorCallback.getLastError())
									validatorCallback.setLastError("zerocore:api.multiblock.validation.invalid_part_for_sides", x, y, z);

								return false;
							}
						}
					}
					else {

						isPartValid = part != null ? part.isGoodForInterior(validatorCallback) : this.isBlockGoodForInterior(this.WORLD, x, y, z, validatorCallback);

						if (!isPartValid) {

							if (null == validatorCallback.getLastError())
								validatorCallback.setLastError("zerocore:api.multiblock.validation.reactor.invalid_part_for_interior", x, y, z);

							return false;
						}
					}
				}
			}
		}

		return true;
	}

	@Override
	public void forceStructureUpdate(final World world) {

		final BlockPos minCoord = this.getMinimumCoord();
		final BlockPos maxCoord = this.getMaximumCoord();
		final int minX = minCoord.getX();
		final int minY = minCoord.getY();
		final int minZ = minCoord.getZ();
		final int maxX = maxCoord.getX();
		final int maxY = maxCoord.getY();
		final int maxZ = maxCoord.getZ();

		for(int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				for (int z = minZ; z <= maxZ; z++) {

					BlockPos pos = new BlockPos(x, y, z);
					IBlockState state = world.getBlockState(pos);

					world.notifyBlockUpdate(pos, state, state, 3);
				}
			}
		}
	}
}
