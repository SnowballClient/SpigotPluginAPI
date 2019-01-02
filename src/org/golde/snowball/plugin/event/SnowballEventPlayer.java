package org.golde.snowball.plugin.event;

import org.golde.snowball.api.object.SnowballPlayer;

public class SnowballEventPlayer extends SnowballEvent  {

	private final SnowballPlayer player;
	
	public SnowballEventPlayer(SnowballPlayer player) {
		this.player = player;
	}
	
	public SnowballPlayer getPlayer() {
		return player;
	}
	
}
