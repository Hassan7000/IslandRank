package com.hassan.islandrank.commandhook;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblock;
import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.commands.SuperiorCommand;
import com.bgsoftware.superiorskyblock.api.island.Island;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IslandPoints implements SuperiorCommand {
	@Override
	public List<String> getAliases() {
		List<String> commands = new ArrayList<>();
		commands.add("points");
		commands.add("point");
		return commands;
	}

	@Override
	public boolean canBeExecutedByConsole() {
		return false;
	}

	@Override
	public String getPermission() {
		return "SuperiorSkyblock.island.points";
	}

	@Override
	public int getMaxArgs() {
		return 2;
	}

	@Override
	public void execute(SuperiorSkyblock superiorSkyblock, CommandSender commandSender, String[] strings) {
		if(strings.length == 0 || strings.length == 1){
			Player player = (Player) commandSender;
			player.performCommand("is values");
		}
		if(strings.length == 2){
			Player player = (Player) commandSender;
			String name = strings[1];
			player.performCommand("is values " + name);

		}
	}

	@Override
	public boolean displayCommand() {
		return false;
	}

	@Override
	public String getDescription(Locale locale) {
		return "Shows the points";
	}

	@Override
	public int getMinArgs() {
		return 0;
	}

	@Override
	public List<String> tabComplete(SuperiorSkyblock superiorSkyblock, CommandSender commandSender, String[] strings) {
		List<String> commands = new ArrayList<>();
		if(strings.length == 2){
			String name = strings[1];
			for(Player player : Bukkit.getOnlinePlayers()){
				if(player.getName().startsWith(name) || player.getName().toLowerCase().startsWith(name)){
					commands.add(player.getName());
				}
			}
			for(Island island : SuperiorSkyblockAPI.getGrid().getIslands()){
				if(island.getName().startsWith(name) || island.getName().toLowerCase().startsWith(name)){
					commands.add(island.getName());
				}
			}
		}

		return commands;
	}

	@Override
	public String getUsage(Locale locale) {
		return "is points <player>";
	}
}
