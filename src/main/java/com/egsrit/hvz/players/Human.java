package com.egsrit.hvz.players;

import org.bukkit.ChatColor;

public class Human{
    private final String player;
    private boolean isAlive;

    public Human(String player, boolean isAlive){
        this.player = player;
        this.isAlive = isAlive;
    }

    public String getPlayerName() {
        return this.player;
    }

    public ChatColor getNameTagColor(){
        return ChatColor.GREEN;
    }

    public boolean isAlive(){
        return this.isAlive;
    }


    public void setAliveStatus(boolean alive){
        this.isAlive = alive;
    }


}
