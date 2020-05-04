package com.egsrit.hvz.players;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Human{
    private final Player player;
    private boolean isAlive;
    private boolean hasBodyArmor;

    public Human(Player player, boolean isAlive, boolean hasBodyArmor){
        this.player = player;
        this.isAlive = isAlive;
        this.hasBodyArmor = hasBodyArmor;
    }

    public Player getHuman() {
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

    public void setAliveStatus(boolean alive){
        this.isAlive = alive;
    }


}
