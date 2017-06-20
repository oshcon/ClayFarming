package net.doodcraft.oshcon.bukkit.clayfarming;

import de.slikey.effectlib.util.ParticleEffect;
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
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.ArrayList;

public class StaticMethods {

    public static ArrayList<Block> tranformTasks = new ArrayList<>();
    public static ArrayList<Integer> bubbleTasks = new ArrayList<>();
    public static Configuration cache = new Configuration(ClayFarmingPlugin.plugin.getDataFolder() + File.separator + "cache.yml");

    public static void transform(Block block) {

        Material material = Material.valueOf(Settings.transformToMaterial.toUpperCase());

        if (!material.isBlock()) {
            log("&c\"Materials.To\" contains an invalid value.");
            return;
        }

        debug("Transforming block: " + locStringFromBlock(block));
        int bubbleTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(ClayFarmingPlugin.plugin, new Runnable(){
            @Override
            public void run() {
                try {
                    Location waterLocation = block.getLocation().add(0.5, 1, 0.5);
                    if (waterLocation.getBlock().getType() == Material.WATER || waterLocation.getBlock().getType() == Material.STATIONARY_WATER) {
                        if (block.getType() == Material.valueOf(Settings.transformFromMaterial.toUpperCase())) {
                            ParticleEffect.WATER_BUBBLE.display(0, 0, 0, 0.35F, 1, waterLocation, 64);
                            return;
                        }
                    }
                    debug("Error spawning bubbles for: " + locStringFromBlock(waterLocation.getBlock()));
                } catch (Exception ex) {
                    debug(ex.getLocalizedMessage());
                }
            }
        },1L,10L);

        if (!bubbleTasks.contains(bubbleTask)) {
            bubbleTasks.add(bubbleTask);
        }

        if (!tranformTasks.contains(block)) {
            tranformTasks.add(block);
        }

        double time = ClayFarmingPlugin.rand.nextInt((Settings.maximumTime*1000 - Settings.minimumTime*1000) + 1) + Settings.minimumTime*1000;
        time = time/1000;

        BukkitTask transformTask = Bukkit.getScheduler().runTaskLater(ClayFarmingPlugin.plugin, new Runnable(){
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                if (tranformTasks.contains(block)) {
                    if (block.getType() == Material.valueOf(Settings.transformFromMaterial.toUpperCase())) {
                        Block up = block.getRelative(BlockFace.UP);
                        if (up.getType() == Material.WATER || up.getType() == Material.STATIONARY_WATER) {
                            block.setType(material);
                            debug("Transformed (" + (System.currentTimeMillis()-startTime) + "ms):" + locStringFromBlock(block));
                        }
                    }
                    debug("Stopping and removing block from tasks..");
                    tranformTasks.remove(block);
                    stopBubbleTask(bubbleTask);
                } else {
                    debug("transformTasks did not contain block: " + locStringFromBlock(block));
                    stopBubbleTask(bubbleTask);
                }
            }
        },(long) (time*20));
    }

    public static void stopBubbleTask(Integer task) {
        try {
            Bukkit.getScheduler().cancelTask(task);
        } catch (Exception ex) {
            debug("Ex: " + ex.getLocalizedMessage());
        }
    }

    public static void dumpActive() {
        if (tranformTasks.size() <= 0) {
            cache.delete();
            return;
        }
        debug("Dumping active tasks to cache file..");
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
        bubbleTasks.clear();
    }

    public static void loadActive() {
        if (cache.get("cache") != null) {
            debug("Loading cache file..");
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

    public static Block locStringToBlock(String string) {
        if (string != null) {
            String[] parts = string.split("~");
            try {
                Location loc = new Location(Bukkit.getServer().getWorld(String.valueOf(parts[0])), Double.valueOf(parts[1]), Double.valueOf(parts[2]), Double.valueOf(parts[3]));
                if (loc.getBlock() != null) {
                    return loc.getBlock();
                }
            } catch (Exception ex) {
                debug(ex.getLocalizedMessage());
                return null;
            }
        }
        return null;
    }

    public static String locStringFromBlock(Block block) {
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
        message = Settings.pluginPrefix + "&r " + message;
        sendConsole(message);
    }

    public static void debug(String message) {
        if (Settings.debug) {
            message = "&8[&dDEBUG&8] &e" + message;
            log(message);
        }
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