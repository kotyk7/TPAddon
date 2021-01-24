package me.kotyk.TPAddon.util;

import me.kotyk.TPAddon.Main;
import org.bukkit.entity.Player;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

public class Cooldown {
    private Main main;
    private static String permission = "tpaddon.cooldown.bypass";
    
    private HashMap<UUID, Date> dict = new HashMap<>();
    private int cooldown;

    public Cooldown(Main main) {
        this.main = main;
        cooldown = main.getConfiguration().getInteger("cooldown");
    }

    /**
     * Set defined in config cooldown for player
     * @param p Player to set cooldown
     */
    public void setCooldown(@NotNull Player p) {
        setCooldown(p.getUniqueId());
    }

    /**
     * Remove the cooldown from player
     * @param p Player
     */
    public void removeCooldown(@NotNull Player p) {
        removeCooldown(p.getUniqueId());
    }

    /**
     * Sets cooldown
     * @param uuid Player's uuid
     */
    public void setCooldown(@NotNull UUID uuid) {
        dict.put(uuid, new Date());
    }

    /**
     * Remove the cooldown from player
     * @param uuid Player's uuid
     */
    public void removeCooldown(@NotNull UUID uuid) {
        dict.remove(uuid);
    }

    /**
     * Returns bool if a player is currently on cooldown
     * @param player Player
     * @return boolean
     */
    public boolean isOnCooldown(@NotNull Player player) {
        if(dict.containsKey(player.getUniqueId())) {
            Date lastChange = dict.get(player.getUniqueId());
            Date currentTime = new Date();
            int seconds = (int) (currentTime.getTime() - lastChange.getTime())/1000;
            if(seconds > cooldown || player.hasPermission(permission)) {
                removeCooldown(player);
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * Returns player cooldown.
     * @param player Player
     * @return Returns current player cooldown, can be 0.
     */
    public int getCooldown(Player player) {
        if(dict.containsKey(player.getUniqueId())) {
            Date lastChange = dict.get(player.getUniqueId());
            Date currentTime = new Date();
            int seconds = (int) (currentTime.getTime() - lastChange.getTime())/1000;
            if(seconds > cooldown || player.hasPermission(permission)) {
                removeCooldown(player);
            }
            return cooldown - seconds;
        } else {
            return 0;
        }
    }
}
