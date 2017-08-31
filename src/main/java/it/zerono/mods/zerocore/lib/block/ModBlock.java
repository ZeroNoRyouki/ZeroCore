package it.zerono.mods.zerocore.lib.block;

import it.zerono.mods.zerocore.lib.init.IGameObject;
import it.zerono.mods.zerocore.util.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import javax.annotation.Nonnull;

public abstract class ModBlock extends Block implements IGameObject {

    @Nonnull
    public ItemStack createItemStack() {
            return ItemHelper.stackFrom(this, 1, 0);
    }

    @Nonnull
    public ItemStack createItemStack(final int amount) {
        return ItemHelper.stackFrom(this, amount, 0);
    }

    @Nonnull
    public ItemStack createItemStack(final int amount, final int meta) {
        return ItemHelper.stackFrom(this, amount, meta);
    }

    /**
     * Register all the ItemBlocks associated to this object
     *
     * @param registry the Items registry
     */
    @Override
    public void onRegisterItemBlocks(@Nonnull IForgeRegistry<Item> registry) {
        registry.register(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    /**
     * Register any entry for this object the Ore Dictionary
     */
    @Override
    public void onRegisterOreDictionaryEntries() {
    }

    /**
     * Register all the recipes for this object
     */
    @Override
    public void onRegisterRecipes() {
    }

    /**
     * Register all the models for this object
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void onRegisterModels() {
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    protected ModBlock(@Nonnull final String blockName, @Nonnull final Material material) {

        super(material);
        this.setRegistryName(blockName);
        this.setUnlocalizedName(this.getRegistryName().toString());
        this.setDefaultState(this.buildDefaultState(this.blockState.getBaseState()));
    }

    @Override
    protected BlockStateContainer createBlockState() {

        final BlockStateContainer.Builder builder = new BlockStateContainer.Builder(this);

        this.buildBlockState(builder);
        return builder.build();
    }

    protected void buildBlockState(@Nonnull final BlockStateContainer.Builder builder) {
    }

    @Nonnull
    protected IBlockState buildDefaultState(@Nonnull IBlockState state) {
        return state;
    }
}
