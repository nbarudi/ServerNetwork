package ca.bungo.core.api.cAPI;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import ca.bungo.core.core.Core;
import ca.bungo.core.util.RankUtilities;
import net.md_5.bungee.api.ChatColor;

public class ConfigApi extends CoreAPIAbstract {
	
	
	/**
	 * Configs have the following format:
	 * 
	 * Players:
	 * 	PlayerUUID:
	 * 		username: PlayerUsername
	 * 		pid: PlayerID
	 * 		etc....
	 * */
	
	private YamlConfiguration cfg;
	
	public class ConfigPlayerInfo extends PlayerInfo {

		public ConfigPlayerInfo(Player player) {
			super(player);
			cfg = core.fm.getConfig("playerdata.yml").get();
		}

		@Override
		public void increaseEXP(int xp) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void removeEXP(int xp) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setEXP(int xp) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void autoUpdater() {
			tasks.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(core, () ->{
				Player player = Bukkit.getPlayer(UUID.fromString(uuid));
				if(player == null)
					return;
				String name = player.getName();
				if(!nickname.isEmpty())
					name = nickname;
				
				if(!disguise.isEmpty()) {
					player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', RankUtilities.ranks.get("user") + " &e" + disguise));
				}else {
					player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', RankUtilities.ranks.get(rank) + " &e" + name));
				}
				
			}, 20, 20));
		}
		
	}
	

	public ConfigApi(Core core) {
		super(core);
	}

	@Override
	public PlayerInfo createPlayerInfo(Player player) {
		if(getPlayerInfo(player) != null)
			return getPlayerInfo(player);
		PlayerInfo info = new ConfigPlayerInfo(player);
		
		if(!info.exists)
			return null;
		
		return info;
	}

	@Override
	public boolean playerExists(Player player) {
		return (cfg != null && cfg.getConfigurationSection("Players") != null && cfg.getConfigurationSection("Players." + player.getUniqueId().toString()) != null);
	}

	@Override
	public boolean playerExists(String username) {
		for(String key : cfg.getConfigurationSection("Players").getKeys(false)) {
			if(cfg.getString("Players." + key + ".username") == username)
				return true;
		}
		return false;
	}

	@Override
	public int getPID(String username) {
		if(!playerExists(username))
			return 0;
		for(String key : cfg.getConfigurationSection("Players").getKeys(false)) {
			if(cfg.getString("Players." + key + ".username") == username)
				return cfg.getInt("Players." + key + ".pid");
		}
		return 0;
	}

	@Override
	public String getOfflineUUID(String username) {
		for(String key : cfg.getConfigurationSection("Players").getKeys(false)) {
			if(cfg.getString("Players." + key + ".username") == username)
				return key;
		}
		return null;
	}

	@Override
	public boolean createPlayer(Player player) {
		String uuid = player.getUniqueId().toString();
		
		if(!playerExists(player)) {
			ConfigurationSection sec = cfg.createSection("Players." + uuid);
			sec.set("username", player.getName());
			sec.set("pid", 69420); //I don't plan on using the PID system for Config Player Info since that was more for the mysql database
			core.fm.saveConfig("playerdata.yml");
		}else {
			PlayerInfo info = createPlayerInfo(player);
			if(info != null) {
				core.pInfo.add(info);
			}
		}
		return true;
	}

	@Override
	public void savePlayer(Player player) {
		// TODO Auto-generated method stub
	}

	@Override
	public PlayerInfo getPlayerInfo(Player player) {
		PlayerInfo pinf = null;
		for(PlayerInfo info : core.pInfo) {
			if(player.getUniqueId().toString().equals(info.uuid)) {
				pinf = info;
			}
		}
		return pinf;
	}

	@Override
	public void removePlayerInfo(Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean checkMute(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mutePlayer(Player player, long endTime, String muter) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unmutePlayer(Player target) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkBan(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String checkBan(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean banPlayer(String username, long time, boolean global, String reason) {
		// TODO Auto-generated method stub
		return false;
	}

}
