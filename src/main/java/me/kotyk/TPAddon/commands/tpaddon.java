package me.kotyk.TPAddon.commands;

import me.kotyk.TPAddon.Main;
import me.kotyk.TPAddon.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class tpaddon implements TabExecutor {
    private Messages msg;
    private Main main;

    public tpaddon(Main main) {
        this.main = main;
        this.msg = main.getMessages();
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length < 1 || !sender.hasPermission("tpaddon.admin")) {
            sender.sendMessage(msg.get("version"));
        } else if (args[0].equalsIgnoreCase("reload")) {
            main.reloadConfig();
            main.getTrecipe().checkIngredients();
            sender.sendMessage(msg.get("messages.configReloaded"));
        } else {
            sender.sendMessage(msg.get("messages.unknownCommand"));
        }

        return true;
    }

    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!sender.hasPermission("tpaddon.admin")) return null;
        if (args.length != 1) return null;
        return Arrays.asList("reload", "removeconfig");
    }
}