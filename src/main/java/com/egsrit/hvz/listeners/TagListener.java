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
            Player entity = (Player) e.getEntity();
            Player damager = (Player) e.getDamager();
            if(entity != damager){
                String entitySpecial = "";
                String damagerSpecial = "";
                if(zombieList.containsKey(entity.getDisplayName())){
                    entitySpecial = zombieList.get(entity.getDisplayName()).getSpecialStatus();
                }
                if(zombieList.containsKey(damager.getDisplayName())){
                    damagerSpecial = zombieList.get(damager.getDisplayName()).getSpecialStatus();
                }
                if((damagerSpecial.equals("Witch") || damagerSpecial.equals("Twitch")) && entitySpecial.equals("Zombie")){
                    e.setCancelled(true);
                    StunListener.stunZombie(entity, damager);
                }
                if(humanList.containsKey(entity.getDisplayName()) && humanList.get(entity.getDisplayName()).isAlive()) {
                    e.setCancelled(true);
                    if (zombieList.containsKey(damager.getDisplayName()) && (!humanList.containsKey(damager.getDisplayName()) || !humanList.get(damager.getDisplayName()).isAlive())) {
                        // is the person being tagged a human and are they alive?
                        if (!Stats.getCooldowns().containsKey(damager.getDisplayName()) || Stats.getStunCooldown(damager.getDisplayName()) <= System.currentTimeMillis() / 1000) { // is Zombie stunned?
                            if (humanList.get(entity.getDisplayName()).getBodyArmor()) { // does Human have body armor?
                                //break body armor, alert zombie that human had body armor, give human some cooldown on body armor
                            } else {
                                Stats.addZombie(entity.getDisplayName(), 300, "Zombie", entity); // make human a zombie
                                Stats.addTag(entity.getDisplayName(), damager.getDisplayName()); // register the tag
                                PlayerScoreboard.updateBoard(entity);
                                PlayerScoreboard.updateBoard(damager);
                                Bukkit.broadcastMessage(ChatColor.GOLD + entity.getDisplayName() + " has been tagged by " +
                                        zombieList.get(damager.getDisplayName()).getNameTagColor() +
                                        zombieList.get(damager.getDisplayName()).getSpecialStatus() + " " +
                                        damager.getDisplayName() + "!");
                            }
                        } else {
                            e.setCancelled(true);
                            damager.sendMessage(ChatColor.RED + "You are stunned and cannot tag!");
                            damager.sendMessage(ChatColor.GOLD + "Stun time remaining: " + ChatColor.RED + (Stats.getStunCooldown(damager.getDisplayName()) - System.currentTimeMillis() / 1000) + " seconds");
                        }
                    }
                }
            }
        }
    }
}
