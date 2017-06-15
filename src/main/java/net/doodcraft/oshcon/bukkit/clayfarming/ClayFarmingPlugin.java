package net.doodcraft.oshcon.bukkit.clayfarming;

import net.doodcraft.oshcon.bukkit.clayfarming.config.Settings;
import net.doodcraft.oshcon.bukkit.clayfarming.listeners.BlockListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import java.io.IOException;
import java.util.Random;

public class ClayFarmingPlugin extends JavaPlugin {

    public static Plugin plugin;
    public static Random rand;

    @Override
    public void onEnable() {
        plugin = this;
        rand = new Random();
        registerListeners();
        setExecutors();
        Settings.setupDefaults();
        StaticMethods.loadActive();
        try {
            Metrics metrics = new Metrics(this);
            sendMetrics(metrics);
        } catch (IOException ignored) {}
    }


    @Override
    public void onDisable() {
        StaticMethods.dumpActive();
    }

    public void registerListeners() {
        registerEvents(plugin, new BlockListener());
    }

    public void registerEvents(Plugin plugin, Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    public void setExecutors() {
        getCommand("cfarm").setExecutor(new CFarmCommand());
    }

    public void sendMetrics(Metrics metrics) {
        Bukkit.getServer().getScheduler().runTask(plugin, new Runnable(){
            @Override
            public void run() {
                metrics.start();
            }
        });
    }
}