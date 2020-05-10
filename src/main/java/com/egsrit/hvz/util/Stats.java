package com.egsrit.hvz.util;

import com.egsrit.hvz.players.Human;
import com.egsrit.hvz.players.HvzZombie;
import org.bukkit.Bukkit;
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
        stunMap.putIfAbsent(pname, new ArrayList<>());
        humans.put(pname, new Human(pname, true, false));
        p.sendMessage(ChatColor.GREEN + "You are now a Human!");
    }

    public static void addZombie(String pname, int stunTime, String specialStatus, Player p){
        Player player = Bukkit.matchPlayer(pname).get(0);
        tagMap.putIfAbsent(pname, new ArrayList<>());
        HvzZombie newZombie = new HvzZombie(pname, stunTime, specialStatus);
        zombies.put(pname, newZombie );
        player.sendMessage("You are now a " + newZombie.getNameTagColor() + newZombie.getSpecialStatus() + "!");
        player.sendMessage("Stun time: " + newZombie.getStunTime());
    }

    public static void addTag(String h, String z){
        ArrayList<String> tagList = tagMap.get(z);
        tagList.add(h);
    }

    public static void addStun(String h, String z){
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
    public static Long getStunCooldown(String p) {
        return stuns.get(p);
    }
    public static void setStunCooldown(String p, Long time) {
        stuns.put(p, time);
    }
}
