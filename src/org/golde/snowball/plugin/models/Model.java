package org.golde.snowball.plugin.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.golde.snowball.plugin.util.Minify;

public class Model {
	
	private String model;

	protected Model(String model) {
		this.model = model;
	}
	
	public String getModel(int id) {
		return model.toLowerCase().replace("%id%", String.valueOf(id));
	}
	
//	protected Model(File model, byte type) throws Exception {
//		this(Minify.minify(readFile(model)), type);
//	}
//
//	private static String readFile(File file) throws Exception {
//		String toReturn = "";
//		BufferedReader br = new BufferedReader(new FileReader(file));
//		String line;
//		while ((line = br.readLine()) != null) {
//			toReturn += line;
//		}
//		br.close();
//		return toReturn;
//
//	}

}
