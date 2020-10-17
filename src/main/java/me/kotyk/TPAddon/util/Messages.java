package me.kotyk.TPAddon.util;

import me.kotyk.TPAddon.Main;
import org.bukkit.ChatColor;

import java.util.Arrays;

public class Messages {
    private Main main;

    public Messages(Main main) {
        this.main = main;
    }

    /**
     * Returns formatted string with prefix from config
     * @param location Location
     * @return String with formatted prefix (if needed)
     */
    public String get(String location) {
        String[] noPrefix = {"user.nazwa-itemu", "gui.name", "skin.1.name", "skin.2.name", "messages.actionbar.tptospawn", "messages.actionbar.functionnotdone", "messages.tpa.usage", "messages.tpa.command", "messages.actionbar.cancelplace"};

        if(Arrays.asList(noPrefix).contains(location)) {
            return ChatColor.translateAlternateColorCodes('&', main.getConfiguration().getString(location));
        }

        if (location.toLowerCase().equals("version")) {
            return ChatColor.translateAlternateColorCodes('&', String.format("%sVersion %s by @kotyk7", main.prefix, main.version));
        }

        return ChatColor.translateAlternateColorCodes('&', main.prefix + main.getConfiguration().getString(location));
    }

    /**
     * Returns formatted string with integer from config
     * @param location Location
     * @param integer Integer to add to the message, if %s is not present in location - ignoring
     * @return String with formatted integer
     */
    public String format(String location, int integer) {
        return ChatColor.translateAlternateColorCodes('&', String.format(get(location), integer));
    }

    /**
     * Returns formatted string with string from config
     * @param location Location
     * @param string String to add to the message, if %s is not present in location - ignoring
     * @return String with formatted string
     */
    public String format(String location, String string) {
        return ChatColor.translateAlternateColorCodes('&', String.format(get(location), string));
    }
}
