package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class GUIButton extends GUIElement {
	protected Vector2f pos;
	private String action;
	private int loc;
	
	public GUIButton(float x, float y, String act, int id, GUI g) {
		super(g);
		tex = TDR.getTexture("icons", "img/icons.png");
		pos = new Vector2f(x, y);
		action = act;
		loc = id;
	}
	
	public boolean over(int x, int y) {
		if (x >= pos.x && x < pos.x + 48) {
			if (y >= pos.y && y <= pos.y + 48) {
				return true;
			}
		}
		return false;
	}
	
	public String getAction() {
		return action;
	}
	
	public void draw() {
		GL11.glPushMatrix();
		float pw = 1.0f / tex.getWidth();
		float ph = 1.0f / tex.getHeight();
		
		float w = pw * 14;
		float h = ph * 14;
		float ty = loc / 2;
		float tx = (loc - ty * 2) * w;
		ty *= h;
		
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glTranslatef(pos.x, pos.y, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(tx, ty + h);
			GL11.glVertex2f(0.0f, 56.0f);
			GL11.glTexCoord2f(tx + w, ty + h);
			GL11.glVertex2f(56.0f, 56.0f);
			GL11.glTexCoord2f(tx + w, ty);
			GL11.glVertex2f(56.0f, 0.0f);
			GL11.glTexCoord2f(tx, ty);
			GL11.glVertex2f(0.0f, 0.0f);
		GL11.glEnd();
		GL11.glPopMatrix();
	}
}
