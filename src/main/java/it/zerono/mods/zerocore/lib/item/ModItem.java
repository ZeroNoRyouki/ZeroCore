package it.zerono.mods.zerocore.lib.item;

import it.zerono.mods.zerocore.lib.init.IGameObject;
import it.zerono.mods.zerocore.util.ItemHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

public class ModItem extends Item implements IGameObject {

    public ModItem(@Nonnull final String itemName) {

        this.setRegistryName(itemName);
        this.setUnlocalizedName(this.getRegistryName().toString());
    }

    @Nonnull
    public ItemStack createItemStack() {
        return ItemHelper.stackFrom(this, 1, 0);
    }

    @Nonnull
    public ItemStack createItemStack(int amount) {
        return ItemHelper.stackFrom(this, amount, 0);
    }

    @Nonnull
    public ItemStack createItemStack(int amount, int meta) {
        return ItemHelper.stackFrom(this, amount, meta);
    }

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
     *
     * @param registry the recipes registry
     */
    @Override
    public void onRegisterRecipes(@Nonnull IForgeRegistry<IRecipe> registry) {
    }

    /**
     * Register all the models for this object
     */
    @Override
    public void onRegisterModels() {
    }

    /**
     * Converts the given ItemStack damage value into a metadata value to be placed in the world when this Item is
     * placed as a Block (mostly used with ItemBlocks).
     */
    @Override
    public int getMetadata(int metadata) {
        return metadata;
    }
}