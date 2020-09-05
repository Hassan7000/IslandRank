package com.hassan.islandrank.hologramplaceholders;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;
import com.hassan.islandrank.Main;
import com.hassan.islandrank.islandtop.CalculateTop;
import com.hassan.islandrank.islandtop.IslandTop;
import com.hassan.islandrank.ranks.Rank;

public class HologramPlaceholderThree implements PlaceholderReplacer {

	@Override
	public String update() {
		String finalMessage = "No island";
		Comparator<Entry<UUID, Integer>> comp = Comparator.<Entry<UUID, Integer>>comparingInt(Entry::getValue).thenComparing(e -> getWorth(e.getKey())).reversed();
	    List<Entry<UUID, Integer>> sorted = Main.getInstance().rankMap.entrySet().stream().sorted(comp).collect(Collectors.toList());
		for (int i = 0; i < sorted.size(); i++) {
			Entry<UUID, Integer> e = sorted.get(i);
			if (i + 1 == 3) {
				Rank rank = new Rank(e.getKey());
				Island island = SuperiorSkyblockAPI.getPlayer(e.getKey()).getIsland();
				String hologramPlaceholder = ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("HologramDisplay"));
				hologramPlaceholder = hologramPlaceholder.replace("{ranknumber}", String.valueOf(3));
				hologramPlaceholder = hologramPlaceholder.replace("{islandowner}", island.getOwner().getName());
				hologramPlaceholder = hologramPlaceholder.replace("{level}", String.valueOf(rank.getRank()));
				hologramPlaceholder = hologramPlaceholder.replace("{worth}", String.valueOf(island.getWorth()));
				hologramPlaceholder = hologramPlaceholder.replace("{islandname}", island.getName());
				finalMessage = hologramPlaceholder;
				
			}

		}

		return finalMessage;
	}
	
	private double getWorth(UUID id) {
		double worth = 0;
        Island island = SuperiorSkyblockAPI.getPlayer(id).getIsland();
        if(island != null) {
        	return island.getWorth().doubleValue();
        }
        return worth;
        
    }

}
