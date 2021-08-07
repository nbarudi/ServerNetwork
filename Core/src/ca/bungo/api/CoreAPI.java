package ca.bungo.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import ca.bungo.main.Core;

public class CoreAPI {
	
	/*
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
	
	public Core pl;
	
	public CoreAPI() {
		pl = Core.instance;
	}
	
	public boolean playerExists(Player player) {
		String uuid = player.getUniqueId().toString();
		try (Connection conn = pl.source.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT pid FROM corePlayerInfo WHERE uuid = ?;")){
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
	
	public boolean createPlayer(Player player) {
		String uuid = player.getUniqueId().toString();
		
		if(!playerExists(player)) {
			try (Connection conn = pl.source.getConnection(); PreparedStatement stmt = conn.prepareStatement("INSERT INTO corePlayerInfo(uuid, username) VALUES(?, ?)")){
				stmt.setString(1, uuid);
				stmt.setString(2, player.getName());
				stmt.execute();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				pl.logConsole("&4Database Connection Failed!");
				return false;
			}
		}
		return true;
	}

}
