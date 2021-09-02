package ca.bungo.hardcore.items.GeneralItems;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ca.bungo.hardcore.hardcore.Hardcore;
import ca.bungo.hardcore.items.CustomItem;


public class ChargedHeart extends CustomItem {

	public ChargedHeart(Hardcore hardcore, String name, Material item) {
		super(hardcore, name, item);
		ItemMeta meta = this.getItemMeta();
		
		meta.setDisplayName(ChatColor.BLUE + "Charged Heart");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.YELLOW + "Used in crafting!");
		meta.setLore(lore);
		meta.addEnchant(Enchantment.ARROW_INFINITE, -1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		this.setItemMeta(meta);
	}
	
	@EventHandler
	public void killEvent(EntityDeathEvent event) {
		if(!(event.getEntity() instanceof IronGolem) || !event.getEntity().getName().substring(2).equals("Angered Golem"))
			return;
		if(event.getEntity().getKiller() == null)
			return;
		
		event.setDroppedExp(0);
		event.getDrops().clear();
		
		event.getDrops().add(this);
		return;
	}
	
	@EventHandler
	public void onInteractEntity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		if(!(entity instanceof IronGolem) && entity.getName().substring(2).equals("Angered Golem"))
			return;
		
		ItemStack item = player.getInventory().getItemInMainHand();
		if(item == null || item.getItemMeta() == null || item.getItemMeta().getDisplayName() == null || !item.getItemMeta().getDisplayName().equals(hardcore.itm.getItem("Life Egg").getItemMeta().getDisplayName()))
			return;
		
		item.setAmount(item.getAmount() - 1);
		Location loc = entity.getLocation();
		hardcore.mu.spawnMob(player, loc);
		entity.remove();
		
	}
	
	@EventHandler
	public void onEntityTarget(EntityTargetLivingEntityEvent event) {
		Entity ent = event.getEntity();
		if(ent.getCustomName() == null)
			return;
		if(!(ent instanceof IronGolem) && !ent.getCustomName().substring(2).equals("Angered Golem"))
			return;
		if(!(event.getTarget() instanceof Player))
			event.setCancelled(true);
	}
	
	

}
