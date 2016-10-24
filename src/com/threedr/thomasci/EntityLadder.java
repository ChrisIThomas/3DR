package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class EntityLadder extends Entity {
	private boolean spawn;
	
	public EntityLadder(float x, float z, Game g, boolean spawn) {
		super(x, z, g);
		rad = 0.5f;
		tex = TDR.getTexture("ladder", "img/ladder.png");
		this.spawn = spawn;
	}
	
	public void activate(Item i) {
		game.finishLevel();
	}
	
	public void draw(float lx, float lz) {
		float w = 1.0f / tex.getWidth() * 12;
		float r = (float) Math.toDegrees(Math.atan2(pos.x - lx, pos.z - lz));
		
		GL11.glPushMatrix();
		
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		if (spawn) {
			GL11.glTranslatef(0.0f, 1.998f, 0.0f);
			
			//roof part
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(0, 0);
				GL11.glVertex3f(0.75f, 0.001f, -0.75f);
				GL11.glTexCoord2f(0, 1);
				GL11.glVertex3f(0.75f, 0.001f, 0.75f);
				GL11.glTexCoord2f(w, 1);
				GL11.glVertex3f(-0.75f, 0.001f, 0.75f);
				GL11.glTexCoord2f(w, 0);
				GL11.glVertex3f(-0.75f, 0.001f, -0.75f);
			GL11.glEnd();
			
			//move the ladder down
			GL11.glTranslatef(0.0f, -1.0f, 0.0f);
		} else {
			//ground part
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(0, 0);
				GL11.glVertex3f(-0.75f, 0.001f, -0.75f);
				GL11.glTexCoord2f(0, 1);
				GL11.glVertex3f(-0.75f, 0.001f, 0.75f);
				GL11.glTexCoord2f(w, 1);
				GL11.glVertex3f(0.75f, 0.001f, 0.75f);
				GL11.glTexCoord2f(w, 0);
				GL11.glVertex3f(0.75f, 0.001f, -0.75f);
			GL11.glEnd();
			
		}
		
		//ladder
		float h = 1.0f / tex.getHeight() * 8;
		GL11.glRotatef(r, 0.0f, 1.0f, 0.0f);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(w, h);
			GL11.glVertex3f(0.5f, 0.0f, 0.0f);
			GL11.glTexCoord2f(w, 0);
			GL11.glVertex3f(0.5f, 1.0f, 0.0f);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex3f(-0.5f, 1.0f, 0.0f);
			GL11.glTexCoord2f(1, h);
			GL11.glVertex3f(-0.5f, 0.0f, 0.0f);
		GL11.glEnd();
		
		GL11.glPopMatrix();
	}
	
	public void drawBox(float lx, float lz) {
		//if this is the spawn ladder
		if (spawn) return;
		
		float r = (float) Math.toDegrees(Math.atan2(pos.x - lx, pos.z - lz));
		float w = 1.0f / tex.getWidth() * 12;
		float h = 1.0f / tex.getHeight() * 8;
		
		GL11.glPushMatrix();
		GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
		GL11.glRotatef(r, 0.0f, 1.0f, 0.0f);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(w, h);
			GL11.glVertex3f(0.5f, 0.0f, 0.0f);
			GL11.glTexCoord2f(w, 0);
			GL11.glVertex3f(0.5f, 1.0f, 0.0f);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex3f(-0.5f, 1.0f, 0.0f);
			GL11.glTexCoord2f(1, h);
			GL11.glVertex3f(-0.5f, 0.0f, 0.0f);
		GL11.glEnd();
		GL11.glPopMatrix();
	}
	
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
			float x = ex, z = ez;
			if (dirx != 0)
				x = pos.x + (rad + er) * dirx;
			if (dirz != 0)
				z = pos.z + (rad + er) * dirz;
			return new Vector3f(x, 0, z);
		}
		return null;
	}
}
