package it.zerono.mods.zerocore.lib.compat.computer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import it.zerono.mods.zerocore.lib.block.ModTileEntity;
import it.zerono.mods.zerocore.lib.compat.ModIDs;
import li.cil.oc.api.API;
import li.cil.oc.api.Network;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

@Optional.InterfaceList({
        @Optional.Interface(iface = "li.cil.oc.api.network.ManagedPeripheral", modid = ModIDs.MODID_OPENCOMPUTERS),
        @Optional.Interface(iface = "li.cil.oc.api.network.Environment", modid = ModIDs.MODID_OPENCOMPUTERS)
})
public class ConnectorOpenComputers extends Connector implements ManagedPeripheral, Environment {

    public static Connector create(@Nonnull final String connectionName, @Nonnull ComputerPeripheral peripheral) {
        return new ConnectorOpenComputers(connectionName, peripheral);
    }

    @Override
    public void onAttachedToController() {

        if (null != this._node && this._node.network() == null) {

            API.network.joinOrCreateNetwork(this.getPeripheral().getTileEntity());
            this.getPeripheral().getTileEntity().markDirty();
        }
    }

    @Override
    public void onDetachedFromController() {

        if (null != this._node)
            this._node.remove();
    }

    @Override
    public void syncDataFrom(NBTTagCompound data, ModTileEntity.SyncReason syncReason) {

        super.syncDataFrom(data, syncReason);

        if (null != this.node() && data.hasKey(NODE_TAG))
            this.node().load(data.getCompoundTag(NODE_TAG));
    }

    @Override
    public void syncDataTo(NBTTagCompound data, ModTileEntity.SyncReason syncReason) {

        super.syncDataTo(data, syncReason);

        // let's do this like OC' AbstractManagedEnvironment do ...

        if (this.node() != null) {

            // Force joining a network when saving and we're not in one yet, so that
            // the address is embedded in the saved data that gets sent to the client,
            // so that that address can be used to associate components on server and
            // client (for example keyboard and screen/text buffer).

            if (this.node().address() == null) {

                li.cil.oc.api.Network.joinNewNetwork(this.node());

                final NBTTagCompound nodeTag = new NBTTagCompound();

                this.node().save(nodeTag);
                data.setTag(NODE_TAG, nodeTag);

                this.node().remove();

            } else {

                final NBTTagCompound nodeTag = new NBTTagCompound();

                this.node().save(nodeTag);
                data.setTag(NODE_TAG, nodeTag);
            }
        }
    }

    public static boolean isComputerCapability(Capability<?> capability) {
        return null != ENVIRONMENT_CAPABILITY && ENVIRONMENT_CAPABILITY == capability;
    }

    // OC ManagedPeripheral

    @Override
    @Optional.Method(modid = ModIDs.MODID_OPENCOMPUTERS)
    public String[] methods() {
        return this.getPeripheral().getMethodsNames();
    }

    @Override
    @Optional.Method(modid = ModIDs.MODID_OPENCOMPUTERS)
    public Object[] invoke(String methodName, Context context, Arguments arguments) throws Exception {

        final ComputerMethod method = this.getPeripheral().getMethod(methodName);

        if (null == method) {
            throw new RuntimeException("Invalid method");
        }

        try {

            final Object[] argsCopy = new Object[arguments.count()];
            /*int idx = 0;

            for (final Object arg: arguments) {

                if (arg instanceof Map) {
                    argsCopy[idx] = ImmutableMap.copyOf((Map)arg); //Maps.newHashMap((Map)arg);
                } else if (arg instanceof List) {
                    argsCopy[idx] = ImmutableList.copyOf((List)arg); // Lists.newArrayList((List)arg);
                } else {
                    argsCopy[idx] = arg;
                }

                ++idx;
            }*/

            for (int idx = 0; idx < arguments.count(); ++idx) {

                Object arg = arguments.checkAny(idx);

                if (arg instanceof Map) {
                    argsCopy[idx] = ImmutableMap.copyOf((Map)arg); //Maps.newHashMap((Map)arg);
                } else if (arg instanceof List) {
                    argsCopy[idx] = ImmutableList.copyOf((List)arg); // Lists.newArrayList((List)arg);
                } else {
                    argsCopy[idx] = arg;
                }
            }


            return method.invoke(this.getPeripheral(), argsCopy);

        } catch (Exception ex) {

            throw new RuntimeException(ex);
        }
    }

    // OC Environment

    @Override
    public Node node() {
        return this._node;
    }

    @Override
    public void onConnect(Node node) {
    }

    @Override
    public void onDisconnect(Node node) {
    }

    @Override
    public void onMessage(Message message) {
    }

    // Internals

    private ConnectorOpenComputers(@Nonnull final String connectionName, @Nonnull ComputerPeripheral peripheral) {

        super(connectionName, peripheral);
        this._node = Network.newNode(this, Visibility.Network).withComponent(this.getConnectionName()).create();
    }

    @CapabilityInject(Environment.class)
    private static Capability<Environment> ENVIRONMENT_CAPABILITY = null;
    private static final String NODE_TAG = "ocNode";

    private final Node _node;
}