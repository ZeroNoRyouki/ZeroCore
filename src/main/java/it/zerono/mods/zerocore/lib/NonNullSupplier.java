package it.zerono.mods.zerocore.lib;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * Equivalent to {@link Supplier}, except with nonnull contract.
 *
 * @see Supplier
 */
@FunctionalInterface
public interface NonNullSupplier<T> {

    @Nonnull
    T get();
}
