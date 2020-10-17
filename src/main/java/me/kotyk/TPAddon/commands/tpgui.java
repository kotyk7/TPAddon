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

    public tpgui(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] args) {
        GUIManager GM = main.getGM();
        Messages msg = main.getMessages();
        ItemStack token = main.getToken().token;

        if(!(sender instanceof Player)) { sender.sendMessage("Ta komenda nie jest dostępna z poziomu konsoli."); return true; }

        Player player = (Player) sender;
        Inventory inv = player.getInventory();

        // Brak argumentów
        if (args.length < 1) {
            if (inv.containsAtLeast(token, 1)) {
                if(!Cooldown.isOnCooldown(player)) {
                    sender.sendMessage(msg.get("messages.gui.open"));
                    openGUI(player);
                    Cooldown.setCooldownTime(player);
                }
                else {
                    sender.sendMessage(msg.format("messages.cooldown.message", Cooldown.getCooldown(player)));
                }
            } else {
                sender.sendMessage(msg.get("messages.token.noToken"));
            }
            return true;
        }

        // Jeżeli argument jest podany
        String subcommand = args[0].toLowerCase();

        // Przykład subkomendy give: /tpaddon give
        switch(subcommand) {
            case "cooldown": {
                if(sender.hasPermission("tpaddon.cooldown.others")) {
                    try {
                        Player target = Bukkit.getPlayerExact(args[1]);
                        assert target != null;
                        if (!target.isOnline()) {
                            sender.sendMessage(msg.get("messages.playernotfound"));
                        }
                        sender.sendMessage(msg.format("messages.cooldown.checkother", Cooldown.getCooldown(target)));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        if (Cooldown.getCooldown(player) <= 0) {
                            sender.sendMessage(msg.format("messages.cooldown.check", Cooldown.getCooldown(player)));
                        } else {
                            sender.sendMessage(msg.get("messages.cooldown.nocooldown"));
                        }
                    }
                } else {
                    if (Cooldown.getCooldown(player) <= 0) {
                        sender.sendMessage(msg.format("messages.cooldown.check", Cooldown.getCooldown(player)));
                    } else {
                        sender.sendMessage(msg.get("messages.cooldown.nocooldown"));
                    }
                }
                break;
            }
            case "cooldownreset": {
                if(!sender.hasPermission("tpaddon.cooldown.reset")) { sender.sendMessage(msg.get("messages.unknownCommand")); return true;}
                    try {
                    Player target = Bukkit.getPlayerExact(args[1]);
                    assert target != null;
                    if (!target.isOnline()) { sender.sendMessage(msg.get("messages.playernotfound")); }
                        sender.sendMessage(msg.format("messages.cooldown.reset", target.getName()));
                    Cooldown.removeCooldownTime(target);
                    break;
                } catch (ArrayIndexOutOfBoundsException e) {
                    sender.sendMessage(msg.format("messages.cooldown.reset", player.getName()));
                    Cooldown.removeCooldownTime(player);
                    break;
                }
            }

            case "give": {
                try {
                    if(!sender.hasPermission("tpaddon.give.others")) { sender.sendMessage(msg.get("messages.unknownCommand")); return true;}
                    Player target = Bukkit.getPlayerExact(args[1]);
                    assert target != null;
                    if (!target.isOnline()) {
                        sender.sendMessage(msg.get("messages.playernotfound"));
                        break;
                    }
                    target.getInventory().addItem(token);
                    sender.sendMessage(msg.format("messages.token.giveother", target.getName()));
                    break;
                } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                    if(!sender.hasPermission("tpaddon.give")) { sender.sendMessage(msg.get("messages.unknownCommand")); return true;}
                    sender.sendMessage(msg.get("messages.token.give"));
                    inv.addItem(token);
                    break;
                }
            }
            case "open": {
                try {
                    if(!sender.hasPermission("tpaddon.open.others")) { sender.sendMessage(msg.get("messages.unknownCommand")); return true;}
                    Player target = Bukkit.getPlayerExact(args[1]);
                    assert target != null;
                    if (!target.isOnline()) {
                        sender.sendMessage(msg.get("messages.playernotfound"));
                        break;
                    }
                    openGUI(target);
                    sender.sendMessage(msg.format("messages.gui.openother", target.getName()));
                    break;
                } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                    if(!sender.hasPermission("tpaddon.open")) { sender.sendMessage(msg.get("messages.unknownCommand")); return true;}
                    sender.sendMessage(msg.get("messages.gui.open"));
                    openGUI(player);
                    break;
                }
            }
            default: {
                sender.sendMessage(msg.get("messages.unknownCommand"));
                break;
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