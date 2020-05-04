package com.egsrit.hvz.players.specialZombies;

import com.egsrit.hvz.players.HvzZombie;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Boomer extends HvzZombie {
    public Boomer(Player player, int stunTime) {
        super(player, stunTime);
    }

    @Override
    public ChatColor getNameTagColor() {
        return ChatColor.YELLOW;
    }
}
