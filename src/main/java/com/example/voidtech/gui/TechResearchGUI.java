package com.example.voidtech.gui;

import com.example.voidtech.database.TechStorage;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class TechResearchGUI implements Listener {

    private static final String GUI_TITLE = ChatColor.BOLD + "" + ChatColor.DARK_PURPLE + "▍科技研究站";

    private static final Map<String, Material> techIcons = new LinkedHashMap<>();
    private static final Map<String, String> techDescriptions = new LinkedHashMap<>();

    static {
        // **個人科技**
        techIcons.put("隨身科技", Material.ENDER_CHEST);
        techDescriptions.put("隨身科技", "研究可攜式科技，如隨身背包、隨身終界箱等");

        // **基礎科技**
        techIcons.put("簡單機械", Material.PISTON);
        techDescriptions.put("簡單機械", "基礎的機械技術，開啟科技時代的第一步");

        techIcons.put("能源傳輸", Material.REDSTONE);
        techDescriptions.put("能源傳輸", "學習如何高效輸送能量");

        techIcons.put("初級研究", Material.BOOK);
        techDescriptions.put("初級研究", "建立科技研究的基礎");

        // **材料科技**
        techIcons.put("採礦技術", Material.IRON_PICKAXE);
        techDescriptions.put("採礦技術", "提高資源開採效率");

        techIcons.put("冶煉系統", Material.FURNACE);
        techDescriptions.put("冶煉系統", "開發更高效的冶煉方式");

        techIcons.put("資源壓縮技術", Material.ANVIL);
        techDescriptions.put("資源壓縮技術", "讓材料更易於儲存與運輸");

        // **能源科技**
        techIcons.put("太陽能", Material.DAYLIGHT_DETECTOR);
        techDescriptions.put("太陽能", "利用太陽能發電，提供清潔能源");

        techIcons.put("風能", Material.FEATHER);
        techDescriptions.put("風能", "風能技術，適用於多種環境");

        techIcons.put("核能", Material.TNT);
        techDescriptions.put("核能", "強大的能量來源，需謹慎使用");

        techIcons.put("無線能量傳輸", Material.END_ROD);
        techDescriptions.put("無線能量傳輸", "遠距離輸送能量的新技術");

        // **生存科技**
        techIcons.put("自動農業", Material.WHEAT);
        techDescriptions.put("自動農業", "開發自動化農場，提高糧食產量");

        techIcons.put("食物生產", Material.APPLE);
        techDescriptions.put("食物生產", "利用科學技術改進食品供應");

        techIcons.put("飲水過濾技術", Material.WATER_BUCKET);
        techDescriptions.put("飲水過濾技術", "確保乾淨的飲水來源");

        // **武器科技**
        techIcons.put("高級武器", Material.DIAMOND_SWORD);
        techDescriptions.put("高級武器", "開發更強大的攻擊武器");

        techIcons.put("裝甲", Material.DIAMOND_CHESTPLATE);
        techDescriptions.put("裝甲", "增強防禦，提高生存率");

        techIcons.put("能量護盾", Material.SHIELD);
        techDescriptions.put("能量護盾", "利用能量場提供額外防護");

        // **自動化科技**
        techIcons.put("自動機械", Material.DISPENSER);
        techDescriptions.put("自動機械", "讓機器自動完成各種工作");

        techIcons.put("物品傳輸系統", Material.HOPPER);
        techDescriptions.put("物品傳輸系統", "智能化物流管理系統");

        techIcons.put("智慧製造技術", Material.CRAFTING_TABLE);
        techDescriptions.put("智慧製造技術", "提高工業生產效率");

        // **載具科技**
        techIcons.put("陸地載具", Material.MINECART);
        techDescriptions.put("陸地載具", "研發高速與耐用的陸地交通工具");

        techIcons.put("空中載具", Material.ELYTRA);
        techDescriptions.put("空中載具", "開發飛行載具，探索更高的世界");

        techIcons.put("太空探索", Material.ENDER_PEARL);
        techDescriptions.put("太空探索", "邁向星辰大海的第一步");

        // **太空科技**
        techIcons.put("太空站建造", Material.IRON_BLOCK);
        techDescriptions.put("太空站建造", "在太空中建立生存基地");

        techIcons.put("行星探測", Material.END_STONE);
        techDescriptions.put("行星探測", "開發探索遠方星球的技術");

        techIcons.put("黑洞能源", Material.NETHER_STAR);
        techDescriptions.put("黑洞能源", "研究宇宙中最神秘的能量來源");
    }

    public static void openTechMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, GUI_TITLE);

        int slot = 0;
        for (Map.Entry<String, Material> entry : techIcons.entrySet()) {
            String techName = entry.getKey();
            String description = techDescriptions.getOrDefault(techName, "未知技術");
            gui.setItem(slot, createMenuItem(entry.getValue(), ChatColor.BOLD + "" + ChatColor.AQUA + techName, description, "點擊進入研究"));
            slot++;
        }

        ItemStack grayGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassMeta = grayGlass.getItemMeta();
        if (glassMeta != null) {
            glassMeta.setDisplayName(ChatColor.GRAY + " ");
            grayGlass.setItemMeta(glassMeta);
        }

        // **填滿 GUI 底部的一橫排**
        for (int i = 45; i < 54; i++) {
            if (i != 49) { // **保留關閉按鈕的位置**
                gui.setItem(i, grayGlass);
            }
        }

        // **添加關閉按鈕**
        gui.setItem(49, createMenuItem(Material.BARRIER, "✖ 關閉", "點擊關閉科技研究站", "關閉"));

        // **打開 GUI**
        player.openInventory(gui);
    }

    private static ItemStack createMenuItem(Material material, String name, String description, String actionText) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(
                    ChatColor.GRAY + description,
                    "",
                    ChatColor.YELLOW + actionText
            ));
            item.setItemMeta(meta);
        }
        return item;
    }

    @EventHandler
    public static void handleMenuClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(GUI_TITLE)) {
            return;
        }

        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta()) return;

        String itemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

        // 🔹 檢查玩家是否已研究該科技
        if (TechStorage.getUnlockedTechs(player.getUniqueId()).contains(itemName)) {
            player.sendMessage(ChatColor.GREEN + "[科技] 你已經研究過這項科技了！");
            return;
        }

        if (itemName.equals("簡單機械")) {
            player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1f, 1f);
            SimpleMachinesGUI.openSimpleMachinesMenu(player); // 開啟簡單機械分頁
        } else if (itemName.equals("✖ 關閉")) {
            player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1f, 1f);
            player.closeInventory();
        } else {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 0.5f);
        }
    }


    public boolean hasUnlocked(Player player, NamespacedKey key) {
        return player.getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }
}
