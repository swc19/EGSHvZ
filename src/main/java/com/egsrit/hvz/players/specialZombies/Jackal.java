package com.egsrit.hvz.players.specialZombies;

import com.egsrit.hvz.players.HvzZombie;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Jackal extends HvzZombie {

    public Jackal(Player player, int stunTime) {
        super(player, stunTime/2);
    }

    @Override
    public ChatColor getNameTagColor() {
        return ChatColor.WHITE;
    }
}
