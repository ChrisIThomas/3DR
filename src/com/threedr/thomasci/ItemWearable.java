package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class ItemWearable extends Item {
	private Vector3f col;
	private final int mloc, slot;
	private Texture texm;
	
	public ItemWearable(int id, int tl, int ml) {
		super(id, tl);
		mloc = ml;
		texm = TDR.getTexture("character", "img/character.png");
		switch (ml) {
			case 1:
				slot = 0;
				break;
			case 2:
				slot = 1;
				break;
			case 3:
				slot = 2;
				break;
			case 4:
				slot = 3;
				break;
			case 5:
				slot = 4;
				break;
			case 6:
				slot = 4;
				break;
			case 7:
				slot = 5;
				break;
			case 8:
				slot = 5;
				break;
			case 9:
				slot = 5;
				break;
			case 10:
				slot = 2;
				break;
			case 11:
				slot = 7;
				break;
			case 12:
				slot = 6;
				break;
			default:
				slot = -1;
		}
		col = new Vector3f(1.0f, 1.0f, 1.0f);
		if (id != 9) {
			if (TDR.funky) {
				col = new Vector3f((float) Math.random(), (float) Math.random(), (float) Math.random());
			} else {
				float rndm = (float) Math.random() * 0.8f + 0.1f;
				if (id == 5 || id == 7 || id == 8) {
					col = new Vector3f(rndm, rndm, rndm);
				} else {
					float rndCol = (float) Math.floor(Math.random() * 3);
					if (rndCol == 0) {
						col = new Vector3f(rndm, rndm, rndm);
					} else if (rndCol == 1) {
						if (id == 2) {
							float r = (float) Math.random() * 0.25f + 0.65f;
							col = new Vector3f(r + 0.1f, r, r - 0.2f);
						} else {
							float r = (float) Math.random() * 0.25f + 0.2f;
							col = new Vector3f((float) Math.random() * 0.55f + r, r, 0.0f);
						}
					} else {
						float r = (float) Math.random() * 0.25f + 0.2f;
						col = new Vector3f((float) Math.random() * 0.55f + r, r, 0.0f);
					}
				}
			}
		}
		if (mloc >= 11) col = new Vector3f(1.0f, 1.0f, 1.0f);
		maxCount = 1;
	}
	
	public void setColour(float r, float g, float b) {
		col = new Vector3f(r, g, b);
	}
	
	public int getSlot() {
		return slot;
	}
	
	public Vector3f getCol() {
		return col;
	}
	
	public void colour() {
		GL11.glColor3f(col.x, col.y, col.z);
	}
	
	public void draw(boolean gui, int val) {
		float pw = 1.0f / tex.getWidth();
		float ph = 1.0f / tex.getHeight();
		
		float w = pw * 12;
		float h = ph * 12;
		float ty = tloc / 16;
		float tx = (tloc - ty * 16) * w;
		ty *= h;
		
		colour();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(tx, ty + h);
			GL11.glVertex3f(0.0f, 48.0f, 0.0f);
			GL11.glTexCoord2f(tx + w, ty + h);
			GL11.glVertex3f(48.0f, 48.0f, 0.0f);
			GL11.glTexCoord2f(tx + w, ty);
			GL11.glVertex3f(48.0f, 0.0f, 0.0f);
			GL11.glTexCoord2f(tx, ty);
			GL11.glVertex3f(0.0f, 0.0f, 0.0f);
		GL11.glEnd();
	}
	
	public void drawModel(int ang, int anim) {
		if (mloc >= 11) return;
		float pw = 1.0f / texm.getWidth();
		float ph = 1.0f / texm.getHeight();
		
		float w = pw * 7;
		float h = ph * 12;
		
		float tx = ang * 4 * w + anim * w;
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texm.getTex());
		float yoff = h * mloc + h;
		GL11.glColor3f(col.x, col.y, col.z);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(tx + w, yoff + h);
			GL11.glVertex3f(0.4375f, 0.0f, 0.0f);
			GL11.glTexCoord2f(tx + w, yoff);
			GL11.glVertex3f(0.4375f, 1.5f, 0.0f);
			GL11.glTexCoord2f(tx, yoff);
			GL11.glVertex3f(-0.4375f, 1.5f, 0.0f);
			GL11.glTexCoord2f(tx, yoff + h);
			GL11.glVertex3f(-0.4375f, 0.0f, 0.0f);
		GL11.glEnd();
	}
}
