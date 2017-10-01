package it.zerono.mods.zerocore.lib.compat;

import li.cil.oc.api.machine.Arguments;
import net.minecraftforge.fml.common.Optional;
import javax.annotation.Nonnull;

public final class LuaHelper {

    public static void validateArgsCount(@Nonnull Object[] arguments, int count) throws Exception {

        if (arguments.length < count)
            raiseInvalidArgsCount(count);
    }

    @Optional.Method(modid = MODID_OPENCOMPUTERS)
    public static void validateArgsCount(@Nonnull Arguments arguments, int count) throws Exception {

        if (arguments.count() < count)
            raiseInvalidArgsCount(count);
    }

    public static double getDoubleFromArgs(@Nonnull Object[] arguments, int index) throws Exception {

        if (null == arguments[index] || !(arguments[index] instanceof Double))
            raiseIllegalArgumentType(index, "Number");

        return (Double)arguments[index];
    }

    @Optional.Method(modid = MODID_OPENCOMPUTERS)
    public static double getDoubleFromArgs(@Nonnull Arguments arguments, int index) throws Exception {

        if (!arguments.isDouble(index))
            raiseIllegalArgumentType(index, "Number");

        return arguments.checkDouble(index);
    }

    public static double getDoubleFromArgs(@Nonnull Object[] arguments, int index, double minValue, double maxValue) throws Exception {

        double value = getDoubleFromArgs(arguments, index);

        if (value < minValue || value > maxValue)
            raiseIllegalArgumentRange(index, minValue, maxValue);

        return value;
    }

    @Optional.Method(modid = MODID_OPENCOMPUTERS)
    public static double getDoubleFromArgs(@Nonnull Arguments arguments, int index, double minValue, double maxValue) throws Exception {

        double value = getDoubleFromArgs(arguments, index);

        if (value < minValue || value > maxValue)
            raiseIllegalArgumentRange(index, minValue, maxValue);

        return value;
    }

    public static int getIntFromArgs(@Nonnull Object[] arguments, int index) throws Exception {
        return (int)Math.round(getDoubleFromArgs(arguments, index));
    }

    @Optional.Method(modid = MODID_OPENCOMPUTERS)
    public static int getIntFromArgs(@Nonnull Arguments arguments, int index) throws Exception {
        return (int)Math.round(getDoubleFromArgs(arguments, index)); // keep a consistent implementation with getIntFromArgs(object[], int)
    }

    public static int getIntFromArgs(@Nonnull Object[] arguments, int index, int minValue, int maxValue) throws Exception {

        int value = getIntFromArgs(arguments, index);

        if (value < minValue || value > maxValue)
            raiseIllegalArgumentRange(index, minValue, maxValue);

        return value;
    }

    @Optional.Method(modid = MODID_OPENCOMPUTERS)
    public static int getIntFromArgs(@Nonnull Arguments arguments, int index, int minValue, int maxValue) throws Exception {

        int value = getIntFromArgs(arguments, index);

        if (value < minValue || value > maxValue)
            raiseIllegalArgumentRange(index, minValue, maxValue);

        return value;
    }

    public static boolean getBooleanFromArgs(@Nonnull Object[] arguments, int index) throws Exception {

        if (null == arguments[index] || !(arguments[index] instanceof Boolean))
            raiseIllegalArgumentType(index, "Boolean");

        return (Boolean)arguments[index];
    }

    @Optional.Method(modid = MODID_OPENCOMPUTERS)
    public static boolean getBooleanFromArgs(@Nonnull Arguments arguments, int index) throws Exception {

        if (!arguments.isBoolean(index))
            raiseIllegalArgumentType(index, "Boolean");

        return arguments.checkBoolean(index);
    }

    public static String getStringFromArgs(@Nonnull Object[] arguments, int index) throws Exception {

        if (null == arguments[index] || !(arguments[index] instanceof String))
            raiseIllegalArgumentType(index, "String");

        return (String)arguments[index];
    }

    @Optional.Method(modid = MODID_OPENCOMPUTERS)
    public static String getStringFromArgs(@Nonnull Arguments arguments, int index) throws Exception {

        if (!arguments.isString(index))
            raiseIllegalArgumentType(index, "String");

        return arguments.checkString(index);
    }

    public static void raiseInvalidArgsCount(int expectedCount) {
        throw new IllegalArgumentException(String.format("Insufficient number of arguments, expected %d", expectedCount));
    }

    public static void raiseIllegalArgumentType(int index, String expectedType) {
        throw new IllegalArgumentException(String.format("Invalid argument %d, expected %s", index, expectedType));
    }

    public static void raiseIllegalArgumentRange(int index, double minValue, double maxValue) {
        throw new IllegalArgumentException(String.format("Invalid argument %d, valid range is %f : %f", index, minValue, maxValue));
    }

    public static void raiseIllegalArgumentRange(int index, int minValue, int maxValue) {
        throw new IllegalArgumentException(String.format("Invalid argument %d, valid range is %d : %d", index, minValue, maxValue));
    }

    private LuaHelper() {
    }

    private static final String MODID_OPENCOMPUTERS = "OpenComputers";
}