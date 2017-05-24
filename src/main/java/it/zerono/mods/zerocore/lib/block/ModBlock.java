package it.zerono.mods.zerocore.lib.block;

import it.zerono.mods.zerocore.lib.IGameObject;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import javax.annotation.Nonnull;

public abstract class ModBlock extends Block implements IGameObject {

    @Nonnull
    public ItemStack createItemStack() {
        return this.createItemStack(1, 0);
    }

    @Nonnull
    public ItemStack createItemStack(final int amount) {
        return this.createItemStack(amount, 0);
    }

    @Nonnull
    public ItemStack createItemStack(final int amount, final int meta) {
        return new ItemStack(this, amount, meta);
    }

    @Override
    public void onPostRegister() {
        GameRegistry.register(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onPostClientRegister() {
    }

    @Override
    public void registerOreDictionaryEntries() {
    }

    @Override
    public void registerRecipes() {
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
