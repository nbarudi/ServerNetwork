package ca.bungo.experimenting.experimenting;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ca.bungo.experimenting.cmds.ExperimentCommand;
import ca.bungo.experimenting.events.ChunkGenEvent;

public class Experimenting extends JavaPlugin {
	
	@Override
	public void onEnable() {
		registerEvents();
		registerCommands();
	}

	private void registerCommands() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new ChunkGenEvent(this), this);
	}

	private void registerEvents() {
		this.getCommand("experiment").setExecutor(new ExperimentCommand(this));
		
	}

}
