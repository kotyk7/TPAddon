package me.kotyk.TPAddon.util;

import me.kotyk.TPAddon.Main;
import org.bukkit.entity.Player;
import java.util.Date;
import org.jetbrains.annotations.NotNull;

public class CooldownManager {
    private Config Config = new Config();

    public void setCooldownTime(Player p) {
        Main.cooldowns.put(p.getUniqueId(), new Date());
    }

    public void removeCooldownTime(@NotNull Player p) {
        Main.cooldowns.remove(p.getUniqueId());
    }

    public boolean isOnCooldown(@NotNull Player player) {
        if(Main.cooldowns.containsKey(player.getUniqueId())) {
            Date lastChange = Main.cooldowns.get(player.getUniqueId());
            Date currentTime = new Date();
            int seconds = (int) (currentTime.getTime() - lastChange.getTime())/1000;
            if(seconds > Config.getInteger("cooldown") || player.hasPermission("tpaddon.cooldown.bypass")) {
                removeCooldownTime(player);
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public int getCooldown(Player player) {
        if(Main.cooldowns.containsKey(player.getUniqueId())) {
            Date lastChange = Main.cooldowns.get(player.getUniqueId());
            Date currentTime = new Date();
            int seconds = (int) (currentTime.getTime() - lastChange.getTime())/1000;
            if(seconds > Config.getInteger("cooldown") || player.hasPermission("tpaddon.cooldown.bypass")) {
                removeCooldownTime(player);
            }
            return Config.getInteger("cooldown") - seconds;
        } else {
            return 0;
        }
    }
}
