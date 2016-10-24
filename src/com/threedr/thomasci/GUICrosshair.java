package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;

public class GUICrosshair extends GUIElement {
	public GUICrosshair(GUI g) {
		super(g);
		tex = TDR.getTexture("crosshair", "img/crosshair.png");
	}
	
	public void draw() {
		GL11.glPushMatrix();
		GL11.glTranslatef(TDR.width / 2 - 5f, TDR.height / 2 - 5f, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex3f(0.0f, 10.0f, 0.0f);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex3f(10.0f, 10.0f, 0.0f);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex3f(10.0f, 0.0f, 0.0f);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(0.0f, 0.0f, 0.0f);
		GL11.glEnd();
		GL11.glPopMatrix();
	}
}
