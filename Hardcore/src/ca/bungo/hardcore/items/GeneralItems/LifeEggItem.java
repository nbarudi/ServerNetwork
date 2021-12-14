package ca.bungo.hardcore.items.GeneralItems;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import ca.bungo.hardcore.hardcore.Hardcore;
import ca.bungo.hardcore.items.CustomItem;
import net.md_5.bungee.api.ChatColor;

public class LifeEggItem extends CustomItem {

	public LifeEggItem(Hardcore hardcore, String name, Material item) {
		super(hardcore, name, item);
		
		ItemMeta im = this.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&aLife Egg"));
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eUsed in Crafting"));
		im.setLore(lore);
		this.setItemMeta(im);
		this.setAmount(1);
		
		hardcore.nameKeys.add("lifeegg");
		ShapedRecipe sr = new ShapedRecipe(new NamespacedKey(hardcore, "lifeegg"), this);
		sr.shape("PWC", 
				 "WEW", 
				 "CWP");
		
		sr.setIngredient('P', Material.POTATO);
		sr.setIngredient('C', Material.CARROT);
		sr.setIngredient('W', Material.WHEAT);
		sr.setIngredient('E', Material.EGG);
		
		Bukkit.getServer().addRecipe(sr);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if(event.getPlayer().getInventory().getItemInMainHand() != null 
				&& event.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null 
				&& event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName() != null 
				&& event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(this.getItemMeta().getDisplayName()))
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			event.setCancelled(true);
	}

}
