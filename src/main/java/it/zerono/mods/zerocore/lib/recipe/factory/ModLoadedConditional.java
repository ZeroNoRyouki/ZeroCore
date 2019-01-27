package it.zerono.mods.zerocore.lib.recipe.factory;

import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.Loader;

import java.util.function.BooleanSupplier;

public class ModLoadedConditional implements IConditionFactory {

    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject json) {

        final String modId = JsonUtils.getString(json, "modid");

        return () -> Loader.isModLoaded(modId);
    }
}
