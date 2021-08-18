package ca.bungo.hardcore.skills.Keep;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import ca.bungo.hardcore.hardcore.Hardcore;
import ca.bungo.hardcore.skills.Skill;
import ca.bungo.hardcore.util.player.PlayerData;

public class KeepInventory extends Skill {

	public KeepInventory(Hardcore hardcore, String name) {
		super(hardcore, name);
		this.desc = "Keep your inventory on death!";
		this.requires = "Keep Levels";
		this.cost = 4;
		this.family = "KP";
		this.tier = 2;
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		PlayerData data = hardcore.pm.getPlayerData(player);
		
		if(!data.hasPerk(this.name))
			return;
		
		event.getDrops().clear();
		event.setKeepInventory(true);
	}

}
