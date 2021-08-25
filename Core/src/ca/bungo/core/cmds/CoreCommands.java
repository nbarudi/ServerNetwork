package ca.bungo.core.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import ca.bungo.core.api.CoreAPI;
import ca.bungo.core.api.PermissionsAPI;
import ca.bungo.core.core.Core;
import net.md_5.bungee.api.ChatColor;

public abstract class CoreCommands implements CommandExecutor {
	
	public Core core;
	
	public String name = "";
	public String description = "";
	public String usage = "";
	public String permLevel = "user";
	
	public boolean reqPlayer = false;
	public boolean levelRequirement = false;
	
	public int requiredLevel = 1;
	
	private CoreAPI cAPI;
	private PermissionsAPI pAPI;
	
	public CoreCommands(Core core, String name) {
		this.core = core;
		this.name = name;
		cAPI = new CoreAPI(core);
		pAPI = new PermissionsAPI(core);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String uPermLevel = "user";
		
		if(!reqPlayer && sender instanceof ConsoleCommandSender)
			uPermLevel = "console";
		else
			uPermLevel = cAPI.getPlayerInfo(((Player) sender)).rank;
		
		
		if(this.reqPlayer && !(sender instanceof Player))
			return true;
		
		if(levelRequirement && reqPlayer) {
			if(cAPI.getPlayerInfo(((Player) sender)).level >= requiredLevel) {
				runCommand(uPermLevel, sender, cmd, label, args);
			}else {
				sendResponse(sender, "You do not have permission to run this command! Level &e" + requiredLevel + " &7required!");
			}
			return true;
		}else {
			if(pAPI.checkPermission(uPermLevel, permLevel)) {
				runCommand(uPermLevel, sender, cmd, label, args);
			}else {
				sendResponse(sender, "You do not have permission to run this command! Rank &e" + permLevel + " &7required!");
			}
			return true;
		}
		
		
	}
	
	public void sendResponse(CommandSender sender, String response) {
		String msg = ChatColor.translateAlternateColorCodes('&', "&9" + this.name + "> &7" + response);
		sender.sendMessage(msg);
		return;
	}
	
	public abstract void runCommand(String pRank, CommandSender sender, Command cmd, String label, String[] args);
	
	

}
