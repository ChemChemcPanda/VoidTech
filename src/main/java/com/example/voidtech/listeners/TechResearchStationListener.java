package com.example.voidtech.listeners;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class TechResearchStationListener implements Listener {

    private final JavaPlugin plugin;
    private final NamespacedKey researchStationKey;

    public TechResearchStationListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.researchStationKey = new NamespacedKey(plugin, "tech_research_station");
    }

    @EventHandler
    public void onHeadPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        ItemStack itemInHand = event.getItemInHand();

        // 確保放置的是 "科技研究站" 頭顱，而非普通的玩家頭顱
        if (itemInHand.getItemMeta() == null ||
                !itemInHand.getItemMeta().getPersistentDataContainer().has(researchStationKey, PersistentDataType.STRING)) {
            return; // 如果沒有 "科技研究站" 標記，則允許正常放置
        }

        // 檢查下方是否為合成台
        Block belowBlock = block.getLocation().subtract(0, 1, 0).getBlock();
        if (belowBlock.getType() != Material.CRAFTING_TABLE) {
            // 取消放置
            event.setCancelled(true);
            player.sendMessage("§c[系統] §f科技研究站只能放置於 §6合成台 §f上方！");
            return;
        }

        // 標記該頭顱為科技研究站
        if (block.getState() instanceof Skull skull) {
            PersistentDataContainer container = skull.getPersistentDataContainer();
            container.set(researchStationKey, PersistentDataType.STRING, "true");
            skull.update();
            player.sendMessage("§a[系統] §f成功放置 §e科技研究站§f！");
        }
    }
}
