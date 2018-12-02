package org.golde.snowball.plugin.packets.server;

import java.io.IOException;

import org.golde.snowball.api.enums.SnowballEnchantmentEnum;
import org.golde.snowball.shared.nbt.NBTConstants;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NBTTagString;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;

public class SPacketAddEnchantment extends SPacket  {

	private final int id;
	private final SnowballEnchantmentEnum.SnowballEnchantmentRarity rarity;
	private final SnowballEnchantmentEnum.SnowballEnchantmentType item;
	private final SnowballEnchantmentEnum.SnowballEnchantmentSlot[] slot;
	private final String name;
	
	private final int property_minEnchantmentLevel;
	private final int property_maxEnchantmentLevel;
	private final boolean property_cursedEnchantment;
	private final boolean property_treasureEnchantment;
	
	public SPacketAddEnchantment(int id, String name, SnowballEnchantmentEnum.SnowballEnchantmentRarity rarity, SnowballEnchantmentEnum.SnowballEnchantmentType item, SnowballEnchantmentEnum.SnowballEnchantmentSlot[] slot, int property_minEnchantmentLevel, int property_maxEnchantmentLevel, boolean property_cursedEnchantment, boolean property_treasureEnchantment) {
		this.id = id;
		this.name = name;
		this.rarity = rarity;
		this.item = item;
		this.slot = slot;
		this.property_minEnchantmentLevel = property_minEnchantmentLevel;
		this.property_maxEnchantmentLevel = property_maxEnchantmentLevel;
		this.property_cursedEnchantment = property_cursedEnchantment;
		this.property_treasureEnchantment = property_treasureEnchantment;
	}
	
	@Override
	public void writePacketData(PacketDataSerializer data) throws IOException {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInt(NBTConstants.KEY_ID, id);
		tag.setString(NBTConstants.KEY_NAME, name);
		
		tag.setString(NBTConstants.KEY_ENCHANTMENT_RARITY, rarity.name());
		tag.setString(NBTConstants.KEY_ENCHANTMENT_ITEM, item.name());
		
		NBTTagList slots = new NBTTagList();
		for(SnowballEnchantmentEnum.SnowballEnchantmentSlot theSlot : slot) {
			slots.add(new NBTTagString(theSlot.name()));
		}
		
		tag.set(NBTConstants.KEY_ENCHANTMENT_SLOTS, slots);
		
		NBTTagCompound properties = new NBTTagCompound();
		
		properties.setInt(NBTConstants.KEY_ENCHANTMENT_PROPERTIES_minEnchantmentLevel, property_minEnchantmentLevel);
		properties.setInt(NBTConstants.KEY_ENCHANTMENT_PROPERTIES_maxEnchantmentLevel, property_maxEnchantmentLevel);
		properties.setBoolean(NBTConstants.KEY_ENCHANTMENT_PROPERTIES_cursedEnchantment, property_cursedEnchantment);
		properties.setBoolean(NBTConstants.KEY_ENCHANTMENT_PROPERTIES_treasureEnchantment, property_treasureEnchantment);
		
		tag.set(NBTConstants.KEY_PROPERTIES, properties);
		
		data.a(tag);
	}

}
