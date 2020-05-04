package com.egsrit.hvz;

import com.egsrit.hvz.commands.HvZCommandExecutor;
import com.egsrit.hvz.listeners.StunListener;
import com.egsrit.hvz.listeners.TagListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class HvZPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("HvZ Plugin installed");
        getServer().getPluginManager().registerEvents(new StunListener(), this);
        getServer().getPluginManager().registerEvents(new TagListener(), this);
        getCommand("hvz").setExecutor(new HvZCommandExecutor());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
