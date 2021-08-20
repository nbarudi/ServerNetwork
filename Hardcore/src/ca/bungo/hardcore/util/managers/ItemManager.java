package ca.bungo.hardcore.util.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ca.bungo.hardcore.hardcore.Hardcore;
import ca.bungo.hardcore.items.CustomItem;

public class ItemManager {
	
	public enum ShopType {
		SP,CREDITS;
	}
	
	//private Hardcore hardcore;
	
	private HashMap<String, CustomItem> customItems;
	
	public ItemManager(Hardcore hardcore) {
		//this.hardcore = hardcore;
		this.customItems = new HashMap<String, CustomItem>();
	}
	
	public void addItem(CustomItem item) {
		customItems.put(item.itemName, item);
	}
	
	public CustomItem getItem(String name) {
		CustomItem item = null;
		for(String key : customItems.keySet()) {
			if(key.equals(name)) {
				item = customItems.get(key);
				break;
			}	
		}
		return item;
	}
	
	public List<CustomItem> getAllItems(){
		List<CustomItem> items = new ArrayList<CustomItem>();
		for(String key : customItems.keySet()) {
			items.add(customItems.get(key));
		}
		return items;
	}
	
	public List<CustomItem> getShopItems(ShopType type){
		List<CustomItem> ci = new ArrayList<CustomItem>();
		
		for(String key : customItems.keySet()) {
			CustomItem item = customItems.get(key);
			if(!item.isBuyable)
				continue;
			if(!type.equals(item.st))
				continue;
			ci.add(item);
		}
		
		return ci;
	}

}
