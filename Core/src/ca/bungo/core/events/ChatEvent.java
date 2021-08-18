package ca.bungo.core.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import ca.bungo.core.api.CoreAPI;
import ca.bungo.core.api.CoreAPI.PlayerInfo;
import ca.bungo.core.core.Core;
import ca.bungo.core.util.ChatUtilities;
import ca.bungo.core.util.RankUtilities;
import net.md_5.bungee.api.ChatColor;

public class ChatEvent implements Listener {
	
	private Core core;
	
	public ChatEvent(Core core) {
		this.core = core;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		
		if(!core.useCoreChat)
			return;
		
		event.setCancelled(true);
		Player player = event.getPlayer();
		CoreAPI cAPI = new CoreAPI(core);
		PlayerInfo info = cAPI.getPlayerInfo(player);
		String rankTag = RankUtilities.ranks.get(info.rank);
		int level = info.level;
		
		String playerName = player.getName();
		if(!info.nickname.isEmpty())
			playerName = info.nickname;
		
		
		String playerDetails = "&9" + level + " " + rankTag + " " + "&e" + playerName + " &7";
		
		if(!info.disguise.isEmpty())
			playerDetails = "&9" + info.fakeLevel + " " + RankUtilities.ranks.get("user") + " &e" + info.disguise + " &7";
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', playerDetails + ChatUtilities.checkCensor(event.getMessage())));
		}
		
		
	}

}
