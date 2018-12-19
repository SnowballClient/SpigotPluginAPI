package org.golde.snowball.plugin.custom;

import org.golde.snowball.shared.util.StringHelper;

public abstract class CustomObjectName implements CustomObject{

	protected final String name;
	protected final String unlocalizedName;
	
	public CustomObjectName(String name) {
		this.name = name;
		unlocalizedName = StringHelper.sanatise(name, true, false);
	}
	
}
