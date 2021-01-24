package me.kotyk.TPAddon.util;

import me.kotyk.TPAddon.Main;
import org.bukkit.ChatColor;

import java.util.Arrays;

public class Messages {
    private Main main;
    private Config config;

    public Messages(Main main) {
        this.main = main;
        this.config = main.getConfiguration();
    }

    /**
     * Gets location from config
     * @param location Location
     * @return string with/out messages.prefix
     */
    public String get(String location) {
        boolean withPrefix = true;

        String[] noPrefix = {
                "user.nazwa-itemu",
                "gui.name",
                "skin.1.name", "skin.2.name",
                "messages.actionbar.tptospawn", "messages.actionbar.functionnotdone", "messages.actionbar.cancelplace",
                "messages.tpa.usage", "messages.tpa.command"
        };

        if(Arrays.asList(noPrefix).contains(location)) {
            withPrefix = false;
        }

        return get(location, withPrefix);
    }

    public String get(String location, boolean withPrefix) {
        if (location.equalsIgnoreCase("version")) {
            return ChatColor.translateAlternateColorCodes('&', String.format("%sVersion %s by @kotyk7", main.prefix, main.version));
        }

        if(withPrefix) {
            return ChatColor.translateAlternateColorCodes('&', main.prefix + config.getString(location));
        } else {
            return ChatColor.translateAlternateColorCodes('&', config.getString(location));
        }
    }

    /**
     * String.format equivalent
     * @param location Location
     * @param args object like Integer or String
     */
    public String format(String location, Object args) {
        return ChatColor.translateAlternateColorCodes('&', String.format(get(location), args));
    }

    public String makeMessage(String message, boolean withPrefix) {
        if(withPrefix) {
            return ChatColor.translateAlternateColorCodes('&', main.prefix + message);
        } else {
            return ChatColor.translateAlternateColorCodes('&', message);
        }
    }
}
