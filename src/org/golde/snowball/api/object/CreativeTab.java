package org.golde.snowball.api.object;

import org.bukkit.inventory.ItemStack;
import org.golde.snowball.shared.util.StringHelper;

public class CreativeTab {

	private final String displayName;
	private final String unlocalizedname;
	
	public CreativeTab(String displayName) {
		this.displayName = displayName;
		this.unlocalizedname = StringHelper.sanatise(displayName);
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getUnlocalizedname() {
		return unlocalizedname;
	}
	
	
}
