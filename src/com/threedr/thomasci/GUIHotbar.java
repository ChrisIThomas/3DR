package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;

public class GUIHotbar extends GUIElement {
	private int sloc;
	public GUIHotbar(GUI g) {
		super(g);
		tex = TDR.getTexture("hotbar", "img/hotbar.png");
		sloc = 0;
	}
	
	public void update() {
		sloc = gui.getGame().getController().getSlot();
	}
	
	public void draw() {
		float pw = 1.0f / tex.getWidth();
		
		float w = pw * 65;
		
		GL11.glPushMatrix();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		GL11.glTranslatef(TDR.width / 2 - tex.getWidth() * 4 / 2, 0.0f, 0.0f);
		//main gui part
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex3f(0.0f, 64.0f, 0.0f);
			GL11.glTexCoord2f(w, 1);
			GL11.glVertex3f(260.0f, 64.0f, 0.0f);
			GL11.glTexCoord2f(w, 0);
			GL11.glVertex3f(260.0f, 0.0f, 0.0f);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(0.0f, 0.0f, 0.0f);
		GL11.glEnd();
		//selector
		GL11.glTranslatef(sloc * 60.0f + 32.0f, 0.0f, 0.0f);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(w, 1);
			GL11.glVertex3f(0.0f, 64.0f, 0.0f);
			GL11.glTexCoord2f(w + pw * 4, 1);
			GL11.glVertex3f(16.0f, 64.0f, 0.0f);
			GL11.glTexCoord2f(w + pw * 4, 0);
			GL11.glVertex3f(16.0f, 0.0f, 0.0f);
			GL11.glTexCoord2f(w, 0);
			GL11.glVertex3f(0.0f, 0.0f, 0.0f);
		GL11.glEnd();
		GL11.glPopMatrix();
	}
}
