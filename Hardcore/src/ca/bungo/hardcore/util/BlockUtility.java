package ca.bungo.hardcore.util;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MagmaCube;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import ca.bungo.hardcore.hardcore.Hardcore;

public class BlockUtility {
	
	public Hardcore hardcore;
	
	public HashMap<Material, ChatColor> blockColors = new HashMap<Material, ChatColor>();
	
	public BlockUtility(Hardcore hardcore) {
		this.hardcore = hardcore;
		
		registerBlockColors();
	}
	
	private void registerBlockColors() {
		//Normal Ores
		blockColors.put(Material.COAL_ORE, ChatColor.BLACK);
		blockColors.put(Material.IRON_ORE, ChatColor.GOLD);
		blockColors.put(Material.GOLD_ORE, ChatColor.YELLOW);
		blockColors.put(Material.REDSTONE_ORE, ChatColor.RED);
		blockColors.put(Material.LAPIS_ORE, ChatColor.BLUE);
		blockColors.put(Material.DIAMOND_ORE, ChatColor.AQUA);
		blockColors.put(Material.EMERALD_ORE, ChatColor.GREEN);
		
		//Nether Ores
		blockColors.put(Material.NETHER_QUARTZ_ORE, ChatColor.WHITE);
		blockColors.put(Material.NETHER_GOLD_ORE, ChatColor.YELLOW);
	}
	
	
	
	//All players near by can see these glowing blocks
	//I will make this PacketBased so only the selected player can see them.
	public void glowBlock(Location loc) {
		Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();

		//if(sb.getTeam(loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ()) != null)
		//	return;
		
		int randomNumber = ((Random) new Random()).nextInt(999999999);
		
		
		
		while(sb.getTeam("" + randomNumber) != null)
			randomNumber = ((Random) new Random()).nextInt(999999999);;
			
		Team team = sb.registerNewTeam("" + randomNumber);
		//This is just lazy coding.. I didn't feel like pre-making teams
		//Because then I would have to unregister them on a shutdown/reload
		//So I make a random team, then unregister it once i'm done with it
		//Takes a random number, very unlikely that it finds a similarity.. I hope... (In testing, It picked the same number 3 times ;-;)
		
		
		if(!blockColors.containsKey(loc.getBlock().getType()))
			return;
		//System.out.println("X: " + loc.getBlockX() + " | Y: " + loc.getBlockY() + "| Z: " + loc.getBlockZ());
		team.setColor(blockColors.get(loc.getBlock().getType()));
		
		loc.setX(loc.getX() + 0.5);
		loc.setY(loc.getY() + 0.5);
		loc.setZ(loc.getZ() + 0.5);
		
		MagmaCube mc = (MagmaCube) loc.getWorld().spawnEntity(loc, EntityType.MAGMA_CUBE);
		mc.setSize(1);
		mc.setGlowing(true);
		mc.setInvisible(true);
		mc.setAI(false);
		mc.teleport(loc);
		mc.setInvulnerable(true);
		mc.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 200));
		
		team.addEntry(mc.getUniqueId().toString());
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(hardcore, () ->{
			mc.remove();
			team.unregister();
		}, 100);
		
		
	}

}
