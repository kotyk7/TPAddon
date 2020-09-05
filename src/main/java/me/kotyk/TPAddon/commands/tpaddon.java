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

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length < 1 || !sender.hasPermission("tpaddon.admin")) {
            sender.sendMessage(Messages.getMessage("version"));
        } else if (args[0].equalsIgnoreCase("reload")) {
            Main.getTpAddon().reloadConfig();
            Main.getTpAddon().recipe.checkIngredients();
            sender.sendMessage(Messages.getMessage("messages.configReloaded"));
        } else {
            sender.sendMessage(Messages.getMessage("messages.unknownCommand"));
        }

        return true;
    }

    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!sender.hasPermission("tpaddon.admin")) return null;
        if (args.length != 1) return null;
        return Arrays.asList("reload", "removeconfig");
    }
}