package com.example.voidtech.database;

import com.example.voidtech.VoidTech;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static Connection connection;

    public static void connect() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    return; // 連線有效，不需重新連線
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        String url = "jdbc:sqlite:plugins/VoidTech/voidtech.db"; // SQLite 資料庫位置
        try {
            connection = DriverManager.getConnection(url);
            createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private static void createTables() {
        try (Statement stmt = connection.createStatement()) {
            String playerTechTable = "CREATE TABLE IF NOT EXISTS player_tech (" +
                    "uuid TEXT NOT NULL," +
                    "tech_id TEXT NOT NULL," +
                    "unlocked BOOLEAN DEFAULT FALSE," +
                    "PRIMARY KEY (uuid, tech_id));";
            stmt.execute(playerTechTable);

            String factionTechTable = "CREATE TABLE IF NOT EXISTS faction_tech (" +
                    "faction_name TEXT NOT NULL," +
                    "tech_id TEXT NOT NULL," +
                    "unlocked BOOLEAN DEFAULT FALSE," +
                    "PRIMARY KEY (faction_name, tech_id));";
            stmt.execute(factionTechTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
                VoidTech.getInstance().getLogger().info("資料庫連線已關閉！");
            }
        } catch (SQLException e) {
            VoidTech.getInstance().getLogger().severe("關閉資料庫時發生錯誤：" + e.getMessage());
        }
    }
}
