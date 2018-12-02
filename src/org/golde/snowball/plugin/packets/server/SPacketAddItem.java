package org.golde.snowball.plugin.packets.server;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.golde.snowball.api.models.ItemModel;
import org.golde.snowball.plugin.packets.SnowballPacket;
import org.golde.snowball.shared.nbt.NBTConstants;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import net.minecraft.server.v1_12_R1.PacketListener;

public class SPacketAddItem extends SPacket {

	private final String name;
	private final String texture;
	private final int id;
	private final ItemModel model;

	public SPacketAddItem(int id, String name, String texture, ItemModel model) {
		this.id = id;
		this.name = name;
		this.texture = texture;
		this.model = model;
	}

	@Override
	public void writePacketData(PacketDataSerializer data) throws IOException {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInt(NBTConstants.KEY_ID, id);
		tag.setString(NBTConstants.KEY_NAME, name);
		tag.setString(NBTConstants.KEY_TEXTURE, texture);
		tag.setString(NBTConstants.KEY_ITEM_MODEL_JSON_ITEM, model.getModel(id));
		data.a(tag);
	}

}
