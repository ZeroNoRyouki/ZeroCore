package it.zerono.mods.zerocore.lib.network;

import it.zerono.mods.zerocore.internal.ZeroCore;
import it.zerono.mods.zerocore.lib.world.WorldHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class ModTileEntityMessageHandler<MessageT extends ModTileEntityMessage> extends ModMessageHandler<MessageT> {

    @Override
    protected void processMessage(final MessageT message, final MessageContext ctx) {

        final World world = this.getWorld(ctx);
        final BlockPos position = message.getPos();

        if (null == world) {

            ZeroCore.getLogger().error("Invalid world instance found while processing a ModTileEntityMessage: skipping message");
            return;
        }

        if (null == position) {

            ZeroCore.getLogger().error("Invalid tile entity position in a ModTileEntityMessage: skipping message");
            return;
        }

        final TileEntity tileEntity = WorldHelper.getTile(world, position);

        if (null != tileEntity) {

            this.processTileEntityMessage(message, ctx, tileEntity);
            return;
        }

        ZeroCore.getLogger().error("Invalid tile entity found at %d, %d, %d while processing a ModTileEntityMessage: skipping message",
                position.getX(), position.getY(), position.getZ());
    }

    protected abstract World getWorld(final MessageContext ctx);

    protected abstract void processTileEntityMessage(final MessageT message, final MessageContext ctx, final TileEntity tileEntity);
}
