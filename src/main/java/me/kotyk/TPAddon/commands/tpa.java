package me.kotyk.TPAddon.commands;

import com.earth2me.essentials.IUser;
import com.earth2me.essentials.User;
import me.kotyk.TPAddon.Main;
import me.kotyk.TPAddon.util.Messages;
import net.ess3.api.events.TPARequestEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class tpa implements TabExecutor, Listener {
    private Main main;
    private Messages msg;

    public tpa(Main main) {
        this.main = main;
        this.msg = main.getMessages();
        main.pm.registerEvents(this, main);
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(msg.get("messages.tpa.usage.msg"));
            sender.sendMessage(msg.get("messages.tpa.usage.command"));
            return true;
        }

        Player player = (Player) sender;
        Player target = Bukkit.getPlayerExact(args[0]);
        User esssender = main.getEssentials().getUser(player);
        User esstarget = main.getEssentials().getUser(target);

        if (target != null) {
            TPARequestEvent tpaEvent = new TPARequestEvent(esssender.getSource(), esstarget, false);
            getServer().getPluginManager().callEvent(tpaEvent);
            sender.sendMessage(String.format(msg.get("messages.tpa.requests.sent"), target.getName()));
            esstarget.requestTeleport(esssender, false);
        } else {
            sender.sendMessage(msg.get("messages.playernotfound"));
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        //noinspection ArraysAsListWithZeroOrOneArgument
        return Arrays.asList("info");
    }

    @EventHandler
    public void onTPRequest(final TPARequestEvent e) {
        Player requester = e.getRequester().getPlayer();
        IUser target = e.getTarget();

        target.sendMessage(String.format(msg.get("messages.tpa.requests.received"), requester.getName()));
    }
}
