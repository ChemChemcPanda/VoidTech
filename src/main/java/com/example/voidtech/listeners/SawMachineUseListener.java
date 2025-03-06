package com.example.voidtech.listeners;

import com.example.voidtech.machines.SawMachine;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class SawMachineUseListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();

        // 檢查是否為鋸木台
        if (SawMachine.isSawMachine(item)) {
            // 這裡放置木材進行處理
            if (block.getType() == Material.OAK_LOG || block.getType() == Material.BIRCH_LOG || block.getType() == Material.SPRUCE_LOG) {
                // 執行鋸木台的處理
                SawMachine.processWood(player, new ItemStack(block.getType()));
            }
        }
    }
}
