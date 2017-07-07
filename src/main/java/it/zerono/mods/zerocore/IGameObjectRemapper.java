package it.zerono.mods.zerocore;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IGameObjectRemapper<T extends IForgeRegistryEntry<T>> {

    /**
     * Add a new object to the remapper
     * @param object the game object to remap
     */
    void add(final T object);

    /**
     * Process a missing mapping
     * The mapping will be sent to all registered IGameObjectRemappers until one of them remap the object
     * @param mapping
     */
    void remap(final RegistryEvent.MissingMappings.Mapping<T> mapping);
}
