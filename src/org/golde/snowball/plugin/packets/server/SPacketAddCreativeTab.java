package org.golde.snowball.plugin.packets.server;

import java.io.IOException;

import org.golde.snowball.shared.nbt.NBTConstants;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;

public class SPacketAddCreativeTab extends SPacket {

	private final ItemStack icon;
	private final String rawName;
	private final boolean hasIcon;
	
	public SPacketAddCreativeTab(ItemStack icon, String rawName) {
		this.icon = icon;
		hasIcon = !(icon == null || icon.isEmpty());
		this.rawName = rawName;
	}
	
	@Override
	public void writePacketData(PacketDataSerializer data) throws IOException {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString(NBTConstants.KEY_NAME, rawName);
		tag.setBoolean(NBTConstants.KEY_CREATIVE_TAB_ICON, hasIcon);
		
		data.a(tag);
		if(hasIcon) {
			data.a(icon);
		}
	}

}
