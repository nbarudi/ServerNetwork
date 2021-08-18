package ca.bungo.hardcore.skills;

import ca.bungo.hardcore.hardcore.Hardcore;

public class Unlock extends Skill {

	public Unlock(Hardcore hardcore, String name) {
		super(hardcore, name);
		this.desc = "Unlock the skill tree!";
		this.cost = 1;
	}

}
