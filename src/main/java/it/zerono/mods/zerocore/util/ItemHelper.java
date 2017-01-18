package it.zerono.mods.zerocore.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public final class ItemHelper {

    /**
     * Create a stack for the item associated with the give block state
     *
     * @param state
     * @param amount
     * @return
     */
    @Nonnull
    public static ItemStack stackFrom(@Nonnull IBlockState state, int amount) {

        final Block block = state.getBlock();
        final Item item = Item.getItemFromBlock(block);

        return new ItemStack(item, amount, item.getHasSubtypes() ? block.getMetaFromState(state) : 0);
    }

    /**
     * Create a stack from the given NBT data
     *
     * @param nbt an NBT Tag Compound containing the data of the stack to create
     * @return the newly create stack
     */
    @Nonnull
    public static ItemStack stackFrom(@Nonnull NBTTagCompound nbt) {
        return new ItemStack(nbt);
    }
    /**
     * Create a copy of the given stack
     *
     * @param stack the stack to duplicate
     * @return a new stack with the same properties as the one passed in
     */
    @Nonnull
    public static ItemStack stackFrom(@Nonnull ItemStack stack) {
        return stack.copy();
    }


    /**
     * Check if the give stack is a valid stack
     *
     * @param stack the stack to query
     * @return MC 1.10.2  : true if the stack is non null, false otherwise
     *         MC 1.11.2+ : true if the stack is not empty, false otherwise
     */
    public static boolean stackIsValid(@Nonnull ItemStack stack) {
        return !stack.isEmpty();
    }

    /**
     * Check if the given stack is empty
     *
     * @param stack the stack to query
     * @return true if the stack is empty, false otherwise
     */
    public static boolean stackIsEmpty(@Nonnull ItemStack stack) {
        return stack.isEmpty();
    }

    /**
     * Get the number of items inside a stack
     *
     * @param stack the stack to query
     * @return the number of items inside the stack
     */
    public static int stackGetSize(@Nonnull ItemStack stack) {
        return stack.getCount();
    }

    /**
     * Set the number of items inside a stack
     *
     * @param stack the stack to query
     * @return the modified stack or an empty stack
     */
    @Nonnull
    public static ItemStack stackSetSize(@Nonnull ItemStack stack, int amount) {

        if (amount <= 0) {

            stack.setCount(0);
            return ItemStack.EMPTY;
        }

        stack.setCount(amount);
        return stack;
    }

    /**
     * Modify the number of items inside a stack by the given amount
     *
     * @param stack the stack to modify
     * @param amount the number of items to add or subtract from the stack
     * @return the modified stack or an empty stack
     */
    @Nonnull
    public static ItemStack stackAdd(@Nonnull ItemStack stack, int amount) {

        stack.grow(amount);
        return stack;
    }

    /**
     * Set a stack as empty, removing all items from it
     *
     * @param stack the stack to empty
     * @return the modified empty stack
     */
    @Nonnull
    public static ItemStack stackEmpty(@Nonnull ItemStack stack) {

        stack.setCount(0);
        return stack;
    }

    /**
     * Return an empty stack
     * @return an empty stack
     */
    @Nonnull
    public static ItemStack stackEmpty() {
        return ItemStack.EMPTY;
    }

    private ItemHelper() {
    }
}
