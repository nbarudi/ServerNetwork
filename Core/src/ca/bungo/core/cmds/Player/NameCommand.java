package ca.bungo.core.cmds.Player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.bungo.core.api.CoreAPI;
import ca.bungo.core.api.cAPI.CoreAPIAbstract.PlayerInfo;
import ca.bungo.core.cmds.CoreCommands;
import ca.bungo.core.core.Core;
import ca.bungo.core.util.ChatUtilities;

public class NameCommand extends CoreCommands{

	public NameCommand(Core core, String name) {
		super(core, name);
		this.reqPlayer = true;
		this.levelRequirement = true;
		this.requiredLevel = 25;
		this.description = "Change your chat name";
		this.usage = "/" + name + " <Name/Off>";
	}

	@Override
	public void runCommand(String pRank, CommandSender sender, Command cmd, String label, String[] args) {
		
		Player player = (Player) sender;
		CoreAPI cAPI = new CoreAPI(core);
		PlayerInfo pInfo = cAPI.getPlayerInfo(player);
		
		if(args.length != 1) {
			sendResponse(sender, "Invalid usage: " + this.usage);
			return;
		}
		
		if(ChatUtilities.hasCensor(args[0])) {
			sendResponse(sender, "You cannot have a name that is censored");
			return;
		}
		
		if(args[0].equalsIgnoreCase("off")) {
			pInfo.nickname = "";
			cAPI.savePlayer(player);
			sendResponse(sender, "Removed your nickname!");
			return;
		}
		
		//To-Do: Check Banned Words
		pInfo.nickname = args[0];
		cAPI.savePlayer(player);
		sendResponse(sender, "Changed your nickanme to " + args[0]);
		return;
	}
	
	

}
