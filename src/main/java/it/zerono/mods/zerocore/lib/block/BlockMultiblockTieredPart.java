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
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class BlockMultiblockTieredPart<Tier extends Enum<Tier> & IPropertyValue,
        PartType extends Enum<PartType> & IMultiblockTieredPartType,
        Descriptor extends MultiblockDescriptor<Tier>>
        extends BlockMultiblockPart<PartType> implements IMultiblockDescriptorProvider {

    public BlockMultiblockTieredPart(@Nonnull final PartType type, @Nonnull final String blockName,
                                     @Nonnull final Material material) {

        super(type, blockName, material);
        s_preDescriptorProvider = null;
    }

    public static void preInitDescriptorProvider(@Nonnull final IMultiblockTieredPartType provider) {
        s_preDescriptorProvider = provider;
    }

    @Nonnull
    @Override
    public Descriptor getMultiblockDescriptor() {

        final IMultiblockTieredPartType provider = null != this._partType ? this._partType : s_preDescriptorProvider;
        @SuppressWarnings("unchecked")
        final Descriptor descriptor = (Descriptor)provider.getMultiblockDescriptor();

        return descriptor;
    }

    /*@Override
    public void onPostRegister() {
        ForgeRegistries.ITEMS.register(new ItemMultiblockTieredPart<>(this).setRegistryName(this.getRegistryName()));
    }*/

    /**
     * Register all the ItemBlocks associated to this object
     *
     * @param registry the Items registry
     */
    @Override
    public void onRegisterItemBlocks(@Nonnull IForgeRegistry<Item> registry) {
        registry.register(new ItemMultiblockTieredPart<>(this).setRegistryName(this.getRegistryName()));
    }

    /**
     * Register all the models for this object
     */
    @Override
    @SideOnly(Side.CLIENT)
    //public void onPostClientRegister() {
    public void onRegisterModels() {

        final Item item = Item.getItemFromBlock(this);
        final ResourceLocation location = this.getRegistryName();
        final IBlockState defaultState = this.getDefaultState();
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

    @Nonnull
    public Tier getTierFromState(@Nonnull final IBlockState state) {
        return state.getValue(this.getMultiblockDescriptor().getTierProperty());
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

    @Nonnull
    public ItemStack createItemStack(@Nonnull final Tier tier, final int amount) {
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

            EnumSet<Tier> tiers = this.getMultiblockDescriptor().getActiveTiers();

            this._subBlocks = new ArrayList<>();

            for (Tier tier : tiers)
                this._subBlocks.add(this.createItemStack(tier, 1));
        }

        list.addAll(this._subBlocks);
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
    private static IMultiblockTieredPartType s_preDescriptorProvider;
}
