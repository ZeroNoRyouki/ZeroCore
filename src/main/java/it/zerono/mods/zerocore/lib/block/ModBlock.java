package it.zerono.mods.zerocore.lib.block;

import it.zerono.mods.zerocore.lib.init.IGameObject;
import it.zerono.mods.zerocore.util.ItemHelper;
import joptsimple.internal.Strings;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

    @Nullable
    public String getOreDictionaryName() {
        return this._oreDictionaryName;
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

        final String name = this.getOreDictionaryName();

        if (!Strings.isNullOrEmpty(name)) {
            OreDictionary.registerOre(name, this);
        }
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
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    protected ModBlock(@Nonnull final String blockName, @Nonnull final Material material,
                       @Nullable final String oreDictionaryName) {

        super(material);
        this._oreDictionaryName = oreDictionaryName;
        this.setRegistryName(blockName);
        this.setUnlocalizedName(this.getRegistryName().toString());
        this.setDefaultState(this.buildDefaultState(this.blockState.getBaseState()));
    }

    protected ModBlock(@Nonnull final String blockName, @Nonnull final Material material) {
        this(blockName, material, null);
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

    private final String _oreDictionaryName;
}
