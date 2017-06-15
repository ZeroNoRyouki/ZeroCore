package it.zerono.mods.zerocore.api.multiblock;

/**
 * Implement on a IMultiblockPart to receive once-per-tick updates from your multiblock controller.
 * You controller should call the onMultiblockServerTick method every tick on each connected parts that
 * implement this interface
 */
public interface ITickableMultiblockPart {

    /**
     * Called once every tick from the multiblock server-side tick loop.
     */
    public void onMultiblockServerTick();
}
