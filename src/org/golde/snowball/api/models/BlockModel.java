package org.golde.snowball.api.models;

import org.golde.snowball.plugin.models.Model;

public class BlockModel extends Model {

	public static final BlockModel DEFAULT = new BlockModel("{\"parent\":\"block/cube_all\",\"textures\":{\"all\":\"snowball:blocks/%ID%\"}}");
	
	private final BlockState blockState;
	
	public BlockModel(String model) {
		this(model, BlockState.DEFAULT);
	}
	
	public BlockModel(String model, BlockState blockState) {
		super(model);
		this.blockState = blockState;
	}
	
	public ItemModel getItemModel() {
		return ItemModel.DEFAULT_BLOCK;
	}
	
	public BlockState getBlockState() {
		return blockState;
	}

}
