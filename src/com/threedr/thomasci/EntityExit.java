package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;

public class EntityExit extends Entity {
	public EntityExit(float x, float z, float rot, Game g) {
		super(x, z, g);
		tex = TDR.getTexture("doors", "img/doors.png");
		this.rot.y = rot;
	}
	
	public void activate(Item i) {
		game.finishGame();
	}
	
	public void draw(float lx, float lz) {
		//don't render this object if it is outside of the player's view
		if (Math.abs(pos.x - lx) > 15) return;
		if (Math.abs(pos.z - lz) > 15) return;
		
		GL11.glPushMatrix();
		float pw = 1.0f / tex.getWidth();
		float w = pw * 16;
		float tx = 4 * w;
		
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
		GL11.glRotatef(rot.y, 0, 1, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(tx + w, 1.0f);
			GL11.glVertex3f(-1.0f, 0.0f, 0.997f);
			GL11.glTexCoord2f(tx + w, 0.0f);
			GL11.glVertex3f(-1.0f, 2.0f, 0.997f);
			GL11.glTexCoord2f(tx, 0.0f);
			GL11.glVertex3f(1.0f, 2.0f, 0.997f);
			GL11.glTexCoord2f(tx, 1.0f);
			GL11.glVertex3f(1.0f, 0.0f, 0.997f);
		GL11.glEnd();
		
		GL11.glPopMatrix();
	}
	
	public void drawBox(float lx, float lz) {
		GL11.glPushMatrix();
		GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
		GL11.glRotatef(rot.y, 0, 1, 0);
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex3f(-1.0f, 0.0f, 0.997f);
			GL11.glVertex3f(-1.0f, 2.0f, 0.997f);
			GL11.glVertex3f(1.0f, 2.0f, 0.997f);
			GL11.glVertex3f(1.0f, 0.0f, 0.997f);
		GL11.glEnd();
		
		GL11.glPopMatrix();
	}
}
