package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;

public class ItemWeapon extends Item {
	private Material mat;
	
	public ItemWeapon(int id, int tl) {
		super(id, tl);
		mat = Material.bronze;
		maxCount = 1;
	}
	
	public void setMaterial(Material m) {
		mat = m;
	}
	
	public String getName() {
		if (mat != null) {
			return mat.getName() + " " + name;
		}
		return name;
	}
	
	public int getSpeed() {
		if (mat != null) {
			int amount = mat.getSpeed() + super.getSpeed();
			if (amount < -3) amount = -3;
			return amount;
		}
		return super.getSpeed();
	}
	
	public int getDamage() {
		if (mat != null) {
			int amount = mat.getDamage() + super.getDamage();
			if (amount < 0) amount = 0;
			return amount;
		}
		return super.getDamage();
	}
	
	public void colour() {
		if (mat != null) mat.colour();
		else GL11.glColor3f(1.0f, 1.0f, 1.0f);
	}
	
	public void colourHand() {
		if (mat != null) mat.colourHand();
		else GL11.glColor3f(1.0f, 1.0f, 1.0f);
	}
	
	public void draw(boolean gui, int val) {
		if (gui) {
			if (id == 26) {
				GL11.glTranslatef(-4.0f, 12.0f, 0.0f);
				GL11.glRotatef(-30, 0, 0, 1);
			}
		}
		float pw = 1.0f / tex.getWidth();
		float ph = 1.0f / tex.getHeight();
		
		float w = pw * 12;
		float h = ph * 12;
		float ty = tloc / 16;
		float tx = (tloc - ty * 16) * w;
		ty = ty * h + h;
		
		colour();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(tx, ty + h);
			GL11.glVertex2f(0.0f, 48.0f);
			GL11.glTexCoord2f(tx + w, ty + h);
			GL11.glVertex2f(48.0f, 48.0f);
			GL11.glTexCoord2f(tx + w, ty);
			GL11.glVertex2f(48.0f, 0.0f);
			GL11.glTexCoord2f(tx, ty);
			GL11.glVertex2f(0.0f, 0.0f);
		GL11.glEnd();
		
		ty -= h;
		colourHand();
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(tx, ty + h);
			GL11.glVertex2f(0.0f, 48.0f);
			GL11.glTexCoord2f(tx + w, ty + h);
			GL11.glVertex2f(48.0f, 48.0f);
			GL11.glTexCoord2f(tx + w, ty);
			GL11.glVertex2f(48.0f, 0.0f);
			GL11.glTexCoord2f(tx, ty);
			GL11.glVertex2f(0.0f, 0.0f);
		GL11.glEnd();
	}
	
	public String write() {
		return id + ";" + idName + ";" + mat.write() + ";" + count + ";" + enchantments.write();
	}
}
