package com.egsrit.hvz.commands;

import com.egsrit.hvz.items.SpecialItems;
import com.egsrit.hvz.util.Stats;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
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
                        return makeHuman(sender, args[1]);
                    } else {
                        sender.sendMessage(ChatColor.RED + "Usage: /hvz human username");
                        return true;
                    }
                case "zombie":
                    // Debug command to make a player a (optionally special) zombie
                    // "Zombie" is a valid special type for a regular zombie
                    if(args.length > 2){ // ex: /hvz zombie Tank swc19
                        return makeZombie(sender, StringUtils.capitalize(args[1]), args[2]);
                    } else {
                        sender.sendMessage(ChatColor.RED + "Usage: /hvz zombie type username");
                        return true;
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
                                sender.sendMessage(ChatColor.RED + "Usage: /hvz list human | zombie | all");
                                return true;
                        }
                    }
                    return showList(sender,"all");
                case "shirt": // Spawns a shirt to make a player a special zombie
                    if(args.length > 2){
                        return giveShirt(sender, args[2], args[1]);
                    } else {
                        sender.sendMessage(ChatColor.RED + "Usage: /hvz shirt type username");
                        return true;
                    }
                case "give":
                    if(args.length > 2){
                        return giveItem(sender, args[2], args[1]);
                    } else {
                        sender.sendMessage(ChatColor.RED + "Usage: /hvz give itemType username");
                        return true;
                    }
                default:
                    sender.sendMessage(ChatColor.RED + "HVZ command not found: " + args[0]);
                    return true;
            }
        }
        return false;
    }

    private boolean makeHuman(CommandSender sender, String username){
        // Makes a player a human, no matter what they already are
        Player player;
        try{
            // finding the player by username gives a player object to update scoreboard and send messages
            player = Bukkit.matchPlayer(username).get(0);
        } catch(Exception e){
            sender.sendMessage(ChatColor.RED + "Player not found: " + username);
            return true;
        }

        Stats.addHuman(username, player);
        return true;
    }

    private boolean makeZombie(CommandSender sender, String specialStatus, String username){
        Player player;
        try{
            player = Bukkit.matchPlayer(username).get(0);
        } catch(Exception e){
            sender.sendMessage(ChatColor.RED + "Player not found: " + username);
            return true;
        }
        int stunTimer;
        switch(StringUtils.capitalize(specialStatus)){
            // Set stun time based on special type
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
                sender.sendMessage(ChatColor.RED + "Invalid status " + specialStatus + ", defaulting to Zombie");
                specialStatus = "Zombie";
                break;
        }

        Stats.addZombie(username, stunTimer, specialStatus, player);
        return true;
    }

    public boolean showList(CommandSender s, String type){
        // Shows the sender a list of humans, zombies or all players colorcoded by type
        List<String> players = new ArrayList<>();
        if(type.equals("human") || type.equals("all")){
            for(String pname : Stats.getHumans().keySet()){
                players.add(ChatColor.GREEN + pname);
            }
        }
        if (type.equals("zombie") || type.equals("all")){
            for(String pname : Stats.getZombies().keySet()){
                if(Stats.getHumans().get(pname) == null){
                    String specialStatus = Stats.getZombies().get(pname).getSpecialStatus();
                    if(!specialStatus.equals("Zombie")){
                        // Add a bold to have the special zombies stand out in the list
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

    private boolean giveShirt(CommandSender sender, String username, String type){
        // Gives a shirt (dyed leather chestplate) that can make a player a given special zombie type
        Player player;
        Color color;
        ChatColor chatColor;
        type = StringUtils.capitalize(type);
        try{
            player = Bukkit.matchPlayer(username).get(0);
        } catch(Exception e){
            sender.sendMessage(ChatColor.RED + "Player not found: " + username);
            return true;
        }
        switch(type){
            // Set the colors for the shirt to be built
            case("Boomer"):
                color = Color.YELLOW;
                chatColor = ChatColor.YELLOW;
                break;
            case("Jackal"):
                color = Color.WHITE;
                chatColor = ChatColor.WHITE;
                break;
            case("Tank"):
                color = Color.FUCHSIA;
                chatColor = ChatColor.LIGHT_PURPLE;
                break;
            case("Twitch"):
                color = Color.PURPLE;
                chatColor = ChatColor.DARK_PURPLE;
                break;
            case("Witch"):
                color = Color.BLUE;
                chatColor = ChatColor.BLUE;
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Type " + type + " not found.");
                return true;
        }
        player.getInventory().addItem(SpecialItems.makeSpecialShirt(chatColor, color, type));
        return true;
    }

    private boolean giveItem(CommandSender sender, String username, String itemType){
        Player p;
        try{
            p = Bukkit.matchPlayer(username).get(0);
        } catch (Exception e){
            sender.sendMessage(ChatColor.RED + "Player not found: " + username);
            return true;
        }
        switch(StringUtils.capitalize(itemType)){
            case "Antivirus":
                p.getInventory().addItem(SpecialItems.makeAntivirus());
                return true;
            case "Biocade":
                p.getInventory().addItem(SpecialItems.makeBioCade());
                return true;
            case "Bodyarmor":
                p.getInventory().addItem(SpecialItems.makeBodyArmor());
                return true;
            case "Deployablecover":
            case "Deploy":
                p.getInventory().addItem(SpecialItems.makeDeployableCover());
                return true;
            case "Deplorablecover":
            case "Deplore":
                p.getInventory().addItem(SpecialItems.makeDeplorableCover());
                return true;
            case "Elephantblaster":
            case "Elephant":
                p.getInventory().addItem(SpecialItems.makeElephantBlaster());
                return true;
            case "Blaster":
                p.getInventory().addItem(SpecialItems.makeBlaster());
                return true;
            case "Sock":
            case "Socks":
                p.getInventory().addItem(SpecialItems.makeSock(10));
                return true;
            default:
                sender.sendMessage(ChatColor.RED + "Couldn't find special item: " + itemType);
                return true;
        }
    }
}













