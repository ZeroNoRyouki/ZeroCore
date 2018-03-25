package it.zerono.mods.zerocore.lib.compat.computer;

import it.zerono.mods.zerocore.api.multiblock.MultiblockTileEntityBase;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class MultiblockComputerPeripheral<Part extends MultiblockTileEntityBase> extends ComputerPeripheral {

    public MultiblockComputerPeripheral(@Nonnull final Part part) {
        super(part);
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public Part getMultiblockPart() {
        return (Part) this.getTileEntity();
    }

    /**
     * Collect the mothods provided by this ComputerPeripheral
     *
     * @param methods add your methods to this List
     */
    @Override
    public void populatePeripheralMethods(@Nonnull List<ComputerMethod> methods) {

        methods.add(new ComputerMethod<>("mbIsConnected", (IComputerMethodHandler<MultiblockComputerPeripheral>) MultiblockComputerPeripheral::mbIsConnected));
        methods.add(new ComputerMethod<>("mbIsAssembled", (IComputerMethodHandler<MultiblockComputerPeripheral>) MultiblockComputerPeripheral::mbIsAssembled));
        methods.add(new ComputerMethod<>("mbIsDisassembled", (IComputerMethodHandler<MultiblockComputerPeripheral>) MultiblockComputerPeripheral::mbIsDisassembled));
        methods.add(new ComputerMethod<>("mbIsPaused", (IComputerMethodHandler<MultiblockComputerPeripheral>) MultiblockComputerPeripheral::mbIsPaused));
        methods.add(new ComputerMethod<>("mbGetMultiblockControllerTypeName", (IComputerMethodHandler<MultiblockComputerPeripheral>) MultiblockComputerPeripheral::mbGetMultiblockControllerTypeName));
        methods.add(new ComputerMethod<>("mbGetMinimumCoordinate", (IComputerMethodHandler<MultiblockComputerPeripheral>) MultiblockComputerPeripheral::mbGetMinimumCoordinate));
        methods.add(new ComputerMethod<>("mbGetMaximumCoordinate", (IComputerMethodHandler<MultiblockComputerPeripheral>) MultiblockComputerPeripheral::mbGetMaximumCoordinate));

        // TEMP BACKCOMP !
        methods.add(new ComputerMethod<>("getConnected", (IComputerMethodHandler<MultiblockComputerPeripheral>) MultiblockComputerPeripheral::mbIsConnected));
        methods.add(new ComputerMethod<>("getMultiblockAssembled", (IComputerMethodHandler<MultiblockComputerPeripheral>) MultiblockComputerPeripheral::mbIsAssembled));
        methods.add(new ComputerMethod<>("getMinimumCoordinate", (IComputerMethodHandler<MultiblockComputerPeripheral>) MultiblockComputerPeripheral::mbGetMinimumCoordinate));
        methods.add(new ComputerMethod<>("getMaximumCoordinate", (IComputerMethodHandler<MultiblockComputerPeripheral>) MultiblockComputerPeripheral::mbGetMaximumCoordinate));
    }

    // Methods

    public static Object[] mbIsConnected(@Nonnull final MultiblockComputerPeripheral peripheral, @Nonnull final Object[] arguments) throws Exception {
        return new Object[] { peripheral.getMultiblockPart().isConnected() };
    }

    public static Object[] mbIsAssembled(@Nonnull final MultiblockComputerPeripheral peripheral, @Nonnull final Object[] arguments) throws Exception {
        return new Object[] { peripheral.getMultiblockPart().isMachineAssembled() };
    }

    public static Object[] mbIsDisassembled(@Nonnull final MultiblockComputerPeripheral peripheral, @Nonnull final Object[] arguments) throws Exception {
        return new Object[] { peripheral.getMultiblockPart().isMachineDisassembled() };
    }

    public static Object[] mbIsPaused(@Nonnull final MultiblockComputerPeripheral peripheral, @Nonnull final Object[] arguments) throws Exception {
        return new Object[] { peripheral.getMultiblockPart().isMachinePaused() };
    }

    public static Object[] mbGetMultiblockControllerTypeName(@Nonnull final MultiblockComputerPeripheral peripheral, @Nonnull final Object[] arguments) throws Exception {
        return new Object[] { peripheral.getMultiblockPart().getMultiblockControllerType().getName() };
    }

    public static Object[] mbGetMinimumCoordinate(@Nonnull final MultiblockComputerPeripheral peripheral, @Nonnull final Object[] arguments) throws Exception {

        final BlockPos coords = peripheral.getMultiblockPart().isConnected() ?
                peripheral.getMultiblockPart().getMultiblockController().getMinimumCoord() : null;

        return null != coords ? new Object[]{ coords.getX(), coords.getY(), coords.getZ() } : null;
    }

    public static Object[] mbGetMaximumCoordinate(@Nonnull final MultiblockComputerPeripheral peripheral, @Nonnull final Object[] arguments) throws Exception {

        final BlockPos coords = peripheral.getMultiblockPart().isConnected() ?
                peripheral.getMultiblockPart().getMultiblockController().getMaximumCoord() : null;

        return null != coords ? new Object[]{ coords.getX(), coords.getY(), coords.getZ() } : null;
    }
}