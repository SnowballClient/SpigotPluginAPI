package org.golde.snowball.api.models;

import org.golde.snowball.plugin.models.Model;

public class ItemModel extends Model {

	protected static final ItemModel DEFAULT_BLOCK = new ItemModel("{\"parent\":\"snowball:block/snowball_%ID%\"}");
	public static final ItemModel DEFAULT = new ItemModel("{\"parent\":\"item/generated\",\"textures\":{\"layer0\":\"snowball:items/%ID%\"}}");
	
	public ItemModel(String model) {
		super(model);
	}

}
