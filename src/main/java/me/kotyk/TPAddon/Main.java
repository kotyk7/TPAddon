package me.kotyk.TPAddon;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.spawn.EssentialsSpawn;
import me.kotyk.TPAddon.commands.token;
import me.kotyk.TPAddon.commands.tpa;
import me.kotyk.TPAddon.commands.tpaddon;
import me.kotyk.TPAddon.commands.tpgui;
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
    private static Main self;
    private static Essentials Essentials;
    private static EssentialsSpawn EssentialsSpawn;
    public static boolean debug = false;
    public static String version = "1.0";
    public static HashMap<UUID, Date> cooldowns = new HashMap<>();
    public PluginManager pm;
    public TokenRecipe recipe;
    public String prefix;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        saveDefaultConfig();
        pm = getServer().getPluginManager();

        self = this;
        prefix = Config.getString("messages.prefix");
        Essentials = (Essentials) pm.getPlugin("Essentials");
        EssentialsSpawn = (EssentialsSpawn) pm.getPlugin("EssentialsSpawn");
        recipe = new TokenRecipe();
        debug = Config.getBoolean("debug");

        getLogger().info(String.format("TPAddon version %s succesfully loaded.", version));

        getCommand("tpaddon").setExecutor(new tpaddon(this));
        getCommand("tpgui").setExecutor(new tpgui(this));
        getCommand("token").setExecutor(new token(this));
        getCommand("tpa").setExecutor(new tpa(this));

        if (Essentials == null) {
            getLogger().info("Essentials not found! This plugin is not required, but recommended.");
        }
        if (EssentialsSpawn == null) {
            getLogger().warning("EssentialsSpawn not found! This plugin is required, disabling.");
            pm.disablePlugin(this);
        }

        pm.registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        e.getPlayer().discoverRecipe(TokenRecipe.nsKey);
    }

    @Override
    public void onDisable() {
        recipe.checkRecipe();
    }

    /**
     * Returns static Main class
     * @return Main class
     */
    public static Main getMain() { return self; }

    /**
     * Returns static Essentials class
     * @return Essentials class
     */
    public static Essentials getEssentials() {
        return Essentials;
    }

    /**
     * Returns static EssentialsSpawn class
     * @return EssentialsSpawn class
     */
    public static EssentialsSpawn getEssentialsSpawn() {
        return EssentialsSpawn;
    }
}
