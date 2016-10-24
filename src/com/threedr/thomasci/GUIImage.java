package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;

public class GUIImage extends GUIElement {
	private int x, y;
	private boolean centre;
	
	public GUIImage(GUI g, String name, String path, int x, int y, boolean c) {
		super(g);
		tex = TDR.getTexture(name, path);
		this.x = x;
		this.y = y;
		centre = c;
	}
	
	public void draw() {
		int w = tex.getWidth();
		int h = tex.getHeight();
		int x = this.x - (centre ? w / 2 : 0);
		int y = this.y - (centre ? h / 2 : 0);
		
		GL11.glPushMatrix();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		GL11.glTranslatef(x, y, 0.0f);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex3f(0.0f, w, 0.0f);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex3f(h, w, 0.0f);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex3f(h, 0.0f, 0.0f);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(0.0f, 0.0f, 0.0f);
		GL11.glEnd();
		GL11.glPopMatrix();
	}
}
