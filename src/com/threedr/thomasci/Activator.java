package com.threedr.thomasci;

import java.util.ArrayList;

public class Activator {
	private ArrayList<EntityActivator> objects;
	private String name;
	private boolean active, once;
	
	public Activator(String name, boolean once) {
		this.name = name;
		this.once = once;
		objects = new ArrayList<EntityActivator>();
	}
	
	public void addObject(EntityActivator e) {
		objects.add(e);
	}
	
	public boolean isActive() {
		if (!once) {
			active = false;
		} else {
			if (active) return true;
		}
		if (objects.size() <= 0) return false;
		for (int i = 0; i < objects.size(); i++) {
			if (!objects.get(i).isActive()) {
				active = false;
				return false;
			}
		}
		active = true;
		return true;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isOnce() {
		return once;
	}
}
