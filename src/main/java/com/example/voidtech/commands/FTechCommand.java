package com.example.voidtech.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FTechCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("此指令只能由玩家執行！");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§a[VoidTech] 使用: /ftech <subcommand>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "info":
                player.sendMessage("§a[VoidTech] 這是勢力科技系統！");
                break;
            default:
                player.sendMessage("§c[VoidTech] 無效的子指令！");
                break;
        }
        return true;
    }
}
