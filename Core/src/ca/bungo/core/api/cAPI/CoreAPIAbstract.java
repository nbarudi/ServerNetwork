package ca.bungo.core.api.cAPI;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ca.bungo.core.core.Core;

public abstract class CoreAPIAbstract {
	
	public abstract class PlayerInfo{
		public boolean exists = false;
		
		public String uuid = "";
		public int pid = 0;
		public String username = "";
		public int exp = 0;
		public int level = 1;
		public String rank = "user";
		
		public String nickname = "";
		
		public String disguise = "";
		public int fakeLevel = 0;
		
		public ArrayList<Integer> tasks = new ArrayList<Integer>();
		
		public PlayerInfo(Player player) {
			this.uuid = player.getUniqueId().toString();
			this.username = player.getName();
		}
		
		/**
		 * Increase the players EXP
		 * If the player reaches the required EXP they will increase 1 level
		 * It is recommended to save the players information after increasing their EXP
		 * */
		public abstract void increaseEXP(int xp);
		public abstract void removeEXP(int xp);
		public abstract void setEXP(int xp);
		
		public abstract void autoUpdater();
	}
	
	public Core core;
	
	public CoreAPIAbstract(Core core) {
		this.core = core;
	}
	
	public abstract PlayerInfo createPlayerInfo(Player player);

	public abstract boolean playerExists(Player player);
	
	public abstract boolean playerExists(String username);
	
	public abstract int getPID(String username);
	
	public abstract String getOfflineUUID(String username);
	
	public abstract boolean createPlayer(Player player);
	
	public abstract void savePlayer(Player player);
	
	public void saveAllPlayers() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			savePlayer(player);
		}
	}
	
	public abstract PlayerInfo getPlayerInfo(Player player);
	
	public abstract void removePlayerInfo(Player player);
	
	public abstract boolean checkMute(Player player);
	
	public abstract boolean mutePlayer(Player player, long endTime, String muter);
	
	public abstract boolean unmutePlayer(Player target);
	
	public abstract boolean checkBan(Player player);
	
	public abstract String checkBan(String uuid);
	
	public abstract boolean banPlayer(String username, long time, boolean global, String reason);
}
