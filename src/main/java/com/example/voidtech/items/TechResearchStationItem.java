package com.example.voidtech.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class TechResearchStationItem {

    private static final String TEXTURE_URL = "http://textures.minecraft.net/texture/899f882b4ecca4a372692a7ae1cd08bdf340700337240d00419168588d98bb75";

    public static ItemStack createResearchStation(JavaPlugin plugin) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();

        if (meta != null) {
            // 設定物品名稱
            meta.setDisplayName("§b科技研究站");

            // 設定物品說明（Lore）
            List<String> lore = Arrays.asList(
                    "§7一個高科技的研究設備",
                    "§7可用於科技點數研究與開發",
                    " ",
                    "§c放置於 §6工作台 §c上來啟動"
            );
            meta.setLore(lore);

            // 設定物品 NBT 標記
            NamespacedKey key = new NamespacedKey(plugin, "tech_research_station");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "true");

            // 設定頭顱皮膚
            PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
            PlayerTextures textures = profile.getTextures();
            try {
                textures.setSkin(new URL(TEXTURE_URL));
                profile.setTextures(textures);
                meta.setOwnerProfile(profile);
            } catch (MalformedURLException e) {
                plugin.getLogger().log(Level.SEVERE, "無法解析頭顱貼圖 URL", e);
            }

            skull.setItemMeta(meta);
        }

        return skull;
    }
}
