package net.doodcraft.oshcon.bukkit.clayfarming.listeners;

import net.doodcraft.oshcon.bukkit.clayfarming.StaticMethods;
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
            if (from.getRelative(BlockFace.DOWN).getType() == Material.GRAVEL) {
                if (!StaticMethods.active.contains(from.getRelative(BlockFace.DOWN))) {
                    StaticMethods.transform(from.getRelative(BlockFace.DOWN), Material.CLAY);
                }
            }
        }
        if (to.getType() == Material.WATER || to.getType() == Material.STATIONARY_WATER) {
            if (to.getRelative(BlockFace.DOWN).getType() == Material.GRAVEL) {
                if (!StaticMethods.active.contains(to.getRelative(BlockFace.DOWN))) {
                    StaticMethods.transform(to.getRelative(BlockFace.DOWN), Material.CLAY);
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        if (block.getType() == Material.WATER) {
            if (block.getRelative(BlockFace.DOWN).getType() == Material.GRAVEL) {
                if (!StaticMethods.active.contains(block.getRelative(BlockFace.DOWN))) {
                    StaticMethods.transform(block.getRelative(BlockFace.DOWN), Material.CLAY);
                }
            }
        }
    }
}