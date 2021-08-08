package ca.bungo.cmds.Player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.bungo.api.CoreAPI;
import ca.bungo.api.CoreAPI.PlayerInfo;
import ca.bungo.api.PermissionsAPI;
import ca.bungo.cmds.CoreCommands;
import ca.bungo.main.Core;
import ca.bungo.util.RankUtilities;

public class InfoCommand extends CoreCommands {

	public InfoCommand(Core core, String name) {
		super(core, name);
		this.description = "Get your player information";
		this.usage = "/" + this.name;
		this.permLevel = "user";
		this.reqPlayer = true;
	}

	@Override
	public void runCommand(String pRank, CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		
		CoreAPI cAPI = new CoreAPI(core);
		PermissionsAPI pAPI = new PermissionsAPI(core);
		
		pAPI.checkPermission(pRank, this.permLevel);
		PlayerInfo info = cAPI.getPlayerInfo(player);
		
		sendResponse(sender, "Here is your player information!");
		sendResponse(sender, "Player ID: " + info.pid);
		sendResponse(sender, "Username: " + info.username);
		sendResponse(sender, "UUID: " + info.uuid);
		sendResponse(sender, "Experience: " + info.exp + "/" + (info.level*250 + 1000));
		sendResponse(sender, "Level: " + info.level);
		sendResponse(sender, "Rank: " + RankUtilities.ranks.get(info.rank));

	}

}
