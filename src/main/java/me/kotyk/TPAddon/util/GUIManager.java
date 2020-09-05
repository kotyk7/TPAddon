package me.kotyk.TPAddon.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.kotyk.TPAddon.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;



import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class GUIManager implements Listener {
    private final Inventory inv;
    private ItemStack token;

    public GUIManager() {
        inv = Bukkit.createInventory(null, 27, Messages.getMessage("gui.name"));

        initializeItems();

        getServer().getPluginManager().registerEvents(this, Main.getTpAddon());
    }

    public void initializeItems() {
        for (int i = 0; i < 27; i++) {
            inv.setItem(i, createGuiItem(Material.BLACK_STAINED_GLASS_PANE, "⠀"));
        }
        inv.setItem(11, createSkull(Config.getString("skin.1.texture"), Messages.getMessage("skin.1.name")));
        inv.setItem(15, createSkull(Config.getString("skin.2.texture"), Messages.getMessage("skin.2.name")));

        Token tokenClass = new Token();
        token = tokenClass.token;
    }

    private static ItemStack createSkull(String url, String name) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        if (url.isEmpty())
            return head;

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        profile.getProperties().put("textures", new Property("textures", url));
        headMeta.setDisplayName(name);

        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);

        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException e) {
            e   .printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }

    @SuppressWarnings("SameParameterValue")
    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);

        return item;
    }

    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory() != inv) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        final Player p = (Player) e.getWhoClicked();

        Location spawn = Main.getEssentialsSpawn().getSpawn("default");
        Inventory inv = p.getInventory();

        switch(e.getRawSlot()) {
            case 11: {
                try {
                    p.teleport(spawn);
                    p.sendActionBar(Messages.getMessage("messages.actionbar.tptospawn"));
                    inv.removeItem(token);
                    break;
                } catch (NullPointerException err) {
                    err.printStackTrace();
                    break;
                }
            }
            case 15: {
//                p.teleport(randomtp specific);
                p.sendActionBar(Messages.getMessage("messages.actionbar.functionnotdone"));
                break;
            }
        }

    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory() == inv) {
            e.setCancelled(true);
        }
    }
}
