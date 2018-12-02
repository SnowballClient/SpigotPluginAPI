package org.golde.snowball.api.object;

import java.util.Random;

import org.golde.snowball.api.enums.SnowballMaterial;
import org.golde.snowball.api.enums.SnowballStepSound;
import org.golde.snowball.api.models.BlockModel;
import org.golde.snowball.plugin.MainPlugin;
import org.golde.snowball.plugin.PacketManager;
import org.golde.snowball.plugin.custom.CustomBlockItemShared;
import org.golde.snowball.plugin.packets.server.SPacketAddBlock;
import org.golde.snowball.shared.Constants;
import org.golde.snowball.shared.nbt.NBTConstants;

import com.google.common.collect.UnmodifiableIterator;

import net.minecraft.server.v1_12_R1.Block;
import net.minecraft.server.v1_12_R1.IBlockData;
import net.minecraft.server.v1_12_R1.IBlockState;
import net.minecraft.server.v1_12_R1.MinecraftKey;

public class CustomBlock extends CustomBlockItemShared {

	private float properties_light = NBTConstants.INT_NULL;
	private float properties_resistance = 10.0F;
	private float properties_hardness = 1.5F;
	private boolean properties_transparent = false;
	private SnowballMaterial properties_material = SnowballMaterial.STONE;
	private boolean properties_silktouch = false;
	private SnowballStepSound properties_stepsound = SnowballStepSound.STONE;
	
	public CustomBlock(String name, BlockModel model, String texture) {
		super(name, texture, model);	
	}

	@Override
	public String toString() {
		return "CustomBlock [name=" + name + ", texture=" + texture + ", id=" + id + "]";
	}
	
	public void setLightValue(float lightValue) {
		this.properties_light = lightValue;
	}
	
	public void setHardnessValue(float hardness) {
		this.properties_hardness = hardness;
	}
	
	public void setResistance(float resistance) {
		this.properties_resistance = resistance;
	}
	
	public void setTransparent(boolean transparent) {
		this.properties_transparent = transparent;
	}
	
	public void setMaterial(SnowballMaterial material) {
		properties_material = material;
	}
	
	public void setSilktouch(boolean silktouch) {
		this.properties_silktouch = silktouch;
	}
	
	public void setStepSound(SnowballStepSound stepsound) {
		this.properties_stepsound = stepsound;
	}

	@Override
	public void registerServer() {
		net.minecraft.server.v1_12_R1.Block block = new BlockDummy(name, texture);
		net.minecraft.server.v1_12_R1.Block.REGISTRY.a(id, new MinecraftKey(registryName, "snowball_" + id), block);
		net.minecraft.server.v1_12_R1.Item item = new net.minecraft.server.v1_12_R1.ItemBlock(block);
		net.minecraft.server.v1_12_R1.Item.REGISTRY.a(id, new MinecraftKey(registryName, "snowball_" + id), item);
		
		refreshServerEnum();
		
		
		for (final IBlockData iblockdata : block.s().a()) {
            final int k = Block.REGISTRY.a(block) << 4 | block.toLegacyData(iblockdata);
            Block.REGISTRY_ID.a(iblockdata, k);
        }

	}
	
	@Override
	public void registerClient(org.bukkit.entity.Player player) {
		PacketManager.sendPacket(player, PacketManager.S_PACKET_ADD_BLOCK, new SPacketAddBlock(registryName, id, name, texture, (BlockModel) model, properties_light, properties_resistance, properties_hardness, properties_transparent, properties_material, properties_silktouch, properties_stepsound));
	}
	
	private class BlockDummy extends net.minecraft.server.v1_12_R1.Block {

		protected BlockDummy(String theName, String texture) {
			super(properties_material.getServer());
			this.a(net.minecraft.server.v1_12_R1.CreativeModeTab.b); //This does not half to match the client.. Not sure if its even used on the server tbh
			c("snowball_" + theName); //name
			//d("snowball_" + texture); //texture
			c(properties_hardness); //hardness
			b(properties_resistance); //resistance
			a(properties_stepsound.getServer()); //stepsound
		}
		
		@Override
		public int a(Random random) { //quantityDropped
			return properties_silktouch ? 0 : 1;
		}
		
		@Override
	    public boolean c(final IBlockData blockData) { //renderAsNormalBlock
	        return !properties_transparent;
	    }
		
		@Override
	    public boolean b(final IBlockData blockData) { //isOpaqueCube
	        return !properties_transparent;
	    }
	    
	    @Override
	    protected boolean n() { //canSilkHarvest
	        return properties_silktouch;
	    }
		
	}

	
}
