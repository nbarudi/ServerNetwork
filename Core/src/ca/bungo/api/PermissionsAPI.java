package ca.bungo.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.entity.Player;

import ca.bungo.main.Core;

public class PermissionsAPI {
	
	public Core core;
	
	private CoreAPI cAPI;
	
	private HashMap<String, Integer> ranks = new HashMap<String, Integer>(){
		private static final long serialVersionUID = 1190592294505797551L;

	{
		put("user", 1);
		put("trainee", 244);
		put("jrmod", 245);
		put("mod", 246);
		put("srmod", 247);
		put("jradmin", 248);
		put("admin", 249);
		put("sradmin", 250);
		put("headadmin", 251);
		put("manager", 252);
		put("developer", 253);
		put("communitylead", 253);
		put("owner", 254);
		put("console", 255);
	}};
	
	
	public PermissionsAPI(Core core) {
		this.core = core;
		cAPI = new CoreAPI(core);
	}
	
	public boolean hasPermission(Player player, String rank) {
		String prnk = cAPI.getPlayerInfo(player).rank;
		int permLvl = ranks.get(prnk);
		int reqLvl = ranks.get(rank);
		return permLvl >= reqLvl;
	}
	
	public boolean hasPermission(Player player, int rank) {
		String prnk = cAPI.getPlayerInfo(player).rank;
		int permLvl = ranks.get(prnk);
		int reqLvl = rank;
		return permLvl >= reqLvl;
	}
	
	public boolean checkPermission(String currentLevel, String reqLevel) {
		int permLvl = ranks.get(currentLevel);
		int reqLvl = ranks.get(reqLevel);
		return permLvl >= reqLvl;
	}
	
	public boolean aboveRank(String currentRank, String minRank) {
		int permLvl = ranks.get(currentRank);
		int reqLvl = ranks.get(minRank);
		return permLvl > reqLvl;
	}
	
	public boolean updateRank(Player player, String rank) {
		if(cAPI.getPlayerInfo(player) != null) {
			cAPI.getPlayerInfo(player).rank = rank.toLowerCase();
			return true;
		}
		return false;
	}
	
	public String getOfflineRank(String username) {
		if(!cAPI.playerExists(username))
			return null;
		try(Connection conn = core.source.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT rank FROM corePlayerInfo WHERE username = ?")){
			stmt.setString(1, username);
			ResultSet results = stmt.executeQuery();
			return results.getString(6);
		} catch (SQLException e) {
			e.printStackTrace();
			core.logConsole("&4Database Connection Failed!");
			return null;
		}
	}
	
	public boolean updateOfflineRank(String username, String rank) {
		if(!cAPI.playerExists(username))
			return false;
		try(Connection conn = core.source.getConnection(); PreparedStatement stmt = conn.prepareStatement("UPDATE corePlayerInfo SET rank = ? WHERE username = ?")){
			stmt.setString(1, rank);
			stmt.setString(2, username);
			stmt.execute();
			return true;
		}catch (SQLException e) {
			e.printStackTrace();
			core.logConsole("&4Database Connection Failed!");
			return false;
		}
	}
	
}
