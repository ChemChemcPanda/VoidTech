package com.example.voidtech.machines;

import com.example.voidtech.database.TechStorage;
import com.example.voidtech.recipes.EnhancedCraftingRecipes;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dropper;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class EnhancedCraftingTable implements Listener {

    private static final String REQUIRED_TECH = "Enhanced_Crafting_Table";
    private static final String SAW_TABLE_TECH = "Saw_Table"; // 鋸木台科技

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if (block == null || block.getType() != Material.CRAFTING_TABLE) return;

        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        Set<String> unlockedTechs = TechStorage.getUnlockedTechs(playerUUID);

        if (!unlockedTechs.contains(REQUIRED_TECH)) {
            player.sendMessage("§c[系統] 你尚未解鎖§e「強化合成台」§c科技，無法使用此設備！");
            return;
        }

        Block below = block.getRelative(BlockFace.DOWN);
        if (below.getType() != Material.DROPPER) return;

        BlockData data = below.getBlockData();
        if (data instanceof Directional) {
            Directional directional = (Directional) data;
            if (directional.getFacing() != BlockFace.UP) {
                player.sendMessage("§c[系統] 投擲器必須朝上！");
                return;
            }
        }

        Dropper dropper = (Dropper) below.getState();
        Inventory dropperInventory = dropper.getInventory();

        if (!unlockedTechs.contains(SAW_TABLE_TECH)) {
            player.sendMessage("§c[系統] 你尚未解鎖§e「鋸木台」§c科技，無法使用此設備！");
            return;
        }

        // 檢查是否符合鋸木台的配方
        ItemStack craftedItem = checkRecipe(dropperInventory);
        if (craftedItem != null) {
            player.getInventory().addItem(craftedItem);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
            return;
        }

        player.sendMessage("§c[系統] 投擲器內的材料不符合任何配方！");
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
    }

    // 檢查是否符合配方（固定擺放）
    private ItemStack checkRecipe(Inventory inventory) {
        ItemStack[] contents = inventory.getContents();

        for (Map.Entry<String, Map<Integer, Material>> entry : EnhancedCraftingRecipes.getRecipes().entrySet()) {
            Map<Integer, Material> required = entry.getValue();

            if (matchesRecipe(contents, required)) {
                removeMaterials(inventory, required);
                return entry.getKey().equals("SawMachine") ? SawMachine.createSawMachine() : null;
            }
        }
        return null;
    }

    // 判斷是否符合配方
    private boolean matchesRecipe(ItemStack[] inventory, Map<Integer, Material> required) {
        for (Map.Entry<Integer, Material> entry : required.entrySet()) {
            int slot = entry.getKey();
            Material expectedMaterial = entry.getValue();

            if (inventory[slot] == null || inventory[slot].getType() != expectedMaterial) {
                return false;
            }
        }
        return true;
    }

    // 移除材料
    private void removeMaterials(Inventory inventory, Map<Integer, Material> recipe) {
        for (Map.Entry<Integer, Material> entry : recipe.entrySet()) {
            int slot = entry.getKey();
            inventory.setItem(slot, null);
        }
    }
}
