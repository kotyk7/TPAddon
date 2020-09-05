package me.kotyk.TPAddon;

import com.earth2me.essentials.spawn.EssentialsSpawn;
import me.kotyk.TPAddon.commands.*;
import me.kotyk.TPAddon.util.Config;
import me.kotyk.TPAddon.util.TokenRecipe;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener {
    private Main self;
    private EssentialsSpawn essspawn;
    public TokenRecipe recipe;
    public static String version = "1.0";
    public String prefix;
    public static HashMap<UUID, Date> cooldowns = new HashMap<>();

    public static boolean debug;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        saveDefaultConfig();
        PluginManager pm = getServer().getPluginManager();
        Config Config = new Config();

        self = this;
        prefix = Config.getString("messages.prefix");
        essspawn = (EssentialsSpawn) pm.getPlugin("EssentialsSpawn");
        recipe = new TokenRecipe();
        debug = Config.getBoolean("plugin.debug");

        getLogger().info(String.format("TPAddon version %s succesfully loaded.", version));

        getCommand("tpaddon").setExecutor(new tpaddon());
        getCommand("tpgui").setExecutor(new tpgui());
        getCommand("token").setExecutor(new token());

        if(essspawn == null) {
            if(debug) {
                getLogger().info("[DEBUG] No EssentialsSpawn found, this plugin won't work. Disabling");
                pm.disablePlugin(this);
            }
        }

        pm.registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        if(debug) {
            getLogger().info("[DEBUG/Main] discovering recipe for player " + e.getPlayer().getDisplayName());
            getLogger().info("[DEBUG/Main] Result of adding recipe: " + e.getPlayer().discoverRecipe(recipe.nsKey));
        }
        e.getPlayer().discoverRecipe(recipe.nsKey);
    }

    @Override
    public void onDisable() {
        if(debug) {
            getLogger().info("[DEBUG/Main] Removing recipe");
        }
        recipe.checkAndRemoveRecipe();
    }

    public Main getTpAddon() { return self; }

    public EssentialsSpawn getEssentialsSpawn() {
        return essspawn;
    }
}
