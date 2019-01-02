package org.golde.snowball.plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.entity.Player;
import org.golde.snowball.plugin.packets.PacketIds;
import org.golde.snowball.plugin.packets.SnowballPacket;
import org.golde.snowball.plugin.packets.client.CPacketKeyPress;
import org.golde.snowball.plugin.packets.server.SPacketAddBlock;
import org.golde.snowball.plugin.packets.server.SPacketAddCreativeTab;
import org.golde.snowball.plugin.packets.server.SPacketAddEnchantment;
import org.golde.snowball.plugin.packets.server.SPacketAddItem;
import org.golde.snowball.plugin.packets.server.SPacketInfo;
import org.golde.snowball.plugin.packets.server.SPacketRefreshResources;
import org.golde.snowball.plugin.packets.server.SPacketShowToast;
import org.golde.snowball.plugin.packets.server.SPacketTTS;
import org.golde.snowball.plugin.packets.server.SPacketUpdatePlayerSkin;
import org.golde.snowball.plugin.packets.server.SPacketUpdatePlayerUsername;
import org.golde.snowball.shared.Constants;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.PacketType.Protocol;
import com.comphenix.protocol.PacketType.Sender;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import com.google.common.collect.BiMap;

import net.minecraft.server.v1_12_R1.EnumProtocol;
import net.minecraft.server.v1_12_R1.EnumProtocolDirection;
import net.minecraft.server.v1_12_R1.Packet;

public class PacketManager {

	public static final PacketType S_PACKET_ADD_BLOCK = new PacketType(Protocol.PLAY, Sender.SERVER, PacketIds.SPacketAddBlock, -1);
	public static final PacketType S_PACKET_ADD_ITEM = new PacketType(Protocol.PLAY, Sender.SERVER, PacketIds.SPacketAddItem, -1);
	public static final PacketType S_PACKET_REFRESH_RESOURCES = new PacketType(Protocol.PLAY, Sender.SERVER, PacketIds.SPacketRefreshResources, -1);
	public static final PacketType S_PACKET_INFO = new PacketType(Protocol.PLAY, Sender.SERVER, PacketIds.SPacketInfo, -1);
	public static final PacketType S_PACKET_SHOW_TOAST = new PacketType(Protocol.PLAY, Sender.SERVER, PacketIds.SPacketShowToast, -1);
	public static final PacketType S_PACKET_ADD_ENCHANTMENT = new PacketType(Protocol.PLAY, Sender.SERVER, PacketIds.SPacketAddEnchantment, -1);
	public static final PacketType S_PACKET_ADD_CREATIVE_TAB = new PacketType(Protocol.PLAY, Sender.SERVER, PacketIds.SPacketAddCreativeTab, -1);
	public static final PacketType S_PACKET_TTS = new PacketType(Protocol.PLAY, Sender.SERVER, PacketIds.SPacketTTS, -1);
	public static final PacketType S_PACKET_UPDATE_PLAYER_SKIN = new PacketType(Protocol.PLAY, Sender.SERVER, PacketIds.SPacketUpdatePlayerSkin, -1);
	public static final PacketType S_PACKET_UPDATE_PLAYER_USERNAME = new PacketType(Protocol.PLAY, Sender.SERVER, PacketIds.SPacketUpdatePlayerUsername, -1);
	
	public static final PacketType C_PACKET_KEYPRESS = new PacketType(Protocol.PLAY, Sender.CLIENT, PacketIds.CPacketKeyPress, -1);
	
	public static final void registerPackets() {

		registerPacket(SPacketAddBlock.class, S_PACKET_ADD_BLOCK);
		registerPacket(SPacketAddItem.class, S_PACKET_ADD_ITEM);
		registerPacket(SPacketRefreshResources.class, S_PACKET_REFRESH_RESOURCES);
		registerPacket(SPacketInfo.class, S_PACKET_INFO);
		registerPacket(SPacketShowToast.class, S_PACKET_SHOW_TOAST);
		registerPacket(SPacketAddEnchantment.class, S_PACKET_ADD_ENCHANTMENT);
		registerPacket(SPacketAddCreativeTab.class, S_PACKET_ADD_CREATIVE_TAB);
		registerPacket(SPacketTTS.class, S_PACKET_TTS);
		registerPacket(SPacketUpdatePlayerSkin.class, S_PACKET_UPDATE_PLAYER_SKIN);
		registerPacket(SPacketUpdatePlayerUsername.class, S_PACKET_UPDATE_PLAYER_USERNAME);
		
		registerPacket(CPacketKeyPress.class, C_PACKET_KEYPRESS);
		
	}
	
	private static void registerPacket(Class packetClass, PacketType packetType) {
		
		final int packetId = packetType.getCurrentId();
		final EnumProtocol protocol = EnumProtocol.PLAY;
		
		final EnumProtocolDirection enumProtocolDirection = packetType.isClient() ? EnumProtocolDirection.SERVERBOUND : EnumProtocolDirection.CLIENTBOUND; //clientbound and serverbound are shitty reverse names
		
		try {
			//protocol.b().put(packetId, packetClass); //1.12 fix
			Map<EnumProtocolDirection, BiMap<Integer, Class<? extends Packet<?>>>> theMap = (Map<EnumProtocolDirection, BiMap<Integer, Class<? extends Packet<?>>>>) FieldUtils.readField(protocol, "h", true);//protocol.b()
			BiMap<Integer, Class<? extends Packet<?>>> biMap = theMap.get(enumProtocolDirection);
			biMap.put(packetId, packetClass);
			theMap.put(enumProtocolDirection, biMap);
			FieldUtils.writeField(protocol, "h", theMap, true);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		Map<Class<?>, EnumProtocol> map = (Map<Class<?>, EnumProtocol>) Accessors.getFieldAccessor(EnumProtocol.class, Map.class, true).get(protocol);
		map.put(packetClass, protocol);
		
		if(MainPlugin.getInstance().isDEBUG()) {MainPlugin.getInstance().getLogger().info("Registered '" + packetClass.getSimpleName() + "' with id " + packetId );}
		
		
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(MainPlugin.getInstance(), packetType) {
			@Override
			public void onPacketSending(PacketEvent event) {
				//System.out.println("onPacketSending " + event.getPacket().getHandle());
			}
			
			@Override
			public void onPacketReceiving(PacketEvent event) {
				//System.out.println("onPacketReceiving " + event.getPacket().getHandle());
			}
		});
		
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(MainPlugin.getInstance(), PacketType.Status.Server.SERVER_INFO) {
			
			@Override
			public void onPacketSending(PacketEvent event) {
				WrappedServerPing ping = (WrappedServerPing)event.getPacket().getServerPings().read(0);
				ping.setVersionProtocol(Constants.PROTOCOL_VERSION);
				ping.setVersionName("Snowball v" + Constants.SNOWBALL_VERSION);
			}
			
		});
	}
	
	public static final void sendPacket(Player player, PacketType packetType, SnowballPacket packet) {
		PacketContainer container = new PacketContainer(packetType, packet);

		if(MainPlugin.getInstance().isDEBUG()) {System.out.println("Sending packet '" + packet.getClass().getSimpleName() + "' to player '" + player.getName() + "'");}
		
		try {
			ProtocolLibrary.getProtocolManager().sendServerPacket(player, container);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
}
