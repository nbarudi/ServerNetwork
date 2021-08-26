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

public class MuteCommand extends CoreCommands {

	public MuteCommand(Core core, String name) {
		super(core, name);
		this.description = "Mute a player for a given time";
		this.usage = "/" + this.name + " <player> <time[s/m/h/d/w/M/y]>";
		this.reqPlayer = false;
		this.permLevel = "trainee";
	}

	@Override
	public void runCommand(String pRank, CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length < 2)
			sendResponse(sender, "Invalid Usage! &c" + this.usage);
		else {
			Player target = Bukkit.getPlayer(args[0]);
			if(target == null)
				sendResponse(sender, "Player is not online!");
			else {
				CoreAPI cAPI = new CoreAPI(core);
				String timeType = args[1].substring(args[1].length()-1);
				if(args[0].length() == 1)
					timeType = "";
				int time = Integer.parseInt(args[1].replace(timeType, ""));
				if(time <= 0) {
					if(!checkTimePermission(pRank, 0)) 
						sendResponse(sender, "You do not have permission to permanently mute a player!");
					else {
						cAPI.mutePlayer(target, 0, sender.getName());
						sendResponse(sender, "You have muted &e" + target.getName());
						sendResponse(target, "You have been muted!");
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
					
					if(response.isEmpty()) {
						sendResponse(sender, "Invalid Usage! &c" + this.usage);
						return;
					}
					
					Date cDate = new Date();
					long cTime = cDate.getTime();
					long endTime = cTime + dTime*1000;
					
					if(!checkTimePermission(pRank, time)) {
						sendResponse(sender, "You do not have permission to mute a player for " + time + " " + response);
						return;
					}else {
						cAPI.mutePlayer(target, endTime, sender.getName());
						sendResponse(sender, "You have muted &e" + target.getName() + "&7 for &e" + time + " " + response);
						sendResponse(target, "You have been muted for " + time + " " + response);
						return;
					}
				}
			}
		}
		
	}
	
	private boolean checkTimePermission(String pRank, int seconds) {
		PermissionsAPI pAPI = new PermissionsAPI(core);
		
		if(seconds <= 0) //Permanent
			return pAPI.checkPermission(pRank, "admin");
		else if(seconds <= 3600) // 1 Hour
			return pAPI.checkPermission(pRank, "trainee");
		else if(seconds <= 10800) // 3 Hours
			return pAPI.checkPermission(pRank, "jrmod");
		else if(seconds <= 21600) // 6 Hours
			return pAPI.checkPermission(pRank, "mod");
		else if(seconds <= 43200) //12 Hours
			return pAPI.checkPermission(pRank, "srmod");
		else if(seconds <= 86400) // 24 Hours
			return pAPI.checkPermission(pRank, "jradmin");
		return false;
	}
	

}
