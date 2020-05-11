package com.egsrit.hvz.listeners;

import com.egsrit.hvz.util.Stats;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MiscListener implements Listener {
    private static final Map<String, Boolean> hasSpecialShirt = new HashMap<>();
    private static final Map<String, ItemStack> specialShirtStatus = new HashMap<>();

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        // This has a lot of overhead and will need to be fixed to a better event to determine if a player is equipping armor
        Player player = e.getPlayer();
        int stunTimer = 300;
        if(player.getInventory().getChestplate() != null){
            if(!hasSpecialShirt.containsKey(player.getDisplayName()) || !hasSpecialShirt.get(player.getDisplayName())){
                ItemStack chestplate = player.getInventory().getChestplate();
                String[] shirtName = chestplate.getItemMeta().getDisplayName().split(" ");
                if(shirtName[1].equals("Shirt")){
                    switch(shirtName[0]){
                        case "Witch":
                        case "Twitch":
                            stunTimer = 10;
                            break;
                        case "Jackal":
                        case "Tank":
                            stunTimer = 150;
                            break;
                        case "Boomer":
                        case "Zombie":
                            stunTimer = 300;
                            break;
                    }
                    Stats.addZombie(player.getDisplayName(), stunTimer, shirtName[0], player);
                    hasSpecialShirt.put(player.getDisplayName(), Boolean.TRUE);
                    specialShirtStatus.put(player.getDisplayName(), chestplate);

                }
            } else {
                if ((player.getInventory().getChestplate() == null) || !player.getInventory().getChestplate().equals(specialShirtStatus.get(player.getDisplayName()))) {
                    hasSpecialShirt.put(player.getDisplayName(), false);
                }
            }
        } else {
            if(hasSpecialShirt.containsKey(player.getDisplayName())){
                if(!Stats.getZombies().get(player.getDisplayName()).getSpecialStatus().equals("Zombie")){
                    Stats.addZombie(player.getDisplayName(), 300, "Zombie", player);
                }
            }
        }
    }
}
