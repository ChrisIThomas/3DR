package com.threedr.thomasci;

import org.lwjgl.util.vector.Vector3f;

public class EntityActivator extends Entity {
	protected boolean active;
	protected String activator;
	
	public EntityActivator(float x, float z, Game g, String activ) {
		super(x, z, g);
		activator = activ;
	}
	
	public void forces() {
		if (-pos.y <= game.getHeight((int) -pos.x / 2, (int) ((-pos.z) / 2))) {
			pos.y = -game.getHeight((int) -pos.x / 2, (int) ((-pos.z) / 2));
			if (game.getTile((int) -pos.x / 2, (int) ((-pos.z) / 2)) == 1) {
				pos.y = (int) pos.y;
			}
		}
	}
	
	public String getActivator() {
		return activator;
	}
	
	public void activate() {
		active = !active;
	}
	
	public boolean isActive() {
		return active;
	}
	
	//change the collide event so it doesn't collide with anything
	public Vector3f collides(Entity e, float er) {return null;}
}
