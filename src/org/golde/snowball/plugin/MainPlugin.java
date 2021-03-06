package org.golde.snowball.plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.golde.snowball.api.SnowballAPI;
import org.golde.snowball.api.event.SnowballPlayerItemDrinkEvent;
import org.golde.snowball.api.event.SnowballPlayerItemEatEvent;
import org.golde.snowball.api.object.CustomCreativeTab;
import org.golde.snowball.api.object.CustomItemDrink.DummyItemDrinkable;
import org.golde.snowball.api.object.CustomItemFood.DummyItemFood;
import org.golde.snowball.api.object.SnowballPlayer;
import org.golde.snowball.plugin.custom.CustomObject;
import org.golde.snowball.plugin.packets.server.SPacketInfo;
import org.golde.snowball.plugin.packets.server.SPacketRefreshResources;
import org.golde.snowball.plugin.util.ReflectionHelper;
import org.golde.snowball.shared.CustomPayloadConstants;

import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutCustomPayload;

public class MainPlugin extends JavaPlugin implements Listener, PluginMessageListener {

	private static MainPlugin instance;
	private List<CustomObject> toBeSentToClients = new ArrayList<CustomObject>();


	private HashMap<UUID, SnowballPlayer> snowballPlayers = new HashMap<UUID, SnowballPlayer>();

	private static final String WORLD_NAME = "snowball";

	private boolean DEBUG = false;

	@Override
	public void onEnable() {
		instance = this;

		saveDefaultConfig();
		DEBUG = getConfig().getBoolean("debug");

		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, CustomPayloadConstants.CHANNEL_NAME);
		Bukkit.getMessenger().registerIncomingPluginChannel(this, CustomPayloadConstants.CHANNEL_NAME, this);
		Bukkit.getMessenger().registerIncomingPluginChannel(this, "MC|Brand", this);

		PacketManager.registerPackets();

		patchMaterialEnumSoItWontFailInTheFuture();

		if(MainPlugin.getInstance().isDEBUG()) {getLogger().info("Registering blocks and items...");}

		registerBlocksAndItems();

		new BukkitRunnable() {
			public void run() {
				doWorldGeneration();

			}
		}.runTaskLater(this, 2);
	}

	public static String getWorldName() {
		return WORLD_NAME;
	}

	private void doWorldGeneration() {
		if(MainPlugin.getInstance().isDEBUG()) {getLogger().info("Generating snowball fix world...");}
		WorldCreator wc = new WorldCreator(WORLD_NAME);
		wc.generateStructures(false);
		wc.type(WorldType.FLAT);
		wc.seed(0);
		wc.environment(Environment.NORMAL);
		wc.generatorSettings("3;minecraft:barrier;1"); //so they dont fall into the void (Damange in this world is not here)
		Bukkit.createWorld(wc);
	}

	private void registerBlocksAndItems() {

		try {
			ReflectionHelper.setFinalStatic(Enchantment.class.getDeclaredField("acceptingNew"), true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//Bukkit.getPluginManager().callEvent(new SnowballRegistryEvent());

		Collections.sort(SnowballAPI.do_not_use_me_getObjects(), customObjectComparator);
		for(CustomObject obj : SnowballAPI.do_not_use_me_getObjects()) {
			obj.registerServer();
			toBeSentToClients.add(obj);
			if(DEBUG) {Bukkit.getLogger().info("Registering Custom Object: " + obj.getClass().getSimpleName());}
		}

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

	@EventHandler
	public void changeWorldEvent(final PlayerChangedWorldEvent e) {

		new BukkitRunnable() {

			@Override
			public void run() {
				customPayload(e.getPlayer(), CustomPayloadConstants.RESPAWN);
			}
		}.runTaskLater(this, 5);

	}


	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent e) {

		final Player player = e.getPlayer();

		new BukkitRunnable() {
			@Override
			public void run() {

				getOrCreateSnowballPlayer(player).refreshClientWorld();

			}
		}.runTaskLater(this, 10);

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

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		new BukkitRunnable() {

			@Override
			public void run() {
				customPayload(e.getPlayer(), CustomPayloadConstants.RESPAWN);
			}

		}.runTaskLater(this, 10);

	}

	private void customPayload(Player player, String cpd) {
		if(DEBUG) {player.sendMessage("CustomPayload: " + cpd);}
		PacketDataSerializer data = new PacketDataSerializer(Unpooled.buffer());
		data.a(cpd);
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutCustomPayload(CustomPayloadConstants.CHANNEL_NAME, data));
	}

	volatile boolean shouldKick = false;
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] rawdata) {

		byte[] newdata = Arrays.copyOfRange(rawdata, 1, rawdata.length); //first byte of the string is a check byte of the length of the string passed
		String data = new String(newdata);
		
		if(DEBUG) {getLogger().info("Channel: " + channel + " msg: " + data);}

	}

	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent e) {

		String[] ipSplit = e.getHostname().split(":");
		String hostName = ipSplit[0];
		int port = Integer.parseInt(ipSplit[1]);
		
		//System.out.println("Arr: " + Dumb.IS_SNOWBALL_CLIENT.toString());
		if(port != 0) {
			e.disallow(Result.KICK_OTHER, "You need the snowball client in order to join this server");
		}
		else {
			//Dumb.IS_SNOWBALL_CLIENT.remove(e.getPlayer().getName());
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				final Player player = e.getPlayer();

				PacketManager.sendPacket(player, PacketManager.S_PACKET_INFO, new SPacketInfo(toBeSentToClients.size()));
				for(CustomObject cb : toBeSentToClients) {
					cb.registerClient(player);
				}

				PacketManager.sendPacket(player, PacketManager.S_PACKET_REFRESH_RESOURCES, new SPacketRefreshResources());
			}
		}.runTaskLater(this, 0); //Yeah, run task later 0 ticks works... 1 and no-delay fail. Its strange.

	}
	
	@EventHandler(ignoreCancelled = true, priority= EventPriority.LOWEST)
	public void onItemConsume(PlayerItemConsumeEvent e) {
		Player player = e.getPlayer();
		SnowballPlayer snowballPlayer = getOrCreateSnowballPlayer(player);
		ItemStack is = e.getItem();
		net.minecraft.server.v1_12_R1.ItemStack nmsIs = CraftItemStack.asNMSCopy(is);
		if(is.getType() == Material.POTION || nmsIs.getItem() instanceof DummyItemDrinkable) {
			SnowballPlayerItemDrinkEvent event = new SnowballPlayerItemDrinkEvent(snowballPlayer, is);
			event.call();
			if(event.isCancelled()) {
				e.setCancelled(true);
			}
			
		}
		
		if(is.getType().isEdible() || nmsIs.getItem() instanceof DummyItemFood) {
			SnowballPlayerItemEatEvent event = new SnowballPlayerItemEatEvent(snowballPlayer, is);
			event.call();
			if(event.isCancelled()) {
				e.setCancelled(true);
			}
			
		}
		
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void playerCommandEvent(PlayerCommandPreprocessEvent e) {
		e.setCancelled(prossessCommand(e.getPlayer(), e.getMessage()));
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void serverCommandEvent(ServerCommandEvent e) {
		e.setCancelled(prossessCommand(e.getSender(), e.getCommand()));
	}
	
	//Somebody who doesn't know what /reload does would try it. Same with plugman. We should stop that! It breaks everything!
	public boolean prossessCommand(CommandSender sender, String cmd) {
		if(cmd.startsWith("/")) {
			cmd = cmd.substring(1);
		}
		if(cmd.equalsIgnoreCase("reload") || 
				cmd.equalsIgnoreCase("rl") || 
				cmd.equalsIgnoreCase("plugman enable SnowballAPI") ||
				cmd.equalsIgnoreCase("plugman disable SnowballAPI") ||
				cmd.equalsIgnoreCase("plugman load SnowballAPI") ||
				cmd.equalsIgnoreCase("plugman unload SnowballAPI") ||
				cmd.equalsIgnoreCase("plugman restart SnowballAPI") ||
				cmd.equalsIgnoreCase("plugman reload SnowballAPI") ||
				
				cmd.equalsIgnoreCase("plugman enable SnowballYML") ||
				cmd.equalsIgnoreCase("plugman disable SnowballYML") ||
				cmd.equalsIgnoreCase("plugman load SnowballYML") ||
				cmd.equalsIgnoreCase("plugman unload SnowballYML") ||
				cmd.equalsIgnoreCase("plugman restart SnowballYML") ||
				cmd.equalsIgnoreCase("plugman reload SnowballYML")
				
				) {
			sender.sendMessage(ChatColor.RED + "Snowball does not support the '/" + cmd + "' command. Please do '/stop' or '/restart'.");
			return true;
		}
		return false;
	}

	private static Comparator<CustomObject> customObjectComparator = new Comparator<CustomObject>() {

		public int compare(CustomObject s1, CustomObject s2) {
			String cn1 = s1.getClass().getSimpleName();
			String cn2 = s2.getClass().getSimpleName();
			
			if(cn1.equals(cn2)) {
				return 0;
			}
			else if(cn1.equals(CustomCreativeTab.class.getSimpleName())) {
				return -1;
			}
			else if(cn2.equals(CustomCreativeTab.class.getSimpleName())){
				return 1;
			}
			else {
				return 0;
			}

		}};

		public boolean isDEBUG() {
			return DEBUG;
		}

}
