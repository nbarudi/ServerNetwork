package ca.bungo.cmds.Administration;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.bungo.api.CoreAPI;
import ca.bungo.api.CoreAPI.PlayerInfo;
import ca.bungo.api.PermissionsAPI;
import ca.bungo.cmds.CoreCommands;
import ca.bungo.main.Core;

public class RankCommand extends CoreCommands {

	public RankCommand(Core core, String name) {
		super(core, name);
		this.description = "Sets the rank of the supplied player";
		this.permLevel = "headadmin";
		this.reqPlayer = false;
		this.usage = "/" + name + " <player> <rank>";
	}

	@Override
	public void runCommand(String pRank, CommandSender sender, Command cmd, String label, String[] args) {
		
		CoreAPI cAPI = new CoreAPI(core);
		PermissionsAPI pAPI = new PermissionsAPI(core);
		
		if(args.length != 2) {
			sendResponse(sender, "Incorrect Usage: " + this.usage);
			return;
		}
		
		String username = args[0];
		String toRank = args[1];
		
		Player target = Bukkit.getPlayer(username);
		
		if(target == null) {
			//Target is currently not online...
			if(!cAPI.playerExists(username)) {
				sendResponse(sender, "Player does not exist!");
				return;
			}else {
				String rank = pAPI.getOfflineRank(username);
				//Is the other person higher rank then you
				if(pAPI.aboveRank(pRank, rank)) {
					//Are you ranking them to a higher rank then yours
					if(pAPI.aboveRank(pRank, toRank)) {
						//Were you able to update the rank
						if(pAPI.updateOfflineRank(username, toRank)) {
							sendResponse(sender, "Changed user &e" + username + "&7 to rank &e" + toRank + "&7!");
							return;
						}else {
							sendResponse(sender, "Failed to change the users rank...");
							return;
						}
					}else {
						sendResponse(sender, "You cannot set someones rank higher then your rank.");
						return;
					}
				}else {
					sendResponse(sender, "You cannot change the rank of someone with a higher permission level then you.");
					return;
				}
			}
		}else {
			if(!cAPI.playerExists(target)) {
				sendResponse(sender, "Player does not exist!");
				return;
			}else {
				PlayerInfo info = cAPI.getPlayerInfo(target);
				String rank = info.rank;
				//Is the other person higher rank then you
				if(pAPI.aboveRank(pRank, rank)) {
					//Are you ranking them to a higher rank then yours
					if(pAPI.aboveRank(pRank, toRank)) {
						//Were you able to update the rank
						if(pAPI.updateRank(target, toRank)) {
							sendResponse(sender, "Changed user &e" + target.getName() + "&7 to rank &e" + toRank + "&7!");
							return;
						}else {
							sendResponse(sender, "Failed to change the users rank...");
							return;
						}
					}else {
						sendResponse(sender, "You cannot set someones rank higher then your rank.");
						return;
					}
				}else {
					sendResponse(sender, "You cannot change the rank of someone with a higher permission level then you.");
					return;
				}
			}
		}
		
	}

}
