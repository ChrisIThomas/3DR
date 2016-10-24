package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;

public class GUISight extends GUIButton {
	private int per;
	
	public GUISight(float x, float y, String act, int id, GUI g) {
		super(x, y, act, id, g);
		tex = TDR.getTexture("sight", "img/sight.png");
	}
	
	//to determine sight i will check:
	//the speed of the player, add noting if they are still, add a little if they are moving slowly and a lot if they are moving fast
	//the more they are standing in the light, the more i will add
	public void setSight(int val) {
		per = val;
		if (per < 0) per = 0;
		else if (per > 100) per = 100;
	}

	public boolean over(int x, int y) {
		if (x >= pos.x && x < pos.x + 48) {
			if (y >= pos.y && y <= pos.y + 48) {
				return true;
			}
		}
		return false;
	}
	
	public void draw() {
		float pw = 1.0f / tex.getWidth();
		
		float w = pw * 16;
		float xp = (int) (per / 100.0f / 0.125f);
		if (xp > 7) xp = 7;
		xp *= w;
		
		GL11.glPushMatrix();
		GL11.glTranslatef(pos.x, pos.y, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(xp, 1);
			GL11.glVertex3f(-32.0f, 32.0f, 0.0f);
			GL11.glTexCoord2f(xp + w, 1);
			GL11.glVertex3f(32.0f, 32.0f, 0.0f);
			GL11.glTexCoord2f(xp + w, 0);
			GL11.glVertex3f(32.0f, 0.0f, 0.0f);
			GL11.glTexCoord2f(xp, 0);
			GL11.glVertex3f(-32.0f, 0.0f, 0.0f);
		GL11.glEnd();
		GL11.glPopMatrix();
	}
}
