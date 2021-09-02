package ca.bungo.hardcore.util.managers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ca.bungo.hardcore.hardcore.Hardcore;
import ca.bungo.hardcore.util.Cooldown;
import ca.bungo.hardcore.util.ReflectionUtility;
import ca.bungo.hardcore.util.player.PlayerData;

public class CooldownManager {
	private Hardcore pl;
	
	private boolean reflectionValid = true;
	
	//Reflection Stuff
	private Method getHandle, sendPacket;
	private Class<?> EntityPlayer, CraftPlayer, PacketPlayOutSetCooldown, Item;
	private Constructor<?> PPOSCConstructor;
	
	public CooldownManager(Hardcore pl) {
		this.pl = pl;
		
		//Reflection stuff
		EntityPlayer = ReflectionUtility.getNMSClass("EntityPlayer");
		try {
			CraftPlayer = ReflectionUtility.getCBClass("entity.CraftPlayer");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
			reflectionValid = false;
			return;
		}
		PacketPlayOutSetCooldown = ReflectionUtility.getNMSClass("PacketPlayOutSetCooldown");
		Item = ReflectionUtility.getNMSClass("Item");
		
		try {
			getHandle = CraftPlayer.getDeclaredMethod("getHandle");
			PPOSCConstructor = PacketPlayOutSetCooldown.getConstructor(Item, int.class);
			sendPacket = ReflectionUtility.getNMSClass("PlayerConnection").getMethod("sendPacket", ReflectionUtility.getNMSClass("Packet"));
		}catch(NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			reflectionValid = false;
			return;
		}
	}
	
	//1 Tick = 50 milliseconds
	public Cooldown giveCooldown(Player player, double ticks) {
		
		Cooldown cooldown = new Cooldown(pl, ticks);
		if(reflectionValid)
			giveEffect(player, (int)ticks);
		return cooldown;
	}
	
	private void giveEffect(Player player, int ticks) {
		Object plr;
		try {
			plr = EntityPlayer.cast(getHandle.invoke(CraftPlayer.cast(player)));
		}catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return;
		}
		
		Object itemstack;
		try {
			Object inv = EntityPlayer.cast(plr).getClass().getField("inventory").get(EntityPlayer.cast(plr));
			
			itemstack = inv.getClass().getMethod("getItemInHand").invoke(inv);
		}catch(NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
			e.printStackTrace();
			return;
		}
		
		Object packet;
		try {
			Object item = itemstack.getClass().getMethod("getItem").invoke(itemstack);
			packet = PPOSCConstructor.newInstance(Item.cast(item), ticks);
			
		}catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			return;
		}
		
		try {
			sendPacket.invoke(ReflectionUtility.getConnection(player), packet);
		}catch(IllegalArgumentException | SecurityException | ReflectiveOperationException e) {
			e.printStackTrace();
			return;
		}
		
	}
	
	public boolean onCooldown(Player player, String name) {
		PlayerData data = pl.pm.getPlayerData(player);
		
		Cooldown cd = data.getCooldown(name);
		if(cd == null)
			return false;
		if(cd.getRemainingTime() > 0) {
			player.sendMessage(ChatColor.YELLOW + "" + cd.getRemainingTime() + ChatColor.BLUE + " seconds remaining until you can use this again...");
			return true;
		}
		data.removeCooldown(name);
		return false;
	}
	
	public void giveCooldown(Player player, String name, double seconds) {
		if(!onCooldown(player, name)) {
			Cooldown cd = giveCooldown(player, seconds*20);
			pl.pm.getPlayerData(player).addCooldown(name, cd);
		}
	}
}
