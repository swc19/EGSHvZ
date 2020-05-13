package com.egsrit.hvz.listeners;

import com.egsrit.hvz.HvZPlugin;
import com.egsrit.hvz.items.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class WeaponListener implements Listener {
    private static final Map<String, Long> shotCooldowns = new HashMap<>();

    @EventHandler
    public void onWeaponShot(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
            ItemStack mainHand = p.getInventory().getItemInMainHand();
            ItemStack offHand = p.getInventory().getItemInOffHand();
            if(mainHand.getType() == Material.BOW || offHand.getType() == Material.BOW){
                if(mainHand.hasItemMeta()){
                    shootArrow(e, p, mainHand);
                } else if(offHand.hasItemMeta()){
                    shootArrow(e, p, offHand);
                }
            }
        }
    }

    private void shootArrow(PlayerInteractEvent e, Player p, ItemStack handItem){
        int cooldownTime = 1; // cooldown time in seconds
        if(ChatColor.stripColor(handItem.getItemMeta().getDisplayName()).equals("Blaster")) {
            e.setCancelled(true);
            if (!shotCooldowns.containsKey(p.getDisplayName()) || shotCooldowns.get(p.getDisplayName()) <= System.currentTimeMillis()) {
                p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0f, 1.0f);
                p.launchProjectile(Arrow.class);
                shotCooldowns.put(p.getDisplayName(), System.currentTimeMillis() + (cooldownTime*1000)); // use milliseconds here to avoid rounding issues with one second increment
                tickDurability(handItem);
            } else {
                p.sendMessage(ChatColor.RED + "You can't shoot yet!");
            }
        }
    }

    private void tickDurability(ItemStack handItem){
        new BukkitRunnable(){
            final ItemBuilder builder = new ItemBuilder(handItem);
            final int bowDurability = handItem.getType().getMaxDurability();
            int counter = bowDurability;
            @Override
            public void run() {
                if(counter <= 0){
                    builder.setDurability(0);
                    this.cancel();
                } else {
                    counter -= bowDurability / 20;
                    builder.setDurability(counter);
                }
            }
        }.runTaskTimer(HvZPlugin.getInstance(), 0L, 1L);

    }
}
