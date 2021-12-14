package ca.bungo.core.api.cAPI;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import ca.bungo.core.core.Core;
import ca.bungo.core.events.CustomEvents.PlayerLevelUpEvent;
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
			this.exists = true;
			
			ConfigurationSection sec = cfg.getConfigurationSection("Players." + uuid);
			
			this.nickname = sec.getString("nickname");
			this.disguise = sec.getString("disguise");
			this.level = sec.getInt("level");
			this.exp = sec.getInt("exp");
			this.rank = sec.getString("rank");
			this.pid = sec.getInt("pid");
			
			if(this.nickname == null)
				this.nickname = "";
			if(this.disguise == null)
				this.disguise = "";
			if(this.level == 0)
				this.level = 1;
			if(this.rank == null)
				this.rank = "user";
					
			autoUpdater();
		}

		@Override
		public void increaseEXP(int xp) {
			int reqXP = (level * 250) + 1000;
			
			this.exp += xp;
			
			if(exp >= reqXP) {
				PlayerLevelUpEvent event = new PlayerLevelUpEvent(Bukkit.getPlayer(UUID.fromString(uuid)), this.level, this.level+1);
				Bukkit.getServer().getPluginManager().callEvent(event);
				if(event.isCancelled())
					return;
				this.level++;
				this.exp -= reqXP;
				increaseEXP(0);
			}
		}

		@Override
		public void removeEXP(int xp) {
			int endingXP = this.exp - xp;
			if(endingXP < 0) {
				this.exp = ((level-1)*250 + 1000) + endingXP;
				this.level--;
			}else {
				this.exp = endingXP;
			}
		}

		@Override
		public void setEXP(int xp) {
			int reqXP = (level * 250) + 1000;
			this.exp = xp;
			
			if(exp >= reqXP) {
				PlayerLevelUpEvent event = new PlayerLevelUpEvent(Bukkit.getPlayer(UUID.fromString(uuid)), this.level, this.level+1);
				Bukkit.getServer().getPluginManager().callEvent(event);
				if(event.isCancelled())
					return;
				this.level++;
				this.exp -= reqXP;
				increaseEXP(0);
			}
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
		cfg = core.fm.getConfig("playerdata.yml").get();
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
			PlayerInfo info = createPlayerInfo(player);
			core.pInfo.add(info);
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
		if(!playerExists(player))
			return;
		
		ConfigurationSection sec = cfg.getConfigurationSection("Players." + player.getUniqueId().toString());
		
		PlayerInfo info = getPlayerInfo(player);
		
		/*stmt.setString(1, player.getName());
		stmt.setInt(2, info.exp);
		stmt.setInt(3, info.level);
		stmt.setString(4, info.rank);
		stmt.setString(5, info.nickname);
		stmt.setString(6, info.disguise);
		stmt.setInt(7, info.pid);*/
		sec.set("username", info.username);
		sec.set("exp", info.exp);
		sec.set("level", info.level);
		sec.set("rank", info.rank);
		sec.set("nickname", info.nickname);
		sec.set("disguise", info.disguise);
		core.fm.saveConfig("playerdata.yml");
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
		PlayerInfo info = getPlayerInfo(player);
		for(int task : info.tasks) {
			Bukkit.getScheduler().cancelTask(task);
		}
		savePlayer(player);
		core.pInfo.remove(info);
	}

	@Override
	public boolean checkMute(Player player) {
		
		if(!playerExists(player))
			return false;
		
		ConfigurationSection sec = cfg.getConfigurationSection("Players." + player.getUniqueId().toString());
		
		if(sec.getConfigurationSection("mutes") == null)
			return false;
		
		for(String key : sec.getConfigurationSection("mutes").getKeys(false)) {
			if(sec.getBoolean("mutes." + key + ".unmuted"))
				continue;
			long endTime = sec.getLong("mutes." + key + ".endtime");
			Date date = new Date();
			if(endTime <= date.getTime() || endTime == 0) {
				sec.set("mutes." + key + ".unmuted", true);
				core.fm.saveConfig("playerdata.yml");
				continue;
			}
			else
				return true;
		}
		
		return false;
	}
	
	private int highestIDValue(String uuid, String section) {
		int max = 0;
		for(String key : cfg.getConfigurationSection("Players." + uuid + "." + section).getKeys(false)) {
				if(Integer.parseInt(key) > max)
					max = Integer.parseInt(key);
		}
		return max;
	}

	@Override
	public boolean mutePlayer(Player player, long endTime, String muter) {
		if(!playerExists(player))
			return false;
		
		ConfigurationSection sec = cfg.getConfigurationSection("Players." + player.getUniqueId().toString());
		
		if(sec.getConfigurationSection("mutes") == null)
			sec.createSection("mutes");
		
		int lastMuteID = highestIDValue(player.getUniqueId().toString(), "mutes");
		
		sec.set("mutes." + (lastMuteID+1) + ".endtime", endTime);
		sec.set("mutes." + (lastMuteID+1) + ".unmuted", false);
		sec.set("mutes." + (lastMuteID+1) + ".whomuted", muter);
		
		core.fm.saveConfig("playerdata.yml");
		
		return true;
	}

	@Override
	public boolean unmutePlayer(Player player) {
		if(!playerExists(player))
			return false;
		
		ConfigurationSection sec = cfg.getConfigurationSection("Players." + player.getUniqueId().toString());
		
		if(sec.getConfigurationSection("mutes") == null)
			return true;
		
		for(String key : sec.getConfigurationSection("mutes").getKeys(false)) 
			sec.set("mutes." + key + ".unmuted", true);

		core.fm.saveConfig("playerdata.yml");
		return true;
	}

	@Override
	public boolean checkBan(Player player) {
		if(!playerExists(player))
			return false;
		ConfigurationSection sec = cfg.getConfigurationSection("Players." + player.getUniqueId().toString());
		if(sec.getConfigurationSection("bans") == null)
			return false;
		
		Date cDate = new Date();
		//Core Difference from this to the MySql version: Server ID is not needed since this is local to this server.
		for(String key : sec.getConfigurationSection("bans").getKeys(false)) {
			if(sec.getBoolean(key + ".unbanned")) //Checking if unbanned
				continue;
			long endTime = sec.getLong(key + ".endTime");
			String bReason = sec.getString(key + ".reason");
			if(endTime == 0) {
				StringBuilder message = new StringBuilder();
				message.append("&7[&bBungo &6Networks&7]\n");
				message.append("&4You have been banned from this server!\n");
				message.append("&7Reason:\n");
				message.append(bReason + "\n");
				message.append("&9Your ban will end on:\n");
				message.append("&cNever!\n");
				message.append("&8You are free to submit a ban appeal at: xxxxxxx\n");
				player.kickPlayer(ChatColor.translateAlternateColorCodes('&', message.toString()));
				return true;
			}else if(cDate.getTime() > endTime) {
				sec.set(key + ".unbanned", true); //Set them to unbanned!
				return false;
			}else {
				StringBuilder message = new StringBuilder();
				message.append("&7[&bBungo &6Networks&7]\n");
				message.append("&4You have been banned from this server!\n");
				message.append("&7Reason:\n");
				message.append(bReason + "\n");
				message.append("&9Your ban will end on:\n");
				Date endDate = new Date(endTime);
				
				//Calendar cal = Calendar.getInstance();
				//cal.setTime(endDate);
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy | hh:mm:ss");
				message.append("&3" + sdf.format(endDate) + "\n");
				message.append("&8You are free to submit a ban appeal at: xxxxxxx\n");
				player.kickPlayer(ChatColor.translateAlternateColorCodes('&', message.toString()));
				return true;
			}
		}
		return false;
	}

	@Override
	public String checkBan(String uuid) {
		ConfigurationSection sec = cfg.getConfigurationSection("Players." + uuid);
		if(sec == null)
			sec = cfg.createSection("Players");
		if(sec.getConfigurationSection("bans") == null)
			return null;
		
		Date cDate = new Date();
		//Core Difference from this to the MySql version: Server ID is not needed since this is local to this server.
		for(String key : sec.getConfigurationSection("bans").getKeys(false)) {
			if(sec.getBoolean(key + ".unbanned")) //Checking if unbanned
				continue;
			long endTime = sec.getLong(key + ".endTime");
			String bReason = sec.getString(key + ".reason");
			if(endTime == 0) {
				StringBuilder message = new StringBuilder();
				message.append("&7[&bBungo &6Networks&7]\n");
				message.append("&4You have been banned from this server!\n");
				message.append("&7Reason:\n");
				message.append(bReason + "\n");
				message.append("&9Your ban will end on:\n");
				message.append("&cNever!\n");
				message.append("&8You are free to submit a ban appeal at: xxxxxxx\n");
				//player.kickPlayer(ChatColor.translateAlternateColorCodes('&', message.toString()));
				return message.toString();
			}else if(cDate.getTime() > endTime) {
				sec.set(key + ".unbanned", true); //Set them to unbanned!
				return null;
			}else {
				StringBuilder message = new StringBuilder();
				message.append("&7[&bBungo &6Networks&7]\n");
				message.append("&4You have been banned from this server!\n");
				message.append("&7Reason:\n");
				message.append(bReason + "\n");
				message.append("&9Your ban will end on:\n");
				Date endDate = new Date(endTime);
				
				//Calendar cal = Calendar.getInstance();
				//cal.setTime(endDate);
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy | hh:mm:ss");
				message.append("&3" + sdf.format(endDate) + "\n");
				message.append("&8You are free to submit a ban appeal at: xxxxxxx\n");
				//player.kickPlayer(ChatColor.translateAlternateColorCodes('&', message.toString()));
				return message.toString();
			}
		}
		return null;
	}

	@Override
	public boolean banPlayer(String username, long time, boolean global, String reason) {
		String uuid = getOfflineUUID(username);
		ConfigurationSection sec = cfg.getConfigurationSection("Players." + uuid);
		
		int lastBanID = highestIDValue(getOfflineUUID(username), "bans");
		
		sec.set("bans." + (lastBanID+1) + ".endTime", time);
		sec.set("bans." + (lastBanID+1) + ".reason", reason);
		sec.set("bans." + (lastBanID+1) + ".unbanned", false);
		
		core.fm.saveConfig("playerdata.yml");
		
		if(Bukkit.getPlayer(username) != null) {
			checkBan(Bukkit.getPlayer(username)); //No point in re creating the above ban results wehn I can use the function in its place!
					//It didnt make sense to do this for the MySQL version because that would require
					// Me to query the server more times then needed.
					// Since this is file based there is no latency in just using the function.
		}
		
		return false;
	}

}
