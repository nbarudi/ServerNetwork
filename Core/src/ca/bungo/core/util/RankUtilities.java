package ca.bungo.core.util;

import java.util.HashMap;

public class RankUtilities {
	
	public static HashMap<String, String> ranks = new HashMap<String, String>(){
		private static final long serialVersionUID = -4046030639359077986L;

	{
		put("user", "&7&lUser");
		put("user+", "&7&lUser+");
		put("user++", "&7&lUser++");
		put("vip", "&5&lVip");
		put("vip+", "&5&lVip+");
		put("vip++", "&5&lVip++");
		put("trainee", "&6&lTrainee");
		put("jrmod", "&a&lJr.Mod");
		put("mod", "&a&lMod");
		put("srmod", "&a&lSr.Mod");
		put("jradmin", "&c&lJr.Admin");
		put("admin", "&c&lAdmin");
		put("sradmin", "&c&lSr.Admin");
		put("headadmin", "&4&lHead-Admin");
		put("manager", "&9&lManager");
		put("developer", "&9&lDeveloper");
		put("communitylead", "&d&lCL");
		put("owner", "&4&lOwner");
		put("console", "&0&lConsole");
	}};

}
