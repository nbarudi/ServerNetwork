package ca.bungo.hardcore.cmds.Administration;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import ca.bungo.core.cmds.CoreCommands;
import ca.bungo.core.core.Core;
import ca.bungo.hardcore.hardcore.Hardcore;
import ca.bungo.hardcore.items.CustomItem;
import net.md_5.bungee.api.ChatColor;

public class ItemsCommand extends CoreCommands {
	
	public static Inventory inv = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', "&eAdmin Item Menu"));

	public ItemsCommand(Core core, String name) {
		super(core, name);
		this.description = "Obtain custom items";
		this.permLevel = "headadmin";
		this.reqPlayer = true;
		this.usage = "/" + name;
		
		for(CustomItem item : Hardcore.hardcore.itm.getAllItems()) {
			inv.addItem(item);
		}
	}
	

	@Override
	public void runCommand(String pRank, CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		
		player.openInventory(inv);
		sendResponse(sender, "Opening admin item menu!");
	}
	
	

}
