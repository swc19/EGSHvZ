package com.egsrit.hvz.listeners;

import com.egsrit.hvz.util.Stats;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SpecialItemListener implements Listener {

    private static final Map<String, Boolean> avUsed = new HashMap<>();

    @EventHandler
    public void onAntivirus(PlayerInteractEvent e){
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
            Player p = e.getPlayer();
            ItemStack mainHandItem = p.getInventory().getItemInMainHand();
            if(mainHandItem.hasItemMeta()){
                if(mainHandItem.getItemMeta().hasDisplayName()){
                    if(ChatColor.stripColor(mainHandItem.getItemMeta().getDisplayName()).equals("Antivirus")){
                        if(Stats.getZombies().containsKey(p.getDisplayName())){
                            if(!avUsed.containsKey(p.getDisplayName())){
                                Stats.addHuman(p.getDisplayName(), p);
                                avUsed.put(p.getDisplayName(), true);
                                p.getInventory().remove(mainHandItem);
                                // On the off chance a special zombie uses an antivirus, remove their shirt
                                p.getInventory().setItem(38, new ItemStack(Material.AIR));
                            } else {
                                p.sendMessage(ChatColor.RED + "You've already used an antivirus!");
                            }
                        } else if (Stats.getHumans().containsKey(p.getDisplayName())){
                            if(!avUsed.containsKey(p.getDisplayName())){
                                p.sendMessage(ChatColor.RED + "You're a human and can't use this... yet.");
                            } else {
                                p.sendMessage(ChatColor.RED + "You've already used an antivirus!");
                            }
                        }
                    }
                }
            }
        }
    }
}
