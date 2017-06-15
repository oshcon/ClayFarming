package net.doodcraft.oshcon.bukkit.clayfarming;

import net.doodcraft.oshcon.bukkit.clayfarming.config.Settings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CFarmCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("cfarm")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!StaticMethods.hasPermission(player, ClayFarmingPlugin.plugin.getName().toLowerCase() + ".command.cfarm")) {
                    return false;
                }
                if (args.length == 0) {
                    sender.sendMessage(StaticMethods.addColor(Settings.pluginPrefix + " &aValid Options: &ereload"));
                    return true;
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    if (!StaticMethods.hasPermission(player, ClayFarmingPlugin.plugin.getName().toLowerCase() + ".command.reload")) {
                        return false;
                    }
                    try {
                        Settings.reload();
                        sender.sendMessage(StaticMethods.addColor(Settings.pluginPrefix + " &aPlugin reloaded."));
                        return true;
                    } catch (Exception ex) {
                        sender.sendMessage(StaticMethods.addColor(Settings.pluginPrefix + " &cError reloading plugin."));
                        return false;
                    }
                }
                player.sendMessage(StaticMethods.addColor(Settings.pluginPrefix + " &cIncorrect subcommand."));
                sender.sendMessage(StaticMethods.addColor(Settings.pluginPrefix + " &aValid Options: &ereload"));
                return false;
            } else {
                if (args.length == 0) {
                    StaticMethods.log("&aValid Options: &ereload");
                    return true;
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    try {
                        Settings.reload();
                        StaticMethods.log("&aPlugin reloaded.");
                        return true;
                    } catch (Exception ex) {
                        StaticMethods.log("&cError reloading plugin.");
                        return true;
                    }
                }
                StaticMethods.log("&cIncorrect subcommand.");
                StaticMethods.log("&aValid Options: &ereload");
                return false;
            }
        }
        return false;
    }
}