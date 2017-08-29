package it.zerono.mods.zerocore.lib.init;

import com.google.common.collect.ImmutableMap;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

import javax.annotation.Nonnull;

public interface IGameObjectMapper<T extends IForgeRegistryEntry<T>> {

    /**
     * Link the currently registred objects to this remapper
     * This method is called after the objects are registered in the game
     * @param map the registered objects
     */
    void linkObjectsMap(@Nonnull final ImmutableMap<String, T> map);

    /**
     * Process a missing mapping
     * The mapping will be sent to all registered IGameObjectRemappers for the particular type of object until one
     * of them remap the object
     * @param mapping the object to remap
     */
    void remap(@Nonnull final FMLMissingMappingsEvent.MissingMapping mapping);
}
