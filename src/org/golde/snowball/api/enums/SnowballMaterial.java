package org.golde.snowball.api.enums;

import net.minecraft.server.v1_12_R1.Material;

public enum SnowballMaterial {

	AIR("AIR", "a", "AIR"),
	GRASS("GRASS", "b", "GRASS"),
	DIRT("GROUND", "c", "EARTH"),
	WOOD("WOOD", "d", "WOOD"),
	STONE("ROCK", "e", "STONE"),
	ORE("IRON", "f", "ORE"),
	ANVIL("ANVIL", "g", "HEAVY"),
	WATER("WATER", "h", "WATER"),
	LAVA("LAVA", "i", "LAVA"),
	LEAVES("LEAVES", "j", "LEAVES"),
	PLANTS("PLANTS", "k", "PLANT"),
	VINES("VINE", "l", "REPLACEABLE_PLANT"),
	SPONGE("SPONGE", "m", "SPONGE"),
	CLOTH("CLOTH", "n", "CLOTH"),
	FIRE("FIRE", "o", "FIRE"),
	SAND("SAND", "p", "SAND"),
	CIRCUITS("CIRCUITS", "q", "ORIENTABLE"),
	CARPET("CARPET", "r", "WOOL"),
	GLASS("GLASS", "s", "SHATTERABLE"),
	REDSTONE_LIGHT("REDSTONE_LIGHT", "t", "BUILDABLE_GLASS"),
	TNT("TNT", "u", "TNT"),
	CORAL("CORAL", "v", "CORAL"),
	ICE("ICE", "w", "ICE"),
	PACKED_ICE("PACKED_ICE", "x", "PACKED_ICE"),
	SNOW("SNOW", "y", "SNOW_LAYER"),
	CRAFTED_SNOW("CRAFTED_SNOW", "z", "SNOW_BLOCK"),
	CACTUS("CACTUS", "A", "CACTUS"),
	CLAY("CLAY", "B", "CLAY"),
	PUMPKIN("GOURD", "C", "PUMPKIN"),
	DRAGON_EGG("DRAGON_EGG", "D", "DRAGON_EGG"),
	PORTAL("PORTAL", "E", "PORTAL"),
	CAKE("CAKE", "F", "CAKE"),
	WEB("WEB", "G", "WEB"),
	PISTON("PISTON", "H", "PISTON"),
	BARRIER("BARRIER", "I", "BANNER"),
	STRUCTURE_VOID("STRUCTURE_VOID", "J", "J"),
	;

	private final String clientDeob;
	private final String client; 
	private final String server;
	private SnowballMaterial(String clientDeob, String client, String server) {
		this.clientDeob = clientDeob;
		this.client = client;
		this.server = server;
	}
	
	public Material getServer() {
		try {
			return (Material)Material.class.getField(server).get(null);
		} 
		catch (IllegalArgumentException |  IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
