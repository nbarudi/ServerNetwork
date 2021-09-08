package ca.bungo.hardcore.cmds;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.bungo.core.cmds.CoreCommands;
import ca.bungo.core.core.Core;
import ca.bungo.hardcore.hardcore.Hardcore;

public class TpaCommand extends CoreCommands {
	
	private HashMap<String, String> requests = new HashMap<>();
	
	public TpaCommand(Core core, String name) {
		super(core, name);
		this.description = "Request to teleport to a player on your team.";
		this.levelRequirement = true;
		this.requiredLevel = 10;
		this.usage = "/" + this.name + " <player>";
		this.reqPlayer = true;
	}

	@Override
	public void runCommand(String pRank, CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		
		if(args.length == 0) {
			if(label.equalsIgnoreCase("tpaccept")) {
				if(!requests.containsKey(player.getUniqueId().toString()))
					sendResponse(sender, "You have no requests pending!");
				else {
					Player target = Bukkit.getPlayer(UUID.fromString(requests.get(player.getUniqueId().toString())));
					requests.remove(player.getUniqueId().toString());
					sendResponse(sender, "You have accepted &e" + target.getName() + "'s &7TP Request!");
					sendResponse(target, "&e" + player.getName() + " &7has accepted your TP Request! Teleporting in 3 seconds!");
					Bukkit.getScheduler().scheduleSyncDelayedTask(core, ()->{
						target.teleport(player);
					}, 60);
				}
			}else if(label.equalsIgnoreCase("tpdeny")) {
				if(!requests.containsKey(player.getUniqueId().toString()))
					sendResponse(sender, "You have no requests pending!");
				else {
					Player target = Bukkit.getPlayer(UUID.fromString(requests.get(player.getUniqueId().toString())));
					requests.remove(player.getUniqueId().toString());
					sendResponse(sender, "You have denied &e" + target.getName() + "'s &7TP Request!");
					sendResponse(target, "&e" + player.getName() + " &7has denied your TP Request!");
				}
			}else {
				sendResponse(sender, "Invalid Usage: &c" + this.usage);
				return;
			}
		}else if(args.length == 1) {
			Player target = Bukkit.getPlayer(args[0]);
			if(target == null)
				sendResponse(sender, "Invalid Usage: &c");
			else {
				if(!Hardcore.hardcore.tm.playerHasTeam(player))
					sendResponse(sender, "You must be in a team to use request a Teleport!");
				else if(!Hardcore.hardcore.tm.playerHasTeam(target) || !(Hardcore.hardcore.tm.getTeam(player).teamName.equals(Hardcore.hardcore.tm.getTeam(target).teamName)))
					sendResponse(sender, "You can only TP to someone in your team!");
				else {
					if(requests.containsKey(target.getUniqueId().toString()))
						sendResponse(sender, "This player already has a request pending!");
					else {
						requests.put(target.getUniqueId().toString(), player.getUniqueId().toString());
						sendResponse(sender, "Send a teleport request to " + target.getName() +"!");
						sendResponse(target, player.getName() + " has send you a TP Request! Respond with &e/tpaccept &7or &e/tpdeny &7The request will expire in 30 seconds!");
						Bukkit.getScheduler().scheduleSyncDelayedTask(core, ()->{
							if(requests.containsKey(target.getUniqueId().toString())) {
								requests.remove(target.getUniqueId().toString());
								sendResponse(sender, target.getName() + " did not answer the request in time!");
								sendResponse(target, player.getName() + "'s request has run out!");
							}
						}, 600);
					}
				}
			}
		}else {
			sendResponse(sender, "Invalid Usage: &c" + this.usage);
		}
		
	}

}
