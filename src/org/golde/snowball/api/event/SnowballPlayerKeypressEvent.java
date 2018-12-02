package org.golde.snowball.api.event;

import org.bukkit.entity.Player;
import org.golde.snowball.api.object.SnowballPlayer;
import org.golde.snowball.plugin.event.SnowballEvent;

public class SnowballPlayerKeypressEvent extends SnowballEvent {

	private final SnowballPlayer player;
	private int key;
	private boolean isInGuiWindow;
	
	public SnowballPlayerKeypressEvent(SnowballPlayer player, int key, boolean isInGuiWindow) {
		this.player = player;
		this.key = key;
		this.isInGuiWindow = isInGuiWindow;
	}
	
	public SnowballPlayer getPlayer() {
		return player;
	}
	
	public int getKey() {
		return key;
	}
	
	public boolean isInGuiWindow() {
		return isInGuiWindow;
	}
	
}
