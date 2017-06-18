package net.doodcraft.oshcon.bukkit.clayfarming.config;

import net.doodcraft.oshcon.bukkit.clayfarming.ClayFarmingPlugin;
import net.doodcraft.oshcon.bukkit.clayfarming.StaticMethods;

import java.io.File;

public class Settings {

    public static Boolean colorfulLogging;
    public static Boolean debug;
    public static int minimumTime;
    public static int maximumTime;
    public static Boolean particles;
    public static String transformFromMaterial;
    public static String transformToMaterial;

    public static String pluginPrefix;
    public static String noPermission;

    public static void setupDefaults() {
        colorfulLogging = true;
        debug = false;
        minimumTime = 1;
        maximumTime = 4;
        particles = true;
        transformFromMaterial = "GRAVEL";
        transformToMaterial = "CLAY";

        pluginPrefix = "&5[&b" + ClayFarmingPlugin.plugin.getName() + "&5]";
        noPermission = "&cYou do not have permission.";

        Configuration config = new Configuration(ClayFarmingPlugin.plugin.getDataFolder() + File.separator + "config.yml");
        Configuration locale = new Configuration(ClayFarmingPlugin.plugin.getDataFolder() + File.separator + "locale.yml");

        config.add("General.ColorfulLogging", colorfulLogging);
        config.add("General.DebugMessages", debug);
        config.add("WaitTime.Minimum", minimumTime);
        config.add("WaitTime.Maximum", maximumTime);
        config.add("Particles.Enabled", particles);
        config.add("Materials.From", transformFromMaterial);
        config.add("Materials.To", transformToMaterial);

        locale.add("General.PluginPrefix", pluginPrefix);
        locale.add("General.NoPermission", noPermission);

        config.save();
        locale.save();

        setNewConfigValues(config);
        setNewLocaleValues(locale);
    }

    public static void setNewConfigValues(Configuration config) {
        colorfulLogging = config.getBoolean("General.ColorfulLogging");
        debug = config.getBoolean("General.DebugMessages");
        minimumTime = config.getInteger("WaitTime.Minimum");
        maximumTime = config.getInteger("WaitTime.Maximum");
        particles = config.getBoolean("Particles.Enabled");
        transformFromMaterial = config.getString("Materials.From");
        transformToMaterial = config.getString("Materials.To");
    }

    public static void setNewLocaleValues(Configuration locale) {
        pluginPrefix = locale.getString("General.PluginPrefix");
        noPermission = locale.getString("General.NoPermission");
    }

    public static void reload() {
        Configuration config = new Configuration(ClayFarmingPlugin.plugin.getDataFolder() + File.separator + "config.yml");
        Configuration locale = new Configuration(ClayFarmingPlugin.plugin.getDataFolder() + File.separator + "locale.yml");

        setNewConfigValues(config);
        setNewLocaleValues(locale);

        StaticMethods.reloadActive();
    }
}