package com.egsrit.hvz.players;

import org.bukkit.entity.Player;

public class Human{
    private Player player;
    private int isAlive;

    public Human(Player player, int isAlive){
        this.player = player;
        this.isAlive = isAlive;
    }

    public Player getHuman() {
        return this.player;
    }

    public String getNameTagColor(){
        return "Green";
    }

    public void setAliveStatus(int alive){
        this.isAlive = alive;
    }

    public boolean isAlive(){
        return this.isAlive == 1;
    }
}
