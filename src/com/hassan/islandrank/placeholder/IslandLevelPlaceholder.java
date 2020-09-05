package com.hassan.islandrank.placeholder;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.hassan.islandrank.Main;
import com.hassan.islandrank.ranks.Rank;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class IslandLevelPlaceholder extends PlaceholderExpansion  {

	Main plugin;
	public IslandLevelPlaceholder(Main plugin){
		this.plugin = plugin;
	}

	@Override
	public String getIdentifier() {
		return "IslandRank";
	}

	@Override
	@Nonnull
	public String getAuthor(){
		return plugin.getDescription().getAuthors().toString();
	}

	@Override
	@Nonnull
	public String getVersion(){
		return plugin.getDescription().getVersion();
	}

	@Override
	public boolean persist(){
		return true;
	}


	@Override
	public boolean canRegister(){
		return true;
	}

	@Override
	public String getRequiredPlugin(){
		return "IslandRank";
	}

	public String onRequest(Player player, String identifier){

		if(player == null){
			return "";
		}


		// %someplugin_placeholder1%
		if(identifier.equals("Rank")){
			String level = "0";
			if(SuperiorSkyblockAPI.getPlayer(player).getIsland() != null) {
				Rank rank = new Rank(SuperiorSkyblockAPI.getPlayer(player).getIsland().getOwner().getUniqueId());
				level = String.valueOf(rank.getRank());
			}
			return level;
		}

		// We return null if an invalid placeholder (f.e. %someplugin_placeholder3%)
		// was provided
		return null;
	}
}
