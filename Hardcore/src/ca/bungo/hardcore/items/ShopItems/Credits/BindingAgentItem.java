package ca.bungo.hardcore.items.ShopItems.Credits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;

import ca.bungo.hardcore.hardcore.Hardcore;
import ca.bungo.hardcore.items.CustomItem;
import ca.bungo.hardcore.util.managers.ItemManager.ShopType;
import net.md_5.bungee.api.ChatColor;

public class BindingAgentItem extends CustomItem {

	public BindingAgentItem(Hardcore hardcore, String name, Material item) {
		super(hardcore, name, item);
		
		ItemMeta im = this.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&aBinding Agent"));
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&eUsed in Crafting"));
		im.setLore(lore);
		this.setItemMeta(im);
		
		this.isBuyable = true;
		
		this.st = ShopType.CREDITS;
		this.cost = 5000;
		this.description = "&7A powerful binding material used for fusing magical items together!";
		this.shopName = "Binding Agent";
		this.requiresLevel = true;
		this.reqLevel = 10;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if(event.getPlayer().getInventory().getItemInMainHand() != null 
				&& event.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null 
				&& event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName() != null 
				&& event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(this.getItemMeta().getDisplayName()))
			if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
				event.setCancelled(true);
	}

}
