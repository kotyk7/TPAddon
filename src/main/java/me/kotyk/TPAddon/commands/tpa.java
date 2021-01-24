package me.kotyk.TPAddon.commands;

import com.earth2me.essentials.User;
import me.kotyk.TPAddon.Main;
import me.kotyk.TPAddon.util.Cooldown;
import me.kotyk.TPAddon.util.GUIManager;
import me.kotyk.TPAddon.util.Messages;
import net.ess3.api.events.TPARequestEvent;
import net.ess3.api.IUser;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.Bukkit.getServer;

public class tpa implements CommandExecutor, Listener {
    private Main main;
    private Messages msg;
    private GUIManager gui;
    private Cooldown cd;

    public tpa(Main main) {
        this.main = main;
        this.msg = main.getMessages();
        this.gui = main.getGM();
        this.cd = main.getCooldownM();

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
        IUser esssender = main.getEssentials().getUser(player);
        IUser esstarget = main.getEssentials().getUser(target);
        User usersender = (User) esssender;

        if(!cd.isOnCooldown(player)) {
            if (target != null) {
                if (player.getInventory().containsAtLeast(main.getToken().token, 1)) {
                    TPARequestEvent tpaEvent = new TPARequestEvent(esssender.getSource(), esstarget, false);
                    getServer().getPluginManager().callEvent(tpaEvent);

                    cd.setCooldown(player);
                    gui.takeItem(player);

                    sender.sendMessage(msg.format("messages.tpa.requests.sent", target.getName()));
                    esstarget.requestTeleport(usersender, false);

                } else {
                    sender.sendMessage(msg.get("messages.token.noToken"));
                }
            } else {
                sender.sendMessage(msg.get("messages.playernotfound"));
            }
        } else {
            sender.sendMessage(msg.format("messages.cooldown.message", cd.getCooldown(player)));
        }


        return true;
    }

    @EventHandler
    public void onTPRequest(final TPARequestEvent e) {
        Player requester = e.getRequester().getPlayer();
        IUser target = e.getTarget();

        target.sendMessage(String.format(msg.get("messages.tpa.requests.received"), requester.getName()));
    }
}
