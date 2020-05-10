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

import java.util.Map;

public class StunListener implements Listener {
    private static final Map<String, Human> humanList = Stats.getHumans();
    private static final Map<String, HvzZombie> zombieList = Stats.getZombies();

    @EventHandler
    public void onStunTag(EntityDamageByEntityEvent e){ // Witch/Twitch hits zombie
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
            Player entity = (Player) e.getEntity(); // the Zombie
            Player damager = (Player) e.getDamager(); // the Witch/Twitch
            String entitySpecialStatus = zombieList.get(entity.getDisplayName()).getSpecialStatus();
            String damagerSpecialStatus = zombieList.get(damager.getDisplayName()).getSpecialStatus();
            if((damagerSpecialStatus.equals("Witch") || damagerSpecialStatus.equals("Twitch")) && zombieList.get(entity.getDisplayName()) != null){
                if(Stats.getStunCooldown(damager.getDisplayName()) <= System.currentTimeMillis()/1000) {
                    if (Stats.getStunCooldown(entity.getDisplayName()) <= System.currentTimeMillis() / 1000) {
                        e.setCancelled(true);
                        stunZombie(entity, damager, entitySpecialStatus);
                        Stats.addStun(entity.getDisplayName(), damager.getDisplayName());
                    } else {
                        damager.sendMessage("Zombie " + entity.getDisplayName() + " is already stunned!");
                    }
                } else {
                    damager.sendMessage("You are currently stunned and cannot stun!");
                }
            }
        }
    }

    @EventHandler
    public void onStunSnowball(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Snowball){
            Player zombie = (Player) e.getEntity();
            Snowball snowball = (Snowball) e.getDamager();
            if(snowball.getShooter() instanceof Player) {
                Player damager = (Player) snowball.getShooter();
                if(humanList.get(damager.getDisplayName()).isAlive()){
                    // All special zombies can be stunned by a snowball/sock
                    if(Stats.getStunCooldown(zombie.getDisplayName()) <= System.currentTimeMillis()/1000){
                        e.setCancelled(true);
                        stunZombie(zombie, damager, null); // only humans will be in this action
                        Stats.addStun(zombie.getDisplayName(), damager.getDisplayName());
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
                    if(Stats.getStunCooldown(zombie.getDisplayName()) <= System.currentTimeMillis()/1000){
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
                        damager.sendMessage(ChatColor.RED + "You cannot stun this " + zombieList.get(zombie.getDisplayName()).getSpecialStatus() + " yet!");
                    }
                }
            }
        }
    }
    public static void stunZombie(Player zombie, Player damager, String special){
        ChatColor color;
        String message;
        if(special == null){ // human stunned zombie
            color = humanList.get(damager.getDisplayName()).getNameTagColor();
            message = "";
        }
        else{ // witch/twitch stunned zombie
            color = zombieList.get(damager.getDisplayName()).getNameTagColor();
            message = zombieList.get(damager.getDisplayName()).getSpecialStatus() + " ";
        }
        Long stunCooldown = Stats.getStunCooldown(zombie.getDisplayName());
        if(stunCooldown != null){ // If player has been stunned before
            if(stunCooldown > System.currentTimeMillis()/1000){
                damager.sendMessage(ChatColor.RED + zombie.getDisplayName() + " is already stunned!");
                return;
            }
        }
        damager.sendMessage(ChatColor.GREEN + "You've stunned " + zombie.getDisplayName() + "!");
        Stats.setStunCooldown(zombie.getDisplayName(), System.currentTimeMillis()/1000 + zombieList.get(zombie.getDisplayName()).getStunTime());
        zombie.sendMessage(ChatColor.RED + "You've been stunned by " + color + message + damager.getDisplayName() + ChatColor.RED + "!");
    }
}
