package com.threedr.thomasci;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class EntityData extends Entity {
	String[] data;
	
	public EntityData(String[] data) {
		super(0, 0, null);
		this.data = data;
	}
	
	public Object getData() {
		int type = Integer.parseInt(data[3]);
		String[] info = data[4].split(";");
		switch (type) {
			case 0: //enchantments
				return new Enchantment(Integer.parseInt(info[0]), Integer.parseInt(info[1]), false);
			case 1: //material
				return Material.getMaterial(Integer.parseInt(info[0]));
			case 2: //colour
				float val = 1.0f / 255;
				return new Vector3f(Integer.parseInt(info[0]) * val, Integer.parseInt(info[1]) * val, Integer.parseInt(info[2]) * val);
			case 3: //money
				return Integer.parseInt(info[0]);
			case 4: //count
				return Integer.parseInt(info[0]);
			case 5: //potion/staff/wand
				return new Vector2f(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
		}
		return null;
	}
	
	public int getType() {
		return Integer.parseInt(data[3]);
	}
	
	public String getName() {
		return data[5];
	}
}
