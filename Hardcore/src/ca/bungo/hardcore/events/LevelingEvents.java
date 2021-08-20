package ca.bungo.hardcore.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import ca.bungo.core.api.CoreAPI.PlayerInfo;
import ca.bungo.hardcore.hardcore.Hardcore;

public class LevelingEvents implements Listener {
	
	Hardcore hardcore;
	
	private String prefix = "&9Experience>";
	
	public LevelingEvents(Hardcore hardcore) {
		this.hardcore = hardcore;
	}
	
	@EventHandler
	public void entityKillEvent(EntityDeathEvent event) {
		if(!(event.getEntity().getKiller() instanceof Player))
			return;
		
		//Leveling Handler
		Player player = event.getEntity().getKiller();
		PlayerInfo info = hardcore.cAPI.getPlayerInfo(player);
		
		int xpGain = 0;
		
		if(event.getEntity() instanceof Monster) {
			xpGain += 50; //100XP per monster kill
		}
		else if(event.getEntity() instanceof Player) {
			xpGain += 100;
		}
		else {
			xpGain = 25;
		}
		
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &7You have gained &e" + xpGain + " &7Experience from killing a " + event.getEntity().getName()));
		info.increaseEXP(xpGain);
		
		
		//Economy Handler
		hardcore.eAPI.depositPlayer(info.username, xpGain);
		player.sendMessage(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&9Economy> &7You have gained &e" + xpGain + " &7credits from killing a " + event.getEntity().getName()));
	}

}
