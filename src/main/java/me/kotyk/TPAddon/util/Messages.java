package me.kotyk.TPAddon.util;

import me.kotyk.TPAddon.Main;
import org.bukkit.ChatColor;

import java.util.Arrays;

public class Messages {
    private Config Config = new Config();
    private final String prefix = Config.getString("plugin.prefix");

    public String getMessage(String message) {
        String[] array = {"user.nazwa-itemu", "gui.name", "skin.1.name", "skin.2.name", "messages.actionbar.tptospawn", "messages.actionbar.functionnotdone"};

        if(Arrays.asList(array).contains(message)) {
            return ChatColor.translateAlternateColorCodes('&', Config.getString(message));
        }

        if (message.toLowerCase().equals("version")) {
            return ChatColor.translateAlternateColorCodes('&', String.format("%sWersja %s by kotyk", prefix, Main.version));
        }

        return ChatColor.translateAlternateColorCodes('&', prefix + Config.getString(message));
    }

    public String createFormattedMessage(String message, int toFormat) {
        return ChatColor.translateAlternateColorCodes('&', String.format(getMessage(message), toFormat));
    }

    public String createFormattedMessage(String message, String toFormat) {
        return ChatColor.translateAlternateColorCodes('&', String.format(getMessage(message), toFormat));
    }
}
