package ca.bungo.hardcore.util.player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ca.bungo.core.api.cAPI.CoreAPIAbstract.PlayerInfo;
import ca.bungo.hardcore.hardcore.Hardcore;
import ca.bungo.hardcore.skills.Skill;
import ca.bungo.hardcore.util.Cooldown;
import ca.bungo.hardcore.util.managers.SkillManager;

public class PlayerData {
	
	public int skillPoints;
	private int maxSkillPoints;
	public int lives;
	public long reviveTime;
	public String uuid;
	public String username;
	
	public List<String> ownedPerks;
	
	public PlayerInfo pInfo;
	
	public int xpBounty = 0;
	
	private HashMap<String, Cooldown> cooldowns = new HashMap<String, Cooldown>();
	
	Hardcore hardcore;
	
	public PlayerData(Hardcore hardcore, String username, String uuid) {
		this.hardcore = hardcore;
		this.uuid = uuid;
		this.username = username;
		
		Player player = Bukkit.getPlayer(username);
		PlayerInfo pInfo = hardcore.cAPI.getPlayerInfo(player);
		this.pInfo = pInfo;
		
		maxSkillPoints = pInfo.level - 1;
		
		int spentPoints = hardcore.getConfig().getInt("Players." + uuid + ".spentPoints");
		
		int points = maxSkillPoints - spentPoints;
		skillPoints = (points < 0 ) ? 0 : points;
		
		lives = hardcore.getConfig().getInt("Players." + uuid + ".lives");
		reviveTime = hardcore.getConfig().getLong("Players." + uuid + ".reviveTime");
		
		ownedPerks = hardcore.getConfig().getStringList("Players." + uuid + ".perks");
		
		xpBounty = hardcore.getConfig().getInt("Players." + uuid + ".xpbounty");
	}
	
	
	public boolean spendSkillPoints(int points) {
		if(skillPoints < points)
			return false;
		skillPoints -= points;
		return true;
	}
	
	public boolean hasPerk(String name) {
		for(String s : ownedPerks)
			if(s.equalsIgnoreCase(name))
				return true;
		return false;
	}
	
	public boolean hasHigherPerk(Skill skill) {
		List<Skill> familyMembers = SkillManager.getSkillFamily(skill.family);
		int maxTier = skill.tier;
		for(Skill s : familyMembers) {
			if(s.tier > maxTier)
				if(hasPerk(s.name))
					maxTier = s.tier;
		}
		
		if(maxTier > skill.tier)
			return true;
		return false;
	}
	
	public void handleDeath() {
		this.lives--;
		if(this.lives <= 0) {
			this.lives = 0;
			int totalDeaths = hardcore.getConfig().getInt("Players." + uuid + ".fullDeaths");
			int hours = (totalDeaths*12) + 12;
			long seconds = (hours*60*60);
			this.reviveTime = new Date().getTime() + (seconds*1000);
			
			hardcore.getConfig().set("Players." + uuid + ".fullDeaths", totalDeaths+1);
			hardcore.saveConfig();
			
			Date date = new Date(this.reviveTime);
			
			Player player = Bukkit.getPlayer(UUID.fromString(uuid));
			StringBuilder sb = new StringBuilder();
			sb.append("&7[&aHardcore&7]\n");
			sb.append("&eSeems you have run out of lives!\n");
			sb.append("&eYou will gain 1 extra life on:\n");
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy | hh:mm:ss");
			sb.append("&3" + sdf.format(date) + "\n");
			sb.append("&eIf you do not want to wait until this day\n");
			sb.append("&eYou can purchase a revive on our website:\n");
			sb.append("&exxxxxxxx");
			player.kickPlayer(ChatColor.translateAlternateColorCodes('&', sb.toString()));
		}else {
			Bukkit.getPlayer(UUID.fromString(uuid)).sendMessage(ChatColor.translateAlternateColorCodes('&',"&7[&aHardcore&7] &eYou have died and lost 1 life! You have " + this.lives + " lives remaining!"));
		}
	}
	
	
	public void addCooldown(String name, Cooldown cooldown) {
		cooldowns.put(name, cooldown);
	}
	
	public void removeCooldown(String name) {
		cooldowns.remove(name);
	}
	
	public Cooldown getCooldown(String name) {
		Cooldown cd = null;
		cd = cooldowns.get(name);
		return cd;
	}
	
	public int getMaxSkillPoints() {
		return pInfo.level-1;
	}

}
