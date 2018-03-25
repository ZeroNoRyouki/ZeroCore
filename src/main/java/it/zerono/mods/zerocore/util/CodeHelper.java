package it.zerono.mods.zerocore.util;

import it.zerono.mods.zerocore.internal.ZeroCore;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public final class CodeHelper {

    /**
     * Mouse button constants
     */
    public static final int MOUSE_BUTTON_LEFT = 0;
    public static final int MOUSE_BUTTON_RIGHT = 1;
    public static final int MOUSE_BUTTON_MIDDLE = 2;
    public static final int MOUSE_BUTTON_WHEEL_DOWN = -1;
    public static final int MOUSE_BUTTON_WHEEL_UP = -2;

    /**
     * Retrieve the ID of the mod from FML active mod container
     * Only call this method while processing a FMLEvent (or derived classes)
     */
    public static String getModIdFromActiveModContainer() {

        ModContainer mc = Loader.instance().activeModContainer();
        String modId = null != mc ? mc.getModId() : null;

        if ((null == modId) || modId.isEmpty())
            throw new RuntimeException("Cannot retrieve the MOD ID from FML");

        return modId;
    }

    public static IThreadListener getClientThreadListener() {
        return ZeroCore.getProxy().getClientThreadListener();
    }

    public static IThreadListener getServerThreadListener() {
        return ZeroCore.getProxy().getServerThreadListener();
    }

    /**
     * i18n support and helpers
     */

    public static String i18nValue(boolean value) {
        return I18n.format(value ? "debug.zerocore.true" : "debug.zerocore.false");
    }

    /**
     * MC-Version independent wrapper around EntityPlayer::addChatMessage()
     */
    public static void sendChatMessage(@Nonnull ICommandSender sender, @Nonnull ITextComponent component) {

        if (sender instanceof EntityPlayer)
            ((EntityPlayer)sender).addChatMessage(component);
        else
            sender.addChatMessage(component);
    }

    /**
     * MC-Version independent wrapper around EntityPlayer::sendStatusMessage() [backported to MC 1.10.2]
     */
    public static void sendStatusMessage(@Nonnull final EntityPlayer player, @Nonnull final ITextComponent message) {
        ZeroCore.getProxy().sendPlayerStatusMessage(player, message);
    }

    /**
     * Check if we are currently running in a deobfuscated/development environment
     */
    public static boolean runningInDevEnv() {
        return (Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment");
    }

    /**
     * Load an NBTTagCompound from the given file
     *
     * @param file the file to read from
     * @return the NBTTagCompound read from the file or null if, for whatever reason, the operation fails
     */
    @Nullable
    public static NBTTagCompound nbtLoadFrom(final File file) {

        if (null == file)
            throw new IllegalArgumentException("The file to read from cannot be null");

        if (file.exists()) {

            try (FileInputStream stream = new FileInputStream(file)) {
                return CompressedStreamTools.readCompressed(stream);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Save an NBTTagCompound to the given file
     *
     * @param file the file to write the data to
     * @param data the data to store in the file
     * @return true if the operation succeeded, false otherwise
     */
    @Nullable
    public static boolean nbtSaveTo(final File file, final NBTTagCompound data) {

        if (null == file)
            throw new IllegalArgumentException("The file to write to cannot be null");

        if (null == data)
            throw new IllegalArgumentException("The data to write cannot be null");

        try (FileOutputStream stream = new FileOutputStream(file)) {

            CompressedStreamTools.writeCompressed(data, stream);
            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    /**
     * Math helper function - Linear interpolate between two numbers
     * @param from
     * @param to
     * @param modifier
     * @return
     */
    public static float mathLerp(float from, float to, float modifier) {

        modifier = Math.min(1.0f, Math.max(0.0f, modifier));
        return from + modifier * (to - from);
    }

    /**
     * Math helper function - Calculate the volume of the cube defined by two coordinates
     * @param minimum Minimum coordinate
     * @param maximum Maximum coordinate
     * @return the cube's volume, in blocks
     */
    public static int mathVolume(final BlockPos minimum, final BlockPos maximum) {
        return (minimum == null || maximum == null) ? 0 :
                CodeHelper.mathVolume(minimum.getX(), minimum.getY(), minimum.getZ(),
                        maximum.getX(), maximum.getY(), maximum.getZ());
    }

    /**
     * Math helper function - Calculate the volume of the cube defined by two coordinates.
     * @param x1 minimum X coordinate
     * @param y1 minimum Y coordinate
     * @param z1 minimum Z coordinate
     * @param x2 maximum X coordinate
     * @param y2 maximum Y coordinate
     * @param z2 maximum Z coordinate
     * @return the cube's volume, in blocks
     */
    public static int mathVolume(int x1, int y1, int z1, int x2, int y2, int z2) {

        int cx = Math.abs(x2 - x1) + 1;
        int cy = Math.abs(y2 - y1) + 1;
        int cz = Math.abs(z2 - z1) + 1;

        return cx * cy * cz;
    }

    public static int packBytes(final int byte0, final int byte1, final int byte2, final int byte3) {

        int packed = (byte3 << 8) + byte2;

        packed = (packed << 8) + byte1;
        packed = (packed << 8) + byte0;
        return packed;
    }

    public static int unpackByte(final int packed, final int index) {

        if (index < 0 || index > 3)
            throw new IllegalArgumentException("Index must be 0-3");

        return (byte)((packed >> (index * 8)) & 255);
        /*
        switch (index) {

            case 1:
                return (byte)(packed & 255);

            case 2:
                return (byte)(packed >> 8 & 255);

            case 3:
                return (byte)(packed >> 16 & 255);

            case 4:
                return (byte)(packed >> 24 & 255);
        }

        return -1;*/
    }

    /**
     * Pack color and alpha values in a single int
     * @param red the red component
     * @param green the green component
     * @param blue the blue component
     * @param alpha the alpha component
     * @return the packed value
     */
    public static int argb(final float red, final float green, final float blue, final float alpha) {
        return argb(MathHelper.floor_float(red * 255.0F), MathHelper.floor_float(green * 255.0F),
                MathHelper.floor_float(blue * 255.0F), MathHelper.floor_float(alpha * 255.0F));
    }

    /**
     * Pack color and alpha values in a single int
     * @param red the red component
     * @param green the green component
     * @param blue the blue component
     * @param alpha the alpha component
     * @return the packed value
     */
    public static int argb(final int red, final int green, final int blue, final int alpha) {

        //return CodeHelper.packBytes(blue, green, red, alpha);
        /**/
        int color = (alpha << 8) + red;

        color = (color << 8) + green;
        color = (color << 8) + blue;
        return color;/**/
    }

    /**
     * Get the red component from a packed argb value
     *
     * @param argb  the packed argb value
     * @return the red compoment
     */
    public static int argbRed(final int argb) {
        return argb >> 16 & 255;
        //return CodeHelper.unpackByte(argb, 2);
    }

    /**
     * Get the green component from a packed argb value
     *
     * @param argb  the packed argb value
     * @return the green compoment
     */
    public static int argbGreen(final int argb) {
        return argb >> 8 & 255;
        //return CodeHelper.unpackByte(argb, 1);
    }

    /**
     * Get the blue component from a packed argb value
     *
     * @param argb  the packed argb value
     * @return the blue compoment
     */
    public static int argbBlue(final int argb) {
        return argb & 255;
        //return CodeHelper.unpackByte(argb, 0);
    }

    /**
     * Get the alpha component from a packed argb value
     *
     * @param argb  the packed argb value
     * @return the alpha compoment
     */
    public static int argbAlpha(final int argb) {
        return argb >> 24 & 255;
        //return CodeHelper.unpackByte(argb, 3);
    }


}
