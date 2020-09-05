package com.hassan.islandrank.ranks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.hassan.islandrank.Main;

public class Rank {
	
	private UUID owner;
	
	public Rank(UUID owner) {
		this.owner = owner;
	}
	
	public int getRank() {
		int rank = 0;
		if(Main.getInstance().rankMap.containsKey(this.owner)) {
			rank = Integer.valueOf(Main.getInstance().rankMap.get(this.owner));
		}
		return rank;
	}
	
	public String nextRank() {
		return Main.getInstance().getConfig().getString("IslandRanks." + this.getRank() + ".Next_Rank");
	}
	
	public double getCost() {
		return Main.getInstance().getConfig().getDouble("IslandRanks." + this.getRank() + ".Price");
	}
	
	public List<String> getRewards(){
		List<String> getCommands = new ArrayList<>();
		for(String commands : Main.getInstance().getConfig().getStringList("IslandRanks." + this.getRank() + ".Commands")) {
			getCommands.add(commands);
		}
		return getCommands;
	}
	
	public void setRank(int rank) {
		Main.getInstance().getData().set("IslandRanks." + this.owner.toString() + ".Level", rank);
		Main.getInstance().saveData();
		this.addRank(String.valueOf(rank));
		Main.getInstance().rankMap.put(this.owner, rank);
	}
	
	public List<String> getLevels(){
		List<String> levels = new ArrayList<>();
		for(String level : Main.getInstance().getData().getStringList("IslandRanks." + this.owner.toString() + ".LevelList")) {
			levels.add(level);
		}
		return levels;
	}
	
	public void addRank(String rank) {
        List<String> vcl = Main.getInstance().getData().getStringList("IslandRanks." + this.owner.toString() + ".LevelList");
        vcl.add(rank);
        Main.getInstance().getData().set("IslandRanks." + this.owner.toString() + ".LevelList", vcl);
        Main.getInstance().saveData();
    }
    
    public void removeRank(String rank) {
    	List<String> vcl = Main.getInstance().getData().getStringList("IslandRanks." + this.owner.toString() + ".LevelList");
        vcl.remove(rank);
        Main.getInstance().getData().set("IslandRanks." + this.owner.toString() + ".LevelList", vcl);
        Main.getInstance().saveData();
        
        if(Main.getInstance().rankMap.containsKey(this.owner)) {
        	Main.getInstance().rankMap.remove(this.owner);
        }
        Main.getInstance().getData().set("IslandRanks." + this.owner.toString() + ".Level", String.valueOf(0));
        Main.getInstance().saveData();
        
    }
	
	
	
	
	
	

}
