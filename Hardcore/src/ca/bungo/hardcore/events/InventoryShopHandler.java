package ca.bungo.hardcore.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ca.bungo.hardcore.hardcore.Hardcore;
import ca.bungo.hardcore.skills.Skill;
import ca.bungo.hardcore.util.ItemUtility;
import ca.bungo.hardcore.util.managers.SkillManager;
import ca.bungo.hardcore.util.player.PlayerData;
import net.md_5.bungee.api.ChatColor;

public class InventoryShopHandler implements Listener {
	
	Hardcore hardcore;
	
	public static HashMap<String, Integer> pageNumbers = new HashMap<>();
	
	public InventoryShopHandler(Hardcore hardcore) {
		this.hardcore = hardcore;
	}
	
	@EventHandler
	public void onInteract(InventoryClickEvent event) {
		if(event.getInventory() == null || event.getCurrentItem() == null)
			return;
		
		String name = event.getView().getTitle();
		Player player = (Player) event.getWhoClicked();
		
		PlayerData data = hardcore.pm.getPlayerData(player);
		
		if(!name.contains("Skill Points: " + data.skillPoints))
			return;
		
		//The player is using the shop menu
		event.setCancelled(true);
		ItemStack item = event.getCurrentItem();
		if(item == null || item.getItemMeta() == null || item.getItemMeta().getDisplayName() == null)
			return;
		
		String itemName = item.getItemMeta().getDisplayName().toString().substring(2);
		
		if(itemName.equalsIgnoreCase("Next Page")) {
			player.closeInventory();
			Inventory inv = createInventoryMenu(data, pageNumbers.get(data.username) + 1);
			player.openInventory(inv);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Perks> &7Opening next page!"));
			return;
		}
		
		Skill skill = SkillManager.getSkill(itemName);
		if(skill == null)
			return;
		if(skill.purchaseSkill(data)) {
			data.ownedPerks.add(skill.name);
			player.closeInventory();
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Perks> &7You have purchased the Skill &e" + skill.name + "&7!"));
			return;
		}else {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Perks> &7Failed to purchase skill &e" + skill.name + "&7!"));
		}
			
	}
	
	public static Inventory createInventoryMenu(PlayerData data, int page) {
		
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', "&aSkill Points: " + data.skillPoints));
		
		if(!data.hasPerk("Unlock")) {
			inv.setItem(0, ItemUtility.createItem("&4" + SkillManager.getSkill("Unlock").name, Material.REDSTONE_BLOCK, "&7" + SkillManager.getSkill("Unlock").desc));
			return inv;
		}
		
		pageNumbers.put(data.username, page);
		
		int index = page*9;
		
		int slot = 1;
		int temp = 1;
		
		for(int i = index; i < SkillManager.families.size(); i++) {
			String family = SkillManager.families.get(i);
			if(i >= index+9)
				break;
			
			List<String> descriptions = new ArrayList<String>();
			String name = "";
			Material mat = Material.REDSTONE_BLOCK;
			for(Skill skill : SkillManager.getSkillFamily(family)) {
				
				if(!data.hasPerk(skill.name) && skill.tier == 1) {
					name = skill.name;
					descriptions.add("&7Tier " + skill.tier + ": " + skill.desc + " &eCost: " + skill.cost);
					mat = Material.REDSTONE_BLOCK;
				}
				else if(!data.hasPerk(skill.name) && (!skill.requires.isEmpty() && data.hasPerk(skill.requires))) {
					name = skill.name;
					descriptions.add("&7Tier " + skill.tier + ": " + skill.desc + " &eCost: " + skill.cost);
				}
				else if(!data.hasPerk(skill.name) && (!skill.requires.isEmpty() && !data.hasPerk(skill.requires))) {
					descriptions.add("&7Tier " + skill.tier + ": " + skill.desc + " &eCost: " + skill.cost);
				}
				else if(!data.hasPerk(skill.name)) {
					name = skill.name;
					descriptions.add("&7Tier " + skill.tier + ": " + skill.desc + " &eCost: " + skill.cost);
				}
				else {
					descriptions.add("&aTier " + skill.tier + ": " + skill.desc + " &eCost: " + skill.cost);
					mat = Material.GREEN_CONCRETE;
				}
			}
			
			if(name.isEmpty()) {
				name = "Completed!";
				mat = Material.EMERALD_BLOCK;
			}
			
			ItemStack item;
			if(SkillManager.getSkillFamily(family).size() == 0) {
				item = ItemUtility.createItem("&7Coming Soon!", Material.BEDROCK, "&eThis Skill Tree is not yet active!", "&8Code-Name: " + family);
			}else {
				item = ItemUtility.createItem("&7" + name, mat, descriptions);
			}
			
			
			inv.setItem(slot, item);
			
			if(temp >= 3) {
				temp = 0;
				slot += 9;
			}
			slot+=3;
			temp++;
			
		}
		
/*
		for(String family : SkillManager.families) {
			if(SkillManager.families.size() > 9 && page <= Math.floor(SkillManager.families.size() / 9))
				break;
			List<String> descriptions = new ArrayList<String>();
			String name = "";
			Material mat = Material.REDSTONE_BLOCK;
			for(Skill skill : SkillManager.getSkillFamily(family)) {
				
				if(!data.hasPerk(skill.name) && skill.tier == 1) {
					name = skill.name;
					descriptions.add("&7Tier " + skill.tier + ": " + skill.desc + " &eCost: " + skill.cost);
					mat = Material.REDSTONE_BLOCK;
				}
				else if(!data.hasPerk(skill.name) && (!skill.requires.isEmpty() && data.hasPerk(skill.requires))) {
					name = skill.name;
					descriptions.add("&7Tier " + skill.tier + ": " + skill.desc + " &eCost: " + skill.cost);
				}
				else if(!data.hasPerk(skill.name) && (!skill.requires.isEmpty() && !data.hasPerk(skill.requires))) {
					descriptions.add("&7Tier " + skill.tier + ": " + skill.desc + " &eCost: " + skill.cost);
				}
				else if(!data.hasPerk(skill.name)) {
					name = skill.name;
					descriptions.add("&7Tier " + skill.tier + ": " + skill.desc + " &eCost: " + skill.cost);
				}
				else {
					descriptions.add("&aTier " + skill.tier + ": " + skill.desc + " &eCost: " + skill.cost);
					mat = Material.GREEN_CONCRETE;
				}
			}
			
			if(name.isEmpty()) {
				name = "Completed!";
				mat = Material.EMERALD_BLOCK;
			}
				
			
			ItemStack item = ItemUtility.createItem("&7" + name, mat, descriptions);
			
			if(((index*3)+1 % 9) == 7 )
				index += 3;
			inv.setItem((index*3)+1, item);
			index++;
		}
		*/
		ItemStack nPage = ItemUtility.createItem("&bNext Page", Material.PAPER, "&7Click to go to the next page of perks!");
		
		if(SkillManager.families.size() > 9 && page < Math.floor(SkillManager.families.size() / 9))
			inv.setItem(49, nPage);
			
		return inv;
	}

}
