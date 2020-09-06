package me.kotyk.TPAddon.util;

import org.bukkit.configuration.file.FileConfiguration;
import me.kotyk.TPAddon.Main;

// This code is from Harbor plugin by techtoolbox (@nkomarn). See https://github.com/nkomarn/Harbor and LICENSE

public class Config {
    /**
     * Fetches an instance of the FileConfiguration.
     * @return The configuration for this server.
     */
    public static FileConfiguration getConfig() {
        return Main.getMain().getConfig();
    }

    /**
     * Fetches a boolean from the configuration
     * if location is not found, false is returned
     * @param location Configuration location of the boolean
     */
    public static boolean getBoolean(String location) {
        return getConfig().getBoolean(location, false);
    }

    /**
     * Fetches a string from the configuration
     * if location is not found, empty string is returned
     * @param location Configuration location of the string
     */
    public static String getString(String location) {
        return getConfig().getString(location, "");
    }

    /**
     * Fetches an integer from the configuration
     * if location is not found, 0 is returned
     * @param location Configuration location of the integer
     */
    public static int getInteger(String location) {
        return getConfig().getInt(location, 0);
    }

    /**
     * Fetches a double from the configuration
     * if location is not found, 0.0 is returned
     * @param location Configuration location of the double
     */
    public static double getDouble(String location) {
        return getConfig().getDouble(location, 0.0);
    }
}