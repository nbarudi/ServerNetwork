package ca.bungo.hardcore.skills;

import org.bukkit.event.Listener;

import ca.bungo.hardcore.hardcore.Hardcore;
import ca.bungo.hardcore.util.player.PlayerData;

public class Skill implements Listener {
	
	public String name;
	public String desc;
	public String requires = "";
	public int cost = 0;
	public String family = "";
	public int tier = 0;
	
	public Hardcore hardcore;
	
	public Skill(Hardcore hardcore, String name) {
		this.hardcore = hardcore;
		this.name = name;
	}
	
	public boolean purchaseSkill(PlayerData data) {
		if(!requires.isEmpty() && !data.hasPerk(requires))
			return false;
		return data.spendSkillPoints(cost);
	}
	

}
