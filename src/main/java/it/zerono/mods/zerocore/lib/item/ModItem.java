package it.zerono.mods.zerocore.lib.item;

import it.zerono.mods.zerocore.lib.init.IGameObject;
import it.zerono.mods.zerocore.util.ItemHelper;
import joptsimple.internal.Strings;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class ModItem extends Item implements IGameObject {

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

    @Nullable
    public String getOreDictionaryName() {
        return this._oreDictionaryName;
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

        final String name = this.getOreDictionaryName();

        if (!Strings.isNullOrEmpty(name)) {
            OreDictionary.registerOre(name, this);
        }
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
    @SideOnly(Side.CLIENT)
    public void onRegisterModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }

    /**
     * Converts the given ItemStack damage value into a metadata value to be placed in the world when this Item is
     * placed as a Block (mostly used with ItemBlocks).
     */
    @Override
    public int getMetadata(int metadata) {
        return metadata;
    }

    protected ModItem(@Nonnull final String itemName, @Nullable final String oreDictionaryName) {

        this._oreDictionaryName = oreDictionaryName;
        this.setRegistryName(itemName);
        this.setUnlocalizedName(this.getRegistryName().toString());
    }

    protected ModItem(@Nonnull final String itemName) {
        this(itemName, null);
    }

    private final String _oreDictionaryName;
}