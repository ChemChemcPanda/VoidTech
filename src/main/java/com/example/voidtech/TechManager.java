package com.example.voidtech;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TechManager {
    private final HashMap<UUID, Integer> playerTP = new HashMap<>();
    private final HashMap<String, Integer> factionFTP = new HashMap<>();

    // 獲取玩家科技點數（TP）
    public int getPlayerTP(Player player) {
        return playerTP.getOrDefault(player.getUniqueId(), 0);
    }

    // 設定玩家科技點數（TP）
    public void setPlayerTP(Player player, int amount) {
        playerTP.put(player.getUniqueId(), amount);
    }

    // 增加玩家科技點數（TP）
    public void addPlayerTP(Player player, int amount) {
        int currentTP = getPlayerTP(player);
        playerTP.put(player.getUniqueId(), currentTP + amount);
    }

    // 消耗玩家科技點數（TP）
    public boolean removePlayerTP(Player player, int amount) {
        int currentTP = getPlayerTP(player);
        if (currentTP >= amount) {
            playerTP.put(player.getUniqueId(), currentTP - amount);
            return true;
        }
        return false;
    }

    // 獲取勢力科技點數（FTP）
    public int getFactionFTP(String factionName) {
        return factionFTP.getOrDefault(factionName, 0);
    }

    // 設定勢力科技點數（FTP）
    public void setFactionFTP(String factionName, int amount) {
        factionFTP.put(factionName, amount);
    }

    // 增加勢力科技點數（FTP）
    public void addFactionFTP(String factionName, int amount) {
        int currentFTP = getFactionFTP(factionName);
        factionFTP.put(factionName, currentFTP + amount);
    }

    // 消耗勢力科技點數（FTP）
    public boolean removeFactionFTP(String factionName, int amount) {
        int currentFTP = getFactionFTP(factionName);
        if (currentFTP >= amount) {
            factionFTP.put(factionName, currentFTP - amount);
            return true;
        }
        return false;
    }
}
