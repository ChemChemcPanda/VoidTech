package com.example.voidtech.listeners;

import com.example.voidtech.items.TechResearchStationItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class TechResearchStationBreakListener implements Listener {

    private final JavaPlugin plugin;
    private final NamespacedKey researchStationKey;

    public TechResearchStationBreakListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.researchStationKey = new NamespacedKey(plugin, "tech_research_station");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        // 1️⃣ **如果破壞的是 "科技研究站" 頭顱**
        if (block.getType() == Material.PLAYER_HEAD || block.getType() == Material.PLAYER_WALL_HEAD) {
            if (block.getState() instanceof Skull skull) {
                PersistentDataContainer container = skull.getPersistentDataContainer();
                if (container.has(researchStationKey, PersistentDataType.STRING)) {

                    // **取消原始掉落物**
                    event.setDropItems(false);

                    // **掉落正確的 "科技研究站" 物品**
                    ItemStack researchStationItem = TechResearchStationItem.createResearchStation(plugin);
                    block.getWorld().dropItemNaturally(block.getLocation(), researchStationItem);

                    player.sendMessage("§c[系統] §f科技研究站被摧毀！");
                }
            }
        }

        // 2️⃣ **如果破壞的是 "科技研究站" 下方的合成台**
        if (block.getType() == Material.CRAFTING_TABLE) {
            // **檢查上方的方塊**
            Block aboveBlock = block.getLocation().add(0, 1, 0).getBlock();

            // **如果上方是玩家頭顱，則進一步檢查是否為科技研究站**
            if (aboveBlock.getType() == Material.PLAYER_HEAD || aboveBlock.getType() == Material.PLAYER_WALL_HEAD) {
                if (aboveBlock.getState() instanceof Skull skull) {
                    PersistentDataContainer container = skull.getPersistentDataContainer();
                    if (container.has(researchStationKey, PersistentDataType.STRING)) {

                        // **取消頭顱的掉落物**
                        aboveBlock.setType(Material.AIR);

                        // **掉落正確的 "科技研究站" 物品**
                        ItemStack researchStationItem = TechResearchStationItem.createResearchStation(plugin);
                        block.getWorld().dropItemNaturally(aboveBlock.getLocation(), researchStationItem);

                        player.sendMessage("§c[系統] §f合成台被摧毀，科技研究站失效！");
                    }
                }
            }
        }
    }
}
