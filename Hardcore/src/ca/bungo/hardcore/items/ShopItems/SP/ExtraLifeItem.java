package ca.bungo.hardcore.items.ShopItems.SP;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import ca.bungo.hardcore.hardcore.Hardcore;
import ca.bungo.hardcore.items.CustomItem;
import ca.bungo.hardcore.util.managers.ItemManager.ShopType;
import ca.bungo.hardcore.util.player.PlayerData;
import net.md_5.bungee.api.ChatColor;

public class ExtraLifeItem extends CustomItem {

	public ExtraLifeItem(Hardcore hardcore, String name, Material item) {
		super(hardcore, name, item);

		ItemMeta im = this.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&9+1 Life"));
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eRight-Click to Gain 1 Life"));
		im.setLore(lore);
		this.setItemMeta(im);
		
		this.isBuyable = true;
		
		this.st = ShopType.SP;
		this.cost = 3;
		this.description = "&eRight-Click to Gain 1 Life";
		this.shopName = "Extra Life";
		this.requiresLevel = true;
		this.reqLevel = 5;
		
		ShapedRecipe sr = new ShapedRecipe(new NamespacedKey(hardcore, "extralife"), this);
		
		sr.shape("BGB", 
				 "DLD", 
				 "BGB");
		
		sr.setIngredient('B', new RecipeChoice.ExactChoice(hardcore.itm.getItem("Binding Agent")));
		sr.setIngredient('L', new RecipeChoice.ExactChoice(hardcore.itm.getItem("Life Egg")));
		sr.setIngredient('G', Material.GOLD_INGOT);
		sr.setIngredient('D', Material.DIAMOND);
		
		Bukkit.getServer().addRecipe(sr);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if(event.getPlayer().getInventory().getItemInMainHand() != null 
				&& event.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null 
				&& event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName() != null 
				&& event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(this.getItemMeta().getDisplayName()))
			if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				Player player = event.getPlayer();
				PlayerData data = hardcore.pm.getPlayerData(player);
				
				data.lives++;
				hardcore.pm.savePlayerData(data);
				
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Lives> &7You have gained an extra life! You currently have: &e" + data.lives));
			}
	}

}
