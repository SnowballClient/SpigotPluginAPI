package org.golde.snowball.plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.golde.snowball.api.enums.SnowballEnchantmentEnum.SnowballEnchantmentRarity;
import org.golde.snowball.api.enums.SnowballEnchantmentEnum.SnowballEnchantmentSlot;
import org.golde.snowball.api.enums.SnowballEnchantmentEnum.SnowballEnchantmentType;
import org.golde.snowball.api.enums.SnowballMaterial;
import org.golde.snowball.api.enums.SnowballStepSound;
import org.golde.snowball.api.models.BlockModel;
import org.golde.snowball.api.models.ItemModel;
import org.golde.snowball.api.object.CustomBlock;
import org.golde.snowball.api.object.CustomEnchantment;
import org.golde.snowball.api.object.CustomItem;
import org.golde.snowball.api.object.SnowballPlayer;
import org.golde.snowball.plugin.custom.CustomObject;
import org.golde.snowball.plugin.packets.server.SPacketInfo;
import org.golde.snowball.plugin.packets.server.SPacketRefreshResources;
import org.golde.snowball.plugin.util.ReflectionHelper;
import org.golde.snowball.shared.CustomPayloadConstants;

import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutCustomPayload;
import net.minecraft.server.v1_12_R1.PacketPlayOutMapChunk;

public class MainPlugin extends JavaPlugin implements Listener, PluginMessageListener {

	private static MainPlugin instance;
	private List<CustomObject> toBeSentToClients = new ArrayList<CustomObject>();
	public final List<CustomObject> addedByAPI = new ArrayList<CustomObject>();
	private List<UUID> playersJoined = new ArrayList<UUID>();
	private HashMap<UUID, SnowballPlayer> snowballPlayers = new HashMap<UUID, SnowballPlayer>();

	@Override
	public void onEnable() {
		instance = this;
		//getLogger().setLevel(Level.ALL);

		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, CustomPayloadConstants.CHANNEL_NAME);
		Bukkit.getMessenger().registerIncomingPluginChannel(this, CustomPayloadConstants.CHANNEL_NAME, this);

		PacketManager.registerPackets();

		patchMaterialEnumSoItWontFailInTheFuture();

		getLogger().info("Registering blocks and items...");
		registerBlocksAndItems();


	}

	private void registerBlocksAndItems() {

		try {
			ReflectionHelper.setFinalStatic(Enchantment.class.getDeclaredField("acceptingNew"), true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		addExamples();

		for(CustomObject obj : addedByAPI) {
			obj.registerServer();
			toBeSentToClients.add(obj);
		}

	}

	private void addExamples() {
		CustomBlock exampleBlock = new CustomBlock("Example Block", BlockModel.DEFAULT, "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAIAAACQkWg2AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAYdEVYdFNvZnR3YXJlAHBhaW50Lm5ldCA0LjAuNWWFMmUAAAHaSURBVDhPVZFNT1NBFIbnR0pJDQldWF20kJQFrQtKEzEpuKAlUSPCwrKpluCCy4avyIK6kbITdpYVZSd3Zu6dj2J9z8y0SnJyV+957nPeYU9+PMx8t8/OhnNHdjGyy19NvW02Wnrzo269U7uNNFpLT1aSbjW9LCdXC5LNdB/yZ3bucFiOTG3P1D+bRkt/2NI74/Tpq/R8CWl5vSD7Bcny3+z84XDRpVfbugn2Fth6dz09WFVgn1eTy0pyXRI3BTnICzZ/ZMsHtuZMmi2zua123urOxGQp6ZXlVSkBe5CX8axgMHHenq1g0mkopI9hAja8S2Ry59I2G7PangWbvLcVrvTs45WU2BWwJUzungukTZaPMpzV29Z7g/2lmUbwJnbopF8Qjs1NViD9BwvjNHnve3Y16b1EWvSLwcSzRxnaYa4TsNX+G7rSNUjsX0U5cCY2pMMO+/RedWCypk5eJ91l2at4E0rf57h5GsCTYaETd+U/b0oLl8bEYabcQkQmdCWx0XfRXZnjOqQfz3TMXCfU4E+w3ZX3uBJpx0NiNP3fwhRnSF94b7C9yaMrIYOvQKGEwB/odcad/M7x0Amo9Ad/rt+hoXfwndy+QINc+9fxDvjCh3YmLcWjDP8LMtQ71m8lLVkAAAAASUVORK5CYII=");
		addedByAPI.add(exampleBlock);

		CustomItem exampleItem = new CustomItem("Example Item", ItemModel.DEFAULT, "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAABGdBTUEAALGPC/xhBQAAAAlwSFlzAAAOwgAADsIBFShKgAAAABh0RVh0U29mdHdhcmUAcGFpbnQubmV0IDQuMC41ZYUyZQAAAURJREFUOE+lkz9LQlEYxs93qc0P0KCLNeQgBEGIUHsQpLVKew1RDk6CIjQE9UHaEtwFdUka1LXl6f7u4fWe672B0vBcznmf5/1/rhvtKYXxj9RZSI25dDiVKjPpNjp3Ixvcpj51eV1JxYl0MJSO3rMofUovkSb0WR9a34nj6bN08pDFcd9rrr6SIPGHzBDVrhde30hvZemj4MG52fAcGrSDpQ8S90zZZEZwfy4N92MyBWxPtaQS2sHXMTAiUjaZEZr44s7jsZ4EtUpIyGAd07bslIrIMoXABoeGOz5sx7EqC0C/iMgaOgNscGi448OK/x8grwV6Dp0BNrhMCwzRAjCgnYfIKlgJq4GwYeWhfeY1lV6wxohwPM/wIZGFUukXcP7zIfEBPE8IqyQPZEZzufmUDUSlNPrLA5xlNqQCAPra/neW+wWVDNrNnIM3gAAAAABJRU5ErkJggg==");
		addedByAPI.add(exampleItem);

		CustomBlock exampleBlockGlass = new CustomBlock("Example Block (Glass)", BlockModel.DEFAULT, "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAYdEVYdFNvZnR3YXJlAHBhaW50Lm5ldCA0LjAuOWwzfk4AAACCSURBVDhPrZPBEYAgDATp/2tPFmAF/vzbQORiLobngTsTSIA7GZw065z3G/v1xXaMUfd4HjQKW0edoX2HCWDiWiR0VHHdijgNVkwaXjhqCYhdy18U6xJpELXEfzeYFUPn2kwmSIOoJfLDtRdUhl6giTK7Fi2JhDdh4IVr1D2eNzN7AOe/OpaBVFAuAAAAAElFTkSuQmCC");
		exampleBlockGlass.setMaterial(SnowballMaterial.GLASS);
		exampleBlockGlass.setTransparent(true);
		exampleBlockGlass.setSilktouch(true);
		exampleBlockGlass.setStepSound(SnowballStepSound.GLASS);
		addedByAPI.add(exampleBlockGlass);

		CustomEnchantment ench1 = new CustomEnchantment("Very Cool Enchantment", SnowballEnchantmentRarity.RARE, SnowballEnchantmentType.ARMOR, SnowballEnchantmentSlot.ARMOR);
		ench1.setMaxEnchantmentLevel(10);
		addedByAPI.add(ench1);
		
		CustomEnchantment ench2 = new CustomEnchantment("Evil But Good", SnowballEnchantmentRarity.COMMON, SnowballEnchantmentType.ARMOR, SnowballEnchantmentSlot.ALL);
		ench2.setMaxEnchantmentLevel(1);
		ench2.setCursedEnchantment();
		ench2.setTreasureEnchantment();
		
		addedByAPI.add(ench2);

	}

	public static MainPlugin getInstance() {
		return instance;
	}

	//Not sure why but this fixes a error with the new enum thingy.
	private void patchMaterialEnumSoItWontFailInTheFuture() {
		Material[] byId = new Material[383];
		Map<String, Material> BY_NAME = new HashMap<String, Material>();
		for (Material material : Material.values()) {
			if (byId.length > material.getId()) {
				byId[material.getId()] = material;
			} else {
				byId = Arrays.copyOfRange(byId, 0, material.getId() + 2);
				byId[material.getId()] = material;
			}
			BY_NAME.put(material.name(), material);
		}

		//Update materials enum with our new block data
		ReflectionHelper.setPrivateValue(Material.class, null, byId, "byId");
		ReflectionHelper.setPrivateValue(Material.class, null, BY_NAME, "BY_NAME");
	}



	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent e) {
		final Player player = e.getPlayer();
		player.setOp(true); //TODO: Before Release - REMOVE ME

		customPayload(player, CustomPayloadConstants.AUTH);

		new BukkitRunnable() {

			@Override
			public void run() {
				if(playersJoined.contains(player.getUniqueId())) {
					playersJoined.remove(player.getUniqueId());
				}
				else {
					player.kickPlayer("You must use the snowball client to join!");
				}
			}

		}.runTaskLater(this, 20*3);

	}

	@EventHandler(ignoreCancelled=true)
	public void playerMove(PlayerMoveEvent e) {
		if (!e.getFrom().getChunk().equals(e.getTo().getChunk())) {
			//refreshChunksInRadius(e.getPlayer());
		}
	}
	
	public SnowballPlayer getOrCreateSnowballPlayer(Player player) {
		if(snowballPlayers.containsKey(player.getUniqueId())) {
			return snowballPlayers.get(player.getUniqueId());
		}
		else {
			SnowballPlayer toReturn = new SnowballPlayer(player);
			snowballPlayers.put(player.getUniqueId(), toReturn);
			return toReturn;
		}
	}
	
	@EventHandler(ignoreCancelled=true)
	public void playerTeleport(PlayerTeleportEvent e) {
		//refreshChunksInRadius(e.getPlayer());
	}
	
	public void refreshChunk(Player player) { //Sometimes things dont work correctly idk
		Chunk chunk = player.getChunk();
		refreshChunk(player, chunk);
	}
	
	public void refreshChunksInRadius(Player player) {
		Location location = player.getLocation();
		int radius = 16;
		for(int zPos = (int) location.getZ() + radius; zPos > location.getZ() - radius; zPos -= 16) {
			   for(int xPos = (int) location.getX() + radius; xPos > location.getX() - radius; xPos -= 16) { //start at the topmost corner (x & z). Run once for every 16 blocks - once a chunk.
				   refreshChunk(player, player.getWorld().getChunkAt(new Location(player.getWorld(), xPos, 0, zPos)));
			   }
			}
	}
	
	public void refreshChunk(Player player, Chunk chunk) {
		player.sendMessage("refresh chunk: " + chunk.getX() + " " + chunk.getZ());
		
		net.minecraft.server.v1_12_R1.Chunk mcChunk = ((CraftChunk)chunk).getHandle();
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutMapChunk(mcChunk, 20));
		player.getWorld().refreshChunk(chunk.getX(), chunk.getZ());
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		new BukkitRunnable() {

			@Override
			public void run() {
				customPayload(e.getPlayer(), CustomPayloadConstants.RESPAWN);
				e.getPlayer().sendMessage("Respawned");
			}

		}.runTaskLater(this, 10);

	}

	private void customPayload(Player player, String cpd) {
		PacketDataSerializer data = new PacketDataSerializer(Unpooled.buffer());
		data.a(cpd);
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutCustomPayload(CustomPayloadConstants.CHANNEL_NAME, data));
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] rawdata) {
		//Bukkit.getLogger().info("I recieved: " + Arrays.toString(rawdata) + " (" + new String(rawdata) + ")");
		byte[] newdata = Arrays.copyOfRange(rawdata, 1, rawdata.length); //first byte of the string is a check byte of the length of the string passed
		//Bukkit.getLogger().info("I recieved2: " + Arrays.toString(newdata) + " (" + new String(newdata) + ")");
		String data = new String(newdata);


		if(data.equalsIgnoreCase(CustomPayloadConstants.AUTH)) {
			//Bukkit.broadcastMessage(player.getName() + " is using Snowball!");

			PacketManager.sendPacket(player, PacketManager.S_PACKET_INFO, new SPacketInfo(toBeSentToClients.size()));

			for(CustomObject cb : toBeSentToClients) {
				cb.registerClient(player);
			}

			PacketManager.sendPacket(player, PacketManager.S_PACKET_REFRESH_RESOURCES, new SPacketRefreshResources());

			playersJoined.add(player.getUniqueId());
		}
	}

}
