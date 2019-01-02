package org.golde.snowball.plugin.packets.server;

import java.io.IOException;

import org.golde.snowball.shared.nbt.NBTConstants;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;

public class SPacketUpdatePlayerSkin extends SPacket{

	private final String username;
	private final String skin_url;
	
	 public SPacketUpdatePlayerSkin(org.bukkit.entity.Player player, String skin_url) {
		 this.username = player.getName();
		 this.skin_url = skin_url;
	 }
	
	@Override
	public void writePacketData(PacketDataSerializer data) throws IOException {
		NBTTagCompound tag = new NBTTagCompound();
		
		tag.setString(NBTConstants.KEY_SPacketUpdatePlayerLooks_skin, skin_url);
		tag.setString(NBTConstants.KEY_SPacketUpdatePlayerLooks_username, username);
		
		data.a(tag);
	}

}
