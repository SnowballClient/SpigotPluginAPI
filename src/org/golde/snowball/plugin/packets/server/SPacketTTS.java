package org.golde.snowball.plugin.packets.server;

import java.io.IOException;

import org.golde.snowball.shared.nbt.NBTConstants;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;

public class SPacketTTS extends SPacket {

	private final String text;
	
	public SPacketTTS(String text) {
		this.text = text;
	}
	
	@Override
	public void writePacketData(PacketDataSerializer data) throws IOException {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString(NBTConstants.KEY_TTS_TEXT, text);
		data.a(tag);
	}

}
