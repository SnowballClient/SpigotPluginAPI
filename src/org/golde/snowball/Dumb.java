package org.golde.snowball;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.inventivetalent.packetlistener.PacketListenerAPI;
import org.inventivetalent.packetlistener.handler.PacketHandler;
import org.inventivetalent.packetlistener.handler.ReceivedPacket;
import org.inventivetalent.packetlistener.handler.SentPacket;

import net.minecraft.server.v1_12_R1.PacketHandshakingInSetProtocol;
import net.minecraft.server.v1_12_R1.PacketStatusInPing;
import net.minecraft.server.v1_12_R1.PacketStatusInStart;

public class Dumb {

	public static volatile boolean HAS_RECIEVED_PACKET = false;
	
	public static void onEnable() {
		PacketListenerAPI.addPacketHandler(new PacketHandler() {

            @Override
            public void onSend(SentPacket packet) {
            }

            @Override
            public void onReceive(ReceivedPacket packet) {
            	//System.out.println("onRecieve " + packet.getPacketName());
//            	if(packet.getPacket() instanceof PacketPlayInCustomPayload) {
//            		PacketPlayInCustomPayload newPacket = (PacketPlayInCustomPayload) packet.getPacket();
//            		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "PacketPlayInCustomPayload");
//            		Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + newPacket.a());
//            	}
            	
            	if(packet.getPacket() instanceof PacketHandshakingInSetProtocol) {
            		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "PacketHandshakingInSetProtocol");
            		PacketHandshakingInSetProtocol newPacket = (PacketHandshakingInSetProtocol)packet.getPacket();
            		Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + newPacket.hostname + " " + newPacket.port);
            		if(newPacket.hostname.equals("I am a snowball client!") && newPacket.port == 1337) {
            			Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "================ AUTHORISED =================");
            			HAS_RECIEVED_PACKET = true;
            		}
            	}
            	
            	if(packet.getPacket() instanceof PacketStatusInStart || packet.getPacket() instanceof PacketStatusInPing) {
            		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "reset");
            		HAS_RECIEVED_PACKET = false;
            	}
            	
            	
            }

        });
	}
	
}
