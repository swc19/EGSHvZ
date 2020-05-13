package com.egsrit.hvz;

import com.egsrit.hvz.commands.HvZCommandExecutor;
import com.egsrit.hvz.listeners.ArmorListener;
import com.egsrit.hvz.listeners.StunListener;
import com.egsrit.hvz.listeners.TagListener;
import com.egsrit.hvz.listeners.WeaponListener;
import com.egsrit.hvz.util.PlayerScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class HvZPlugin extends JavaPlugin {
    private static HvZPlugin INSTANCE;
    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        System.out.println("HvZ Plugin installed");
        getServer().getPluginManager().registerEvents(new StunListener(), this);
        getServer().getPluginManager().registerEvents(new TagListener(), this);
        getServer().getPluginManager().registerEvents(new ArmorListener(), this);
        getServer().getPluginManager().registerEvents(new WeaponListener(), this);
        getCommand("hvz").setExecutor(new HvZCommandExecutor());
        for(Player p : Bukkit.getOnlinePlayers()){
            // This will be changed to only update everyone once a new game begins/players select human/zombie
            PlayerScoreboard.updateBoard(p);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static HvZPlugin getInstance(){
        return INSTANCE;
    }
}
