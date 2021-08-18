package ca.bungo.hardcore.util.managers;

import java.util.ArrayList;
import java.util.Collections;

import ca.bungo.hardcore.hardcore.Hardcore;
import ca.bungo.hardcore.skills.Skill;

public class SkillManager {
	
	public static ArrayList<String> families = new ArrayList<String>() {/**
		 * 
		 */
		private static final long serialVersionUID = -8405064827403517569L;

	{
		add("KP");
		add("ASM");
		add("HST");
		add("SPD");
		add("RST");
		add("HRG");
	}};
	
	public static Skill getSkill(String name) {
		for(Skill s : Hardcore.hardcore.skills) {
			if(s.name.equalsIgnoreCase(name))
				return s;
		}
		return null;
	}
	
	public static ArrayList<Skill> getSkillFamily(String family){
		ArrayList<Skill> skills = new ArrayList<Skill>();
		
		for(Skill skill : Hardcore.hardcore.skills) {
			if(skill.family.equalsIgnoreCase(family))
				skills.add(skill);
		}
		Collections.sort(skills, (a, b) -> a.tier < b.tier ? -1 : a.tier == b.tier ? 0 : 1);
		return skills;
	}

}
