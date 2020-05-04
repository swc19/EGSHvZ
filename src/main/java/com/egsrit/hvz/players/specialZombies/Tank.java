package com.egsrit.hvz.players.specialZombies;

import com.egsrit.hvz.players.HvzZombie;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Tank extends HvzZombie {
    public Tank(Player player, int stunTime) {
        super(player, stunTime);
    }

    @Override
    public ChatColor getNameTagColor() {
        return ChatColor.LIGHT_PURPLE; // Pink isn't a thing, close enough
    }
}
