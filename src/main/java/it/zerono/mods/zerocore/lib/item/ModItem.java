package it.zerono.mods.zerocore.lib.item;

import it.zerono.mods.zerocore.lib.IGameObject;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;

public class ModItem extends Item implements IGameObject {

    public ModItem(@Nonnull final String itemName) {

        this.setRegistryName(itemName);
        this.setUnlocalizedName(this.getRegistryName().toString());
    }

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

}
