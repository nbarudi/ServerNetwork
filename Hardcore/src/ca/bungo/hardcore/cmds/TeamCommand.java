package ca.bungo.hardcore.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.bungo.core.cmds.CoreCommands;
import ca.bungo.core.core.Core;
import ca.bungo.hardcore.hardcore.Hardcore;
import ca.bungo.hardcore.util.managers.TeamManager.PlayerTeam;

public class TeamCommand extends CoreCommands {

	public TeamCommand(Core core, String name) {
		super(core, name);
		this.description = "Main command for teams";
		this.usage = "/" + this.name + " <create/invite/join/leave/promote/demote/transferownership/disband> [Player/TeamName]";
		this.levelRequirement = true;
		this.reqPlayer = true;
		this.requiredLevel = 5;
	}

	@Override
	public void runCommand(String pRank, CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		
		if(args.length == 1) {
			if(!Hardcore.hardcore.tm.playerHasTeam(player)) {
				sendResponse(sender, "You must be in a team to use this command!");
				return;
			}
			
			if(args[0].equalsIgnoreCase("leave")) {
				
				PlayerTeam team = Hardcore.hardcore.tm.getTeam(player);
				String resp = team.leaveTeam(player);
				if(resp == null) {
					sendResponse(sender, "You have left the team!");
					return;
				}else {
					sendResponse(sender, "Something went wrong! &c" + resp);
				}
			}
			else if(args[0].equalsIgnoreCase("disband")) {
				if(!Hardcore.hardcore.tm.teamExists(args[1])) {
					sendResponse(sender, "That team does not exist!");
					return;
				}
				
				PlayerTeam team = Hardcore.hardcore.tm.getTeam(args[1]);
				team.disband();
				sendResponse(sender, "You have disbanded your team!");
			}
		}
		else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("create")) {
				String resp = Hardcore.hardcore.tm.createTeam(player, args[1]);
				if(resp == null) {
					sendResponse(sender, "You have created a new team!");
					return;
				}else {
					sendResponse(sender, "Something went wrong! &c" + resp);
					return;
				}
			}
			else if(args[0].equalsIgnoreCase("join")) {
				if(!Hardcore.hardcore.tm.teamExists(args[1])) {
					sendResponse(sender, "That team does not exist!");
					return;
				}
				
				PlayerTeam team = Hardcore.hardcore.tm.getTeam(args[1]);
				String resp = team.joinTeam(player);
				if(resp == null) {
					sendResponse(sender, "You have joined the team!");
					return;
				}else {
					sendResponse(sender, "Something went wrong! &c" + resp);
					return;
				}
			}
			
			if(!Hardcore.hardcore.tm.playerHasTeam(player)) {
				sendResponse(sender, "You must be in a team to use this command!");
				return;
			}
			
			if(args[0].equalsIgnoreCase("invite")) {
				Player target = Bukkit.getPlayer(args[1]);
				if(target == null) {
					sendResponse(sender, "The player you have chosen is not online!");
					return;
				}
				
				PlayerTeam team = Hardcore.hardcore.tm.getTeam(player);
				String resp = team.invitePlayer(target, player);
				if(resp == null) {
					sendResponse(sender, "You have invited " + target.getName() + " to the team!");
					return;
				}else {
					sendResponse(sender, "Something went wrong! &c" + resp);
				}
			}
			else if(args[0].equalsIgnoreCase("promote")) {
				Player target = Bukkit.getPlayer(args[1]);
				if(target == null) {
					sendResponse(sender, "The player you have chosen is not online!");
					return;
				}
				
				PlayerTeam team = Hardcore.hardcore.tm.getTeam(player);
				String resp = team.promotePlayer(player, target);
				
				if(resp == null) {
					sendResponse(sender, "You have promoted " + target.getName() + " to officer!");
					return;
				}else {
					sendResponse(sender, "Something went wrong! &c" + resp);
				}
			}
			else if(args[0].equalsIgnoreCase("demote")) {
				Player target = Bukkit.getPlayer(args[1]);
				if(target == null) {
					sendResponse(sender, "The player you have chosen is not online!");
					return;
				}
				
				PlayerTeam team = Hardcore.hardcore.tm.getTeam(player);
				String resp = team.demotePlayer(player, target);
				
				if(resp == null) {
					sendResponse(sender, "You have demoted " + target.getName() + " to officer!");
					return;
				}else {
					sendResponse(sender, "Something went wrong! &c" + resp);
				}
			}
			else if(args[0].equalsIgnoreCase("transferownership")) {
				Player target = Bukkit.getPlayer(args[1]);
				if(target == null) {
					sendResponse(sender, "The player you have chosen is not online!");
					return;
				}
				
				PlayerTeam team = Hardcore.hardcore.tm.getTeam(player);
				String resp = team.transferOwnership(player, target);
				
				if(resp == null) {
					sendResponse(sender, "You have trasnfered ownership to " + target.getName() + "!");
					return;
				}else {
					sendResponse(sender, "Something went wrong! &c" + resp);
				}
			}
		}else {
			sendResponse(sender, "Invalid usage: " + this.usage);
			return;
		}
		
		

	}

}
