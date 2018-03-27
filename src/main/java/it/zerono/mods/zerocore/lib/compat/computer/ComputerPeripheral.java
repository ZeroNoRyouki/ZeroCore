package it.zerono.mods.zerocore.lib.compat.computer;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.zerono.mods.zerocore.lib.compat.LuaHelper;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public abstract class ComputerPeripheral {

    public ComputerPeripheral(@Nonnull final TileEntity peripheral) {
        this._tile = peripheral;
    }

    @Nonnull
    public TileEntity getTileEntity() {
        return this._tile;
    }

    /**
     * Get the name of this ComputerPeripheral
     *
     * @return the name
     */
    public abstract String getPeripheralStaticName();

    /**
     * Collect the mothods provided by this ComputerPeripheral
     *
     * @param methods add your methods to this List
     */
    public abstract void populatePeripheralMethods(@Nonnull final List<ComputerMethod> methods);

    // Methods

    // Required Args: string (method name)
    public static Object[] help(@Nonnull final MultiblockComputerPeripheral peripheral, @Nonnull final Object[] arguments) throws Exception {
        return new Object[] { "The help() method is not implemented yet" };
    }

    // Required Args: string (method name)
    public static Object[] isMethodAvailable(@Nonnull final MultiblockComputerPeripheral peripheral, @Nonnull final Object[] arguments) throws Exception {
        return new Object[] { null != peripheral.getMethod(LuaHelper.getStringFromArgs(arguments, 0)) };
    }

    // Internals

    @Nonnull
    protected String[] getMethodsNames() {

        if (!s_methodsNames.containsKey(this.getPeripheralStaticName())) {

            // no method maps cached for this Peripheral. build one...
            this.buildMethodsMap();
        }

        return s_methodsNames.getOrDefault(this.getPeripheralStaticName(), new String[0]);
    }

    @Nullable
    protected ComputerMethod getMethod(@Nonnull final String name) {

        if (!s_methodsNamesMap.containsKey(this.getPeripheralStaticName())) {

            // no method maps cached for this Peripheral. build one...
            this.buildMethodsMap();
        }

        final Map<String, ComputerMethod> map = s_methodsNamesMap.get(this.getPeripheralStaticName());

        return map.getOrDefault(name, null);
    }

    @Nullable
    protected ComputerMethod getMethod(@Nonnull final int index) {

        if (!s_methodsNamesMap.containsKey(this.getPeripheralStaticName())) {

            // no method maps cached for this Peripheral. build one...
            this.buildMethodsMap();
        }

        final Map<Integer, ComputerMethod> map = s_methodsIndexMap.get(this.getPeripheralStaticName());

        return map.getOrDefault(index, null);
    }

    private void buildMethodsMap() {

        final String peripheralName = this.getPeripheralStaticName();
        final List<ComputerMethod> methods = Lists.newArrayList();

        // put in standard methods
        methods.add(new ComputerMethod<>("help", ComputerPeripheral::help));
        methods.add(new ComputerMethod<>("isMethodAvailable", ComputerPeripheral::isMethodAvailable));

        // ask the peripheral to add it's own methods
        this.populatePeripheralMethods(methods);

        methods.sort(Comparator.comparing(ComputerMethod::getName));

        final Map<String, ComputerMethod> nameMap = Maps.newHashMapWithExpectedSize(methods.size());
        final Map<Integer, ComputerMethod> indexMap = Maps.newHashMapWithExpectedSize(methods.size());
        final String[] names = new String[methods.size()];
        int index = 0;

        for (ComputerMethod method : methods) {

            if (null == method) {
                continue;
            }

            names[index] = method.getName();
            nameMap.put(names[index], method);
            indexMap.put(index, method);
            ++index;
        }

        s_methodsNamesMap.put(peripheralName, ImmutableMap.copyOf(nameMap));
        s_methodsIndexMap.put(peripheralName, ImmutableMap.copyOf(indexMap));
        s_methodsNames.put(peripheralName, names);
    }

    private final TileEntity _tile;

    private static Map<String, Map<String, ComputerMethod>> s_methodsNamesMap;
    private static Map<String, Map<Integer, ComputerMethod>> s_methodsIndexMap;
    private static Map<String, String[]> s_methodsNames;

    static {
        s_methodsNamesMap = Maps.newHashMap();
        s_methodsIndexMap = Maps.newHashMap();
        s_methodsNames = Maps.newHashMap();
    }
}