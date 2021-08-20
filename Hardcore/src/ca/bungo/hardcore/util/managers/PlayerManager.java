package ca.bungo.hardcore.util.managers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import ca.bungo.hardcore.hardcore.Hardcore;
import ca.bungo.hardcore.util.player.PlayerData;

public class PlayerManager {
	
	Hardcore hardcore;
	
	public ArrayList<PlayerData> playerData = new ArrayList<PlayerData>();
	
	public HashMap<String, Inventory> playerOpened = new HashMap<String, Inventory>();
	
	public PlayerManager(Hardcore hardcore) {
		this.hardcore = hardcore;
	}
	
	public PlayerData createPlayerData(Player player) {
		
		if(getPlayerData(player) != null)
			return getPlayerData(player);
		
		ConfigurationSection cfgSec = hardcore.getConfig().getConfigurationSection("Players." + player.getUniqueId().toString());
		String uuid = player.getUniqueId().toString();
		
		if(cfgSec == null) {
			cfgSec = hardcore.getConfig().createSection("Players." + uuid);
			cfgSec.set("username", player.getName());
			cfgSec.set("lives", 3);
			cfgSec.set("fullDeaths", 0);
			cfgSec.set("spentPoints" , 0);
			cfgSec.set("reviveTime", 0);
			cfgSec.set("perks", new ArrayList<String>());
			hardcore.saveConfig();
		}
		
		PlayerData data = new PlayerData(hardcore, player.getName(), uuid);
		playerData.add(data);
		return data;
	}
	
	public void savePlayerData(PlayerData data) {
		String uuid = data.uuid;
		ConfigurationSection cfgSec = hardcore.getConfig().getConfigurationSection("Players." + uuid);
		cfgSec.set("lives", data.lives);
		cfgSec.set("spentPoints" , data.maxSkillPoints - data.skillPoints);
		cfgSec.set("reviveTime", data.reviveTime);
		cfgSec.set("perks", data.ownedPerks);
		hardcore.saveConfig();
	}
	
	public void removePlayerData(PlayerData data) {
		savePlayerData(data);
		playerData.remove(data);
	}
	
	public PlayerData getPlayerData(Player player) {
		PlayerData pData = null;
		
		for(PlayerData data : playerData) {
			if(data.uuid.equals(player.getUniqueId().toString()))
				pData = data;
		}
		
		return pData;
	}
	
	public void revivePlayer(String username) {
		ConfigurationSection cfgSec = hardcore.getConfig().getConfigurationSection("Players");
		for(String key : cfgSec.getKeys(false)) {
			if(cfgSec.getString(key + ".username").equalsIgnoreCase(username)) {
				cfgSec.set(key + ".reviveTime", new Date().getTime());
				hardcore.saveConfig();
				hardcore.reloadConfig();
				return;
			}
		}
	}
	
	
	

}
