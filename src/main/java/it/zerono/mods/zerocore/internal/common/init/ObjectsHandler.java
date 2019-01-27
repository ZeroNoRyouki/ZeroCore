package it.zerono.mods.zerocore.internal.common.init;

import it.zerono.mods.zerocore.internal.common.item.ItemDebugTool;
import it.zerono.mods.zerocore.lib.init.GameObjectsHandler;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

public class ObjectsHandler extends GameObjectsHandler {

    @GameRegistry.ObjectHolder("zerocore:debugtool")
    public static final ItemDebugTool debugTool = null;

    public ObjectsHandler() {
        super(DATA_VERSION);
    }

    /**
     * Create all the items instances for this mod and register them
     * Override in your subclass to create your items instances and register them with the provided registry
     *
     * @param registry the item registry
     */
    @Override
    protected void onRegisterItems(@Nonnull IForgeRegistry<Item> registry) {
        registry.register(new ItemDebugTool("debugtool"));
    }

    private static final int DATA_VERSION = 1;
}
