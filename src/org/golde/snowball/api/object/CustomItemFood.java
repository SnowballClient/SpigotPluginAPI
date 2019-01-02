package org.golde.snowball.api.object;

import org.bukkit.entity.Player;
import org.golde.snowball.api.models.ItemModel;
import org.golde.snowball.plugin.PacketManager;
import org.golde.snowball.plugin.custom.CustomItemFoodDrinkAbstract;
import org.golde.snowball.plugin.packets.server.SPacketAddItem;

import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.ItemFood;

public class CustomItemFood extends CustomItemFoodDrinkAbstract {

	private int properties_food_amount = 5;
	private float properties_food_saturation = 0.6f;
	private boolean properties_food_isWolfFood = false;
	private boolean properties_food_alwaysEdible = false;
	
	public CustomItemFood(String name, ItemModel model, String texture) {
		super(name, model, texture);
	}
	
	public void setFoodAmount(int amount) {
		properties_food_amount = amount;
	}
	
	public void setSaturation(float saturation) {
		properties_food_saturation = saturation;
	}
	
	public void setIsWolfFood() {
		properties_food_isWolfFood = true;
	}
	
	public void setAlwaysEdible() {
		properties_food_alwaysEdible = true;
	}
	
	@Override
	public void registerClient(Player player) {
		PacketManager.sendPacket(player, PacketManager.S_PACKET_ADD_ITEM, new SPacketAddItem(id, name, texture, (ItemModel)model, properties_creativeTab, properties_maxStackSize, properties_glowing, properties_food_drink_maxItemUseDuration, properties_food_amount, properties_food_saturation, properties_food_isWolfFood, properties_food_alwaysEdible));
	}
	
	@Override
	public Item getItemToBeRegistered() {
		return new DummyItemFood();
	}
	
	private class DummyItemFood extends ItemFood {

		public DummyItemFood() {
			super(properties_food_amount, properties_food_saturation, properties_food_isWolfFood);
			
			//From DummyItem in CustomItem.java
			this.b(net.minecraft.server.v1_12_R1.CreativeModeTab.b); //This does not half to match the client.. Not sure if its even used on the server tbh
			c("snowball_" + name); //name
			d(properties_maxStackSize);
			//f("snowball:" + texture); //texture
			
			if(properties_food_alwaysEdible) {
				this.h();
			}
			
		}
		
	}
	

}
