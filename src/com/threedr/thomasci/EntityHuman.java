package com.threedr.thomasci;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class EntityHuman extends EntityEnemy {
	public EntityHuman(float x, float z, float rot, Game g) {
		super(x, z, rot, g);
		inventory.setItem(Item.newItem(0, "Def"), 0);
		inventory.setItem(Item.newItem(1, "Def"), 1);
		inventory.setItem(Item.newItem(3, "Def"), 3);
		if (Math.random() <= 0.25f) {
			inventory.setItem(Item.newItem(2, "Def"), 2);
		}
		if (Math.random() <= 0.25f) {
			inventory.setItem(Item.newItem(5, "Def"), 4);
		}
		if (Math.random() <= 0.25f) {
			if (Math.random() <= 0.25f) {
				inventory.setItem(Item.newItem(8, "Def"), 5);
			} else {
				inventory.setItem(Item.newItem(7, "Def"), 5);
			}
		}
	}
	
	public int damage(int num, int type) {
		ALLoader.addSound(17, (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] {pos.x, pos.y, pos.z}).rewind());
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
		return amount;
	}
}
