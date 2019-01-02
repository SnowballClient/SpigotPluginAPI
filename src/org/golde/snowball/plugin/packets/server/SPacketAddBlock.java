package org.golde.snowball.plugin.packets.server;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.golde.snowball.api.enums.SnowballMaterial;
import org.golde.snowball.api.enums.SnowballStepSound;
import org.golde.snowball.api.models.BlockModel;
import org.golde.snowball.api.object.CustomCreativeTab;
import org.golde.snowball.plugin.models.Model;
import org.golde.snowball.plugin.packets.SnowballPacket;
import org.golde.snowball.shared.nbt.NBTConstants;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import net.minecraft.server.v1_12_R1.PacketListener;

public class SPacketAddBlock extends SPacket {

	private final String registryName;
	private final String name;
	private final String texture;
	private final int id;
	private final BlockModel model;
	
	private final float properties_light;
	private final float properties_resistance;
	private final float properties_hardness;
	private final boolean properties_transparent;
	private final SnowballMaterial properties_material;
	private final boolean properties_silktouch;
	private final SnowballStepSound properties_stepsound;
	private final CustomCreativeTab properties_tab;

	public SPacketAddBlock(String registryName, int id, String name, String texture, BlockModel model, float properties_light, float properties_resistance, float properties_hardness, boolean properties_transparent, SnowballMaterial property_material, boolean property_silktouch, SnowballStepSound properties_stepsound, CustomCreativeTab properties_tab) {
		this.registryName = registryName;
		this.id = id;
		this.name = name;
		this.texture = texture;
		this.model = model;
		this.properties_light = properties_light;
		this.properties_hardness = properties_hardness;
		this.properties_resistance = properties_resistance;
		this.properties_transparent = properties_transparent;
		this.properties_material = property_material;
		this.properties_silktouch = property_silktouch;
		this.properties_stepsound = properties_stepsound;
		this.properties_tab = properties_tab;
	}

	@Override
	public void writePacketData(PacketDataSerializer data) throws IOException {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInt(NBTConstants.KEY_ID, id);
		tag.setString(NBTConstants.KEY_NAME, name);
		tag.setString(NBTConstants.KEY_TEXTURE, texture);
		tag.setString(NBTConstants.KEY_REGISTRY, registryName);
		
		tag.setString(NBTConstants.KEY_BLOCK_MODEL_JSON_BLOCK, model.getModel(id));
		tag.setString(NBTConstants.KEY_ITEM_MODEL_JSON_ITEM, model.getItemModel().getModel(id));
		tag.setString(NBTConstants.KEY_BLOCK_MODEL_JSON_STATE, model.getBlockState().getModel(id));
		
		NBTTagCompound properties = new NBTTagCompound();
		
		properties.setFloat(NBTConstants.KEY_BLOCK_PROPERTIES_LIGHT, properties_light);
		properties.setFloat(NBTConstants.KEY_BLOCK_PROPERTIES_RESISTANCE, properties_resistance);
		properties.setFloat(NBTConstants.KEY_BLOCK_PROPERTIES_HARDNESS, properties_hardness);
		properties.setBoolean(NBTConstants.KEY_BLOCK_PROPERTIES_TRANSPARENT, properties_transparent);
		properties.setString(NBTConstants.KEY_BLOCK_PROPERTIES_MATERIAL, properties_material.name());
		properties.setBoolean(NBTConstants.KEY_BLOCK_PROPERTIES_SILKTOUCH, properties_silktouch);
		properties.setString(NBTConstants.KEY_BLOCK_PROPERTIES_STEPSOUND, properties_stepsound.name());
		properties.setString(NBTConstants.KEY_BLOCK_ITEM_TAB, properties_tab == null ? "null" : properties_tab.getUnlocalizedName());
		
		tag.set(NBTConstants.KEY_PROPERTIES, properties);
		
		data.a(tag);
	}

}
