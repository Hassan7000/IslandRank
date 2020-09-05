package com.hassan.islandrank.placeholder;

import org.bukkit.entity.Player;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.hassan.islandrank.Main;
import com.hassan.islandrank.ranks.Rank;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import java.util.UUID;

public class RankPlaceholder extends PlaceholderExpansion {

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "Hassan";
	}

	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return "IslandRank";
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return Main.getInstance().getDescription().getVersion();
	}
	
	@Override
    public boolean persist(){
        return true;
    }

    /**
     * Because this is a internal class, this check is not needed
     * and we can simply return {@code true}
     *
     * @return Always true since it's an internal class.
     */
    @Override
    public boolean canRegister(){
        return true;
    }
	

    public String onPlaceholderRequest(Player player, String identifier){

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
