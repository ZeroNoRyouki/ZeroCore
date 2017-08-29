package it.zerono.mods.zerocore.internal.common.item;

import it.zerono.mods.zerocore.lib.IDebugMessages;
import it.zerono.mods.zerocore.lib.IDebuggable;
import it.zerono.mods.zerocore.lib.init.IGameObject;
import it.zerono.mods.zerocore.lib.crafting.RecipeHelper;
import it.zerono.mods.zerocore.lib.item.ModItem;
import it.zerono.mods.zerocore.util.CodeHelper;
import it.zerono.mods.zerocore.util.OreDictionaryHelper;
import it.zerono.mods.zerocore.lib.world.WorldHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemDebugTool extends ModItem {

    public ItemDebugTool(String itemName) {

        super(itemName);
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.TOOLS);
    }

    /*
    @Override
    public void onPostRegister() {
    }

    @Override
    public void onPostClientRegister() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }

    @Override
    public void registerOreDictionaryEntries() {
    }

    @Override
    public void registerRecipes() {
        RecipeHelper.addShapedRecipe(new ItemStack(this, 1, 0), "IDI", "CGX", "IRI",
                'I', Items.IRON_INGOT, 'D', Items.GLOWSTONE_DUST, 'C', Items.COMPARATOR, 'G', Blocks.GLASS,
                'X', Items.COMPASS, 'R', Items.REDSTONE);
    }
    */

    /**
     * Register all the recipes for this object
     *
     * @param registry the recipes registry
     */
    @Override
    public void onRegisterRecipes(@Nonnull IForgeRegistry<IRecipe> registry) {
        RecipeHelper.addShapedRecipe(new ItemStack(this, 1, 0), "IDI", "CGX", "IRI",
                'I', Items.IRON_INGOT, 'D', Items.GLOWSTONE_DUST, 'C', Items.COMPARATOR, 'G', Blocks.GLASS,
                'X', Items.COMPASS, 'R', Items.REDSTONE);
    }

    /**
     * Register all the models for this object
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void onRegisterModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {

        tooltip.add(I18n.format("zerocore:debugTool.block.tooltip1"));
        tooltip.add("");
        tooltip.add(I18n.format("zerocore:debugTool.block.tooltip2", TextFormatting.ITALIC.toString()));
        tooltip.add(I18n.format("zerocore:debugTool.block.tooltip3", TextFormatting.GREEN,
                TextFormatting.GRAY.toString() + TextFormatting.ITALIC.toString()));
    }

    /**
     * This is called when the item is used, before the block is activated.
     * @param player The Player that used the item
     * @param world The Current World
     * @param pos Target position
     * @param side The side of the target hit
     * @param hand Which hand the item is being held in.
     * @return Return PASS to allow vanilla handling, any other to skip normal code.
     */
    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {

        if (player.isSneaking() != WorldHelper.calledByLogicalClient(world))
            return EnumActionResult.PASS;

        final TileEntity te = WorldHelper.getTile(world, pos);

        if (te instanceof IDebuggable) {

            final IDebuggable debugee = (IDebuggable)te;
            final MessagesPool pool = new MessagesPool();

            debugee.getDebugMessages(pool);

            if (pool.MESSAGES.size() > 0) {

                this.sendMessages(player, new TextComponentTranslation("zerocore:debugTool.tile.header",
                        WorldHelper.getWorldSideName(world), pos.getX(), pos.getY(), pos.getZ()), pool);
                return EnumActionResult.SUCCESS;
            }
        }

        if (!world.isAirBlock(pos)) {

            final IBlockState blockState = world.getBlockState(pos);
            final String[] names = OreDictionaryHelper.getOreNames(blockState);
            final MessagesPool pool = new MessagesPool();
            final ITextComponent header = new TextComponentTranslation("zerocore:debugTool.block.header",
                    WorldHelper.getWorldSideName(world), pos.getX(), pos.getY(), pos.getZ());

            if (null != names && names.length > 0) {

                pool.add("zerocore:debugTool.block.intro", names.length, blockState.getBlock().getUnlocalizedName());
                for (String name : names)
                    pool.add("zerocore:debugTool.block.nameentry", name);

            } else {

                pool.add("zerocore:debugTool.block.notfound");
            }

            this.sendMessages(player, header, pool);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    private void sendMessages(final EntityPlayer player, final ITextComponent header, final MessagesPool pool) {

        CodeHelper.sendChatMessage(player, header);
        for(ITextComponent message : pool.MESSAGES)
            CodeHelper.sendChatMessage(player, message);
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return false;
    }

    private static final class MessagesPool implements IDebugMessages {

        public MessagesPool() {
            this.MESSAGES = new ArrayList<>(2);
        }

        @Override
        public void add(ITextComponent message) {
            this.MESSAGES.add(message);
        }

        @Override
        public void add(String messageFormatStringResourceKey, Object... messageParameters) {
            this.MESSAGES.add(new TextComponentTranslation(messageFormatStringResourceKey, messageParameters));
        }

        final List<ITextComponent> MESSAGES;
    }
}
