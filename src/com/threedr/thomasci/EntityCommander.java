package com.threedr.thomasci;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

public class EntityCommander extends Entity {
	private int type;
	private String[] data;
	
	public EntityCommander(float x, float z, Game g, int type, String data) {
		super(x, z, g);
		this.type = type;
		this.data = data.split(";");
		rad = 1.0f;
	}
	
	public void doCommand(Entity e) {
		System.out.println(data[0] + ", " + data[1]);
		switch (type) {
			case 0: //teleport
				if (data.length > 2) {
					e.setPos(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]));
				} else {
					e.setPos(Integer.parseInt(data[0]), 0, Integer.parseInt(data[1]));
				}
				for (int i = 0; i < 20; i++) {
					float r = (float) Math.random() * 0.5f - 0.25f;
					game.addParticle(new Particle(new Vector3f((float) Math.random() * 2 - 1 + e.getX(), e.getY() - 1, (float) Math.random() * 2 - 1 + e.getZ()),
							new Vector3f((float) Math.random() * 0.02f - 0.01f, (float) Math.random() * 0.05f - 0.025f, (float) Math.random() * 0.02f - 0.01f),
							new Vector3f(0.5f + r, 0.0f, 0.75f + r), false, game));
				}
				e.setVel(0.0f, 0.0f, 0.0f);
				ALLoader.addSound(10, (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] {e.getX(), e.getY(), e.getZ()}).rewind());
				break;
		}
	}
	
	public Vector3f collides(Entity e, float er) {
		if (rad <= 0) return null;
		float ex = e.getX();
		float ez = e.getZ();
		boolean in = false;
		main: {
			if ((ex - er >= pos.x - rad && ex - er <= pos.x + rad) &&
					(ez >= pos.z - rad && ez <= pos.z + rad)) {
				in = true;
				break main;
			} else if ((ex + er >= pos.x - rad && ex + er <= pos.x + rad) &&
					(ez >= pos.z - rad && ez <= pos.z + rad)) {
				in = true;
				break main;
			}
			if ((ez - er >= pos.z - rad && ez - er <= pos.z + rad) &&
					(ex >= pos.x - rad && ex <= pos.x + rad)) {
				in = true;
				break main;
			} else if ((ez + er >= pos.z - rad && ez + er <= pos.z + rad) &&
					(ex >= pos.x - rad && ex <= pos.x + rad)) {
				in = true;
				break main;
			}
		}
		if (in) {
			if (e.getY() >= pos.y - height) doCommand(e);
		}
		return null;
	}
}
