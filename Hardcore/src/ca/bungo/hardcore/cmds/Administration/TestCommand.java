package ca.bungo.hardcore.cmds.Administration;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.bungo.core.cmds.CoreCommands;
import ca.bungo.core.core.Core;
import ca.bungo.hardcore.hardcore.Hardcore;

public class TestCommand extends CoreCommands {

	public TestCommand(Core core, String name) {
		super(core, name);
		this.permLevel = "communitylead";
		this.description = "A command for testing special features";
		this.usage = "/" + this.name;
		this.reqPlayer = true;
	}

	@Override
	public void runCommand(String pRank, CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		
		Location pLoc = player.getLocation();
		
		Hardcore.hardcore.mu.spawnMob(player, pLoc);
	}

}
