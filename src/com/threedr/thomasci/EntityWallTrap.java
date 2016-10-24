package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;

public class EntityWallTrap extends EntityTrap {
	private float depth;
	
	public EntityWallTrap(float x, float z, Game g, String activ, float rot) {
		super(x, z, g, activ);
		tex = TDR.getTexture("wallTrap", "img/wallTrap.png");
		this.rot.y = rot;
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
				depth -= 0.01f;
				if (depth <= 0.0f) {
					depth = 0.0f;
					active = true;
				}
			} else {
				if (depth < 1.5f) {
					depth += 0.15f;
				}
				if (depth > 1.5f) {
					depth = 1.5f;
				}
			}
		}
		
		forces();
	}
	
	public void draw(float lx, float lz) {
		GL11.glPushMatrix();
		
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glTranslatef(-pos.x, -pos.y + 1.0f, -pos.z);
		switch ((int) rot.y) {
			case 0:
				GL11.glTranslatef(0, 0, -1.037f);
				break;
			case 90:
				GL11.glTranslatef(-1.037f, 0, 0);
				break;
			case 180:
				GL11.glTranslatef(0, 0, 1.037f);
				break;
			case 270:
				GL11.glTranslatef(1.037f, 0, 0);
				break;
		}
		GL11.glRotatef(rot.y, 0, 1, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		
		//wall part
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex3f(-1.0f, -1.0f, 0.04f);
			GL11.glTexCoord2f(0.5f, 1);
			GL11.glVertex3f(1.0f, -1.0f, 0.04f);
			GL11.glTexCoord2f(0.5f, 0);
			GL11.glVertex3f(1.0f, 1.0f, 0.04f);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(-1.0f, 1.0f, 0.04f);
		GL11.glEnd();
		
		//spikes
		if (!active) {
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glTranslatef(0.0f, 0.0f, depth - 1.5f);
			GL11.glTranslatef(0.375f, 0, 0);
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(0.5f, 1);
				GL11.glVertex3f(0.0f, 1.0f, 0.0f);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex3f(0.0f, -1.0f, 0.0f);
				GL11.glTexCoord2f(1, 0);
				GL11.glVertex3f(0.0f, -1.0f, 2.0f);
				GL11.glTexCoord2f(0.5f, 0);
				GL11.glVertex3f(0.0f, 1.0f, 2.0f);
			GL11.glEnd();
			
			GL11.glTranslatef(-0.75f, 0, 0);
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(0.5f, 1);
				GL11.glVertex3f(0.0f, 1.0f, 0.0f);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex3f(0.0f, -1.0f, 0.0f);
				GL11.glTexCoord2f(1, 0);
				GL11.glVertex3f(0.0f, -1.0f, 2.0f);
				GL11.glTexCoord2f(0.5f, 0);
				GL11.glVertex3f(0.0f, 1.0f, 2.0f);
			GL11.glEnd();
			GL11.glEnable(GL11.GL_CULL_FACE);
		}
		
		GL11.glPopMatrix();
	}
}
