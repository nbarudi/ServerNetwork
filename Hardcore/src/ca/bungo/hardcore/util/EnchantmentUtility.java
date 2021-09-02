package ca.bungo.hardcore.util;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class EnchantmentUtility {
	
	public static boolean enchantItem(ItemStack item, ItemStack enchantBook) {
		boolean retVal = false;
		if(item.getType().equals(Material.ENCHANTED_BOOK)) {
			Map<Enchantment, Integer> enchants = enchantBook.getEnchantments();
			for(Enchantment ench : enchants.keySet()) {
				if(item.containsEnchantment(ench))
					if(item.getEnchantmentLevel(ench) >= enchants.get(ench))
						continue;
				else {
					item.addUnsafeEnchantment(ench, enchants.get(ench));
					retVal = true;
				}
			}
		}else {
			Map<Enchantment, Integer> enchants = enchantBook.getEnchantments();
			System.out.println(enchants.size());
			for(Enchantment ench : enchants.keySet()) {
				if(item.containsEnchantment(ench)) {
					if(item.getEnchantmentLevel(ench) > enchants.get(ench))
						continue;
					else if (item.getEnchantmentLevel(ench) == enchants.get(ench))
						continue;
				}
				else {
					item.addUnsafeEnchantment(ench, enchants.get(ench));
					retVal = true;
				}
					
			}
		}
		return retVal;
	}
	

}
