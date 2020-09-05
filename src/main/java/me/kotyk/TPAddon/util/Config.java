package me.kotyk.TPAddon.util;

import org.bukkit.configuration.file.FileConfiguration;
import me.kotyk.TPAddon.Main;

// This code is from Harbor plugin by techtoolbox (@nkomarn). See https://github.com/nkomarn/Harbor and LICENSE

public class Config {
    private Main plugin = new Main();

    /**
     * Fetches an instance of the FileConfiguration.
     * @return The configuration for this server.
     */
    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    /**
     * Fetches a boolean from the configuration
     * if location is not found, false is returned
     * @param location Configuration location of the boolean
     */
    public boolean getBoolean(String location) {
        return getConfig().getBoolean(location, false);
    }

    /**
     * Fetches a string from the configuration
     * if location is not found, empty string is returned
     * @param location Configuration location of the string
     */
    public String getString(String location) {
        return getConfig().getString(location, "");
    }

    /**
     * Fetches an integer from the configuration
     * if location is not found, 0 is returned
     * @param location Configuration location of the integer
     */
    public int getInteger(String location) {
        return getConfig().getInt(location, 0);
    }

    /**
     * Fetches a double from the configuration
     * if location is not found, 0.0 is returned
     * @param location Configuration location of the double
     */
    public double getDouble(String location) {
        return getConfig().getDouble(location, 0.0);
    }
}