package com.threedr.thomasci;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class EntityDoor extends EntityObstacle {
	private int type;
	private boolean open;
	private float start = 0;
	private Activator activator;
	private String activName;
	private Vector3f col;
	
	public EntityDoor(float x, float z, Game g, int rot, int style, String actName) {
		super(x, z, g, "");
		rad = 0.5f;
		tex = TDR.getTexture("doors", "img/doors.png");
		type = style;
		open = false;
		this.rot.y = rot;
		start = this.rot.y;
		activName = actName;
	}
	
	public void setup() {
		activator = game.getActivator(activName);
	}
	
	public void setCol(float r, float g, float b) {
		col = new Vector3f(r, g, b);
	}
	
	public String getActivName() {
		return activName;
	}
	
	public void update() {
		if (activator != null)
			if (activator.isActive()) open = true;
		if (open) {
			if (start == 270) {
				if (rot.y > 0) {
					rot.y+= 2;
				}
			} else {
				if (rot.y != start + 90) {
					rot.y += 2;
				}
			}
		} else {
			if (rot.y != start) {
				rot.y -= 2;
			}
		}
	}
	
	public void tick() {
		if (rot.y < 0) rot.y += 360;
		if (rot.y >= 360) rot.y -= 360;
		
		forces();
	}
	
	public void activate(Item i) {
		boolean state = open;
		if (i != null) {
			if (!activName.equals("Def")) {
				if (activName.equals(i.getIDName())) {
					open = !open;
					i.setDelete(true);
				}
			}
		}
		if (activator == null && activName.equals("Def")) open = !open;
		if (state != open) {
			if (type == 1) {
				ALLoader.addSound(0, (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] {pos.x, pos.y, pos.z}).rewind());
			} else {
				ALLoader.addSound(1, (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] {pos.x, pos.y, pos.z}).rewind());
			}
		}
	}
	
	public void draw(float lx, float lz) {
		//don't render this object if it is outside of the player's view
		if (Math.abs(pos.x - lx) > 15) return;
		if (Math.abs(pos.z - lz) > 15) return;
		
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		float pw = 1.0f / tex.getWidth();
		float w = pw * 16;
		int tx = type;
		
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		float dirx = 0, dirz = 0;
		switch ((int) start) {
			case 0:
				dirz = 0.997f;
				break;
			case 90:
				dirx = 0.997f;
				break;
			case 180:
				dirz = -0.997f;
				break;
			case 270:
				dirx = -0.997f;
				break;
		}
		GL11.glTranslatef(-pos.x + dirx, -pos.y, -pos.z + dirz);
		GL11.glRotatef(rot.y, 0, 1, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(tx * w, 1.0f);
			GL11.glVertex3f(0.0f, 0.0f, -2.0f);
			GL11.glTexCoord2f((tx + 1) * w, 1.0f);
			GL11.glVertex3f(0.0f, 0.0f, 0.0f);
			GL11.glTexCoord2f((tx + 1) * w, 0.0f);
			GL11.glVertex3f(0.0f, 2.0f, 0.0f);
			GL11.glTexCoord2f(tx * w, 0.0f);
			GL11.glVertex3f(0.0f, 2.0f, -2.0f);
		GL11.glEnd();
		
		tx += 2;
		
		if (col != null) GL11.glColor3f(col.x, col.y, col.z);
		else GL11.glColor3f(0.1f, 0.1f, 0.1f);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(tx * w, 1.0f);
			GL11.glVertex3f(0.0f, 0.0f, -2.0f);
			GL11.glTexCoord2f((tx + 1) * w, 1.0f);
			GL11.glVertex3f(0.0f, 0.0f, 0.0f);
			GL11.glTexCoord2f((tx + 1) * w, 0.0f);
			GL11.glVertex3f(0.0f, 2.0f, 0.0f);
			GL11.glTexCoord2f(tx * w, 0.0f);
			GL11.glVertex3f(0.0f, 2.0f, -2.0f);
		GL11.glEnd();
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}
	
	public void drawBox(float lx, float lz) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		float dirx = 0, dirz = 0;
		switch ((int) start) {
			case 0:
				dirz = 0.997f;
				break;
			case 90:
				dirx = 0.997f;
				break;
			case 180:
				dirz = -0.997f;
				break;
			case 270:
				dirx = -0.997f;
				break;
		}
		GL11.glTranslatef(-pos.x + dirx, -pos.y, -pos.z + dirz);
		GL11.glRotatef(rot.y, 0, 1, 0);
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex3f(0.0f, 0.0f, -2.0f);
			GL11.glVertex3f(0.0f, 0.0f, 0.0f);
			GL11.glVertex3f(0.0f, 2.0f, 0.0f);
			GL11.glVertex3f(0.0f, 2.0f, -2.0f);
		GL11.glEnd();
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}
	
	public Vector3f collides(Entity e, float er) {
		if (rad <= 0) return null;
		if (open) return null;
		if (rot.y != start) return null;
		float dirx = 0, dirz = 0;
		float ex = e.getX();
		float ez = e.getZ();
		if (rot.y / 90 % 2 == 0) {
			if ((ex - er >= pos.x - rad && ex - er <= pos.x + rad) &&
					(ez >= pos.z - 1.0f && ez <= pos.z + 1.0f)) {
				dirx = 1;
			} else if ((ex + er >= pos.x - rad && ex + er <= pos.x + rad) &&
					(ez >= pos.z - 1.0f && ez <= pos.z + 1.0f)) {
				dirx = -1;
			}
			if (dirx != 0 || dirz != 0) {
				float x = ex, z = ez;
				if (dirx != 0)
					x = pos.x + (rad + er) * dirx;
				if (dirz != 0)
					z = pos.z + (1.0f + er) * dirz;
				return new Vector3f(x, 0, z);
			}
		} else {
			if ((ez - er >= pos.z - rad && ez - er <= pos.z + rad) &&
					(ex >= pos.x - 1.0f && ex <= pos.x + 1.0f)) {
				dirz = 1;
			} else if ((ez + er >= pos.z - rad && ez + er <= pos.z + rad) &&
					(ex >= pos.x - 1.0f && ex <= pos.x + 1.0f)) {
				dirz = -1;
			}
			if (dirx != 0 || dirz != 0) {
				float x = ex, z = ez;
				if (dirx != 0)
					x = pos.x + (1.0f + er) * dirx;
				if (dirz != 0)
					z = pos.z + (rad + er) * dirz;
				return new Vector3f(x, 0, z);
			}
		}
		return null;
	}
	
	public boolean collides(float x, float z, float er) {
		if (open) return false;
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
}
