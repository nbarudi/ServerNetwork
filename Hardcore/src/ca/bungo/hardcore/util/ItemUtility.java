package ca.bungo.hardcore.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class ItemUtility {
	
	public static ItemStack createItem(String name, Material material, String ... lore) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		
		List<String> iLore = new ArrayList<String>();
		for(String s : lore) {
			iLore.add(ChatColor.translateAlternateColorCodes('&', s));
		}
		
		meta.setLore(iLore);
		
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack createItem(String name, Material material, List<String> lore) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		
		List<String> iLore = new ArrayList<String>();
		for(String s : lore) {
			iLore.add(ChatColor.translateAlternateColorCodes('&', s));
		}
		
		meta.setLore(iLore);
		
		item.setItemMeta(meta);
		return item;
	}

}
