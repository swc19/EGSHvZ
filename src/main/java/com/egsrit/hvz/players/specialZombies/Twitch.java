package com.egsrit.hvz.players.specialZombies;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Twitch extends Witch {
    public Twitch(Player player, int stunTime) {
        super(player, stunTime);
    }

    @Override
    public ChatColor getNameTagColor() {
        return ChatColor.DARK_PURPLE;
    }
}
