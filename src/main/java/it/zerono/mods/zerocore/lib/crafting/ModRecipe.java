package it.zerono.mods.zerocore.lib.crafting;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

public abstract class ModRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe{

    public ModRecipe(ResourceLocation res) {
        RecipeHelper.addRecipe(res, this);
    }

}
