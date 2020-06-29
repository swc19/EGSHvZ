package com.egsrit.hvz.listeners;

import com.egsrit.hvz.util.Stats;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpecialItemListener implements Listener {

    private static final Map<String, Boolean> avUsed = new HashMap<>();
	private static final List<Location> safeZones = new ArrayList<>();
	private static final List<List<Location>> biocadeLocations = new ArrayList<>();

    @EventHandler
    public void onSpecialItem(PlayerInteractEvent e){
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
            Player p = e.getPlayer();
            EquipmentSlot equip = e.getHand();
            if(equip.equals(EquipmentSlot.HAND)){
                // fire this event once for the main hand
                ItemStack mainHandItem = p.getInventory().getItemInMainHand();
                if(mainHandItem.hasItemMeta()){
                    if(mainHandItem.getItemMeta().hasDisplayName()){
						String itemName = ChatColor.stripColor(mainHandItem.getItemMeta().getDisplayName());
						switch(itemName){
							case "Antivirus":
								doAntiVirus(p, mainHandItem);
								break;
							case "Deplorable Cover":
								doDeplorableCover(p, e);
								break;
							case "Deployable Cover":
								doDeployableCover(p, e);
								break;
							case "Biocade":
								doBiocade(p, e);
								break;
						}
                    }
                }
            }
        }
    }
	/*
	@EventHandler
	public void onMoveInBiocade(PlayerMoveEvent e){
		//TODO
		// Check player location, compare to biocade, push out or halt movement if so
		if(biocadeLocations.size() > 0){
			
		}
	}
	*/
	public void doAntiVirus(Player p, ItemStack mainHandItem){
		if(Stats.getZombies().containsKey(p.getDisplayName())){
			if(!avUsed.containsKey(p.getDisplayName())){
				Stats.addHuman(p.getDisplayName(), p);
				avUsed.put(p.getDisplayName(), true);
				p.getInventory().remove(mainHandItem);
				// On the off chance a special zombie uses an antivirus, remove their shirt
				p.getInventory().setItem(38, new ItemStack(Material.AIR));
			} else {
				p.sendMessage(ChatColor.RED + "You've already used an antivirus!");
			}
		} else if (Stats.getHumans().containsKey(p.getDisplayName())){
			if(!avUsed.containsKey(p.getDisplayName())){
				p.sendMessage(ChatColor.RED + "You're a human and can't use this... yet.");
			} else {
				p.sendMessage(ChatColor.RED + "You've already used an antivirus!");
			}
		}
	}
	
	public void doDeplorableCover(Player p, PlayerInteractEvent e){ // May change event param to location for boomer drop
		//TODO deplorable cover for zombies
		//Place yellow concrete (traffic cone) at location of event (or on the player's location if in air)
		//Put up holo text (spawn armor stand and name it) with a timer for how long it'll last
		//Check back with another event if a player rclicks it to remove their timer
		//make runnable to destroy
	
	}
	
	public void doDeployableCover(Player p, PlayerInteractEvent e){
		//TODO deployable cover for humans
		//Place green (red?) concrete at location of event (or on player if in air)
		//Put up holo text with timer left
		//place other color concrete around the center to identify the safezone
		//put safezone center in list
		//make runnable to destroy 
	}
	
	public void doBiocade(Player p, PlayerInteractEvent e){
		//TODO biocade
		// have player pick first point, then second
		// check distance between the two, if too far then prompt the player to place again
		// shift click to reset first point?
		// find shortest straight path between two points, log every location to a list, log to main biocade list
		// place anchor points at endpoints (black concrete, 3 high), cobweb or something in between
		// holo text for how long it lasts
		// what to do for visual?
		// if possible, cobwebs and remove zombie slowness through them?
		// new eventhandler for movement, only use when a biocade is active to save on resources
		// check if (only) human is trying to enter biocade location, kick them out if so
		// this will be the hardest item to implement cause math
	
	}
	public static List<Location> getSafeZones(){
		return safeZones;
	}
	
	public List<List<Location>> getBiocadeLocations(){
		return biocadeLocations;
	}
	
}
