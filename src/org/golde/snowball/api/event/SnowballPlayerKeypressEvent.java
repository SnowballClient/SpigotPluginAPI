package org.golde.snowball.api.event;

import org.bukkit.entity.Player;
import org.golde.snowball.api.object.SnowballPlayer;
import org.golde.snowball.plugin.event.SnowballEvent;
import org.golde.snowball.plugin.event.SnowballEventPlayer;

public class SnowballPlayerKeypressEvent extends SnowballEventPlayer {
	
	private int key;
	private boolean isInGuiWindow;
	
	public SnowballPlayerKeypressEvent(SnowballPlayer player, int key, boolean isInGuiWindow) {
		super(player);
		this.key = key;
		this.isInGuiWindow = isInGuiWindow;
	}
	
	public int getKey() {
		return key;
	}
	
	public boolean isInGuiWindow() {
		return isInGuiWindow;
	}
	
}
