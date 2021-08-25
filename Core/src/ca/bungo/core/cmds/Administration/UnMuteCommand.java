package ca.bungo.core.cmds.Administration;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.bungo.core.api.CoreAPI;
import ca.bungo.core.cmds.CoreCommands;
import ca.bungo.core.core.Core;

public class UnMuteCommand extends CoreCommands {

	public UnMuteCommand(Core core, String name) {
		super(core, name);
		this.description = "UnMute a player";
		this.usage = "/" + this.name + " <player>";
		this.reqPlayer = false;
		this.permLevel = "trainee";
	}

	@Override
	public void runCommand(String pRank, CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length < 1)
			sendResponse(sender, "Invalid Usage! &c" + this.usage);
		else {
			Player target = Bukkit.getPlayer(args[0]);
			if(target == null)
				sendResponse(sender, "Player is not online!");
			else {
				CoreAPI cAPI = new CoreAPI(core);
				cAPI.unmutePlayer(target);
				sendResponse(sender, "Unmuted &e" + target.getName());
				sendResponse(target, "You have been unmuted!");
				return;
			}
		}
		
	}
	

}
