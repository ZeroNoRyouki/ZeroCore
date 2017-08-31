package it.zerono.mods.zerocore.lib.config;

import it.zerono.mods.zerocore.util.CodeHelper;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class ConfigHandler implements /*IModInitializationHandler,*/ IConfigListener {

    public ConfigHandler(final String fileName) {
        this(fileName, null, null);
    }

    public ConfigHandler(final String fileName, final String directoryName) {
        this(fileName, directoryName, null);
    }

    public ConfigHandler(final String fileName, final String directoryName, final String languageKeyPrefix) {

        if (null == languageKeyPrefix || languageKeyPrefix.isEmpty())
            this._languageKeyPrefix = "config." + CodeHelper.getModIdFromActiveModContainer() + ".";
        else if (!languageKeyPrefix.endsWith("."))
            this._languageKeyPrefix = languageKeyPrefix + ".";
        else
            this._languageKeyPrefix = languageKeyPrefix;

        this._configFileName = fileName;
        this._configDirectoryName = directoryName;
        this._categories = new ArrayList<>();
        this._listeners = new ArrayList<>();

        final File configDirectory = Loader.instance().getConfigDir();
        final File directory = null != this._configDirectoryName ? new File(configDirectory, this._configDirectoryName) : configDirectory;

        if (!directory.exists() && !directory.mkdir())
            throw new RuntimeException(String.format("Unable to create config directory %s", directory.getName()));

        this._configuration = new Configuration(new File(directory, this._configFileName));

        this.loadConfigurationCategories();
        this.addListener(this);
    }

    public void sync() {

        this.loadConfigurationValues();

        if (this._configuration.hasChanged())
            this._configuration.save();
    }

    public void addListener(IConfigListener listener) {
        this._listeners.add(listener);
    }

    public void notifyListeners() {

        for (IConfigListener listener: this._listeners)
            listener.onConfigChanged();
    }

    public List<IConfigElement> getConfigElements() {

        final List<IConfigElement> elements = new ArrayList<>(this._categories.size());

        for (ConfigCategory category: this._categories)
            elements.add(new ConfigElement(category));

        return elements;
    }

    @Override
    public String toString() {
        return null != this._configuration ? this._configuration.toString() : "configuration";
    }

    protected abstract void loadConfigurationCategories();

    protected abstract void loadConfigurationValues();

    protected ConfigCategory getCategory(final String name) {
        return this.getCategory(name, null);
    }

    protected ConfigCategory getCategory(final String name, final String comment) {

        final ConfigCategory category = this._configuration.getCategory(name);

        if (null != comment)
            category.setComment(comment);

        this.config(category);
        this._categories.add(category);
        return category;
    }

    // Boolean properties and values

    protected Property getProperty(String propertyName, ConfigCategory category, boolean defaultValue, String comment) {
        return this.config(this._configuration.get(category.getName(), propertyName, defaultValue, comment), category);
    }

    protected Property getProperty(String propertyName, ConfigCategory category, boolean[] defaultValue, String comment) {
        return this.config(this._configuration.get(category.getName(), propertyName, defaultValue, comment), category);
    }

    protected boolean getValue(String propertyName, ConfigCategory category, boolean defaultValue, String comment) {
        return this.getProperty(propertyName, category, defaultValue, comment).getBoolean();
    }

    protected boolean[] getValue(String propertyName, ConfigCategory category, boolean[] defaultValue, String comment) {
        return this.getProperty(propertyName, category, defaultValue, comment).getBooleanList();
    }

    // Integer properties and values

    protected Property getProperty(String propertyName, ConfigCategory category, int defaultValue, String comment) {
        return this.config(this._configuration.get(category.getName(), propertyName, defaultValue, comment), category);
    }

    protected Property getProperty(String propertyName, ConfigCategory category, int[] defaultValue, String comment) {
        return this.config(this._configuration.get(category.getName(), propertyName, defaultValue, comment), category);
    }

    protected int getValue(String propertyName, ConfigCategory category, int defaultValue, String comment) {
        return this.getProperty(propertyName, category, defaultValue, comment).getInt();
    }

    protected int[] getValue(String propertyName, ConfigCategory category, int[] defaultValue, String comment) {
        return this.getProperty(propertyName, category, defaultValue, comment).getIntList();
    }

    // Double properties and values

    protected Property getProperty(String propertyName, ConfigCategory category, double defaultValue, String comment) {
        return this.config(this._configuration.get(category.getName(), propertyName, defaultValue, comment), category);
    }

    protected Property getProperty(String propertyName, ConfigCategory category, double[] defaultValue, String comment) {
        return this.config(this._configuration.get(category.getName(), propertyName, defaultValue, comment), category);
    }

    protected double getValue(String propertyName, ConfigCategory category, double defaultValue, String comment) {
        return this.getProperty(propertyName, category, defaultValue, comment).getDouble();
    }

    protected double[] getValue(String propertyName, ConfigCategory category, double[] defaultValue, String comment) {
        return this.getProperty(propertyName, category, defaultValue, comment).getDoubleList();
    }

    protected float getValue(String propertyName, ConfigCategory category, float defaultValue, String comment) {
        return (float)this.getValue(propertyName, category, (double)defaultValue, comment);
    }

    protected float[] getValue(String propertyName, ConfigCategory category, float[] defaultValue, String comment) {

        double[] doubles;
        float[] floats;
        int length, idx;

        length = defaultValue.length;
        doubles = new double[length];

        for (idx = 0; idx < length; ++idx)
            doubles[idx] = defaultValue[idx];

        doubles = this.getValue(propertyName, category, doubles, comment);

        length = doubles.length;
        floats = new float[length];

        for (idx = 0; idx < length; ++idx)
            floats[idx] = (float)doubles[idx];

        return floats;
    }

    // String properties and values

    protected Property getProperty(String propertyName, ConfigCategory category, String defaultValue, String comment) {
        return this.config(this._configuration.get(category.getName(), propertyName, defaultValue, comment), category);
    }

    protected Property getProperty(String propertyName, ConfigCategory category, String[] defaultValue, String comment) {
        return this.config(this._configuration.get(category.getName(), propertyName, defaultValue, comment), category);
    }

    protected String getValue(String propertyName, ConfigCategory category, String defaultValue, String comment) {

        String value = this.getProperty(propertyName, category, defaultValue, comment).getString();

        return null != value ? value : defaultValue;
    }

    protected String[] getValue(String propertyName, ConfigCategory category, String[] defaultValue, String comment) {

        String[] value = this.getProperty(propertyName, category, defaultValue, comment).getStringList();

        return null != value ? value : defaultValue;
    }

    private ConfigCategory config(ConfigCategory category) {
        return category.setLanguageKey(this._languageKeyPrefix + category.getName());
    }

    private Property config(Property property, ConfigCategory category) {
        return property.setLanguageKey(this._languageKeyPrefix + category.getName() + "." + property.getName());
    }

    private final String _languageKeyPrefix;
    private final String _configFileName;
    private final String _configDirectoryName;
    private final List<ConfigCategory> _categories;
    private final List<IConfigListener> _listeners;
    private final Configuration _configuration;
}