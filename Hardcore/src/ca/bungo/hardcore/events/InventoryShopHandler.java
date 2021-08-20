package ca.bungo.hardcore.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ca.bungo.hardcore.hardcore.Hardcore;
import ca.bungo.hardcore.util.managers.ItemManager.ShopType;
import ca.bungo.hardcore.util.player.PlayerData;
import net.md_5.bungee.api.ChatColor;

public class InventoryShopHandler implements Listener {
	
	private Hardcore hardcore;
	
	public InventoryShopHandler(Hardcore hardcore) {
		this.hardcore = hardcore;
	}
	
	@EventHandler
	public void onInvInteract(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		PlayerData data = hardcore.pm.getPlayerData(player);
		
		if(event.getClickedInventory() == null)
			return;
		
		if(!event.getClickedInventory().equals(hardcore.pm.playerOpened.get(player.getName())))
			return;
		
		event.setCancelled(true);
	}
	
	public static Inventory openInventory(PlayerData data, ShopType type) {
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', "&eShop Menu"));
		
		if(type == null) {
			ItemStack spShop = new ItemStack(Material.TOTEM_OF_UNDYING);
			ItemMeta spim = spShop.getItemMeta();
			spim.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9Skill-Point Shop"));
			List<String> splore = new ArrayList<String>();
			splore.add(ChatColor.translateAlternateColorCodes('&', "&eAll items in this shop require Skill Points to purchase!"));
			spim.setLore(splore);
			spShop.setItemMeta(spim);
			
			inv.setItem(20, spShop);
			
			ItemStack cShop = new ItemStack(Material.EMERALD);
			ItemMeta cim = cShop.getItemMeta();
			cim.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9Credit Shop"));
			List<String> clore = new ArrayList<String>();
			clore.add(ChatColor.translateAlternateColorCodes('&', "&eAll items in this shop require Credits to purchase!"));
			cim.setLore(clore);
			cShop.setItemMeta(cim);
			
			inv.setItem(24, cShop);
		}else {
			
		}
		
		Hardcore.hardcore.pm.playerOpened.put(data.username, inv);
		
		return inv;
	}

}
