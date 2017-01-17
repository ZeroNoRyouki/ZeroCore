package it.zerono.mods.zerocore.internal;

import it.zerono.mods.zerocore.internal.common.CommonProxy;
import it.zerono.mods.zerocore.internal.common.init.ZeroItems;
import it.zerono.mods.zerocore.lib.IModInitializationHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = References.MOD_ID, name = References.MOD_NAME, acceptedMinecraftVersions = "",
        dependencies = "required-after:Forge", version = "0.0.0.0")
public final class ZeroCore implements IModInitializationHandler {

    public static CommonProxy getProxy() {
        return s_proxy;
    }

    @Override
    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        ZeroCore.s_proxy.onPreInit(event);
    }

    @Override
    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        ZeroCore.s_proxy.onInit(event);
    }

    @Override
    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        ZeroCore.s_proxy.onPostInit(event);
    }

    @Mod.EventHandler
    public void onMissingMapping(FMLMissingMappingsEvent event) {

        for (FMLMissingMappingsEvent.MissingMapping mapping : event.get()) {

            String oldName = mapping.resourceLocation.getResourcePath().toLowerCase();

            if (GameRegistry.Type.ITEM == mapping.type && "debugtool".equals(oldName)) {

                mapping.remap(ZeroItems.debugTool);
                return;
            }
        }
    }

    @Mod.Instance
    private static ZeroCore s_instance;

    @SidedProxy(clientSide = References.PROXY_CLIENT, serverSide = References.PROXY_COMMON)
    private static CommonProxy s_proxy;
}
