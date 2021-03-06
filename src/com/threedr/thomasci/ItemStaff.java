package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class ItemStaff extends Item {
	//the wand will only charge when you are holding it
	private int charge, spin, type, val;
	
	public ItemStaff(int id, int tl, int type, int val) {
		super(id, tl);
		this.type = type;
		this.val = val;
		name = "Dark " + name;
		col = new Vector3f(0.15f, 0.15f, 0.15f);
		switch (type) {
			case 1: //fire
				name = "Fire " + name;
				col = new Vector3f(0.85f, 0.35f, 0.0f);
				break;
			case 2: //cold
				name = "Cold " + name;
				col = new Vector3f(0.55f, 0.55f, 0.65f);
				break;
			case 3: //magic
				name = "Arcane " + name;
				col = new Vector3f(0.35f, 0.0f, 0.25f);
				break;
			case 4: //acid
				name = "Acid " + name;
				col = new Vector3f(0.0f, 0.45f, 0.0f);
				break;
		}
		spin = 45;
	}
	
	protected Item setName(String n) {
		name = n;
		return this;
	}
	
	public ItemStaff setType(int val) {
		type = val;
		name = "Dark Staff";
		col = new Vector3f(0.15f, 0.15f, 0.15f);
		switch (type) {
			case 1: //fire
				name = "Fire Staff";
				col = new Vector3f(0.85f, 0.35f, 0.0f);
				break;
			case 2: //cold
				name = "Cold Staff";
				col = new Vector3f(0.55f, 0.55f, 0.65f);
				break;
			case 3: //magic
				name = "Arcane Staff";
				col = new Vector3f(0.35f, 0.0f, 0.25f);
				break;
			case 4: //acid
				name = "Acid Staff";
				col = new Vector3f(0.0f, 0.45f, 0.0f);
				break;
		}
		return this;
	}
	
	public String getName() {
		switch (type) {
			case 1:
				return "Fire " + name;
			case 2:
				return "Cold " + name;
			case 3:
				return "Arcane " + name;
			case 4:
				return "Acid " + name;
		}
		return "Dark " + name;
	}
	
	public void setVal(int amount) {
		val = amount;
	}
	
	public int getCharge() {
		return charge;
	}
	
	public int getType() {
		return type;
	}
	
	public int getVal() {
		return val;
	}
	
	public void resetCharge() {
		charge = 0;
		spin = 45;
	}
	
	public void draw(boolean gui, int val) {
		if (gui) {
			GL11.glTranslatef(8.0f, 12.0f, 0.0f);
			GL11.glRotatef(30, 0, 0, 1);
		}
		
		float pw = 1.0f / tex.getWidth();
		float ph = 1.0f / tex.getHeight();
		
		float w = pw * 12;
		float h = ph * 12;
		float ty = tloc / 16;
		float tx = (tloc - ty * 16) * w;
		ty *= h;
		
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
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

		if (gui) {
			tx = 0;
			ty = 2 * h;
			w = pw * 3;
			h = ph * 3;
			
			float size = 1.25f;
			if (spin / 90 % 2 == 0) size -= (spin / 90.0f - spin / 90) * 0.5f;
			else size -= 0.5f - (spin / 90.0f - spin / 90) * 0.5f;
			GL11.glTranslatef(8, 8, 0);
			GL11.glScalef(size, size, 1.0f);
			if (charge >= 240) {
				spin++;
				if (spin > 360) spin -= 360;
				GL11.glRotatef(spin, 0.0f, 0.0f, 1.0f);
			} else {
				spin = 45;
			}
			GL11.glColor4f(col.x, col.y, col.z, charge / 480.0f);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(tx, ty + h);
				GL11.glVertex2f(-6.0f, 6.0f);
				GL11.glTexCoord2f(tx + w, ty + h);
				GL11.glVertex2f(6.0f, 6.0f);
				GL11.glTexCoord2f(tx + w, ty);
				GL11.glVertex2f(6.0f, -6.0f);
				GL11.glTexCoord2f(tx, ty);
				GL11.glVertex2f(-6.0f, -6.0f);
			GL11.glEnd();
		}
	}
	
	public void charge() {
		if (charge < 240) charge++;
	}
	
	public String write() {
		return id + ";" + idName + ";" + col.x + ";" + col.y + ";" + col.z + ";" + count + ";" + type + ";" + val + ";" + enchantments.write();
	}
}
