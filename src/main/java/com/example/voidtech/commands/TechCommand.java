package com.example.voidtech.commands;

import com.example.voidtech.TechManager;
import com.example.voidtech.database.TechStorage;
import com.example.voidtech.gui.SimpleMachinesGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TechCommand implements CommandExecutor {
    private final TechManager techManager;

    public TechCommand(TechManager techManager) {
        this.techManager = techManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "[系統] 使用: /tech unlock <玩家> <科技名稱|ALL>, /tech removetech <玩家> <科技名稱|ALL>");
            return true;
        }

        Player target = null; // 讓所有指令共用這個變數
        String techId;

        switch (args[0].toLowerCase()) {
            case "info":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "[系統] 只有玩家可以使用此指令！");
                    return true;
                }
                Player player = (Player) sender;
                int tp = techManager.getPlayerTP(player);
                player.sendMessage(ChatColor.GREEN + "[系統] 你的科技點數（TP）： " + tp);
                break;

            case "unlock":
                if (args.length < 3) {
                    sender.sendMessage("§e[系統] §f使用方法: /tech unlock <玩家> <科技名稱 | ALL>");
                    return true;
                }

                target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage("§c[系統] 找不到玩家 §e" + args[1]);
                    return true;
                }

                techId = args[2];

                if (techId.equalsIgnoreCase("ALL") || techId.equalsIgnoreCase("*")) {
                    // ✅ 解鎖所有科技
                    for (String tech : SimpleMachinesGUI.getAllTechs()) {
                        TechStorage.unlockPlayerTech(target.getUniqueId(), tech);
                    }
                    sender.sendMessage("§a[系統] §b" + target.getName() + " §f的所有科技已成功解鎖！");
                    target.sendMessage("§a[系統] 你的所有科技已被解鎖！");
                } else {
                    if (TechStorage.getUnlockedTechs(target.getUniqueId()).contains(techId)) {
                        sender.sendMessage("§c[系統] §b" + target.getName() + " §f已經解鎖科技 §e" + techId + "§f！");
                        return true;
                    }

                    TechStorage.unlockPlayerTech(target.getUniqueId(), techId);
                    sender.sendMessage("§a[系統] §b" + target.getName() + " §f已成功解鎖 §e" + techId + " §f科技！");
                    target.sendMessage("§a[系統] 你已成功研究科技 §e" + techId + "§f！");
                }
                break;

            case "removetech":
                if (args.length < 3) {
                    sender.sendMessage("§e[系統] §f使用方法: /tech removetech <玩家> <科技名稱 | ALL>");
                    return true;
                }

                target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage("§c[系統] 找不到玩家 §e" + args[1]);
                    return true;
                }

                techId = args[2];

                if (techId.equalsIgnoreCase("ALL") || techId.equalsIgnoreCase("*")) {
                    // ✅ 移除所有科技
                    for (String tech : TechStorage.getUnlockedTechs(target.getUniqueId())) {
                        TechStorage.removePlayerTech(target.getUniqueId(), tech);
                    }
                    sender.sendMessage("§a[系統] §b" + target.getName() + " §f的所有科技已被移除！");
                    target.sendMessage("§c[系統] 你的所有科技已被移除！");
                } else {
                    if (!TechStorage.getUnlockedTechs(target.getUniqueId()).contains(techId)) {
                        sender.sendMessage("§c[系統] §b" + target.getName() + " §f未解鎖科技 §e" + techId + "§f，無法移除！");
                        return true;
                    }

                    TechStorage.removePlayerTech(target.getUniqueId(), techId);
                    sender.sendMessage("§a[系統] §b" + target.getName() + " §f的科技 §e" + techId + " §f已被移除！");
                    target.sendMessage("§c[系統] 你的科技 §e" + techId + " §c已被移除！");
                }
                break;

            default:
                sender.sendMessage(ChatColor.RED + "[系統] 無效的子指令！");
                break;
        }
        return true;
    }
}
