package ca.bungo.hardcore.skills.HealthRegeneration;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import ca.bungo.hardcore.hardcore.Hardcore;
import ca.bungo.hardcore.skills.Skill;
import ca.bungo.hardcore.util.player.PlayerData;

public class Regen2 extends Skill {

	public Regen2(Hardcore hardcore, String name) {
		super(hardcore, name);
		this.cost = 10;
		this.desc = "Gives Infinite Hunger";
		this.tier = 2;
		this.family = "HRG";
	}
	
	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent event) {
		Player player = (Player) event.getEntity();
		PlayerData data = hardcore.pm.getPlayerData(player);
		if(data == null)
			return;
		if(!data.hasPerk(this.name) || data.hasHigherPerk(this))
			return;
		
		event.setFoodLevel(20);
		player.setSaturation(20);
	}

}
