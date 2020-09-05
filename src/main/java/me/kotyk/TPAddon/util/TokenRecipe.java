package me.kotyk.TPAddon.util;

import me.kotyk.TPAddon.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getServer;

public class TokenRecipe {
    private Main plugin = new Main();
    private Config Config = new Config();
    public NamespacedKey nsKey = new NamespacedKey(plugin, "teleport_token");
    private final Token tokenclass = new Token();
    private final ItemStack token = tokenclass.token;
    private final ShapedRecipe tokenRecipe = new ShapedRecipe(nsKey, token);


    public TokenRecipe() {
        checkIngredients();
    }

    public void checkAndRemoveRecipe() {
        if (getServer().getRecipe(nsKey) != null) {
            getServer().removeRecipe(nsKey);
        }
    }

    public void checkIngredients() {
        String material1name = Config.getString("user.token.material1");
        String material2name = Config.getString("user.token.material2");

        Material material1 = Material.getMaterial(material1name);
        Material material2 = Material.getMaterial(material2name);

        if(material1 != null && material2 != null) {
            tokenRecipe.shape("drd", "rdr", "drd");

            tokenRecipe.setIngredient('d', material1);
            tokenRecipe.setIngredient('r', material2);

            checkAndRemoveRecipe();
            getServer().addRecipe(tokenRecipe);

        } else {
            getLogger().warning("Podane w konfiguracji materiały na przepis tokenu są nieprawidłowe. Nie rejestruje przepisu.");
        }


    }
}
