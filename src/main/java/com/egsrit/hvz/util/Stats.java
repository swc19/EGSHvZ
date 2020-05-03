package com.egsrit.hvz.util;

import com.egsrit.hvz.players.Human;
import com.egsrit.hvz.players.Zombie;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class Stats {
    private static HashMap<Player, ArrayList<Player>> humans = new HashMap<>();
    private static HashMap<Player, ArrayList<Player>> zombies = new HashMap<>();

    public static void addHuman(Human h){
        humans.put(h.getHuman(), new ArrayList<>());
    }

    public static void addZombie(Zombie z){
        zombies.put(z.getZombie(), new ArrayList<>());
    }

    public static void addTag(Human h, Zombie z){
        ArrayList<Player> tagList = zombies.get(z.getZombie());
        tagList.add(h.getHuman());
    }

    public static void addStun(Human h, Zombie z){
        ArrayList<Player> stunList = humans.get(h.getHuman());
        stunList.add(z.getZombie());
    }

    public ArrayList<Player> getStunned(Human h){
        return humans.get(h.getHuman());
    }

    public ArrayList<Player> getTagged(Zombie z){
        return zombies.get(z.getZombie());
    }
}
