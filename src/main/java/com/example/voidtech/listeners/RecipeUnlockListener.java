package com.example.voidtech.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class RecipeUnlockListener implements Listener {

    private final JavaPlugin plugin;

    public RecipeUnlockListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        NamespacedKey recipeKey = new NamespacedKey(plugin, "tech_research_station");

        // 給玩家合成表
        event.getPlayer().discoverRecipe(recipeKey);
    }
}
