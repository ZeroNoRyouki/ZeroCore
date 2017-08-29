package it.zerono.mods.zerocore.lib.block;

import it.zerono.mods.zerocore.lib.init.IGameObject;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

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

    /*
    @Override
    public void onPostRegister() {
        ForgeRegistries.ITEMS.register(new ItemBlock(this).setRegistryName(this.getRegistryName()));
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
    */

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
    @SideOnly(Side.CLIENT)
    public void onRegisterModels() {
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    /*
    public static <Tile> Tile getTileEntity(@Nonnull final IBlockAccess world, @Nonnull final BlockPos position) {

        final TileEntity te = world.getTileEntity(position);
        @SuppressWarnings("unchecked")
        final Tile tile = null != te ? (Tile)te : null;

        return tile;
    }*/

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
