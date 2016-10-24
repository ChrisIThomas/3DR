package com.threedr.thomasci;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Controller {
	private Entity entity;
	private int bdir;
	private float bob;
	private int slot;
	
	public Controller(Entity e) {
		entity = e;
		bdir = 1;
		slot = 0;
	}
	
	public Vector3f getDir() {
		if (((EntityLiving) entity).getHealth() > 0) {
			float x = 0, z = 0;
			if (Keyboard.isKeyDown(Keyboard.KEY_A)) x += 0.05f;
			if (Keyboard.isKeyDown(Keyboard.KEY_D)) x -= 0.05f;
			if (Keyboard.isKeyDown(Keyboard.KEY_W)) z += 0.05f;
			if (Keyboard.isKeyDown(Keyboard.KEY_S)) z -= 0.05f;
			return new Vector3f(x, 0.0f, z);
		}
		return new Vector3f(0.0f, 0.0f, 0.0f);
	}
	
	public void update() {
		if (Keyboard.isKeyDown(Keyboard.KEY_1)) slot = 0;
		else if (Keyboard.isKeyDown(Keyboard.KEY_2)) slot = 1;
		else if (Keyboard.isKeyDown(Keyboard.KEY_3)) slot = 2;
		else if (Keyboard.isKeyDown(Keyboard.KEY_4)) slot = 3;
		ALLoader.setListenPos(entity.pos);
	}
	
	public Vector3f getRot() {
		if (((EntityLiving) entity).getHealth() > 0) {
			if (Mouse.isGrabbed())
				return new Vector3f(0.0f, Mouse.getDX() / (float) TDR.sensitivity, -Mouse.getDY() / (float) TDR.sensitivity);
		}
		return new Vector3f(0, 0, 0);
	}
	
	public void look(boolean move) {
		entity.look();
		if (move) {
			Vector3f vel = entity.getVel();
			if (vel.z != 0 || vel.x != 0) {
				float v = Math.abs(vel.z) + Math.abs(vel.x);
				if (v > 0.06f) v = 0.06f;
				if (v < -0.06f) v = -0.06f;
				bob += bdir / Math.abs((100.0f / (v * 10)));
				if (bob >= 0.05f) bdir = -1;
				if (bob <= -0.05f) bdir = 1;
			}
			GL11.glTranslatef(0.0f, bob, 0.0f);
		}
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public int getSlot() {
		return slot;
	}
	
	public float getBob() {
		return bob;
	}
}
