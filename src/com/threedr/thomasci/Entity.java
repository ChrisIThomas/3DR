package com.threedr.thomasci;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Entity {
	//vel.x represents our left and right movement speed, vel.z represents our forward and backwards movement speed.
	protected Vector3f pos, vel, rot;
	protected Texture tex;
	protected final Game game;
	protected float height, rad;
	private boolean destroy, drop;
	protected ArrayList<Effect> effects;
	
	public Entity(float x, float z, Game g) {
		pos = new Vector3f(x, 0.0f, z);
		vel = new Vector3f(0.0f, 0.0f, 0.0f);
		rot = new Vector3f(0.0f, 0.0f, 0.0f);
		game = g;
		height = 1.5f;
		rad = 0.25f;
		if (game != null) pos.y = -game.getHeight((int) -pos.x / 2, (int) ((-pos.z) / 2));
		effects = new ArrayList<Effect>();
	}
	
	public void setup() {}
	
	public void activate(Item i) {}
	
	public void update() {}
	
	public void forces() {
		if (game.getHeight((int) ((-pos.x - 0.25f) / 2), (int) -pos.z / 2) > -pos.y + 0.3f) {
			pos.x = Math.round(pos.x) - 0.25f;
		} else if (game.getHeight((int) ((-pos.x + 0.25f) / 2), (int) -pos.z / 2) > -pos.y + 0.3f) {
			pos.x = Math.round(pos.x) + 0.25f;
		}
		
		if (game.getHeight((int) -pos.x / 2, (int) ((-pos.z - 0.25f) / 2)) > -pos.y + 0.3f) {
			pos.z = Math.round(pos.z) - 0.25f;
		} else if (game.getHeight((int) -pos.x / 2, (int) ((-pos.z + 0.25f) / 2)) > -pos.y + 0.3f) {
			pos.z = Math.round(pos.z) + 0.25f;
		}
		
		
		if (game.getRoof((int) ((-pos.x) / 2), (int) -pos.z / 2) < -pos.y + height) {
			pos.y = game.getRoof((int) -pos.x / 2, (int) ((-pos.z) / 2)) - height;
			vel.y = 0;
		}
		
		if (game.getRoof((int) ((-pos.x - 0.25f) / 2), (int) -pos.z / 2) < -pos.y + height) {
			pos.x = Math.round(pos.x) - 0.25f;
			System.out.println("HIT");
		} else if (game.getRoof((int) ((-pos.x + 0.25f) / 2), (int) -pos.z / 2) < -pos.y + height) {
			pos.x = Math.round(pos.x) + 0.25f;
		}
		
		if (game.getRoof((int) -pos.x / 2, (int) ((-pos.z - 0.25f) / 2)) < -pos.y + height) {
			pos.z = Math.round(pos.z) - 0.25f;
			System.out.println("HIT");
		} else if (game.getRoof((int) -pos.x / 2, (int) ((-pos.z + 0.25f) / 2)) < -pos.y + height) {
			pos.z = Math.round(pos.z) + 0.25f;
			System.out.println("HIT");
		}
		
		if (vel.y > 2.0f) vel.y = 2.0f;
		if (vel.y < -2.0f) vel.y = -2.0f;
		
		if (-pos.y <= game.getHeight((int) -pos.x / 2, (int) ((-pos.z) / 2))) {
			pos.y = -game.getHeight((int) -pos.x / 2, (int) ((-pos.z) / 2));
			vel.y = 0;
		}
	}
	
	public void tick() {
		if (rot.y < 0) rot.y += 360;
		if (rot.y >= 360) rot.y -= 360;
		if (-pos.y > game.getHeight((int) -pos.x / 2, (int) ((-pos.z) / 2))) {
			vel.y += 0.025f;
		}
		if (-pos.y == game.getHeight((int) -pos.x / 2, (int) ((-pos.z) / 2))) {
			vel.x *= 0.85f;
			vel.z *= 0.85f;
		}
		if (vel.x <= 0.0001f && vel.x >= -0.0001f) vel.x = 0;
		if (vel.z <= 0.0001f && vel.z >= -0.0001f) vel.z = 0;
		//left and right movement
		pos.x += vel.x * Math.sin(Math.toRadians(rot.y + 90));
		pos.z -= vel.x * Math.cos(Math.toRadians(rot.y + 90));
		
		pos.y += vel.y;
		
		//forward and backward movement
		pos.x -= vel.z * Math.sin(Math.toRadians(rot.y));
		pos.z += vel.z * Math.cos(Math.toRadians(rot.y));
		
		if (rot.z < -90.0f) rot.z = -90;
		if (rot.z > 90.0f) rot.z = 90;
		if (pos.x > 0) {
			pos.x = 0;
		}
		if (pos.z > 0) {
			pos.z = 0;
		}
		if (pos.x < -game.getWidth() * 2) {
			pos.x = -game.getWidth() * 2;
		}
		if (pos.z < -game.getDepth() * 2) {
			pos.z = -game.getDepth() * 2;
		}
		forces();
	}
	
	public void draw(float lx, float lz) {}
	
	public void look() {
		GL11.glRotatef(rot.z, 1.0f, 0.0f, 0.0f);
		GL11.glRotatef(rot.y, 0.0f, 1.0f, 0.0f);
		GL11.glTranslatef(pos.x, pos.y - 1.3f, pos.z);
	}
	
	public Vector3f getVel() {
		return vel;
	}
	
	public void setPos(float x, float y, float z) {
		pos.x = -x;
		pos.y = -y;
		pos.z = -z;
	}
	
	public float getX() {
		return pos.x;
	}
	
	public float getY() {
		return pos.y;
	}
	
	public float getZ() {
		return pos.z;
	}
	
	public float getRot() {
		return rot.y;
	}
	
	public void drawBox(float lx, float lz) {}
	
	public Vector3f collides(Entity e, float er) {
		if (rad <= 0) return null;
		float dirx = 0, dirz = 0;
		float ex = e.getX();
		float ez = e.getZ();
		if ((ex - er >= pos.x - rad && ex - er <= pos.x + rad) &&
				(ez >= pos.z - rad && ez <= pos.z + rad)) {
			dirx = 1;
		} else if ((ex + er >= pos.x - rad && ex + er <= pos.x + rad) &&
				(ez >= pos.z - rad && ez <= pos.z + rad)) {
			dirx = -1;
		}
		if ((ez - er >= pos.z - rad && ez - er <= pos.z + rad) &&
				(ex >= pos.x - rad && ex <= pos.x + rad)) {
			dirz = 1;
		} else if ((ez + er >= pos.z - rad && ez + er <= pos.z + rad) &&
				(ex >= pos.x - rad && ex <= pos.x + rad)) {
			dirz = -1;
		}
		if (dirx != 0 || dirz != 0) {
			if (e.pos.y >= pos.y - height && e.pos.y <= pos.y) {
				float x = ex, z = ez;
				if (dirx != 0)
					x = pos.x + (rad + er) * dirx;
				if (dirz != 0)
					z = pos.z + (rad + er) * dirz;
				return new Vector3f(x, 0, z);
			}
		}
		return null;
	}
	
	public boolean collides(float x, float z, float er) {
		if (rad <= 0) return false;
		if ((x - er >= pos.x - rad && x - er <= pos.x + rad) &&
				(z >= pos.z - rad && z <= pos.z + rad)) {
			return true;
		} else if ((x + er >= pos.x - rad && x + er <= pos.x + rad) &&
				(z >= pos.z - rad && z <= pos.z + rad)) {
			return true;
		}
		if ((z - er >= pos.z - rad && z - er <= pos.z + rad) &&
				(x >= pos.x - rad && x <= pos.x + rad)) {
			return true;
		} else if ((z + er >= pos.z - rad && z + er <= pos.z + rad) &&
				(x >= pos.x - rad && x <= pos.x + rad)) {
			return true;
		}
		return false;
	}
	
	public void resetVel() {
		vel.x = 0.0f;
		vel.y = 0.0f;
		vel.z = 0.0f;
	}
	
	public void destroy(boolean d) {
		destroy = true;
		drop = d;
	}
	
	public boolean shouldDestroy() {
		return destroy;
	}
	
	public boolean shouldDrop() {
		return drop;
	}
	
	public void addEffect(Effect e) {
		for (int i = 0; i < effects.size(); i++) {
			if (effects.get(i).getType() == e.getType()) {
				effects.remove(i);
				i--;
			}
		}
		effects.add(e);
	}
	
	public Vector3f getPos() {
		return pos;
	}
	
	public void setVel(float x, float y, float z) {
		vel.x = x;
		vel.y = y;
		vel.z = z;
	}
	
	public ArrayList<Effect> getEffects() {
		return effects;
	}
}
