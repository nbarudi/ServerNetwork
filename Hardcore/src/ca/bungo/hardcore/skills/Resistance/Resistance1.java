package ca.bungo.hardcore.skills.Resistance;

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

public class Resistance1 extends Skill {

	public Resistance1(Hardcore hardcore, String name) {
		super(hardcore, name);
		this.cost = 3;
		this.desc = "Gain a costant Resistance 1 effect!";
		this.tier = 1;
		this.family = "RST";
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		PlayerData data = hardcore.pm.getPlayerData(player);
		if(data == null)
			return;
		if(!data.hasPerk(this.name) || data.hasHigherPerk(this))
			return;
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 99999999, 0));
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		PlayerData data = hardcore.pm.getPlayerData(player);
		if(data == null)
			return;
		
		if(!data.hasPerk(this.name) || data.hasHigherPerk(this))
			return;
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 99999999, 0));
	}

	@Override
	public boolean purchaseSkill(PlayerData data) {
		Bukkit.getPlayer(UUID.fromString(data.uuid)).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 99999999, 0));
		return super.purchaseSkill(data);
	}
}
