package ca.bungo.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import ca.bungo.api.CoreAPI;
import ca.bungo.api.CoreAPI.PlayerInfo;
import ca.bungo.events.CustomEvents.PlayerLevelUpEvent;
import ca.bungo.main.Core;

public class PlayerEventManagement implements Listener {
	
	CoreAPI cAPI;
	
	private Core core;
	
	public PlayerEventManagement(Core core) {
		cAPI = new CoreAPI(core);
		this.core = core;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		cAPI.createPlayer(player);
		PlayerInfo info = cAPI.getPlayerInfo(player);
		if(info == null){
			core.logConsole("&4Something went wrong!");
			return;
		}
		core.logConsole("&3Player Info Found: UUID: &e" + info.uuid + " &3| Username: &a" + info.username + " &3| Level: &a" + info.level + " &3| Rank: &a" + info.rank);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		cAPI.removePlayerInfo(player);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLevelup(PlayerLevelUpEvent event) {
		Player player = event.getPlayer();
		if(event.isCancelled())
			return;
		player.sendMessage("&9Levels>&7 Congratulations! You have gone from level &e" + event.getPreviousLevel() + "&7 to level &e" + event.getNewLevel() + "&7!");
	}
	

}
