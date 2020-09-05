package com.hassan.islandrank.events;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.google.common.collect.Sets;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.hassan.islandrank.Main;
import com.hassan.islandrank.ranks.Rank;

import me.clip.placeholderapi.PlaceholderAPI;
import me.realized.tokenmanager.api.TokenManager;



public class GuiClickEvent implements Listener {
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		String guiName = ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("IslandRanksGUIName"));
		Inventory inv = event.getInventory();
		if(inv.getName().equalsIgnoreCase(guiName)) {
			
			if(event.getClickedInventory() == null) return;
			event.setCancelled(true);
			List<String> members = new ArrayList<>();
			Player player = (Player) event.getWhoClicked();
			Rank rank = new Rank(SuperiorSkyblockAPI.getPlayer(player).getIsland().getOwner().getUniqueId());
				if(inv.getItem(event.getSlot()) != null && inv.getItem(event.getSlot()).getType() != Material.AIR) {
					if(inv.getItem(event.getSlot()).getData().getData() != (byte) Main.getInstance().getConfig().getInt("GuiMaterials.Bought.Data")) {
						if(Integer.valueOf(rank.getRank()) + 1 == event.getSlot() + 1) {
							int rankNumber = Integer.valueOf(rank.getRank()) + 1;
							double price = Main.getInstance().getConfig().getDouble("IslandRanks." + String.valueOf(rankNumber) + ".Price");
							int tokenPrice = Main.getInstance().getConfig().getInt("IslandRanks." + String.valueOf(rankNumber) + ".Token-Price");
							TokenManager tokenManager = (TokenManager) Bukkit.getPluginManager().getPlugin("TokenManager");
							double balance = Main.getInstance().econ.getBalance(player);
							int tokenBalance = (int) tokenManager.getTokens(player).getAsLong();
							if(balance >= price && tokenBalance >= tokenPrice) {
								for(SuperiorPlayer uuid : SuperiorSkyblockAPI.getPlayer(player).getIsland().getIslandMembers(true)) {
									OfflinePlayer member = uuid.asOfflinePlayer();
									members.add(member.getName());
									
								}
								for(String command : Main.getInstance().getConfig().getStringList("IslandRanks." + String.valueOf(rankNumber) + ".Commands")) {
									if(command.contains("{player}")) {
										for(String member : members) {
											Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", member));
										}
									}else {
										if(command.contains("{sender}")) {
											Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{sender}", player.getName()));
										}
									}
									
								}
								
								
								rank.setRank(rankNumber);
								
								Main.getInstance().econ.withdrawPlayer(player, price);
								tokenManager.removeTokens(player, tokenPrice);
								
								String buyMessage = Main.getInstance().getConfig().getString("Messages.BoughtLevel");
								buyMessage = buyMessage.replace("{level}", String.valueOf(rankNumber));
								buyMessage = buyMessage.replace("{price}", Main.getInstance().formatNumbers(price));
								player.sendMessage(Main.getInstance().colorMessage(buyMessage));
								
								String rankupMessage = Main.getInstance().getConfig().getString("Messages.RankupMessage");
								rankupMessage = rankupMessage.replace("{owner}", SuperiorSkyblockAPI.getPlayer(player).getIsland().getOwner().getName());
								rankupMessage = rankupMessage.replace("{level}", String.valueOf(rankNumber));
								String withPlaceholders = PlaceholderAPI.setPlaceholders(SuperiorSkyblockAPI.getPlayer(player).asOfflinePlayer(), rankupMessage);
								Bukkit.broadcastMessage(Main.getInstance().colorMessage(withPlaceholders));
								
								player.closeInventory();
								Main.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
									  public void run() {
										  Main.getInstance().gui.createGUI(player);
									  }
									}, 3L);
								
							}else {
								player.sendMessage(Main.getInstance().colorMessage(Main.getInstance().getConfig().getString("Messages.NotEnoughMoney")));
							}
						}else {
							player.sendMessage(Main.getInstance().colorMessage(Main.getInstance().getConfig().getString("Messages.BuyRanksInOrder")));
						}
					}else {
						player.sendMessage(Main.getInstance().colorMessage(Main.getInstance().getConfig().getString("Messages.AlreadyThatRank")));
					}
				}
		}
	}

}
