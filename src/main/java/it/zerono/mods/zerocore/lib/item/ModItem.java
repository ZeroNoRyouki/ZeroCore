package it.zerono.mods.zerocore.lib.item;

import it.zerono.mods.zerocore.lib.init.IGameObject;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

import javax.annotation.Nonnull;

public class ModItem extends Item implements IGameObject {

    public ModItem(@Nonnull final String itemName) {

        this.setRegistryName(itemName);
        this.setUnlocalizedName(this.getRegistryName().toString());
    }
    /*
    @Override
    public void onPostRegister() {

    }

    @Override
    public void onPostClientRegister() {

    }

    @Override
    public void registerOreDictionaryEntries() {

    }

    @Override
    public void registerRecipes() {

    }
    */

    /**
     * Register all the ItemBlocks associated to this object
     *
     * @param registry the Items registry
     */
    @Override
    public void onRegisterItemBlocks(@Nonnull IForgeRegistry<Item> registry) {
    }

    /**
     * Register any entry for this object the Ore Dictionary
     */
    @Override
    public void onRegisterOreDictionaryEntries() {
    }

    /**
     * Register all the recipes for this object
     */
    @Override
    public void onRegisterRecipes() {
    }

    /**
     * Register all the models for this object
     */
    @Override
    public void onRegisterModels() {
    }
}
