package ca.bungo.hardcore.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import ca.bungo.core.cmds.CoreCommands;
import ca.bungo.core.core.Core;
import ca.bungo.hardcore.hardcore.Hardcore;

public class HelpCommand extends CoreCommands {

	public HelpCommand(Core core, String name) {
		super(core, name);
		this.description = "Gives information!";
		this.permLevel = "user";
		this.usage = "/" + this.name + " [section]";
	}

	@Override
	public void runCommand(String pRank, CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0) {
			sender.spigot().sendMessage(Hardcore.hardcore.hu.mainComp);
		}
		else {
			switch(args[0].toLowerCase()) {
			case "info":
				sender.spigot().sendMessage(Hardcore.hardcore.hu.infoComp);
				break;
			case "sp":
				sender.spigot().sendMessage(Hardcore.hardcore.hu.skillPointsComp);
				break;
			case "credits":
				sender.spigot().sendMessage(Hardcore.hardcore.hu.creditsComp);
				break;
			case "bounties":
				sender.spigot().sendMessage(Hardcore.hardcore.hu.bountiesComp);
				break;
			case "commands":
				sender.spigot().sendMessage(Hardcore.hardcore.hu.commandsComp);
				break;
			case "crafting":
				sender.spigot().sendMessage(Hardcore.hardcore.hu.craftingComp);
				break;
			}
		}

	}

}
