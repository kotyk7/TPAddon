package me.kotyk.TPAddon.commands;

import io.papermc.lib.PaperLib;
import me.kotyk.TPAddon.Main;
import me.kotyk.TPAddon.util.Cooldown;
import me.kotyk.TPAddon.util.Messages;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class spawn implements CommandExecutor {
    private Main main;
    private Messages msg;
    private Cooldown cd;
    private Location spawn;

    public spawn(Main main) {
        this.main = main;
        this.cd = main.getCooldownM();
        this.msg = main.getMessages();
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) sender;
        Inventory inv = player.getInventory();

        spawn = main.getServer().getWorlds().get(0).getSpawnLocation();

        if(!cd.isOnCooldown(player)) {
            if (inv.containsAtLeast(main.getToken().token, 1)) {
                PaperLib.teleportAsync(player, spawn).thenAccept(result -> {
                    if (result) {
                        player.sendActionBar(msg.get("messages.actionbar.tptospawn"));
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
            sender.sendMessage(msg.format("messages.cooldown.message", cd.getCooldown(player)));
        }
        return true;
    }
}
