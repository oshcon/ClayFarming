package net.doodcraft.oshcon.bukkit.clayfarming;

import net.doodcraft.oshcon.bukkit.clayfarming.config.Configuration;
import net.doodcraft.oshcon.bukkit.clayfarming.config.Settings;
import net.doodcraft.oshcon.bukkit.clayfarming.util.BukkitSerialization;
import net.doodcraft.oshcon.bukkit.clayfarming.util.StaticMethods;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class CFarmCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("cfarm")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!StaticMethods.hasPermission(player, "clayfarming.command.clayfarming")) {
                    return false;
                }
                if (args.length == 0) {
                    sendValidCommands(sender);
                    return true;
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    if (!StaticMethods.hasPermission(player, "clayfarming.command.reload")) {
                        return false;
                    }
                    sendReloaded(Settings.reload(), sender);
                    return true;
                }
                if (args[0].equalsIgnoreCase("addhand")) {
                    if (!StaticMethods.hasPermission(player, "clayfarming.command.addhand")) {
                        return false;
                    }
                    float chance = 5.0f;
                    if (args.length > 1) {
                        if (args[1] != null) {
                            if (Float.parseFloat(args[1]) > 0) {
                                chance = Float.parseFloat(args[1]);
                            }
                        }
                    }
                    ItemStack hand = player.getItemInHand().clone();
                    String item = BukkitSerialization.itemStackArrayToBase64(new ItemStack[]{hand});
                    StaticMethods.log(item);
                    String id = hand.getType().name();
                    if (hand.hasItemMeta()) {
                        if (hand.getItemMeta().hasDisplayName()) {
                            id = hand.getItemMeta().getDisplayName();
                        }
                    }
                    Configuration items = new Configuration(ClayFarmingPlugin.plugin.getDataFolder() + File.separator + "items.yml");
                    items.remove(id);
                    items.add( id + ".item", item);
                    items.add( id + ".chance", chance);
                    items.save();
                    player.sendMessage(StaticMethods.addColor("&7Added " + id + " &7with a &e" + chance + " &7percent chance."));
                    return true;
                }
                player.sendMessage(StaticMethods.addColor(Settings.pluginPrefix + "&cIncorrect subcommand."));
                sendValidCommands(sender);
                return false;
            } else {
                if (args.length == 0) {
                    sendValidCommands(sender);
                    return true;
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    boolean error = Settings.reload();
                    sendReloaded(error, sender);
                    return true;
                }
            }
        }
        return false;
    }

    public static void sendReloaded(boolean error, CommandSender sender) {
        if (!error) {
            if (sender instanceof Player) {
                sender.sendMessage(StaticMethods.addColor(Settings.pluginPrefix + " &aPlugin reloaded!"));
                StaticMethods.log("&aPlugin reloaded!");
            } else {
                StaticMethods.log("&aPlugin reloaded!");
            }
        } else {
            if (sender instanceof Player) {
                sender.sendMessage(StaticMethods.addColor(Settings.pluginPrefix + " &cError reloading plugin!"));
                StaticMethods.log("&cError reloading plugin!");
            } else {
                StaticMethods.log("&cError reloading plugin!");
            }
        }
    }

    public static void sendValidCommands(CommandSender sender) {
        if (sender instanceof Player) {
            sender.sendMessage(StaticMethods.addColor(Settings.pluginPrefix + " &3Valid Commands:"));
            sender.sendMessage(StaticMethods.addColor(Settings.pluginPrefix + " &b/cfarm reload: &7Reloads the config and active tasks"));
        } else {
            StaticMethods.log("&3Valid Commands:");
            StaticMethods.log("&b/chisel reload: &7Reloads the config and active tasks");
        }
    }
}