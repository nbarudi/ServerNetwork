package ca.bungo.hardcore.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.entity.Player;

import ca.bungo.hardcore.hardcore.Hardcore;

public class PlayerUtility {
	
	Class<?> Entity, EntityPlayer, PacketPlayOutEntityStatus, CraftPlayer;
	Constructor<?> PPOESCon;
	
	public PlayerUtility(Hardcore hardcore) {
		try {
			EntityPlayer = ReflectionUtility.getNMSClass("EntityPlayer");
			PacketPlayOutEntityStatus = ReflectionUtility.getNMSClass("PacketPlayOutEntityStatus");
			Entity = EntityPlayer.getSuperclass().getSuperclass().getSuperclass();
			CraftPlayer = ReflectionUtility.getCBClass("entity.CraftPlayer");
			PPOESCon = PacketPlayOutEntityStatus.getConstructor(Entity, byte.class);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void giveTotemEffect(Player player) {
		//NMS Code:
		//EntityPlayer ep = ((CraftPlayer)player).getHandle();
		//PacketPlayOutEntityStatus status = new PacketPlayOutEntityStatus(ep, (byte)35);
		//ep.playerConnection.sendPacket(status);
		
		//Reflection Code:
		try {
			Object ep = CraftPlayer.cast(player).getClass().getMethod("getHandle").invoke(CraftPlayer.cast(player));
			Object pket = PPOESCon.newInstance(EntityPlayer.cast(ep), (byte)35);
			Method sendPacket = ReflectionUtility.getNMSClass("PlayerConnection").getMethod("sendPacket", ReflectionUtility.getNMSClass("Packet"));
			sendPacket.invoke(ReflectionUtility.getConnection(player), pket);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException | InstantiationException | NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
