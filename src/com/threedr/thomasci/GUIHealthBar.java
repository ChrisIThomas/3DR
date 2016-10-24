package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;

public class GUIHealthBar extends GUIButton {
	public GUIHealthBar(float x, float y, String act, int id, GUI g) {
		super(x, y, act, id, g);
		tex = TDR.getTexture("healthbar", "img/health.png");
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
		EntityLiving e = ((EntityLiving) gui.getGame().getController().getEntity());
		int amount = e.getHealth();
		float healthper = 1.0f / e.getMaxHealth();
		float health = amount * healthper;
		
		float pw = 1.0f / tex.getWidth();
		float ph = 1.0f / tex.getHeight() * 10;
		
		float w = pw * 12;
		
		GL11.glPushMatrix();
		GL11.glTranslatef(pos.x, pos.y, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		//black border
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex3f(0.0f, 48.0f, 0.0f);
			GL11.glTexCoord2f(w, 1);
			GL11.glVertex3f(48.0f, 48.0f, 0.0f);
			GL11.glTexCoord2f(w, 0);
			GL11.glVertex3f(48.0f, 0.0f, 0.0f);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(0.0f, 0.0f, 0.0f);
		GL11.glEnd();
		//inner heart
		GL11.glTranslatef(4.0f, 44.0f - health * 40, 0.0f);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex3f(0.0f, health * 40, 0.0f);
			GL11.glTexCoord2f(w, 0);
			GL11.glVertex3f(40.0f, health * 40, 0.0f);
			GL11.glTexCoord2f(w, ph * health);
			GL11.glVertex3f(40.0f, 0.0f, 0.0f);
			GL11.glTexCoord2f(1, ph * health);
			GL11.glVertex3f(0.0f, 0.0f, 0.0f);
		GL11.glEnd();
		GL11.glPopMatrix();
	}
}
