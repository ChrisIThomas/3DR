package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class EntityPressurePlate extends EntityActivator {
	private String activName;
	private int wait;
	
	public EntityPressurePlate(float x, float z, Game g, int rot, String activ) {
		super(x, z, g, activ);
		this.rot.y = rot;
		tex = TDR.getTexture("pressure", "img/pressure.png");
		activName = activ;
		rad = 0.75f;
	}
	
	public void setup() {
		Activator act = game.getActivator(activName);
		if (act != null) {
			act.addObject(this);
		}
	}
	
	public void tick() {
		if (wait > 0) wait--;
		else active = false;
		forces();
	}
	
	public void draw(float lx, float lz) {
		//don't render this object if it is outside of the player's view
		if (Math.abs(pos.x - lx) > 15) return;
		if (Math.abs(pos.z - lz) > 15) return;
		
		GL11.glPushMatrix();
		float ph = 1.0f / tex.getHeight();
		
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
		if (active) GL11.glTranslatef(0.0f, -0.124f, 0.0f);
		GL11.glRotatef(rot.y, 0, 1, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		
		//top
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex3f(0.75f, 0.125f, -0.75f);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex3f(-0.75f, 0.125f, -0.75f);
			GL11.glTexCoord2f(1, ph);
			GL11.glVertex3f(-0.75f, 0.125f, 0.75f);
			GL11.glTexCoord2f(0, ph);
			GL11.glVertex3f(0.75f, 0.125f, 0.75f);
		GL11.glEnd();
		
		//front
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(-0.75f, 0.0f, -0.75f);
			GL11.glTexCoord2f(0, ph);
			GL11.glVertex3f(-0.75f, 0.125f, -0.75f);
			GL11.glTexCoord2f(1, ph);
			GL11.glVertex3f(0.75f, 0.125f, -0.75f);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex3f(0.75f, 0.0f, -0.75f);
		GL11.glEnd();
		
		//left
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(-0.75f, 0.0f, 0.75f);
			GL11.glTexCoord2f(0, ph);
			GL11.glVertex3f(-0.75f, 0.125f, 0.75f);
			GL11.glTexCoord2f(1, ph);
			GL11.glVertex3f(-0.75f, 0.125f, -0.75f);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex3f(-0.75f, 0.0f, -0.75f);
		GL11.glEnd();
		
		//back
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(0.75f, 0.0f, 0.75f);
			GL11.glTexCoord2f(0, ph);
			GL11.glVertex3f(0.75f, 0.125f, 0.75f);
			GL11.glTexCoord2f(1, ph);
			GL11.glVertex3f(-0.75f, 0.125f, 0.75f);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex3f(-0.75f, 0.0f, 0.75f);
		GL11.glEnd();
		
		//right
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(0.75f, 0.0f, -0.75f);
			GL11.glTexCoord2f(0, ph);
			GL11.glVertex3f(0.75f, 0.125f, -0.75f);
			GL11.glTexCoord2f(1, ph);
			GL11.glVertex3f(0.75f, 0.125f, 0.75f);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex3f(0.75f, 0.0f, 0.75f);
		GL11.glEnd();
		
		GL11.glPopMatrix();
	}
	
	public void drawBox(float lx, float lz) {
		GL11.glPushMatrix();
		
		GL11.glTranslatef(-pos.x, -pos.y + 0.5f, -pos.z);
		switch ((int) rot.y) {
			case 0:
				GL11.glTranslatef(-0.5f, 0, -1);
				break;
			case 90:
				GL11.glTranslatef(-1, 0, 0.5f);
				break;
			case 180:
				GL11.glTranslatef(0.5f, 0, 1);
				break;
			case 270:
				GL11.glTranslatef(1, 0, -0.5f);
				break;
		}
		GL11.glRotatef(rot.y, 0, 1, 0);
		
		//front
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex3f(0.0f, 0.0f, 0.125f);
			GL11.glVertex3f(1.0f, 0.0f, 0.125f);
			GL11.glVertex3f(1.0f, 1.0f, 0.125f);
			GL11.glVertex3f(0.0f, 1.0f, 0.125f);
		GL11.glEnd();
		
		//top
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex3f(0.0f, 1.0f, 0.125f);
			GL11.glVertex3f(1.0f, 1.0f, 0.125f);
			GL11.glVertex3f(1.0f, 1.0f, -0.125f);
			GL11.glVertex3f(0.0f, 1.0f, -0.125f);
		GL11.glEnd();
		
		//left
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex3f(0.0f, 0.0f, -0.125f);
			GL11.glVertex3f(0.0f, 0.0f, 0.125f);
			GL11.glVertex3f(0.0f, 1.0f, 0.125f);
			GL11.glVertex3f(0.0f, 1.0f, -0.125f);
		GL11.glEnd();
		
		//bottom
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex3f(0.0f, 0.0f, -0.125f);
			GL11.glVertex3f(1.0f, 0.0f, -0.125f);
			GL11.glVertex3f(1.0f, 0.0f, 0.125f);
			GL11.glVertex3f(0.0f, 0.0f, 0.125f);
		GL11.glEnd();
		
		//right
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex3f(1.0f, 0.0f, 0.125f);
			GL11.glVertex3f(1.0f, 0.0f, -0.125f);
			GL11.glVertex3f(1.0f, 1.0f, -0.125f);
			GL11.glVertex3f(1.0f, 1.0f, 0.125f);
		GL11.glEnd();
		
		GL11.glPopMatrix();
	}
	
	public Vector3f collides(Entity e, float er) {
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
				wait = 32;
				active = true;
			}
		}
		return null;
	}
}
