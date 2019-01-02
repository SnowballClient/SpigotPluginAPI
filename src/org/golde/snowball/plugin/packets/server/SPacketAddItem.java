package org.golde.snowball.plugin.packets.server;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.golde.snowball.api.models.ItemModel;
import org.golde.snowball.api.object.CustomCreativeTab;
import org.golde.snowball.plugin.packets.SnowballPacket;
import org.golde.snowball.shared.nbt.NBTConstants;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import net.minecraft.server.v1_12_R1.PacketListener;

public class SPacketAddItem extends SPacket {

	public enum ItemType {
		ITEM, FOOD, DRINK;
	}
	
	private final String name;
	private final String texture;
	private final int id;
	private final ItemModel model;
	
	private final CustomCreativeTab properties_tab;
	private final int properties_maxStackSize;
	private final boolean properties_glowing;
	
	private int properties_food_drink_maxItemUseDuration;
	
	private int properties_food_amount;
	private float properties_food_saturation;
	private boolean properties_food_isWolfFood;
	private boolean properties_food_alwaysEdible;
	
	
	private ItemType itemType;

	public SPacketAddItem(int id, String name, String texture, ItemModel model, CustomCreativeTab properties_tab, int maxStackSize, boolean glowing) {
		this.id = id;
		this.name = name;
		this.texture = texture;
		this.model = model;
		this.properties_tab = properties_tab;
		this.properties_maxStackSize = maxStackSize;
		this.properties_glowing = glowing;
		this.itemType = ItemType.ITEM;
	}
	
	public SPacketAddItem(int id, String name, String texture, ItemModel model, CustomCreativeTab properties_tab,  int maxStackSize, boolean glowing, int maxItemUseDuration) {
		this(id, name, texture, model, properties_tab, maxStackSize, glowing);
		this.itemType = ItemType.DRINK;
		this.properties_food_drink_maxItemUseDuration = maxItemUseDuration;
	}
	
	public SPacketAddItem(int id, String name, String texture, ItemModel model, CustomCreativeTab properties_tab, int maxStackSize, boolean glowing, int maxItemUseDuration, int food_amount, float food_saturation, boolean isWolfFood, boolean canAlwaysEat){
		this(id, name, texture, model, properties_tab, maxStackSize, glowing, maxItemUseDuration);
		this.itemType = ItemType.FOOD;
		this.properties_food_amount = food_amount;
		this.properties_food_saturation = food_saturation;
		this.properties_food_isWolfFood = isWolfFood;
		this.properties_food_alwaysEdible = canAlwaysEat;
	}

	@Override
	public void writePacketData(PacketDataSerializer data) throws IOException {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInt(NBTConstants.KEY_ID, id);
		tag.setString(NBTConstants.KEY_NAME, name);
		tag.setString(NBTConstants.KEY_TEXTURE, texture);
		tag.setString(NBTConstants.KEY_ITEM_MODEL_JSON_ITEM, model.getModel(id));
		
		NBTTagCompound properties = new NBTTagCompound();
		properties.setString(NBTConstants.KEY_BLOCK_ITEM_TAB, properties_tab == null ? "null" : properties_tab.getUnlocalizedName());
		properties.setBoolean(NBTConstants.KEY_ITEM_PROPERTIES_GLOWING, properties_glowing);
		properties.setInt(NBTConstants.KEY_ITEM_PROPERTIES_MAX_STACK_SIZE, properties_maxStackSize);
		
		if(itemType != ItemType.ITEM) {
			properties.setInt(NBTConstants.KEY_ITEM_FOOD_DRINK_PROPERTIES_DURATION, properties_food_drink_maxItemUseDuration);
		}
		
		if(itemType == ItemType.DRINK) {
			properties.setBoolean(NBTConstants.KEY_ITEM_IS_DRINKABLE, true);
		}
		
		if(itemType == ItemType.FOOD) {
			properties.setBoolean(NBTConstants.KEY_ITEM_IS_FOOD, true);
			properties.setInt(NBTConstants.KEY_ITEM_FOOD_PROPERTIES_AMOUNT, properties_food_amount);
			properties.setFloat(NBTConstants.KEY_ITEM_FOOD_PROPERTIES_SATURATION, properties_food_saturation);
			properties.setBoolean(NBTConstants.KEY_ITEM_FOOD_PROPERTIES_WOLF_FOOD, properties_food_isWolfFood);
			properties.setBoolean(NBTConstants.KEY_ITEM_FOOD_PROPERTIES_ALWAYS_EDITABLE, properties_food_alwaysEdible);
		}
		
		tag.set(NBTConstants.KEY_PROPERTIES, properties);
		
		System.out.println("Properties Tag for " + id + " : " + tag.toString());
		System.out.println("");
		data.a(tag);
	}

}
