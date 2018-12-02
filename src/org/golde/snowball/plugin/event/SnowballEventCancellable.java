package org.golde.snowball.plugin.event;

import org.bukkit.event.Cancellable;

public class SnowballEventCancellable extends SnowballEvent implements Cancellable{

	private boolean cancelled = false;

	public boolean isCancelled() {
		return cancelled;
	}

	public final void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

}
