package ca.bungo.hardcore.items.ShopItems.SP;

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
import ca.bungo.hardcore.util.managers.ItemManager.ShopType;

public class MinersIntuition1 extends CustomItem{
	
	public MinersIntuition1(Hardcore hardcore, String name, Material item) {
		super(hardcore, name, item);
		this.description = "&eUse your miners intuition to find ores! &7(7x7 Area)";
		this.shopName = name;
		this.isBuyable = true;
		this.st = ShopType.SP;
		this.cost = 10;
		this.reqLevel = 20;
		this.requiresLevel = true;
		
		
		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Miners Intuition");
		meta.addEnchant(Enchantment.ARROW_INFINITE, -1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		this.setItemMeta(meta);
		
		
		ShapedRecipe sr = new ShapedRecipe(new NamespacedKey(hardcore, "minersintuition1"), this);
		sr.shape("BDB", 
				 "ICG", 
				 "BEB");
		sr.setIngredient('B', new RecipeChoice.ExactChoice(hardcore.itm.getItem("Binding Agent")));
		sr.setIngredient('D', Material.DIAMOND_BLOCK);
		sr.setIngredient('I', Material.IRON_BLOCK);
		sr.setIngredient('G', Material.GOLD_BLOCK);
		sr.setIngredient('E', Material.EMERALD_BLOCK);
		sr.setIngredient('C', Material.COMPASS);
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
				
				for(int x = -3; x < 4; x++)
					for(int y = -3; y < 4; y++)
						for(int z = -3; z < 4; z++) {
							Location loc = new Location(pLoc.getWorld(), pLoc.getBlockX() + x + 0.5, pLoc.getBlockY() + y + 0.5, pLoc.getBlockZ() + z + 0.5);
							Hardcore.hardcore.bu.glowBlockNMS(player, loc);
						}
				
				hardcore.cm.giveCooldown(player, "MinersIntuition", 15);
				
			}
		
	}
	
	

}
