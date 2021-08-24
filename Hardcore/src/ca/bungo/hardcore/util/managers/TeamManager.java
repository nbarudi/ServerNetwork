package ca.bungo.hardcore.util.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import ca.bungo.hardcore.hardcore.Hardcore;
import net.md_5.bungee.api.ChatColor;

public class TeamManager {
	
	public class PlayerTeam {
		
		public String teamName;
		public List<String> playerUUIDs = new ArrayList<String>();
		
		public String teamLeader;
		public List<String> officerUUIDs = new ArrayList<String>();
		public int memberCount;
		
		public List<String> invited = new ArrayList<String>(); //This is reset every time the server reloads / restarts
									 //Only meant to be temporary
		
		//Maybe: Might add a 'Team Bank' system to allow for players to have a shared economy
		
		public PlayerTeam(String teamName) {
			this.teamName = teamName;
		}
		
		public void saveTeamData() {
			ConfigurationSection cfgSec = hardcore.getConfig().getConfigurationSection("Teams");
			if(cfgSec == null)
				cfgSec = hardcore.getConfig().createSection("Teams");
			ConfigurationSection teamSection = cfgSec.getConfigurationSection(teamName);
			if(teamSection == null)
				teamSection = cfgSec.createSection(teamName);
			teamSection.set("Members", playerUUIDs);
			teamSection.set("Officers", officerUUIDs);
			teamSection.set("Leader", teamLeader);
			
			hardcore.saveConfig();
		}
		
		public void announceTeam(String message) {
			for(String uuid : playerUUIDs) {
				Player player = Bukkit.getPlayer(UUID.fromString(uuid));
				if(player == null)
					continue;
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Teams> &7" + message));
			}
		}
		
		public String invitePlayer(Player invitee, Player inviter) {
			if(playerUUIDs.contains(invitee.getUniqueId().toString()))
				return "This player is already in your team!";
			if(invited.contains(invitee.getUniqueId().toString()))
				return "This player already has an invite!";
			invited.add(invitee.getUniqueId().toString());
			invitee.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Teams> &e" + inviter.getName() + "&7 has invited you to join: &e" + teamName + "&7! Do /teams join " + teamName + " to accept the invite!"));
			announceTeam("&e" + inviter.getName() + " &7has invited &e" + invitee.getName() + "&7 to the team!");
			return null;
		}
		
		public String joinTeam(Player joiner) {
			if(playerUUIDs.contains(joiner.getUniqueId().toString()))
				return "You are already in this team!";
			if(!invited.contains(joiner.getUniqueId().toString()))
				return "You do not have an invite to this team!";
			invited.remove(joiner.getUniqueId().toString());
			playerUUIDs.add(joiner.getUniqueId().toString());
			announceTeam("&e" + joiner.getName() + "&7 has joined the team!");
			saveTeamData();
			return null;
		}
		
		public String promotePlayer(Player promoter, Player promotee) {
			if(!promoter.getUniqueId().toString().equals(teamLeader))
				return "You must be the leader to promote a player!";
			if(!playerUUIDs.contains(promotee.getUniqueId().toString()))
				return "This player is not in your team!";
			if(officerUUIDs.contains(promotee.getUniqueId().toString()))
				return "This player is already an officer!";
			officerUUIDs.add(promotee.getUniqueId().toString());
			announceTeam("&e" + promoter.getName() + " &7has promoted &e" + promotee.getName() + "&7 to Officer!");
			saveTeamData();
			return null;
		}
		
		public String demotePlayer(Player demoter, Player demotee) {
			if(!demoter.getUniqueId().toString().equals(teamLeader))
				return "You must be the leader to demote a player!";
			if(!playerUUIDs.contains(demotee.getUniqueId().toString()))
				return "This player is not in your team!";
			if(!officerUUIDs.contains(demotee.getUniqueId().toString()))
				return "This player is not an officer!";
			officerUUIDs.remove(demotee.getUniqueId().toString());
			announceTeam("&e" + demoter.getName() + "&7 had demoted &e" + demotee.getName() + "&7 from Officer!");
			saveTeamData();
			return null;
		}
		
		public String transferOwnership(Player teamLeader, Player newLeader) {
			if(!teamLeader.getUniqueId().toString().equals(this.teamLeader))
				return "You are not the team leader!";
			if(!playerUUIDs.contains(newLeader.getUniqueId().toString()))
				return "This player is not in your team!";
			this.teamLeader = newLeader.getUniqueId().toString();
			this.officerUUIDs.add(teamLeader.getUniqueId().toString());
			announceTeam("&e" + teamLeader.getName() + "&7 Has transferred Team Ownership to &e" + newLeader.getName() + "&7!");
			saveTeamData();
			return null;
		}
		
		public String leaveTeam(Player leaver) {
			if(!playerUUIDs.contains(leaver.getUniqueId().toString()))
				return "You are not in this team!";
			if(teamLeader.equals(leaver.getUniqueId().toString()))
				return "You cannot leave your own team!";
			playerUUIDs.remove(leaver.getUniqueId().toString());
			announceTeam("&e" + leaver.getName() + "&7 has left the team!");
			saveTeamData();
			return null;
		}
		
		public void disband() {
			hardcore.tm.teams.remove(this.teamName);
			hardcore.getConfig().set("Teams." + this.teamName, null);
			hardcore.saveConfig();
		}
	}
	
	
	//To-Do: Create a team system
	/*
	 * 
	 * Only team members will be allowed to Teleport to each other
	 * 
	 * If admins discover players working together constantly
	 * 	(Outside of trading / small assists)
	 * Players will be warnned, kicked, and in worse case banned for not using a team.
	 * 
	 * 
	 * Why enforce a team system?
	 * 	Players who are on the same team CANNOT claim Kill Bounties on their team mates.
	 * 	This is to avoid players using exploitable methods to quickly level up them selves or their friends
	 * 	and to keep players from removing their XP Bounty Punishment caused by killing too many players
	 * 
	 */
	
	public HashMap<String, PlayerTeam> teams = new HashMap<String, PlayerTeam>(); 
	
	private Hardcore hardcore;
	
	public TeamManager(Hardcore hardcore) {
		this.hardcore = hardcore;
		
		ConfigurationSection cfgSec = hardcore.getConfig().getConfigurationSection("Teams");
		if(cfgSec == null) {
			hardcore.getConfig().createSection("Teams");
			hardcore.saveConfig();
			return;
		}
		
		for(String teamName : cfgSec.getKeys(false)) {
			if(teams.containsKey(teamName))
				continue;
			PlayerTeam pTeam = new PlayerTeam(teamName);
			//I know that this is probably redundant but i'm to tired to do anything normal.
			ConfigurationSection teamSection = cfgSec.getConfigurationSection(teamName);
			List<String> uuids = teamSection.getStringList("Members");
			
			pTeam.playerUUIDs = uuids;
			pTeam.memberCount = uuids.size();
			pTeam.teamLeader = teamSection.getString("Leader");
			pTeam.officerUUIDs = teamSection.getStringList("Officers");
			teams.put(teamName, pTeam);
		}
	}
	
	public PlayerTeam getTeam(String teamName) {
		for(PlayerTeam team : teams.values()) {
			if(team.teamName.equalsIgnoreCase(teamName))
				return team;
		}
		return null;
	}
	
	public PlayerTeam getTeam(Player player) {
		if(!playerHasTeam(player))
			return null;
		for(PlayerTeam team : teams.values())
			if(team.playerUUIDs.contains(player.getUniqueId().toString()))
				return team;
		return null;
	}
	
	public boolean playerHasTeam(Player player) {
		for(PlayerTeam team : teams.values())
			for(String uuid : team.playerUUIDs)
				if(player.getUniqueId().toString().equals(uuid))
					return true;
		return false;
	}
	
	public boolean teamExists(String teamName) {
		for(String name : teams.keySet()) 
			if(name.equalsIgnoreCase(teamName))
				return true;
		return false;
	}
	
	public String createTeam(Player player, String teamName) {
		if(teamExists(teamName))
			return "Team Already Exists";
		if(playerHasTeam(player))
			return "You are already in a team!";
		PlayerTeam team = new PlayerTeam(teamName);
		team.teamLeader = player.getUniqueId().toString();
		team.memberCount++;
		team.playerUUIDs.add(player.getUniqueId().toString());
		teams.put(teamName, team);
		
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&9Teams> &e" + player.getName() + "&7 has created the team: &e" + teamName + "&7!"));
		team.saveTeamData();
		return null;
	}
	
	public void saveAllData() {
		for(PlayerTeam team : teams.values())
			team.saveTeamData();
	}

}
