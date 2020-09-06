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
    private final ItemStack token;
    private final GUIManager GM;

    public tpgui(Main main) {
        Token tokenClass = new Token();
        GM = new GUIManager(main);
        token = tokenClass.token;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] args) {
        if(!(sender instanceof Player)) { sender.sendMessage("Ta komenda nie jest dostępna z poziomu konsoli."); return true; }

        Player player = (Player) sender;
        Inventory inv = player.getInventory();

        // Brak argumentów
        if (args.length < 1) {
            if (inv.containsAtLeast(token, 1)) {
                if(!CooldownManager.isOnCooldown(player)) {
                    sender.sendMessage(Messages.getMessage("messages.gui.open"));
                    openGUI(player);
                    CooldownManager.setCooldownTime(player);
                }
                else {
                    sender.sendMessage(Messages.createFormattedMessage("messages.cooldown.message", CooldownManager.getCooldown(player)));
                }
            } else {
                sender.sendMessage(Messages.getMessage("messages.token.noToken"));
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
                            sender.sendMessage(Messages.getMessage("messages.playernotfound"));
                        }
                        sender.sendMessage(Messages.createFormattedMessage("messages.cooldown.checkother", CooldownManager.getCooldown(target)));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        if (CooldownManager.getCooldown(player) <= 0) {
                            sender.sendMessage(Messages.createFormattedMessage("messages.cooldown.check", CooldownManager.getCooldown(player)));
                        } else {
                            sender.sendMessage(Messages.getMessage("messages.cooldown.nocooldown"));
                        }
                    }
                } else {
                    if (CooldownManager.getCooldown(player) <= 0) {
                        sender.sendMessage(Messages.createFormattedMessage("messages.cooldown.check", CooldownManager.getCooldown(player)));
                    } else {
                        sender.sendMessage(Messages.getMessage("messages.cooldown.nocooldown"));
                    }
                }
                break;
            }
            case "cooldownreset": {
                if(!sender.hasPermission("tpaddon.cooldown.reset")) { sender.sendMessage(Messages.getMessage("messages.unknownCommand")); return true;}
                    try {
                    Player target = Bukkit.getPlayerExact(args[1]);
                    assert target != null;
                    if (!target.isOnline()) { sender.sendMessage(Messages.getMessage("messages.playernotfound")); }
                        sender.sendMessage(Messages.createFormattedMessage("messages.cooldown.reset", target.getName()));
                    CooldownManager.removeCooldownTime(target);
                    break;
                } catch (ArrayIndexOutOfBoundsException e) {
                    sender.sendMessage(Messages.createFormattedMessage("messages.cooldown.reset", player.getName()));
                    CooldownManager.removeCooldownTime(player);
                    break;
                }
            }

            case "give": {
                try {
                    if(!sender.hasPermission("tpaddon.give.others")) { sender.sendMessage(Messages.getMessage("messages.unknownCommand")); return true;}
                    Player target = Bukkit.getPlayerExact(args[1]);
                    assert target != null;
                    if (!target.isOnline()) {
                        sender.sendMessage(Messages.getMessage("messages.playernotfound"));
                        break;
                    }
                    target.getInventory().addItem(token);
                    sender.sendMessage(Messages.createFormattedMessage("messages.token.giveother", target.getName()));
                    break;
                } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                    if(!sender.hasPermission("tpaddon.give")) { sender.sendMessage(Messages.getMessage("messages.unknownCommand")); return true;}
                    sender.sendMessage(Messages.getMessage("messages.token.give"));
                    inv.addItem(token);
                    break;
                }
            }
            case "open": {
                try {
                    if(!sender.hasPermission("tpaddon.open.others")) { sender.sendMessage(Messages.getMessage("messages.unknownCommand")); return true;}
                    Player target = Bukkit.getPlayerExact(args[1]);
                    assert target != null;
                    if (!target.isOnline()) {
                        sender.sendMessage(Messages.getMessage("messages.playernotfound"));
                        break;
                    }
                    openGUI(target);
                    sender.sendMessage(Messages.createFormattedMessage("messages.gui.openother", target.getName()));
                    break;
                } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                    if(!sender.hasPermission("tpaddon.open")) { sender.sendMessage(Messages.getMessage("messages.unknownCommand")); return true;}
                    sender.sendMessage(Messages.getMessage("messages.gui.open"));
                    openGUI(player);
                    break;
                }
            }
            default: {
                sender.sendMessage(Messages.getMessage("messages.unknownCommand"));
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
        GM.openInventory(p);
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