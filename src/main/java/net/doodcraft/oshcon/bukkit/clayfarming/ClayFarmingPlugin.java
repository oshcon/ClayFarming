package net.doodcraft.oshcon.bukkit.clayfarming;

import de.slikey.effectlib.EffectManager;
import net.doodcraft.oshcon.bukkit.clayfarming.config.Settings;
import net.doodcraft.oshcon.bukkit.clayfarming.listeners.BlockListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class  ClayFarmingPlugin extends JavaPlugin {

    public static Plugin plugin;
    public static Random rand;
    public static EffectManager effectManager;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();

        plugin = this;
        rand = new Random();
        effectManager = new EffectManager(plugin);

        registerListeners();
        setExecutors();

        Settings.setupDefaults();
        StaticMethods.loadActive();

        long finish = System.currentTimeMillis();
        StaticMethods.log("&aClayFarming v" + plugin.getDescription().getVersion() + " is now loaded. &e(" + (finish-start) + "ms)");
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
}