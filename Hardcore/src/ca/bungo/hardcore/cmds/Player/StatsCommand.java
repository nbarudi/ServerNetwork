package ca.bungo.hardcore.cmds.Player;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.bungo.core.cmds.CoreCommands;
import ca.bungo.core.core.Core;
import ca.bungo.hardcore.hardcore.Hardcore;
import ca.bungo.hardcore.util.player.PlayerData;

public class StatsCommand extends CoreCommands {

	public StatsCommand(Core core, String name) {
		super(core, name);
		this.name = "Stats";
		this.usage = "/" + this.name;
		this.description = "Displays Stats about the player!";
		this.reqPlayer = true;
	}

	@Override
	public void runCommand(String pRank, CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		sendResponse(sender, "------------&eNetwork Stats&7------------");
		Bukkit.dispatchCommand(player, "core:stats");
		sendResponse(sender, "------------&eHardcore Stats&7------------");
		PlayerData data = Hardcore.hardcore.pm.getPlayerData(player);
		sendResponse(sender, "Skill-Points: &e" + data.skillPoints);
		sendResponse(sender, "Lives: &e" + data.lives);
		sendResponse(sender, "Owned Perks:");
		for(String perk : data.ownedPerks) {
			sendResponse(sender, "    -&e" + perk);
		}
		sendResponse(sender, "XP-Bounty: &e" + data.xpBounty);
		
	}

}
