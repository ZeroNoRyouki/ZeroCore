package it.zerono.mods.zerocore.util;

import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class ItemHelper {

    /**
     * Create a stack for the given item
     *
     * @param item the item
     * @param amount the number of items to put into the stack
     * @param meta the metadata associated with the stack
     * @return the newly create stack
     */
    @Nonnull
    public static ItemStack stackFrom(@Nonnull final Item item, final int amount, final int meta) {
        return new ItemStack(item, amount, meta, null);
    }

    /**
     * Create a stack for the given item
     *
     * @param item the item
     * @param amount the number of items to put into the stack
     * @param meta the metadata associated with the stack
     * @param nbt the data to be associated with the stack
     * @return the newly create stack
     */
    @Nonnull
    public static ItemStack stackFrom(@Nonnull final Item item, final int amount, final int meta, @Nullable NBTTagCompound nbt) {
        return new ItemStack(item, amount, meta, nbt);
    }

    /**
     * Create a stack for the item associated to the given block
     *
     * @param block the block
     * @param amount the number of items to put into the stack
     * @param meta the metadata associated with the stack
     * @return the newly create stack
     */
    @Nonnull
    public static ItemStack stackFrom(@Nonnull final Block block, final int amount, final int meta) {
        return new ItemStack(block, amount, meta);
    }

    /**
     * Create a stack for the item associated with the give block state
     *
     * @param state the source block state
     * @param amount the number of items to put into the stack
     * @return a newly create stack containing the specified amount of items or null if no item associated to the block state can be found
     */
    @Nullable
    public static ItemStack stackFrom(@Nonnull final IBlockState state, final int amount) {

        final Block block = state.getBlock();
        final Item item = Item.getItemFromBlock(block);

        return null == item ? null : new ItemStack(item, amount, item.getHasSubtypes() ? block.getMetaFromState(state) : 0);
    }

    /**
     * Create a stack from the given NBT data
     *
     * @param nbt an NBT Tag Compound containing the data of the stack to create
     * @return the newly create stack
     */
    @Nullable
    public static ItemStack stackFrom(@Nonnull final NBTTagCompound nbt) {
        return ItemStack.loadItemStackFromNBT(nbt);
    }

    /**
     * Create a copy of the given stack
     *
     * @param stack the stack to duplicate
     * @return a new stack with the same properties as the one passed in
     */
    @Nullable
    public static ItemStack stackFrom(@Nullable ItemStack stack) {

        if (ItemHelper.stackIsEmpty(stack))
            return ItemHelper.stackEmpty();

        stack = stack.copy();

        if (0 == stack.stackSize)
            stack.stackSize = 1;

        return stack;
    }

    /**
     * Create a copy of the given stack and modify it's size
     *
     * @param stack the stack to duplicate
     * @param amount the new size of the stack
     * @return a new stack with the same properties as the one passed in
     */
    @Nullable
    public static ItemStack stackFrom(@Nullable ItemStack stack, int amount) {

        stack = ItemHelper.stackFrom(stack);

        if (ItemHelper.stackIsEmpty(stack))
            return ItemHelper.stackEmpty();

        ItemHelper.stackSetSize(stack, amount);
        return stack;
    }

    /**
     * Check if the give stack is a valid stack
     *
     * @param stack the stack to query
     * @return MC 1.10.2  : true if the stack is not null and not empty, false otherwise
     *         MC 1.11.2+ : true if the stack is not empty, false otherwise
     */
    public static boolean stackIsValid(@Nullable ItemStack stack) {
        return null != stack && stack.stackSize > 0;
    }

    /**
     * Check if the given stack is empty
     *
     * @param stack the stack to query
     * @return true if the stack is empty, false otherwise
     */
    public static boolean stackIsEmpty(@Nullable ItemStack stack) {
        return null == stack || stack.stackSize <= 0;
    }

    /**
     * Get the number of items inside a stack
     *
     * @param stack the stack to query
     * @return the number of items inside the stack
     */
    public static int stackGetSize(@Nullable ItemStack stack) {
        return ItemHelper.stackIsEmpty(stack) ? 0 : stack.stackSize;
    }

    /**
     * Set the number of items inside a stack
     *
     * @param stack the stack to query
     * @param amount the new size of the stack
     * @return the modified stack or an empty stack
     */
    @Nullable
    public static ItemStack stackSetSize(@Nonnull ItemStack stack, int amount) {

        if (amount <= 0) {

            stack.stackSize = 0;
            return ItemHelper.stackEmpty();
        }

        stack.stackSize = amount;
        return stack;
    }

    /**
     * Modify the number of items inside a stack by the given amount
     *
     * @param stack the stack to modify
     * @param amount the number of items to add or subtract from the stack
     * @return the modified stack or an empty stack
     */
    @Nullable
    public static ItemStack stackAdd(@Nonnull ItemStack stack, int amount) {

        stack.stackSize += amount;
        return stack.stackSize <= 0 ? ItemHelper.stackEmpty() : stack;
    }

    /**
     * Set a stack as empty, removing all items from it
     *
     * @param stack the stack to empty
     * @return the modified empty stack
     */
    @Nonnull
    public static ItemStack stackEmpty(@Nonnull ItemStack stack) {

        stack.stackSize = 0;
        return stack;
    }

    /**
     * Return an empty stack
     * @return an empty stack
     */
    @Nullable
    public static ItemStack stackEmpty() {
        return null;
    }

    public static boolean stackHasData(@Nonnull ItemStack stack, @Nonnull String key) {

        Preconditions.checkNotNull(stack, "'stack' must be non-null");
        Preconditions.checkNotNull(key, "'key' must be non-null");
        Preconditions.checkArgument(!key.isEmpty(), "'key' must not be empty");

        final NBTTagCompound tag = stack.getTagCompound();

        return null != tag && tag.hasKey(key);
    }

    @Nullable
    public static NBTBase stackGetData(@Nonnull ItemStack stack, @Nonnull String key) {
        return ItemHelper.stackHasData(stack, key) ? stack.getTagCompound().getTag(key) : null;
    }

    public static void stackSetData(@Nonnull ItemStack stack, @Nonnull String key, @Nonnull NBTBase value) {

        Preconditions.checkNotNull(stack, "'stack' must be non-null");
        Preconditions.checkNotNull(key, "'key' must be non-null");
        Preconditions.checkArgument(!key.isEmpty(), "'key' must not be empty");
        Preconditions.checkNotNull(value, "'value' must be non-null");

        NBTTagCompound tag = stack.getTagCompound();

        if (null == tag)
            stack.setTagCompound(tag = new NBTTagCompound());

        tag.setTag(key, value);
    }

    private ItemHelper() {
    }
}
