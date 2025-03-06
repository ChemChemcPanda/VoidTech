package com.example.voidtech.gui;

import com.example.voidtech.VoidTech;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SimpleMachinesDetailGUI implements Listener {

    private static final Map<String, Material> researchIcons = SimpleMachinesGUI.researchIcons;

    public static void openResearchedDetailMenu(Player player, String techId) {
        boolean DEBUG = false; // 設為 true 就會顯示，false 就不會顯示

        if (DEBUG) {
            Bukkit.getLogger().info("科技ID: " + techId);
        }
        String techName = SimpleMachinesGUI.researchItems.getOrDefault(techId, techId);
        Inventory gui = Bukkit.createInventory(null, 27, "▍" + techName + " 詳情");

        // **科技說明書移至左上角 (10 格)**
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta bookMeta = book.getItemMeta();
        bookMeta.setDisplayName("§e科技說明");
        bookMeta.setLore(Arrays.asList(
                "§7此科技的用途與功能說明。"
        ));
        book.setItemMeta(bookMeta);
        gui.setItem(0, book);

        // **科技類別標識 (11 格)**
        ItemStack techCategory;
        if (isEnhancedCraftingTable(techId)) {
            techCategory = new ItemStack(Material.CRAFTING_TABLE);
            ItemMeta techCategoryMeta = techCategory.getItemMeta();
            techCategoryMeta.setDisplayName("§6強化合成台");
            techCategoryMeta.setLore(Arrays.asList(
                    "§7如右側方式擺放於強化合成台內",
                    "§7普通的工作台無法勝任此工作。"
            ));
            techCategory.setItemMeta(techCategoryMeta);
        } else {
            techCategory = new ItemStack(Material.BRICKS);
            ItemMeta techCategoryMeta = techCategory.getItemMeta();
            techCategoryMeta.setDisplayName("§6建造機械");
            techCategoryMeta.setLore(Arrays.asList(
                    "§7如右側方式擺放可建造機械"
            ));
            techCategory.setItemMeta(techCategoryMeta);
        }
        gui.setItem(13, techCategory);

        // **科技名稱標識 (13 格)**
        Material iconMaterial = researchIcons.getOrDefault(techId, Material.BARRIER);
        ItemStack nameIndicator = new ItemStack(iconMaterial);
        ItemMeta nameMeta = nameIndicator.getItemMeta();
        nameMeta.setDisplayName("§e" + techName);
        nameIndicator.setItemMeta(nameMeta);
        gui.setItem(10, nameIndicator);

        // **合成表顯示 (15, 16, 17, 24, 25, 26 格) - 依照科技擺放方式顯示**
        if (techId.equals("Saw_Table")) {
            gui.setItem(15, createItem(Material.REDSTONE, "§f紅石"));
            gui.setItem(16, createItem(Material.STONECUTTER, "§f切石機"));
            gui.setItem(17, createItem(Material.REDSTONE, "§f紅石"));

            gui.setItem(24, createItem(Material.STONE_SLAB, "§f石半磚"));
            gui.setItem(25, createItem(Material.DROPPER, "§f投擲器 (朝上)"));
            gui.setItem(26, createItem(Material.STONE_SLAB, "§f石半磚"));
        } else if (techId.equals("Enhanced_Crafting_Table")) {
            gui.setItem(16, createItem(Material.CRAFTING_TABLE, "§f工作台"));
            gui.setItem(25, createItem(Material.DROPPER, "§G投擲器 (朝上)"));
        } else if (techId.equals("Grinding_Machine")) {
            gui.setItem(16, createItem(Material.OAK_FENCE, "§f柵欄 (任意木製柵欄)"));
            gui.setItem(25, createItem(Material.DROPPER, "§f投擲器 (朝上)"));
        } else if (techId.startsWith("Water_Purifier")) {
            // 解析等級，例如 Water_Purifier1、Water_Purifier2
            int level = 1;
            try {
                level = Integer.parseInt(techId.replace("Water_Purifier", ""));
            } catch (NumberFormatException ignored) {}

            // 不同等級的合成方式
            if (level == 1) {
                gui.setItem(15, createItem(Material.IRON_BARS, "§f鐵欄杆"));
                gui.setItem(16, createItem(Material.BUCKET, "§f水桶"));
                gui.setItem(17, createItem(Material.IRON_BARS, "§f鐵欄杆"));

                gui.setItem(24, createItem(Material.IRON_BLOCK, "§f鐵方塊"));
                gui.setItem(25, createItem(Material.DROPPER, "§f投擲器 (朝上)"));
                gui.setItem(26, createItem(Material.IRON_BLOCK, "§f鐵方塊"));
            } else if (level == 2) {
                gui.setItem(15, createItem(Material.IRON_BARS, "§f強化鐵欄杆"));
                gui.setItem(16, createItem(Material.WATER_BUCKET, "§f強化水桶"));
                gui.setItem(17, createItem(Material.IRON_BARS, "§f強化鐵欄杆"));

                gui.setItem(24, createItem(Material.GOLD_BLOCK, "§f強化金方塊"));
                gui.setItem(25, createItem(Material.DROPPER, "§f強化投擲器 (朝上)"));
                gui.setItem(26, createItem(Material.GOLD_BLOCK, "§f強化金方塊"));
            }
        } else if (techId.startsWith("Storage_Unit_Lv")) {
            // 解析等級，例如 Water_Purifier1、Water_Purifier2
            int level = 1;
            try {
                level = Integer.parseInt(techId.replace("Storage_Unit_Lv", ""));
            } catch (NumberFormatException ignored) {}

            // 不同等級的合成方式
            if (level == 1) {
                gui.setItem(15, createItem(Material.IRON_BARS, "§f鐵欄杆"));
                gui.setItem(16, createItem(Material.BUCKET, "§f水桶"));
                gui.setItem(17, createItem(Material.IRON_BARS, "§f鐵欄杆"));

                gui.setItem(24, createItem(Material.IRON_BLOCK, "§f鐵方塊"));
                gui.setItem(25, createItem(Material.DROPPER, "§f投擲器 (朝上)"));
                gui.setItem(26, createItem(Material.IRON_BLOCK, "§f鐵方塊"));
            } else if (level == 2) {
                gui.setItem(15, createItem(Material.IRON_BARS, "§f強化鐵欄杆"));
                gui.setItem(16, createItem(Material.WATER_BUCKET, "§f強化水桶"));
                gui.setItem(17, createItem(Material.IRON_BARS, "§f強化鐵欄杆"));

                gui.setItem(24, createItem(Material.GOLD_BLOCK, "§f強化金方塊"));
                gui.setItem(25, createItem(Material.DROPPER, "§f強化投擲器 (朝上)"));
                gui.setItem(26, createItem(Material.GOLD_BLOCK, "§f強化金方塊"));
            }
        }

        // **返回按鈕 (18 格)**
        ItemStack backButton = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backButton.getItemMeta();
        backMeta.setDisplayName("§a返回");
        backButton.setItemMeta(backMeta);
        gui.setItem(18, backButton);

        player.openInventory(gui);
    }



    private static boolean isEnhancedCraftingTable(String techId) {
        List<String> enhancedCraftingTables = Arrays.asList(
                "Saw_Table", "Water_Purifier", "Storage_Unit_Lv"
        );
        return enhancedCraftingTables.contains(techId) ||
                techId.startsWith("Water_Purifier") ||
                techId.startsWith("Storage_Unit_Lv");
    }

    private static ItemStack createItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().contains("詳情")) {
            event.setCancelled(true); // 禁止移動物品
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || !clickedItem.hasItemMeta()) return;

            Player player = (Player) event.getWhoClicked();
            String itemName = clickedItem.getItemMeta().getDisplayName();

            // 返回功能
            if (itemName.equals("§a返回")) {
                player.playSound(player.getLocation(), org.bukkit.Sound.ITEM_BOOK_PAGE_TURN, 1f, 1f);
                SimpleMachinesGUI.openSimpleMachinesMenu(player);
            }
        }
    }
}