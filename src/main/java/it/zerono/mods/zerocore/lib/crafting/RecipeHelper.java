package it.zerono.mods.zerocore.lib.crafting;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class RecipeHelper {

    // Shaped recipe

    public static void addShapedRecipe(@Nonnull final ItemStack output, @Nonnull final Object... inputs) {
        addShapedRecipe(null, output, inputs);
    }

    public static void addShapedRecipe(@Nullable final ResourceLocation group,
                                       @Nonnull final ItemStack output, @Nonnull final Object... inputs) {

        final ResourceLocation name = output.getItem().getRegistryName();

        if (null == name)
            throw new IllegalArgumentException("Invalid output item registry name");

        addShapedRecipe(name, group, output, inputs);
    }

    public static void addShapedRecipe(@Nonnull final ResourceLocation name, @Nullable final ResourceLocation group,
                                       @Nonnull final ItemStack output, @Nonnull final Object... inputs) {

        final CraftingHelper.ShapedPrimer primer = CraftingHelper.parseShaped(inputs);

        ForgeRegistries.RECIPES.register(new ShapedRecipes(groupName(group), primer.width, primer.height,
                primer.input, output).setRegistryName(name));
    }

    // Shapeless recipe

    public static void addShapelessRecipe(@Nonnull final ItemStack output, @Nonnull final Object... inputs) {
        addShapelessRecipe(null, output, inputs);
    }

    public static void addShapelessRecipe(@Nullable final ResourceLocation group,
                                          @Nonnull final ItemStack output, @Nonnull final Object... inputs) {

        final ResourceLocation name = output.getItem().getRegistryName();

        if (null == name)
            throw new IllegalArgumentException("Invalid output item registry name");

        addShapelessRecipe(name, group, output, inputs);
    }

    public static void addShapelessRecipe(@Nonnull final ResourceLocation name, @Nullable final ResourceLocation group,
                                          @Nonnull final ItemStack output, @Nonnull final Object... inputs) {

        final NonNullList<Ingredient> ingredients = NonNullList.create();

        for (Object input : inputs)
            ingredients.add(asIngredient(input));

        ForgeRegistries.RECIPES.register(new ShapelessRecipes(groupName(group), output, ingredients).setRegistryName(name));
    }

    // Shaped Ore-Dict recipe

    public static void addShapedOreDictRecipe(@Nonnull final ItemStack output, @Nonnull final Object... inputs) {
        addShapedOreDictRecipe(null, output, inputs);
    }

    public static void addShapedOreDictRecipe(@Nullable final ResourceLocation group,
                                              @Nonnull final ItemStack output, @Nonnull final Object... inputs) {

        final ResourceLocation name = output.getItem().getRegistryName();

        if (null == name)
            throw new IllegalArgumentException("Invalid output item registry name");

        addShapedOreDictRecipe(name, group, output, inputs);
    }

    public static void addShapedOreDictRecipe(@Nonnull final ResourceLocation name, @Nullable final ResourceLocation group,
                                              @Nonnull final ItemStack output, @Nonnull final Object... inputs) {

        final CraftingHelper.ShapedPrimer primer = CraftingHelper.parseShaped(inputs);

        ForgeRegistries.RECIPES.register(new ShapedOreRecipe(groupResourceLocation(group), output, primer).setRegistryName(name));
    }

    // Shapeless Ore-Dict recipe

    public static void addShapelessOreDictRecipe(@Nonnull final ItemStack output, @Nonnull final Object... inputs) {
        addShapelessOreDictRecipe(null, output, inputs);
    }

    public static void addShapelessOreDictRecipe(@Nullable final ResourceLocation group,
                                              @Nonnull final ItemStack output, @Nonnull final Object... inputs) {

        final ResourceLocation name = output.getItem().getRegistryName();

        if (null == name)
            throw new IllegalArgumentException("Invalid output item registry name");

        addShapelessOreDictRecipe(name, group, output, inputs);
    }

    public static void addShapelessOreDictRecipe(@Nonnull final ResourceLocation name, @Nullable final ResourceLocation group,
                                                 @Nonnull final ItemStack output, @Nonnull final Object... inputs) {
        ForgeRegistries.RECIPES.register(new ShapelessOreRecipe(groupResourceLocation(group), output, inputs).setRegistryName(name));
    }

    @Nonnull
    private static String groupName(@Nullable final ResourceLocation group) {
        return null == group ? "" : group.toString();
    }

    @Nonnull
    private static ResourceLocation groupResourceLocation(@Nullable final ResourceLocation group) {
        return null == group ? new ResourceLocation("") : group;
    }

    private static Ingredient asIngredient(Object object) {

        if (object instanceof Item)
            return Ingredient.fromItems((Item)object);

        else if (object instanceof Block)
            return Ingredient.fromStacks(new ItemStack((Block)object));

        else if (object instanceof ItemStack)
            return Ingredient.fromStacks((ItemStack)object);

        else if (object instanceof String)
            return new OreIngredient((String) object);

        throw new IllegalArgumentException("Cannot convert object of type " + object.getClass().toString() + " to an Ingredient!");
    }

    /*
    public static void addShapedOreDictRecipe(ItemStack output, Object... inputs) {
        addShapedRecipe(output, inputs);
    }

    public static void addShapelessOreDictRecipe(ItemStack output, Object... inputs) {
        addShapelessRecipe(output, inputs);
    }

    public static void addShapelessRecipe(ItemStack output, Object... inputs) {
        String namespace = getNamespace();
        NonNullList<Ingredient> ingredients = NonNullList.create();

        for(Object input : inputs)
            ingredients.add(asIngredient(input));

        if(ingredients.isEmpty())
            throw new IllegalArgumentException("No ingredients for shapeless recipe");
        else if(ingredients.size() > 9)
            throw new IllegalArgumentException("Too many ingredients for shapeless recipe");

        ShapelessRecipes recipe = new ShapelessRecipes(outputGroup(namespace, output), output, ingredients);
        addRecipe(unusedLocForOutput(namespace, output), recipe);
    }

    public static void addShapedRecipe(ItemStack output, Object... inputs) {
        String namespace = getNamespace();
        ArrayList<String> pattern = Lists.newArrayList();
        Map<String, Ingredient> key = Maps.newHashMap();
        Iterator itr = Arrays.asList(inputs).iterator();

        while(itr.hasNext()) {
            Object obj = itr.next();

            if (obj instanceof String) {
                String str = (String) obj;

                if(str.length() > 3)
                    throw new IllegalArgumentException("Invalid string length for recipe " + str.length());

                if(pattern.size() <= 2)
                    pattern.add(str);
                else
                    throw new IllegalArgumentException("Recipe has too many crafting rows!");
            }
            else if (obj instanceof Character)
                key.put(((Character)obj).toString(), asIngredient(itr.next()));
            else
                throw new IllegalArgumentException("Unexpected argument of type " + obj.getClass().toString());
        }

        int width = pattern.get(0).length();
        int height = pattern.size();

        try {
            key.put(" ", Ingredient.EMPTY);
            Object ingredients = prepareMaterials(pattern.toArray(new String[pattern.size()]), key, width, height);
            ShapedRecipes recipe = new ShapedRecipes(outputGroup(namespace, output), width, height, (NonNullList<Ingredient>) ingredients, output);
            addRecipe(unusedLocForOutput(namespace, output), recipe);

            ForgeRegistries.RECIPES.re

            GameRegistry.addShapedRecipe(ResourceLocation name, ResourceLocation group, @Nonnull ItemStack output, Object... params)

        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    // copy from vanilla
    private static NonNullList<Ingredient> prepareMaterials(String[] p_192402_0_, Map<String, Ingredient> p_192402_1_, int p_192402_2_, int p_192402_3_) {
        NonNullList<Ingredient> nonnulllist = NonNullList.<Ingredient>withSize(p_192402_2_ * p_192402_3_, Ingredient.EMPTY);
        Set<String> set = Sets.newHashSet(p_192402_1_.keySet());
        set.remove(" ");

        for(int i = 0; i < p_192402_0_.length; ++i)
            for (int j = 0; j < p_192402_0_[i].length(); ++j) {
                String s = p_192402_0_[i].substring(j, j + 1);
                Ingredient ingredient = p_192402_1_.get(s);

                set.remove(s);
                nonnulllist.set(j + p_192402_2_ * i, ingredient);
            }

        return nonnulllist;
    }

    public static void addRecipe(ResourceLocation res, IRecipe recipe) {
        if(!(recipe instanceof ModRecipe) && recipe.getRecipeOutput().isEmpty())
            throw new IllegalArgumentException("Illegal recipe output");

        recipe.setRegistryName(res);
        GameRegistry.register(recipe);
    }

    private static Ingredient asIngredient(Object object) {
        if(object instanceof Item)
            return Ingredient.fromItems((Item)object);

        else if(object instanceof Block)
            return Ingredient.fromStacks(new ItemStack((Block)object));

        else if(object instanceof ItemStack)
            return Ingredient.fromStacks((ItemStack)object);

        else if(object instanceof String)
            return new OreIngredient((String) object);

        throw new IllegalArgumentException("Cannot convert object of type " + object.getClass().toString() + " to an Ingredient!");
    }

    private static ResourceLocation unusedLocForOutput(String namespace, ItemStack output) {
        ResourceLocation baseLoc = new ResourceLocation(namespace, output.getItem().getRegistryName().getResourcePath());
        ResourceLocation recipeLoc = baseLoc;
        int index = 0;

        // find unused recipe name
        while (CraftingManager.REGISTRY.containsKey(recipeLoc)) {
            index++;
            recipeLoc = new ResourceLocation(namespace, baseLoc.getResourcePath() + "_" + index);
        }

        return recipeLoc;
    }

    private static String outputGroup(String namespace, ItemStack output) {
        Item item = output.getItem();
        if(item instanceof IRecipeGrouped)
            return namespace + ":" + ((IRecipeGrouped) item).getRecipeGroup();
        if(item instanceof ItemBlock) {
            Block block = ((ItemBlock) item).getBlock();
            if(block instanceof IRecipeGrouped)
                return namespace + ":" + ((IRecipeGrouped) block).getRecipeGroup();
        }

        return output.getItem().getRegistryName().toString();
    }

    private static String getNamespace() {
        return Loader.instance().activeModContainer().getModId();
    }
    */
}