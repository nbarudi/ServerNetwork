package ca.bungo.core.cmds.Administration;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import ca.bungo.core.cmds.CoreCommands;
import ca.bungo.core.core.Core;

public class ReloadCommand extends CoreCommands {

	public ReloadCommand(Core core, String name) {
		super(core, name);
		this.description = "Reload the server";
		this.permLevel = "manager";
		this.usage = "/" + this.name;
		this.reqPlayer = false;
	}

	@Override
	public void runCommand(String pRank, CommandSender sender, Command cmd, String label, String[] args) {
		sendResponse(sender, "Reloading the server!");
		Bukkit.reload();
		sendResponse(sender, "Reload Finished!");
	}

}
