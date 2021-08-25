package ca.bungo.hardcore.skills.Keep;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import ca.bungo.hardcore.hardcore.Hardcore;
import ca.bungo.hardcore.skills.Skill;
import ca.bungo.hardcore.util.player.PlayerData;

public class KeepLevels extends Skill {

	public KeepLevels(Hardcore hardcore, String name) {
		super(hardcore, name);
		this.desc = "Keep your experience levels on death!";
		this.cost = 2;
		this.family = "KP";
		this.tier = 1;
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		PlayerData data = hardcore.pm.getPlayerData(player);
		if(data == null)
			return;
		
		if(!data.hasPerk(this.name))
			return;
		
		event.setDroppedExp(0);
		event.setKeepLevel(true);
	}
	
	

}
