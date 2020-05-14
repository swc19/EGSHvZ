package com.egsrit.hvz.players;

import com.egsrit.hvz.items.SpecialItems;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Human{
    private final String player;
    private boolean hasBodyArmor;

    public Human(String player, boolean hasBodyArmor){
        this.player = player;
        this.hasBodyArmor = hasBodyArmor;
    }

    public String getPlayerName() {
        return this.player;
    }

    public ChatColor getNameTagColor(){
        return ChatColor.GREEN;
    }

    public boolean playerHasBodyArmor(){
        return this.hasBodyArmor;
    }

    public void setHasBodyArmor(boolean bodyarmor){
        this.hasBodyArmor = bodyarmor;
    }

    public static void giveItems(Player p){
        //p.getInventory().clear(); // Clears everything in inventory
        p.getInventory().addItem(SpecialItems.makeBlaster());
        p.getInventory().addItem(SpecialItems.makeSock(10));
    }

}
