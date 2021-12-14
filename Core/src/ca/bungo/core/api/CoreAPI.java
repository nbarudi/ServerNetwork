package ca.bungo.core.api;

import org.bukkit.entity.Player;

import ca.bungo.core.api.cAPI.ConfigApi;
import ca.bungo.core.api.cAPI.CoreAPIAbstract;
import ca.bungo.core.api.cAPI.MySQLApi;
import ca.bungo.core.core.Core;

public class CoreAPI extends CoreAPIAbstract {
	
	public CoreAPIAbstract cAPI;
	
	public CoreAPI(Core core) {
		super(core);
		if(core.usingMySQL && core.source != null) {
			cAPI = new MySQLApi(core);
			core.logConsole("&eLoading MySQL based API!");
		}
		else {
			cAPI = new ConfigApi(core);
			core.logConsole("&eLoading Config Based API!");
		}
			
		
	}

	@Override
	public PlayerInfo createPlayerInfo(Player player) {
		return cAPI.createPlayerInfo(player);
	}

	@Override
	public boolean playerExists(Player player) {
		return cAPI.playerExists(player);
	}

	@Override
	public boolean playerExists(String username) {
		return cAPI.playerExists(username);
	}

	@Override
	public int getPID(String username) {
		return cAPI.getPID(username);
	}

	@Override
	public String getOfflineUUID(String username) {
		return cAPI.getOfflineUUID(username);
	}

	@Override
	public boolean createPlayer(Player player) {
		return cAPI.createPlayer(player);
	}

	@Override
	public void savePlayer(Player player) {
		cAPI.savePlayer(player);
	}

	@Override
	public PlayerInfo getPlayerInfo(Player player) {
		return cAPI.getPlayerInfo(player);
	}

	@Override
	public void removePlayerInfo(Player player) {
		cAPI.removePlayerInfo(player);
	}

	@Override
	public boolean checkMute(Player player) {
		return cAPI.checkMute(player);
	}

	@Override
	public boolean mutePlayer(Player player, long endTime, String muter) {
		return cAPI.mutePlayer(player, endTime, muter);
	}

	@Override
	public boolean unmutePlayer(Player target) {
		return cAPI.unmutePlayer(target);
	}

	@Override
	public boolean checkBan(Player player) {
		return cAPI.checkBan(player);
	}

	@Override
	public String checkBan(String uuid) {
		return cAPI.checkBan(uuid);
	}

	@Override
	public boolean banPlayer(String username, long time, boolean global, String reason) {
		return cAPI.banPlayer(username, time, global, reason);
	}

}
