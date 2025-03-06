package com.example.voidtech.commands;

import com.example.voidtech.database.TechStorage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TechTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("info");
            completions.add("unlock");
            completions.add("removetech");
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("unlock") || args[0].equalsIgnoreCase("removetech"))) {
            completions.addAll(Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .collect(Collectors.toList()));
        } else if (args.length == 3 && (args[0].equalsIgnoreCase("unlock") || args[0].equalsIgnoreCase("removetech"))) {
            completions.addAll(TechStorage.getAllTechs()); // ✅ 自動補全所有科技（包含等級）
        }

        return completions;
    }
}
