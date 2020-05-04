package com.egsrit.hvz.players;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HvzZombie {
    private Player player;
    private int stunTime;

    public HvzZombie(Player player, int stunTime){
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
    public ChatColor getNameTagColor(){
        return ChatColor.RED;
    }
}
