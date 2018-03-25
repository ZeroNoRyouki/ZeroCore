package it.zerono.mods.zerocore.lib.compat.computer;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import it.zerono.mods.zerocore.lib.compat.ModIDs;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Optional.InterfaceList({
        @Optional.Interface(iface = "dan200.computercraft.api.peripheral.IPeripheral", modid = ModIDs.MODID_COMPUTERCRAFT)
})
public class ConnectorComputerCraft extends Connector implements IPeripheral {

    public static Connector create(@Nonnull final String connectionName, @Nonnull ComputerPeripheral peripheral) {
        return new ConnectorComputerCraft(connectionName, peripheral);
    }

    // CC IPeripheral

    @Nonnull
    @Override
    @Optional.Method(modid = ModIDs.MODID_COMPUTERCRAFT)
    public String[] getMethodNames() {
        return this.getPeripheral().getMethodsNames();
    }

    @Nullable
    @Override
    @Optional.Method(modid = ModIDs.MODID_COMPUTERCRAFT)
    public Object[] callMethod(@Nonnull IComputerAccess computer, @Nonnull ILuaContext luaContext, int methodIdx,
                               @Nonnull Object[] arguments) throws LuaException, InterruptedException {

        final ComputerMethod method = this.getPeripheral().getMethod(methodIdx);

        if (null == method) {
            throw new LuaException("Invalid method");
        }

        try {

            return method.invoke(this.getPeripheral(), arguments);

        } catch (Exception ex) {

            throw new LuaException(ex.toString());
        }
    }

    @Nonnull
    @Override
    @Optional.Method(modid = ModIDs.MODID_COMPUTERCRAFT)
    public String getType() {
        return this.getConnectionName();
    }

    @Override
    @Optional.Method(modid = ModIDs.MODID_COMPUTERCRAFT)
    public void attach(@Nonnull IComputerAccess iComputerAccess) {
    }

    @Override
    @Optional.Method(modid = ModIDs.MODID_COMPUTERCRAFT)
    public void detach(@Nonnull IComputerAccess iComputerAccess) {
    }

    @Override
    @Optional.Method(modid = ModIDs.MODID_COMPUTERCRAFT)
    public boolean equals(@Nullable IPeripheral other) {
        return null != other && this.hashCode() == other.hashCode();
    }

    // Internals

    private ConnectorComputerCraft(@Nonnull final String connectionName, @Nonnull ComputerPeripheral peripheral) {
        super(connectionName, peripheral);
    }
}