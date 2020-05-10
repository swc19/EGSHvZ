package com.egsrit.hvz.players;

import org.bukkit.ChatColor;

public class Human{
    private final String player;
    private boolean isAlive;
    private boolean hasBodyArmor;

    public Human(String player, boolean isAlive, boolean hasBodyArmor){
        this.player = player;
        this.isAlive = isAlive;
        this.hasBodyArmor = hasBodyArmor;
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

    public boolean getBodyArmor(){
        return this.hasBodyArmor;
    }

    public void setBodyArmor(boolean bodyArmor){
        this.hasBodyArmor = bodyArmor;
    }

    public void setAliveStatus(boolean alive){
        this.isAlive = alive;
    }


}
