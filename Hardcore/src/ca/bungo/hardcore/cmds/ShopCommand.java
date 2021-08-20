package ca.bungo.hardcore.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.bungo.core.cmds.CoreCommands;
import ca.bungo.core.core.Core;
import ca.bungo.hardcore.events.InventoryShopHandler;
import ca.bungo.hardcore.hardcore.Hardcore;
import ca.bungo.hardcore.util.player.PlayerData;

public class ShopCommand extends CoreCommands {

	public ShopCommand(Core core, String name) {
		super(core, name);
		this.description = "Access the shop";
		this.usage = "/" + this.name;
		this.reqPlayer = true;
		this.levelRequirement = true;
		this.requiredLevel = 2;
	}

	@Override
	public void runCommand(String pRank, CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		PlayerData data = Hardcore.hardcore.pm.getPlayerData(player);
		
		if(args.length > 0) {
			sendResponse(sender, "Invalid usage: " + this.usage);
			return;
		}
		
		player.openInventory(InventoryShopHandler.openInventory(data, null));
		
		sendResponse(sender, "Opening Shop menu!");
	}

}
