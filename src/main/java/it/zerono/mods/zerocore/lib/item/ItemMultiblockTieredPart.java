package it.zerono.mods.zerocore.lib.item;

import it.zerono.mods.zerocore.api.multiblock.tier.IMultiblockDescriptorProvider;
import it.zerono.mods.zerocore.lib.block.BlockMultiblockTieredPart;
import it.zerono.mods.zerocore.lib.block.properties.IPropertyValue;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemMultiblockTieredPart<Tier extends Enum<Tier> & IPropertyValue,
        PartType extends Enum<PartType> & IPropertyValue & IMultiblockDescriptorProvider<Tier>> extends ItemBlock {

    public ItemMultiblockTieredPart(BlockMultiblockTieredPart<Tier, PartType> block) {

        super(block);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        @SuppressWarnings("unchecked")
        final BlockMultiblockTieredPart<Tier, PartType> block = (BlockMultiblockTieredPart<Tier, PartType>)this.getBlock();

        return super.getUnlocalizedName() + "." +
                block.getMultiblockDescriptor().getTierFromMeta(stack.getMetadata()).getName();
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }
}