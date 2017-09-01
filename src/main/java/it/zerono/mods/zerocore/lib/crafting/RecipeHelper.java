package it.zerono.mods.zerocore.lib.crafting;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;

public final class RecipeHelper {

    // Shaped recipe

    public static void addShapedRecipe(@Nonnull final ItemStack output, @Nonnull final Object... inputs) {
        GameRegistry.addShapedRecipe(output, inputs);
    }

    // Shapeless recipe

    public static void addShapelessRecipe(@Nonnull final ItemStack output, @Nonnull final Object... inputs) {
        GameRegistry.addShapelessRecipe(output, inputs);
    }

    // Shaped Ore-Dict recipe

    public static void addShapedOreDictRecipe(@Nonnull final ItemStack output, @Nonnull final Object... inputs) {
        GameRegistry.addRecipe(new ShapedOreRecipe(output, inputs));
    }

    // Shapeless Ore-Dict recipe

    public static void addShapelessOreDictRecipe(@Nonnull final ItemStack output, @Nonnull final Object... inputs) {
        GameRegistry.addRecipe(new ShapelessOreRecipe(output, inputs));
    }
}