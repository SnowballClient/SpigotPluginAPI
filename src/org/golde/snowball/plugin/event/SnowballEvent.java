package org.golde.snowball.plugin.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SnowballEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	public final HandlerList getHandlers() {
	    return handlers;
	}

	public static final HandlerList getHandlerList() {
	    return handlers;
	}
	
	public final void call() {
		Bukkit.getPluginManager().callEvent(this);
	}
	
	@Override
	public final String getEventName() { //so when you type <space> get it doesn't come up
		return super.getEventName();
	}
	
}
