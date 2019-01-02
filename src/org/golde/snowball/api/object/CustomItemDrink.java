package org.golde.snowball.api.object;

import org.bukkit.entity.Player;
import org.golde.snowball.api.models.ItemModel;
import org.golde.snowball.plugin.PacketManager;
import org.golde.snowball.plugin.packets.server.SPacketAddItem;

import net.minecraft.server.v1_12_R1.CriterionTriggers;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EnumAnimation;
import net.minecraft.server.v1_12_R1.EnumHand;
import net.minecraft.server.v1_12_R1.EnumInteractionResult;
import net.minecraft.server.v1_12_R1.InteractionResultWrapper;
import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.StatisticList;
import net.minecraft.server.v1_12_R1.World;

public class CustomItemDrink extends CustomItemFoodDrinkExtend {

	public CustomItemDrink(String name, ItemModel model, String texture) {
		super(name, model, texture);
	}

	@Override
	public void registerClient(Player player) {
		PacketManager.sendPacket(player, PacketManager.S_PACKET_ADD_ITEM, new SPacketAddItem(id, name, texture, (ItemModel)model, properties_creativeTab, properties_maxStackSize, properties_glowing, properties_food_drink_maxItemUseDuration));
	}
	
	@Override
	public Item getItemToBeRegistered() {
		return new DummyItemDrinkable();
	}
	
	private class DummyItemDrinkable extends ItemDummy {
		
		@Override
	    public ItemStack a(final ItemStack itemStack, final World world, final EntityLiving entityliving) {
	        final EntityHuman entityHuman = (entityliving instanceof EntityHuman) ? ((EntityHuman)entityliving) : null;
	        if (entityHuman == null || !entityHuman.abilities.canInstantlyBuild) {
	            itemStack.subtract(1);
	        }
	        if (entityHuman instanceof EntityPlayer) {
	            CriterionTriggers.y.a((EntityPlayer)entityHuman, itemStack);
	        }
//	        if (!world.isClientSide) {
//	            for (final MobEffect mobEffect : PotionUtil.getEffects(itemStack)) {
//	                if (mobEffect.getMobEffect().isInstant()) {
//	                    mobEffect.getMobEffect().applyInstantEffect(entityHuman, entityHuman, entityliving, mobEffect.getAmplifier(), 1.0);
//	                }
//	                else {
//	                    entityliving.addEffect(new MobEffect(mobEffect));
//	                }
//	            }
//	        }
	        if (entityHuman != null) {
	            entityHuman.b(StatisticList.b(this));
	        }
//	        if (entityHuman == null || !entityHuman.abilities.canInstantlyBuild) {
//	            if (itemStack.isEmpty()) {
//	                return new ItemStack(Items.GLASS_BOTTLE);
//	            }
//	            if (entityHuman != null) {
//	                entityHuman.inventory.pickup(new ItemStack(Items.GLASS_BOTTLE));
//	            }
//	        }
	        return itemStack;
	    }
		
		@Override
	    public int e(final ItemStack itemStack) {
	        return properties_food_drink_maxItemUseDuration;
	    }
		
		@Override
	    public EnumAnimation f(final ItemStack itemStack) {
	        return EnumAnimation.DRINK;
	    }
		
		@Override
	    public InteractionResultWrapper<ItemStack> a(final World world, final EntityHuman entityHuman, final EnumHand enumHand) {
	        entityHuman.c(enumHand);
	        return new InteractionResultWrapper<ItemStack>(EnumInteractionResult.SUCCESS, entityHuman.b(enumHand));
	    }
		
	}
	
}
