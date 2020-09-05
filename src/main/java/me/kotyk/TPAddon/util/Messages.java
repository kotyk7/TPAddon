package me.kotyk.TPAddon.util;

import me.kotyk.TPAddon.Main;
import org.bukkit.ChatColor;

import java.util.Arrays;

public class Messages {
    public static String getMessage(String message) {
        String[] array = {"user.nazwa-itemu", "gui.name", "skin.1.name", "skin.2.name", "messages.actionbar.tptospawn", "messages.actionbar.functionnotdone"};

        if(Arrays.asList(array).contains(message)) {
            return ChatColor.translateAlternateColorCodes('&', Config.getString(message));
        }

        if (message.toLowerCase().equals("version")) {
            return ChatColor.translateAlternateColorCodes('&', String.format("%sWersja %s by kotyk", Main.getTpAddon().prefix, Main.version));
        }

        return ChatColor.translateAlternateColorCodes('&', Main.getTpAddon().prefix + Config.getString(message));
    }

    public static String createFormattedMessage(String message, int toFormat) {
        return ChatColor.translateAlternateColorCodes('&', String.format(getMessage(message), toFormat));
    }

    public static String createFormattedMessage(String message, String toFormat) {
        return ChatColor.translateAlternateColorCodes('&', String.format(getMessage(message), toFormat));
    }
}
