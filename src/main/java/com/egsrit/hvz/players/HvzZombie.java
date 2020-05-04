package com.egsrit.hvz.players;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HvzZombie {
    private Player player;
    private int stunTime;
    private String specialStatus;
    private boolean canBeStunned;
    private boolean canTag;

    public HvzZombie(Player player, int stunTime, String specialStatus, boolean canBeStunned, boolean canTag){
        this.player = player;
        this.stunTime = stunTime;
        this.specialStatus = specialStatus;
        this.canBeStunned = canBeStunned;
        this.canTag = canTag;
    }
    public Player getZombie() {
        return this.player;
    }
    public int getStunTime(){
        return this.stunTime;
    }
    public boolean canBeStunned(){
        return this.canBeStunned;
    }

    public String getSpecialStatus(){
        return this.specialStatus;
    }
    public boolean canTagHumans(){
        return this.canTag;
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
    public void setStun(boolean stun){
        this.canBeStunned = stun;
    }
    public void setSpecialStatus(String specialStatus){
        this.specialStatus = this.specialStatus;
    }
}
