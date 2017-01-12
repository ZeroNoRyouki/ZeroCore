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
import it.zerono.mods.zerocore.api.multiblock.MultiblockTileEntityBase;
import it.zerono.mods.zerocore.api.multiblock.validation.IMultiblockValidator;
import it.zerono.mods.zerocore.lib.BlockFacings;
import li.cil.oc.api.event.RackMountableRenderEvent;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class RectangularMultiblockTileEntityBase extends MultiblockTileEntityBase {

	private PartPosition position;
	private BlockFacings outwardFacings;
	
	public RectangularMultiblockTileEntityBase() {
		
		position = PartPosition.Unknown;
		outwardFacings = BlockFacings.NONE;
	}

	// Positional Data

	/**
	 * Get the outward facing of the part in the formed multiblock
	 *
	 * @return the outward facing of the part. A face is "set" in the BlockFacings object if that face is facing outward
	 */
	@Nonnull
	public BlockFacings getOutwardsDir() {

		return outwardFacings;
	}

	/**
	 * Get the position of the part in the formed multiblock
	 *
	 * @return the position of the part
	 */
	@Nonnull
	public PartPosition getPartPosition() {

		return position;
	}

	/**
	 * Return the single direction this part is facing if the part is in one side of the multiblock
	 *
	 * @return the direction toward with the part is facing or null if the part is not in one side of the multiblock
	 */
	@Nullable
	public EnumFacing getOutwardFacing() {

		EnumFacing facing = null != this.position ? this.position.getFacing() : null;

		if (null == facing) {

			BlockFacings out = this.getOutwardsDir();

			if (!out.none() && 1 == out.countFacesIf(true))
				facing = out.firstIf(true);
		}

		return facing;
	}

	/**
	 * Return the single direction this part is facing based on it's position in the multiblock
	 *
	 * @return the direction toward with the part is facing or null if the part is not in one side of the multiblock
	 */
	@Nullable
	public EnumFacing getOutwardFacingFromWorldPosition() {

		BlockFacings facings = null;
		MultiblockControllerBase controller = this.getMultiblockController();

		if (null != controller) {

			BlockPos position = this.getWorldPosition();
			BlockPos min = controller.getMinimumCoord();
			BlockPos max = controller.getMaximumCoord();
			int x = position.getX(), y = position.getY(), z = position.getZ();

			facings = BlockFacings.from(min.getY() == y, max.getY() == y,
										min.getZ() == z, max.getZ() == z,
										min.getX() == x, max.getX() == x);
		}

		return null != facings && !facings.none() && 1 == facings.countFacesIf(true) ? facings.firstIf(true) : null;
	}

	/*
	Tell all blocks in our outward-facing-set that our blockstate is changed
	*/
	public void notifyOutwardNeighborsOfStateChange() {

		final Block blockType = this.getBlockType();
		final BlockFacings facings = this.getOutwardsDir();
		final BlockPos position = this.getWorldPosition();

		for (EnumFacing facing: EnumFacing.VALUES)
			if (facings.isSet(facing))
				this.worldObj.notifyBlockOfStateChange(position.offset(facing), blockType);
	}


	// Handlers from MultiblockTileEntityBase

	@Override
	public void onAttached(MultiblockControllerBase newController) {
		super.onAttached(newController);
		recalculateOutwardsDirection(newController.getMinimumCoord(), newController.getMaximumCoord());
	}

	/*
	@Override
	public void onMachineAssembled(MultiblockControllerBase controller) {

		// Discover where I am on the reactor
		recalculateOutwardsDirection(controller.getMinimumCoord(), controller.getMaximumCoord());
	}
	*/
	@Deprecated
	@Override
	public void onMachineAssembled(MultiblockControllerBase controller) {
	}
	@Override
	public void onPreMachineAssembled(MultiblockControllerBase controller) {
		// Discover where I am on the reactor
		this.recalculateOutwardsDirection(controller.getMinimumCoord(), controller.getMaximumCoord());
	}

	@Override
	public void onPostMachineAssembled(MultiblockControllerBase controller) {
	}
	/*
	@Override
	public void onMachineBroken() {
		position = PartPosition.Unknown;
		outwardFacings = BlockFacings.NONE;
	}*/
	@Deprecated
	@Override
	public void onMachineBroken() {
	}

	@Override
	public void onPreMachineBroken() {
	}

	@Override
	public void onPostMachineBroken() {
		position = PartPosition.Unknown;
		outwardFacings = BlockFacings.NONE;
	}
	
	// Positional helpers

	public void recalculateOutwardsDirection(BlockPos minCoord, BlockPos maxCoord) {
		BlockPos myPosition = this.getPos();
		int myX = myPosition.getX();
		int myY = myPosition.getY();
		int myZ = myPosition.getZ();
		int facesMatching = 0;


		// witch direction are we facing?

		boolean downFacing = myY == minCoord.getY();
		boolean upFacing = myY == maxCoord.getY();
		boolean northFacing = myZ == minCoord.getZ();
		boolean southFacing = myZ == maxCoord.getZ();
		boolean westFacing = myX == minCoord.getX();
		boolean eastFacing = myX == maxCoord.getX();

		this.outwardFacings = BlockFacings.from(downFacing, upFacing, northFacing, southFacing, westFacing, eastFacing);


		// how many faces are facing outward?

		if (eastFacing || westFacing)
			++facesMatching;

		if (upFacing || downFacing)
			++facesMatching;

		if (southFacing || northFacing)
			++facesMatching;


		// what is our position in the multiblock structure?

		if (facesMatching <= 0)
			this.position = PartPosition.Interior;

		else if (facesMatching >= 3)
			this.position = PartPosition.FrameCorner;

		else if (facesMatching == 2) {

			if (!eastFacing && !westFacing)
				this.position = PartPosition.FrameEastWest;
			else if (!southFacing && !northFacing)
				this.position = PartPosition.FrameSouthNorth;
			else
				this.position = PartPosition.FrameUpDown;

		} else {

			// only 1 face matches

			if (eastFacing) {

				this.position = PartPosition.EastFace;

			} else if (westFacing) {

				this.position = PartPosition.WestFace;

			} else if (southFacing) {

				this.position = PartPosition.SouthFace;

			} else if (northFacing) {

				this.position = PartPosition.NorthFace;

			} else if (upFacing) {

				this.position = PartPosition.TopFace;

			} else {

				this.position = PartPosition.BottomFace;
			}
		}
	}
	
	///// Validation Helpers (IMultiblockPart)

	public abstract boolean isGoodForFrame(IMultiblockValidator validatorCallback);

	public abstract boolean isGoodForSides(IMultiblockValidator validatorCallback);

	public abstract boolean isGoodForTop(IMultiblockValidator validatorCallback);

	public abstract boolean isGoodForBottom(IMultiblockValidator validatorCallback);

	public abstract boolean isGoodForInterior(IMultiblockValidator validatorCallback);
}
