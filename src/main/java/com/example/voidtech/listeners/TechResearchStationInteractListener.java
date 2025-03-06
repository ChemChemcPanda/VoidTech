package com.example.voidtech.listeners;

import com.example.voidtech.gui.TechResearchGUI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class TechResearchStationInteractListener implements Listener {

    private final JavaPlugin plugin;
    private final NamespacedKey researchStationKey;

    public TechResearchStationInteractListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.researchStationKey = new NamespacedKey(plugin, "tech_research_station");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        // **確保事件來自 "主手" 而非 "副手"，避免重複觸發**
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        // **確保玩家是右鍵點擊**
        if (block == null || event.getAction().toString().contains("LEFT_CLICK")) {
            return;
        }

        // **確保點擊的是玩家頭顱**
        if (block.getType() != Material.PLAYER_HEAD && block.getType() != Material.PLAYER_WALL_HEAD) {
            return;
        }

        // **檢查是否為科技研究站**
        if (block.getState() instanceof Skull skull) {
            if (skull.getPersistentDataContainer().has(researchStationKey, PersistentDataType.STRING)) {
                player.sendMessage("§a[系統] §f科技研究站啟動中...");
                player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1f, 1f);

                // **開啟科技研究 GUI**
                TechResearchGUI.openTechMenu(player);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        TechResearchGUI.handleMenuClick(event);
    }
}
