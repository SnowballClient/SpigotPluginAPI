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

public class CustomItem extends CustomBlockItemShared {

	protected CustomCreativeTab properties_creativeTab;
	protected int properties_maxStackSize= 64;
	protected boolean properties_glowing = false;
	
	public CustomItem(String name, ItemModel model, String texture) {
		super(name, texture, model);
	}
	
	public void setCreativeTab(CustomCreativeTab creativeTab) {
		this.properties_creativeTab = creativeTab;
	}
	
	public void setMaxStackSize(int maxStackSize) {
		this.properties_maxStackSize = maxStackSize;
	}
	
	public void setGlowing() {
		properties_glowing = true;
	}
	
	public Item getItemToBeRegistered() {
		return new ItemDummy();
	}

	@Override
	public void registerServer() {
		Item.REGISTRY.a(id,	new MinecraftKey(Constants.MINECRAFT_KEY, "snowball_" + id), getItemToBeRegistered());
		refreshServerEnum();
	}

	@Override
	public void registerClient(Player player) {
		PacketManager.sendPacket(player, PacketManager.S_PACKET_ADD_ITEM, new SPacketAddItem(id, name, texture, (ItemModel)model, properties_creativeTab, properties_maxStackSize, properties_glowing));
	}
	
	public class ItemDummy extends Item {
		public ItemDummy() {
			this.b(net.minecraft.server.v1_12_R1.CreativeModeTab.b); //This does not half to match the client.. Not sure if its even used on the server tbh
			c("snowball_" + name); //name
			d(properties_maxStackSize);
			//f("snowball:" + texture); //texture
		}
	}

}
