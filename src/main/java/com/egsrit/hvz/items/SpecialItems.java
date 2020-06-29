package com.egsrit.hvz.items;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class SpecialItems {

    public static ItemStack makeBlaster(){
        return new ItemBuilder(Material.BOW, 1)
                .setName(ChatColor.BLUE + "" + ChatColor.BOLD + "Blaster")
                .setLore("Your very own blaster!")
                .build();
    }

    public static ItemStack makeSock(int qty){
        return new ItemBuilder(Material.SNOWBALL, qty)
                .setName(ChatColor.BLUE + "" + ChatColor.BOLD + "Sock")
                .setLore("Throw it at a zombie!")
                .hideFlags(ItemFlag.HIDE_ATTRIBUTES)
                .build();
    }

    public static ItemStack makeAntivirus(){
        return new ItemBuilder(Material.NAME_TAG)
                .setName(ChatColor.GOLD + "" + ChatColor.BOLD + "Antivirus")
                .setLore("This will turn you back into a human!", "One use only")
                .hideFlags(ItemFlag.HIDE_ATTRIBUTES)
                .build();
    }

    public static ItemStack makeBioCade(){
        //TODO
        return new ItemBuilder(Material.BLACK_CONCRETE)
				.setName(ChatColor.GRAY + "Biocade")
				.setLore("Place this to stop humans from moving through an area!", "Max distance: 15 blocks")
				.hideFlags(ItemFlag.HIDE_ATTRIBUTES)
				.build();
    }

    public static ItemStack makeBodyArmor(){
        return new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .setName(ChatColor.GRAY + "Body Armor")
                .setLore("Equipping this will allow you to block one zombie tag!")
                .setInfiniteDurability()
                .hideFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE)
                .setLeatherArmorColor(Color.GRAY)
                .build();
    }

    public static ItemStack makeDeployableCover(){
        //TODO
        return new ItemBuilder(Material.GREEN_CONCRETE)
				.setName(ChatColor.GREEN + "Deployable Cover")
				.setLore("Place this for a temporary safezone!", "Lasts 2.5 minutes")
				.hideFlags(ItemFlag.HIDE_ATTRIBUTES)
				.build();
    }

    public static ItemStack makeDeplorableCover(){
        //TODO
        return new ItemBuilder(Material.YELLOW_CONCRETE)
				.setName(ChatColor.YELLOW + "Deplorable Cover")
				.setLore("Place this for a respawn point!", "Lasts for 2.5 minutes")
				.hideFlags(ItemFlag.HIDE_ATTRIBUTES)
				.build();

    }

    public static ItemStack makeElephantBlaster(int itemUses){
        return new ItemBuilder(Material.BOW)
                .setName(ChatColor.BLUE + "" + ChatColor.BOLD + "Elephant Blaster")
                .setLore("This can remove a Special Zombie's shirt!", "Uses remaining: " + itemUses)
                .build();
    }

    public static ItemStack makeSpecialShirt(ChatColor ccolor, Color color, String type){
        return new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .setName(ccolor + type + " Shirt")
                .setInfiniteDurability()
                .setLeatherArmorColor(color)
                .setLore("This shirt will make you a", ccolor + "" + ChatColor.BOLD + type)
                .hideFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE)
                .build();
    }
}
