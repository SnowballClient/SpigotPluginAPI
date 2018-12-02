package org.golde.snowball.plugin.custom;

import java.util.Arrays;
import java.util.Map;

import javax.swing.text.Utilities;

import org.golde.snowball.plugin.models.Model;
import org.golde.snowball.plugin.util.EnumHelper;
import org.golde.snowball.plugin.util.ReflectionHelper;
import org.golde.snowball.shared.Constants;
import org.golde.snowball.shared.util.StringHelper;

public abstract class CustomBlockItemShared extends CustomObjectName {

	protected final String texture;
	protected final Model model;
	
	private static int currentId = 3000;
	protected final int id;
	protected String registryName = Constants.MINECRAFT_KEY;
	
	public CustomBlockItemShared(String name, String texture, Model model) {
		super(name);
//		if(this.name.length() > 20) {
//			throw new IllegalArgumentException("name can not be more then 20 characters!");
//		}
		this.texture = texture;
		this.model = model;
//		if(this.texture.length() > 20) {
//			throw new IllegalArgumentException("texture can not be more then 20 characters!");
//		}
		id = getNextAvaiableId();
	}
	
	private int getNextAvaiableId() {
		currentId++;
		return currentId;
	}
	
	protected final void refreshServerEnum() {
		
		String materialName = name.toUpperCase();
		org.bukkit.Material material = EnumHelper.addMaterial(materialName, id);
		
		org.bukkit.Material[] byId = ReflectionHelper.getPrivateValue(org.bukkit.Material.class, null, "byId");
		Map<String, org.bukkit.Material> BY_NAME = ReflectionHelper.getPrivateValue(org.bukkit.Material.class,  null,  "BY_NAME");		
        if (byId.length > material.getId()) {
            byId[material.getId()] = material;
        } else {
            byId = Arrays.copyOfRange(byId, 0, material.getId() + 2);
            byId[material.getId()] = material;
        }
        BY_NAME.put(material.name(), material);
    
		//Update materials enum with our new block data
		ReflectionHelper.setPrivateValue(org.bukkit.Material.class, null, byId, "byId");
		ReflectionHelper.setPrivateValue(org.bukkit.Material.class, null, BY_NAME, "BY_NAME");
	}
	
	public final int getId() {return id;};
	
	
//	@Deprecated
//	public void do_not_use_setRegistryName(String regName) {
//		this.registryName = regName;
//	}
	
}
