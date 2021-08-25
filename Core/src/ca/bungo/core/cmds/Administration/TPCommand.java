package ca.bungo.core.cmds.Administration;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.bungo.core.cmds.CoreCommands;
import ca.bungo.core.core.Core;

public class TPCommand extends CoreCommands {

	public TPCommand(Core core, String name) {
		super(core, name);
		this.description = "Teleport to a player";
		this.usage = "/" + this.name + " <player> [player]";
		this.permLevel = "srmod";
		this.reqPlayer = false;
	}

	@Override
	public void runCommand(String pRank, CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length == 1) {
			if(!(sender instanceof Player))
				sendResponse(sender, "Invalid Usage! &c" + this.usage);
			else {
				Player player = (Player) sender;
				Player target = Bukkit.getPlayer(args[0]);
				if(target == null)
					sendResponse(sender, "Couldn't find player " + args[0]);
				else {
					player.teleport(target);
					sendResponse(sender, "Teleporting to &e" + target.getName());
				}
			}
		}
		else if(args.length == 2) {
			Player player = Bukkit.getPlayer(args[1]);
			Player target = Bukkit.getPlayer(args[0]);
			if(target == null)
				sendResponse(sender, "Couldn't find player " + args[0]);
			else if(player == null)
				sendResponse(sender, "Couldn't find player " + args[1]);
			else {
				player.teleport(target);
				sendResponse(sender, "Teleporting &e" + player.getName() + "&7 to &e" + target.getName());
			}
		}
		else {
			sendResponse(sender, "Invalid Usage! &c" + this.usage);
			return;
		}
		
	}
	
	

}
