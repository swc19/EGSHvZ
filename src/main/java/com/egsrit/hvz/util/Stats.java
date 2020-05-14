package com.egsrit.hvz.util;

import com.egsrit.hvz.players.Human;
import com.egsrit.hvz.players.HvzZombie;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Stats {
    private static final Map<String, Human> humans = new HashMap<>();
    private static final Map<String, HvzZombie> zombies = new HashMap<>();
    private static final Map<String, ArrayList<String>> stunMap = new HashMap<>();
    private static final Map<String, ArrayList<String>> tagMap = new HashMap<>();
    private static final Map<String, Long> stuns = new HashMap<>();

    public static void addHuman(String pname, Player p){
        // Designates a player as a human
        // An entry in the humans list means that the player is an alive human
        stunMap.putIfAbsent(pname, new ArrayList<>());
        humans.put(pname, new Human(pname, false));
        zombies.remove(pname); // Remove any instance of the player from the zombie list
        if(getCooldowns().containsKey(pname) && getCooldowns().get(pname) > System.currentTimeMillis()/1000){
            // If player used an antivirus, remove any stun timer from when they were a zombie
            setStunCooldown(pname, System.currentTimeMillis()/1000 - 1);
        }
        Human.giveItems(p);
        p.sendMessage(ChatColor.GREEN + "You are now a Human!");
        PlayerScoreboard.updateBoard(p);
    }

    public static void addZombie(String pname, int stunTime, String specialStatus, Player p){
        // Designates a player as a (optionally special) zombie
        tagMap.putIfAbsent(pname, new ArrayList<>());
        stunMap.putIfAbsent(pname, new ArrayList<>()); // Used for Witch/Twitch stuns, putIfAbsent if original zombie
        HvzZombie newZombie = new HvzZombie(pname, stunTime, specialStatus);
        zombies.put(pname, newZombie);
        getHumans().remove(pname); // Remove the player from the human list, does not affect stats
        p.sendMessage(ChatColor.AQUA + "You are now a " + newZombie.getNameTagColor() + newZombie.getSpecialStatus() + "!");
        p.sendMessage(ChatColor.AQUA + "Stun time: " + newZombie.getStunTime());
        PlayerScoreboard.updateBoard(p);
    }

    public static void addTag(String h, String z){
        // Add a tag event (zombie tags human) to the database
        tagMap.putIfAbsent(z, new ArrayList<>());
        ArrayList<String> tagList = tagMap.get(z);
        tagList.add(h);
    }

    public static void addStun(String h, String z){
        // Add a stun event (human, witch/twitch stuns zombie) to the database
        stunMap.putIfAbsent(h, new ArrayList<>());
        ArrayList<String> stunList = stunMap.get(h);
        stunList.add(z);
    }
    public static Map<String, Human> getHumans(){
        return humans;
    }
    public static Map<String, HvzZombie> getZombies(){
        return zombies;
    }
    public static Map<String, ArrayList<String>> getStunMap(){
        return stunMap;
    }
    public static Map<String, ArrayList<String>> getTagMap(){
        return tagMap;
    }
    public static ArrayList<String> getStunned(String h){
        return stunMap.get(h);
    }
    public static ArrayList<String> getTagged(String z){
        return tagMap.get(z);
    }
    public static Map<String, Long> getCooldowns(){
        return stuns;
    }
    public static Long getStunCooldown(String p) {
        return stuns.get(p);
    }
    public static void setStunCooldown(String p, Long time) {
        stuns.put(p, time);
    }
}
