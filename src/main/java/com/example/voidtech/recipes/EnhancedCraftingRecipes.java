package com.example.voidtech.recipes;

import org.bukkit.Material;
import java.util.HashMap;
import java.util.Map;

public class EnhancedCraftingRecipes {

    private static final Map<String, Map<Integer, Material>> recipes = new HashMap<>();

    static {
        // 定義鋸木機的合成配方
        // 配方為 3x3 格： 012
        //                      345
        //                      678
        // 空空空
        // 紅切紅
        // 石投石

        Map<Integer, Material> sawMachineRecipe = new HashMap<>();
        sawMachineRecipe.put(3, Material.REDSTONE);  // 紅石
        sawMachineRecipe.put(4, Material.STONECUTTER); // 切石機
        sawMachineRecipe.put(5, Material.REDSTONE);  // 紅石
        sawMachineRecipe.put(6, Material.STONE_SLAB); // 石半磚
        sawMachineRecipe.put(7, Material.DROPPER);  // 投擲器
        sawMachineRecipe.put(8, Material.STONE_SLAB); // 石半磚

        // 儲存配方，鍵是配方的名稱
        recipes.put("SawMachine", sawMachineRecipe);
    }

    public static Map<String, Map<Integer, Material>> getRecipes() {
        return recipes;
    }
}
