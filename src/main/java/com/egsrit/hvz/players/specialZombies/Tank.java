package com.egsrit.hvz.players.specialZombies;

import com.egsrit.hvz.players.Zombie;
import org.bukkit.entity.Player;

public class Tank extends Zombie {
    public Tank(Player player, int stunTime) {
        super(player, stunTime);
    }

    @Override
    public String getNameTagColor() {
        return "Pink";
    }
}
