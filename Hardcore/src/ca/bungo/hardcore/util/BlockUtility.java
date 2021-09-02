package ca.bungo.hardcore.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import ca.bungo.hardcore.hardcore.Hardcore;

public class BlockUtility {
	
	public Hardcore hardcore;
	
	public HashMap<Material, ChatColor> blockColors = new HashMap<Material, ChatColor>();
	
	
	//Reflection Stuff
	public Class<?> CraftWorld, EntityMagmaCube, PacketPlayOutSpawnEntityLiving, PacketPlayOutEntityMetadata, 
						EntityTypes, EntityLiving, DataWatcher, World, PacketPlayOutEntityDestroy;
	public Method getHandle, sendPacket;
	public Constructor<?> PPOSELConstructor, PPOEMConstructor, EMCConstructor, PPOEDConstructor;
	
	public BlockUtility(Hardcore hardcore) {
		this.hardcore = hardcore;
		
		registerBlockColors();
		setupReflection();
	}	
	
	private void setupReflection() {
		EntityMagmaCube = ReflectionUtility.getNMSClass("EntityMagmaCube");
		PacketPlayOutSpawnEntityLiving = ReflectionUtility.getNMSClass("PacketPlayOutSpawnEntityLiving");
		PacketPlayOutEntityMetadata = ReflectionUtility.getNMSClass("PacketPlayOutEntityMetadata");
		PacketPlayOutEntityDestroy = ReflectionUtility.getNMSClass("PacketPlayOutEntityDestroy");
		EntityLiving = ReflectionUtility.getNMSClass("EntityLiving");
		DataWatcher = ReflectionUtility.getNMSClass("DataWatcher");
		World = ReflectionUtility.getNMSClass("World");
		EntityTypes = ReflectionUtility.getNMSClass("EntityTypes");
		try {
			CraftWorld = ReflectionUtility.getCBClass("CraftWorld");
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			getHandle = CraftWorld.getDeclaredMethod("getHandle");
			sendPacket = ReflectionUtility.getNMSClass("PlayerConnection").getDeclaredMethod("sendPacket", ReflectionUtility.getNMSClass("Packet"));
			PPOSELConstructor = PacketPlayOutSpawnEntityLiving.getDeclaredConstructor(EntityLiving);
			PPOEMConstructor = PacketPlayOutEntityMetadata.getDeclaredConstructor(int.class, DataWatcher, boolean.class);
			PPOEDConstructor = PacketPlayOutEntityDestroy.getDeclaredConstructor(int[].class);
			EMCConstructor = EntityMagmaCube.getDeclaredConstructor(EntityTypes, World);
		} catch(NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			return;
		}
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
	
	public void glowBlockNMS(Player player, Location loc) {
		//PacketPlayOutEntityDestroy;
		Object cWorld, emc, enttype, packet1, packet2;
		try {
			
			cWorld = CraftWorld.cast(loc.getWorld()).getClass().getDeclaredMethod("getHandle").invoke(CraftWorld.cast(loc.getWorld()));
			enttype = EntityTypes.getDeclaredField("MAGMA_CUBE").get(EntityTypes);
			emc = EMCConstructor.newInstance(enttype, cWorld);
			
			emc.getClass().getDeclaredMethod("setSize", int.class, boolean.class).invoke(emc, 1, true);
			emc.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("setFlag", int.class, boolean.class).invoke(emc, 6, true);
			emc.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("setInvulnerable", boolean.class).invoke(emc, true);
			emc.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("setInvisible", boolean.class).invoke(emc, true);
			emc.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("setLocation", double.class, double.class, double.class, float.class, float.class).invoke(emc, loc.getX(), loc.getY(), loc.getZ(), 0, 0);
			
			packet1 = PPOSELConstructor.newInstance(emc);
			int entID = (int) emc.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("getId").invoke(emc);
			packet2 = PPOEMConstructor.newInstance(entID, emc.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("getDataWatcher").invoke(emc), true);
			
			Object conn = ReflectionUtility.getConnection(player);
			
			Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
			
			int randomNumber = ((Random) new Random()).nextInt(999999999);
			
			while(sb.getTeam("" + randomNumber) != null)
				randomNumber = ((Random) new Random()).nextInt(999999999);
			
			
			if(!blockColors.containsKey(loc.getBlock().getType()))
				return;
			Team team = sb.registerNewTeam("" + randomNumber);
			team.setColor(blockColors.get(loc.getBlock().getType()));
			
			sendPacket.invoke(conn, packet1);
			sendPacket.invoke(conn, packet2);
			
			team.addEntry((String)emc.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("getUniqueID").invoke(emc).getClass().getDeclaredMethod("toString").invoke(emc.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("getUniqueID").invoke(emc)));
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(hardcore, ()->{
				Object packet3;
				try {
					packet3 = PPOEDConstructor.newInstance(new Object[] {new int[] {entID}});
					sendPacket.invoke(conn, packet3);
					team.unregister();
				}catch(Exception e) {
					e.printStackTrace();
					return;
				}
			}, 150);
			
		}catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
	}
	
	//All players near by can see these glowing blocks
	//I will make this PacketBased so only the selected player can see them.
	public void glowBlock(Location loc) {
		Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();
		
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
