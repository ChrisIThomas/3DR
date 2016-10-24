package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;

public class EntityWallDecoration extends EntityDecoration {
	public EntityWallDecoration(float x, float z, Game g, float rot, int type) {
		super(x, z, g, type);
		this.rot.y = rot;
		tex = TDR.getTexture("wallDecor", "img/wallDecor.png");
	}
	
	public void draw(float lx, float lz) {
		//don't render this object if it is outside of the player's view
		if (Math.abs(pos.x - lx) > 15) return;
		if (Math.abs(pos.z - lz) > 15) return;
		
		float pw = 1.0f / tex.getWidth();
		float ph = 1.0f / tex.getHeight();
		
		int tw = getWidth();
		int th = getHeight();
		
		float w = pw * 16;
		float h = ph * 16;
		float ty = getTexLoc() / 4;
		float tx = (getTexLoc() - ty * 4) * w;
		ty *= h;
		w *= tw;
		h *= th;
		
		GL11.glPushMatrix();
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
		GL11.glRotatef(rot.y, 0, 1, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(tx + w, ty + h);
			GL11.glVertex3f(1.0f * tw, 0.0f, -0.997f);
			GL11.glTexCoord2f(tx + w, ty);
			GL11.glVertex3f(1.0f * tw, th * 2, -0.997f);
			GL11.glTexCoord2f(tx, ty);
			GL11.glVertex3f(-1.0f * tw, th * 2, -0.997f);
			GL11.glTexCoord2f(tx, ty + h);
			GL11.glVertex3f(-1.0f * tw, 0.0f, -0.997f);
		GL11.glEnd();
		
		GL11.glPopMatrix();
	}
	
	private int getTexLoc() {
		switch (type) {
			case 1:
				return 1;
			case 2:
				return 2;
			case 3:
				return 3;
			case 4:
				return 4;
			case 5:
				return 6;
			case 6:
				return 8;
			case 7:
				return 9;
			case 8:
				return 10;
			case 9:
				return 12;
			case 10:
				return 13;
			case 11:
				return 14;
			case 12:
				return 15;
			case 13:
				return 16;
		}
		return 0;
	}
	
	private int getWidth() {
		switch (type) {
			case 4:
				return 2;
			case 5:
				return 2;
			case 8:
				return 2;
		}
		return 1;
	}
	
	private int getHeight() {
		switch (type) {
			case 13:
				return 2;
		}
		return 1;
	}
}