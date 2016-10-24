package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Material {
	private String name;
	private Vector3f col, col2;
	private int speed, damage, resistance, type;
	
	public Material(int type) {
		this.type = type;
		name = "";
		resistance = 1;
		col = new Vector3f(1.0f, 1.0f, 1.0f);
		col2 = new Vector3f(0.392f, 0.196f, 0.0f);
	}
	
	private Material setName(String n) {
		name = n;
		return this;
	}
	
	private Material setWeaponCol(float r, float g, float b) {
		col = new Vector3f(r, g, b);
		return this;
	}
	
	private Material setHandCol(float r, float g, float b) {
		col2 = new Vector3f(r, g, b);
		return this;
	}
	
	private Material setDamage(int amount) {
		damage = amount;
		return this;
	}
	
	private Material setSpeed(int amount) {
		speed = amount;
		return this;
	}
	
	private Material setResistance(int amount) {
		resistance = amount;
		return this;
	}
	
	public String getName() {
		return name;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public int getResistance() {
		return resistance;
	}
	
	public Vector3f getCol() {
		return col;
	}
	
	public void colour() {
		GL11.glColor3f(col.x, col.y, col.z);
	}
	
	public void colourHand() {
		GL11.glColor3f(col2.x, col2.y, col2.z);
	}
	
	public static Material getMaterial(int type) {
		switch (type) {
			case 0:
				return bronze;
			case 1:
				return iron;
			case 2:
				return steel;
			case 3:
				return mithril;
			case 4:
				return adamantine;
			case 5:
				return dark;
			case 6:
				return light;
			default:
				return bronze;
		}
	}
	
	public String write() {
		return "" + type;
	}
	
	public static final Material bronze, iron, steel, mithril, adamantine, dark, light;
	
	static {
		bronze = new Material(0).setName("Bronze").setWeaponCol(0.686f, 0.471f, 0.275f).setDamage(-1);
		iron = new Material(1).setName("Iron").setWeaponCol(0.65f, 0.65f, 0.65f).setResistance(2);
		steel = new Material(2).setName("Steel").setWeaponCol(1.0f, 1.0f, 1.0f).setDamage(1).setResistance(3);
		mithril = new Material(3).setName("Mithril").setWeaponCol(0.15f, 0.15f, 0.4f).setHandCol(0.75f, 0.75f, 0.75f).setDamage(1).setSpeed(1).setResistance(4);
		adamantine = new Material(4).setName("Adamantine").setWeaponCol(0.5f, 0.5f, 0.9f).setHandCol(0.75f, 0.75f, 0.75f).setDamage(2).setSpeed(2).setResistance(5);
		//light and dark materials are designed for weapons, they act like bronze if they are used for armour
		dark = new Material(5).setName("Dark").setWeaponCol(0.15f, 0.15f, 0.15f).setHandCol(0.25f, 0.25f, 0.25f).setDamage(4).setSpeed(-1);
		light = new Material(6).setName("Light").setWeaponCol(0.9f, 0.9f, 0.9f).setHandCol(0.85f, 0.85f, 0.85f).setSpeed(4).setDamage(-1);
	}
}
