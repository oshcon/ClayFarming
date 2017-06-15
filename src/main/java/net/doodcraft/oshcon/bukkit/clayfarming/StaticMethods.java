package net.doodcraft.oshcon.bukkit.clayfarming;

import net.doodcraft.oshcon.bukkit.clayfarming.config.Configuration;
import net.doodcraft.oshcon.bukkit.clayfarming.config.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;

public class StaticMethods {

    public static ArrayList<Block> active = new ArrayList<>();

    public static void transform(Block block, Material material) {
        if (!material.isBlock()) {
            return;
        }
        if (!active.contains(block)) {
            active.add(block);
        }
        double time = ClayFarmingPlugin.rand.nextInt((Settings.maximumTime*1000 - Settings.minimumTime*1000) + 1) + Settings.minimumTime*1000;
        time = time/1000;
        Bukkit.getScheduler().runTaskLater(ClayFarmingPlugin.plugin, new Runnable(){
            @Override
            public void run() {
                if (active.contains(block)) {
                    Block up = block.getRelative(BlockFace.UP);
                    if (up.getType() == Material.WATER || up.getType() == Material.STATIONARY_WATER) {
                        block.setType(material);
                        active.remove(block);
                    } else {
                        active.remove(block);
                    }
                }
            }
        },(long) (time * 20));
    }

    public static void dumpActive() {
        if (active.size() <= 0) {
            return;
        }
        Configuration cache = new Configuration(ClayFarmingPlugin.plugin.getDataFolder() + File.separator + "cache.yml");
        Bukkit.getScheduler().cancelTasks(ClayFarmingPlugin.plugin);
        log("&aDumping current tasks to file..");
        ArrayList<String> dumpList = new ArrayList<>();
        for (Block block : active) {
            Location location = block.getLocation();
            if (location != null) {
                String locString = location.getWorld().getName() + "~" + location.getX() + "~" + location.getY() + "~" + location.getZ();
                dumpList.add(locString);
            }
        }
        cache.add("cache", dumpList);
        cache.save();
    }

    public static void loadActive() {
        Configuration cache = new Configuration(ClayFarmingPlugin.plugin.getDataFolder() + File.separator + "cache.yml");
        if (cache.get("cache") != null) {
            log("&aLoading prior task cache-file..");
            for (String s : cache.getStringList("cache")) {
                Block block = getBlock(s);
                if (block != null) {
                    transform(block, Material.CLAY);
                }
            }
            cache.remove("cache");
        }
        cache.save();
    }

    public static void reloadActive() {
        dumpActive();
        loadActive();
    }

    public static Block getBlock(String string) {
        String[] parts = string.split("~");
        try {
            Location loc = new Location(Bukkit.getServer().getWorld(String.valueOf(parts[0])), Double.valueOf(parts[1]), Double.valueOf(parts[2]), Double.valueOf(parts[3]));
            if (loc.getBlock() != null) {
                return loc.getBlock();
            }
        } catch (Exception ignored) {}
        return null;
    }

    public static Boolean hasPermission(Player player, String node) {
        if (player.isOp()) {
            return true;
        }
        if (player.hasPermission(ClayFarmingPlugin.plugin.getName().toLowerCase() + ".*")) {
            return true;
        }
        if (player.hasPermission(node)) {
            return true;
        } else {
            player.sendMessage(addColor(Settings.pluginPrefix + " &r" + Settings.noPermission));
            return false;
        }
    }

    public static void log(String message) {
        message = Settings.pluginPrefix + "&r " + message;
        sendConsole(message);
    }

    public static void sendConsole(String message) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        try {
            if (Settings.colorfulLogging) {
                console.sendMessage(addColor(message));
            } else {
                console.sendMessage(removeColor(addColor(message)));
            }
        } catch (Exception ignored) {
            console.sendMessage(removeColor(addColor(message)));
        }
    }

    public static String addColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String removeColor(String message) {
        message = addColor(message);
        return ChatColor.stripColor(message);
    }
}