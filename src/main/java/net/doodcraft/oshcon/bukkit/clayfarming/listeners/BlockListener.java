package net.doodcraft.oshcon.bukkit.clayfarming.listeners;

import net.doodcraft.oshcon.bukkit.clayfarming.StaticMethods;
import net.doodcraft.oshcon.bukkit.clayfarming.config.Settings;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {

    @EventHandler
    public void onFromTo(BlockFromToEvent event) {
        Block from = event.getBlock();
        Block to = event.getToBlock();

        if (from.getType() == Material.WATER || from.getType() == Material.STATIONARY_WATER) {
            if (from.getRelative(BlockFace.DOWN).getType() == Material.valueOf(Settings.transformFromMaterial.toUpperCase())) {
                if (!StaticMethods.tranformTasks.contains(from.getRelative(BlockFace.DOWN))) {
                    StaticMethods.transform(from.getRelative(BlockFace.DOWN));
                }
            }
        }

        if (to.getType() == Material.WATER || to.getType() == Material.STATIONARY_WATER) {
            if (to.getRelative(BlockFace.DOWN).getType() == Material.valueOf(Settings.transformFromMaterial.toUpperCase())) {
                if (!StaticMethods.tranformTasks.contains(to.getRelative(BlockFace.DOWN))) {
                    StaticMethods.transform(to.getRelative(BlockFace.DOWN));
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();

        if (block.getType() == Material.WATER) {
            if (block.getRelative(BlockFace.DOWN).getType() == Material.valueOf(Settings.transformFromMaterial.toUpperCase())) {
                if (!StaticMethods.tranformTasks.contains(block.getRelative(BlockFace.DOWN))) {
                    StaticMethods.transform(block.getRelative(BlockFace.DOWN));
                }
            }
        }
    }
}