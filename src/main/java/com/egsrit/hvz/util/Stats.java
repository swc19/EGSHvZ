package com.egsrit.hvz.util;

import com.egsrit.hvz.players.Human;
import com.egsrit.hvz.players.HvzZombie;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class Stats {
    private static final HashMap<String, Human> humans = new HashMap<>();
    private static final HashMap<String, HvzZombie> zombies = new HashMap<>();
    private static final HashMap<String, ArrayList<String>> stunMap = new HashMap<>();
    private static final HashMap<String, ArrayList<String>> tagMap = new HashMap<>();

    public static void addHuman(Player p){
        stunMap.putIfAbsent(p.getDisplayName(), new ArrayList<>());
        humans.put(p.getDisplayName(), new Human(p, true, false));
    }

    public static void addZombie(Player p, int stunTime, String specialStatus){
        tagMap.putIfAbsent(p.getDisplayName(), new ArrayList<>());
        zombies.put(p.getDisplayName(), new HvzZombie(p, stunTime, specialStatus, true, true));
    }

    public static void addTag(Player h, Player z){
        ArrayList<String> tagList = tagMap.get(z.getDisplayName());
        tagList.add(h.getDisplayName());
    }

    public static void addStun(Player h, Player z){
        ArrayList<String> stunList = stunMap.get(h.getDisplayName());
        stunList.add(z.getDisplayName());
    }
    public static HashMap<String, Human> getHumans(){
        return humans;
    }
    public static HashMap<String, HvzZombie> getZombies(){
        return zombies;
    }
    public static HashMap<String, ArrayList<String>> getStunMap(){
        return stunMap;
    }
    public static HashMap<String, ArrayList<String>> getTagMap(){
        return tagMap;
    }
    public static ArrayList<String> getStunned(Player h){
        return stunMap.get(h.getDisplayName());
    }

    public static ArrayList<String> getTagged(Player z){
        return tagMap.get(z.getDisplayName());
    }
}
