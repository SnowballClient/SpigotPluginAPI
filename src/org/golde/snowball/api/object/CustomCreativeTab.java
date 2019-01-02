package org.golde.snowball.api.object;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.golde.snowball.plugin.PacketManager;
import org.golde.snowball.plugin.custom.CustomObjectName;
import org.golde.snowball.plugin.packets.server.SPacketAddCreativeTab;
import org.golde.snowball.shared.util.StringHelper;

public class CustomCreativeTab extends CustomObjectName {
	
	private final net.minecraft.server.v1_12_R1.ItemStack icon;
	
	public CustomCreativeTab(String name) {
		this(name, null);
	}
	
	public CustomCreativeTab(String name, org.bukkit.inventory.ItemStack iconIn) {
		super(name);
		if(iconIn == null) {
			iconIn = new org.bukkit.inventory.ItemStack(Material.AIR);
		}
		this.icon = org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack.asNMSCopy(iconIn); //turn bukkit itemstack into net.minecraft itemstack
	}

	@Override
	public void registerServer() { /*No need to register server side, tabs are client side */ }

	@Override
	public void registerClient(Player player) {
		PacketManager.sendPacket(player, PacketManager.S_PACKET_ADD_CREATIVE_TAB, new SPacketAddCreativeTab(icon, this.name));
	}

	@Override
	public int getId() {return 0; /* This does not matter, creative tabs do not have ids */}
	
	public final String getUnlocalizedName() {
		return unlocalizedName;
	}
	
}
