package org.golde.snowball.api.enums;

import net.minecraft.server.v1_12_R1.Enchantment.Rarity;
import net.minecraft.server.v1_12_R1.EnchantmentSlotType;
import net.minecraft.server.v1_12_R1.EnumItemSlot;

public class SnowballEnchantmentEnum {

	public static enum SnowballEnchantmentRarity {
		COMMON("COMMON", "a", "COMMON"), 
		UNCOMMON("UNCOMMON", "b", "UNCOMMON"), 
		RARE("RARE", "c", "RARE"), 
		VERY_RARE("VERY_RARE", "d", "VERY_RARE");
		
		private final String clientDeob;
		private final String client; 
		private final String server;
		private SnowballEnchantmentRarity(String clientDeob, String client, String server) {
			this.clientDeob = clientDeob;
			this.client = client;
			this.server = server;
		}
		
		public Rarity getServer() {
			return Rarity.valueOf(server);
		}
	}
	
	public static enum SnowballEnchantmentType {
		ALL("ALL", "a", "ALL"),
		ARMOR("ARMOR", "b", "ARMOR"),
		ARMOR_FEET("ARMOR_FEET", "c", "ARMOR_FEET"),
		ARMOR_LEGS("ARMOR_LEGS", "d", "ARMOR_LEGS"),
		ARMOR_CHEST("ARMOR_CHEST", "e", "ARMOR_CHEST"),
		ARMOR_HEAD("ARMOR_HEAD", "f", "ARMOR_HEAD"),
		WEAPON("WEAPON", "g", "WEAPON"),
		DIGGER("DIGGER", "h", "DIGGER"),
		FISHING_ROD("FISHING_ROD", "i", "FISHING_ROD"),
		BREAKABLE("BREAKABLE", "j", "BREAKABLE"),
		BOW("BOW", "k", "BOW"),
		WEARABLE("WEARABLE", "l", "WEARABLE");
		
		private final String clientDeob;
		private final String client; 
		private final String server;
		private SnowballEnchantmentType(String clientDeob, String client, String server) {
			this.clientDeob = clientDeob;
			this.client = client;
			this.server = server;
		}
		
		public EnchantmentSlotType getServer() {
			return EnchantmentSlotType.valueOf(server);
		}
	}
	
	public static enum SnowballEnchantmentSlot {
		MAINHAND("MAINHAND", "a", "MAINHAND"),
		OFFHAND("OFFHAND", "b", "OFFHAND"),
		FEET("FEET", "c", "FEET"),
		LEGS("LEGS", "d", "LEGS"),
		CHEST("CHEST", "e", "CHEST"),
		HEAD("HEAD", "f", "HEAD");
		
		private final String clientDeob;
		private final String client; 
		private final String server;
		private SnowballEnchantmentSlot(String clientDeob, String client, String server) {
			this.clientDeob = clientDeob;
			this.client = client;
			this.server = server;
		}
		
		public EnumItemSlot getServer() {
			return EnumItemSlot.valueOf(server);
		}
		
		public static final SnowballEnchantmentSlot[] ALL = {MAINHAND, OFFHAND, FEET, LEGS, CHEST, HEAD};
		public static final SnowballEnchantmentSlot[] ARMOR = {FEET, LEGS, CHEST, HEAD};
		public static final SnowballEnchantmentSlot[] HANDS = {MAINHAND, OFFHAND};
	}

}
