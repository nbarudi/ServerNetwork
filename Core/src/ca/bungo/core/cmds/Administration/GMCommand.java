package ca.bungo.core.cmds.Administration;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.bungo.core.cmds.CoreCommands;
import ca.bungo.core.core.Core;

public class GMCommand extends CoreCommands {

	public GMCommand(Core core, String name) {
		super(core, name);
		this.description = "Change your gamemode";
		this.permLevel = "sradmin";
		this.usage = "/" + this.name + " <gamemode> [player]";
		this.reqPlayer = true;
	}

	@Override
	public void runCommand(String pRank, CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		
		if(label.equalsIgnoreCase("gmc")) {
			player.setGameMode(GameMode.CREATIVE);
			sendResponse(sender, "Set gamemode to &eCreative");
			return;
		}
		else if(label.equalsIgnoreCase("gms")) {
			player.setGameMode(GameMode.SURVIVAL);
			sendResponse(sender, "Set gamemode to &eSurvival");
			return;
		}
		else if(label.equalsIgnoreCase("gmsp")) {
			player.setGameMode(GameMode.SPECTATOR);
			sendResponse(sender, "Set gamemode to &eSpectator");
			return;
		}
		else if(label.equalsIgnoreCase("gma")) {
			player.setGameMode(GameMode.ADVENTURE);
			sendResponse(sender, "Set gamemode to &eAdventure");
			return;
		}
		
		if(args.length == 1) {
			switch(args[0].toLowerCase()) {
			case "creative":
			case "c":
				player.setGameMode(GameMode.CREATIVE);
				sendResponse(sender, "Set gamemode to &eCreative");
				break;
			case "survival":
			case "s":
				player.setGameMode(GameMode.SURVIVAL);
				sendResponse(sender, "Set gamemode to &eSurvival");
				break;
			case "spectator":
			case "spec":
				player.setGameMode(GameMode.SPECTATOR);
				sendResponse(sender, "Set gamemode to &eSpectator");
				break;
			case "adventure":
			case "a":
				player.setGameMode(GameMode.ADVENTURE);
				sendResponse(sender, "Set gamemode to &eAdventure");
				break;
			}
		}
		else if(args.length == 2) {
			Player target = Bukkit.getPlayer(args[1]);
			if(target == null)
				sendResponse(sender, "Could not find player " + args[1]);
			else
				switch(args[0].toLowerCase()) {
				case "creative":
				case "c":
					target.setGameMode(GameMode.CREATIVE);
					sendResponse(sender, "Set &e" + target.getName() + "'s&7 gamemode to &eCreative");
					break;
				case "survival":
				case "s":
					target.setGameMode(GameMode.SURVIVAL);
					sendResponse(sender, "Set &e" + target.getName() + "'s &7gamemode to &eSurvival");
					break;
				case "spectator":
				case "spec":
					target.setGameMode(GameMode.SPECTATOR);
					sendResponse(sender, "Set &e" + target.getName() + "'s &7gamemode to &eSpectator");
					break;
				case "adventure":
				case "a":
					target.setGameMode(GameMode.ADVENTURE);
					sendResponse(sender, "Set &e" + target.getName() + "'s &7gamemode to &eAdventure");
					break;
				}
		}
		else {
			sendResponse(sender, "Invalid Usage! &c" + this.usage);
			return;
		}
	}

}
