package com.egsrit.hvz.listeners;

import com.egsrit.hvz.players.Human;
import com.egsrit.hvz.players.HvzZombie;
import com.egsrit.hvz.util.PlayerScoreboard;
import com.egsrit.hvz.util.Stats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;

public class TagListener implements Listener {

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
                if(humanList.containsKey(entity.getDisplayName())) {
                    // event gets cancelled if anybody hits a human, so there's no human v human damage
                    e.setCancelled(true);
                    if (zombieList.containsKey(damager.getDisplayName()) && humanList.containsKey(entity.getDisplayName())) {
                        // only actually begin to register a tag if it's a zombie hitting a human
                        if (!Stats.getCooldowns().containsKey(damager.getDisplayName()) || Stats.getStunCooldown(damager.getDisplayName()) <= System.currentTimeMillis() / 1000) {
                            // check if the zombie's stunned
                            if (false) {
                                // TODO function to check human's body armor, then logic after to break/cooldown
                            } else {
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
                            // zombie is stunned
                            damager.sendMessage(ChatColor.RED + "You are stunned and cannot tag!");
                            damager.sendMessage(ChatColor.GOLD + "Stun time remaining: " + ChatColor.RED + (Stats.getStunCooldown(damager.getDisplayName()) - System.currentTimeMillis() / 1000) + " seconds");
                        }
                    }
                }
            }
        }
    }
}
