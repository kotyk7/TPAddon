package me.kotyk.TPAddon.util;

import me.kotyk.TPAddon.Main;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static org.bukkit.Bukkit.getServer;

public class Token implements Listener {
    public ItemStack token;
    public ItemMeta tokenMeta;

    public Token() {
        token = new ItemStack(Material.REDSTONE_TORCH);
        tokenMeta = token.getItemMeta();
        initializeToken();

        getServer().getPluginManager().registerEvents(this, Main.getMain());
    }

    public void initializeToken() {
        tokenMeta.setDisplayName(Messages.getMessage("user.nazwa-itemu"));
        token.setItemMeta(tokenMeta);
        addEnchant(Enchantment.ARROW_KNOCKBACK, 1, token, tokenMeta, true);
    }

    public void addEnchant(Enchantment enchant, int level, ItemStack item, ItemMeta itemMeta, boolean hide) {
        itemMeta.addEnchant(enchant, level, true);
        if (hide) {
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(itemMeta);
        }
    }

    @EventHandler
    public void interactEvent(final PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getItem() != null) {
            String name = e.getItem().getItemMeta().getDisplayName().toLowerCase();
            if(name.contains("§1token teleportacji")) {
                e.setCancelled(true);
                e.getPlayer().sendActionBar(Messages.getMessage("messages.actionbar.cancelplace"));
            }
        }
    }
}
