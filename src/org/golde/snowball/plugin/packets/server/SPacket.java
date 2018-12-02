package org.golde.snowball.plugin.packets.server;

import java.io.IOException;

import org.golde.snowball.plugin.packets.SnowballPacket;

import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import net.minecraft.server.v1_12_R1.PacketListener;

public abstract class SPacket extends SnowballPacket {

	@Override
	public void handle(PacketListener arg0) {
		//Sent from servers, won't be getting these messages
	}
	
	@Override
	public void readPacketData(PacketDataSerializer data) throws IOException {
		//Sent from servers, won't be getting these messages
	}

}
