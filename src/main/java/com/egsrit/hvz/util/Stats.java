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
        stunMap.putIfAbsent(pname, new ArrayList<>());
        humans.put(pname, new Human(pname, true, false));
        if(getCooldowns().containsKey(pname) && getCooldowns().get(pname) > System.currentTimeMillis()/1000){
            setStunCooldown(pname, System.currentTimeMillis()/1000 - 1);
        }
        p.sendMessage(ChatColor.GREEN + "You are now a Human!");
        PlayerScoreboard.updateBoard(p);
    }

    public static void addZombie(String pname, int stunTime, String specialStatus, Player p){
        if(getHumans().containsKey(pname)){
            getHumans().put(pname, new Human(pname, false, false));
        }
        tagMap.putIfAbsent(pname, new ArrayList<>());
        HvzZombie newZombie = new HvzZombie(pname, stunTime, specialStatus);
        zombies.put(pname, newZombie);
        p.sendMessage("You are now a " + newZombie.getNameTagColor() + newZombie.getSpecialStatus() + "!");
        p.sendMessage("Stun time: " + newZombie.getStunTime());
        PlayerScoreboard.updateBoard(p);
    }

    public static void addTag(String h, String z){
        tagMap.putIfAbsent(z, new ArrayList<>());
        ArrayList<String> tagList = tagMap.get(z);
        tagList.add(h);
    }

    public static void addStun(String h, String z){
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
