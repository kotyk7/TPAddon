package me.kotyk.TPAddon.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.papermc.lib.PaperLib;
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

public class GUIManager implements Listener {
    private Main main;
    private Messages msg;
    private final Inventory inv;
    private ItemStack token;

    public GUIManager(Main main) {
        this.main = main;
        this.token = main.getToken().token;
        this.msg = main.getMessages();

        this.inv = Bukkit.createInventory(null, 27, msg.get("gui.name"));

        initializeItems();

        main.pm.registerEvents(this, main);
    }

    /**
     * Initialize items in the GUI
     */
    public void initializeItems() {
        for (int i = 0; i < 27; i++) {
            inv.setItem(i, createGuiItem(Material.BLACK_STAINED_GLASS_PANE, "⠀"));
        }

        inv.setItem(11, createSkull(main.getConfiguration().getString("skin.1.texture"), msg.get("skin.1.name")));
        inv.setItem(15, createSkull(main.getConfiguration().getString("skin.2.texture"), msg.get("skin.2.name")));
    }

    public void takeItem(Player player) {
        Inventory inv = player.getInventory();
        inv.removeItem(token);
    }

    /**
     * Create player head from texture and name
     * @param texture base64 texture value
     * @param name Name of the itemstack
     * @return head with the texture and name
     */
    private ItemStack createSkull(String texture, String name) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        if (texture.isEmpty())
            return head;

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);

        profile.getProperties().put("textures", new Property("textures", texture));
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

    /**
     * Create a GUI item
     * @param material Item in Material class
     * @param name Name of the item
     * @param lore Lore of the item
     * @return Created ItemStack
     */
    @SuppressWarnings("SameParameterValue")
    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);

        return item;
    }

    /**
     * Open an inventory to player
     * @param ent Player/Entity
     */
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

        Location spawn = main.getServer().getWorlds().get(0).getSpawnLocation();
        Inventory inv = p.getInventory();

        switch(e.getRawSlot()) {
            case 11: {
                try {
                    PaperLib.teleportAsync(p, spawn).thenAccept(result -> {
                        if (result) {
                            p.sendActionBar(msg.get("messages.actionbar.tptospawn", false));
                            inv.removeItem(token);
                        } else {
                            p.sendActionBar(msg.get("messages.actionbar.tperror", false));
                        }
                    });
                    break;
                } catch (NullPointerException err) {
                    err.printStackTrace();
                    break;
                }
            }
            case 15: {
                try {
                    if (p.getBedSpawnLocation() != null && p.getBedSpawnLocation() != p.getWorld().getSpawnLocation()) {
                        if(p.getInventory().containsAtLeast(main.getToken().token, 1)) {
                            p.teleport(p.getBedSpawnLocation());
                            main.getGM().takeItem(p);
                            p.sendActionBar(msg.get("messages.home.teleported", false));
                        }
                    } else {
                        p.sendActionBar(msg.get("messages.home.nohome", false));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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
