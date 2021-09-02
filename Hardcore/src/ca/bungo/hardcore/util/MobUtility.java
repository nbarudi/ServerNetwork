package ca.bungo.hardcore.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ca.bungo.hardcore.hardcore.Hardcore;

public class MobUtility {

	Hardcore hardcore;
	
	//Reflection Stuff
	Class<?> EntityIronGolem, CraftEntity, EntityInsistant, EntityLiving, CraftPlayer;
	Method ceGetHandle, cpGetHandle, cwGetHandle;
	
	
	public MobUtility(Hardcore hardcore) {
		this.hardcore = hardcore;
		
		setupReflection();
	}
	
	private void setupReflection() {
		try {
			EntityIronGolem = ReflectionUtility.getNMSClass("EntityIronGolem");
			EntityInsistant = EntityIronGolem.getSuperclass().getSuperclass().getSuperclass();
			EntityLiving = ReflectionUtility.getNMSClass("EntityLiving");
			CraftEntity = ReflectionUtility.getCBClass("entity.CraftEntity");
			CraftPlayer = ReflectionUtility.getCBClass("entity.CraftPlayer");
			ceGetHandle = CraftEntity.getDeclaredMethod("getHandle");
			cpGetHandle = CraftPlayer.getDeclaredMethod("getHandle");
		}catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	public void spawnMob(Player player, Location loc) {
		
		IronGolem golem = (IronGolem)loc.getWorld().spawnEntity(loc, EntityType.IRON_GOLEM);
		golem.setCustomName(ChatColor.RED + "Angered Golem");
		golem.setCustomNameVisible(true);
		Method setGoalTarget, setNav;
		Object nav, eig;
		try {
			eig = ceGetHandle.invoke(CraftEntity.cast(golem));
			
			nav = EntityInsistant.getDeclaredMethod("getNavigation").invoke(eig);
			setNav = nav.getClass().getSuperclass().getDeclaredMethod("a", double.class, double.class, double.class, double.class);
			setGoalTarget = EntityInsistant.getDeclaredMethod("setGoalTarget", EntityLiving, TargetReason.class, boolean.class);
			setNav.invoke(nav, loc.getX(), loc.getY(), loc.getZ(), 1d);
			setGoalTarget.invoke(eig, cpGetHandle.invoke(CraftPlayer.cast(player)), TargetReason.TARGET_ATTACKED_ENTITY, false);
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
		Bukkit.getScheduler().scheduleSyncRepeatingTask(hardcore, ()->{
			if(golem.isDead())
				return;
			try {
				setGoalTarget.invoke(eig, cpGetHandle.invoke(CraftPlayer.cast(player)), TargetReason.TARGET_ATTACKED_ENTITY, false);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		}, 20, 20);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(hardcore, ()->{
			if(golem.isDead())
				return;
			
			Location gloc = golem.getLocation();
			
			Random rnd = new Random();
			int amount = rnd.nextInt(3) + 1;
			
			for(int i = 0; i < amount; i++)
				gloc.getWorld().spawnEntity(gloc, EntityType.VEX);
			
			
			gloc.setY(gloc.getY() + 20);
			gloc.getWorld().strikeLightning(gloc);
		}, 120, 120);
		
		golem.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999999, 1));
	}
	
}
