package com.example.voidtech.machines;

import com.example.voidtech.database.TechStorage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.block.TileState;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SawMachine implements Listener {

    private static final String SAW_TABLE_KEY = "saw_table_key";  // 鋸木台的唯一標籤
    private static JavaPlugin plugin;

    // 設置插件實例
    public static void setPlugin(JavaPlugin pluginInstance) {
        plugin = pluginInstance; // 確保插件實例正確設置
    }

    // 創建鋸木台
    public static ItemStack createSawMachine() {
        ItemStack sawMachine = new ItemStack(Material.STONECUTTER);
        ItemMeta meta = sawMachine.getItemMeta();

        if (meta != null) {
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(new NamespacedKey("voidtech", SAW_TABLE_KEY), PersistentDataType.STRING, "true");
            meta.setDisplayName("§6鋸木台");
            List<String> lore = new ArrayList<>();
            lore.add("§e分解木材，提高木材利用率");
            meta.setLore(lore);

            sawMachine.setItemMeta(meta);
        }
        return sawMachine;
    }

    // 判斷是否為鋸木台
    public static boolean isSawMachine(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.getPersistentDataContainer().has(new NamespacedKey("voidtech", SAW_TABLE_KEY), PersistentDataType.STRING)) {
                return true;
            }
        }
        return false;
    }

    // 使用插件的 logger
    public static void logInfo(String message) {
        if (plugin != null) {
            plugin.getLogger().info(message);
        } else {
            System.out.println("Plugin instance is null!");
        }
    }

    // 放置鋸木台時，設置NBT標籤
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        logInfo("onBlockPlace 事件被觸發！");

        Block block = event.getBlockPlaced();
        ItemStack itemInHand = event.getItemInHand();

        // 確保放置的物品是鋸木台
        if (itemInHand.getType() == Material.STONECUTTER && isSawMachine(itemInHand)) {
            BlockState state = block.getState();
            if (state instanceof TileState) {
                TileState tileState = (TileState) state;
                PersistentDataContainer container = tileState.getPersistentDataContainer();
                UUID uuid = UUID.randomUUID(); // 創建唯一的UUID
                container.set(new NamespacedKey("voidtech", SAW_TABLE_KEY), PersistentDataType.STRING, uuid.toString());

                // 強制更新TileState資料
                tileState.update();

                // 訊息：檢查是否儲存了NBT資料
                logInfo("鋸木台已經放置，並成功儲存UUID資料！UUID: " + uuid);
            }
        } else {
            logInfo("放置的物品不是鋸木台，物品名稱: " + itemInHand.getType());
        }
    }

    // 破壞鋸木台時，處理掉落物品與NBT標籤
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        logInfo("onBlockBreak 事件被觸發，破壞的方塊: " + event.getBlock().getType());

        Block block = event.getBlock();

        if (block.getState() instanceof TileState) {
            TileState tileState = (TileState) block.getState();
            PersistentDataContainer container = tileState.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey("voidtech", SAW_TABLE_KEY);

            // 如果資料存在，處理鋸木台的掉落
            if (container.has(key, PersistentDataType.STRING)) {
                event.setDropItems(false);  // 禁止掉落普通方塊
                block.getWorld().dropItemNaturally(block.getLocation(), createSawMachine());  // 丟出鋸木台
                logInfo("鋸木台已經被破壞，並且正確丟出鋸木台！");  // 顯示破壞訊息
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
