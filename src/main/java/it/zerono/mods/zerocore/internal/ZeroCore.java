package it.zerono.mods.zerocore.internal;

import it.zerono.mods.zerocore.internal.common.CommonProxy;
import it.zerono.mods.zerocore.internal.common.init.ObjectsHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;

@Mod(modid = References.MOD_ID, name = References.MOD_NAME, acceptedMinecraftVersions = "",
        dependencies = "required-after:Forge", version = "0.0.0.0")
public final class ZeroCore {

    public static CommonProxy getProxy() {
        return s_proxy;
    }

    public ZeroCore() {
    }

    @Mod.Instance
    private static ZeroCore s_instance;

    @SidedProxy(clientSide = References.PROXY_CLIENT, serverSide = References.PROXY_COMMON)
    private static CommonProxy s_proxy;

    private final ObjectsHandler _objectsHandler = new ObjectsHandler();
}