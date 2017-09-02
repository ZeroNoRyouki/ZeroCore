package it.zerono.mods.zerocore.lib.block;

import it.zerono.mods.zerocore.api.multiblock.IMultiblockPart;
import it.zerono.mods.zerocore.api.multiblock.MultiblockControllerBase;
import it.zerono.mods.zerocore.api.multiblock.MultiblockTileEntityBase;
import it.zerono.mods.zerocore.api.multiblock.validation.ValidationError;
import it.zerono.mods.zerocore.lib.world.WorldHelper;
import it.zerono.mods.zerocore.util.CodeHelper;
import it.zerono.mods.zerocore.util.ItemHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockMultiblockPart<PartType extends Enum<PartType> & IMultiblockPartType> extends ModBlock {

    public BlockMultiblockPart(@Nonnull final PartType type, @Nonnull final String blockName,
                               @Nonnull final Material material) {

        super(blockName, material);
        this._partType = type;
    }

    @Nonnull
    public PartType getPartType() {
        return this._partType;
    }

    /**
     * Called throughout the code as a replacement for block instanceof BlockContainer
     * Moving this to the Block base class allows for mods that wish to extend vanilla
     * blocks, and also want to have a tile entity on that block, may.
     * <p>
     * Return true from this function to specify this block has a tile entity.
     *
     * @param state State of the current block
     * @return True if block has a tile entity, false otherwise
     */
    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    /**
     * Called throughout the code as a replacement for ITileEntityProvider.createNewTileEntity
     * Return the same thing you would from that function.
     * This will fall back to ITileEntityProvider.createNewTileEntity(World) if this block is a ITileEntityProvider
     *
     * @param world
     * @param state
     * @return A instance of a class extending TileEntity
     */
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return this.getPartType().createTileEntity(world, state);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos position) {

        TileEntity te = WorldHelper.getTile(world, position);

        if (te instanceof MultiblockTileEntityBase)
            state = this.buildActualState(state, world, position, (MultiblockTileEntityBase)te);

        return state;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos position, EnumFacing side) {

        final IBlockState sideState = blockAccess.getBlockState(position.offset(side));

        return this != sideState.getBlock();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos position, IBlockState state, EntityPlayer player,
                                    EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side,
                                    float hitX, float hitY, float hitZ) {

        if (this.hasTileEntity(state) && !player.isSneaking()) {

            final TileEntity te = WorldHelper.getTile(world, position);

            // If the player's hands are empty and they rightclick on a multiblock, they get a
            // multiblock-debugging message if the machine is not assembled.

            if ((te instanceof IMultiblockPart) && WorldHelper.calledByLogicalServer(world) &&
                    (ItemHelper.stackIsEmpty(heldItem)) && (hand == EnumHand.OFF_HAND)) {

                final MultiblockControllerBase controller = ((IMultiblockPart)te).getMultiblockController();
                ITextComponent message = null;

                if (null != controller) {

                    final ValidationError error = controller.getLastError();

                    if (null != error)
                        message = error.getChatMessage();

                } else {

                    message = new TextComponentTranslation("multiblock.validation.block_not_connected");
                }

                if (null != message) {

                    //CodeHelper.sendChatMessage(player, message);
                    CodeHelper.sendStatusMessage(player, message);
                    return true;
                }
            }
        }

        return super.onBlockActivated(world, position, state, player, hand, heldItem, side, hitX, hitY, hitZ);
    }

    @Nonnull
    protected IBlockState buildActualState(@Nonnull IBlockState state, @Nonnull IBlockAccess world,
                                           @Nonnull BlockPos position, @Nonnull MultiblockTileEntityBase part) {
        return state;
    }

    protected final PartType _partType;
}
