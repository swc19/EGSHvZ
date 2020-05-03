package com.egsrit.hvz.players.specialZombies;

import com.egsrit.hvz.players.Zombie;
import org.bukkit.entity.Player;

public class Jackal extends Zombie {

    public Jackal(Player player, int stunTime) {
        super(player, stunTime/2);
    }

    @Override
    public String getNameTagColor() {
        return "White";
    }
}
