package com.egsrit.hvz.players.specialZombies;

import com.egsrit.hvz.players.Zombie;
import org.bukkit.entity.Player;

public class Witch extends Zombie {
    public Witch(Player player, int stunTime) {
        super(player, 10);
    }

    @Override
    public String getNameTagColor() {
        return "Blue";
    }
}
