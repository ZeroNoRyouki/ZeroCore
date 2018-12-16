package it.zerono.mods.zerocore.lib.compat.computer;

import it.zerono.mods.zerocore.internal.ZeroCore;
import it.zerono.mods.zerocore.lib.compat.LuaHelper;
import it.zerono.mods.zerocore.util.CodeHelper;

import javax.annotation.Nonnull;

public class ComputerMethod<Peripheral extends ComputerPeripheral> {

    public static final Object[] EMPTY_RESULT = new Object[0];

    public ComputerMethod(@Nonnull final String name, @Nonnull final IComputerMethodHandler<Peripheral> handler) {
        this(name, handler, 0, false);
    }

    public ComputerMethod(@Nonnull final String name, @Nonnull final IComputerMethodHandler<Peripheral> handler,
                          final int minArgumentsCount) {
        this(name, handler, minArgumentsCount, false);
    }

    public ComputerMethod(@Nonnull final String name, @Nonnull final IComputerMethodHandler<Peripheral> handler,
                          final int minArgumentsCount, final boolean runOnServerThread) {

        this._name = name;
        this._handler = handler;
        this._minArgumentsCount = minArgumentsCount;
        this._runOnServerThread = runOnServerThread;
    }

    @Nonnull
    public String getName() {
        return this._name;
    }

    @Nonnull
    public Object[] invoke(@Nonnull final Peripheral peripheral, @Nonnull final Object[] arguments) throws Exception {

        LuaHelper.validateArgsCount(arguments, this._minArgumentsCount);

        if (this._runOnServerThread) {

            final IComputerMethodHandler<Peripheral> handler = this._handler;

            CodeHelper.getServerThreadListener().addScheduledTask(() -> {
                try {
                    handler.execute(peripheral, arguments);
                } catch (Exception ex) {
                    ZeroCore.getLogger().error("Exception raised while running computer method on server thread", ex);
                }
            });

            return EMPTY_RESULT;

        } else {

            return this._handler.execute(peripheral, arguments);
        }
    }

    private final String _name;
    private final IComputerMethodHandler<Peripheral> _handler;
    private final int _minArgumentsCount;
    private final boolean _runOnServerThread;
}