package org.golde.snowball.plugin.packets.server;

import java.io.IOException;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.golde.snowball.shared.nbt.NBTConstants;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;

public class SPacketShowToast extends SPacket {

	private final String title;
	private final String desc;
	private final net.minecraft.server.v1_12_R1.ItemStack icon;
	
	public SPacketShowToast(String title, String desc) {
		this(title, desc, null);
	}
	
	public SPacketShowToast(String title, String desc, org.bukkit.inventory.ItemStack icon) {
		this.title = title;
		this.desc = desc;
		
		if(icon == null) {
			this.icon = null;
		}
		else {
			this.icon = CraftItemStack.asNMSCopy(icon);
		}
	}
	
	@Override
	public void writePacketData(PacketDataSerializer data) throws IOException {
		NBTTagCompound tag = new NBTTagCompound();
		
		tag.setString(NBTConstants.KEY_SPacketShowToast_title, title);
		tag.setString(NBTConstants.KEY_SPacketShowToast_desc, desc);
		tag.setBoolean(NBTConstants.KEY_SPacketShowToast_hasIcon, (icon != null));
		
		data.a(tag);
		
		if(icon != null) {
			data.a(icon);
		}
	}

}
