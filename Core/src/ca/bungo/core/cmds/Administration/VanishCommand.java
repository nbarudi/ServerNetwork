package ca.bungo.core.cmds.Administration;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.bungo.core.api.CoreAPI;
import ca.bungo.core.api.PermissionsAPI;
import ca.bungo.core.cmds.CoreCommands;
import ca.bungo.core.core.Core;
import ca.bungo.core.events.PlayerEventManagement;

public class VanishCommand extends CoreCommands {

	public VanishCommand(Core core, String name) {
		super(core, name);
		this.description = "Hide yourself from other players";
		this.permLevel = "jradmin";
		this.usage = "/" + this.name;
		this.reqPlayer = true;
	}

	@Override
	public void runCommand(String pRank, CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		CoreAPI cAPI = new CoreAPI(core);
		PermissionsAPI pAPI = new PermissionsAPI(core);
		if(PlayerEventManagement.vanished.contains(player.getUniqueId().toString())) {
			PlayerEventManagement.vanished.remove(player.getUniqueId().toString());
			sendResponse(sender, "You have been revealed!");
			for(Player plr : Bukkit.getOnlinePlayers()) {
				if(player.getName().equalsIgnoreCase(plr.getName()))
					continue;
				plr.showPlayer(core, player);
			}
		}else {
			PlayerEventManagement.vanished.add(player.getUniqueId().toString());
			sendResponse(sender, "You have been hidden!");
			for(Player plr : Bukkit.getOnlinePlayers()) {
				if(player.getName().equalsIgnoreCase(plr.getName()))
					continue;
				if(pAPI.aboveRank(cAPI.getPlayerInfo(plr).rank, "jradmin"))
					continue;
				plr.hidePlayer(core, player);
			}
		}
	}

}
