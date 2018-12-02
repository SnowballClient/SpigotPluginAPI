package org.golde.snowball.plugin.custom;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.entity.Player;
import org.golde.snowball.plugin.MainPlugin;
import org.golde.snowball.plugin.packets.SnowballPacket;
import org.golde.snowball.plugin.packets.server.SPacketAddBlock;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;

public interface CustomObject {
	
	public void registerServer();
	public void registerClient(Player player);
	public int getId();

}
