package com.hassan.islandrank.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.hassan.islandrank.Main;
import com.hassan.islandrank.ranks.Rank;

public class Gui {
	
	
	public void createGUI(Player player) {
		int maxRank = 0;
		Rank rank = new Rank(SuperiorSkyblockAPI.getPlayer(player).getIsland().getOwner().getUniqueId());
		Inventory inv = Bukkit.createInventory(player, Main.getInstance().getConfig().getInt("IslandRanksSize"), Main.getInstance().colorMessage(Main.getInstance().getConfig().getString("IslandRanksGUIName")));
		ItemStack item = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("GuiMaterials.NotBought.Material")), 1, (short) Main.getInstance().getConfig().getInt("GuiMaterials.NotBought.Data"));
		ItemMeta meta = item.getItemMeta();
		ItemStack item1 = new ItemStack(Material.valueOf(Main.getInstance().getConfig().getString("GuiMaterials.Bought.Material")), 1, (short) Main.getInstance().getConfig().getInt("GuiMaterials.Bought.Data"));
		ItemMeta meta1 = item1.getItemMeta();
		int rankNumber = 1;
		for(String gui : Main.getInstance().getConfig().getConfigurationSection("IslandRanksGUI").getKeys(false)) {
			maxRank = Integer.valueOf(gui);
				String name = Main.getInstance().colorMessage(Main.getInstance().getConfig().getString("IslandRanksGUI." + rankNumber + ".Name"));
				List<String> lore = Main.getInstance().getConfig().getStringList("IslandRanksGUI." + rankNumber + ".Lore");
                ArrayList<String> formattedLore = new ArrayList<>();
                if(lore != null && lore.size() > 0){
                    for(String lineInLore : lore){
                    	lineInLore = lineInLore.replace("{infolevel}", Main.getInstance().getConfig().getString("Messages.NotBoughtInfoLevel"));
                        formattedLore.add(ChatColor.translateAlternateColorCodes('&',lineInLore));
                    }
                }
                meta.setDisplayName(name);
                meta.setLore(formattedLore);
                item.setItemMeta(meta);
               
                String name1 = Main.getInstance().colorMessage(Main.getInstance().getConfig().getString("IslandRanksGUI." + rankNumber + ".Name"));
				List<String> lore1 = Main.getInstance().getConfig().getStringList("IslandRanksGUI." + rankNumber + ".Lore");
                ArrayList<String> formattedLore1 = new ArrayList<>();
                if(lore1 != null && lore1.size() > 0){
                    for(String lineInLore1 : lore1){
                    	if(rank.getRank() == rankNumber) {
                    		lineInLore1 = lineInLore1.replace("{infolevel}", Main.getInstance().getConfig().getString("Messages.AlreadyTheInfoLevel"));
                    	}else {
                    		lineInLore1 = lineInLore1.replace("{infolevel}",  Main.getInstance().getConfig().getString("Messages.BoughtInfoLevel"));
                    	}
                        formattedLore1.add(ChatColor.translateAlternateColorCodes('&',lineInLore1));
                    }
                }
                meta1.setDisplayName(name1);
                meta1.setLore(formattedLore1);
                item1.setItemMeta(meta1);
                inv.setItem(Integer.valueOf(gui) - 1, item);
                rankNumber++;
                
                
                //inv.setItem(Integer.valueOf(gui) - 1, item1);
                
                
		}
		for(String levels : rank.getLevels()) {
			String name1 = Main.getInstance().colorMessage(Main.getInstance().getConfig().getString("IslandRanksGUI." + levels + ".Name"));
			List<String> lore1 = Main.getInstance().getConfig().getStringList("IslandRanksGUI." + levels + ".Lore");
	        ArrayList<String> formattedLore1 = new ArrayList<>();
	        if(lore1 != null && lore1.size() > 0){
	            for(String lineInLore1 : lore1){
	            	if(rank.getRank() == Integer.valueOf(levels)) {
	            		lineInLore1 = lineInLore1.replace("{infolevel}", Main.getInstance().getConfig().getString("Messages.AlreadyTheInfoLevel"));
	            	}else {
	            		lineInLore1 = lineInLore1.replace("{infolevel}",  Main.getInstance().getConfig().getString("Messages.BoughtInfoLevel"));
	            	}
	                formattedLore1.add(ChatColor.translateAlternateColorCodes('&',lineInLore1));
	            }
	        }
	        meta1.setDisplayName(name1);
	        meta1.setLore(formattedLore1);
	        item1.setItemMeta(meta1);
        	inv.setItem(Integer.valueOf(levels) -1 , item1);
        }
		player.openInventory(inv);
	}
	
	

}
