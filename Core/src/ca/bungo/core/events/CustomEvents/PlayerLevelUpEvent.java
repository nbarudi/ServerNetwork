package ca.bungo.core.events.CustomEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLevelUpEvent extends Event {

	private Player player;
	private int prevLevel;
	private int curLevel;
	private boolean cancelled = false;
	
	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	
	public PlayerLevelUpEvent(Player player, int prevLevel, int curLevel) {
		this.player = player;
		this.prevLevel = prevLevel;
		this.curLevel = curLevel;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public int getPreviousLevel() {
		return prevLevel;
	}
	
	public int getNewLevel() {
		return curLevel;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	

}
