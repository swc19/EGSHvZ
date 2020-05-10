package com.egsrit.hvz.commands;

import com.egsrit.hvz.players.Human;
import com.egsrit.hvz.util.Stats;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HvZCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command,String label, String[] args){
        if(command.getName().equalsIgnoreCase("hvz")){
            switch(args[0]){
                case "human":
                    // Debug command to make a player a human
                    if(args.length > 1){
                        return makeHuman((Player) sender, args[1]);
                    } else {
                        return makeHuman((Player) sender, ((Player) sender).getDisplayName());
                    }
                case "zombie":
                    // Debug command to make a player a (optionally special) zombie
                    if(args.length > 2){ // ex: /hvz zombie Tank swc19
                        return makeZombie((Player) sender, StringUtils.capitalize(args[1]), args[2]);
                    } else if (args.length == 2){ // ex /hvz zombie Tank
                        return makeZombie((Player) sender, StringUtils.capitalize(args[1]), ((Player) sender).getDisplayName());
                    } else { // ex: /hvz zombie
                    return makeZombie((Player) sender, "Zombie", ((Player) sender).getDisplayName());
                    }
                case "list":
                    // show the sender a list of humans, zombies, or all players playing
                    if(args.length > 1){
                        switch(args[1]){
                            case "human":
                            case "humans":
                                return showList(sender,"human");
                            case "zombie":
                            case "zombies":
                                return showList(sender,"zombie");
                            case "all":
                                return showList(sender, "all");
                            default:
                                sender.sendMessage(ChatColor.RED + "Usage: /hvz list [human|zombie|all]");
                                return true;
                        }
                    }
                    return showList(sender,"all");
                default:
                    sender.sendMessage(ChatColor.RED + "HVZ command not found.");
                    return true;
            }
        }
        return false;
    }

    private boolean makeHuman(Player player, String username){
        if(!username.equals(player.getDisplayName())){
            try{
                player = Bukkit.matchPlayer(username).get(0);
            } catch(Exception e){
                player.sendMessage(ChatColor.RED + "Player not found: " + username);
                return true;
            }
        }
        Stats.addHuman(username, player);
        //System.out.println(Stats.getStunMap());
        //System.out.println(Stats.getHumans());
        return true;
    }

    private boolean makeZombie(Player player, String specialStatus, String username){
        if(!username.equals(player.getDisplayName())){
            try{
                player = Bukkit.matchPlayer(username).get(0);
            } catch(Exception e){
                player.sendMessage(ChatColor.RED + "Player not found: " + username);
                return true;
            }
        }
        if(Stats.getHumans().get(player.getDisplayName()) != null){
            if(Stats.getHumans().get(player.getDisplayName()).isAlive()){
                // Kill the human if you make them a zombie to prevent any mixup of types
                Stats.getHumans().put(player.getDisplayName(), new Human(player.getDisplayName(), false, false));
            }
        }
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

        Stats.addZombie(username, stunTimer, specialStatus, player);
        //System.out.println(Stats.getTagMap());
        //System.out.println(Stats.getZombies().get(player.getDisplayName()).getSpecialStatus());
        //System.out.println(Stats.getZombies().get(player.getDisplayName()).getStunTime());
        return true;
    }

    public boolean showList(CommandSender s, String type){
        List<String> players = new ArrayList<>();
        if(type.equals("human") || type.equals("all")){
            for(String pname : Stats.getHumans().keySet()){
                if(Stats.getHumans().get(pname).isAlive()){
                    players.add(ChatColor.GREEN + pname);
                }
            }
        }
        if (type.equals("zombie") || type.equals("all")){
            for(String pname : Stats.getZombies().keySet()){
                if(Stats.getHumans().get(pname) == null || !Stats.getHumans().get(pname).isAlive()){
                    // Antivirus can have a person in both lists while being alive
                    String specialStatus = Stats.getZombies().get(pname).getSpecialStatus();
                    if(!specialStatus.equals("Zombie")){
                        players.add(Stats.getZombies().get(pname).getNameTagColor() + "" + ChatColor.BOLD + specialStatus + " " + pname);

                    } else {
                        players.add(Stats.getZombies().get(pname).getNameTagColor() + pname);
                    }
                }
            }
        }
        if(players.isEmpty()){
            s.sendMessage(ChatColor.RED + "Nobody found.");
            return true;
        }
        String message = String.join(", ", players);
        s.sendMessage(ChatColor.GOLD + StringUtils.capitalize(type) + ": " + message);
        return true;

    }
}
