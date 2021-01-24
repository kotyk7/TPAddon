package me.kotyk.TPAddon.commands;

import me.kotyk.TPAddon.Main;
import me.kotyk.TPAddon.util.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.List;

public class tpgui implements TabExecutor {
    private Main main;
    private Cooldown cd;

    public tpgui(Main main) {
        this.main = main;
        this.cd = main.getCooldownM();
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] args) {
        Messages msg = main.getMessages();
        ItemStack token = main.getToken().token;

        if(!(sender instanceof Player)) { sender.sendMessage("Ta komenda nie jest dostępna z poziomu konsoli."); return true; }

        Player player = (Player) sender;
        Inventory inv = player.getInventory();

        // Brak argumentów
        if (args.length < 1 || sender.hasPermission("tpaddon.open")) {
            if (inv.containsAtLeast(token, 1)) {
                if(!cd.isOnCooldown(player)) {
                    sender.sendMessage(msg.get("messages.gui.open"));
                    openGUI(player);
                    cd.setCooldown(player);
                }
                else {
                    sender.sendMessage(msg.format("messages.cooldown.message", cd.getCooldown(player)));
                }
            } else {
                sender.sendMessage(msg.get("messages.token.noToken"));
            }
            return true;
        }

        // Jeżeli argument jest podany
        String subcommand = args[0].toLowerCase();

        if(subcommand.equals("cooldown")) {
            if(args.length >= 2) {
                if(args[1].equals("reset") || sender.hasPermission("tpaddon.cooldown.others")) {
                    Player targetToReset = Bukkit.getPlayerExact(args[2]);
                    if(targetToReset != null) {
                        sender.sendMessage(msg.format("messages.cooldown.checkother", cd.getCooldown(targetToReset)));
                    } else {
                        sender.sendMessage(msg.get("messages.playernotfound"));
                    }
                }
            } else {
                if (cd.isOnCooldown(player)) {
                    sender.sendMessage(msg.format("messages.cooldown.check", cd.getCooldown(player)));
                } else {
                    sender.sendMessage(msg.get("messages.cooldown.nocooldown"));
                }
            }

        } else if(subcommand.equals("open")) {
            if(args.length >= 1) {
                if(sender.hasPermission("tpaddon.open.others")) {
                    Player targetToOpenGUI = Bukkit.getPlayerExact(args[1]);
                    if(targetToOpenGUI != null) {
                        openGUI(targetToOpenGUI);
                    } else {
                        sender.sendMessage(msg.get("messages.playernotfound"));
                    }
                } else if(sender.hasPermission("tpaddon.open")) {
                    if(player.getInventory().containsAtLeast(main.getToken().token, 1)) {
                        openGUI(player);
                    }
                } else {
                    sender.sendMessage(msg.get("messages.unknownCommand"));
                }
            }
        }
        return true;
    }

    /**
     * Otwiera GUI podanemu graczowi
     * @param p Gracz, któremu chcemy otworzyć GUI
     */
    public void openGUI(Player p) {
        main.getGM().openInventory(p);
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] args) {
        if (args.length != 1) return null;
        if (sender.hasPermission("tpaddon.admin")) {
            return Arrays.asList("give", "cooldown", "cooldownreset", "open");
        } else {
            //noinspection ArraysAsListWithZeroOrOneArgument
            return Arrays.asList("cooldown");
        }

    }
}