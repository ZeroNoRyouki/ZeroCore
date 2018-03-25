package it.zerono.mods.zerocore.lib.compat.computer;

import it.zerono.mods.zerocore.lib.block.ModTileEntity;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public abstract class Connector {

    public Connector(@Nonnull final String connectionName, @Nonnull final ComputerPeripheral peripheral) {

        this._connectionName = connectionName;
        this._peripheral = peripheral;
    }

    @Nonnull
    public String getConnectionName() {
        return this._connectionName;
    }

    @Nonnull
    public ComputerPeripheral getPeripheral() {
        return this._peripheral;
    }

    public void onAttachedToController() {
    }

    public void onDetachedFromController() {
    }

    public void syncDataFrom(NBTTagCompound data, ModTileEntity.SyncReason syncReason) {
    }

    public void syncDataTo(NBTTagCompound data, ModTileEntity.SyncReason syncReason) {
    }

    private final String _connectionName;
    private final ComputerPeripheral _peripheral;
}