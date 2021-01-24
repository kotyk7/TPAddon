package me.kotyk.TPAddon.util;

import me.kotyk.TPAddon.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getServer;

public class Token implements Listener {
    private Main main;
    private Messages msg;
    private ShapedRecipe tokenRecipe;
    public NamespacedKey nsKey;
    public ItemStack token;
    public ItemMeta tokenMeta;

    public Token(Main main) {
        this.main = main;
        this.msg = main.getMessages();

        this.nsKey = new NamespacedKey(main, "teleport_token");

        this.token = new ItemStack(Material.REDSTONE_TORCH);
        this.tokenMeta = token.getItemMeta();

        initializeToken();

        this.tokenRecipe = new ShapedRecipe(nsKey, token);
        checkIngredients();

        getServer().getPluginManager().registerEvents(this, main);
    }

    public void initializeToken() {
        tokenMeta.setDisplayName(main.getConfiguration().getString("user.item-name").replace("&", "§"));
        token.setItemMeta(tokenMeta);
        addEnchant(Enchantment.ARROW_KNOCKBACK, 1, token, tokenMeta, true);
    }

    /**
     * Add an enchant to an item.
     * @param enchant enchantment
     * @param level level of the enchantment
     * @param item ItemStack you want to enchant
     * @param itemMeta ItemMeta of this ItemStack
     * @param hideEnchants should we hide enchants
     */
    public void addEnchant(Enchantment enchant, int level, ItemStack item, ItemMeta itemMeta, boolean hideEnchants) {
        itemMeta.addEnchant(enchant, level, true);
        if (hideEnchants) {
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(itemMeta);
        }
    }

    @EventHandler
    public void interactEvent(final PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getItem() != null) {
            if(e.getItem().isSimilar(token)) {
                e.setCancelled(true);
                main.getGM().openInventory(e.getPlayer());
            }
        } else if(e.getAction() == Action.RIGHT_CLICK_AIR && e.getItem() != null) {
            if(e.getItem().isSimilar(token)) {
                e.setCancelled(true);
                main.getGM().openInventory(e.getPlayer());
            }
        }
    }

    /**
     * If recipe exists, delete it
     */
    public void removeRecipe() {
        if (getServer().getRecipe(nsKey) != null) {
            getServer().removeRecipe(nsKey);
        }
    }

    /**
     * Check if ingredients from config are valid
     */
    public void checkIngredients() {
        String material1name = main.getConfiguration().getString("user.token.material1");
        String material2name = main.getConfiguration().getString("user.token.material2");

        Material material1 = Material.getMaterial(material1name);
        Material material2 = Material.getMaterial(material2name);

        if(material1 != null && material2 != null) {
            tokenRecipe.shape("drd", "rdr", "drd");

            tokenRecipe.setIngredient('d', material1);
            tokenRecipe.setIngredient('r', material2);

            removeRecipe();
            getServer().addRecipe(tokenRecipe);

        } else {
            getLogger().warning("Provided in config materials are not valid. Unregistering the recipe");
        }

    }
}
