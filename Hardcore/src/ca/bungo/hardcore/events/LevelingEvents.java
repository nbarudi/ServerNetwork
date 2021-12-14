package ca.bungo.hardcore.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import ca.bungo.core.api.cAPI.CoreAPIAbstract.PlayerInfo;
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
			xpGain += 75; //100XP per monster kill
		}
		else if(event.getEntity() instanceof Player) {
			xpGain += 150;
		}
		else {
			xpGain = 50;
		}
		
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &7You have gained &e" + xpGain + " &7Experience from killing a " + event.getEntity().getName()));
		info.increaseEXP(xpGain);
		
		
		//Economy Handler
		if(hardcore.eAPI.depositPlayer(info.username, xpGain)) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Economy> &7You have gained &e" + xpGain + " &7credits from killing a " + event.getEntity().getName()));	
		}
		
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		PlayerInfo info = hardcore.cAPI.getPlayerInfo(player);
		
		info.increaseEXP(1); //Want to add XP by 1 for breaking and placing a block.. Just so there is an 'idle' way of gaining XP besides killing mobs
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		PlayerInfo info = hardcore.cAPI.getPlayerInfo(player);
		
		info.increaseEXP(1); //Want to add XP by 1 for breaking and placing a block.. Just so there is an 'idle' way of gaining XP besides killing mobs
	}

}
