package ca.bungo.hardcore.cmds;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import ca.bungo.core.api.PermissionsAPI;
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
		PermissionsAPI pAPI = new PermissionsAPI(core);
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
				
				int pageNum = 1;
				
				if(args.length == 1 || Integer.parseInt(args[1]) == 0)
					pageNum = 1;
				else
					pageNum = Integer.parseInt(args[1]);
				
				ArrayList<String> cmds = new ArrayList<>();
				for(CoreCommands coreCMD : core.helpList) {
					if(!pAPI.aboveRank(pRank, coreCMD.permLevel))
						continue;
					StringBuilder sb = new StringBuilder();
					sb.append("---------------------------\n");
					sb.append("&c/" + coreCMD.name + "\n");
					sb.append("&aDescription: &e" + coreCMD.description + "\n");
					sb.append("&aUsage: &e" + coreCMD.usage + "\n");
					sb.append("&aPermission: &e" + coreCMD.permLevel + "\n");
					cmds.add(sb.toString());
				}
				
				if(pageNum > cmds.size()/3) {
					sendResponse(sender, "Error: Page does not exist!");
					return;
				}
				
				sendResponse(sender, "Here are some commands: (Page " + pageNum + "/" + cmds.size()/3 + ")");
				for(int i = (pageNum*3)-3; i < (pageNum*3); i++) {
					if(cmds.get(i) == null)
						continue;
					sendResponse(sender, cmds.get(i));
				}
				sendResponse(sender, "Use /help commands <page> for more commands!");
				break;
			case "crafting":
				sender.spigot().sendMessage(Hardcore.hardcore.hu.craftingComp);
				break;
			}
		}

	}

}
