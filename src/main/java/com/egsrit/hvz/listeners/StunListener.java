package com.egsrit.hvz.listeners;

import com.egsrit.hvz.players.Human;
import com.egsrit.hvz.players.HvzZombie;
import com.egsrit.hvz.util.Stats;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;

public class StunListener implements Listener {
    HashMap<String, Human> humanList = Stats.getHumans();
    HashMap<String, HvzZombie> zombieList = Stats.getZombies();
    // needs another event handler for seeing which weapon was used to stun the zombie (blaster, sock, elephant blaster)

    @EventHandler
    public void onStun(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
            Player entity = (Player) e.getEntity(); // the Zombie
            Player damager = (Player) e.getDamager(); // the Human or Witch/Twitch
            boolean damagerAlive = humanList.get(damager.getDisplayName()).isAlive(); // true if human, false if witch/twitch
            String entitySpecialStatus = zombieList.get(entity.getDisplayName()).getSpecialStatus(); // used for special zombie stunning, null if human
            String damagerSpecialStatus = zombieList.get(damager.getDisplayName()).getSpecialStatus();
            if((damagerAlive && zombieList.get(entity.getDisplayName()) != null) ||
                    ((damagerSpecialStatus.equals("Witch") || damagerSpecialStatus.equals("Twitch")) && zombieList.get(entity.getDisplayName()) != null)){
                if(zombieList.get(entity.getDisplayName()).canBeStunned()){
                    // check item used
                    stunZombie(entity);
                    Stats.addStun(entity, damager);
                }
                else {
                    damager.sendMessage("Zombie " + entity.getDisplayName() + " is already stunned!");
                }
            }
        }
    }
    public void stunZombie(Player p){
        //use zombie's stun timer, make them unable to hit players/tag them
    }
}
