package org.golde.snowball.api.object;

import javax.annotation.Nonnull;

import org.bukkit.craftbukkit.v1_12_R1.enchantments.CraftEnchantment;
import org.bukkit.entity.Player;
import org.golde.snowball.api.enums.SnowballEnchantmentEnum;
import org.golde.snowball.plugin.PacketManager;
import org.golde.snowball.plugin.custom.CustomObject;
import org.golde.snowball.plugin.custom.CustomObjectName;
import org.golde.snowball.plugin.packets.server.SPacketAddEnchantment;
import org.golde.snowball.shared.Constants;

import net.minecraft.server.v1_12_R1.Enchantment;
import net.minecraft.server.v1_12_R1.Enchantment.Rarity;
import net.minecraft.server.v1_12_R1.EnchantmentSlotType;
import net.minecraft.server.v1_12_R1.EnumItemSlot;
import net.minecraft.server.v1_12_R1.MinecraftKey;

public class CustomEnchantment extends CustomObjectName {

	private static int currentId = 72;
	private final int id;
	private final SnowballEnchantmentEnum.SnowballEnchantmentRarity rarity;
	private final SnowballEnchantmentEnum.SnowballEnchantmentType type;
	private final SnowballEnchantmentEnum.SnowballEnchantmentSlot[] slot;
	
	private int property_minEnchantmentLevel = 1;
	private int property_maxEnchantmentLevel = 1;
	private boolean property_cursedEnchantment = false;
	private boolean property_treasureEnchantment = false;
	
	
	public CustomEnchantment(String name, SnowballEnchantmentEnum.SnowballEnchantmentRarity rarity, SnowballEnchantmentEnum.SnowballEnchantmentType item, SnowballEnchantmentEnum.SnowballEnchantmentSlot... slot){
		super(name);
		this.id = getNextAvaiableId();
		this.rarity = rarity;
		this.type = item;
		
		if(slot == null || slot.length == 0) {
			this.slot = SnowballEnchantmentEnum.SnowballEnchantmentSlot.ALL;
		} 
		else {
			this.slot = slot;
		}
		
		
	}
	
	public void setMinEnchantmentLevel(int min) {
		this.property_minEnchantmentLevel = min;
	}
	
	public void setMaxEnchantmentLevel(int max) {
		this.property_maxEnchantmentLevel = max;
	}
	
	public void setCursedEnchantment() {
		this.property_cursedEnchantment = true;
	}
	
	public void setTreasureEnchantment() {
		this.property_treasureEnchantment = true;
	}
	
	@Override
	public void registerServer() {
		EnumItemSlot[] aenumitemslot = new EnumItemSlot[slot.length];
		for(int i = 0; i < slot.length; i++) {
			aenumitemslot[i] = slot[i].getServer();
		}
		Enchantment ench = new DummyEnchantment(rarity.getServer(), type.getServer(), aenumitemslot);
		net.minecraft.server.v1_12_R1.Enchantment.enchantments.a(id, new MinecraftKey(Constants.MINECRAFT_KEY, unlocalizedName), ench);
		org.bukkit.enchantments.Enchantment.registerEnchantment(new CraftEnchantment((net.minecraft.server.v1_12_R1.Enchantment)ench));
	}

	@Override
	public void registerClient(Player player) {
		PacketManager.sendPacket(player, PacketManager.S_PACKET_ADD_ENCHANTMENT, new SPacketAddEnchantment(id, name, rarity, type, slot, property_minEnchantmentLevel, property_maxEnchantmentLevel, property_cursedEnchantment, property_treasureEnchantment));
	}

	@Override
	public int getId() {
		return id;
	}
	
	private int getNextAvaiableId() {
		currentId++;
		return currentId;
	}
	
	private class DummyEnchantment extends net.minecraft.server.v1_12_R1.Enchantment {

		protected DummyEnchantment(Rarity enchantment_rarity, EnchantmentSlotType enchantmentslottype, EnumItemSlot[] aenumitemslot) {
			super(enchantment_rarity, enchantmentslottype, aenumitemslot);
			this.c("snowball_" + id);
		}
		
		@Override
		public int getMaxLevel() {
			return property_maxEnchantmentLevel;
		}
		
		@Override
		public int getStartLevel() {
			return property_minEnchantmentLevel;
		}
		
		@Override
		public boolean isCursed() {
			return property_cursedEnchantment;
		}
		
		@Override
		public boolean isTreasure() {
			return property_treasureEnchantment;
		}
		
	}

}
