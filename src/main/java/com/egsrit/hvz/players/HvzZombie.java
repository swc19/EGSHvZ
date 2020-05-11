package com.egsrit.hvz.players;

import org.bukkit.ChatColor;

public class HvzZombie {
    private final String player;
    private int stunTime;
    private String specialStatus;

    public HvzZombie(String player, int stunTime, String specialStatus){
        this.player = player;
        this.stunTime = stunTime;
        this.specialStatus = specialStatus;

    }
    public int getStunTime(){
        return this.stunTime;
    }
    public String getSpecialStatus(){
        return this.specialStatus;
    }
    public ChatColor getNameTagColor(){
        switch(this.specialStatus){
            case "Boomer":
                return ChatColor.YELLOW;
            case "Jackal":
                return ChatColor.WHITE;
            case "Tank":
                return ChatColor.LIGHT_PURPLE;
            case "Twitch":
                return ChatColor.DARK_PURPLE;
            case "Witch":
                return ChatColor.BLUE;
            default:
                return ChatColor.RED;
        }
    }
}
