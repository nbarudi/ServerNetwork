package ca.bungo.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RankUtilities {
	
	private static boolean isInitialized = false;
	
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
	
	public static HashMap<String, List<String>> permissionNodes = new HashMap<String, List<String>>();
	
	public static void initPermissionNodes() {
		if(isInitialized)
			return;
		isInitialized = true;
		
		userNodes();
		userPNodes();
		userPPNodes();
		vipNodes();
		vipPNodes();
		vipPPNodes();
		trainee();
		jrmod();
		mod();
		srmod();
		jradmin();
		admin();
		sradmin();
		headadmin();
		manager();
		developer();
		communitylead();
		owner();
	}
	
	private static void userNodes() {
		List<String> pN = new ArrayList<String>();
		pN.add("core.user");
		permissionNodes.put("user", pN);
	}
	
	private static void userPNodes() {
		List<String> pN = new ArrayList<String>();
		pN.add("core.userp");
		pN.add("core.lvlignore10");
		permissionNodes.put("user+", pN);
	}
	
	private static void userPPNodes() {
		List<String> pN = new ArrayList<String>();
		pN.add("core.userpp");
		pN.add("core.lvlignore20");
		permissionNodes.put("user++", pN);
	}
	
	private static void vipNodes() {
		List<String> pN = new ArrayList<String>();
		pN.add("core.vip");
		pN.add("core.lvlignore30");
		permissionNodes.put("vip", pN);
	}
	
	private static void vipPNodes() {
		List<String> pN = new ArrayList<String>();
		pN.add("core.vipp");
		pN.add("core.lvlignore40");
		permissionNodes.put("vip+", pN);
	}
	
	private static void vipPPNodes() {
		List<String> pN = new ArrayList<String>();
		pN.add("core.vippp");
		pN.add("core.lvlignore50");
		permissionNodes.put("vip++", pN);
	}
	
	private static void trainee() {
		List<String> pN = new ArrayList<String>();
		pN.add("core.trainee");
		pN.add("core.lvlignore10");
		permissionNodes.put("trainee", pN);
	}
	
	private static void jrmod() {
		List<String> pN = new ArrayList<String>();
		pN.add("core.jrmod");
		pN.add("core.lvlignore15");
		permissionNodes.put("jrmod", pN);
	}
	
	private static void mod() {
		List<String> pN = new ArrayList<String>();
		pN.add("core.mod");
		pN.add("core.lvlignore20");
		permissionNodes.put("mod", pN);
	}
	
	private static void srmod() {
		List<String> pN = new ArrayList<String>();
		pN.add("core.srmod");
		pN.add("core.lvlignore25");
		permissionNodes.put("srmod", pN);
	}
	
	private static void jradmin() {
		List<String> pN = new ArrayList<String>();
		pN.add("core.jradmin");
		pN.add("core.lvlignore30");
		permissionNodes.put("jradmin", pN);
	}
	
	private static void admin() {
		List<String> pN = new ArrayList<String>();
		pN.add("core.admin");
		pN.add("core.lvlignore35");
		permissionNodes.put("admin", pN);
	}
	
	private static void sradmin() {
		List<String> pN = new ArrayList<String>();
		pN.add("core.sradmin");
		pN.add("core.lvlignore40");
		permissionNodes.put("sradmin", pN);
	}
	
	private static void headadmin() {
		List<String> pN = new ArrayList<String>();
		pN.add("core.headadmin");
		pN.add("core.lvlignore50");
		permissionNodes.put("headadmin", pN);
	}
	
	private static void manager() {
		List<String> pN = new ArrayList<String>();
		pN.add("core.manager");
		pN.add("core.lvlignore100");
		permissionNodes.put("manager", pN);
	}
	
	private static void developer() {
		List<String> pN = new ArrayList<String>();
		pN.add("core.developer");
		pN.add("core.lvlignore100");
		pN.add("bukkit.command.reload");
		permissionNodes.put("developer", pN);
	}

	private static void communitylead() {
		List<String> pN = new ArrayList<String>();
		pN.add("core.communitylead");
		pN.add("core.lvlignore100");
		permissionNodes.put("communitylead", pN);
	}
	
	private static void owner() {
		List<String> pN = new ArrayList<String>();
		pN.add("core.owner");
		pN.add("core.lvlignore100");
		permissionNodes.put("owner", pN);
	}
}
