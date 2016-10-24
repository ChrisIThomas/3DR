package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;

public class GUIAlchemy extends GUIElement {
	public GUIAlchemy(GUI g) {
		super(g);
		tex = TDR.getTexture("alchemy", "img/alchemy.png");
	}
	
	public void draw() {
		//576 224
		GL11.glPushMatrix();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		GL11.glTranslatef(TDR.width / 2 + 200, 84.0f, 0.0f);
		//main gui part
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex3f(0.0f, 200.0f, 0.0f);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex3f(184.0f, 200.0f, 0.0f);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex3f(184.0f, 0.0f, 0.0f);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(0.0f, 0.0f, 0.0f);
		GL11.glEnd();
		GL11.glPopMatrix();
	}
}
