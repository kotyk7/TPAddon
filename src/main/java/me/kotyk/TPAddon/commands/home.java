package me.kotyk.TPAddon.commands;

import io.papermc.lib.PaperLib;
import me.kotyk.TPAddon.Main;
import me.kotyk.TPAddon.util.Cooldown;
import me.kotyk.TPAddon.util.Messages;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;


public class home implements CommandExecutor {
    private Main main;
    private Messages msg;
    private Cooldown cd;
    private Location bed;

    public home(Main main) {
        this.main = main;
        this.cd = main.getCooldownM();
        this.msg = main.getMessages();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) sender;
        Inventory inv = player.getInventory();
        PaperLib.getBedSpawnLocationAsync(player, false).thenAccept(location -> this.bed = location);


        if(!cd.isOnCooldown(player)) {
            if (bed != null) {
                if (inv.containsAtLeast(main.getToken().token, 1)) {

                    PaperLib.teleportAsync(player, bed).thenAccept(result -> {
                        if (result) {
                            player.sendActionBar(msg.get("messages.home.teleported", false));
                            main.getGM().takeItem(player);
                            cd.setCooldown(player);
                        } else {
                            player.sendActionBar(msg.get("messages.actionbar.tperror"));
                        }
                    });

                } else {
                    sender.sendMessage(msg.get("messages.token.noToken"));
                }
            } else {
                sender.sendMessage(msg.get("messages.home.nohome"));
            }
        } else {
            sender.sendMessage(msg.format("messages.cooldown.message", cd.getCooldown(player)));
        }


        return true;
    }
}
