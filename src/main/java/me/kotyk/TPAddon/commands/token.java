package me.kotyk.TPAddon.commands;

import me.kotyk.TPAddon.Main;
import me.kotyk.TPAddon.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class token implements TabExecutor {
    public token(Main main) {

    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Messages.getMessage("messages.token.info"));
            return true;
        }

        String subcommand = args[0].toLowerCase();

        if(subcommand.equals("info")) {
            sender.sendMessage(Messages.getMessage("messages.token.info"));
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        //noinspection ArraysAsListWithZeroOrOneArgument
        return Arrays.asList("info");
    }
}
