package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;

public class GUIInventory extends GUIElement {
	public GUIInventory(GUI g) {
		super(g);
		tex = TDR.getTexture("inventory", "img/inventory.png");
	}
	
	public void draw() {
		GL11.glPushMatrix();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		GL11.glTranslatef(TDR.width / 2 - tex.getWidth() * 4 / 2 - 60, 32.0f, 0.0f);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex3f(0.0f, 244.0f, 0.0f);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex3f(496.0f, 244.0f, 0.0f);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex3f(496.0f, 0.0f, 0.0f);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(0.0f, 0.0f, 0.0f);
		GL11.glEnd();
		GL11.glPopMatrix();
	}
}
