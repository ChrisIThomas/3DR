package com.threedr.thomasci;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

//basically the same as the enemy except it is a large sub group of small vermin (spiders, rats, etc.)
public class EntityVermin extends EntityEnemy {
	private int type;
	
	public EntityVermin(int type, float x, float z, float rot, Game g) {
		super(x, z, rot, g);
		height = 0.5f;
		maxHealth = 2;
		health = 2;
		this.type = type;
		switch (type) {
			case 0: // rat
				tex = TDR.getTexture("rat", "img/rat.png");
				break;
			case 1: // spider
				tex = TDR.getTexture("spider", "img/spider.png");
				health = 1;
				maxSpeed = 0.1f;
				break;
			case 2: // crawler
				tex = TDR.getTexture("crawler", "img/crawler.png");
				maxHealth = 4;
				health = 4;
				maxSpeed = 0.075f;
				break;
		}
	}
	
	public void drawBox(float lx, float lz) {
		if (health > 0) {
			float r = (float) Math.toDegrees(Math.atan2(pos.x - lx, pos.z - lz));
			
			GL11.glPushMatrix();
			GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
			GL11.glRotatef(r, 0, 1, 0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex3f(0.4375f, 0.0f, 0.0f);
				GL11.glVertex3f(0.4375f, 1.0f, 0.0f);
				GL11.glVertex3f(-0.4375f, 1.0f, 0.0f);
				GL11.glVertex3f(-0.4375f, 0.0f, 0.0f);
			GL11.glEnd();
			GL11.glPopMatrix();
		}
	}
	
	public int damage(int num, int type) {
		int oldHealth = health;
		int amount = super.damage(num, type);
		if (health > 0) {
			if (type == 0) {
				float r = (float) Math.toDegrees(Math.atan2(pos.x - target.getX(), pos.z - target.getZ()));
				r += 180;
				int ang = (int) (r - rot.y + 225);
				if (ang < 0) ang += 360;
				else if (ang > 360) ang -= 360;
				ang = ang / 90;
				if (ang == 2) {
					amount += super.damage(num, type);
				}
			}
			
			if (canSee(target.getX(), target.getZ(), false)) { //if we can see the player then face them and head towards them
				found = true;
				rot.y = (float) Math.toDegrees(Math.atan2(pos.x - target.getX(), pos.z - target.getZ()));
				vel.z += 0.01f;
				attack = 60;
			}
		}
		if (health <= 0 && oldHealth > 0) {
			game.addEnt(new EntityFloorDecoration(pos.x, pos.z, game, rot.y, 6));
		}
		if (this.type == 0) {
			ALLoader.addSound(12, (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] {pos.x, pos.y, pos.z}).rewind());
		} else if (this.type == 2) {
			ALLoader.addSound(13, (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] {pos.x, pos.y, pos.z}).rewind());
		}
		return amount;
	}
}
