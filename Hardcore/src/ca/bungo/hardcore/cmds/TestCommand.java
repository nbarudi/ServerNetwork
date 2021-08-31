package ca.bungo.hardcore.cmds;

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
		
		for(int x = -10; x < 11; x++)
			for(int y = -10; y < 11; y++)
				for(int z = -10; z < 11; z++) {
					Location loc = new Location(pLoc.getWorld(), pLoc.getBlockX() + x, pLoc.getBlockY() + y, pLoc.getBlockZ() + z);
					Hardcore.hardcore.bu.glowBlock(loc);
				}

	}

}
