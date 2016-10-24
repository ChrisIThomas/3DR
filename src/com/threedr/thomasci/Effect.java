package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;

public class Effect {
	private final int type;
	private int wait, times, val;
	private float anim;
	private boolean finished;
	private final Texture tex;
	
	//0: Fire (effective against un-armored targets)
	//1: Cold (no damage, slows, long lasting)
	//2: Arcane (ignores armor, large damage, instant effect)
	//3: Acid (ignores armor, little damage, long lasting)
	//4: Poison (ignores armor, chance to get over effect, long lasting)
	//5: Speed (increases the object's speed)
	//6: Defense (increases the object's defense)
	//7: Attack Speed (increases the object's attack speed)
	
	public Effect(int type, int val) {
		this.type = type;
		if (type < 0) type = 0;
		else if (type > 4) type = 4;
		if (type == 1) {
			times = 15;
		} else if (type == 2) {
			times = 1;
		} else if (type == 3 || type == 4) {
			times = 10;
		} else {
			times = 5;
		}
		wait = 0;
		this.val = val;
		tex = TDR.getTexture("effect", "img/effect.png");
	}
	
	public void tick(Entity ent) {
		if (wait <= 0) {
			if (ent instanceof EntityLiving) {
				/*if (type == 2) damage = 6;
				else if (type == 3) damage = 1;*/
				wait = 60;
				if (type <= 4) {
					((EntityLiving) ent).damage(val, type + 1);
					if (type == 4) {
						if (Math.random() <= 0.1) finished = true;
					}
				}
				times--;
				if (times <= 0) {
					finished = true;
					if (type == 1) ((EntityLiving) ent).setMaxSpeed(0.1f);
				}
			}
		} else {
			wait--;
		}
		anim += 0.15f;
		if (anim >= 8) anim = 0;
	}
	
	public int getType() {
		return type;
	}
	
	public int getVal() {
		return val;
	}
	
	public void finish() {
		finished = true;
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	public void draw() {
		float pw = 1.0f / tex.getWidth();
		float ph = 1.0f / tex.getHeight();
		
		float w = pw * 7;
		float h = ph * 8;
		
		float ty = type * h;
		float tx = (int) anim * w;
		
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glTranslatef(0.0f, 0.0f, 0.001f);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(tx + w, ty + h);
			GL11.glVertex3f(0.4375f, 0.0f, 0.0f);
			GL11.glTexCoord2f(tx + w, ty);
			GL11.glVertex3f(0.4375f, 1.0f, 0.0f);
			GL11.glTexCoord2f(tx, ty);
			GL11.glVertex3f(-0.4375f, 1.0f, 0.0f);
			GL11.glTexCoord2f(tx, ty + h);
			GL11.glVertex3f(-0.4375f, 0.0f, 0.0f);
		GL11.glEnd();
	}
}
