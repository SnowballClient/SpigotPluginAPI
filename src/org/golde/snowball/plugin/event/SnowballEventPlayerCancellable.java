package org.golde.snowball.plugin.event;

import org.golde.snowball.api.object.SnowballPlayer;

public class SnowballEventPlayerCancellable extends SnowballEventCancellable {

	private final SnowballPlayer player;
	
	public SnowballEventPlayerCancellable(SnowballPlayer player) {
		this.player = player;
	}
	
	public SnowballPlayer getPlayer() {
		return player;
	}
	
}
