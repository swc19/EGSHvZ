package com.egsrit.hvz.players.specialZombies;

import com.egsrit.hvz.players.Zombie;
import org.bukkit.entity.Player;

public class Boomer extends Zombie {
    public Boomer(Player player, int stunTime) {
        super(player, stunTime);
    }

    @Override
    public String getNameTagColor() {
        return "Yellow";
    }
}
