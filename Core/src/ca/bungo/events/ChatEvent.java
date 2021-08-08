package ca.bungo.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import ca.bungo.api.CoreAPI;
import ca.bungo.api.CoreAPI.PlayerInfo;
import ca.bungo.main.Core;
import ca.bungo.util.RankUtilities;

public class ChatEvent implements Listener {
	
	private Core core;
	
	public ChatEvent(Core core) {
		this.core = core;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		event.setCancelled(true);
		Player player = event.getPlayer();
		CoreAPI cAPI = new CoreAPI(core);
		PlayerInfo info = cAPI.getPlayerInfo(player);
		String rankTag = RankUtilities.ranks.get(info.rank);
		
		StringBuilder sb = new StringBuilder();
	}

}
