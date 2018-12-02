package org.golde.snowball.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.golde.snowball.api.object.CustomBlock;
import org.golde.snowball.api.object.CustomEnchantment;
import org.golde.snowball.api.object.CustomItem;
import org.golde.snowball.api.object.SnowballPlayer;
import org.golde.snowball.plugin.MainPlugin;
import org.golde.snowball.plugin.PacketManager;
import org.golde.snowball.plugin.packets.server.SPacketShowToast;
import org.golde.snowball.plugin.packets.server.SPacketUpdatePlayerLooks;
import org.golde.snowball.shared.util.StringHelper;

public class SnowballAPI {

	private final String pluginIdentifier;
	
	public SnowballAPI(JavaPlugin pluginInstance) {
		this.pluginIdentifier = StringHelper.sanatise(pluginInstance.getName());
	}
	
	public static SnowballAPI getInstance(JavaPlugin pluginInstance) {
		return new SnowballAPI(pluginInstance);
	}
	
	public void addBlock(CustomBlock block) {
		//block.do_not_use_setRegistryName(pluginIdentifier);
		MainPlugin.getInstance().addedByAPI.add(block);
	}
	
	public void addItem(CustomItem item) {
		//item.do_not_use_setRegistryName(pluginIdentifier);
		MainPlugin.getInstance().addedByAPI.add(item);
	}
	
	public void addEnchantment(CustomEnchantment ench) {
		MainPlugin.getInstance().addedByAPI.add(ench);
	}
	
	public SnowballPlayer getSnowballPlayer(Player player) {
		return MainPlugin.getInstance().getOrCreateSnowballPlayer(player);
	}
	
	
	
}
