package org.golde.snowball.plugin.custom;

import org.golde.snowball.api.models.ItemModel;
import org.golde.snowball.api.object.CustomItem;

public class CustomItemFoodDrinkAbstract extends CustomItem {

	protected int properties_food_drink_maxItemUseDuration = 32;
	
	public CustomItemFoodDrinkAbstract(String name, ItemModel model, String texture) {
		super(name, model, texture);
	}

	public void setMaxItemUseDuration(int maxItemUseDuration) {
		this.properties_food_drink_maxItemUseDuration = maxItemUseDuration;
	}
	
}
