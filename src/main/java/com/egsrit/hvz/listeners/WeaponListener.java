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
import org.bukkit.metadata.FixedMetadataValue;
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
        // TODO check elephant blaster
        String itemName = ChatColor.stripColor(handItem.getItemMeta().getDisplayName());
        if(itemName.contains("Blaster")) {
            e.setCancelled(true);
            if (!shotCooldowns.containsKey(p.getDisplayName()) || shotCooldowns.get(p.getDisplayName()) <= System.currentTimeMillis()) {
                if(itemName.equals("Elephant Blaster")){
                    // Increase the cooldown on elephant blaster
                    cooldownTime = 3;
                    String loreLine = handItem.getItemMeta().getLore().get(1); // Change the lore to reflect the number of uses left
                    int itemUses = Integer.parseInt(loreLine.substring(loreLine.length() - 1));
                    if(itemUses == 1){
                        p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 0.8f, 1.0f);
                        p.sendMessage(ChatColor.GOLD + "That was your last use!");
                        p.getInventory().remove(handItem);
                    } else {
                        handItem = new ItemBuilder(handItem).setLore("This can remove a Special Zombie's shirt!", "Uses remaining: " + (itemUses - 1)).build();
                    }
                }
                p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0f, 1.0f);
                Arrow arrow = p.launchProjectile(Arrow.class);
                arrow.setMetadata("Weapon Name", new FixedMetadataValue(HvZPlugin.getInstance(), itemName)); // set metadata of the weapon name to check later against a stun
                shotCooldowns.put(p.getDisplayName(), System.currentTimeMillis() + (cooldownTime*1000)); // use milliseconds here to avoid rounding issues with one second increment
                tickDurability(handItem, cooldownTime);
            } else {
                p.sendMessage(ChatColor.RED + "You can't shoot yet!");
            }
        }
    }

    private void tickDurability(ItemStack handItem, int seconds){
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
                    counter -= bowDurability / (seconds * 20); // number of ticks to increment
                    builder.setDurability(counter);
                }
            }
        }.runTaskTimer(HvZPlugin.getInstance(), 0L, 1L);
    }
}
