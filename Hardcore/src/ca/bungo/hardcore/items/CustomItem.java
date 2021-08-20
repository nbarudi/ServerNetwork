package ca.bungo.hardcore.items;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import ca.bungo.hardcore.hardcore.Hardcore;
import ca.bungo.hardcore.util.managers.ItemManager.ShopType;

public class CustomItem extends ItemStack implements Listener {	
	
	public Hardcore hardcore;
	
	public String itemName;
	public boolean isBuyable;
	
	//Shop Values
	public ShopType st;
	public int cost;
	public String description;
	public String shopName;
	public boolean requiresLevel = false;
	public int reqLevel = 0;
	
	public CustomItem(Hardcore hardcore, String name, Material item) {
		super(item);
		this.hardcore = hardcore;
		this.itemName = name;
	}

}
