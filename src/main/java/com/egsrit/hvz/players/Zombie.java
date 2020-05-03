package com.egsrit.hvz.players;

import org.bukkit.entity.Player;

public class Zombie {
    private Player player;
    private int stunTime;

    public Zombie(Player player, int stunTime){
        this.player = player;
        this.stunTime = stunTime;
    }
    public Player getZombie() {
        return this.player;
    }

    public int getStunTime(){
        return this.stunTime;
    }
    public boolean canBeStunned(){
        return this.stunTime == 0;
    }
    public String getNameTagColor(){
        return "Red";
    }
}
