package org.golde.snowball.api.object;

import org.bukkit.entity.Player;
import org.golde.snowball.api.models.ItemModel;
import org.golde.snowball.plugin.MainPlugin;
import org.golde.snowball.plugin.PacketManager;
import org.golde.snowball.plugin.custom.CustomBlockItemShared;
import org.golde.snowball.plugin.packets.server.SPacketAddItem;
import org.golde.snowball.shared.Constants;

import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.MinecraftKey;

public class CustomItem extends CustomBlockItemShared{

	public CustomItem(String name, ItemModel model, String texture) {
		super(name, texture, model);
	}

	@Override
	public void registerServer() {
		ItemDummy item = new ItemDummy();
		Item.REGISTRY.a(id,	new MinecraftKey(Constants.MINECRAFT_KEY, "snowball_" + id), item);
		refreshServerEnum();
	}

	@Override
	public void registerClient(Player player) {
		PacketManager.sendPacket(player, PacketManager.S_PACKET_ADD_ITEM, new SPacketAddItem(id, name, texture, (ItemModel)model));
	}
	
	private class ItemDummy extends Item {
		public ItemDummy() {
			this.b(net.minecraft.server.v1_12_R1.CreativeModeTab.b); //This does not half to match the client.. Not sure if its even used on the server tbh
			c("snowball_" + name); //name
			//f("snowball:" + texture); //texture
		}
	}

}
