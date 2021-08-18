package ca.bungo.core.cmds.Administration;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.bungo.core.api.CoreAPI;
import ca.bungo.core.api.CoreAPI.PlayerInfo;
import ca.bungo.core.cmds.CoreCommands;
import ca.bungo.core.core.Core;

public class ExperienceCommand extends CoreCommands {

	public ExperienceCommand(Core core, String name) {
		super(core, name);
		this.description = "Modify a players user experience level";
		this.usage = "/" + name + " <player> <add/set/remove> <ammount>";
		this.permLevel = "manager";
		this.reqPlayer = false;
	}

	@Override
	public void runCommand(String pRank, CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length != 3) {
			sendResponse(sender, "Invalid usage: " + this.usage);
			return;
		}
		
		Player player = Bukkit.getPlayer(args[0]);
		if(player == null) {
			sendResponse(sender, "The player is requried to be online to use this command!");
			return;
		}
		
		CoreAPI cAPI = new CoreAPI(core);
		PlayerInfo info = cAPI.getPlayerInfo(player);
		int xp = Integer.parseInt(args[2]);
		
		if(args[1].equalsIgnoreCase("add")) {
			info.increaseEXP(xp);
			sendResponse(sender, "Added &e" + xp + " &7 XP to &e" + player.getName() );
		}
		else if(args[1].equalsIgnoreCase("set")) {
			info.setEXP(xp);
			sendResponse(sender, "Set &e" + player.getName() + "'s &7XP to &e" + xp );
		}
		else if(args[1].equalsIgnoreCase("remove")) {
			info.removeEXP(xp);
			sendResponse(sender, "Removed &e" + xp + " &7 XP from &e" + player.getName() );
		}

	}

}
