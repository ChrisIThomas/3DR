package com.threedr.thomasci;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class EntityObstacle extends Entity {
	private float height, wheight;
	private boolean open;
	protected Activator activator;
	private String activName;
	
	public EntityObstacle(float x, float z, Game g, String activ) {
		super(x, z, g);
		rad = 1.0f;
		tex = TDR.getTexture("tiles", "img/tiles.png");
		open = false;
		height = 0.0f;
		wheight = game.getHeight((int) -pos.x / 2, (int) -pos.z / 2);
		if (game.getTile((int) -pos.x / 2, (int) -pos.z / 2) == 1) {
			wheight = ((int) wheight) - wheight;
		} else {
			wheight = 0;
		}
		pos.y -= wheight;
		activName = activ;
	}
	
	public void setup() {
		activator = game.getActivator(activName);
	}
	
	//i can overwrite the tick function because the obstacle doesn't move
	public void tick() {
		if (activator != null) {
			boolean start = open;
			open = activator.isActive();
			if (open) {
				if (height > -2.01f - wheight) {
					height -= 0.01f;
				} else if (height < -2.01f - wheight) {
					height = -2.01f - wheight;
				}
			} else {
				if (height < 0.0f) {
					height += 0.01f;
				} else if (height > 0.0f) {
					height = 0.0f;
				}
			}
			if (start != open) {
				ALLoader.addSound(2, (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] {pos.x, pos.y, pos.z}).rewind());
			}
		}
		
		forces();
	}
	
	public void draw(float lx, float lz) {
		//don't render this object if it is outside of the player's view
		if (Math.abs(pos.x - lx) > 15) return;
		if (Math.abs(pos.z - lz) > 15) return;
		
		if (height < -2.01f - wheight) return;
		float pw = 1.0f / tex.getWidth();
		float ph = 1.0f / tex.getHeight();
		
		GL11.glPushMatrix();
		//int tile = 3;
		float w = pw * 16;
		float h = ph * 16;
		int tile = (int) game.getTile((int) -pos.x / 2, (int) -pos.z / 2);
		if (tile == 1) tile = 2;
		int ty = tile / 8;
		int tx = tile - ty * 8;
		ty *= 8;
		
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glTranslatef(-pos.x, -pos.y + height, -pos.z);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		
		//FLOOR
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(tx * w, ty * h);
			GL11.glVertex3f(-1.0f, 2.0f, -1.0f);
			GL11.glTexCoord2f(tx * w, (ty + 1) * h);
			GL11.glVertex3f(-1.0f, 2.0f, 1.0f);
			GL11.glTexCoord2f((tx + 1) * w, (ty + 1) * h);
			GL11.glVertex3f(1.0f, 2.0f, 1.0f);
			GL11.glTexCoord2f((tx + 1) * w, ty * h);
			GL11.glVertex3f(1.0f, 2.0f, -1.0f);
		GL11.glEnd();
		
		/*tile = this.tile;
		ty = tile / 8;
		tx = tile - ty * 8;
		ty *= 8;*/
		
		int water = game.getTile((int) -pos.x / 2, (int) -pos.z / 2) == 1 ? 2 : 1;
		tile = 2;
		ty = tile / 8;
		tx = tile - ty * 8;
		ty *= 8;
		
		//WALLS
		while (water > 0) {
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(tx * w, (ty + 1) * h);
				GL11.glVertex3f(-1.0f, 0.0f, -1.0f);
				GL11.glTexCoord2f((tx + 1) * w, (ty + 1) * h);
				GL11.glVertex3f(-1.0f, 0.0f, 1.0f);
				GL11.glTexCoord2f((tx + 1) * w, ty * h);
				GL11.glVertex3f(-1.0f, 2.0f, 1.0f);
				GL11.glTexCoord2f(tx * w, ty * h);
				GL11.glVertex3f(-1.0f, 2.0f, -1.0f);
			GL11.glEnd();
			
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f((tx + 1) * w, (ty + 1) * h);
				GL11.glVertex3f(-1.0f, 0.0f, -1.0f);
				GL11.glTexCoord2f((tx + 1) * w, ty * h);
				GL11.glVertex3f(-1.0f, 2.0f, -1.0f);
				GL11.glTexCoord2f(tx * w, ty * h);
				GL11.glVertex3f(1.0f, 2.0f, -1.0f);
				GL11.glTexCoord2f(tx * w, (ty + 1) * h);
				GL11.glVertex3f(1.0f, 0.0f, -1.0f);
			GL11.glEnd();
			
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(tx * w, (ty + 1) * h);
				GL11.glVertex3f(1.0f, 0.0f, 1.0f);
				GL11.glTexCoord2f((tx + 1) * w, (ty + 1) * h);
				GL11.glVertex3f(1.0f, 0.0f, -1.0f);
				GL11.glTexCoord2f((tx + 1) * w, ty * h);
				GL11.glVertex3f(1.0f, 2.0f, -1.0f);
				GL11.glTexCoord2f(tx * w, ty * h);
				GL11.glVertex3f(1.0f, 2.0f, 1.0f);
			GL11.glEnd();
			
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f((tx + 1) * w, (ty + 1) * h);
				GL11.glVertex3f(1.0f, 0.0f, 1.0f);
				GL11.glTexCoord2f((tx + 1) * w, ty * h);
				GL11.glVertex3f(1.0f, 2.0f, 1.0f);
				GL11.glTexCoord2f(tx * w, ty * h);
				GL11.glVertex3f(-1.0f, 2.0f, 1.0f);
				GL11.glTexCoord2f(tx * w, (ty + 1) * h);
				GL11.glVertex3f(-1.0f, 0.0f, 1.0f);
			GL11.glEnd();
			water--;
			if (water > 0) GL11.glTranslatef(0, -2.0f, 0);
		}
		
		GL11.glPopMatrix();
	}
	
	public Vector3f collides(Entity e, float er) {
		if (open && height <= -2.01f) return null;
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
			float x = ex, z = ez;
			if (dirx != 0)
				x = pos.x + (rad + er) * dirx;
			if (dirz != 0)
				z = pos.z + (rad + er) * dirz;
			return new Vector3f(x, 0, z);
		}
		return null;
	}
	
	public boolean collides(float ex, float ez, float er) {
		if (open && height <= -2.01f) return false;
		if (rad <= 0) return false;
		if ((ex - er >= pos.x - rad && ex - er <= pos.x + rad) &&
				(ez >= pos.z - rad && ez <= pos.z + rad)) {
			return true;
		} else if ((ex + er >= pos.x - rad && ex + er <= pos.x + rad) &&
				(ez >= pos.z - rad && ez <= pos.z + rad)) {
			return true;
		}
		if ((ez - er >= pos.z - rad && ez - er <= pos.z + rad) &&
				(ex >= pos.x - rad && ex <= pos.x + rad)) {
			return true;
		} else if ((ez + er >= pos.z - rad && ez + er <= pos.z + rad) &&
				(ex >= pos.x - rad && ex <= pos.x + rad)) {
			return true;
		}
		return false;
	}
}
