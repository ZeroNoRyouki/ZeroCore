package it.zerono.mods.zerocore.lib.item;

import it.zerono.mods.zerocore.api.multiblock.tier.MultiblockDescriptor;
import it.zerono.mods.zerocore.lib.block.BlockMultiblockTieredPart;
import it.zerono.mods.zerocore.lib.block.IMultiblockTieredPartType;
import it.zerono.mods.zerocore.lib.block.properties.IPropertyValue;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemMultiblockTieredPart<Tier extends Enum<Tier> & IPropertyValue,
        PartType extends Enum<PartType> & IMultiblockTieredPartType,
        Descriptor extends MultiblockDescriptor<Tier>> extends ItemBlock {

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
}