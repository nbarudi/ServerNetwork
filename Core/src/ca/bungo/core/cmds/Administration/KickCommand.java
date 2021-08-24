package ca.bungo.core.cmds.Administration;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.bungo.core.cmds.CoreCommands;
import ca.bungo.core.core.Core;
import net.md_5.bungee.api.ChatColor;

public class KickCommand extends CoreCommands {

	public KickCommand(Core core, String name) {
		super(core, name);
		this.description = "Kick a player from the server";
		this.reqPlayer = false;
		this.permLevel = "mod";
		this.usage = "/" + this.name + " <player> [reason]";
	}

	@Override
	public void runCommand(String pRank, CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length == 1) {
			Player target = Bukkit.getPlayer(args[0]);
			if(target == null)
				sendResponse(sender, "Could not find player " + args[0]);
			else {
				StringBuilder sb = new StringBuilder();
				sb.append("&7[&bBungo &6Networks&7]\n");
				sb.append("&eYou have been kicked from the server!\n");
				sb.append("&eReason:\n");
				sb.append("&aYou were kicked from the server!");
				target.kickPlayer(ChatColor.translateAlternateColorCodes('&', sb.toString()));
				core.broadcast("&e" + target.getName() + " &7has been kicked from the server!");
				sendResponse(sender, "Kicked &e" + target.getName() + " &7from the server!");
			}
		}
		else if(args.length >= 2) {
			Player target = Bukkit.getPlayer(args[0]);
			if(target == null)
				sendResponse(sender, "Could not find player " + args[0]);
			else {
			StringBuilder sb = new StringBuilder();
			sb.append("&7[&bBungo &6Networks&7]\n");
			sb.append("&eYou have been kicked from the server!\n");
			sb.append("&eReason:\n");
			
			StringBuilder reason = new StringBuilder();
			for(int i = 1; i < args.length; i++) {
				reason.append(args[i] + " ");
			}
			sb.append("&a" + reason.toString());
			core.broadcast("&e" + target.getName() + " &7has been kicked from the server! Reason: &a" + reason.toString());
			target.kickPlayer(ChatColor.translateAlternateColorCodes('&', sb.toString()));
			sendResponse(sender, "Kicked &e" + target.getName() + " &7from the server!");
			}
		}
		else {
			sendResponse(sender, "Invalid Usage: &c" + this.usage);
		}
		
	}
	
	

}
