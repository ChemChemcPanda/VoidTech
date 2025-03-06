package com.example.voidtech.recipes;

import com.example.voidtech.items.TechResearchStationItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class TechResearchStationRecipe {

    public static void register(JavaPlugin plugin) {
        // 設定合成表的 Key
        NamespacedKey key = new NamespacedKey(plugin, "tech_research_station");

        // **先移除舊的合成表，避免重複註冊**
        Bukkit.removeRecipe(key);

        // 獲取 "科技研究站" 頭顱物品
        ItemStack researchStation = TechResearchStationItem.createResearchStation(plugin);

        // 設定合成表
        ShapedRecipe recipe = new ShapedRecipe(key, researchStation);
        recipe.shape("IRI", "QCQ", "IRI");

        // 設定材料對應
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('R', Material.REDSTONE);
        recipe.setIngredient('Q', Material.PAPER);
        recipe.setIngredient('C', Material.CRAFTING_TABLE);

        // 註冊合成配方
        Bukkit.addRecipe(recipe);
        plugin.getLogger().info("§a[系統] §f已成功註冊 '科技研究站' 合成表！");
    }
}
