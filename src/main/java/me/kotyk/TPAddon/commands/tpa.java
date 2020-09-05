package me.kotyk.TPAddon.commands;

import com.earth2me.essentials.User;
import me.kotyk.TPAddon.Main;
import me.kotyk.TPAddon.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class tpa implements TabExecutor {
    public tpa() {
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Messages.getMessage("messages.tpa.usage.msg"));
            sender.sendMessage(Messages.getMessage("messages.tpa.usage.command"));
            return true;
        }

        Player player = (Player) sender;
        Player target = Bukkit.getPlayerExact(args[0]);
        User user = Main.getEssentials().getUser(target);

        if (target != null) {
            sender.sendMessage(String.format(Messages.getMessage("messages.tpa.requests.sent"), target.getName()));
            Main.getEssentials().getUser(player).requestTeleport(user, false);
        } else {
            sender.sendMessage(Messages.getMessage("messages.playernotfound"));
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        //noinspection ArraysAsListWithZeroOrOneArgument
        return Arrays.asList("info");
    }
}
