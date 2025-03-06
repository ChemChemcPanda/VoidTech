package com.example.voidtech;

import com.example.voidtech.commands.FTechCommand;
import com.example.voidtech.commands.TechCommand;
import com.example.voidtech.commands.TechTabCompleter;
import com.example.voidtech.database.DatabaseManager;
import com.example.voidtech.database.TechStorage;
import com.example.voidtech.gui.SimpleMachinesDetailGUI;
import com.example.voidtech.gui.SimpleMachinesGUI;
import com.example.voidtech.gui.TechResearchGUI;
import com.example.voidtech.listeners.*;
import com.example.voidtech.machines.EnhancedCraftingTable;
import com.example.voidtech.recipes.TechResearchStationRecipe;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class VoidTech extends JavaPlugin {
    private TechManager techManager;
    private static VoidTech instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("[VoidTech] 插件已啟動！");
        this.techManager = new TechManager();

        DatabaseManager.connect(); // ✅ 確保連接資料庫

        // ✅ 載入所有玩家的科技研究狀態（但不強制開啟 GUI）
        int loadedPlayers = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            TechStorage.reloadPlayerResearch(player.getUniqueId()); // ✅ 獨立方法重新載入玩家的研究資料
            loadedPlayers++;
        }
        if (loadedPlayers > 0) {
            getLogger().info("[VoidTech] 已成功刷新 " + loadedPlayers + " 位玩家的科技狀態！");
        }

        // ✅ 註冊指令 & 事件
        registerCommands();
        registerEvents();

        // ✅ 儲存預設設定檔
        saveDefaultConfig();

        // ✅ 註冊合成配方
        TechResearchStationRecipe.register(this);

        // ✅ 註冊登入事件，讓玩家獲得合成表
        getServer().getPluginManager().registerEvents(new RecipeUnlockListener(this), this);
    }

    @Override
    public void onDisable() {
        DatabaseManager.disconnect();
        getLogger().info("[VoidTech] 插件已卸載！");
        instance = null;
    }

    public static VoidTech getInstance() {
        return instance;
    }

    private void registerCommands() {
        if (getCommand("tech") != null) {
            getCommand("tech").setTabCompleter(new TechTabCompleter());
            getCommand("tech").setExecutor(new TechCommand(techManager));
        }
        if (getCommand("ftech") != null) {
            getCommand("ftech").setExecutor(new FTechCommand());
        }
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new TechResearchStationListener(this), this);
        getServer().getPluginManager().registerEvents(new TechResearchStationInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new TechResearchStationBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new TechResearchGUI(), this);
        getServer().getPluginManager().registerEvents(new SimpleMachinesGUI(), this);
        getServer().getPluginManager().registerEvents(new SimpleMachinesDetailGUI(), this);
        getServer().getPluginManager().registerEvents(new EnhancedCraftingTable(), this);
        getServer().getPluginManager().registerEvents(new EnhancedCraftingTableListener(), this);
        getServer().getPluginManager().registerEvents(new SawMachineUseListener(), this);
    }
}
