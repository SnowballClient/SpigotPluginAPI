package org.golde.snowball.plugin.packets;

import java.io.IOException;

import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import net.minecraft.server.v1_12_R1.PacketListener;

public abstract class SnowballPacket implements Packet {

	@Override
	public void a(PacketDataSerializer arg0) throws IOException {
		readPacketData(arg0);
	}

	@Override
	public void b(PacketDataSerializer arg0) throws IOException {
		writePacketData(arg0);
	}
	
	@Override
	public void a(PacketListener arg0) {
		handle(arg0);
	}

	public abstract void readPacketData(PacketDataSerializer data) throws IOException;
	public abstract void writePacketData(PacketDataSerializer data) throws IOException;
	public abstract void handle(PacketListener arg0);
	
}
