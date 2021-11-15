package ca.bungo.core.events;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import ca.bungo.core.api.CoreAPI;
import ca.bungo.core.api.PermissionsAPI;
import ca.bungo.core.api.cAPI.CoreAPIAbstract.PlayerInfo;
import ca.bungo.core.core.Core;
import ca.bungo.core.events.CustomEvents.PlayerLevelUpEvent;
import net.md_5.bungee.api.ChatColor;

public class PlayerEventManagement implements Listener {
	
	CoreAPI cAPI;
	
	private Core core;
	
	public static List<String> vanished = new ArrayList<String>();
	
	public PlayerEventManagement(Core core) {
		cAPI = new CoreAPI(core);
		this.core = core;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		//if(cAPI.checkBan(player))
		//	return;
		
		cAPI.createPlayer(player);
		PlayerInfo info = cAPI.getPlayerInfo(player);
		if(info == null){
			core.logConsole("&4Something went wrong!");
			return;
		}
		core.logConsole("&3Player Info Found: UUID: &e" + info.uuid + " &3| Username: &a" + info.username + " &3| Level: &a" + info.level + " &3| Rank: &a" + info.rank + " &3| Nickname: " + info.nickname);
		PermissionsAPI pAPI = new PermissionsAPI(core);
		for(String uuid : vanished) {
			if(pAPI.aboveRank(cAPI.getPlayerInfo(player).rank, "jradmin"))
				continue;
			player.hidePlayer(core, Bukkit.getPlayer(UUID.fromString(uuid)));
		}
	}
	
	@EventHandler
	public void onPreJoin(AsyncPlayerPreLoginEvent event) {
		String resp = cAPI.checkBan(event.getUniqueId().toString());
		if(resp != null)
			event.disallow(Result.KICK_BANNED, ChatColor.translateAlternateColorCodes('&', resp));
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
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Levels>&7 Congratulations! You have gone from level &e" + event.getPreviousLevel() + "&7 to level &e" + event.getNewLevel() + "&7!"));
	}
	

}
