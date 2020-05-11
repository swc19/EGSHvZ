package com.egsrit.hvz.listeners;

import com.codingforcookies.armorequip.ArmorEquipEvent;
import com.egsrit.hvz.util.Stats;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ArmorListener implements Listener {
    private static final Map<String, Boolean> hasSpecialShirt = new HashMap<>();

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent e){
        Player player = e.getPlayer();
        int stunTimer = 300;
        if(e.getNewArmorPiece() != null && e.getNewArmorPiece().getType() != Material.AIR){
            if(!hasSpecialShirt.containsKey(player.getDisplayName()) || !hasSpecialShirt.get(player.getDisplayName())){
                if(e.getNewArmorPiece().getType() == Material.LEATHER_CHESTPLATE){
                    if(e.getNewArmorPiece().hasItemMeta()){
                        ItemStack chestplate = e.getNewArmorPiece();
                        String[] shirtName = chestplate.getItemMeta().getDisplayName().split(" ");
                        if(shirtName[1].equals("Shirt")) {
                            switch (ChatColor.stripColor(shirtName[0])) {
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
                            Stats.addZombie(player.getDisplayName(), stunTimer, shirtName[0], player);
                            hasSpecialShirt.put(player.getDisplayName(), true);
                        }
                    }
                }
            }
        } else {
            if(e.getOldArmorPiece() != null && e.getOldArmorPiece().getType() != Material.AIR){
                if(e.getOldArmorPiece().getType() == Material.LEATHER_CHESTPLATE){
                    if(e.getOldArmorPiece().hasItemMeta()){
                        hasSpecialShirt.put(player.getDisplayName(), false);
                        Stats.addZombie(player.getDisplayName(), 300, "Zombie", player);
                    }
                }
            }
        }
    }
}
