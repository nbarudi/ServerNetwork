package ca.bungo.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ca.bungo.events.CustomEvents.PlayerLevelUpEvent;
import ca.bungo.main.Core;

public class CoreAPI {
	
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
	
	
	public class PlayerInfo {
		
		public boolean exists = false;
		
		public String uuid = "";
		public int pid = 0;
		public String username = "";
		public int exp = 0;
		public int level = 0;
		public String rank = "user";

		public PlayerInfo(Player player) {
			
			if(playerExists(player)) {
				try (Connection conn = pl.source.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM corePlayerInfo WHERE uuid = ?")){
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
						
						this.exists = true;
						return;
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
					pl.logConsole("&4Database Connection Failed!");
					this.exists = false;
					return;
				}
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
			}
		}
	}
	
	public Core pl;
	
	public CoreAPI(Core core) {
		pl = core;
	}
	
	private PlayerInfo createPlayerInfo(Player player) {
		
		if(getPlayerInfo(player) != null)
			return getPlayerInfo(player);
		PlayerInfo info = new PlayerInfo(player);
		
		if(!info.exists)
			return null;
		
		return info;
	}
	
	public boolean playerExists(Player player) {
		String uuid = player.getUniqueId().toString();
		try (Connection conn = pl.source.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT pid FROM corePlayerInfo WHERE uuid = ?")){
			stmt.setString(1, uuid);
			ResultSet results = stmt.executeQuery();
			if(results.next()) {
				return true;
			}else {
				return false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			pl.logConsole("&4Database Connection Failed!");
			return false;
		}
	}
	
	public boolean playerExists(String username) {
		//String uuid = player.getUniqueId().toString();
		try (Connection conn = pl.source.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT pid FROM corePlayerInfo WHERE username = ?")){
			stmt.setString(1, username);
			ResultSet results = stmt.executeQuery();
			if(results.next()) {
				return true;
			}else {
				return false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			pl.logConsole("&4Database Connection Failed!");
			return false;
		}
	}
	
	public boolean createPlayer(Player player) {
		String uuid = player.getUniqueId().toString();
		
		if(!playerExists(player)) {
			try (Connection conn = pl.source.getConnection(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO corePlayerInfo(uuid, username) VALUES(?, ?)")){
				stmt.setString(1, uuid);
				stmt.setString(2, player.getName());
				stmt.execute();
				PlayerInfo info = createPlayerInfo(player);
				if(info != null) {
					pl.pInfo.add(info);
				}
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				pl.logConsole("&4Database Connection Failed!");
				return false;
			}
		}else {
			PlayerInfo info = createPlayerInfo(player);
			if(info != null) {
				pl.pInfo.add(info);
			}
		}
		
		return true;
	}
	
	public void savePlayer(Player player) {
		PlayerInfo info = getPlayerInfo(player);
		
		pl.logConsole("&3Saving Data for Player: &b" + player.getName());
		
		try (Connection conn = pl.source.getConnection(); PreparedStatement stmt = conn.prepareStatement("UPDATE corePlayerInfo SET username = ?, exp = ?, level = ?, rank = ? WHERE pid = ?")){
			stmt.setString(1, player.getName());
			stmt.setInt(2, info.exp);
			stmt.setInt(3, info.level);
			stmt.setString(4, info.rank);
			stmt.setInt(5, info.pid);
			stmt.execute();
		}catch (SQLException e) {
			pl.logConsole("&4Database Connection Failed!");
			e.printStackTrace();
			return;
		}
	}
	
	public void saveAllPlayers() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			savePlayer(player);
		}
	}
	
	public PlayerInfo getPlayerInfo(Player player) {
		PlayerInfo pinf = null;
		for(PlayerInfo info : pl.pInfo) {
			if(player.getUniqueId().toString().equals(info.uuid)) {
				pinf = info;
			}
		}
		return pinf;
	}

	public void removePlayerInfo(Player player) {
		PlayerInfo info = getPlayerInfo(player);
		savePlayer(player);
		pl.pInfo.remove(info);
	}
	
}
