package ca.bungo.hardcore.skills.HealthRegeneration;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import ca.bungo.hardcore.hardcore.Hardcore;
import ca.bungo.hardcore.skills.Skill;
import ca.bungo.hardcore.util.player.PlayerData;

public class Regen1 extends Skill {

	public Regen1(Hardcore hardcore, String name) {
		super(hardcore, name);
		this.cost = 2;
		this.desc = "Enable Health Regeneration";
		this.tier = 1;
		this.family = "HRG";
	}
	
	@EventHandler
	public void onRegen(EntityRegainHealthEvent event) {
		if(!(event.getEntity() instanceof Player))
			return;
		Player player = (Player) event.getEntity();
		PlayerData data = hardcore.pm.getPlayerData(player);
		event.setCancelled(true);
		if(!data.hasPerk(this.name) || data.hasHigherPerk(this))
			return;
		event.setCancelled(false);
		
	}

}
