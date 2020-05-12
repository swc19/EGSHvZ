package com.egsrit.hvz.util;

import com.egsrit.hvz.HvZPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;

public class PlayerScoreboard {
    private static final List<Player> playersWithBoards = new ArrayList<>();
    public static void updateBoard(Player player){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective obj = board.registerNewObjective("HvzScoreboard","dummy", ChatColor.AQUA + "HvZ Stats");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score blankLine = obj.getScore(ChatColor.AQUA + "=-=-=-=-=-=-=-=-=");
        blankLine.setScore(3);
        Score specialType = obj.getScore(ChatColor.GOLD + "Not playing");
        Boolean zombie = null;
        if(Stats.getHumans().containsKey(player.getDisplayName()) && Stats.getHumans().get(player.getDisplayName()).isAlive()){
            zombie = false;
            specialType = obj.getScore(ChatColor.GOLD + "You are a: " + ChatColor.GREEN + "Human");
        } else if (Stats.getZombies().containsKey(player.getDisplayName())){
            zombie = true;
            specialType = obj.getScore(ChatColor.GOLD + "You are a: " + Stats.getZombies().get(player.getDisplayName()).getNameTagColor() + Stats.getZombies().get(player.getDisplayName()).getSpecialStatus());
        }
        specialType.setScore(2);
        Score stunOrTags;
        if(zombie != null && !zombie){
            stunOrTags = obj.getScore(ChatColor.GOLD + "Stuns: " + Stats.getStunned(player.getDisplayName()).size());
            stunOrTags.setScore(1);
        } else if (zombie == null){
            // Do nothing here
        } else {
            stunOrTags = obj.getScore(ChatColor.GOLD + "Tags: " + Stats.getTagged(player.getDisplayName()).size());
            stunOrTags.setScore(1);
        }
        if(zombie != null && zombie && Stats.getCooldowns().containsKey(player.getDisplayName()) && Stats.getStunCooldown(player.getDisplayName()) > System.currentTimeMillis()/1000){
            getStunTimer(player, obj, board);
        }
        playersWithBoards.add(player);
        player.setScoreboard(board);
    }
    public static void getStunTimer(Player player, Objective obj, Scoreboard board){
        new BukkitRunnable(){

            @Override
            public void run() {
                    if(Stats.getStunCooldown(player.getDisplayName()) < System.currentTimeMillis()/1000){
                        board.resetScores(ChatColor.GOLD + "Stun Time: " + ChatColor.RED + "0");
                        this.cancel();
                    } else {
                        Score stunTimer = obj.getScore(ChatColor.GOLD + "Stun Time: " + ChatColor.RED + (Stats.getStunCooldown(player.getDisplayName()) - System.currentTimeMillis()/1000));
                        board.resetScores(ChatColor.GOLD + "Stun Time: " + ChatColor.RED + (Stats.getStunCooldown(player.getDisplayName()) - System.currentTimeMillis()/1000 + 1));
                        stunTimer.setScore(0);
                    }
                }

            }.runTaskTimer(HvZPlugin.getInstance(), 0, 20);
        }
    public static List<Player> getPlayersWithBoards(){
        return playersWithBoards;
    }
}

