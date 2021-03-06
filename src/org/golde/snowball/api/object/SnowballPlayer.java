package org.golde.snowball.api.object;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.golde.snowball.plugin.MainPlugin;
import org.golde.snowball.plugin.PacketManager;
import org.golde.snowball.plugin.packets.server.SPacketShowToast;
import org.golde.snowball.plugin.packets.server.SPacketTTS;
import org.golde.snowball.plugin.packets.server.SPacketUpdatePlayerSkin;
import org.golde.snowball.plugin.packets.server.SPacketUpdatePlayerUsername;

public class SnowballPlayer {

	private final Player player;
	private String skinUrl = "null";
	private String customUsername;

	public SnowballPlayer(Player player) {
		this.player = player;
		customUsername = player.getName();
	}

	public Player getBukkitPlayer() {
		return player;
	}

	public boolean hasCustomSkin() {
		return !skinUrl.equals("null");
	}

	public void sendToast(String title, String desc) {
		sendToast(title, desc, null);
	}

	public void sendToast(String title, String desc, ItemStack icon) {
		PacketManager.sendPacket(player, PacketManager.S_PACKET_SHOW_TOAST, new SPacketShowToast(title, desc, icon));
	}

	public void removeCustomSkin() {
		this.setCustomSkin("null");
	}

	public void setCustomSkin(String skinUrl) {
		this.skinUrl = skinUrl;
		for(Player p : Bukkit.getOnlinePlayers()) {
			PacketManager.sendPacket(p, PacketManager.S_PACKET_UPDATE_PLAYER_SKIN, new SPacketUpdatePlayerSkin(player, skinUrl));
		}
	}

	public void resetCustomUsername() {
		this.setCustomUsername("null");
	}

	public void setCustomUsername(String name) {
		if(name == null || name.equals("null")) {
			name = player.getName();
		}
		customUsername = name;
		player.setPlayerListName(name);
		for(Player p : Bukkit.getOnlinePlayers()) {
			PacketManager.sendPacket(p, PacketManager.S_PACKET_UPDATE_PLAYER_USERNAME, new SPacketUpdatePlayerUsername(player, customUsername));
		}
	}
	
	public void refreshClientWorld() {
		final Location PLAYER_LOCATION = player.getLocation();
		Location newLoc = PLAYER_LOCATION.clone();
		newLoc.setWorld(Bukkit.getWorld(MainPlugin.getInstance().getWorldName()));
		player.teleport(newLoc, TeleportCause.PLUGIN);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				player.teleport(PLAYER_LOCATION, TeleportCause.PLUGIN);
			}
		}.runTaskLater(MainPlugin.getInstance(), 2);
	}
	
	public void sendTTS(String msg) {
		PacketManager.sendPacket(player, PacketManager.S_PACKET_TTS, new SPacketTTS(msg));
	}

}
