package com.egsrit.hvz.listeners;

import com.egsrit.hvz.players.Human;
import com.egsrit.hvz.players.HvzZombie;
import com.egsrit.hvz.util.Stats;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class TagListener implements Listener {

    @EventHandler
    public void onTag(EntityDamageByEntityEvent e){
        // Needs special case for witch also
        if(e.getEntity() instanceof Human && e.getDamager() instanceof HvzZombie){
            // Check for body armor
            Human human = (Human) e.getEntity();
            HvzZombie hvzZombie = (HvzZombie) e.getDamager();
            human.setAliveStatus(0);
            HvzZombie newHvzZombie = new HvzZombie((Player) e.getEntity(), 300);
            Stats.addZombie(newHvzZombie);
            Stats.addTag(human, hvzZombie);
            System.out.println(((Player) e.getEntity()).getDisplayName() + " has been tagged by " + ((Player) e.getDamager()).getDisplayName() + "!");
        }
    }
}
