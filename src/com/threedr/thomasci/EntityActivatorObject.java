package com.threedr.thomasci;

public class EntityActivatorObject extends Entity {
	private final String name;
	private final boolean once;
	
	public EntityActivatorObject(float x, float z, Game g, String name, boolean once) {
		super(x, z, g);
		this.name = name;
		this.once = once;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isOnce() {
		return once;
	}
}
