package com.threedr.thomasci;

import org.lwjgl.util.vector.Vector3f;

public class ItemArmour extends ItemWearable {
	private Material mat;
	
	public ItemArmour(int id, int tl, int ml) {
		super(id, tl, ml);
		int type = (int) Math.floor(Math.random() * 2);
		switch (type) {
			case 0:
				mat = Material.bronze;
				break;
			case 1:
				mat = Material.iron;
				break;
			case 2:
				mat = Material.steel;
				break;
			case 3:
				mat = Material.mithril;
				break;
			case 4:
				mat = Material.adamantine;
				break;
			case 5:
				mat = Material.dark;
				break;
			case 6:
				mat = Material.light;
				break;
			default:
				mat = Material.bronze;
				break;
		}
		Vector3f col = mat.getCol();
		setColour(col.x, col.y, col.z);
	}
	
	public void setMaterial(Material m) {
		mat = m;
	}
	
	public String getName() {
		if (mat != null) {
			return mat.getName() + " " + name;
		}
		return name;
	}
	
	public int getResistance() {
		return mat.getResistance();
	}
	
	public String write() {
		return id + ";" + idName + ";" + mat.write() + ";" + count + ";" + enchantments.write();
	}
}
