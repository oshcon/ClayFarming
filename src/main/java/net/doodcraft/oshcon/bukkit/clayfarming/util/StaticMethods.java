package net.doodcraft.oshcon.bukkit.clayfarming.util;

import net.doodcraft.oshcon.bukkit.clayfarming.ClayFarmingPlugin;
import net.doodcraft.oshcon.bukkit.clayfarming.config.Configuration;
import net.doodcraft.oshcon.bukkit.clayfarming.config.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class StaticMethods {

    public static Random random;
    public static ArrayList<Block> tranformTasks = new ArrayList<>();

    public static boolean isLiquid(Block block) {
        if (!block.isLiquid()) return false;
        return (Settings.liquids.contains(block.getType().name()));
    }

    public static void transform(Block block) {
        Material material = Material.valueOf(Settings.transformToMaterial.toUpperCase());
        if (!tranformTasks.contains(block)) {
            tranformTasks.add(block);
        }
        double time = ClayFarmingPlugin.rand.nextInt((Settings.maximumTime * 1000 - Settings.minimumTime * 1000) + 1) + Settings.minimumTime * 1000;
        Bukkit.getScheduler().runTaskLater(ClayFarmingPlugin.plugin, () -> {
            if (tranformTasks.contains(block)) {
                if (block.getType() == Material.valueOf(Settings.transformFromMaterial.toUpperCase())) {
                    dropItems(block);
                    block.setType(material);
                }
                tranformTasks.remove(block);
            }
        }, (long) ((time / 1000) * 20));
    }

    public static void dropItems(Block block) {
        if (!Settings.dropItems) return;
        if (random == null) {
            random = new Random();
        }
        Configuration items = new Configuration(ClayFarmingPlugin.plugin.getDataFolder() + File.separator + "items.yml");
        for (String s : items.getKeys(false)) {
            double r = 0.01 + random.nextDouble() * (99.99);
            if (r <= items.getDouble(s + ".chance")) {
                try {
                    ItemStack[] i = BukkitSerialization.itemStackArrayFromBase64(items.getString(s + ".item"));
                    ItemStack item = i[0];
                    if (item != null) {
                        Location loc = block.getLocation().clone();
                        if (loc.getWorld() != null) {
                            loc.getWorld().dropItemNaturally(loc, item);
                        }
                    }
                } catch (IOException ignored) {}
            }
        }
    }

    public static void dumpActive() {
        Configuration cache = new Configuration(ClayFarmingPlugin.plugin.getDataFolder() + File.separator + "cache.yml");
        if (tranformTasks.size() <= 0) {
            cache.delete();
            return;
        }
        ArrayList<String> dumpList = new ArrayList<>();
        for (Block block : tranformTasks) {
            Location location = block.getLocation();
            if (location != null) {
                dumpList.add(locStringFromBlock(block));
            }
        }
        cache.add("cache", dumpList);
        cache.save();
        Bukkit.getScheduler().cancelTasks(ClayFarmingPlugin.plugin);
        tranformTasks.clear();
    }

    public static void loadActive() {
        Configuration cache = new Configuration(ClayFarmingPlugin.plugin.getDataFolder() + File.separator + "cache.yml");
        if (cache.get("cache") != null) {
            for (String s : cache.getStringList("cache")) {
                Block block = locStringToBlock(s);
                if (block != null) {
                    transform(block);
                }
            }
            cache.remove("cache");
        }
        cache.delete();
    }

    public static void reloadActive() {
        dumpActive();
        loadActive();
    }

    private static Block locStringToBlock(String string) {
        if (string != null) {
            String[] parts = string.split("~");
            try {
                Location loc = new Location(Bukkit.getServer().getWorld(String.valueOf(parts[0])), Double.valueOf(parts[1]), Double.valueOf(parts[2]), Double.valueOf(parts[3]));
                if (loc.getBlock() != null) {
                    return loc.getBlock();
                }
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }

    private static String locStringFromBlock(Block block) {
        if (block == null) {
            return null;
        } else {
            Location loc = block.getLocation();
            String world = loc.getWorld().getName();
            double x = loc.getX();
            double y = loc.getY();
            double z = loc.getZ();
            return world + "~" + x + "~" + y + "~" + z;
        }
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
        message = Settings.pluginPrefix + " &r" + message;
        sendConsole(message);
    }

    private static void sendConsole(String message) {
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

    private static String removeColor(String message) {
        message = addColor(message);
        return ChatColor.stripColor(message);
    }
}