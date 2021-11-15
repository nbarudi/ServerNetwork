package ca.bungo.core.api.cAPI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import ca.bungo.core.core.Core;
import ca.bungo.core.events.CustomEvents.PlayerLevelUpEvent;
import ca.bungo.core.util.RankUtilities;
import net.md_5.bungee.api.ChatColor;

public class MySQLApi extends CoreAPIAbstract {
	
	/*
	 * SQL arrays start at 1 not 0
	 * 
	 * Types of Tables
	 * 	- PlayerInfo table
	 * 		- Player Experience
	 * 		- Player Level
	 * 		- Player Rank
	 * - Leaderboard Table
	 * 		- Mob Kill Count
	 * 		- Player Level
	 * 		- Player Kill Count
	 * 		- Player Deaths
	 * */
	
	
	public class MySQLPlayerInfo extends PlayerInfo {
		
		public MySQLPlayerInfo(Player player) {
			super(player);
			if(playerExists(player)) {
				try (Connection conn = core.source.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM corePlayerInfo WHERE uuid = ?")){
					stmt.setString(1, player.getUniqueId().toString());
					ResultSet results = stmt.executeQuery();
					
					if(!results.next()) {
						this.exists = false;
						return;
					}else {
						pid = results.getInt(1);
						uuid = results.getString(2);
						username = results.getString(3);
						exp = results.getInt(4);
						level = results.getInt(5);
						rank = results.getString(6);
						nickname = results.getString(7);
						disguise = results.getString(8);
						
						this.exists = true;
					}
					autoUpdater();
					return;
				} catch (SQLException e) {
					e.printStackTrace();
					core.logConsole("&4Database Connection Failed!");
					this.exists = false;
					return;
				}
			}else {
				System.out.println("I don't know how you did it.. but you broke something");
			}
		}
		
		
		/**
		 * Increase the players EXP
		 * If the player reaches the required EXP they will increase 1 level
		 * It is recommended to save the players information after increasing their EXP
		 * */
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
		
		public void removeEXP(int xp) {
			int endingXP = this.exp - xp;
			if(endingXP < 0) {
				this.exp = ((level-1)*250 + 1000) + endingXP;
				this.level--;
			}else {
				this.exp = endingXP;
			}
		}
		
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

	
	public MySQLApi(Core core) {
		super(core);
	}
	
	public PlayerInfo createPlayerInfo(Player player) {
		
		if(getPlayerInfo(player) != null)
			return getPlayerInfo(player);
		PlayerInfo info = new MySQLPlayerInfo(player);
		
		if(!info.exists)
			return null;
		
		return info;
	}
	
	public boolean playerExists(Player player) {
		String uuid = player.getUniqueId().toString();
		try (Connection conn = core.source.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT pid FROM corePlayerInfo WHERE uuid = ?")){
			stmt.setString(1, uuid);
			ResultSet results = stmt.executeQuery();
			if(results.next()) {
				return true;
			}else {
				return false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			core.logConsole("&4Database Connection Failed!");
			return false;
		}
	}
	
	public boolean playerExists(String username) {
		//String uuid = player.getUniqueId().toString();
		try (Connection conn = core.source.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT pid FROM corePlayerInfo WHERE username = ?")){
			stmt.setString(1, username);
			ResultSet results = stmt.executeQuery();
			if(results.next()) {
				return true;
			}else {
				return false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			core.logConsole("&4Database Connection Failed!");
			return false;
		}
	}
	
	public int getPID(String username) {
		if(!playerExists(username))
			return 0;
		try (Connection conn = core.source.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT pid FROM corePlayerInfo WHERE username = ?")){
			stmt.setString(1, username);
			ResultSet results = stmt.executeQuery();
			if(results.next())
				return results.getInt(1);
			return 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
			core.logConsole("&4Database Connection Failed!");
			return 0;
		}
	}
	
	public String getOfflineUUID(String username) {
		int pID = getPID(username);
		if(pID == 0)
			return null;
		
		try (Connection conn = core.source.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT uuid FROM corePlayerInfo WHERE pid = ?")){
			stmt.setInt(1, pID);
			ResultSet results = stmt.executeQuery();
			if(!results.next())
				return null;
			return results.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
			core.logConsole("&4Database Connection Failed!");
			return null;
		}
	}
	
	public boolean createPlayer(Player player) {
		String uuid = player.getUniqueId().toString();
		
		if(!playerExists(player)) {
			try (Connection conn = core.source.getConnection(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO corePlayerInfo(uuid, username) VALUES(?, ?)")){
				stmt.setString(1, uuid);
				stmt.setString(2, player.getName());
				stmt.execute();
				PlayerInfo info = createPlayerInfo(player);
				if(info != null) {
					core.pInfo.add(info);
				}
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				core.logConsole("&4Database Connection Failed!");
				return false;
			}
		}else {
			PlayerInfo info = createPlayerInfo(player);
			if(info != null) {
				core.pInfo.add(info);
			}
		}
		
		return true;
	}
	
	public void savePlayer(Player player) {
		PlayerInfo info = getPlayerInfo(player);
		
		core.logConsole("&3Saving Data for Player: &b" + player.getName());
		
		try (Connection conn = core.source.getConnection(); PreparedStatement stmt = conn.prepareStatement("UPDATE corePlayerInfo SET username = ?, exp = ?, level = ?, rank = ?, nickname = ?, disguise = ? WHERE pid = ?")){
			stmt.setString(1, player.getName());
			stmt.setInt(2, info.exp);
			stmt.setInt(3, info.level);
			stmt.setString(4, info.rank);
			stmt.setString(5, info.nickname);
			stmt.setString(6, info.disguise);
			stmt.setInt(7, info.pid);
			
			stmt.execute();
		}catch (SQLException e) {
			core.logConsole("&4Database Connection Failed!");
			e.printStackTrace();
			return;
		}
	}

	
	public PlayerInfo getPlayerInfo(Player player) {
		PlayerInfo pinf = null;
		for(PlayerInfo info : core.pInfo) {
			if(player.getUniqueId().toString().equals(info.uuid)) {
				pinf = info;
			}
		}
		return pinf;
	}

	public void removePlayerInfo(Player player) {
		PlayerInfo info = getPlayerInfo(player);
		for(int task : info.tasks) {
			Bukkit.getScheduler().cancelTask(task);
		}
		savePlayer(player);
		core.pInfo.remove(info);
	}
	
	public boolean checkMute(Player player) {
		try(Connection conn = core.source.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM corePlayerMutes WHERE uuid = ? AND unmuted = 0")){
			stmt.setString(1, player.getUniqueId().toString());
			ResultSet result = stmt.executeQuery();
			if(!result.next())
				return false;
			
			long endTime = result.getLong(4);
			if(endTime == 0)
				return true;
			if(endTime <= new Date().getTime()) {
				PreparedStatement ps = conn.prepareStatement("UPDATE corePlayerMutes SET unmuted = 1 WHERE uuid = ?");
				ps.setString(1, player.getUniqueId().toString());
				ps.execute();
				ps.close();
				return false;
			}
			return true;
		} catch (SQLException e) {
			core.logConsole("&4Database Connection Failed!");
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean mutePlayer(Player player, long endTime, String muter) {
		try(Connection conn = core.source.getConnection(); 
				PreparedStatement stmt = conn.prepareStatement("INSERT INTO corePlayerMutes(uuid, username, whomuted, endtime) VALUES(?, ?, ?, ?)")){
			stmt.setString(1, player.getUniqueId().toString());
			stmt.setString(2, player.getName());
			stmt.setString(3, muter);
			stmt.setLong(4, endTime);
			
			stmt.execute();
			return true;
			
			
		} catch (SQLException e) {
			core.logConsole("&4Database Connection Failed!");
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean unmutePlayer(Player target) {
		try(Connection conn = core.source.getConnection();
				PreparedStatement stmt = conn.prepareStatement("UPDATE corePlayerMutes SET unmuted = 1 WHERE uuid = ?")){
			stmt.setString(1, target.getUniqueId().toString());
			stmt.execute();
			return true;
		} catch (SQLException e) {
			core.logConsole("&4Database Connection Failed!");
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean checkBan(Player player) {
		try(Connection conn = core.source.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT reason, isglobal, server, endtime FROM corePlayerBans WHERE uuid = ? AND unbanned = 0")){
			stmt.setString(1, player.getUniqueId().toString());
			ResultSet results = stmt.executeQuery();
			if(!results.next())
				return false;
			
			String bReason = results.getString(1);
			boolean isGlobal = results.getBoolean(2);
			String sID = results.getString(3);
			long endTime = results.getLong(4);
			
			if(!sID.equalsIgnoreCase(core.getConfig().getString("server-id")))
				return false;
			
			Date cDate = new Date();
			if(endTime == 0) {
				StringBuilder message = new StringBuilder();
				message.append("&7[&bBungo &6Networks&7]\n");
				message.append("&4You have been banned from this server " + (isGlobal ? "network" : "gamemode") + "!\n");
				message.append("&7Reason:\n");
				message.append(bReason + "\n");
				if(!isGlobal)
					message.append("&7You are free to join any other gamemode until your ban is up.\n");
				message.append("&9Your ban will end on:\n");
				Date endDate = new Date(endTime);
				
				//Calendar cal = Calendar.getInstance();
				//cal.setTime(endDate);
				if(endTime > 0) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy | hh:mm:ss");
					message.append("&3" + sdf.format(endDate) + "\n");
				}else {
					message.append("&cNever!\n");
				}
				message.append("&8You are free to submit a ban appeal at: xxxxxxx\n");
				player.kickPlayer(ChatColor.translateAlternateColorCodes('&', message.toString()));
				return true;
			}
			else if(cDate.getTime() > endTime) {
				PreparedStatement ps = conn.prepareStatement("UPDATE corePlayerBans SET unbanned = 1 WHERE uuid = ? AND server = ?");
				ps.setString(1, player.getUniqueId().toString());
				ps.setString(2, core.getConfig().getString("server-id"));
				ps.execute();
				ps.close();
				return false;
			}
			else {
				StringBuilder message = new StringBuilder();
				message.append("&7[&bBungo &6Networks&7]\n");
				message.append("&4You have been banned from this server " + (isGlobal ? "network" : "gamemode") + "!\n");
				message.append("&7Reason:\n");
				message.append(bReason + "\n");
				if(!isGlobal)
					message.append("&7You are free to join any other gamemode until your ban is up.\n");
				message.append("&9Your ban will end on:\n");
				Date endDate = new Date(endTime);
				
				//Calendar cal = Calendar.getInstance();
				//cal.setTime(endDate);
				if(endTime > 0) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy | hh:mm:ss");
					message.append("&3" + sdf.format(endDate) + "\n");
				}else {
					message.append("&cNever!\n");
				}
				message.append("&8You are free to submit a ban appeal at: xxxxxxx\n");
				player.kickPlayer(ChatColor.translateAlternateColorCodes('&', message.toString()));
				return true;
			}
		} catch (SQLException e) {
			core.logConsole("&4Database Connection Failed!");
			e.printStackTrace();
			return false;
		}
	}
	
	public String checkBan(String uuid) {
		try(Connection conn = core.source.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT reason, isglobal, server, endtime FROM corePlayerBans WHERE uuid = ? AND unbanned = 0")){
			stmt.setString(1, uuid);
			ResultSet results = stmt.executeQuery();
			if(!results.next())
				return null;
			
			String bReason = results.getString(1);
			boolean isGlobal = results.getBoolean(2);
			String sID = results.getString(3);
			long endTime = results.getLong(4);
			
			if(!sID.equalsIgnoreCase(core.getConfig().getString("server-id")))
				return null;
			
			Date cDate = new Date();
			if(endTime == 0) {
				StringBuilder message = new StringBuilder();
				message.append("&7[&bBungo &6Networks&7]\n");
				message.append("&4You have been banned from this server " + (isGlobal ? "network" : "gamemode") + "!\n");
				message.append("&7Reason:\n");
				message.append(bReason + "\n");
				if(!isGlobal)
					message.append("&7You are free to join any other gamemode until your ban is up.\n");
				message.append("&9Your ban will end on:\n");
				Date endDate = new Date(endTime);
				
				//Calendar cal = Calendar.getInstance();
				//cal.setTime(endDate);
				if(endTime > 0) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy | hh:mm:ss");
					message.append("&3" + sdf.format(endDate) + "\n");
				}else {
					message.append("&cNever!\n");
				}
				message.append("&8You are free to submit a ban appeal at: xxxxxxx\n");
				return message.toString();
			}
			else if(cDate.getTime() > endTime) {
				PreparedStatement ps = conn.prepareStatement("UPDATE corePlayerBans SET unbanned = 1 WHERE uuid = ? AND server = ?");
				ps.setString(1, uuid);
				ps.setString(2, core.getConfig().getString("server-id"));
				ps.execute();
				ps.close();
				return null;
			}
			else {
				StringBuilder message = new StringBuilder();
				message.append("&7[&bBungo &6Networks&7]\n");
				message.append("&4You have been banned from this server " + (isGlobal ? "network" : "gamemode") + "!\n");
				message.append("&7Reason:\n");
				message.append(bReason + "\n");
				if(!isGlobal)
					message.append("&7You are free to join any other gamemode until your ban is up.\n");
				message.append("&9Your ban will end on:\n");
				Date endDate = new Date(endTime);
				
				//Calendar cal = Calendar.getInstance();
				//cal.setTime(endDate);
				if(endTime > 0) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy | hh:mm:ss");
					message.append("&3" + sdf.format(endDate) + "\n");
				}else {
					message.append("&cNever!\n");
				}
				message.append("&8You are free to submit a ban appeal at: xxxxxxx\n");
				return message.toString();
			}
		} catch (SQLException e) {
			core.logConsole("&4Database Connection Failed!");
			e.printStackTrace();
			return null;
		}
	}
	
	//Ban System
	public boolean banPlayer(String username, long time, boolean global, String reason) {
		try(Connection conn = core.source.getConnection(); 
				PreparedStatement stmt = conn.prepareStatement("SELECT uuid FROM corePlayerInfo WHERE username = ?"); 
				PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO corePlayerBans(uuid, username, reason, isglobal, server, endtime) VALUES(?, ?, ?, ?, ?, ?)")){
			
			stmt.setString(1, username);
			ResultSet rez = stmt.executeQuery();
			if(!rez.next())
				return false;
			String uuid = rez.getString(1);
			
			OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
			stmt2.setString(1, uuid);
			stmt2.setString(2, op.getName());
			stmt2.setString(3, reason);
			stmt2.setBoolean(4, global);
			stmt2.setString(5, core.getConfig().getString("server-id"));
			stmt2.setLong(6, time);
			stmt2.execute();
			
			if(Bukkit.getPlayer(username) != null) {
				StringBuilder message = new StringBuilder();
				message.append("&7[&bBungo &6Networks&7]\n");
				message.append("&4You have been banned from this server " + (global ? "network" : "gamemode") + "!\n");
				message.append("&7Reason:\n");
				message.append(reason + "\n");
				if(!global)
					message.append("&7You are free to join any other gamemode until your ban is up.\n");
				message.append("&9Your ban will end on:\n");
				Date endDate = new Date();
				endDate.setTime(time);
				
				//Calendar cal = Calendar.getInstance();
				//cal.setTime(endDate);
				if(time > 0) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy | hh:mm:ss");
					message.append("&3" + sdf.format(endDate) + "\n");
				}else {
					message.append("&cNever!\n");
				}
				message.append("&8You are free to submit a ban appeal at: xxxxxxx\n");
				Bukkit.getPlayer(username).kickPlayer(ChatColor.translateAlternateColorCodes('&', message.toString()));
			}
			
			return true;
		} catch (SQLException e) {
			core.logConsole("&4Database Connection Failed!");
			e.printStackTrace();
			return false;
		}
	}
	
}
