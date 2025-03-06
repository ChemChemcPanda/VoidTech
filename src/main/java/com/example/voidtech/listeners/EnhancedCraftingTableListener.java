package com.example.voidtech.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class EnhancedCraftingTableListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if (block == null || block.getType() != Material.CRAFTING_TABLE) return;

        // ✅ 檢查合成台下方是否有投擲器
        Block below = block.getRelative(BlockFace.DOWN);
        if (below.getType() != Material.DROPPER) return;

        if (below.getBlockData() instanceof Directional) {
            Directional directional = (Directional) below.getBlockData();
            if (directional.getFacing() == BlockFace.UP) {
                // ✅ 阻止點擊工作台
                event.setCancelled(true);
            }
        }
    }
}
