package com.egsrit.hvz.listeners;

import com.egsrit.hvz.players.Human;
import com.egsrit.hvz.players.HvzZombie;
import com.egsrit.hvz.util.Stats;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;

public class StunListener implements Listener {
    private static HashMap<String, Human> humanList = Stats.getHumans();
    private static HashMap<String, HvzZombie> zombieList = Stats.getZombies();
    // needs another event handler for seeing which weapon was used to stun the zombie (blaster, sock, elephant blaster)

    @EventHandler
    public void onStunTag(EntityDamageByEntityEvent e){ // Witch/Twitch hits zombie
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
            e.setCancelled(true);
            Player entity = (Player) e.getEntity(); // the Zombie
            Player damager = (Player) e.getDamager(); // the Witch/Twitch
            String entitySpecialStatus = zombieList.get(entity.getDisplayName()).getSpecialStatus();
            String damagerSpecialStatus = zombieList.get(damager.getDisplayName()).getSpecialStatus();
            if((damagerSpecialStatus.equals("Witch") || damagerSpecialStatus.equals("Twitch")) && zombieList.get(entity.getDisplayName()) != null){
                e.setCancelled(true);
                if(zombieList.get(entity.getDisplayName()).canBeStunned()){
                    stunZombie(entity, damager, entitySpecialStatus);
                    Stats.addStun(entity, damager);
                }
                else {
                    damager.sendMessage("Zombie " + entity.getDisplayName() + " is already stunned!");
                }
            }
        }
    }

    @EventHandler
    public void onStunSnowball(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Snowball){
            e.setCancelled(true);
            Player zombie = (Player) e.getEntity();
            Snowball snowball = (Snowball) e.getDamager();
            if(snowball.getShooter() instanceof Player) {
                Player damager = (Player) snowball.getShooter();
                if(humanList.get(damager.getDisplayName()).isAlive()){
                    // All special zombies can be stunned by a snowball/sock
                    if(zombieList.get(zombie.getDisplayName()).canBeStunned()){
                        e.setCancelled(true);
                        stunZombie(zombie, damager, null); // only humans will be in this action
                        Stats.addStun(zombie, damager);
                    }
                    else {
                        damager.sendMessage("You cannot stun this " + zombieList.get(zombie.getDisplayName()).getSpecialStatus() + " yet!");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onStunArrow(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Arrow){
            Player zombie = (Player) e.getEntity();
            Arrow arrow = (Arrow) e.getDamager();
            if(arrow.getShooter() instanceof Player){
                Player damager = (Player) arrow.getShooter();
                if(humanList.get(damager.getDisplayName()).isAlive()){
                    if(zombieList.get(zombie.getDisplayName()).canBeStunned()){
                        e.setCancelled(true);
                        String specialStatus = zombieList.get(zombie.getDisplayName()).getSpecialStatus();
                        switch(specialStatus){
                            case "Tank":
                            case "Twitch":
                                damager.sendMessage("You cannot stun this " + specialStatus + " with a blaster!");
                                break;
                            default:
                                stunZombie(zombie, damager, specialStatus);
                        }
                    }
                    else {
                        damager.sendMessage("You cannot stun this " + zombieList.get(zombie.getDisplayName()).getSpecialStatus() + " yet!");
                    }
                }
            }
        }
    }
    public static void stunZombie(Player zombie, Player damager, String special){
        ChatColor color;
        String message;
        if(special == (null)){ // human stunned zombie
            color = humanList.get(damager.getDisplayName()).getNameTagColor();
            message = "";
        }
        else{ // witch/twitch stunned zombie
            color = zombieList.get(damager.getDisplayName()).getNameTagColor();
            message = zombieList.get(damager.getDisplayName()).getSpecialStatus() + " ";
        }
        zombieList.get(zombie.getDisplayName()).setStun(true);
        zombie.sendMessage("You've been stunned by " + color + message + damager.getDisplayName() + "!");
        //use zombie's stun timer, make them unable to hit players/tag them
    }
}
