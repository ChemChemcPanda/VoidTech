package com.example.voidtech.gui;

import com.example.voidtech.VoidTech;
import com.example.voidtech.database.TechStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.Set;

public class SimpleMachinesGUI implements Listener {

    private static final String GUI_TITLE = ChatColor.BOLD + "" + ChatColor.GOLD + "▍簡單機械 研究";

    public static final Map<String, String> researchItems = new LinkedHashMap<>();// 科技代碼 -> 中文名稱
    static final Map<String, Material> researchIcons = new LinkedHashMap<>(); // 科技代碼 -> Material
    private static final Map<String, String> researchDescriptions = new LinkedHashMap<>();
    private static final Map<String, Boolean> researchStatus = new HashMap<>(); // 存儲玩家的研究狀態
    private static final Map<String, List<ItemStack>> researchMaterials = new HashMap<>();
    private static final Map<String, List<String>> researchRequirements = new HashMap<>();

    static {
        researchItems.put("Enhanced_Crafting_Table", "強化合成台");
        researchIcons.put("Enhanced_Crafting_Table", Material.CRAFTING_TABLE);
        researchDescriptions.put("Enhanced_Crafting_Table", "更高效的合成系統，可用於機械設備製造");
        researchMaterials.put("Enhanced_Crafting_Table", Arrays.asList(
                new ItemStack(Material.CRAFTING_TABLE, 1),
                new ItemStack(Material.DISPENSER, 1)
        ));

        researchItems.put("Grinding_Machine", "研磨機");
        researchIcons.put("Grinding_Machine", Material.GRINDSTONE);
        researchDescriptions.put("Grinding_Machine", "將礫石、骨頭、骨塊、烈焰棒等物品磨碎");
        researchRequirements.put("Grinding_Machine", Arrays.asList("Enhanced_Crafting_Table"));
        researchMaterials.put("Grinding_Machine", Arrays.asList(
                new ItemStack(Material.IRON_INGOT, 2),
                new ItemStack(Material.REDSTONE, 4)
        ));

        researchItems.put("Saw_Table", "鋸木台");
        researchIcons.put("Saw_Table", Material.STONECUTTER);
        researchDescriptions.put("Saw_Table", "分解木材，提高木材利用率");
        researchRequirements.put("Saw_Table", Arrays.asList("Enhanced_Crafting_Table"));
        researchMaterials.put("Saw_Table", Arrays.asList(
                new ItemStack(Material.IRON_INGOT, 2),
                new ItemStack(Material.REDSTONE, 4)
        ));

        researchItems.put("Advanced_Furnace", "冶煉爐");
        researchIcons.put("Advanced_Furnace", Material.BLAST_FURNACE);
        researchDescriptions.put("Advanced_Furnace", "燒製粉狀礦粉成可用的礦石");
        researchRequirements.put("Advanced_Furnace", Arrays.asList("Ore_Crusher"));
        researchMaterials.put("Advanced_Furnace", Arrays.asList(
                new ItemStack(Material.IRON_INGOT, 2),
                new ItemStack(Material.REDSTONE, 4)
        ));

        researchItems.put("Energy_Refinery", "燃料精煉機");
        researchIcons.put("Energy_Refinery", Material.LAVA_BUCKET);
        researchDescriptions.put("Energy_Refinery", "提煉更高效的燃料，提高能量利用率");
        researchRequirements.put("Energy_Refinery", Arrays.asList("Enhanced_Crafting_Table"));
        researchMaterials.put("Energy_Refinery", Arrays.asList(
                new ItemStack(Material.IRON_INGOT, 2),
                new ItemStack(Material.REDSTONE, 4)
        ));

        researchItems.put("Item_Compressor", "物品壓縮機");
        researchIcons.put("Item_Compressor", Material.PISTON);
        researchDescriptions.put("Item_Compressor", "壓縮大量資源，使其更易存儲");
        researchRequirements.put("Item_Compressor", Arrays.asList("Enhanced_Crafting_Table"));
        researchMaterials.put("Item_Compressor", Arrays.asList(
                new ItemStack(Material.IRON_INGOT, 2),
                new ItemStack(Material.REDSTONE, 4)
        ));

        researchItems.put("Ore_Crusher", "礦石粉碎機");
        researchIcons.put("Ore_Crusher", Material.ANVIL);
        researchDescriptions.put("Ore_Crusher", "粉碎礦石，增加獲取資源的機率");
        researchRequirements.put("Ore_Crusher", Arrays.asList("Enhanced_Crafting_Table"));
        researchMaterials.put("Ore_Crusher", Arrays.asList(
                new ItemStack(Material.IRON_INGOT, 2),
                new ItemStack(Material.REDSTONE, 4)
        ));

        researchItems.put("Power_Core", "動力核心");
        researchIcons.put("Power_Core", Material.REDSTONE_BLOCK);
        researchDescriptions.put("Power_Core", "簡單機械的能量來源，可提供機械運行動力");
        researchRequirements.put("Power_Core", Arrays.asList("Enhanced_Crafting_Table"));
        researchMaterials.put("Power_Core", Arrays.asList(
                new ItemStack(Material.IRON_INGOT, 2),
                new ItemStack(Material.REDSTONE, 4)
        ));

        researchItems.put("Conveyor_Belt", "輸送帶");
        researchIcons.put("Conveyor_Belt", Material.HOPPER);
        researchDescriptions.put("Conveyor_Belt", "自動搬運物品，提高生產效率");
        researchRequirements.put("Conveyor_Belt", Arrays.asList("Enhanced_Crafting_Table"));
        researchMaterials.put("Conveyor_Belt", Arrays.asList(
                new ItemStack(Material.IRON_INGOT, 2),
                new ItemStack(Material.REDSTONE, 4)
        ));

        researchItems.put("Scrap_Recycling", "廢料回收站");
        researchIcons.put("Scrap_Recycling", Material.IRON_BARS);
        researchDescriptions.put("Scrap_Recycling", "回收廢棄物品，轉換為可用資源");
        researchRequirements.put("Scrap_Recycling", Arrays.asList("Enhanced_Crafting_Table"));
        researchMaterials.put("Scrap_Recycling", Arrays.asList(
                new ItemStack(Material.IRON_INGOT, 2),
                new ItemStack(Material.REDSTONE, 4)
        ));

        researchItems.put("Power_Transmission", "動力傳輸");
        researchIcons.put("Power_Transmission", Material.IRON_BLOCK);
        researchDescriptions.put("Power_Transmission", "學習如何將動力傳輸至不同機械");
        researchRequirements.put("Power_Transmission", Arrays.asList("Enhanced_Crafting_Table"));
        researchMaterials.put("Power_Transmission", Arrays.asList(
                new ItemStack(Material.IRON_INGOT, 2),
                new ItemStack(Material.REDSTONE, 4)
        ));

        for (int i = 1; i <= 2; i++) {
            String techId = "Storage_Unit_Lv" + i;
            researchItems.put(techId, "倉儲單元 Lv." + i);
            researchIcons.put(techId, Material.CHEST);
            researchDescriptions.put(techId, "大型儲存裝置，可存放更多物品 " + i);

            if (i == 1) {
                researchRequirements.put(techId, Arrays.asList("Enhanced_Crafting_Table"));
            } else {
                researchRequirements.put(techId, Arrays.asList("Storage_Unit_Lv" + (i - 1)));
            }

            researchMaterials.put(techId, Arrays.asList(
                    new ItemStack(Material.IRON_INGOT, 2 * i),
                    new ItemStack(Material.REDSTONE, 4 * i),
                    new ItemStack(Material.CHEST, 2 * i)
            ));
        }

        for (int i = 1; i <= 12; i++) {
            String techId = "Enhanced_Furnace_Lv" + i;
            researchItems.put(techId, "強化熔爐 Lv." + i);
            researchIcons.put(techId, Material.FURNACE);
            researchDescriptions.put(techId, "更高效的熔煉系統，熔煉速度提升至等級 " + i);

            if (i == 1) {
                researchRequirements.put(techId, Arrays.asList("Enhanced_Crafting_Table", "Advanced_Furnace"));
            } else {
                researchRequirements.put(techId, Arrays.asList("Enhanced_Furnace_Lv" + (i - 1)));
            }

            researchMaterials.put(techId, Arrays.asList(
                    new ItemStack(Material.IRON_INGOT, 2 * i),
                    new ItemStack(Material.REDSTONE, 4 * i)
            ));
        }

        for (int i = 1; i <= 2; i++) {
            String techId = "Water_Purifier" + i;
            researchItems.put(techId, "淨水器 Lv." + i);
            researchIcons.put(techId, Material.WATER_BUCKET);
            researchDescriptions.put("淨水器 Lv." + i, "淨水效率提升至等級 " + i + "，可更快過濾污染水");

            if (i == 1) {
                researchRequirements.put(techId, Arrays.asList("Enhanced_Crafting_Table"));
            } else {
                researchRequirements.put(techId, Arrays.asList("Water_Purifier" + (i - 1)));
            }

            researchMaterials.put(techId, Arrays.asList(
                    new ItemStack(Material.GLASS, 4 + (i * 2)),
                    new ItemStack(Material.WATER_BUCKET, 2),
                    new ItemStack(Material.IRON_INGOT, 2 * i)
            ));
        }
    }

    public static List<String> getTechRequirements(String techId) {
        return researchRequirements.getOrDefault(techId, Collections.emptyList());
    }

    public static void openSimpleMachinesMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, GUI_TITLE);

        int slot = 0;
        for (Map.Entry<String, String> entry : researchItems.entrySet()) {
            String techId = entry.getKey(); // 科技 ID
            String techName = entry.getValue(); // 中文名稱
            Material displayMaterial = researchIcons.getOrDefault(techId, Material.BARRIER);
            String description = researchDescriptions.getOrDefault(techId, "未知技術");

            boolean isResearched = researchStatus.getOrDefault(player.getName() + "_" + techId, false);
            Material icon = isResearched ? displayMaterial : Material.RED_STAINED_GLASS_PANE;
            String researchState = isResearched ? ChatColor.GREEN + "已研究" : ChatColor.RED + "未研究";
            String actionText = isResearched ? "點擊查看更多" : "點擊進行研究";

            gui.setItem(slot, createMenuItem(icon, ChatColor.YELLOW + techName, researchState, description, actionText, techId, player));
            slot++;
        }

        // **返回按鈕**
        gui.setItem(45, createMenuItem(Material.ARROW, ChatColor.GREEN + "返回", "", "返回科技研究站", "點擊返回", null, player));

        // **關閉按鈕**
        gui.setItem(49, createMenuItem(Material.BARRIER, ChatColor.RED + "✖ 關閉", "", "點擊關閉科技研究站", "點擊關閉", null, player));

        // **填滿灰色玻璃**
        ItemStack grayGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassMeta = grayGlass.getItemMeta();
        if (glassMeta != null) {
            glassMeta.setDisplayName(ChatColor.GRAY + " ");
            grayGlass.setItemMeta(glassMeta);
        }
        for (int i = 46; i < 54; i++) {
            if (i != 49) {
                gui.setItem(i, grayGlass);
            }
        }

        player.openInventory(gui);
    }

    private static ItemStack createMenuItem(Material material, String name, String status, String description, String actionText, String techId, Player player) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);

            // **Lore（顯示資訊）**
            List<String> lore = new ArrayList<>();
            lore.add(status); // **顯示已研究 / 未研究**
            lore.add(ChatColor.GRAY + description); // **顯示描述**
            lore.add("");

            // **檢查玩家是否已研究該科技**
            boolean isResearched = researchStatus.getOrDefault(player.getName() + "_" + techId, false);

            if (!isResearched) {  // **只有未研究時才顯示前置科技與材料**
                // **顯示前置科技**
                if (researchRequirements.containsKey(techId)) {
                    lore.add(ChatColor.GOLD + "📌 需要前置科技:");
                    for (String requirement : researchRequirements.get(techId)) {
                        lore.add(ChatColor.YELLOW + " - " + researchItems.getOrDefault(requirement, requirement));
                    }
                    lore.add("");
                }

                // **顯示研究材料**
                if (researchMaterials.containsKey(techId)) {
                    lore.add(ChatColor.BLUE + "🔹 需要材料:");
                    for (ItemStack materialStack : researchMaterials.get(techId)) {
                        lore.add(ChatColor.AQUA + " - " + ChatColor.WHITE + getChineseMaterialName(materialStack.getType()) +
                                " × " + materialStack.getAmount());
                    }
                    lore.add("");
                }
            }

            // **顯示操作提示**
            if (isResearched) {
                lore.add(ChatColor.YELLOW + "📖 點擊查看更多");
            } else {
                lore.add(ChatColor.YELLOW + "⚡ 點擊進行研究");
            }

            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    @EventHandler
    public void handleMenuClick(InventoryClickEvent event) {
        if (!ChatColor.stripColor(event.getView().getTitle()).equals(ChatColor.stripColor(GUI_TITLE))) {
            return;
        }

        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null || !clickedItem.hasItemMeta()) return;

        String itemName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
        String techId = researchItems.entrySet().stream()
                .filter(entry -> entry.getValue().equals(itemName))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        if (techId != null) {
            // ✅ 研究狀態要從 `TechStorage` 重新讀取，確保資料正確
            boolean isResearched = TechStorage.getUnlockedTechs(player.getUniqueId()).contains(techId);

            if (isResearched) {
                SimpleMachinesDetailGUI.openResearchedDetailMenu(player, techId);
                player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1f, 1f);
                return;
            }

            // **檢查前置科技**
            if (researchRequirements.containsKey(techId)) {
                List<String> requiredTechs = researchRequirements.get(techId);
                for (String requiredTech : requiredTechs) {
                    if (!TechStorage.getUnlockedTechs(player.getUniqueId()).contains(requiredTech)) {
                        player.sendMessage(ChatColor.RED + "[科技] ⚠ 你需要 " + ChatColor.WHITE + researchItems.getOrDefault(requiredTech, requiredTech) + ChatColor.RED + " 才能研究此科技！");
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 0.5f);
                        return;
                    }
                }
            }

            // **檢查玩家是否擁有足夠的材料**
            List<ItemStack> requiredMaterials = researchMaterials.get(techId);
            List<String> missingMaterials = new ArrayList<>();

            if (requiredMaterials != null) {
                for (ItemStack material : requiredMaterials) {
                    if (!player.getInventory().containsAtLeast(material, material.getAmount())) {
                        missingMaterials.add(ChatColor.WHITE + getChineseMaterialName(material.getType()) + " × " + material.getAmount());
                    }
                }

                if (!missingMaterials.isEmpty()) {
                    player.sendMessage(ChatColor.RED + "[科技] ⚠ 你需要 " + String.join(ChatColor.RED + "、", missingMaterials) + ChatColor.RED + " 才能研究此科技！");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 0.5f);
                    return;
                }

                // **扣除材料**
                for (ItemStack material : requiredMaterials) {
                    player.getInventory().removeItem(material);
                }
            }

            // **研究成功**
            TechStorage.unlockPlayerTech(player.getUniqueId(), techId);
            researchStatus.put(player.getName() + "_" + techId, true); // ✅ 更新研究狀態

            // ✅ 研究成功後直接刷新 GUI，不發送訊息
            Bukkit.getScheduler().runTaskLater(VoidTech.getInstance(), () -> openSimpleMachinesMenu(player), 2);
        }
        else if (itemName.equals("返回")) {
            player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1f, 1f);
            TechResearchGUI.openTechMenu(player);
        }
        else if (itemName.equals("✖ 關閉")) {
            player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1f, 1f);
            player.closeInventory();
        }
    }


    private static final Map<Material, String> materialNames = new HashMap<>();

    static {
        materialNames.put(Material.CRAFTING_TABLE, "工作台");
        materialNames.put(Material.DISPENSER, "發射器");
        materialNames.put(Material.IRON_INGOT, "鐵錠");
        materialNames.put(Material.REDSTONE, "紅石");
        materialNames.put(Material.GRINDSTONE, "研磨機");
        materialNames.put(Material.STONECUTTER, "鋸木台");
        materialNames.put(Material.BLAST_FURNACE, "高爐");
        materialNames.put(Material.LAVA_BUCKET, "熔岩桶");
        materialNames.put(Material.PISTON, "活塞");
        materialNames.put(Material.ANVIL, "鐵砧");
        materialNames.put(Material.REDSTONE_BLOCK, "紅石塊");
        materialNames.put(Material.HOPPER, "漏斗");
        materialNames.put(Material.IRON_BARS, "鐵柵欄");
        materialNames.put(Material.IRON_BLOCK, "鐵磚");
        materialNames.put(Material.WATER_BUCKET, "水桶");
        materialNames.put(Material.CHEST, "箱子");
        materialNames.put(Material.GLASS, "玻璃");
        materialNames.put(Material.FURNACE, "熔爐");
        materialNames.put(Material.BARREL, "鐵桶");
        materialNames.put(Material.OBSERVER, "偵測器");
    }

    // **取得對應的中文名稱**
    static String getChineseMaterialName(Material material) {
        return materialNames.getOrDefault(material, material.name()); // 沒有對應則回傳英文
    }

    public static Map<String, Boolean> getResearchStatus() {
        return researchStatus;
    }

    public static void updateResearchStatus(Player player, String techId, boolean status) {
        researchStatus.put(player.getName() + "_" + techId, status);
    }

    public static void setResearchStatus(String playerName, Map<String, Boolean> status) {
        researchStatus.clear();
        for (Map.Entry<String, Boolean> entry : status.entrySet()) {
            researchStatus.put(playerName + "_" + entry.getKey(), entry.getValue());
        }
    }

    // ✅ 新增方法：返回所有科技 ID
    public static Set<String> getAllTechs() {
        return researchItems.keySet(); // 取得所有科技 ID
    }
}
