package me.kotyk.TPAddon;

import com.earth2me.essentials.Essentials;
import io.papermc.lib.PaperLib;
import me.kotyk.TPAddon.commands.*;
import me.kotyk.TPAddon.util.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class Main extends JavaPlugin implements Listener {
    private Main self;
    private Essentials ess;

    private Config config;
    private Cooldown cmanager;
    private Messages msg;
    private GUIManager GM;
    private Token token;

    public String version = "1.1";
    public PluginManager pm;
    public String prefix;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        PaperLib.suggestPaper(this);

        self = this;

        saveDefaultConfig();
        pm = getServer().getPluginManager();

        config = new Config(this);
        cmanager = new Cooldown(this);
        msg = new Messages(this);
        token = new Token(this);
        GM = new GUIManager(this);

        prefix = config.getString("messages.prefix") + " ";
        ess = (Essentials) pm.getPlugin("Essentials");

        getLogger().info(msg.format("TPAddon version %s succesfully loaded.", version));

        if (ess == null) {
            getLogger().severe("Essentials not found! Disabling plugin...");
            pm.disablePlugin(this);
        }

        getCommand("tpaddon").setExecutor(new tpaddon(this));
        getCommand("tpgui").setExecutor(new tpgui(this));
        getCommand("tpa").setExecutor(new tpa(this));
        getCommand("spawn").setExecutor(new spawn(this));
        getCommand("home").setExecutor(new home(this));

        pm.registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        e.getPlayer().discoverRecipe(token.nsKey);
    }

    @Override
    public void onDisable() {
        token.removeRecipe();
    }

    public Main getMain() { return self; }

    public Essentials getEssentials() {
        return ess;
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
}
