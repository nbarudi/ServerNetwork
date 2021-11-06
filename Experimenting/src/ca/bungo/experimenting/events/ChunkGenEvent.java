package ca.bungo.experimenting.events;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ca.bungo.experimenting.experimenting.Experimenting;
import ca.bungo.experimenting.util.Laser;

public class ChunkGenEvent implements Listener {
	
	Experimenting experimenting;
	
	HashMap<String, Laser> chests = new HashMap<String, Laser>();
	
	public ChunkGenEvent(Experimenting experimenting) {
		this.experimenting = experimenting;
	}
	
	@EventHandler
	public void onChunkGen(ChunkLoadEvent event) {
		Random rnd = new Random();
		if(rnd.nextInt(1000) > 995) {
			System.out.println("Generating Chest");
			Block block = event.getChunk().getBlock(7, 7, 7);
			block.setType(Material.CHEST);
			
			Location loc = block.getLocation();
			loc.setY(255);

			try {
				Laser laser = new Laser.GuardianLaser(block.getLocation(), loc, -1, -1);
				laser.start(experimenting);
				chests.put(loc_string(block.getLocation()), laser);
			} catch (ReflectiveOperationException e) {
				e.printStackTrace();
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(event.getClickedBlock().getType().equals(Material.CHEST) && chests.containsKey(loc_string(event.getClickedBlock().getLocation()))) {
				
				ItemStack item = new ItemStack(Material.PAPER);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(ChatColor.GREEN + "Proof of Concept");
				item.setItemMeta(meta);
				
				Chest chest = (Chest)event.getClickedBlock().getState();
				chest.getInventory().addItem(item);
				
				Laser laser = chests.get(loc_string(chest.getLocation()));
				laser.stop();
				
				
			}
		}
	}
	
	private String loc_string(Location loc) {
		return "Loc-X:"+loc.getBlockX()+"Loc-Y:"+loc.getBlockY()+"Loc-Z:"+loc.getBlockZ();
	}

}
