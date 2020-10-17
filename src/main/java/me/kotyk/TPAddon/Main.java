package me.kotyk.TPAddon;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.spawn.EssentialsSpawn;
import me.kotyk.TPAddon.commands.token;
import me.kotyk.TPAddon.commands.tpa;
import me.kotyk.TPAddon.commands.tpaddon;
import me.kotyk.TPAddon.commands.tpgui;
import me.kotyk.TPAddon.util.*;
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
    private Essentials ess;
    private EssentialsSpawn essSpawn;

    private Config config;
    private Cooldown cmanager;
    private Messages msg;
    private GUIManager GM;
    private Token token;
    private TokenRecipe trecipe;

    public String version = "1.0";
    public PluginManager pm;
    public String prefix;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        self = this;

        saveDefaultConfig();
        pm = getServer().getPluginManager();

        config = new Config(this);
        cmanager = new Cooldown(this);
        msg = new Messages(this);
        token = new Token(this);
        trecipe = new TokenRecipe(this);
        GM = new GUIManager(this);

        prefix = config.getString("messages.prefix");
        ess = (Essentials) pm.getPlugin("Essentials");
        essSpawn = (EssentialsSpawn) pm.getPlugin("EssentialsSpawn");

        getLogger().info(String.format("TPAddon version %s succesfully loaded.", version));

        getCommand("tpaddon").setExecutor(new tpaddon(this));
        getCommand("tpgui").setExecutor(new tpgui(this));
        getCommand("token").setExecutor(new token(this));
        getCommand("tpa").setExecutor(new tpa(this));

        if (ess == null) {
            getLogger().info("Essentials not found! This plugin is not required, but recommended.");
        }
        if (essSpawn == null) {
            getLogger().warning("EssentialsSpawn not found! This plugin is required, disabling.");
            pm.disablePlugin(this);
        }

        pm.registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        e.getPlayer().discoverRecipe(trecipe.nsKey);
    }

    @Override
    public void onDisable() {
        trecipe.checkRecipe();
    }

    public Main getMain() { return self; }

    public Essentials getEssentials() {
        return ess;
    }

    public EssentialsSpawn getEssentialsSpawn() {
        return essSpawn;
    }

    public Config getConfiguration() {
        return config;
    }

    public Cooldown getCooldownM() {
        return cmanager;
    }

    public Messages getMessages() {
        return msg;
    }

    public GUIManager getGM() {
        return GM;
    }

    public Token getToken() {
        return token;
    }

    public TokenRecipe getTrecipe() {
        return trecipe;
    }
}
