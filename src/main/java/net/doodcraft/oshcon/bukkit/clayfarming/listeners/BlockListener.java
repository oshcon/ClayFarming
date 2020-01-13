package net.doodcraft.oshcon.bukkit.clayfarming.listeners;

import net.doodcraft.oshcon.bukkit.clayfarming.config.Settings;
import net.doodcraft.oshcon.bukkit.clayfarming.util.StaticMethods;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {

    BlockFace[] faces = {BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};

    @EventHandler
    public void onUpdate(BlockPhysicsEvent event) {
        Block block = event.getBlock();
        if (!StaticMethods.isLiquid(block)) return;
        for (BlockFace f : faces) {
            if (block.getRelative(f).getType() == Material.valueOf(Settings.transformFromMaterial.toUpperCase())) {
                if (!StaticMethods.tranformTasks.contains(block.getRelative(f))) {
                    StaticMethods.transform(block.getRelative(f));
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        if (block.getType() != Material.valueOf(Settings.transformFromMaterial.toUpperCase())) return;
        for (BlockFace f : faces) {
            if (StaticMethods.isLiquid(block.getRelative(f))) {
                if (!StaticMethods.tranformTasks.contains(block)) {
                    StaticMethods.transform(block);
                    break;
                }
            }
        }
    }

    public void attemptTransform(Block block) {
        for (BlockFace f : faces) {
            if (StaticMethods.isLiquid(block.getRelative(f))) {
                if (!StaticMethods.tranformTasks.contains(block)) {
                    StaticMethods.transform(block);
                    break;
                }
            }
        }
    }
}