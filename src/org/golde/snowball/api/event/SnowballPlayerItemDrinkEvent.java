package org.golde.snowball.api.event;

import org.bukkit.inventory.ItemStack;
import org.golde.snowball.api.object.SnowballPlayer;
import org.golde.snowball.plugin.event.SnowballEventPlayerCancellable;

public class SnowballPlayerItemDrinkEvent extends SnowballEventPlayerCancellable {

	private final ItemStack itemstack;
	
	public SnowballPlayerItemDrinkEvent(SnowballPlayer player, ItemStack itemstack) {
		super(player);
		this.itemstack = itemstack;
	}
	
	public ItemStack getItemStack() {
		return itemstack.clone();
	}

}
