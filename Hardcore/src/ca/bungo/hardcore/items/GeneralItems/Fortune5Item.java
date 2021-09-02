package ca.bungo.hardcore.items.GeneralItems;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
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
import ca.bungo.hardcore.util.EnchantmentUtility;

public class Fortune5Item extends CustomItem{

	public Fortune5Item(Hardcore hardcore, String name, Material item) {
		super(hardcore, name, item);
		
		this.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 5);	
		
		ItemMeta meta = this.getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.YELLOW + "Hold the item you want to enchant in your off hand!");
		lore.add(ChatColor.YELLOW + "Right click the enchantment book in your main hand!");
		meta.setLore(lore);
		this.setItemMeta(meta);
		
		ShapedRecipe sr = new ShapedRecipe(new NamespacedKey(hardcore, "fortune5"), this);
		sr.shape("BLB",
				 "LbL",
				 "BLB");
		sr.setIngredient('B', new RecipeChoice.ExactChoice(hardcore.itm.getItem("Binding Agent")));
		sr.setIngredient('L', Material.LAPIS_BLOCK);
		sr.setIngredient('b', new RecipeChoice.ExactChoice(hardcore.itm.getItem("Fortune 4")));
		
		Bukkit.addRecipe(sr);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if(!event.getAction().equals(Action.RIGHT_CLICK_AIR))
			return;
		
		ItemStack mainItem = player.getInventory().getItemInMainHand();
		ItemStack subItem = player.getInventory().getItemInOffHand();
		
		if(mainItem == null || subItem == null)
			return;
		
		if(mainItem.getItemMeta() == null || subItem.getItemMeta() == null)
			return;
		
		if(!mainItem.getItemMeta().equals(this.getItemMeta()))
			return;
		if(!EnchantmentUtility.enchantItem(subItem, mainItem))
			return;
		mainItem.setAmount(0);
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Enchantments> &7Enchanted your " + subItem.getType().toString()));
	}
	

}
