package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;

public class EntityButton extends EntityActivator {
	private String activName;
	private int wait;
	private boolean once;
	
	public EntityButton(float x, float z, Game g, int rot, String activ) {
		super(x, z, g, activ);
		this.rot.y = rot;
		tex = TDR.getTexture("hbutton", "img/hbutton.png");
		activName = activ;
	}
	
	public void setup() {
		Activator act = game.getActivator(activName);
		if (act != null) {
			act.addObject(this);
			if (act.isOnce()) once = true;
		}
	}
	
	public void tick() {
		if (wait > 0) wait--;
		if (wait <= 0) {
			if (!once) active = false;
		}
		forces();
	}
	
	public void activate(Item i) {
		active = true;
		wait = 32;
	}
	
	public void draw(float lx, float lz) {
		//don't render this object if it is outside of the player's view
		if (Math.abs(pos.x - lx) > 15) return;
		if (Math.abs(pos.z - lz) > 15) return;
		
		GL11.glPushMatrix();
		float pw = 1.0f / tex.getWidth(), ph = 1.0f / tex.getHeight();
		
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glTranslatef(-pos.x, -pos.y + 1.0f, -pos.z);
		float dist = 0.0f;
		if (wait > 0) dist = 0.039f;
		switch ((int) rot.y) {
			case 0:
				GL11.glTranslatef(-0.625f, 0, -1 - dist);
				break;
			case 90:
				GL11.glTranslatef(-1 - dist, 0, 0.625f);
				break;
			case 180:
				GL11.glTranslatef(0.625f, 0, 1 + dist);
				break;
			case 270:
				GL11.glTranslatef(1 + dist, 0, -0.625f);
				break;
		}
		GL11.glRotatef(rot.y, 0, 1, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		
		//front
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(pw, 4 * ph);
			GL11.glVertex3f(0.0f, 0.0f, 0.04f);
			GL11.glTexCoord2f(8 * pw, 4 * ph);
			GL11.glVertex3f(0.875f, 0.0f, 0.04f);
			GL11.glTexCoord2f(8 * pw, ph);
			GL11.glVertex3f(0.875f, 0.375f, 0.04f);
			GL11.glTexCoord2f(pw, ph);
			GL11.glVertex3f(0.0f, 0.375f, 0.04f);
		GL11.glEnd();
		
		//top
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(pw, ph);
			GL11.glVertex3f(0.0f, 0.375f, 0.04f);
			GL11.glTexCoord2f(8 * pw, ph);
			GL11.glVertex3f(0.875f, 0.375f, 0.04f);
			GL11.glTexCoord2f(8 * pw, 0);
			GL11.glVertex3f(0.875f, 0.375f, -0.04f);
			GL11.glTexCoord2f(pw, 0);
			GL11.glVertex3f(0.0f, 0.375f, -0.04f);
		GL11.glEnd();
		
		//left
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 4 * ph);
			GL11.glVertex3f(0.0f, 0.0f, -0.04f);
			GL11.glTexCoord2f(pw, 4 * ph);
			GL11.glVertex3f(0.0f, 0.0f, 0.04f);
			GL11.glTexCoord2f(pw, ph);
			GL11.glVertex3f(0.0f, 0.375f, 0.04f);
			GL11.glTexCoord2f(0, ph);
			GL11.glVertex3f(0.0f, 0.375f, -0.04f);
		GL11.glEnd();
		
		//bottom
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(pw, ph);
			GL11.glVertex3f(0.0f, 0.0f, -0.04f);
			GL11.glTexCoord2f(8 * pw, ph);
			GL11.glVertex3f(0.875f, 0.0f, -0.04f);
			GL11.glTexCoord2f(8 * pw, 0);
			GL11.glVertex3f(0.875f, 0.0f, 0.04f);
			GL11.glTexCoord2f(pw, 0);
			GL11.glVertex3f(0.0f, 0.0f, 0.04f);
		GL11.glEnd();
		
		//right
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 4 * ph);
			GL11.glVertex3f(0.875f, 0.0f, 0.04f);
			GL11.glTexCoord2f(pw, 4 * ph);
			GL11.glVertex3f(0.875f, 0.0f, -0.04f);
			GL11.glTexCoord2f(pw, ph);
			GL11.glVertex3f(0.875f, 0.375f, -0.04f);
			GL11.glTexCoord2f(0, ph);
			GL11.glVertex3f(0.875f, 0.375f, 0.04f);
		GL11.glEnd();
		
		GL11.glPopMatrix();
	}
	
	public void drawBox(float lx, float lz) {
		GL11.glPushMatrix();
		
		GL11.glTranslatef(-pos.x, -pos.y + 1.0f, -pos.z);
		switch ((int) rot.y) {
			case 0:
				GL11.glTranslatef(-0.625f, 0, -1);
				break;
			case 90:
				GL11.glTranslatef(-1, 0, 0.625f);
				break;
			case 180:
				GL11.glTranslatef(0.625f, 0, 1);
				break;
			case 270:
				GL11.glTranslatef(1, 0, -0.625f);
				break;
		}
		GL11.glRotatef(rot.y, 0, 1, 0);
		
		//front
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex3f(0.0f, 0.0f, 0.04f);
			GL11.glVertex3f(0.875f, 0.0f, 0.04f);
			GL11.glVertex3f(0.875f, 0.375f, 0.04f);
			GL11.glVertex3f(0.0f, 0.375f, 0.04f);
		GL11.glEnd();
		
		//top
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex3f(0.0f, 0.375f, 0.04f);
			GL11.glVertex3f(0.875f, 0.375f, 0.04f);
			GL11.glVertex3f(0.875f, 0.375f, -0.04f);
			GL11.glVertex3f(0.0f, 0.375f, -0.04f);
		GL11.glEnd();
		
		//left
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex3f(0.0f, 0.0f, -0.04f);
			GL11.glVertex3f(0.0f, 0.0f, 0.04f);
			GL11.glVertex3f(0.0f, 0.375f, 0.04f);
			GL11.glVertex3f(0.0f, 0.375f, -0.04f);
		GL11.glEnd();
		
		//bottom
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex3f(0.0f, 0.0f, -0.04f);
			GL11.glVertex3f(0.875f, 0.0f, -0.04f);
			GL11.glVertex3f(0.875f, 0.0f, 0.04f);
			GL11.glVertex3f(0.0f, 0.0f, 0.04f);
		GL11.glEnd();
		
		//right
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex3f(0.875f, 0.0f, 0.04f);
			GL11.glVertex3f(0.875f, 0.0f, -0.04f);
			GL11.glVertex3f(0.875f, 0.375f, -0.04f);
			GL11.glVertex3f(0.875f, 0.375f, 0.04f);
		GL11.glEnd();
		
		GL11.glPopMatrix();
	}
}
