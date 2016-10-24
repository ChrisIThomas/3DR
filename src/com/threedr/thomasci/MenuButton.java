package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class MenuButton {
	private String text;
	private Vector2f pos;
	private Texture tex;
	private boolean over, active;
	
	public MenuButton(float x, float y, String txt) {
		text = txt;
		pos = new Vector2f(x, y);
		tex = TDR.getTexture("menuButton", "img/menuButton.png");
		active = true;
	}
	
	public void setActive(boolean val) {
		active = val;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public boolean isOver(int x, int y) {
		if (x >= pos.x - 128 && x <= pos.x + 128) {
			if (y >= pos.y - 32 && y <= pos.y + 32) {
				return true;
			}
		}
		return false;
	}
	
	public void setOver(boolean val) {
		over = val;
	}
	
	public boolean isOver() {
		return over;
	}
	
	public void setText(String txt) {
		text = txt;
	}
	
	public void draw() {
		GL11.glPushMatrix();
		
		float h = over?0.5f:0;
		
		GL11.glTranslatef(pos.x, pos.y, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, h + 0.5f);
			GL11.glVertex3f(-128, 32, 0.0f);
			GL11.glTexCoord2f(1, h + 0.5f);
			GL11.glVertex3f(128, 32, 0.0f);
			GL11.glTexCoord2f(1, h);
			GL11.glVertex3f(128, -32, 0.0f);
			GL11.glTexCoord2f(0, h);
			GL11.glVertex3f(-128, -32, 0.0f);
		GL11.glEnd();
		if (active) Text.drawText("§0" + text, 2, true);
		else Text.drawText("§2" + text, 2, true);
		
		GL11.glPopMatrix();
	}
}
