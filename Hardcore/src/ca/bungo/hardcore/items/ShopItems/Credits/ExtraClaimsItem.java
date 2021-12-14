package ca.bungo.hardcore.items.ShopItems.Credits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import ca.bungo.hardcore.hardcore.Hardcore;
import ca.bungo.hardcore.items.CustomItem;
import ca.bungo.hardcore.util.managers.ItemManager.ShopType;
import net.md_5.bungee.api.ChatColor;

public class ExtraClaimsItem extends CustomItem {

	public ExtraClaimsItem(Hardcore hardcore, String name, Material item) {
		super(hardcore, name, item);
		
		
		this.description = "Gain 5 extra claim blocks!";
		
		this.isBuyable = true;
		this.st = ShopType.CREDITS;
		this.cost = 3000;
		this.shopName = name;
		this.requiresLevel = true;
		this.reqLevel = 4;
		
		ItemMeta meta = this.getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Right-Click to add 5 claim blocks to your account!"));
		meta.setLore(lore);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aIncrease Claim Size"));
		this.setItemMeta(meta);
		
		ItemStack tm = this.clone();
		
		tm.setAmount(3);
		hardcore.nameKeys.add("extraclaims");
		ShapedRecipe sr = new ShapedRecipe(new NamespacedKey(hardcore, "extraclaims"), tm);
		sr.shape("DGD",
				 "GBG",
				 "DGD");
		sr.setIngredient('D', Material.DIRT);
		sr.setIngredient('G', Material.GOLD_INGOT);
		sr.setIngredient('B', new RecipeChoice.ExactChoice(hardcore.itm.getItem("Binding Agent")));
		
		Bukkit.addRecipe(sr);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		ItemStack item = player.getInventory().getItemInMainHand();
		
		if(item == null || item.getItemMeta() == null || item.getItemMeta().getDisplayName() == null || !item.getItemMeta().getDisplayName().equals(this.getItemMeta().getDisplayName()))
			return;
		
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			event.setCancelled(true);
			
			int amount = player.getInventory().getItemInMainHand().getAmount();
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "acb " + player.getName() + " 5");
			player.getInventory().getItemInMainHand().setAmount(amount - 1);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Increased your claim size by 5 blocks!"));
			return;
		}
	}

}
