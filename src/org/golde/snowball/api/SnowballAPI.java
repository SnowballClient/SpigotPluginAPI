package org.golde.snowball.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.golde.snowball.api.object.CustomBlock;
import org.golde.snowball.api.object.CustomEnchantment;
import org.golde.snowball.api.object.CustomItem;
import org.golde.snowball.api.object.SnowballPlayer;
import org.golde.snowball.plugin.MainPlugin;
import org.golde.snowball.plugin.custom.CustomObject;
import org.golde.snowball.shared.util.StringHelper;

public class SnowballAPI {

	private final String pluginIdentifier;
	private static final List<CustomObject> OBJECTS = new ArrayList<CustomObject>();
	
	public SnowballAPI(JavaPlugin pluginInstance) {
		this.pluginIdentifier = StringHelper.sanatise(pluginInstance.getName());
	}
	
	public static SnowballAPI getInstance(JavaPlugin pluginInstance) {
		return new SnowballAPI(pluginInstance);
	}
	
	public void addBlock(CustomBlock block) {
		//block.do_not_use_setRegistryName(pluginIdentifier);
		System.out.println("API: ADDED: " + block.getClass().getName());
		OBJECTS.add(block);
	}
	
	public void addItem(CustomItem item) {
		//item.do_not_use_setRegistryName(pluginIdentifier);
		System.out.println("API: ADDED: " +item.getClass().getName());
		OBJECTS.add(item);
	}
	
	public void addEnchantment(CustomEnchantment ench) {
		System.out.println("API: ADDED: " + ench.getClass().getName());
		OBJECTS.add(ench);
	}
	
	public SnowballPlayer getSnowballPlayer(Player player) {
		return MainPlugin.getInstance().getOrCreateSnowballPlayer(player);
	}
	
	@Deprecated
	public static List<CustomObject> do_not_use_me_getObjects(){
		return OBJECTS;
	}
	
	
	
}
