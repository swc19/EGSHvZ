package com.egsrit.hvz.listeners;

import com.egsrit.hvz.players.Human;
import com.egsrit.hvz.players.HvzZombie;
import com.egsrit.hvz.util.Stats;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;

public class TagListener implements Listener {

    @EventHandler
    public void onTag(EntityDamageByEntityEvent e){
        HashMap<String, Human> humanList = Stats.getHumans();
        HashMap<String, HvzZombie> zombieList = Stats.getZombies();

        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
            e.setCancelled(true);
            Player entity = (Player) e.getEntity();
            Player damager = (Player) e.getDamager();
            String entitySpecial = zombieList.get(entity.getDisplayName()).getSpecialStatus(); // can be null, if human
            String damagerSpecial = zombieList.get(damager.getDisplayName()).getSpecialStatus();
            if((damagerSpecial.equals("Witch") || damagerSpecial.equals("Twitch")) && entitySpecial.equals("Zombie")){
                StunListener.stunZombie(entity, damager, damagerSpecial);
            }
            if(zombieList.get(damager.getDisplayName()).canTagHumans()){ // is Zombie stunned?
                if(humanList.get(entity.getDisplayName()).getBodyArmor()){ // does Human have body armor?
                    //break body armor, alert zombie that human had body armor, give human some cooldown on body armor
                } else {
                    Stats.getHumans().get(entity.getDisplayName()).setAliveStatus(false); // kill human
                    Stats.addZombie(entity, 300, "Zombie"); // make human a zombie
                    Stats.addTag(entity, damager); // register the tag
                    System.out.println(entity.getDisplayName() + " has been tagged by " +
                            zombieList.get(damager.getDisplayName()).getNameTagColor() +
                            zombieList.get(damager.getDisplayName()).getSpecialStatus() + " " +
                            damager.getDisplayName() + "!");
                }
            } else if(!zombieList.get(damager.getDisplayName()).canTagHumans()) {
                damager.sendMessage("You are stunned and cannot tag!");
            } // else statement would be human attacking human
        }
    }
}
