package com.egsrit.hvz.util;

import com.egsrit.hvz.HvZPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class PlayerScoreboard {

    public static void updateBoard(Player player){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("HvzScoreboard","dummy", ChatColor.AQUA + "HvZ Stats");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score blankLine = obj.getScore(ChatColor.AQUA + "=-=-=-=-=-=-=-=-=");
        blankLine.setScore(4);
        Score specialType = obj.getScore(ChatColor.GOLD + "Not playing"); // Default message
        Boolean zombie = null; // Only null if a player is not a human or a zombie
        if(Stats.getHumans().containsKey(player.getDisplayName())){
            // Tells the player what type of player they are
            zombie = false;
            specialType = obj.getScore(ChatColor.GOLD + "You are a: " + ChatColor.GREEN + "Human");
        } else if (Stats.getZombies().containsKey(player.getDisplayName())){
            zombie = true;
            specialType = obj.getScore(ChatColor.GOLD + "You are a: " + Stats.getZombies().get(player.getDisplayName()).getNameTagColor() + Stats.getZombies().get(player.getDisplayName()).getSpecialStatus());
        }
        specialType.setScore(3);
        Score stunOrTags; // Used to show the stun/tag counter based on player type
        if(zombie != null && !zombie){
            stunOrTags = obj.getScore(ChatColor.GOLD + "Stuns: " + Stats.getStunned(player.getDisplayName()).size());
            stunOrTags.setScore(2);
        } else if (zombie == null){
            // Do nothing here
        } else {
            stunOrTags = obj.getScore(ChatColor.GOLD + "Tags: " + Stats.getTagged(player.getDisplayName()).size());
            stunOrTags.setScore(2);
        }
        if(zombie != null && zombie && Stats.getZombies().containsKey(player.getDisplayName())){
            String specialStatus = ChatColor.stripColor(Stats.getZombies().get(player.getDisplayName()).getSpecialStatus());
            if(specialStatus.equals("Witch") || specialStatus.equals("Twitch")){
                // Use both a Tags and Stuns counter if the player is a witch/twitch, as they can do both
                Score stuns = obj.getScore(ChatColor.GOLD + "Stuns: " + Stats.getStunned(player.getDisplayName()).size());
                stuns.setScore(1);
            }
        }
        if(zombie != null && zombie && Stats.getCooldowns().containsKey(player.getDisplayName()) && Stats.getStunCooldown(player.getDisplayName()) > System.currentTimeMillis()/1000){
            // Display the stun timer if a zombie is stunned
            getStunTimer(player, obj, board);
        }
        player.setScoreboard(board);
    }
    public static void getStunTimer(Player player, Objective obj, Scoreboard board){
        // Tick down the player's stun time remaining
        new BukkitRunnable(){

            @Override
            public void run() {
                    if(Stats.getStunCooldown(player.getDisplayName()) <= System.currentTimeMillis()/1000){
                        for(int i = 0; i < Stats.getZombies().get(player.getDisplayName()).getStunTime() + 1; i++){
                            // Timer can get stuck sometimes (rarely), this will clear all of them including 0 to end the task
                            board.resetScores(ChatColor.GOLD + "Stun Time: " + ChatColor.RED + i);
                        }
                        this.cancel();
                    } else {
                        Score stunTimer = obj.getScore(ChatColor.GOLD + "Stun Time: " + ChatColor.RED + (Stats.getStunCooldown(player.getDisplayName()) - System.currentTimeMillis()/1000));
                        board.resetScores(ChatColor.GOLD + "Stun Time: " + ChatColor.RED + (Stats.getStunCooldown(player.getDisplayName()) - System.currentTimeMillis()/1000 + 1));
                        stunTimer.setScore(0);
                    }
                }

            }.runTaskTimer(HvZPlugin.getInstance(), 0, 20);
        }
}

