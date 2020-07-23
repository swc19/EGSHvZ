package com.egsrit.hvz.listeners;

import com.egsrit.hvz.players.Human;
import com.egsrit.hvz.players.HvzZombie;
import com.egsrit.hvz.util.PlayerScoreboard;
import com.egsrit.hvz.util.Stats;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagListener implements Listener {

    private static final Map<String, Long> gracePeriod = new HashMap<>();

    @EventHandler
    public void onTag(EntityDamageByEntityEvent e){
        Map<String, Human> humanList = Stats.getHumans();
        Map<String, HvzZombie> zombieList = Stats.getZombies();


        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
            // Is player hitting a player?
            Player entity = (Player) e.getEntity();
            Player damager = (Player) e.getDamager();
            if(entity != damager){
                // ^ this will probably never be the case but never know
                if(humanList.containsKey(entity.getDisplayName()) || humanList.containsKey(damager.getDisplayName())) {
                    // event gets cancelled if anybody hits a human, so there's no human v human damage
                    // also gets cancelled if a human is brave enough to hit a zombie, though nothing will happen
                    e.setCancelled(true);
                    if (zombieList.containsKey(damager.getDisplayName()) && humanList.containsKey(entity.getDisplayName())) {
                        // only actually begin to register a tag if it's a zombie hitting a human
                        if (!Stats.getCooldowns().containsKey(damager.getDisplayName()) || Stats.getStunCooldown(damager.getDisplayName()) <= System.currentTimeMillis() / 1000) {
                            // check if the zombie's stunned
                            if(!gracePeriod.containsKey(entity.getDisplayName()) || gracePeriod.get(entity.getDisplayName()) <= System.currentTimeMillis()){
								//Check if entity in safe zone
								if(nearSafeZone(entity, 3)){
                                    damager.sendMessage(ChatColor.GREEN + entity.getDisplayName() + ChatColor.GOLD + " is in a safe zone and can't be tagged!");
                                    return;
                                }
                                if (Stats.getHumans().get(entity.getDisplayName()).playerHasBodyArmor() && entity.getInventory().getChestplate() != null) {
                                    // Check if human is wearing body armor
                                    damager.sendMessage(ChatColor.GOLD + "You tried tagging " + ChatColor.GREEN + entity.getDisplayName()
                                            + ChatColor.GOLD + ", but they had Body Armor on!");
                                    entity.sendMessage(ChatColor.GOLD + "You were tagged, but your Body Armor saved you! It's broken now.");
                                    entity.getInventory().setItem(38, new ItemStack(Material.AIR));
                                    entity.playSound(entity.getLocation(), Sound.ENTITY_ITEM_BREAK, 0.8f, 1.0f);
                                    // Give the human a 5 second grace period to try and stun the zombie or just get out of danger
                                    // so the zombie can't double-tag, can increase grace period
                                    gracePeriod.put(entity.getDisplayName(), System.currentTimeMillis() + 5000);
                                    Stats.getHumans().get(entity.getDisplayName()).setHasBodyArmor(false);
                                } else {
                                    damager.playSound(damager.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.8f, 1.0f);
                                    entity.playSound(entity.getLocation(), Sound.ENTITY_ZOMBIE_AMBIENT, 0.8f, 1.0f);

                                    Stats.addZombie(entity.getDisplayName(), 300, "Zombie", entity); // make human a zombie
                                    Stats.addTag(entity.getDisplayName(), damager.getDisplayName()); // register the tag
                                    PlayerScoreboard.updateBoard(entity); // update scoreboards
                                    PlayerScoreboard.updateBoard(damager);
                                    Bukkit.broadcastMessage(ChatColor.GOLD + entity.getDisplayName() + " has been tagged by " +
                                            zombieList.get(damager.getDisplayName()).getNameTagColor() +
                                            zombieList.get(damager.getDisplayName()).getSpecialStatus() + " " +
                                            damager.getDisplayName() + "!"); // might not be a broadcast if too many tags
                                }
                            } else {
                                damager.sendMessage(ChatColor.RED + "You can't tag this human yet!");
                            }
                        } else {
                            // zombie is stunned
                            damager.sendMessage(ChatColor.RED + "You are stunned and cannot tag!");
                            damager.sendMessage(ChatColor.GOLD + "Stun time remaining: " + ChatColor.RED + (Stats.getStunCooldown(damager.getDisplayName()) - System.currentTimeMillis() / 1000) + " seconds");
                        }
                    }
                }
            }
        }
    }

	
	public boolean nearSafeZone(Player p, int radius){
		//TODO
		//Iterate through safezones, compare player distance
		//If within pre-set radius, return true
		//Uses m a t h
        List<Location> safeZones = SpecialItemListener.getSafeZones();
        return false;
	}
}
