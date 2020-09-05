package com.hassan.islandrank.islandtop;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.hassan.islandrank.Main;



public class CalculateTop {
	
	
	
	BukkitTask task;
	
	public void startTopTimer(){
		 task = new BukkitRunnable() {
           public void run() {
        	   for (int i = 0; i < calculateTop().size(); i++) {
                   Entry<UUID, Integer> e = calculateTop().get(i);
                   Main.getInstance().topMap.put(i + 1, e.getKey());
                   Bukkit.broadcastMessage("Updated");
               }
           }
		}.runTaskTimer(Main.getInstance(), 20, Main.getInstance().getConfig().getInt("IslandTopTimer") * 20);
   }
	
	public List<Entry<UUID, Integer>> calculateTop() {
		Comparator<Entry<UUID, Integer>> comp = Comparator.<Entry<UUID, Integer>>comparingInt(Entry::getValue).thenComparing(e -> getWorth(e.getKey())).reversed();
	    List<Entry<UUID, Integer>> sorted = Main.getInstance().rankMap.entrySet().stream().sorted(comp).collect(Collectors.toList());
	    return sorted;
	}
	
	

   // for (int i = 0; i < sorted.size(); i++) {
     //   Entry<UUID, Integer> e = sorted.get(i);
    //    System.out.println("#" + i + ": " + e.getKey() + " with level " + e.getValue() + " and worth " + getWorth(e.getKey()));
   // }
	
	private double getWorth(UUID id) {
		double worth = 0;
        Island island = SuperiorSkyblockAPI.getPlayer(id).getIsland();
        if(island != null) {
        	return island.getWorth().doubleValue();
        }
        return worth;
        
    }
	
	public void cancelTask() {
		task.cancel();
	}

}
