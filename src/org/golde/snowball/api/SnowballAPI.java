package org.golde.snowball.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.golde.snowball.api.object.CustomBlock;
import org.golde.snowball.api.object.CustomCreativeTab;
import org.golde.snowball.api.object.CustomEnchantment;
import org.golde.snowball.api.object.CustomItem;
import org.golde.snowball.api.object.CustomItemDrink;
import org.golde.snowball.api.object.CustomItemFood;
import org.golde.snowball.api.object.SnowballPlayer;
import org.golde.snowball.plugin.MainPlugin;
import org.golde.snowball.plugin.custom.CustomObject;
import org.golde.snowball.shared.util.StringHelper;

public class SnowballAPI {

	private final String pluginIdentifier;
	private static final List<CustomObject> OBJECTS = new ArrayList<CustomObject>();
	
	public SnowballAPI(JavaPlugin pluginInstance) {
		this.pluginIdentifier = StringHelper.sanatise(pluginInstance.getName(), true, true);
	}
	
	public static SnowballAPI getInstance(JavaPlugin pluginInstance) {
		return new SnowballAPI(pluginInstance);
	}
	
	public void addBlock(CustomBlock block) {
		//block.do_not_use_setRegistryName(pluginIdentifier);
		OBJECTS.add(block);
	}
	
	public void addItem(CustomItem item) {
		//item.do_not_use_setRegistryName(pluginIdentifier);
		OBJECTS.add(item);
	}
	
	/*
	 * Alright, yeah this might look strange because THERETICALLY it would work if you just pass a custom food item into the additem function 
	 * because it extends customitem, but it doesnt. Who knows. I tried for 3 hours fixing this. 
	 * Some bug in bukkit or java made this happen. Just leave this class along and dont delete these methods what ever you do.
	 * Same thing goes with addItem(CustomItemDrink).
	 * 
	 */
	public void addItem(CustomItemFood item) {
		OBJECTS.add(item);
	}
	
	public void addItem(CustomItemDrink item) {
		OBJECTS.add(item);
	}
	
	public void addEnchantment(CustomEnchantment ench) {
		OBJECTS.add(ench);
	}
	
	public void addCreativeTab(CustomCreativeTab creativeTab) {
		OBJECTS.add(creativeTab);
	}
	
	public SnowballPlayer getSnowballPlayer(Player player) {
		return MainPlugin.getInstance().getOrCreateSnowballPlayer(player);
	}
	
	@Deprecated
	public static List<CustomObject> do_not_use_me_getObjects(){
		return OBJECTS;
	}
	
	
	
}
