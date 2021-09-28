package ca.bungo.hardcore.cmds.Administration;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.bungo.core.cmds.CoreCommands;
import ca.bungo.core.core.Core;
import ca.bungo.hardcore.hardcore.Hardcore;

public class ReviveCommand extends CoreCommands {

	public ReviveCommand(Core core, String name) {
		super(core, name);
		this.usage = "/" + this.name + " <player>";
		this.reqPlayer = false;
		this.permLevel = "communitylead";
	}

	@Override
	public void runCommand(String pRank, CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length != 1) {
			sendResponse(sender, "Invalid usage: " + this.usage);
			return;
		}
		
		Player player = Bukkit.getPlayer(args[0]);
		if(player != null) {
			sendResponse(sender, "Cannot revive an alive player!");
			return;
		}
		
		Hardcore.hardcore.pm.revivePlayer(args[0]);
		sendResponse(sender, "Reviving the player: " + args[0]);
		
	}
	
	

}
