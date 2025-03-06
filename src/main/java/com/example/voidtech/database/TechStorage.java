package com.example.voidtech.database;

import com.example.voidtech.VoidTech;
import com.example.voidtech.gui.SimpleMachinesGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class TechStorage {

    // 查詢玩家已解鎖的科技
    public static Set<String> getUnlockedTechs(UUID uuid) {
        Set<String> techs = new HashSet<>();
        String query = "SELECT tech_id FROM player_tech WHERE uuid = ? AND unlocked = TRUE";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                techs.add(rs.getString("tech_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return techs;
    }

    // 解鎖玩家科技
    public static void unlockPlayerTech(UUID uuid, String techId) {
        if ("ALL".equalsIgnoreCase(techId) || "*".equals(techId)) {
            // 解鎖所有科技
            String query = "INSERT INTO player_tech (uuid, tech_id, unlocked) " +
                    "SELECT ?, tech_id, TRUE FROM tech_list " +
                    "ON CONFLICT(uuid, tech_id) DO UPDATE SET unlocked = TRUE;";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, uuid.toString());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // 單獨解鎖科技
            String query = "INSERT INTO player_tech (uuid, tech_id, unlocked) VALUES (?, ?, ?) " +
                    "ON CONFLICT(uuid, tech_id) DO UPDATE SET unlocked = TRUE;";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, uuid.toString());
                stmt.setString(2, techId);
                stmt.setBoolean(3, true);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        reloadPlayerResearch(uuid);
    }

    // 🔹 加載所有玩家的研究狀態
    public static Map<String, Boolean> loadAllResearchStatus(UUID uuid) {
        Map<String, Boolean> researchStatus = new HashMap<>();
        String query = "SELECT tech_id, unlocked FROM player_tech WHERE uuid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                researchStatus.put(rs.getString("tech_id"), rs.getBoolean("unlocked"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return researchStatus;
    }

    // 🔹 勢力科技解鎖
    public static void unlockFactionTech(String factionName, String techId) {
        String query = "INSERT INTO faction_tech (faction_name, tech_id, unlocked) VALUES (?, ?, ?) ON CONFLICT(faction_name, tech_id) DO UPDATE SET unlocked = TRUE;";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, factionName);
            stmt.setString(2, techId);
            stmt.setBoolean(3, true);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔹 查詢勢力已解鎖的科技
    public static Set<String> getUnlockedFactionTechs(String factionName) {
        Set<String> techs = new HashSet<>();
        String query = "SELECT tech_id FROM faction_tech WHERE faction_name = ? AND unlocked = TRUE";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, factionName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                techs.add(rs.getString("tech_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return techs;
    }

    public static void removePlayerTech(UUID uuid, String techId) {
        if ("ALL".equalsIgnoreCase(techId) || "*".equals(techId)) {
            String query = "DELETE FROM player_tech WHERE uuid = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, uuid.toString());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            String query = "DELETE FROM player_tech WHERE uuid = ? AND tech_id = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, uuid.toString());
                stmt.setString(2, techId);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        reloadPlayerResearch(uuid);
    }

    public static void reloadPlayerResearch(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return; // 玩家不在線，跳過

        // ✅ 清除暫存的研究狀態
        SimpleMachinesGUI.getResearchStatus().entrySet().removeIf(entry -> entry.getKey().startsWith(player.getName() + "_"));

        // ✅ 重新從資料庫載入
        Map<String, Boolean> researchStatus = TechStorage.loadAllResearchStatus(uuid);
        for (Map.Entry<String, Boolean> entry : researchStatus.entrySet()) {
            SimpleMachinesGUI.getResearchStatus().put(player.getName() + "_" + entry.getKey(), entry.getValue());
        }
    }

    public static List<String> getAllTechs() {
        return new ArrayList<>(SimpleMachinesGUI.researchItems.keySet()); // ✅ 確保回傳完整科技 ID
    }
}
