package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class EntityTrap extends EntityObstacle {
	protected boolean active;
	private float height;
	protected long timer;
	
	public EntityTrap(float x, float z, Game g, String activ) {
		super(x, z, g, activ);
		tex = TDR.getTexture("floorTrap", "img/floorTrap.png");
		active = true;
		rad = 0.375f;
		height = 0.0f;
	}
	
	public void tick() {
		if (activator != null) {
			if (activator.isActive()) {
				active = false;
				timer = System.currentTimeMillis() + 5000;
			}
		}
		if (!active) {
			if (System.currentTimeMillis() > timer) {
				height -= 0.01f;
				if (height <= 0.0f) {
					height = 0.0f;
					active = true;
				}
			} else {
				if (height < 0.75f) {
					height += 0.15f;
				}
				if (height > 0.75f) {
					height = 0.75f;
				}
			}
		}
		
		forces();
	}
	
	public void draw(float lx, float lz) {
		GL11.glPushMatrix();
		
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		
		//ground part
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(-1.0f, 0.001f, -1.0f);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex3f(-1.0f, 0.001f, 1.0f);
			GL11.glTexCoord2f(0.5f, 1);
			GL11.glVertex3f(1.0f, 0.001f, 1.0f);
			GL11.glTexCoord2f(0.5f, 0);
			GL11.glVertex3f(1.0f, 0.001f, -1.0f);
		GL11.glEnd();
		
		//spikes
		if (!active) {
			GL11.glTranslatef(0.0f, height - 0.75f, 0.0f);
			float r = (float) Math.toDegrees(Math.atan2(pos.x + 0.375f - lx, pos.z + 0.375f - lz));
			
			GL11.glPushMatrix();
			GL11.glTranslatef(-0.375f, 0.0f, -0.375f);
			GL11.glRotatef(r, 0.0f, 1.0f, 0.0f);
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(0.5f, 1);
				GL11.glVertex3f(1.0f, 0.0f, 0.0f);
				GL11.glTexCoord2f(0.5f, 0);
				GL11.glVertex3f(1.0f, 2.0f, 0.0f);
				GL11.glTexCoord2f(1, 0);
				GL11.glVertex3f(-1.0f, 2.0f, 0.0f);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex3f(-1.0f, 0.0f, 0.0f);
			GL11.glEnd();
			GL11.glPopMatrix();
			
			r = (float) Math.toDegrees(Math.atan2(pos.x - 0.375f - lx, pos.z + 0.375f - lz));
			
			GL11.glPushMatrix();
			GL11.glTranslatef(0.375f, 0.0f, -0.375f);
			GL11.glRotatef(r, 0.0f, 1.0f, 0.0f);
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(0.5f, 1);
				GL11.glVertex3f(1.0f, 0.0f, 0.0f);
				GL11.glTexCoord2f(0.5f, 0);
				GL11.glVertex3f(1.0f, 2.0f, 0.0f);
				GL11.glTexCoord2f(1, 0);
				GL11.glVertex3f(-1.0f, 2.0f, 0.0f);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex3f(-1.0f, 0.0f, 0.0f);
			GL11.glEnd();
			GL11.glPopMatrix();
			
			r = (float) Math.toDegrees(Math.atan2(pos.x - 0.375f - lx, pos.z - 0.375f - lz));
			
			GL11.glPushMatrix();
			GL11.glTranslatef(0.375f, 0.0f, 0.375f);
			GL11.glRotatef(r, 0.0f, 1.0f, 0.0f);
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(0.5f, 1);
				GL11.glVertex3f(1.0f, 0.0f, 0.0f);
				GL11.glTexCoord2f(0.5f, 0);
				GL11.glVertex3f(1.0f, 2.0f, 0.0f);
				GL11.glTexCoord2f(1, 0);
				GL11.glVertex3f(-1.0f, 2.0f, 0.0f);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex3f(-1.0f, 0.0f, 0.0f);
			GL11.glEnd();
			GL11.glPopMatrix();
			
			r = (float) Math.toDegrees(Math.atan2(pos.x + 0.375f - lx, pos.z - 0.375f - lz));
			
			GL11.glPushMatrix();
			GL11.glTranslatef(-0.375f, 0.0f, 0.375f);
			GL11.glRotatef(r, 0.0f, 1.0f, 0.0f);
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(0.5f, 1);
				GL11.glVertex3f(1.0f, 0.0f, 0.0f);
				GL11.glTexCoord2f(0.5f, 0);
				GL11.glVertex3f(1.0f, 2.0f, 0.0f);
				GL11.glTexCoord2f(1, 0);
				GL11.glVertex3f(-1.0f, 2.0f, 0.0f);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex3f(-1.0f, 0.0f, 0.0f);
			GL11.glEnd();
			GL11.glPopMatrix();
		}
		
		GL11.glPopMatrix();
	}
	
	public Vector3f collides(Entity e, float er) {
		if (!active) return null;
		if (rad <= 0) return null;
		float dirx = 0, dirz = 0;
		float ex = e.getX();
		float ez = e.getZ();
		if ((ex - er > pos.x - rad && ex - er < pos.x + rad) &&
				(ez > pos.z - rad && ez < pos.z + rad)) {
			dirx = 1;
		} else if ((ex + er > pos.x - rad && ex + er < pos.x + rad) &&
				(ez > pos.z - rad && ez < pos.z + rad)) {
			dirx = 1;
		}
		if ((ez - er > pos.z - rad && ez - er <= pos.z + rad) &&
				(ex > pos.x - rad && ex <= pos.x + rad)) {
			dirz = 1;
		} else if ((ez + er > pos.z - rad && ez + er < pos.z + rad) &&
				(ex > pos.x - rad && ex < pos.x + rad)) {
			dirz = 1;
		}
		if (dirx != 0 || dirz != 0) {
			if (e.getY() > pos.y - 0.125f) {
				if (e instanceof EntityLiving) {
					((EntityLiving) e).damage(5, 0);
				}
				active = false;
				timer = System.currentTimeMillis() + 5000;
			}
		}
		return null;
	}
}
