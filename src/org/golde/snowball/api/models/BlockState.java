package org.golde.snowball.api.models;

import org.golde.snowball.plugin.models.Model;

public class BlockState extends Model {

	public static final BlockState DEFAULT = new BlockState("{\"variants\":{\"normal\":{\"model\":\"snowball:snowball_%ID%\"}}}");
	
	public BlockState(String model) {
		super(model);
	}

}
