package me.kotyk.TPAddon.util;

import me.kotyk.TPAddon.Main;
import org.bukkit.ChatColor;

import java.util.Arrays;

public class Messages {
    public Messages(Main main) {

    }

    /**
     * Returns formatted string with prefix from config
     * @param location Location
     * @return String with formatted prefix (if needed)
     */
    public static String getMessage(String location) {
        String[] noPrefix = {"user.nazwa-itemu", "gui.name", "skin.1.name", "skin.2.name", "messages.actionbar.tptospawn", "messages.actionbar.functionnotdone", "messages.tpa.usage", "messages.tpa.command", "messages.actionbar.cancelplace"};

        if(Arrays.asList(noPrefix).contains(location)) {
            return ChatColor.translateAlternateColorCodes('&', Config.getString(location));
        }

        if (location.toLowerCase().equals("version")) {
            return ChatColor.translateAlternateColorCodes('&', String.format("%sVersion %s by @kotyk7", Main.getMain().prefix, Main.version));
        }

        return ChatColor.translateAlternateColorCodes('&', Main.getMain().prefix + Config.getString(location));
    }

    /**
     * Returns formatted string with integer from config
     * @param location Location
     * @param integer Integer to add to the message, if %s is not present in location - ignoring
     * @return String with formatted integer
     */
    public static String createFormattedMessage(String location, int integer) {
        return ChatColor.translateAlternateColorCodes('&', String.format(getMessage(location), integer));
    }

    /**
     * Returns formatted string with string from config
     * @param location Location
     * @param string String to add to the message, if %s is not present in location - ignoring
     * @return String with formatted string
     */
    public static String createFormattedMessage(String location, String string) {
        return ChatColor.translateAlternateColorCodes('&', String.format(getMessage(location), string));
    }
}
