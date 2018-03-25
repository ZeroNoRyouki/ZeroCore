package it.zerono.mods.zerocore.lib.compat.computer;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface IComputerMethodHandler<Peripheral extends ComputerPeripheral> {
    Object[] execute(@Nonnull final Peripheral peripheral, @Nonnull final Object[] arguments) throws Exception;
}