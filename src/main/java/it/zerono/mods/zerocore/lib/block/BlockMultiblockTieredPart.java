package it.zerono.mods.zerocore.lib.block;

import it.zerono.mods.zerocore.api.multiblock.tier.IMultiblockDescriptorProvider;
import it.zerono.mods.zerocore.api.multiblock.tier.MultiblockDescriptor;
import it.zerono.mods.zerocore.lib.block.properties.IPropertyValue;
import it.zerono.mods.zerocore.lib.item.ItemMultiblockTieredPart;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class BlockMultiblockTieredPart<Tier extends Enum<Tier> & IPropertyValue,
        PartType extends Enum<PartType> & IPropertyValue & IMultiblockDescriptorProvider<Tier>>
        extends BlockMultiblockPart<PartType> implements IMultiblockDescriptorProvider<Tier> {

    public BlockMultiblockTieredPart(PartType type, String blockName, Material material) {

        super(type, blockName, material);
        s_preDescriptorProvider = null;
    }

    public static void preInitDescriptorProvider(@Nonnull final IMultiblockDescriptorProvider provider) {
        s_preDescriptorProvider = provider;
    }

    @Nonnull
    @Override
    public MultiblockDescriptor<Tier> getMultiblockDescriptor() {

        @SuppressWarnings("unchecked")
        final IMultiblockDescriptorProvider<Tier> provider = null != this._partType ?
                this._partType : (IMultiblockDescriptorProvider<Tier>)s_preDescriptorProvider;

        return provider.getMultiblockDescriptor();
    }

    @Override
    public void onPostRegister() {
        GameRegistry.register(new ItemMultiblockTieredPart<>(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onPostClientRegister() {

        Item item = Item.getItemFromBlock(this);
        ResourceLocation location = this.getRegistryName();
        IBlockState defaultState = this.getDefaultState();
        StringBuilder sb = new StringBuilder(32);
        boolean first = true;

        for (IProperty<?> prop : defaultState.getProperties().keySet()) {

            String name = prop.getName();

            if (!first)
                sb.append(',');

            if ("tier".equals(name))
                sb.append("tier=%s");
            else {
                sb.append(name);
                sb.append('=');
                sb.append(defaultState.getValue(prop));
            }

            first = false;
        }

        String mapFormat = sb.toString();
        EnumSet<Tier> activeTiers = this.getMultiblockDescriptor().getActiveTiers();

        for (Tier tier : activeTiers)
            ModelLoader.setCustomModelResourceLocation(item, tier.toMeta(),
                    new ModelResourceLocation(location, String.format(mapFormat, tier.getName())));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(this.getMultiblockDescriptor().getTierProperty()).toMeta();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {

        final MultiblockDescriptor<Tier> descriptor = this.getMultiblockDescriptor();

        return super.getStateFromMeta(meta).withProperty(descriptor.getTierProperty(),
                descriptor.getTierFromMeta(meta));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(this.getMultiblockDescriptor().getTierProperty()).toMeta();
    }

    public ItemStack createItemStack(Tier tier, int amount) {
        return new ItemStack(this, amount, tier.toMeta());
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     *
     * @param item
     * @param tab
     * @param list
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, NonNullList<ItemStack> list) {

        if (null == this._subBlocks) {
            /*
            PartTier[] tiers = PartTier.RELEASED_TIERS;
            int length = tiers.length;

            this._subBlocks = new ArrayList<>(length);

            for (int i = 0; i < length; ++i)
                this._subBlocks.add(new ItemStack(item, 1, tiers[i].toMeta()));
            */
            EnumSet<Tier> tiers = this.getMultiblockDescriptor().getActiveTiers();

            this._subBlocks = new ArrayList<>();

            for (Tier tier : tiers)
                this._subBlocks.add(new ItemStack(item, 1, tier.toMeta()));
        }

        list.addAll(this._subBlocks);
    }

    public Tier getTierFromState(IBlockState state) {
        return state.getValue(this.getMultiblockDescriptor().getTierProperty());
    }

    @Override
    protected void buildBlockState(@Nonnull final BlockStateContainer.Builder builder) {

        super.buildBlockState(builder);
        builder.add(this.getMultiblockDescriptor().getTierProperty());
    }

    @Nonnull
    @Override
    protected IBlockState buildDefaultState(@Nonnull IBlockState state) {

        final MultiblockDescriptor<Tier> descriptor = this.getMultiblockDescriptor();

        return super.buildDefaultState(state).withProperty(descriptor.getTierProperty(),
                descriptor.getDefaultTier());
    }

    private List<ItemStack> _subBlocks;
    private static IMultiblockDescriptorProvider s_preDescriptorProvider;
}
