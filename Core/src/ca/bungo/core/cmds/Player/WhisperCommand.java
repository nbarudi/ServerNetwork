package ca.bungo.core.cmds.Player;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.bungo.core.cmds.CoreCommands;
import ca.bungo.core.core.Core;

public class WhisperCommand extends CoreCommands {
	
	private HashMap<String, String> lastMessage = new HashMap<String, String>();

	public WhisperCommand(Core core, String name) {
		super(core, name);
		this.description = "Send a private message to a player";
		this.usage = "/" + this.name + " <player> <message>";
		this.levelRequirement = true;
		this.requiredLevel = 5;
	}

	@Override
	public void runCommand(String pRank, CommandSender sender, Command cmd, String label, String[] args) {
		
		if(label.equalsIgnoreCase("reply") || label.equalsIgnoreCase("r")) {
			StringBuilder message = new StringBuilder();
			for(int i = 0; i < args.length; i++)
				message.append(args[i] + " ");
			String lastPerson = lastMessage.get(sender.getName());
			if(lastPerson == null)
				sendResponse(sender, "There is no one to reply to!");
			else if(lastPerson == "CONSOLE") {
				CommandSender target = Bukkit.getConsoleSender();
				sendResponse(target, "&d" + sender.getName() + "&7->&d" + target.getName() + "&7:&e " + message.toString());
				sendResponse(sender, "&d" + sender.getName() + "&7->&d" + target.getName() + "&7:&e " + message.toString());
				lastMessage.put(lastPerson, sender.getName());
				lastMessage.put(sender.getName(), lastPerson);
			}
			else {
				Player target = Bukkit.getPlayer(lastPerson);
				if(target == null) {
					sendResponse(sender, "Couldn't find player! Are they offline?");
					return;
				}
				sendResponse(target, "&d" + sender.getName() + "&7->&d" + target.getName() + "&7:&e " + message.toString());
				sendResponse(sender, "&d" + sender.getName() + "&7->&d" + target.getName() + "&7:&e " + message.toString());
				lastMessage.put(lastPerson, sender.getName());
				lastMessage.put(sender.getName(), lastPerson);
			}
		}
		else if(args.length < 2)
			sendResponse(sender, "Invalid Usage! &c" + this.usage);
		else {
			StringBuilder message = new StringBuilder();
			for(int i = 1; i < args.length; i++)
				message.append(args[i] + " ");
			if(args[0].equalsIgnoreCase("CONSOLE")) {
				CommandSender target = Bukkit.getConsoleSender();
				sendResponse(target, "&d" + sender.getName() + "&7->&d" + target.getName() + "&7:&e " + message.toString());
				sendResponse(sender, "&d" + sender.getName() + "&7->&d" + target.getName() + "&7:&e " + message.toString());
				lastMessage.put(target.getName(), sender.getName());
				lastMessage.put(sender.getName(), target.getName());
			}
			else {
				Player target = Bukkit.getPlayer(args[0]);
				if(target == null) {
					sendResponse(sender, "Couldn't find player! Are they offline?");
					return;
				}
				sendResponse(target, "&d" + sender.getName() + "&7->&d" + target.getName() + "&7:&e " + message.toString());
				sendResponse(sender, "&d" + sender.getName() + "&7->&d" + target.getName() + "&7:&e " + message.toString());
				lastMessage.put(target.getName(), sender.getName());
				lastMessage.put(sender.getName(), target.getName());
			}
		}
	}
	
	

}
