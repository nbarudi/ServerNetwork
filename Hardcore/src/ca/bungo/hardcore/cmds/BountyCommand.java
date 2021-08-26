package ca.bungo.hardcore.cmds;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.bungo.core.cmds.CoreCommands;
import ca.bungo.core.core.Core;
import ca.bungo.hardcore.hardcore.Hardcore;

public class BountyCommand extends CoreCommands {

	public BountyCommand(Core core, String name) {
		super(core, name);
		this.reqPlayer = true;
		this.levelRequirement = true;
		this.requiredLevel = 10;
		this.description = "Get a list of player bounties!";
		this.usage = "/" + this.name;
	}

	@Override
	public void runCommand(String pRank, CommandSender sender, Command cmd, String label, String[] args) {
		//Player player = (Player) sender;
		
		Map<String, Integer> bounties = Hardcore.hardcore.pm.getBounties();
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("&ePlayer Bounties:\n");
		
		bounties.forEach((k,v) ->{
			Player other = Bukkit.getPlayer(k);
			sb.append("&7" + k + ": &e" + v + ((other != null) ? "&7(&aOnline&7)" : "&7(&4Offline&7)") + "\n");
		});
		
		
		
		sendResponse(sender, sb.toString());
	}

}
