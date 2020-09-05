package com.hassan.islandrank.islandtop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.key.Key;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.hassan.islandrank.EntryComparator;
import com.hassan.islandrank.Main;
import com.hassan.islandrank.ranks.Rank;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class IslandTop {

	public IslandTop() {

	}

	

	
	
	private double getWorth(UUID id) {
		double worth = 0;
        Island island = SuperiorSkyblockAPI.getPlayer(id).getIsland();
        if(island != null) {
        	return island.getWorth().doubleValue();
        }
        return worth;
        
    }
	

	@SuppressWarnings("deprecation")
	public void getTop10IslandMessage(Player player1) {

		int rankNumber = 0;
		CalculateTop top = new CalculateTop();
		String headerMessage = Main.getInstance().getConfig().getString("IslandTop_Header");
		player1.sendMessage(ChatColor.translateAlternateColorCodes('&', headerMessage));
		List<String> topSize = new ArrayList<>();
		Comparator<Entry<UUID, Integer>> comp = Comparator.<Entry<UUID, Integer>>comparingInt(Entry::getValue).thenComparing(e -> getWorth(e.getKey())).reversed();
	    List<Entry<UUID, Integer>> sorted = Main.getInstance().rankMap.entrySet().stream().sorted(comp).collect(Collectors.toList());
		for (int i = 0; i < sorted.size(); i++) {
			Entry<UUID, Integer> e = sorted.get(i);
			 
			topSize.add(e.getKey().toString());
			if (topSize.size() != 10 + 1 && topSize.size() < 11) {
				Island island = SuperiorSkyblockAPI.getPlayer(e.getKey()).getIsland();

				if (island != null) {
					Rank rank = new Rank(e.getKey());
					rankNumber++;

					List<String> yourStringList = Main.getInstance().getConfig().getStringList("IslandTopHoverFormat");
					ComponentBuilder cp = new ComponentBuilder("");
					for (String str : yourStringList) {
						if (!str.contains("{members}")) {
							str = str.replace("{worth}", String.valueOf(island.getWorth()));
							str = str.replace("{level}", String.valueOf(rank.getRank()));
							String withPlaceholders = PlaceholderAPI
									.setPlaceholders(island.getOwner().asOfflinePlayer(), str);
							cp.append(org.bukkit.ChatColor.translateAlternateColorCodes('&', withPlaceholders) + "\n");
						} else {
							for (SuperiorPlayer uuid : island.getIslandMembers(true)) {
								String memberMessage = ChatColor.translateAlternateColorCodes('&',
										Main.getInstance().getConfig().getString("IslandMembersFormat"));
								Player player = uuid.asPlayer();
								if (player != null) {
									str.replace("{members}", "");
									memberMessage = memberMessage.replace("{member_name}", player.getName());
									memberMessage = memberMessage.replace("{status}",
											Main.getInstance().getConfig().getString("IslandMemberStatus.Online"));
									cp.append(org.bukkit.ChatColor.translateAlternateColorCodes('&', memberMessage)
											+ "\n");
								} else {
									memberMessage = memberMessage.replace("{member_name}", uuid.getName());
									memberMessage = memberMessage.replace("{status}",
											Main.getInstance().getConfig().getString("IslandMemberStatus.Offline"));
									cp.append(org.bukkit.ChatColor.translateAlternateColorCodes('&', memberMessage)
											+ "\n");
								}

							}
						}
					}

					String islandTopName = ChatColor.translateAlternateColorCodes('&',
							Main.getInstance().getConfig().getString("IslandTopName"));
					islandTopName = islandTopName.replace("{islandowner}", island.getOwner().getName());
					islandTopName = islandTopName.replace("{level}", String.valueOf(rank.getRank()));
					islandTopName = islandTopName.replace("{ranknumber}", String.valueOf(rankNumber));
					islandTopName = islandTopName.replace("{online_members}",
							String.valueOf(this.getOnlineMember(island)));
					islandTopName = islandTopName.replace("{offline_members}", String.valueOf(island.getTeamLimit()));
					String withPlaceholders = PlaceholderAPI.setPlaceholders(
							SuperiorSkyblockAPI.getPlayer(e.getKey()).asOfflinePlayer(), islandTopName);
					TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', withPlaceholders));
					String command = Main.getInstance().getConfig().getString("IslandClickCommand");
					command = command.replace("{islandowner}", island.getOwner().getName());
					message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
					message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, cp.create()));

					player1.spigot().sendMessage(message);

				}
			}

		}

		String notFound = Main.getInstance().getConfig().getString("IslandNotFound");
		for (int i = 0; i < 10 + 1; ++i) {
			if (i > topSize.size()) {
				player1.sendMessage(ChatColor.translateAlternateColorCodes('&',
						notFound.replace("{ranknumber}", String.valueOf(i))));
				
			}
		}
		String footerMessage = Main.getInstance().getConfig().getString("IslandTop_Footer");
		player1.sendMessage(ChatColor.translateAlternateColorCodes('&', footerMessage));
		sorted.clear();
		return;
	}

	private int getOnlineMember(Island island) {
		List<String> memberSize = new ArrayList<>();
		for (SuperiorPlayer uuid : island.getIslandMembers(true)) {
			Player player = uuid.asPlayer();
			if (player != null) {
				memberSize.add(player.getName());
			}
		}
		return memberSize.size();
	}

	public List<UUID> isInMap(HashMap<Integer, UUID> map) {
		List<UUID> uuid = new ArrayList<>();

		for (Map.Entry<Integer, UUID> entry : map.entrySet()) {
			uuid.add(entry.getValue());
		}

		return uuid;
	}

}
