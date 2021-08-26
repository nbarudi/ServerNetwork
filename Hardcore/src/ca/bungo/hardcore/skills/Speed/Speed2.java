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

public class Speed2 extends Skill {

	public Speed2(Hardcore hardcore, String name) {
		super(hardcore, name);
		this.cost = 5;
		this.desc = "Gain a costant Speed 2 effect!";
		this.tier = 2;
		this.family = "SPD";
		this.requires = "Speed 1";
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		PlayerData data = hardcore.pm.getPlayerData(player);
		if(data == null)
			return;
		if(!data.hasPerk(this.name) || data.hasHigherPerk(this))
			return;
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999999, 1));
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		PlayerData data = hardcore.pm.getPlayerData(player);
		if(data == null)
			return;
		
		if(!data.hasPerk(this.name) || data.hasHigherPerk(this))
			return;
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999999, 1));
	}

	@Override
	public boolean purchaseSkill(PlayerData data) {
		Bukkit.getPlayer(UUID.fromString(data.uuid)).addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999999, 1));
		return super.purchaseSkill(data);
	}
}
