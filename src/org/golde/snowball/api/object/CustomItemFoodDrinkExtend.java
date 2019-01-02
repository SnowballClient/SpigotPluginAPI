package org.golde.snowball.api.object;

import org.bukkit.entity.Player;
import org.golde.snowball.api.models.ItemModel;
import org.golde.snowball.plugin.MainPlugin;
import org.golde.snowball.plugin.PacketManager;
import org.golde.snowball.plugin.custom.CustomBlockItemShared;
import org.golde.snowball.plugin.packets.server.SPacketAddItem;
import org.golde.snowball.plugin.packets.server.SPacketAddItem.ItemType;
import org.golde.snowball.shared.Constants;

import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.MinecraftKey;

public class CustomItemFoodDrinkExtend extends CustomItem {

	protected int properties_food_drink_maxItemUseDuration = 32;
	
	public CustomItemFoodDrinkExtend(String name, ItemModel model, String texture) {
		super(name, model, texture);
	}
	
	public void setMaxItemUseDuration(int maxItemUseDuration) {
		this.properties_food_drink_maxItemUseDuration = maxItemUseDuration;
	}

}
