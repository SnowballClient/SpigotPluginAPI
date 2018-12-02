package org.golde.snowball.plugin.packets.server;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.golde.snowball.shared.enums.EnumCosmetic;
import org.golde.snowball.shared.nbt.NBTConstants;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NBTTagString;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;

public class SPacketUpdatePlayerLooks extends SPacket {

	private final String username;
	private final String skin_url;
	private final String customUsername;
	List<EnumCosmetic> cosmetics;
	
	 public SPacketUpdatePlayerLooks(org.bukkit.entity.Player player, String skin_url, String customUsername, List<EnumCosmetic> cosmetics) {
		 this.username = player.getName();
		 this.skin_url = skin_url;
		 this.customUsername = customUsername;
		 this.cosmetics = cosmetics;
	 }
	
	@Override
	public void writePacketData(PacketDataSerializer data) throws IOException {
		NBTTagCompound tag = new NBTTagCompound();
		
		tag.setString(NBTConstants.KEY_SPacketUpdatePlayerLooks_skin, skin_url);
		tag.setString(NBTConstants.KEY_SPacketUpdatePlayerLooks_username, username);
		tag.setString(NBTConstants.KEY_SPacketUpdatePlayerLooks_custom_username, customUsername);
		
		NBTTagList cosmeticList = new NBTTagList();
		
		for(EnumCosmetic cos : cosmetics) {
			cosmeticList.add(new NBTTagString(cos.name()));
			
		}

		tag.set(NBTConstants.KEY_SPacketUpdatePlayerLooks_cosmetics, cosmeticList);
		
		data.a(tag);
	}

}
