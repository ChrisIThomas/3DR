package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;

public class ItemPotion extends Item {
	private int type, val;
	
	public ItemPotion(int id, int tl, int type, int val) {
		super(id, tl);
		this.type = type;
		this.val = val;
		maxCount = 0;
	}
	
	// 0: empty vial
	// 1: water vial
	// 2: healing potion
	// 3: speed potion
	// 4: defense potion
	// 5: fire resistance potion !
	// 6: ice resistance potion !
	// 7: magic resistance potion !
	// 8: acid resistance potion !
	// 9: poison resistance potion !
	//10: attack speed potion
	//11: night vision potion !
	//12: x-ray potion !
	//13: damaging vial
	//14: fire vial
	//15: cold vial
	//16: magic vial
	//17: acid vial
	//18: poison vial
	
	public void colour() {
		switch (type) {
			case 1: //water
				GL11.glColor3f(0.15f, 0.25f, 0.35f);
				break;
			case 2: //healing
				GL11.glColor3f(0.65f, 0.05f, 0.05f);
				break;
			case 3: //speed
				GL11.glColor3f(0.85f, 0.85f, 0.95f);
				break;
			case 4: //defense
				GL11.glColor3f(0.15f, 0.15f, 0.2f);
				break;
			case 5: //fire resistance
				GL11.glColor3f(0.65f, 0.15f, 0.0f);
				break;
			case 6: //cold resistance
				GL11.glColor3f(0.35f, 0.35f, 0.45f);
				break;
			case 7: //magic resistance
				GL11.glColor3f(0.2f, 0.0f, 0.1f);
				break;
			case 8: //acid resistance
				GL11.glColor3f(0.0f, 0.25f, 0.0f);
				break;
			case 9: //poison resistance
				GL11.glColor3f(0.45f, 0.55f, 0.45f);
				break;
			case 10: //attack speed
				GL11.glColor3f(0.75f, 0.95f, 0.95f);
				break;
			case 11: //night vision
				GL11.glColor3f(0.75f, 0.75f, 0.45f);
				break;
			case 12: //x-ray vision
				GL11.glColor3f(0.85f, 0.95f, 0.05f);
				break;
			case 13: //damage
				GL11.glColor3f(0.15f, 0.15f, 0.15f);
				break;
			case 14: //fire
				GL11.glColor3f(0.85f, 0.35f, 0.0f);
				break;
			case 15: //cold
				GL11.glColor3f(0.55f, 0.55f, 0.65f);
				break;
			case 16: //magic
				GL11.glColor3f(0.35f, 0.0f, 0.25f);
				break;
			case 17: //acid
				GL11.glColor3f(0.0f, 0.45f, 0.0f);
				break;
			case 18: //poison
				GL11.glColor3f(0.65f, 0.75f, 0.65f);
				break;
			default:
				GL11.glColor3f(1.0f, 1.0f, 1.0f);
		}
	}
	
	public void affect(Entity e) {
		if (e instanceof EntityLiving) {
			EntityLiving ent = (EntityLiving) e;
			switch (type) {
				case 2: //healing
					ent.heal(val);
					break;
				case 3: //speed
					ent.addEffect(new Effect(5, 5));
					break;
				case 4: //defense
					ent.addEffect(new Effect(6, 2));
					break;
				case 10: //attack speed
					ent.addEffect(new Effect(7, 2));
					break;
				case 13: //damage
					ent.damage(val, 0);
					break;
				case 14: //fire
					ent.addEffect(new Effect(0, 2));
					break;
				case 15: //cold
					ent.addEffect(new Effect(1, 2));
					break;
				case 16: //magic
					ent.addEffect(new Effect(2, 4));
					break;
				case 17: //acid
					ent.addEffect(new Effect(3, 1));
					break;
				case 18: //poison
					ent.addEffect(new Effect(4, 1));
					break;
			}
		}
		reset();
	}
	
	public void reset() {
		name = "Empty Vial";
		type = 0;
		val = 0;
	}
	
	public void setType(int num) {
		type = num;
		switch (type) {
			case 0:
				name = "Empty Vial";
				break;
			case 1:
				name = "Vial of Water";
				break;
			case 2:
				name = "Healing Potion";
				break;
			case 3:
				name = "Speed Potion";
				break;
			case 4:
				name = "Defense Potion";
				break;
			case 5:
				name = "Fire Protection Potion";
				break;
			case 6:
				name = "Ice Protection Potion";
				break;
			case 7:
				name = "Magic Protection Potion";
				break;
			case 8:
				name = "Acid Protection Potion";
				break;
			case 9:
				name = "Poison Protection Potion";
				break;
			case 10:
				name = "Attack Potion";
				break;
			case 11:
				name = "Night Vision Potion";
				break;
			case 12:
				name = "X-Ray Potion";
				break;
			case 13:
				name = "Dark Vial";
				break;
			case 14:
				name = "Fire Vial";
				break;
			case 15:
				name = "Cold Vial";
				break;
			case 16:
				name = "Magic Vial";
				break;
			case 17:
				name = "Acid Vial";
				break;
			case 18:
				name = "Poison Vial";
				break;
		}
	}
	
	public void setVal(int amount) {
		val = amount;
	}
	
	public int getType() {
		return type;
	}
	
	public int getVal() {
		return val;
	}
	
	public void draw(boolean gui, int val) {
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
		if (type != 0) {
			tx += pw * 12;
			colour();
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
	}
	public String write() {
		return id + ";" + idName + ";" + col.x + ";" + col.y + ";" + col.z + ";" + count + ";" + type + ";" + val + ";" + enchantments.write();
	}
}
