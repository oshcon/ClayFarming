package net.doodcraft.oshcon.bukkit.clayfarming;

import net.doodcraft.oshcon.bukkit.clayfarming.config.Settings;
import net.doodcraft.oshcon.bukkit.clayfarming.listeners.BlockListener;
import net.doodcraft.oshcon.bukkit.clayfarming.util.StaticMethods;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.concurrent.Callable;

public class ClayFarmingPlugin extends JavaPlugin {

    public static Plugin plugin;
    public static Random rand;
    public static Metrics metrics;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        plugin = this;
        rand = new Random();
        metrics = new Metrics(this);
        registerListeners();
        setExecutors();
        Settings.setupDefaults();
        StaticMethods.loadActive();
        long finish = System.currentTimeMillis();
        StaticMethods.log("&aClayFarming v" + plugin.getDescription().getVersion() + " is now loaded. &e(" + (finish - start) + "ms)");
        if (!Settings.metrics) return;
        try {
            metrics.addCustomChart(new Metrics.SingleLineChart("using_itemdrops", new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    if (Settings.dropItems) return 1;
                    return 0;
                }
            }));
        } catch (Exception ignored) {}
    }

    @Override
    public void onDisable() {
        StaticMethods.dumpActive();
    }

    private void registerListeners() {
        registerEvents(plugin, new BlockListener());
    }

    private void registerEvents(Plugin plugin, Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    private void setExecutors() {
        getCommand("cfarm").setExecutor(new CFarmCommand());
    }
}