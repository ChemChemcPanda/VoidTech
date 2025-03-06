package com.example.voidtech.machines;

import com.example.voidtech.database.TechStorage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.block.TileState;

import java.util.ArrayList;
import java.util.List;

public class SawMachine implements Listener {

    private static final String SAW_TABLE_KEY = "saw_table_key";  // 鋸木台的唯一標籤

    // 創建鋸木台
    public static ItemStack createSawMachine() {
        // 創建一個鋸木台物品（切石機）
        ItemStack sawMachine = new ItemStack(Material.STONECUTTER);  // 使用切石機作為鋸木台物品的原形
        ItemMeta meta = sawMachine.getItemMeta();

        if (meta != null) {
            // 添加唯一標籤到物品上
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(new NamespacedKey("voidtech", SAW_TABLE_KEY), PersistentDataType.STRING, "true");

            // 設置顯示名稱
            meta.setDisplayName("§6鋸木台");

            // 設置LORE（描述文字）
            List<String> lore = new ArrayList<>();
            lore.add("§e分解木材，提高木材利用率");
            meta.setLore(lore);

            // 設置物品的meta
            sawMachine.setItemMeta(meta);
        }

        return sawMachine;
    }

    // 判斷是否為鋸木台
    public static boolean isSawMachine(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.getPersistentDataContainer().has(new NamespacedKey("voidtech", SAW_TABLE_KEY), PersistentDataType.STRING)) {
                return true;  // 返回為鋸木台
            }
        }
        return false;  // 不是鋸木台
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        ItemStack itemInHand = event.getItemInHand();

        // 如果放置的是鋸木台物品
        if (itemInHand.getType() == Material.STONECUTTER && SawMachine.isSawMachine(itemInHand)) {
            // 重新設置NBT標籤
            BlockState state = block.getState();
            if (state instanceof TileState) {
                TileState tileState = (TileState) state;
                PersistentDataContainer container = tileState.getPersistentDataContainer();
                container.set(new NamespacedKey("voidtech", SAW_TABLE_KEY), PersistentDataType.STRING, "true");

                // 更新TileEntity的狀態
                tileState.update();
            }
        }
    }

    // 處理木材分解的邏輯（確認鋸木台處於正確狀態）
    public static void processWood(Player player, ItemStack item) {
        if (TechStorage.getUnlockedTechs(player.getUniqueId()).contains("Saw_Table")) {
            if (item.getType() == Material.OAK_LOG || item.getType() == Material.BIRCH_LOG || item.getType() == Material.SPRUCE_LOG) {
                item.setType(Material.OAK_PLANKS); // 改為木板
                player.getInventory().addItem(new ItemStack(Material.STICK, 2)); // 添加木棒
                player.sendMessage("§a你已成功將木材切割為木板和木棒！");
            }
        } else {
            player.sendMessage("§c你尚未解鎖鋸木台科技，無法使用！");
        }
    }
}
