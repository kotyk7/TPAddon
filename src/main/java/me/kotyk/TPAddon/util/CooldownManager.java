package me.kotyk.TPAddon.util;

import me.kotyk.TPAddon.Main;
import org.bukkit.entity.Player;
import java.util.Date;
import org.jetbrains.annotations.NotNull;

public class CooldownManager {
    /**
     * Set defined in config cooldown for player
     * @param p Player to set cooldown
     */
    public static void setCooldownTime(Player p) {
        Main.cooldowns.put(p.getUniqueId(), new Date());
    }

    /**
     * Remove the cooldown from player
     * @param p Player
     */
    public static void removeCooldownTime(@NotNull Player p) {
        Main.cooldowns.remove(p.getUniqueId());
    }

    /**
     * The method checks if player is currently on cooldown
     * @param player Player to check
     * @return boolean
     */
    public static boolean isOnCooldown(@NotNull Player player) {
        if(Main.cooldowns.containsKey(player.getUniqueId())) {
            Date lastChange = Main.cooldowns.get(player.getUniqueId());
            Date currentTime = new Date();
            int seconds = (int) (currentTime.getTime() - lastChange.getTime())/1000;
            if(seconds > Config.getInteger("cooldown") || player.hasPermission("tpaddon.cooldown.bypass")) {
                CooldownManager.removeCooldownTime(player);
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
        if(Main.cooldowns.containsKey(player.getUniqueId())) {
            Date lastChange = Main.cooldowns.get(player.getUniqueId());
            Date currentTime = new Date();
            int seconds = (int) (currentTime.getTime() - lastChange.getTime())/1000;
            if(seconds > Config.getInteger("cooldown") || player.hasPermission("tpaddon.cooldown.bypass")) {
                CooldownManager.removeCooldownTime(player);
            }
            return Config.getInteger("cooldown") - seconds;
        } else {
            return 0;
        }
    }
}
