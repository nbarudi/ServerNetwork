package ca.bungo.hardcore.hardcore;

import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ca.bungo.core.api.CoreAPI;
import ca.bungo.core.api.EconomyAPI;
import ca.bungo.core.api.PermissionsAPI;
import ca.bungo.core.core.Core;
import ca.bungo.hardcore.cmds.BountyCommand;
import ca.bungo.hardcore.cmds.ItemsCommand;
import ca.bungo.hardcore.cmds.PerksCommand;
import ca.bungo.hardcore.cmds.ReviveCommand;
import ca.bungo.hardcore.cmds.ShopCommand;
import ca.bungo.hardcore.events.InventoryPerkHandler;
import ca.bungo.hardcore.events.InventoryShopHandler;
import ca.bungo.hardcore.events.LevelingEvents;
import ca.bungo.hardcore.events.PlayerEvents;
import ca.bungo.hardcore.items.CustomItem;
import ca.bungo.hardcore.items.GeneralItems.LifeEggItem;
import ca.bungo.hardcore.items.ShopItems.Credits.BindingAgentItem;
import ca.bungo.hardcore.items.ShopItems.Credits.ExtraClaimsItem;
import ca.bungo.hardcore.items.ShopItems.SP.ExtraLifeItem;
import ca.bungo.hardcore.skills.Skill;
import ca.bungo.hardcore.skills.Unlock;
import ca.bungo.hardcore.skills.Autosmelt.AutoSmelt1;
import ca.bungo.hardcore.skills.Autosmelt.AutoSmelt2;
import ca.bungo.hardcore.skills.Autosmelt.AutoSmelt3;
import ca.bungo.hardcore.skills.Autosmelt.AutoSmelt4;
import ca.bungo.hardcore.skills.Autosmelt.AutoSmelt5;
import ca.bungo.hardcore.skills.HealthRegeneration.Regen1;
import ca.bungo.hardcore.skills.Keep.KeepInventory;
import ca.bungo.hardcore.skills.Keep.KeepLevels;
import ca.bungo.hardcore.util.managers.ItemManager;
import ca.bungo.hardcore.util.managers.PlayerManager;



public class Hardcore extends JavaPlugin {
	
	//API Systems
	public Core core;
	public CoreAPI cAPI;
	public PermissionsAPI pAPI;
	public EconomyAPI eAPI;
	
	//Hardcore Systems
	public PlayerManager pm;
	public ItemManager itm;
	
	public ArrayList<Skill> skills = new ArrayList<Skill>();
	
	public static Hardcore hardcore;
	
	@Override
	public void onEnable() {
		//Setting up API
		core = (Core) Bukkit.getPluginManager().getPlugin("Core");
		if(core == null) {
			Bukkit.getLogger().log(Level.SEVERE, "Core plugin not availible...");
			return;
		}
		
		hardcore = this;
		
		cAPI = new CoreAPI(core);
		pAPI = new PermissionsAPI(core);
		eAPI = new EconomyAPI(core);
		
		pm = new PlayerManager(this);
		itm = new ItemManager(this);
		
		registerConfigs();
		registerItems();
		registerCommands();
		registerSkills();
		registerEvents();
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			pm.createPlayerData(player);
		}
	}
	
	private void registerItems() {
		//Normal Items
		itm.addItem(new LifeEggItem(this, "Life Egg", Material.VILLAGER_SPAWN_EGG));
		
		//Credit Items
		itm.addItem(new BindingAgentItem(this, "Binding Agent", Material.SNOWBALL));
		itm.addItem(new ExtraClaimsItem(this, "Extra Claims", Material.GRASS_BLOCK));
		
		//Skill-Point Items
		itm.addItem(new ExtraLifeItem(this, "Extra Life", Material.TOTEM_OF_UNDYING));
	}
	
	private void registerSkills() {
		
		skills.add(new Unlock(this, "Unlock"));
		
		//Keep Family
		skills.add(new KeepLevels(this, "Keep Levels"));
		skills.add(new KeepInventory(this, "Keep Inventory"));
		
		//AutoSmelt Family
		skills.add(new AutoSmelt1(this, "AutoSmelt 1"));
		skills.add(new AutoSmelt2(this, "AutoSmelt 2"));
		skills.add(new AutoSmelt3(this, "AutoSmelt 3"));
		skills.add(new AutoSmelt4(this, "AutoSmelt 4"));
		skills.add(new AutoSmelt5(this, "AutoSmelt 5"));
		
		//Haste Family
		//skills.add(new Haste1(this, "Haste 1"));
		//skills.add(new Haste2(this, "Haste 2"));
		//skills.add(new Haste3(this, "Haste 3"));
		
		/*Speed Family
		 * skills.add(new Speed1(this, "Speed 1"));
		 * skills.add(new Speed2(this, "Speed 2"));
		 */
		
		/*Resistance Family
		 * skills.add(new Resistance1(this, "Resistance 1"));
		 * skills.add(new Resistance2(this, "Resistance 2"));
		 * skills.add(new Resistance3(this, "Resistance 3"));
		 */
		
		//Health Regeneration Family
		skills.add(new Regen1(this, "Health Regeneration"));
		//skills.add(new Regen2(this, "Perfect Hunger"));
	}
	
	private void registerCommands() {
		core.coreCommands.add(new ReviveCommand(core, "Revive"));
		core.coreCommands.add(new PerksCommand(core, "Perks"));
		core.coreCommands.add(new ShopCommand(core, "Shop"));
		core.coreCommands.add(new ItemsCommand(core, "Items"));
		core.coreCommands.add(new BountyCommand(core, "Bounty"));
		
		core.reregisterCommands(this);
	}
	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvents(new PlayerEvents(this), this);
		pm.registerEvents(new LevelingEvents(this), this);
		pm.registerEvents(new InventoryPerkHandler(this), this);
		pm.registerEvents(new InventoryShopHandler(this), this);
		
		for(Skill s : skills) {
			pm.registerEvents(s, this);
		}
		
		for(CustomItem ci : itm.getAllItems()) {
			pm.registerEvents(ci, this);
		}
		
	}
	private void registerConfigs() {
		saveDefaultConfig();
	}

}
