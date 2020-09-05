package com.hassan.islandrank.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.hassan.islandrank.commandhook.IsLevelHook;


public class IslandCommand implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCommand(PlayerCommandPreprocessEvent e) {
		Player player = e.getPlayer();
		String label = e.getMessage().substring(1);
		boolean parsed = IsLevelHook.parseCommand(player, label);
		if (parsed) {
			e.setCancelled(true);
		}
			
	}

}
