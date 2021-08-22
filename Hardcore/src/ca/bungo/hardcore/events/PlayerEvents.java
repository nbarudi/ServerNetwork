package ca.bungo.hardcore.events;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import ca.bungo.core.api.CoreAPI.PlayerInfo;
import ca.bungo.core.events.CustomEvents.PlayerLevelUpEvent;
import ca.bungo.hardcore.hardcore.Hardcore;
import ca.bungo.hardcore.util.player.PlayerData;

public class PlayerEvents implements Listener {
	
	Hardcore hardcore;
	
	//private String prefix = "&9Levels>";
	
	public PlayerEvents(Hardcore hardcore) {
		this.hardcore = hardcore;
	}
	
	@EventHandler
	public void onLevelUp(PlayerLevelUpEvent event) {
		Player player = event.getPlayer();
		PlayerInfo info = hardcore.cAPI.getPlayerInfo(player);
		PlayerData data = hardcore.pm.getPlayerData(player);
		data.skillPoints++;
		
		//player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &7Congradulations! You have gone from level &e" + event.getPreviousLevel() + " &7to level &e" + event.getNewLevel() + "&7!"));
		
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "acb " + player.getName() + " " + info.level * 10);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		PlayerData data = hardcore.pm.createPlayerData(player);
		PlayerInfo info = hardcore.cAPI.getPlayerInfo(player);
		
		if(data.lives == 0 && data.reviveTime > new Date().getTime()) {
			if(hardcore.pAPI.aboveRank(info.rank, "admin")) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4You have 0 Lives remaining but your rank allows you to bypass live bans!"));
			}
			else {
				long reviveTime = data.reviveTime;
				Date date = new Date(reviveTime);
				StringBuilder sb = new StringBuilder();
				sb.append("&7[&aHardcore&7]\n");
				sb.append("&eSeems you have run out of lives!\n");
				sb.append("&eYou will gain 1 extra life on:\n");
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy | hh:mm:ss");
				sb.append("&3" + sdf.format(date) + "\n");
				sb.append("&eIf you do not want to wait until this day\n");
				sb.append("&eYou can purchase a revive on our website:\n");
				sb.append("&exxxxxxxx");
				player.kickPlayer(ChatColor.translateAlternateColorCodes('&', sb.toString()));
			}
		}
		else if(data.lives == 0 && data.reviveTime <= new Date().getTime()) {
			data.lives++;
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou have been revived into this world!"));
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		hardcore.pm.removePlayerData(hardcore.pm.getPlayerData(event.getPlayer()));
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		PlayerData data = hardcore.pm.getPlayerData(player);
		data.handleDeath();
		
		//Bounty System
		Player killer = player.getKiller();
		if(!(killer instanceof Player))
			return;
		PlayerData kData = hardcore.pm.getPlayerData(killer);
		PlayerInfo kInfo = hardcore.cAPI.getPlayerInfo(killer);
		
		kData.xpBounty += 200;
		
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&9Bounty> &e" + killer.getName() + " &7has killed a player and increased their bounty! It is now worth: &e" + kData.xpBounty + " &7XP!"));
		
		if(data.xpBounty > 0) {
			killer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Bounty> &7You have killed a player with a bounty on their head! You have gained &e" + data.xpBounty + " &7XP!"));
			kInfo.increaseEXP(data.xpBounty);
			data.xpBounty = 0;
		}
		
		hardcore.pm.savePlayerData(kData);
		hardcore.pm.savePlayerData(data);
	}
	

}
