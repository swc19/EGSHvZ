package com.egsrit.hvz.listeners;

import com.egsrit.hvz.players.Human;
import com.egsrit.hvz.players.HvzZombie;
import com.egsrit.hvz.util.PlayerScoreboard;
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
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player && e.getEntity() != e.getDamager()){
            Player entity = (Player) e.getEntity(); // the Zombie
            Player damager = (Player) e.getDamager(); // the Witch/Twitch
            String entitySpecialStatus = "";
            String damagerSpecialStatus = "";
            if(zombieList.containsKey(entity.getDisplayName())){
                // These cancel events are kinda weird, should change around to be able to cancel once without disabling pvp everywhere
                e.setCancelled(true);
                entitySpecialStatus = ChatColor.stripColor(zombieList.get(entity.getDisplayName()).getSpecialStatus());
            }
            if(zombieList.containsKey(damager.getDisplayName())){
                e.setCancelled(true);
                damagerSpecialStatus = ChatColor.stripColor(zombieList.get(damager.getDisplayName()).getSpecialStatus());
            }
            if((damagerSpecialStatus.equals("Witch") || damagerSpecialStatus.equals("Twitch")) && zombieList.get(entity.getDisplayName()) != null
                    && (!humanList.containsKey(entity.getDisplayName()) || !humanList.get(entity.getDisplayName()).isAlive())){
                e.setCancelled(true);
                if(!Stats.getCooldowns().containsKey(damager.getDisplayName()) || Stats.getStunCooldown(damager.getDisplayName()) <= System.currentTimeMillis()/1000) {
                    if (!Stats.getCooldowns().containsKey(entity.getDisplayName()) || Stats.getStunCooldown(entity.getDisplayName()) <= System.currentTimeMillis() / 1000) {
                        stunZombie(entity, damager);
                        Stats.addStun(entity.getDisplayName(), damager.getDisplayName());
                        PlayerScoreboard.updateBoard(entity);
                        PlayerScoreboard.updateBoard(damager);
                    } else {
                        damager.sendMessage(zombieList.get(entity.getDisplayName()).getNameTagColor() + zombieList.get(entity.getDisplayName()).getSpecialStatus() +  " " + entity.getDisplayName() + ChatColor.RED + " is already stunned!");
                    }
                } else {
                    damager.sendMessage(ChatColor.RED + "You are currently stunned and cannot stun!");
                    damager.sendMessage(ChatColor.GOLD + "Stun time remaining: " + ChatColor.RED + (Stats.getStunCooldown(damager.getDisplayName()) - System.currentTimeMillis()/1000) + " seconds");
                }
            }
        }
    }

    @EventHandler
    public void onStunSnowball(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Snowball){
            Player zombie = (Player) e.getEntity();
            Snowball snowball = (Snowball) e.getDamager();
            if(snowball.getShooter() instanceof Player && snowball.getShooter() != zombie) {
                Player damager = (Player) snowball.getShooter();
                if(humanList.get(damager.getDisplayName()).isAlive() && (zombieList.containsKey(zombie.getDisplayName())
                        && (!humanList.containsKey(zombie.getDisplayName()) || !humanList.get(zombie.getDisplayName()).isAlive()))){
                    // All special zombies can be stunned by a snowball/sock
                    if(!Stats.getCooldowns().containsKey(damager.getDisplayName()) || Stats.getStunCooldown(zombie.getDisplayName()) <= System.currentTimeMillis()/1000){
                        e.setCancelled(true);
                        snowball.remove();
                        stunZombie(zombie, damager); // only humans will be in this action
                        Stats.addStun(damager.getDisplayName(), zombie.getDisplayName());
                        PlayerScoreboard.updateBoard(zombie);
                        PlayerScoreboard.updateBoard(damager);
                    }
                    else {
                        damager.sendMessage(ChatColor.RED + "You cannot stun this " + zombieList.get(zombie.getDisplayName()).getSpecialStatus() + " yet!");
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
            if(arrow.getShooter() instanceof Player && arrow.getShooter() != zombie){
                Player damager = (Player) arrow.getShooter();
                if((humanList.containsKey(damager.getDisplayName()) && humanList.containsKey(zombie.getDisplayName()))
                        && humanList.get(damager.getDisplayName()).isAlive() && humanList.get(zombie.getDisplayName()).isAlive()){
                    // Human hitting human during a game
                    arrow.remove();
                    e.setCancelled(true);
                    return;
                }
                if(humanList.get(damager.getDisplayName()).isAlive() && (zombieList.containsKey(zombie.getDisplayName())
                        && (!humanList.containsKey(zombie.getDisplayName()) || !humanList.get(zombie.getDisplayName()).isAlive()))){
                    e.setCancelled(true);
                    arrow.remove();
                    if(!Stats.getCooldowns().containsKey(zombie.getDisplayName()) || Stats.getStunCooldown(zombie.getDisplayName()) <= System.currentTimeMillis()/1000){
                        String specialStatus = null;
                        if(zombieList.containsKey(zombie.getDisplayName())){
                            specialStatus = ChatColor.stripColor(zombieList.get(zombie.getDisplayName()).getSpecialStatus());
                        }
                        switch(specialStatus){
                            case "Tank":
                            case "Twitch":
                                damager.sendMessage(ChatColor.RED + "You cannot stun this " + specialStatus + " with a blaster!");
                                break;
                            default:
                                stunZombie(zombie, damager);
                                Stats.addStun(damager.getDisplayName(), zombie.getDisplayName());
                                PlayerScoreboard.updateBoard(zombie);
                                PlayerScoreboard.updateBoard(damager);
                        }
                    }
                    else {
                        damager.sendMessage(ChatColor.RED + "You cannot stun this " + zombieList.get(zombie.getDisplayName()).getSpecialStatus() + " yet!");
                    }
                }
            }
        }
    }
    public static void stunZombie(Player zombie, Player damager){
        ChatColor color;
        String message;
        if(Stats.getHumans().containsKey(damager.getDisplayName()) && Stats.getHumans().get(damager.getDisplayName()).isAlive()){ // human stunned zombie
            color = humanList.get(damager.getDisplayName()).getNameTagColor();
            message = "";
        }
        else{ // witch/twitch stunned zombie
            color = zombieList.get(damager.getDisplayName()).getNameTagColor();
            message = zombieList.get(damager.getDisplayName()).getSpecialStatus() + " ";
        }
        if(Stats.getCooldowns().containsKey(zombie.getDisplayName())){ // If player has been stunned before
            Long stunCooldown = Stats.getStunCooldown(zombie.getDisplayName());
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
