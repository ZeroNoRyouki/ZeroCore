package it.zerono.mods.zerocore.internal;

import it.zerono.mods.zerocore.internal.common.CommonProxy;
import it.zerono.mods.zerocore.internal.common.init.ObjectsHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

@Mod(modid = References.MOD_ID, name = References.MOD_NAME, acceptedMinecraftVersions = "",
        dependencies = "required-after:Forge", version = "0.0.0.0")
public final class ZeroCore {

    public static CommonProxy getProxy() {
        return s_proxy;
    }

    public static Logger getLogger() {

        if (null == s_modLogger)
            s_modLogger = LogManager.getLogger(References.MOD_ID);

        return s_modLogger;
    }

    public static ResourceLocation createResourceLocation(@Nonnull final String path) {
        return new ResourceLocation(References.MOD_ID, path);
    }

    public ZeroCore() {
    }

    @Mod.Instance
    private static ZeroCore s_instance;

    @SidedProxy(clientSide = References.PROXY_CLIENT, serverSide = References.PROXY_COMMON)
    private static CommonProxy s_proxy;

    private static Logger s_modLogger;

    private final ObjectsHandler _objectsHandler = new ObjectsHandler();
}