package com.egsrit.hvz.listeners;

import com.egsrit.hvz.players.Human;
import com.egsrit.hvz.players.Zombie;
import com.egsrit.hvz.util.Stats;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class TagListener implements Listener {

    @EventHandler
    public void onTag(EntityDamageByEntityEvent e){
        // Needs special case for witch also
        if(e.getEntity() instanceof Human && e.getDamager() instanceof Zombie){
            // Check for body armor
            Human human = (Human) e.getEntity();
            Zombie zombie = (Zombie) e.getDamager();
            human.setAliveStatus(0);
            Zombie newZombie = new Zombie((Player) e.getEntity(), 300);
            Stats.addZombie(newZombie);
            Stats.addTag(human, zombie);
            System.out.println(((Player) e.getEntity()).getDisplayName() + " has been tagged by " + ((Player) e.getDamager()).getDisplayName() + "!");
        }
    }
}
