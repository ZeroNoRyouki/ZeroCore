package it.zerono.mods.zerocore.lib.recipe.factory;

import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.OreDictionary;

import java.util.function.BooleanSupplier;

public class OreNameRegisteredConditional implements IConditionFactory {

    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject json) {

        final String oreName = JsonUtils.getString(json, "ore");

        return () -> OreDictionary.doesOreNameExist(oreName);
    }
}
