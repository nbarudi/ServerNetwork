package ca.bungo.hardcore.skills.Speed;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ca.bungo.hardcore.hardcore.Hardcore;
import ca.bungo.hardcore.skills.Skill;
import ca.bungo.hardcore.util.player.PlayerData;

public class Speed3 extends Skill {

	public Speed3(Hardcore hardcore, String name) {
		super(hardcore, name);
		this.cost = 7;
		this.desc = "Gain a costant Speed 3 effect!";
		this.tier = 3;
		this.family = "SPD";
		this.requires = "Speed 2";
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		PlayerData data = hardcore.pm.getPlayerData(player);
		if(data == null)
			return;
		if(!data.hasPerk(this.name) || data.hasHigherPerk(this))
			return;
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999999, 2));
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		PlayerData data = hardcore.pm.getPlayerData(player);
		if(data == null)
			return;
		
		if(!data.hasPerk(this.name) || data.hasHigherPerk(this))
			return;
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999999, 2));
	}
	
	@Override
	public boolean purchaseSkill(PlayerData data) {
		Bukkit.getPlayer(UUID.fromString(data.uuid)).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999999, 2));
		return super.purchaseSkill(data);
	}

}
