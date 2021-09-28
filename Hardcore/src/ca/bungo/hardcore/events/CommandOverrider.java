package ca.bungo.hardcore.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandOverrider implements Listener {
	
	@EventHandler
	public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if(event.getMessage().startsWith("/stats")) {
			Bukkit.dispatchCommand(event.getPlayer(), "hardcore:stats");
			event.setCancelled(true);
		}
	}

}
