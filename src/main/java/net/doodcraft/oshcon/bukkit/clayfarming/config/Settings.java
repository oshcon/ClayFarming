package net.doodcraft.oshcon.bukkit.clayfarming.config;

import net.doodcraft.oshcon.bukkit.clayfarming.ClayFarmingPlugin;
import net.doodcraft.oshcon.bukkit.clayfarming.util.StaticMethods;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Settings {
    // CONFIG
    public static Boolean colorfulLogging;
    public static int minimumTime;
    public static int maximumTime;
    public static String transformFromMaterial;
    public static String transformToMaterial;
    public static List<String> liquids;
    // LOCALE
    public static String pluginPrefix;
    public static String noPermission;

    public static void setupDefaults() {
        // CONFIG
        colorfulLogging = true;
        minimumTime = 1;
        maximumTime = 4;
        transformFromMaterial = "GRAVEL";
        transformToMaterial = "CLAY";
        // LOCALE
        pluginPrefix = "&5[&b" + ClayFarmingPlugin.plugin.getName() + "&5]";
        noPermission = "&cYou do not have permission.";

        Configuration config = new Configuration(ClayFarmingPlugin.plugin.getDataFolder() + File.separator + "config.yml");
        Configuration locale = new Configuration(ClayFarmingPlugin.plugin.getDataFolder() + File.separator + "locale.yml");
        config.add("General.ColorfulLogging", colorfulLogging);
        config.remove("General.DebugMessages");
        config.add("WaitTime.Minimum", minimumTime);
        config.add("WaitTime.Maximum", maximumTime);
        config.remove("Particles");
        config.add("Materials.From", transformFromMaterial);
        config.add("Materials.To", transformToMaterial);
        liquids = new ArrayList<>();
        liquids.add("WATER");
        liquids.add("STATIONARY_WATER");
        liquids.add("LAVA");
        liquids.add("STATIONARY_LAVA");
        config.add("Materials.Liquids", liquids);
        locale.add("General.PluginPrefix", pluginPrefix);
        locale.add("General.NoPermission", noPermission);
        config.save();
        locale.save();
        setNewConfigValues(config);
        setNewLocaleValues(locale);
    }

    private static void setNewConfigValues(Configuration config) {
        colorfulLogging = config.getBoolean("General.ColorfulLogging");
        minimumTime = config.getInteger("WaitTime.Minimum");
        maximumTime = config.getInteger("WaitTime.Maximum");
        transformFromMaterial = config.getString("Materials.From");
        transformToMaterial = config.getString("Materials.To");
        liquids = config.getStringList("Materials.Liquids");
    }

    private static void setNewLocaleValues(Configuration locale) {
        pluginPrefix = locale.getString("General.PluginPrefix");
        noPermission = locale.getString("General.NoPermission");
    }

    public static boolean reload() {
        try {
            Configuration config = new Configuration(ClayFarmingPlugin.plugin.getDataFolder() + File.separator + "config.yml");
            Configuration locale = new Configuration(ClayFarmingPlugin.plugin.getDataFolder() + File.separator + "locale.yml");
            setNewConfigValues(config);
            setNewLocaleValues(locale);
            StaticMethods.reloadActive();
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return true;
        }
    }
}