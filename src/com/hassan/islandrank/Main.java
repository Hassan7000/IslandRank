package com.hassan.islandrank;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.hassan.islandrank.commandhook.IslandPoints;
import com.hassan.islandrank.commandhook.IslandWho;
import com.hassan.islandrank.placeholder.IslandLevelPlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.hassan.islandrank.cleaner.Cleaner;
import com.hassan.islandrank.commandhook.IsLevelHook;
import com.hassan.islandrank.events.GuiClickEvent;
import com.hassan.islandrank.events.IslandCommand;
import com.hassan.islandrank.events.IslandEvents;
import com.hassan.islandrank.gui.Gui;
import com.hassan.islandrank.hologramplaceholders.HologramPlaceholder;
import com.hassan.islandrank.hologramplaceholders.HologramPlaceholderEight;
import com.hassan.islandrank.hologramplaceholders.HologramPlaceholderFive;
import com.hassan.islandrank.hologramplaceholders.HologramPlaceholderFour;
import com.hassan.islandrank.hologramplaceholders.HologramPlaceholderNine;
import com.hassan.islandrank.hologramplaceholders.HologramPlaceholderSeven;
import com.hassan.islandrank.hologramplaceholders.HologramPlaceholderSix;
import com.hassan.islandrank.hologramplaceholders.HologramPlaceholderTen;
import com.hassan.islandrank.hologramplaceholders.HologramPlaceholderThree;
import com.hassan.islandrank.hologramplaceholders.HologramPlaceholderTwo;
import com.hassan.islandrank.islandtop.CalculateTop;
import com.hassan.islandrank.placeholder.RankPlaceholder;

import net.milkbowl.vault.economy.Economy;




public class Main extends JavaPlugin {
	
	public HashMap<UUID, Integer> rankMap = new HashMap<>();
	private static Main instance;
	private File dfile;
	private FileConfiguration data;
	public Economy econ = null;
	
	public HashMap<Integer, UUID> topMap = new HashMap<>();
	public Gui gui = new Gui();
	private CalculateTop top;
	public void loadConfig() {
		this.getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		this.setup(this);
		this.getData();
		this.saveData();
		
	}
		
	
	public void onEnable() {
		instance = this;
		loadConfig();
		this.setupEconomy();
		Bukkit.getPluginManager().registerEvents(new IslandEvents(), this);
		Bukkit.getPluginManager().registerEvents(new IslandCommand(), this);
		Bukkit.getPluginManager().registerEvents(new GuiClickEvent(), this);
		SuperiorSkyblockAPI.registerCommand(new IslandWho());
		SuperiorSkyblockAPI.registerCommand(new IslandPoints());
		this.loadRanks();
		Cleaner.init().registerClass(IsLevelHook.class);
		if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new RankPlaceholder().register();
		//	new IslandLevelPlaceholder(this).register();
      }
		this.registerHologramPlaceholders();
	}
	
	
	public void onDisable(Object objec) {
		this.getConfig().options().copyDefaults(false);
		this.saveRanks();
		Cleaner.init().clean();
	}
	

	private boolean setupEconomy() {
		if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
			return false;
		} else {
			RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager()
					.getRegistration(Economy.class);
			if (rsp == null) {
				return false;
			} else {
				econ = (Economy) rsp.getProvider();
				return econ != null;
			}
		}
	}
	
	public static Main getInstance() {
        return instance;
    }
	
	public void setup(Plugin p) {
		this.dfile = new File(p.getDataFolder(), "IslandRanks.yml");

		if (!this.dfile.exists()) {
			try {
				this.dfile.createNewFile();
			} catch (IOException e) {
				Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not create IslandRanks.yml!");
			}
		}
		this.data = YamlConfiguration.loadConfiguration(this.dfile);

	}

	public FileConfiguration getData() {
		return this.data;
	}

	public void saveData() {
		try {
			this.data.save(this.dfile);
		} catch (IOException e) {
			Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save IslandRanks.yml!");
		}
	}

	public void reloadData() {
		this.data = YamlConfiguration.loadConfiguration(this.dfile);
	}
	
	
	
	private void  saveRanks() {
		for(Map.Entry<UUID, Integer> entry : Main.getInstance().rankMap.entrySet()){
			if(entry != null) {
				Main.getInstance().getData().set("IslandRanks." + entry.getKey().toString() + ".Level", String.valueOf(entry.getValue()));
				Main.getInstance().saveData();
			}
		}
	}
	
	private void loadRanks() {
		if(Main.getInstance().getData().contains("IslandRanks")) {
			for(String ranks : Main.getInstance().getData().getConfigurationSection("IslandRanks").getKeys(false)) {
				String rank = Main.getInstance().getData().getString("IslandRanks." + ranks + ".Level");
				if(!(rank.equalsIgnoreCase("0"))) {
					Main.getInstance().rankMap.put(UUID.fromString(ranks), Integer.valueOf(Main.getInstance().getData().getString("IslandRanks." + ranks + ".Level")));
				}
			}
		}
	}
	
	public String colorMessage(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	public Location LocFromString(String string) {
		String[] loc = string.split(":");
		return new Location(Bukkit.getWorld(loc[0]), Double.parseDouble(loc[1]), Double.parseDouble(loc[2]),
				Double.parseDouble(loc[3]), (float) Double.parseDouble(loc[4]), (float) Double.parseDouble(loc[5]));
	}

	public String SringFromLoc(Location loc) {
		return loc.getWorld().getName() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":" + loc.getYaw() + ":" + loc.getPitch();
	}
	
	
	private void registerHologramPlaceholders() {
		HologramsAPI.registerPlaceholder(Main.instance, "%IslandTop_1%", 32.0, new HologramPlaceholder());
		HologramsAPI.registerPlaceholder(Main.instance, "%IslandTop_2%", 32.0, new HologramPlaceholderTwo());
		HologramsAPI.registerPlaceholder(Main.instance, "%IslandTop_3%", 32.0, new HologramPlaceholderThree());
		HologramsAPI.registerPlaceholder(Main.instance, "%IslandTop_4%", 32.0, new HologramPlaceholderFour());
		HologramsAPI.registerPlaceholder(Main.instance, "%IslandTop_5%", 32.0, new HologramPlaceholderFive());
		HologramsAPI.registerPlaceholder(Main.instance, "%IslandTop_6%", 32.0, new HologramPlaceholderSix());
		HologramsAPI.registerPlaceholder(Main.instance, "%IslandTop_7%", 32.0, new HologramPlaceholderSeven());
		HologramsAPI.registerPlaceholder(Main.instance, "%IslandTop_8%", 32.0, new HologramPlaceholderEight());
		HologramsAPI.registerPlaceholder(Main.instance, "%IslandTop_9%", 32.0, new HologramPlaceholderNine());
		HologramsAPI.registerPlaceholder(Main.instance, "%IslandTop_10%", 32.0, new HologramPlaceholderTen());
	}

	public String formatNumbers(Double number){
		DecimalFormat dformater = new DecimalFormat("###,###,###,###.###");

		String formated = dformater.format(number);



		return formated;

	}
	

}
