package com.egsrit.hvz.commands;

import com.egsrit.hvz.util.Stats;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HvZCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command,String label, String[] args){
        if(command.getName().equals("hvz")){
            switch(args[0]){
                case "human":
                    return makeHuman((Player) sender);
                case "zombie":
                    if(args.length > 1){
                        return makeZombie((Player) sender, StringUtils.capitalize(args[1]));
                    } else{
                        return makeZombie((Player) sender, "Zombie");
                    }
            }
        }
        return false;
    }

    private boolean makeHuman(Player player){
        Stats.addHuman(player);
        //System.out.println(Stats.getStunMap());
        //System.out.println(Stats.getHumans());
        return true;
    }

    private boolean makeZombie(Player player, String specialStatus){
        int stunTimer;
        switch(specialStatus){
            case "Witch":
            case "Twitch":
                stunTimer = 10;
                break;
            case "Jackal":
            case "Tank":
                stunTimer = 150;
                break;
            case "Boomer":
            case "Zombie":
                stunTimer = 300;
                break;
            default:
                stunTimer = 300;
                player.sendMessage(ChatColor.RED + "Invalid status " + specialStatus + ", defaulting to Zombie");
                specialStatus = "Zombie";
                break;
        }
        Stats.addZombie(player, stunTimer, specialStatus);
        //System.out.println(Stats.getTagMap());
        //System.out.println(Stats.getZombies().get(player.getDisplayName()).getSpecialStatus());
        //System.out.println(Stats.getZombies().get(player.getDisplayName()).getStunTime());
        return true;
    }
}
