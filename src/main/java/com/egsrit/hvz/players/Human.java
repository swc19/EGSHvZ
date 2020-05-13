package com.egsrit.hvz.players;

import com.egsrit.hvz.items.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

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

    public static void giveItems(Player p){
        //p.getInventory().clear(); // Clears everything in inventory
        ItemStack blaster = new ItemBuilder(Material.BOW, 1)
                .setName(ChatColor.BLUE + "" + ChatColor.BOLD + "Blaster")
                .setLore("Your very own blaster!")
                .build();

        ItemStack socks = new ItemBuilder(Material.SNOWBALL, 10)
                .setName(ChatColor.BLUE + "" + ChatColor.BOLD + "Sock")
                .setLore("Throw it at a zombie!")
                .hideFlags(ItemFlag.HIDE_ATTRIBUTES)
                .build();

        p.getInventory().addItem(blaster);
        p.getInventory().addItem(socks);
    }

}
