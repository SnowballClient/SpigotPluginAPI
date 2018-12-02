package org.golde.snowball.plugin.packets.client;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.golde.snowball.api.event.SnowballPlayerKeypressEvent;
import org.golde.snowball.api.object.SnowballPlayer;
import org.golde.snowball.plugin.MainPlugin;

import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import net.minecraft.server.v1_12_R1.PacketListener;

public class CPacketKeyPress extends CPacket {

	private String name;
	private int key;
	private boolean isInGuiWindow;
	
	@Override
	public void readPacketData(PacketDataSerializer data) throws IOException {
		name = data.e(32);
		key = data.readInt();
		isInGuiWindow = data.readBoolean();
	}

	@Override
	public void handle(PacketListener arg0) {
		SnowballPlayer player = MainPlugin.getInstance().getOrCreateSnowballPlayer(Bukkit.getPlayer(name));
		new SnowballPlayerKeypressEvent(player, key, isInGuiWindow).call();
	}

}
