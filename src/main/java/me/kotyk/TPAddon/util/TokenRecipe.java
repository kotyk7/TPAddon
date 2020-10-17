package me.kotyk.TPAddon.util;

import me.kotyk.TPAddon.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getServer;

public class TokenRecipe {
    private Main main;
    public NamespacedKey nsKey;
    private ShapedRecipe tokenRecipe;

    public TokenRecipe(Main main) {
        this.main = main;
        ItemStack token = main.getToken().token;
        nsKey = new NamespacedKey(main, "teleport_token");
        this.tokenRecipe = new ShapedRecipe(nsKey, token);
        checkIngredients();
    }

    /**
     * If recipe exists, delete it
     */
    public void checkRecipe() {
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

            checkRecipe();
            getServer().addRecipe(tokenRecipe);

        } else {
            getLogger().warning("Provided in config materials are not valid. Unregistering the recipe");
        }

    }
}
