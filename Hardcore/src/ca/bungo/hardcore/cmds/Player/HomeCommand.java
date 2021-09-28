package ca.bungo.hardcore.cmds.Player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import ca.bungo.core.cmds.CoreCommands;
import ca.bungo.core.core.Core;
import ca.bungo.hardcore.hardcore.Hardcore;

public class HomeCommand extends CoreCommands {

	public HomeCommand(Core core, String name) {
		super(core, name);
		this.description = "Set/Remove/Teleport to homes";
		this.usage = "/sethome <name>| /delhome <name> | /home <name>";
		this.levelRequirement = true;
		this.requiredLevel = 3;
		this.reqPlayer = true;
		
		if(!Hardcore.hardcore.getConfig().isConfigurationSection("Homes")) {
			Hardcore.hardcore.getConfig().createSection("Homes");
		}
	}

	@Override
	public void runCommand(String pRank, CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		ConfigurationSection csec = Hardcore.hardcore.getConfig().getConfigurationSection("Homes");
		if(args.length != 1) {
			
			if(label.equalsIgnoreCase("home") || label.equalsIgnoreCase("homes")) {
				ConfigurationSection psec = csec.getConfigurationSection(player.getUniqueId().toString());
				if(psec == null){
					sendResponse(sender, "You have no homes set!");
					return;
				}
				
				if(psec.getKeys(false).size() == 1) {
					sendResponse(sender, "Teleporting to home &e" + (String)psec.getKeys(false).toArray()[0]);
					player.teleport(getHomeLoc(psec, (String)psec.getKeys(false).toArray()[0]));
					return;
				}else {
					StringBuilder sb = new StringBuilder();
					sb.append("Here are a list of your homes: ");
					for(String k : psec.getKeys(false)) {
						sb.append("&e" + k + "&7, ");
					}
					sendResponse(sender, sb.toString());
					return;
				}
			}
			
			sendResponse(sender, "Invalid usage!");
			return;
		}
		
		if(label.equalsIgnoreCase("sethome")) {
			ConfigurationSection psec = csec.getConfigurationSection(player.getUniqueId().toString());
			if(psec == null)
				psec = csec.createSection(player.getUniqueId().toString());
			if(psec.getKeys(false).size() >= maxHomes(pRank)) {
				sendResponse(sender, "You have reached the maximum number of homes! &e" + maxHomes(pRank));
				return;
			}
			String name = args[0].toLowerCase();
			if(psec.isConfigurationSection(name)) {
				sendResponse(sender, "You already have a home with this name!");
				return;
			}
			
			Location loc = player.getLocation();
			psec.set(name + ".x", loc.getBlockX());
			psec.set(name + ".y", loc.getBlockY());
			psec.set(name + ".z", loc.getBlockZ());
			psec.set(name + ".world", loc.getWorld().getName());
			psec.set(name + ".yaw", loc.getYaw());
			psec.set(name + ".pitch", loc.getPitch());
			
			sendResponse(sender, "Set home &e" + name + " &7to your current location!");
		}
		else if(label.equalsIgnoreCase("delhome")) {
			ConfigurationSection psec = csec.getConfigurationSection(player.getUniqueId().toString());
			if(psec == null) {
				sendResponse(sender, "You do not have any homes!");
				return;
			}
			String name = args[0].toLowerCase();
			if(!psec.isConfigurationSection(name)) {
				sendResponse(sender, "You do not have any homes with this name!");
				return;
			}
			psec.set(name, null);
			sendResponse(sender, "Removed home &e" + name);
			return;
		}
		else if(label.equalsIgnoreCase("home")) {
			ConfigurationSection psec = csec.getConfigurationSection(player.getUniqueId().toString());
			String name = args[0].toLowerCase();
			
			if(psec == null) {
				sendResponse(sender, "You do not have any homes!");
				return;
			}
			if(!psec.isConfigurationSection(name)) {
				sendResponse(sender, "You do not have any homes with this name!");
				return;
			}
			
			player.teleport(getHomeLoc(psec, name));
			sendResponse(sender, "Teleporting to home &e" + name);
		}
		
	}
	
	private int maxHomes(String rank) {
		switch (rank.toLowerCase()) {
		case "user":
		case "user+":
			return 1;
		case "user++":
		case "vip":
			return 2;
		case "vip+":
		case "vip++":
			return 3;
		}
		return 5;
	}
	
	private Location getHomeLoc(ConfigurationSection sec, String name) {
		World w = Bukkit.getWorld(sec.getString(name + ".world"));
		double x = sec.getDouble(name + ".x");
		double y = sec.getDouble(name + ".y");
		double z = sec.getDouble(name + ".z");
		float yaw = (float)sec.getDouble(name + ".yaw");
		float pitch = (float)sec.getDouble(name + ".pitch");
		
		return new Location(w, x,y,z,yaw,pitch);
	}

}
