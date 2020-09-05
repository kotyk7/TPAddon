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
    private static Main self;
    private static EssentialsSpawn essspawn;
    public TokenRecipe recipe;
    public static String version = "1.0";
    public String prefix;
    public static HashMap<UUID, Date> cooldowns = new HashMap<>();

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        saveDefaultConfig();
        PluginManager pm = getServer().getPluginManager();

        self = this;
        prefix = Config.getString("messages.prefix");
        essspawn = (EssentialsSpawn) pm.getPlugin("EssentialsSpawn");
        recipe = new TokenRecipe();

        getLogger().info(String.format("TPAddon version %s succesfully loaded.", version));

        getCommand("tpaddon").setExecutor(new tpaddon());
        getCommand("tpgui").setExecutor(new tpgui());
        getCommand("token").setExecutor(new token());

        if(essspawn == null) {
            getLogger().info("Nie znaleziono pluginu EssentialsSpawn, ten plugin jest wymagany do działania pluginu");
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

    public static Main getTpAddon() { return self; }

    public static EssentialsSpawn getEssentialsSpawn() {
        return essspawn;
    }
}
