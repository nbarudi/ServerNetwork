package ca.bungo.hardcore.items.GeneralItems;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import ca.bungo.hardcore.hardcore.Hardcore;
import ca.bungo.hardcore.items.CustomItem;

public class MinersIntuition3 extends CustomItem{
	
	public MinersIntuition3(Hardcore hardcore, String name, Material item) {
		super(hardcore, name, item);
		
		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Miners Intuition (Tier 3)");
		meta.addEnchant(Enchantment.ARROW_INFINITE, -1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eUse your miners intuition to find ores! &7(20x20 Area)"));
		meta.setLore(lore);
		this.setItemMeta(meta);
		
		
		ShapedRecipe sr = new ShapedRecipe(new NamespacedKey(hardcore, "minersintuition3"), this);
		sr.shape("BHB", 
				 "HCH", 
				 "BHB");
		sr.setIngredient('B', new RecipeChoice.ExactChoice(hardcore.itm.getItem("Binding Agent")));
		sr.setIngredient('H', new RecipeChoice.ExactChoice(hardcore.itm.getItem("Charged Heart")));
		sr.setIngredient('C', new RecipeChoice.ExactChoice(hardcore.itm.getItem("Miners Intuition 2")));
		
		Bukkit.addRecipe(sr);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if(event.getPlayer().getInventory().getItemInMainHand() != null 
				&& event.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null 
				&& event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName() != null 
				&& event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(this.getItemMeta().getDisplayName()))
			if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				event.setCancelled(true);
				if(hardcore.cm.onCooldown(player, "MinersIntuition"))
					return;
				
				Location pLoc = player.getLocation();
				
				for(int x = -10; x < 11; x++)
					for(int y = -10; y < 11; y++)
						for(int z = -10; z < 11; z++) {
							Location loc = new Location(pLoc.getWorld(), pLoc.getBlockX() + x + 0.5, pLoc.getBlockY() + y + 0.5, pLoc.getBlockZ() + z + 0.5);
							Hardcore.hardcore.bu.glowBlockNMS(player, loc);
						}
				
				hardcore.cm.giveCooldown(player, "MinersIntuition", 12);
				
			}
		
	}
	
	

}
