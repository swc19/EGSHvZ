package com.egsrit.hvz.listeners;

import com.egsrit.hvz.players.Human;
import com.egsrit.hvz.players.HvzZombie;
import com.egsrit.hvz.util.PlayerScoreboard;
import com.egsrit.hvz.util.Stats;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.Map;

public class StunListener implements Listener {
    private static final Map<String, Human> humanList = Stats.getHumans();
    private static final Map<String, HvzZombie> zombieList = Stats.getZombies();

    @EventHandler
    public void onStunTag(EntityDamageByEntityEvent e){
        /* This method checks if a Witch or Twitch has stunned a zombie.
        * Only these two special types can stun a zombie through tagging. */
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player && e.getEntity() != e.getDamager()){
            Player zombie = (Player) e.getEntity(); // the Zombie
            Player damager = (Player) e.getDamager(); // the Witch/Twitch
            if(zombieList.containsKey(zombie.getDisplayName()) && zombieList.containsKey(damager.getDisplayName())){
                // Ensure that both the entity and damager are zombie types, check for actual damager type later
                // If a zombie is present in the zombieList, they cannot be an alive human.
                e.setCancelled(true);
                String damagerSpecialStatus = ChatColor.stripColor(zombieList.get(damager.getDisplayName()).getSpecialStatus());
                if(damagerSpecialStatus.equals("Witch") || damagerSpecialStatus.equals("Twitch")){
                    // Check whether the damager is a Witch or Twitch
                    if(!Stats.getCooldowns().containsKey(damager.getDisplayName()) || Stats.getStunCooldown(damager.getDisplayName()) <= System.currentTimeMillis()/1000) {
                        // Check if the damager is stunned
                        if (!Stats.getCooldowns().containsKey(zombie.getDisplayName()) || Stats.getStunCooldown(zombie.getDisplayName()) <= System.currentTimeMillis() / 1000) {
                            // Check if the zombie is stunned (cannot restun)
                            stunZombie(zombie, damager);
                            Stats.addStun(damager.getDisplayName(), zombie.getDisplayName());
                            // Update each scoreboard to reflect the stats/stun time
                            PlayerScoreboard.updateBoard(zombie);
                            PlayerScoreboard.updateBoard(damager);
                        } else {
                            damager.sendMessage(zombieList.get(zombie.getDisplayName()).getNameTagColor() + zombieList.get(zombie.getDisplayName()).getSpecialStatus() +  " " + zombie.getDisplayName() + ChatColor.RED + " is already stunned!");
                        }
                    } else {
                        damager.sendMessage(ChatColor.RED + "You are currently stunned and cannot stun!");
                        damager.sendMessage(ChatColor.GOLD + "Stun time remaining: " + ChatColor.RED + (Stats.getStunCooldown(damager.getDisplayName()) - System.currentTimeMillis()/1000) + " seconds");
                    }
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
                if(humanList.containsKey(damager.getDisplayName()) && zombieList.containsKey(zombie.getDisplayName())){
                    // Check if the shooter is a human and the entity is a zombie
                    // All special zombies can be stunned by a snowball/sock
                    e.setCancelled(true);
                    snowball.remove();
                    if(!Stats.getCooldowns().containsKey(damager.getDisplayName()) || Stats.getStunCooldown(zombie.getDisplayName()) <= System.currentTimeMillis()/1000){
                        // If the zombie isn't already stunned, stun the zombie
                        stunZombie(zombie, damager);
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
            List<MetadataValue> metaData = arrow.getMetadata("Weapon Name");
            String weaponName = ChatColor.stripColor(metaData.get(0).asString());
            System.out.println(weaponName);
            if(arrow.getShooter() instanceof Player && arrow.getShooter() != zombie){
                Player damager = (Player) arrow.getShooter();
                if(humanList.containsKey(damager.getDisplayName()) && humanList.containsKey(zombie.getDisplayName())){
                    // Human hitting human during a game, cancel damage and move on
                    e.setCancelled(true);
                    arrow.remove();
                    return;
                }
                if(humanList.containsKey(damager.getDisplayName()) && zombieList.containsKey(zombie.getDisplayName())){
                    // Check if the human shot at a zombie
                    e.setCancelled(true);
                    arrow.remove();
                    if(!Stats.getCooldowns().containsKey(zombie.getDisplayName()) || Stats.getStunCooldown(zombie.getDisplayName()) <= System.currentTimeMillis()/1000){
                        String specialStatus = ChatColor.stripColor(zombieList.get(zombie.getDisplayName()).getSpecialStatus());
                        switch(specialStatus){
                            case "Tank":
                            case "Twitch":
                                if(weaponName.equals("Blaster")){
                                    // Any special type can be affected by the elephant blaster, seen below
                                    damager.sendMessage(ChatColor.RED + "You cannot stun this " + specialStatus + " with a normal blaster!");
                                    break;
                                }
                            default:
                                // Everything except the above two types can be stunned with a normal blaster, so default to registering the stun
                                if(weaponName.equals("Elephant Blaster") && !specialStatus.equals("Zombie")){
                                    // If a special is hit by elephant blaster, remove their shirt and make them a regular zombie again
                                    zombie.sendMessage(ChatColor.RED + "You've been hit by an Elephant Blaster and are no longer a " + specialStatus + "!");
                                    zombie.getInventory().setItem(38, new ItemStack(Material.AIR));
                                    zombie.playSound(zombie.getLocation(), Sound.ENTITY_ITEM_BREAK, 0.8f, 1.0f);
                                    Stats.addZombie(zombie.getDisplayName(), 300, "Zombie", zombie);
                                }
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
        if(Stats.getHumans().containsKey(damager.getDisplayName())){ // human stunned zombie
            color = humanList.get(damager.getDisplayName()).getNameTagColor();
            message = "";
        } else { // witch/twitch stunned zombie
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
        damager.playSound(damager.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.8f, 0.5f);
        zombie.playSound(zombie.getLocation(), Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 0.8f, 0.7f);

        damager.sendMessage(ChatColor.GREEN + "You've stunned " + zombie.getDisplayName() + "!");
        Stats.setStunCooldown(zombie.getDisplayName(), System.currentTimeMillis()/1000 + zombieList.get(zombie.getDisplayName()).getStunTime());
        zombie.sendMessage(ChatColor.RED + "You've been stunned by " + color + message + damager.getDisplayName() + ChatColor.RED + "!");
    }
}
