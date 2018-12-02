package org.golde.snowball.plugin.packets.server;

import java.io.IOException;

import net.minecraft.server.v1_12_R1.PacketDataSerializer;

public class SPacketInfo extends SPacket {

	private final int customObjectsCount;
	
	public SPacketInfo(int customObjectsCount) {
		this.customObjectsCount = customObjectsCount;
	}
	
	@Override
	public void writePacketData(PacketDataSerializer data) throws IOException {
		data.writeInt(customObjectsCount);
	}

}
