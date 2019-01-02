package org.golde.snowball.plugin.packets.server;

import java.io.IOException;

import org.golde.snowball.shared.nbt.NBTConstants;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;

public class SPacketUpdatePlayerUsername extends SPacket {

	private final String username;
	private final String customUsername;
	
	 public SPacketUpdatePlayerUsername(org.bukkit.entity.Player player, String customUsername) {
		 this.username = player.getName();
		 this.customUsername = customUsername;
	 }
	
	@Override
	public void writePacketData(PacketDataSerializer data) throws IOException {
		NBTTagCompound tag = new NBTTagCompound();
		
		tag.setString(NBTConstants.KEY_SPacketUpdatePlayerLooks_username, username);
		tag.setString(NBTConstants.KEY_SPacketUpdatePlayerLooks_custom_username, customUsername);
		
		data.a(tag);
	}
	
}
