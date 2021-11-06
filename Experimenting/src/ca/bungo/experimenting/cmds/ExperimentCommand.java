package ca.bungo.experimenting.cmds;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.bungo.experimenting.experimenting.Experimenting;
import ca.bungo.experimenting.util.Laser;

public class ExperimentCommand implements CommandExecutor {
	
	Experimenting experimenting;
	
	public ExperimentCommand(Experimenting experimenting) {
		this.experimenting = experimenting;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player))
			return true;
		
		Player player  = (Player) sender;
		
		Location loc = player.getLocation();
		loc.setY(255);
		
		try {
			Laser laser = new Laser.GuardianLaser(player.getLocation(), loc, -1, -1);
			laser.start(experimenting);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
		
		return true;
	}

}
