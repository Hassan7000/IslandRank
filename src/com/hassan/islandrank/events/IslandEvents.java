package com.hassan.islandrank.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.events.IslandCreateEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandJoinEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandKickEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandQuitEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandTransferEvent;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.hassan.islandrank.Main;
import com.hassan.islandrank.islandtop.IslandTop;
import com.hassan.islandrank.ranks.Rank;

public class IslandEvents implements Listener {
	
	
	@EventHandler
	public void islandCreate(IslandCreateEvent e) {
		Rank rank = new Rank(e.getPlayer().getUniqueId());
		String getRank = "";
		for(String ranks : Main.getInstance().getConfig().getConfigurationSection("IslandRanks").getKeys(false)) {
			boolean firstRank = Main.getInstance().getConfig().getBoolean("IslandRanks." + ranks + ".FirstRank");
			if(firstRank) {
				getRank = ranks;
			}
		}
		rank.setRank(Integer.valueOf(getRank));
	}
	
	@EventHandler
	public void islandDelete(IslandDisbandEvent e) {
		Rank rank = new Rank(e.getPlayer().getTeamLeader());
		for(String levels : rank.getLevels()) {
			rank.removeRank(levels);
		}
		for(String command : Main.getInstance().getConfig().getStringList("Leave.Commands")) {
			for(SuperiorPlayer uuid : e.getPlayer().getIsland().getIslandMembers(true)) {
				OfflinePlayer member = uuid.asOfflinePlayer();
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", member.getName()));
			}
		}
		
		
		
		
		
		
	}
	
	
	
	@EventHandler
	public void islandKick(IslandKickEvent e) {
		Player player = e.getTarget().asPlayer();
		for(String command : Main.getInstance().getConfig().getStringList("Leave.Commands")) {
			command = command.replace("{player}", player.getName());
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
		}
	}
	
	@EventHandler
	public void islandQuit(IslandQuitEvent e) {
		Player player = e.getPlayer().asPlayer();
		
		
		for(String command : Main.getInstance().getConfig().getStringList("Leave.Commands")) {
			command = command.replace("{player}", player.getName());
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
		}
		
		Island island = SuperiorSkyblockAPI.getPlayer(e.getPlayer().asPlayer()).getIsland();
		
		if(Main.getInstance().rankMap.containsKey(e.getPlayer().asPlayer().getUniqueId())){
			Bukkit.broadcastMessage(e.getPlayer().getName() + "has been removed");
			Main.getInstance().rankMap.remove(e.getPlayer().asPlayer().getUniqueId());
		}
		
		
		
		
	}
	
	@EventHandler
	public void islandJoin(IslandJoinEvent e) {
		Player player = e.getPlayer().asPlayer();
		Rank rank = new Rank(e.getIsland().getOwner().getUniqueId());
		for(String commands : Main.getInstance().getConfig().getStringList("IslandRanks." + rank.getRank() + ".Commands")) {
			commands = commands.replace("{player}", player.getName());
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands);
		}
	}
	
	@EventHandler
	public void islandChangeLeader(IslandTransferEvent e) {
		Rank rank = new Rank(e.getOldOwner().getUniqueId());
		Rank newOwner = new Rank(e.getNewOwner().getUniqueId());
		newOwner.setRank(rank.getRank());
		for(String levels : rank.getLevels()) {
			newOwner.addRank(levels);
		}
		Main.getInstance().getData().set("IslandRanks." + e.getOldOwner().getUniqueId().toString(), null);
		Main.getInstance().saveData();
		
		if(Main.getInstance().rankMap.containsKey(e.getOldOwner().getUniqueId())) {
			Main.getInstance().rankMap.remove(e.getOldOwner().getUniqueId());
		}
		
		
	}
	
	
	
	
	
	
}
