package com.hassan.islandrank.commandhook;

import java.util.*;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.hassan.islandrank.Main;
import com.hassan.islandrank.islandtop.IslandTop;
import com.hassan.islandrank.ranks.Rank;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.metadata.MetadataValue;


public class IsLevelHook {
	
	private static final String ARGS_SEPARATOR = " ";
	private static final String[] ASB_COMMAND_ALIASES = {"is", "island"};
	private static final String[] ARGS_TO_OVERRIDE = {"level", "rank", "rankreload", "top", "worth", "who", "info", "show"};
	@SuppressWarnings("deprecation")
	public static boolean parseCommand(Player player, String label) {
		 String[] args = label.split(ARGS_SEPARATOR);
	      String cmd = args[0];
	      SuperiorSkyblockAPI api;
	      if (Arrays.stream(ASB_COMMAND_ALIASES).noneMatch(asbCommand -> asbCommand.equalsIgnoreCase(cmd))) return false;
	      args = Arrays.copyOfRange(args, 1, args.length);
	       if (args.length == 0) return false;
	       
	       String finalCmd = args[0];
	       if (Arrays.stream(ARGS_TO_OVERRIDE).noneMatch(arg -> arg.equalsIgnoreCase(finalCmd))) return false;
	       if(finalCmd.equalsIgnoreCase("level") || finalCmd.equalsIgnoreCase("rank")) {
	    	   if(args.length == 1) {
	    		   if (SuperiorSkyblockAPI.getPlayer(player).getIsland() != null && SuperiorSkyblockAPI.getPlayer(player).getIsland().isInside(player.getLocation())) {
	    			   Main.getInstance().gui.createGUI(player);
	    			   return true;
	    		   }else {
	    			   if(SuperiorSkyblockAPI.getIslandAt(player.getLocation()) != null) {
	    				   
	    				   Island island = SuperiorSkyblockAPI.getIslandAt(player.getLocation());
	    				   
	    				   if(island == null) {
	    					   player.sendMessage(Main.getInstance().colorMessage("&cYou have to be on your island before you can execute this command"));
	    					   return true;
	    				   }
	    				   
	    				   
	    				   SuperiorPlayer target = island.getOwner();
	    				   
	    				   
			    		   if(target != null) {
			    			   Rank rank = new Rank(target.getIsland().getOwner().getUniqueId());
			    			   String levelMessage = Main.getInstance().getConfig().getString("Messages.LevelMessage");
			    			   levelMessage = levelMessage.replace("{target}", target.getName());
			    			   levelMessage = levelMessage.replace("{level}", String.valueOf(rank.getRank()));
			    			   player.sendMessage(Main.getInstance().colorMessage(levelMessage));
			    			   return true;
			    		   }else {
			    			   Main.getInstance().gui.createGUI(player);  
			    			   return true;
			    		   }
	    			   }else {
	    				   Main.getInstance().gui.createGUI(player);
	    				   return true;
	    			   }
	    		   }
	    	   }
	    	   if(args.length == 2) {
	    		  Player target = Bukkit.getPlayer(args[1]);
	    		   if(target != null) {
	    			   if(SuperiorSkyblockAPI.getPlayer(target.getPlayer()).getIsland() != null) {
	    				   
	    				   Island island = null;
	    				   if(SuperiorSkyblockAPI.getPlayer(target.getPlayer()).getIsland() != null) {
	    					   island = SuperiorSkyblockAPI.getPlayer(target.getPlayer()).getIsland();
	    				   }else if(SuperiorSkyblockAPI.getGrid().getIsland(args[1]) != null) {
	    					   island = SuperiorSkyblockAPI.getGrid().getIsland(args[1]);
	    				   }
	    				   
	    				   if(island == null) {
	    					   player.sendMessage(Main.getInstance().colorMessage(Main.getInstance().getConfig().getString("Messages.PlayerDoesNotHaveAnIsland")));
		    				   return true;
	    				   }
	    				   
	    				   Rank rank = new Rank(island.getOwner().getUniqueId());
		    			   String levelMessage = Main.getInstance().getConfig().getString("Messages.LevelMessage");
		    			   levelMessage = levelMessage.replace("{target}", target.getName());
		    			   levelMessage = levelMessage.replace("{level}", String.valueOf(rank.getRank()));
		    			   player.sendMessage(Main.getInstance().colorMessage(levelMessage));
		    			   return  true;
	    			   }else {
	    				   player.sendMessage(Main.getInstance().colorMessage(Main.getInstance().getConfig().getString("Messages.PlayerDoesNotHaveAnIsland")));
	    				   return true;
	    			   }
	    		   }else {
	    			   if(SuperiorSkyblockAPI.getPlayer(args[1]).getIsland() != null || SuperiorSkyblockAPI.getGrid().getIsland(args[1]) != null) {
	    				   
	    				   Island island = null;
	    				   
	    				   if(SuperiorSkyblockAPI.getPlayer(args[1]).getIsland() != null) {
	    					   island = SuperiorSkyblockAPI.getPlayer(args[1]).getIsland();
	    				   }else if(SuperiorSkyblockAPI.getGrid().getIsland(args[1]) != null) {
	    					   island = SuperiorSkyblockAPI.getGrid().getIsland(args[1]);
	    				   }
	    				   
	    				   Rank rank = new Rank(island.getOwner().getUniqueId());
		    			   String levelMessage = Main.getInstance().getConfig().getString("Messages.LevelMessage");
		    			   levelMessage = levelMessage.replace("{target}", args[1]);
		    			   levelMessage = levelMessage.replace("{level}", String.valueOf(rank.getRank()));
		    			   player.sendMessage(Main.getInstance().colorMessage(levelMessage));
	    				   return true;
	    			   }else {
	    				   player.sendMessage(Main.getInstance().colorMessage(Main.getInstance().getConfig().getString("Messages.PlayerDoesNotHaveAnIsland")));
	    				   return true;
	    			   }
	    			   
	    		   }
	    	   }
	       }
	       if(finalCmd.equalsIgnoreCase("rankreload")) {
	    	   if(args.length == 1) {
	    		   if(player.hasPermission("IslandRank.reload")) {
	    			   Main.getInstance().reloadConfig();
	    			   player.sendMessage("You have reloaded the config file");
	    			   return true;
	    		   }
		       }
	       }
	       if(finalCmd.equalsIgnoreCase("top")) {
	    	   if(args.length == 1) {
	    		   
	    		   
	    		   IslandTop top = new IslandTop();
	    		   top.getTop10IslandMessage(player);
	    			  
	    		   
	    	   }
	       }
	       if(finalCmd.equalsIgnoreCase("worth")) {
	    	   if(args.length == 1) {
	    		   
	    		   String worthMessage = PlaceholderAPI.setPlaceholders(player, Main.getInstance().getConfig().getString("Messages.Worth-Message"));
	    		   player.sendMessage(worthMessage);

	    	   }
	       }
	       if(finalCmd.equalsIgnoreCase("who") || finalCmd.equalsIgnoreCase("show") || finalCmd.equalsIgnoreCase("info")) {
	    	   if(args.length == 1) {
	    		   showWhoMessage(player);
	    		   return true;
	    		   
	    	   }
			   if(args.length == 2) {
				   String name = args[1];
				   showTargetWhoMessage(player, name);
				   return true;
			   }
	    	   
	    	   

	       }

	    	   
		return true;
	 }



	 public static void showWhoMessage(Player player){
		 if(SuperiorSkyblockAPI.getPlayer(player).getIsland() != null) {
			 Island island = SuperiorSkyblockAPI.getPlayer(player).getIsland();
			 String placeholderMessage = "";
			 int amountOnline = 0;
			 int amountOffline = 0;
			 int islandLevel = 0;
			 TreeSet<String> roleOnline = new TreeSet<String>();
			 TreeSet<String> roleOffline = new TreeSet<String>();
			 for (SuperiorPlayer uuid : island.getIslandMembers(true)) {
				 Player sPlayer = Bukkit.getPlayer(uuid.getUniqueId());
				 islandLevel = getLevel(uuid.getIslandLeader().getUniqueId());
				 if(sPlayer != null) {
					 if(!isVanished(sPlayer)){
						 amountOnline++;
						 roleOnline.add(Main.getInstance().colorMessage("&a(" + uuid.getPlayerRole() +")" + " " + uuid.getName()));
					 }

				 }else {
					 amountOffline++;
					 roleOffline.add(Main.getInstance().colorMessage("&c(" + uuid.getPlayerRole() +")" + " " + uuid.getName()));
				 }

			 }

			 List<String> fullMessage = new ArrayList<>();

			 for(String whoMessage : Main.getInstance().getConfig().getStringList("WhoMessage")) {
				 whoMessage = whoMessage.replace("%amount_online%", String.valueOf(amountOnline));
				 whoMessage = whoMessage.replace("%amount_offline%", String.valueOf(amountOffline));
				 whoMessage = whoMessage.replace("%island_level%", String.valueOf(islandLevel));
				 whoMessage = whoMessage.replace("{0}", roleOnline.toString());
				 whoMessage = whoMessage.replace("{1}", roleOffline.toString());
				 whoMessage = whoMessage.replace("[", "");
				 whoMessage = whoMessage.replace("]", "");
				 placeholderMessage = PlaceholderAPI.setPlaceholders(player, whoMessage);
				 TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', placeholderMessage));
				 if(whoMessage.contains("Points")){
					 message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/is values"));
				 }
				 player.spigot().sendMessage(message);

			 }
			 roleOnline.clear();
			 roleOffline.clear();


		 }else{
		 	player.sendMessage(Main.getInstance().colorMessage("&d&lRift&5&lMC &8>> &dYou don't have a island"));
		 }
	 }

	 public static void showTargetWhoMessage(Player player, String name){
		 if(SuperiorSkyblockAPI.getPlayer(name) != null && SuperiorSkyblockAPI.getPlayer(name).getIsland() != null) {
			 Island island = SuperiorSkyblockAPI.getPlayer(name).getIsland();
			 String placeholderMessage = "";
			 int amountOnline = 0;
			 int amountOffline = 0;
			 TreeSet<String> roleOnline = new TreeSet<String>();
			 TreeSet<String> roleOffline = new TreeSet<String>();
			 int islandLevel = 0;
			 for (SuperiorPlayer uuid : island.getIslandMembers(true)) {
				 Player sPlayer = Bukkit.getPlayer(uuid.getUniqueId());
				 islandLevel = getLevel(uuid.getIslandLeader().getUniqueId());
				 if(sPlayer != null) {
					 if(!isVanished(sPlayer)){
						 amountOnline++;
						 roleOnline.add(Main.getInstance().colorMessage("&a(" + uuid.getPlayerRole() +")" + " " + uuid.getName()));
					 }
				 }else {
					 amountOffline++;
					 roleOffline.add(Main.getInstance().colorMessage("&c(" + uuid.getPlayerRole() +")" + " " + uuid.getName()));
				 }

			 }

			 List<String> fullMessage = new ArrayList<>();
			 ComponentBuilder cp = new ComponentBuilder("");
			 for(String whoMessage : Main.getInstance().getConfig().getStringList("WhoMessage")) {
				 whoMessage = whoMessage.replace("%amount_online%", String.valueOf(amountOnline));
				 whoMessage = whoMessage.replace("%amount_offline%", String.valueOf(amountOffline));
				 whoMessage = whoMessage.replace("%island_level%", String.valueOf(islandLevel));
				 whoMessage = whoMessage.replace("{0}", roleOnline.toString());
				 whoMessage = whoMessage.replace("{1}", roleOffline.toString());
				 whoMessage = whoMessage.replace("[", "");
				 whoMessage = whoMessage.replace("]", "");
				 placeholderMessage = PlaceholderAPI.setPlaceholders(SuperiorSkyblockAPI.getPlayer(name).asOfflinePlayer(), whoMessage);
				 TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', placeholderMessage));
				 if(whoMessage.contains("Points")){
					 message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/is values " + island.getName()));
				 }
				 player.spigot().sendMessage(message);
			 }
			 roleOnline.clear();
			 roleOffline.clear();

		 }else {
			 if(SuperiorSkyblockAPI.getGrid()!=null && SuperiorSkyblockAPI.getGrid().getIsland(name) != null) {
				 Island island = SuperiorSkyblockAPI.getGrid().getIsland(name);
				 String placeholderMessage = "";
				 int amountOnline = 0;
				 int amountOffline = 0;
				 int islandLevel = 0;
				 TreeSet<String> roleOnline = new TreeSet<String>();
				 TreeSet<String> roleOffline = new TreeSet<String>();
				 for (SuperiorPlayer uuid : island.getIslandMembers(true)) {
					 Player sPlayer = Bukkit.getPlayer(uuid.getUniqueId());
					 islandLevel = getLevel(uuid.getIslandLeader().getUniqueId());
					 if(sPlayer != null) {
						 if(!isVanished(sPlayer)){
							 amountOnline++;
							 roleOnline.add(Main.getInstance().colorMessage("&a(" + uuid.getPlayerRole() +")" + " " + uuid.getName()));
						 }
					 }else {
						 amountOffline++;
						 roleOffline.add(Main.getInstance().colorMessage("&c(" + uuid.getPlayerRole() +")" + " " + uuid.getName()));
					 }

				 }

				 List<String> fullMessage = new ArrayList<>();

				 for(String whoMessage : Main.getInstance().getConfig().getStringList("WhoMessage")) {
					 whoMessage = whoMessage.replace("%amount_online%", String.valueOf(amountOnline));
					 whoMessage = whoMessage.replace("%amount_offline%", String.valueOf(amountOffline));
					 whoMessage = whoMessage.replace("%island_level%", String.valueOf(islandLevel));
					 whoMessage = whoMessage.replace("{0}", roleOnline.toString());
					 whoMessage = whoMessage.replace("{1}", roleOffline.toString());
					 whoMessage = whoMessage.replace("[", "");
					 whoMessage = whoMessage.replace("]", "");
					 placeholderMessage = PlaceholderAPI.setPlaceholders(island.getOwner().asOfflinePlayer(), whoMessage);
					 TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', placeholderMessage));
					 if(whoMessage.contains("Points")){
						 message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/is values " + island.getName()));
					 }
					 player.spigot().sendMessage(message);

				 }
				 roleOnline.clear();
				 roleOffline.clear();

			 }else{
				 player.sendMessage(Main.getInstance().colorMessage("&d&lRift&5&lMC &8>> &dThat island doesn't exist"));
			 }
		 }
	 }

	private static boolean isVanished(Player player) {
		for (MetadataValue meta : player.getMetadata("vanished")) {
			if (meta.asBoolean()) return true;
		}
		return false;
	}

	 public static int getLevel(UUID uuid){
		Rank rank = new Rank(uuid);
		return rank.getRank();
	 }

}
