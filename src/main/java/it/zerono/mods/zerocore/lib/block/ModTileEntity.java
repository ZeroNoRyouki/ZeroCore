package it.zerono.mods.zerocore.lib.block;

import it.zerono.mods.zerocore.util.WorldHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * A base class for modded tile entities
 *
 * Partially ported from TileCoFHBase
 * https://github.com/CoFH/CoFHCore/blob/master/src/main/java/cofh/core/block/TileCoFHBase.java
 */
public abstract class ModTileEntity extends TileEntity {

    public boolean isUseableByPlayer(EntityPlayer entityplayer) {

        BlockPos position = this.getPos();

        if (this.getWorld().getTileEntity(position) != this)
            return false;

        return entityplayer.getDistanceSq((double)position.getX() + 0.5D, (double)position.getY() + 0.5D,
                (double)position.getZ() + 0.5D) <= 64D;
    }

    /*
    GUI management
     */

    /**
     * Check if the tile entity has a GUI or not
     * Override in derived classes to return true if your tile entity got a GUI
     */
    public boolean canOpenGui(World world, BlockPos posistion, IBlockState state) {
        return false;
    }

    /**
     * Open the specified GUI
     *
     * @param player the player currently interacting with your block/tile entity
     * @param guiId the GUI to open
     * @return true if the GUI was opened, false otherwise
     */
    public boolean openGui(Object mod, EntityPlayer player, int guiId) {

        player.openGui(mod, guiId, this.getWorld(), this.pos.getX(), this.pos.getY(), this.pos.getZ());
        return true;
    }

    /**
     * Returns a Server side Container to be displayed to the user.
     *
     * @param guiId the GUI ID mumber
     * @param player the player currently interacting with your block/tile entity
     * @return A GuiScreen/Container to be displayed to the user, null if none.
     */
    public Object getServerGuiElement(int guiId, EntityPlayer player) {
        return null;
    }

    /**
     * Returns a Container to be displayed to the user. On the client side, this
     * needs to return a instance of GuiScreen On the server side, this needs to
     * return a instance of Container
     *
     * @param guiId the GUI ID mumber
     * @param player the player currently interacting with your block/tile entity
     * @return A GuiScreen/Container to be displayed to the user, null if none.
     */
    public Object getClientGuiElement(int guiId, EntityPlayer player) {
        return null;
    }

    /*
    TileEntity synchronization
     */

    public enum SyncReason {
        FullSync,       // full sync from storage
        NetworkUpdate   // update from the other side
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {

        super.readFromNBT(data);
        this.syncDataFrom(data, SyncReason.FullSync);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {

        this.syncDataTo(super.writeToNBT(data), SyncReason.FullSync);
        return data;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound data) {

        super.readFromNBT(data);
        this.syncDataFrom(data, SyncReason.NetworkUpdate);
    }

    @Override
    public NBTTagCompound getUpdateTag() {

        NBTTagCompound data = super.getUpdateTag();

        this.syncDataTo(data, SyncReason.NetworkUpdate);
        return data;
    }

    @Override
    public final void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        this.syncDataFrom(packet.getNbtCompound(), SyncReason.NetworkUpdate);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {

        NBTTagCompound data = new NBTTagCompound();

        this.syncDataTo(data, SyncReason.NetworkUpdate);
        return new SPacketUpdateTileEntity(this.getPos(), 0, data);
    }

    /**
     * Sync tile entity data from the given NBT compound
     * @param data the data
     * @param syncReason the reason why the synchronization is necessary
     */
    protected abstract void syncDataFrom(NBTTagCompound data, SyncReason syncReason);

    /**
     * Sync tile entity data to the given NBT compound
     * @param data the data
     * @param syncReason the reason why the synchronization is necessary
     */
    protected abstract void syncDataTo(NBTTagCompound data, SyncReason syncReason);

    /*
     Chunk and block updates
     */

    /*
    @Override
    public void onChunkUnload() {
        if (!tileEntityInvalid)
            this.invalidate();
    }*/

    public void markChunkDirty() {
        this.getWorld().markChunkDirty(this.getPos(), this);
    }

    public void callNeighborBlockChange() {
        WorldHelper.notifyNeighborsOfStateChange(this.getWorld(), this.getPos(), this.getBlockType());
    }

    @Deprecated // not implemented
    public void callNeighborTileChange() {

        //this.WORLD.func_147453_f(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
    }

    public void notifyBlockUpdate() {
        WorldHelper.notifyBlockUpdate(this.getWorld(), this.getPos(), null, null);
    }

    public void notifyBlockUpdate(IBlockState oldState, IBlockState newState) {
        WorldHelper.notifyBlockUpdate(this.getWorld(), this.getPos(), oldState, newState);
    }

    public void nofityTileEntityUpdate() {

        this.markDirty();
        WorldHelper.notifyBlockUpdate(this.getWorld(), this.getPos(), null, null);
    }
}
