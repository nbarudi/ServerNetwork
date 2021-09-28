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
import org.bukkit.inventory.meta.ItemMeta;

import ca.bungo.core.api.CoreAPI.PlayerInfo;
import ca.bungo.hardcore.cmds.Administration.ItemsCommand;
import ca.bungo.hardcore.hardcore.Hardcore;
import ca.bungo.hardcore.items.CustomItem;
import ca.bungo.hardcore.util.managers.ItemManager.ShopType;
import ca.bungo.hardcore.util.player.PlayerData;
import net.md_5.bungee.api.ChatColor;

public class InventoryShopHandler implements Listener {
	
	private Hardcore hardcore;
	
	private HashMap<String, ShopType> shopStorage = new HashMap<String, ShopType>();
	
	public InventoryShopHandler(Hardcore hardcore) {
		this.hardcore = hardcore;
	}
	
	@EventHandler
	public void onInvInteract(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		PlayerData data = hardcore.pm.getPlayerData(player);
		
		if(event.getClickedInventory() == null)
			return;
		
		if(event.getClickedInventory().equals(ItemsCommand.inv)) {
			ItemStack item = event.getCurrentItem();
			if(item == null || item.getItemMeta() == null || item.getItemMeta().getDisplayName() == null)
				return;
			player.getInventory().addItem(item);
			event.setCancelled(true);
			return;
		}
		
		if(!event.getClickedInventory().equals(hardcore.pm.playerOpened.get(player.getName())))
			return;
		
		event.setCancelled(true);
		ItemStack item = event.getCurrentItem();
		if(item == null || item.getItemMeta() == null || item.getItemMeta().getDisplayName() == null)
			return;
		
		String itemName = item.getItemMeta().getDisplayName();
		
		if(itemName.contains("Skill-Point Shop")) {
			player.closeInventory();
			Inventory inv = openInventory(data, ShopType.SP);
			hardcore.pm.playerOpened.put(player.getName(), inv);
			this.shopStorage.put(data.username, ShopType.SP);
			player.openInventory(inv);
		}else if(itemName.contains("Credit Shop")) {
			player.closeInventory();
			Inventory inv = openInventory(data, ShopType.CREDITS);
			hardcore.pm.playerOpened.put(player.getName(), inv);
			this.shopStorage.put(data.username, ShopType.CREDITS);
			player.openInventory(inv);
		}else {
			CustomItem ci = hardcore.itm.getItem(itemName);
			int cost = ci.cost;
			
			if(ci.st.equals(ShopType.CREDITS)) {
				if(!hardcore.eAPI.hasCredits(data.username, cost)) {
					player.closeInventory();
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Shop> &7You do not have enough credits to buy this item! Have: &e" + hardcore.eAPI.getBalance(data.username) + " &7Need: &e" + ci.cost));
					return;
				}
				
				hardcore.eAPI.withdrawPlayer(data.username, cost);
				player.getInventory().addItem(ci.clone());
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Shop> &7You have purchased: &e" + ci.shopName));
			}else if(ci.st.equals(ShopType.SP)) {
				if(data.spendSkillPoints(cost)) {
					player.getInventory().addItem(ci.clone());
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Shop> &7You have purchased: &e" + ci.shopName));
				}else {
					player.closeInventory();
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Shop> &7You do not have enough Skill Points to buy this item! Have: &e" + data.skillPoints + " &7Need: &e" + ci.cost));
					return;
				}
			}
		}
		
		
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
		}else if(type.equals(ShopType.CREDITS)) {
			List<CustomItem> itemList = Hardcore.hardcore.itm.getShopItems(type);
			List<ItemStack> shopItems = new ArrayList<ItemStack>();
			PlayerInfo info = Hardcore.hardcore.cAPI.getPlayerInfo(Bukkit.getPlayer(data.username));
			for(CustomItem ci : itemList) {
				//Are they allowed to buy this item?
				if(Hardcore.hardcore.pAPI.aboveRank(info.rank, "vip")) {
					ItemStack item = ci.clone();
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(ci.shopName);
					List<String> desc = new ArrayList<String>();
					desc.add(ChatColor.translateAlternateColorCodes('&', ci.description));
					desc.add(ChatColor.translateAlternateColorCodes('&', "&7This item costs: &e" + ci.cost + "&7 Credits"));
					meta.setLore(desc);
					item.setItemMeta(meta);
					shopItems.add(item);
				}else if(ci.requiresLevel && info.level >= ci.reqLevel) {
					ItemStack item = ci.clone();
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(ci.shopName);
					List<String> desc = new ArrayList<String>();
					desc.add(ChatColor.translateAlternateColorCodes('&', ci.description));
					desc.add(ChatColor.translateAlternateColorCodes('&', "&7This item costs: &e" + ci.cost + "&7 Credits"));
					meta.setLore(desc);
					item.setItemMeta(meta);
					shopItems.add(item);
				}else if(!ci.requiresLevel) {
					ItemStack item = ci.clone();
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(ci.shopName);
					List<String> desc = new ArrayList<String>();
					desc.add(ChatColor.translateAlternateColorCodes('&', ci.description));
					desc.add(ChatColor.translateAlternateColorCodes('&', "&7This item costs: &e" + ci.cost + "&7 Credits"));
					meta.setLore(desc);
					item.setItemMeta(meta);
					shopItems.add(item);
				}
			}
			
			for(ItemStack item : shopItems) {
				inv.addItem(item);
			}
		} else {
			List<CustomItem> itemList = Hardcore.hardcore.itm.getShopItems(type);
			List<ItemStack> shopItems = new ArrayList<ItemStack>();
			PlayerInfo info = Hardcore.hardcore.cAPI.getPlayerInfo(Bukkit.getPlayer(data.username));
			for(CustomItem ci : itemList) {
				//Are they allowed to buy this item?
				if(Hardcore.hardcore.pAPI.aboveRank(info.rank, "vip")) {
					ItemStack item = ci.clone();
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(ci.shopName);
					List<String> desc = new ArrayList<String>();
					desc.add(ChatColor.translateAlternateColorCodes('&', ci.description));
					desc.add(ChatColor.translateAlternateColorCodes('&', "&7This item costs: &e" + ci.cost + "&7 Skill-Points"));
					meta.setLore(desc);
					item.setItemMeta(meta);
					shopItems.add(item);
				}else if(ci.requiresLevel && info.level >= ci.reqLevel) {
					ItemStack item = ci.clone();
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(ci.shopName);
					List<String> desc = new ArrayList<String>();
					desc.add(ChatColor.translateAlternateColorCodes('&', ci.description));
					desc.add(ChatColor.translateAlternateColorCodes('&', "&7This item costs: &e" + ci.cost + "&7 Skill-Points"));
					meta.setLore(desc);
					item.setItemMeta(meta);
					shopItems.add(item);
				}else if(!ci.requiresLevel) {
					ItemStack item = ci.clone();
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(ci.shopName);
					List<String> desc = new ArrayList<String>();
					desc.add(ChatColor.translateAlternateColorCodes('&', ci.description));
					desc.add(ChatColor.translateAlternateColorCodes('&', "&7This item costs: &e" + ci.cost + "&7 Skill-Points"));
					meta.setLore(desc);
					item.setItemMeta(meta);
					shopItems.add(item);
				}
			}
			
			for(ItemStack item : shopItems) {
				inv.addItem(item);
			}
		}
		
		Hardcore.hardcore.pm.playerOpened.put(data.username, inv);
		
		return inv;
	}

}
