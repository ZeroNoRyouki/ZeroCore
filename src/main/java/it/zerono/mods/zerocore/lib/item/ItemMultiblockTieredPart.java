package it.zerono.mods.zerocore.lib.item;

import it.zerono.mods.zerocore.api.multiblock.tier.MultiblockDescriptor;
import it.zerono.mods.zerocore.lib.block.BlockMultiblockTieredPart;
import it.zerono.mods.zerocore.lib.block.IMultiblockTieredPartType;
import it.zerono.mods.zerocore.lib.block.properties.IPropertyValue;
import it.zerono.mods.zerocore.lib.init.IGameObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

public class ItemMultiblockTieredPart<Tier extends Enum<Tier> & IPropertyValue,
        PartType extends Enum<PartType> & IMultiblockTieredPartType,
        Descriptor extends MultiblockDescriptor<Tier>> extends ItemBlock implements IGameObject {

    public ItemMultiblockTieredPart(BlockMultiblockTieredPart<Tier, PartType, Descriptor> block) {

        super(block);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        @SuppressWarnings("unchecked")
        final BlockMultiblockTieredPart<Tier, PartType, Descriptor> block =
                (BlockMultiblockTieredPart<Tier, PartType, Descriptor>)this.getBlock();

        return super.getUnlocalizedName() + "." +
                block.getMultiblockDescriptor().getTierFromMeta(stack.getMetadata()).getName();
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
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
}