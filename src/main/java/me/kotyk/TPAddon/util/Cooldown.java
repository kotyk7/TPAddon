package me.kotyk.TPAddon.util;

import me.kotyk.TPAddon.Main;
import org.bukkit.entity.Player;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

public class Cooldown {
    private static HashMap<UUID, Date> dict = new HashMap<>();
    private static int cooldown;

    public Cooldown(Main main) {
        cooldown = main.getConfiguration().getInteger("cooldown");
    }

    /**
     * Set defined in config cooldown for player
     * @param p Player to set cooldown
     */
    public static void setCooldownTime(@NotNull Player p) {
        dict.put(p.getUniqueId(), new Date());
    }

    /**
     * Remove the cooldown from player
     * @param p Player
     */
    public static void removeCooldownTime(@NotNull Player p) {
        dict.remove(p.getUniqueId());
    }

    /**
     * The method checks if player is currently on cooldown
     * @param player Player to check
     * @return boolean
     */
    public static boolean isOnCooldown(@NotNull Player player) {
        if(dict.containsKey(player.getUniqueId())) {
            Date lastChange = dict.get(player.getUniqueId());
            Date currentTime = new Date();
            int seconds = (int) (currentTime.getTime() - lastChange.getTime())/1000;
            if(seconds > cooldown || player.hasPermission("tpaddon.cooldown.bypass")) {
                removeCooldownTime(player);
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * Returns player cooldown if present
     * @param player Player to check
     * @return Returns cooldown, if no key in the HashMap it returns 0
     */
    public static int getCooldown(Player player) {
        if(dict.containsKey(player.getUniqueId())) {
            Date lastChange = dict.get(player.getUniqueId());
            Date currentTime = new Date();
            int seconds = (int) (currentTime.getTime() - lastChange.getTime())/1000;
            if(seconds > cooldown || player.hasPermission("tpaddon.cooldown.bypass")) {
                removeCooldownTime(player);
            }
            return cooldown - seconds;
        } else {
            return 0;
        }
    }
}
