package ca.bungo.core.cmds.Administration;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.bungo.core.api.CoreAPI;
import ca.bungo.core.api.PermissionsAPI;
import ca.bungo.core.cmds.CoreCommands;
import ca.bungo.core.core.Core;

public class GBanCommand extends CoreCommands {

	public GBanCommand(Core core, String name) {
		super(core, name);
		this.permLevel = "jradmin";
		this.description = "Ban a player from the server network";
		this.usage = "/" + name + " <player> <time[s/m/h/d/w/M/y]> [reason]";
	}

	@Override
	public void runCommand(String pRank, CommandSender sender, Command cmd, String label, String[] args) {
		
		CoreAPI cAPI = new CoreAPI(core);
		
		
		if(args.length < 2) {
			sendResponse(sender, "Invalid usage: " + this.usage);
			return;
		}
		
		//No Ban Reason
		if(args.length == 2) {
			String timeType = args[1].substring(args[1].length()-1);
			if(args[0].length() == 1)
				timeType = "";
			int time = Integer.parseInt(args[1].replace(timeType, ""));
			//Is Perma Ban
			if(time <= 0) {
				if(!checkTimePermission(pRank, 0)) {
					sendResponse(sender, "You do not have permission to permanently ban a player!");
					return;
				}else {
					Player player = Bukkit.getPlayer(args[0]);
					if(player == null) {
						if(!cAPI.playerExists(args[0])) {
							sendResponse(sender, "Could not find player " + args[0] + "!");
							return;
						}else {
							cAPI.banPlayer(args[0], 0, true, "You have been banned from this server!");
							sendResponse(sender, "You have banned " + args[0] + " from the server!");
						}
					}else {
						cAPI.banPlayer(player.getName(), 0, true, "You have been banned from this server!");
						sendResponse(sender, "You have banned " + player.getName() + " from the server!");
					}
				}
			}else {
				//Convert time values into seconds
				String response = "";
				long dTime = 0;
				switch(timeType) {
				case "s":
					dTime = time;
					response = "Second(s)";
					break;
				case "m":
					dTime = time * 60;
					response = "Minute(s)";
					break;
				case "h":
					dTime = time * 60 * 60;
					response = "Hour(s)";
					break;
				case "d":
					dTime = time * 60 * 60 * 24;
					response = "Day(s)";
					break;
				case "w":
					dTime = time * 60 * 60 * 24 * 7;
					response = "Week(s)";
					break;
				case "M":
					dTime = time * 60 * 60 * 24 * 7 * 4;
					response = "Month(s)";
					break;
				case "y":
					dTime = time * 60 * 60 * 24 * 7 * 4 * 12;
					response = "Year(s)";
					break;
				}
				
				Date cDate = new Date();
				long cTime = cDate.getTime();
				long endTime = cTime + dTime*1000;
				
				if(!checkTimePermission(pRank, time)) {
					sendResponse(sender, "You do not have permission to ban a player for " + time + " " + response);
					return;
				}else {
					Player player = Bukkit.getPlayer(args[0]);
					if(player == null) {
						if(!cAPI.playerExists(args[0])) {
							sendResponse(sender, "Could not find player " + args[0] + "!");
							return;
						}else {
							cAPI.banPlayer(args[0], endTime, true, "You have been banned from this server!");
							sendResponse(sender, "You have banned " + args[0] + " from the server!");
						}
					}else {
						cAPI.banPlayer(player.getName(), endTime, true, "You have been banned from this server!");
						sendResponse(sender, "You have banned " + player.getName() + " from the server!");
					}
				}
			}
		}else {
			//Staff gave a reason
			
			StringBuilder sb = new StringBuilder();
			for(int i = 2; i < args.length; i++) {
				sb.append(args[i] + " ");
			}
			
			String timeType = args[1].substring(args[1].length()-1);
			if(args[0].length() == 1)
				timeType = "";
			int time = Integer.parseInt(args[1].replace(timeType, ""));
			//Is Perma Ban
			if(time <= 0) {
				if(!checkTimePermission(pRank, 0)) {
					sendResponse(sender, "You do not have permission to permanently ban a player!");
					return;
				}else {
					Player player = Bukkit.getPlayer(args[0]);
					if(player == null) {
						if(!cAPI.playerExists(args[0])) {
							sendResponse(sender, "Could not find player " + args[0] + "!");
							return;
						}else {
							if(!cAPI.banPlayer(args[0], 0, true, sb.toString())) {
								sendResponse(sender, "Failed to ban the player! Contact Bungo (nbarudi#0001)!");
								return;
							}
							sendResponse(sender, "You have banned " + args[0] + " from the server!");
						}
					}else {
						if(!cAPI.banPlayer(player.getName(), 0, true, sb.toString())) {
							sendResponse(sender, "Failed to ban the player! Contact Bungo (nbarudi#0001)!");
							return;
						}
						sendResponse(sender, "You have banned " + player.getName() + " from the server!");
					}
				}
			}else {
				//Convert time values into seconds
				String response = "";
				long dTime = 0;
				switch(timeType) {
				case "s":
					dTime = time;
					response = "Second(s)";
					break;
				case "m":
					dTime = time * 60;
					response = "Minute(s)";
					break;
				case "h":
					dTime = time * 60 * 60;
					response = "Hour(s)";
					break;
				case "d":
					dTime = time * 60 * 60 * 24;
					response = "Day(s)";
					break;
				case "w":
					dTime = time * 60 * 60 * 24 * 7;
					response = "Week(s)";
					break;
				case "M":
					dTime = time * 60 * 60 * 24 * 7 * 4;
					response = "Month(s)";
					break;
				case "y":
					dTime = time * 60 * 60 * 24 * 7 * 4 * 12;
					response = "Year(s)";
					break;
				}
				
				Date cDate = new Date();
				long cTime = cDate.getTime();
				long endTime = cTime + dTime*1000;
				
				
				if(!checkTimePermission(pRank, time)) {
					sendResponse(sender, "You do not have permission to ban a player for " + time + " " + response);
					return;
				}else {
					Player player = Bukkit.getPlayer(args[0]);
					if(player == null) {
						if(!cAPI.playerExists(args[0])) {
							sendResponse(sender, "Could not find player " + args[0] + "!");
							return;
						}else {
							if(!cAPI.banPlayer(args[0], endTime, true, sb.toString())) {
								sendResponse(sender, "Failed to ban the player! Contact Bungo (nbarudi#0001)!");
								return;
							}
							sendResponse(sender, "You have banned " + args[0] + " from the server!");
						}
					}else {
						if(!cAPI.banPlayer(player.getName(), endTime, true, sb.toString())) {
							sendResponse(sender, "Failed to ban the player! Contact Bungo (nbarudi#0001)!");
							return;
						}
						sendResponse(sender, "You have banned " + player.getName() + " from the server!");
					}
				}
			}
			
		}

	}
	
	/*
	 * General Ban Permissions:
	 * Moderator: Can ban up to 1 day
	 * SrModerator: Can ban up to 1 week
	 * JrAdmin: Can ban up to 1 month
	 * Admin: Can ban up to 3 Month
	 * SrAdmin+: Can Perm Ban
	 * */
	private boolean checkTimePermission(String pRank, int seconds) {
		PermissionsAPI pAPI = new PermissionsAPI(core);
		
		if(seconds <= 0) //Permanent
			return pAPI.checkPermission(pRank, "headadmin");
		else if(seconds <= 604800) //1 week
			return pAPI.checkPermission(pRank, "jradmin");
		else if(seconds <= 2419200) //1 month
			return pAPI.checkPermission(pRank, "admin");
		else if(seconds <= 7257600) //3 month
			return pAPI.checkPermission(pRank, "sradmin");
		
		return false;
	}

}
