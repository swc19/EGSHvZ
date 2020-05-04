package com.egsrit.hvz.players.specialZombies;

import com.egsrit.hvz.players.HvzZombie;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Witch extends HvzZombie {
    public Witch(Player player, int stunTime) {
        super(player, 10);
    }

    @Override
    public ChatColor getNameTagColor() {
        return ChatColor.BLUE;
    }
}
