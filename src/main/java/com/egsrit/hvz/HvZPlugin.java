package com.egsrit.hvz;

import org.bukkit.plugin.java.JavaPlugin;

public final class HvZPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("HvZ Plugin installed");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
