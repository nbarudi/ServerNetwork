package ca.bungo.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import ca.bungo.api.CoreAPI;
import ca.bungo.main.Core;

public class JoinLeaveEvent implements Listener {
	
	CoreAPI cAPI;
	
	public JoinLeaveEvent() {
		cAPI = new CoreAPI();
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		if(!cAPI.playerExists(player)) {
			Core.instance.logConsole("&3Creating Player Information for: &e" + player.getUniqueId().toString() + " | " + player.getName());
			if(cAPI.createPlayer(player)) 
				Core.instance.logConsole("&aPlayer Created!");
			else
				Core.instance.logConsole("&4Failed to create player!");
		}
		
	}
	

}
