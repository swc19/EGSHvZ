package com.egsrit.hvz.commands;

import com.egsrit.hvz.items.ItemBuilder;
import com.egsrit.hvz.players.Human;
import com.egsrit.hvz.util.Stats;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

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
                case "giveShirt":
                case "shirt":
                    if(args.length > 2){
                        return giveShirt(sender, args[2], args[1]);
                    } else {
                        sender.sendMessage(ChatColor.RED + "Usage: /hvz giveShirt | shirt type username");
                        return true;
                    }
                default:
                    sender.sendMessage(ChatColor.RED + "HVZ command not found.");
                    return true;
            }
        }
        return false;
    }

    private boolean makeHuman(CommandSender sender, String username){
        Player player;
        try{
            player = Bukkit.matchPlayer(username).get(0);
        } catch(Exception e){
            sender.sendMessage(ChatColor.RED + "Player not found: " + username);
            return true;
        }

        Stats.addHuman(username, player);
        //System.out.println(Stats.getStunMap());
        //System.out.println(Stats.getHumans());
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
        if(Stats.getHumans().get(player.getDisplayName()) != null){
            if(Stats.getHumans().get(player.getDisplayName()).isAlive()){
                // Kill the human if you make them a zombie to prevent any mixup of types
                Stats.getHumans().put(player.getDisplayName(), new Human(player.getDisplayName(), false, false));
            }
        }
        int stunTimer;
        switch(StringUtils.capitalize(specialStatus)){
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

    private boolean giveShirt(CommandSender sender, String username, String type){
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
        ItemStack shirt = new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .setName(chatColor + type + " Shirt")
                .setInfiniteDurability()
                .setLeatherArmorColor(color)
                .setLore("This shirt will make you a", chatColor + "" + ChatColor.BOLD + type)
                .hideFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE)
                .build();
        player.getInventory().addItem(shirt);
        return true;
    }
}
