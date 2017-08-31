package it.zerono.mods.zerocore.lib.init;

import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

public interface IGameObject {

    /**
     * Register all the ItemBlocks associated to this object
     * @param registry the Items registry
     */
    void onRegisterItemBlocks(@Nonnull final IForgeRegistry<Item> registry);

    /**
     * Register any entry for this object the Ore Dictionary
     */
    void onRegisterOreDictionaryEntries();

    /**
     * Register all the recipes for this object
     * @param registry the recipes registry
     */
    void onRegisterRecipes(@Nonnull final IForgeRegistry<IRecipe> registry);

    /**
     * Register all the models for this object
     */
    @SideOnly(Side.CLIENT)
    void onRegisterModels();
}
