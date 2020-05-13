package com.egsrit.hvz.listeners;

import com.codingforcookies.armorequip.ArmorEquipEvent;
import com.egsrit.hvz.util.Stats;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class ArmorListener implements Listener {

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent e){
        // Uses ArmorEquip dependency to tell if a player is equipping/unequipping armor in any way
        Player player = e.getPlayer();
        int stunTimer = 300;
        if(e.getNewArmorPiece() != null && e.getNewArmorPiece().getType() != Material.AIR){
            if(e.getNewArmorPiece().getType() == Material.LEATHER_CHESTPLATE){
                if(e.getNewArmorPiece().hasItemMeta()){ // All custom spawned shirts will have meta
                    ItemStack chestplate = e.getNewArmorPiece();
                    String[] shirtName = chestplate.getItemMeta().getDisplayName().split(" ");
                    if(shirtName[1].equals("Shirt")) {
                        switch (ChatColor.stripColor(shirtName[0])) {
                            // Only check for special zombie shirts
                            case "Witch":
                            case "Twitch":
                                stunTimer = 10;
                                break;
                            case "Jackal":
                            case "Tank":
                                stunTimer = 150;
                                break;
                            case "Boomer":
                                break;
                            default:
                                return;
                        }
                        // Make the wearer of the special shirt that type of zombie
                        Stats.addZombie(player.getDisplayName(), stunTimer, shirtName[0], player);
                    }
                }
            }
        } else {
            if(e.getOldArmorPiece() != null && e.getOldArmorPiece().getType() != Material.AIR){
                if(e.getOldArmorPiece().getType() == Material.LEATHER_CHESTPLATE){
                    if(e.getOldArmorPiece().hasItemMeta()){
                        if(e.getOldArmorPiece().getItemMeta().getDisplayName().split(" ")[1].equals("Shirt")){
                            // When unequipping a special shirt, make the unequipper a regular zombie again
                            Stats.addZombie(player.getDisplayName(), 300, "Zombie", player);
                        }
                    }
                }
            }
        }
    }
}
