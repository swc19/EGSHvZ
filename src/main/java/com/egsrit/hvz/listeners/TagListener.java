package com.egsrit.hvz.listeners;

import com.egsrit.hvz.players.Human;
import com.egsrit.hvz.players.HvzZombie;
import com.egsrit.hvz.util.Stats;
import org.bukkit.Bukkit;
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
            String entitySpecial = zombieList.get(entity.getDisplayName()).getSpecialStatus(); // can be null, if human
            String damagerSpecial = zombieList.get(damager.getDisplayName()).getSpecialStatus();
            if((damagerSpecial.equals("Witch") || damagerSpecial.equals("Twitch")) && entitySpecial.equals("Zombie")){
                StunListener.stunZombie(entity, damager, damagerSpecial);
            }
            if(humanList.get(entity.getDisplayName()) != null && humanList.get(entity.getDisplayName()).isAlive()){
                // is the person being tagged a human and are they alive?
                if (Stats.getStunCooldown(damager.getDisplayName()) <= System.currentTimeMillis() / 1000) { // is Zombie stunned?
                    if (humanList.get(entity.getDisplayName()).getBodyArmor()) { // does Human have body armor?
                        //break body armor, alert zombie that human had body armor, give human some cooldown on body armor
                    } else {
                        e.setCancelled(true);
                        Stats.getHumans().get(entity.getDisplayName()).setAliveStatus(false); // kill human
                        Stats.addZombie(entity.getDisplayName(), 300, "Zombie", entity); // make human a zombie
                        Stats.addTag(entity.getDisplayName(), damager.getDisplayName()); // register the tag
                        Bukkit.broadcastMessage(entity.getDisplayName() + " has been tagged by " +
                                zombieList.get(damager.getDisplayName()).getNameTagColor() +
                                zombieList.get(damager.getDisplayName()).getSpecialStatus() + " " +
                                damager.getDisplayName() + "!");
                    }
                } else if (Stats.getStunCooldown(damager.getDisplayName()) > System.currentTimeMillis() / 1000) {
                    damager.sendMessage("You are stunned and cannot tag!");
                }
            } // else statement would be human attacking human
        }
    }
}
