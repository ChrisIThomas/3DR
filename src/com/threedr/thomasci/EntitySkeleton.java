package com.threedr.thomasci;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class EntitySkeleton extends EntityEnemy {
	public EntitySkeleton(float x, float z, float rot, Game g) {
		super(x, z, rot, g);
		tex = TDR.getTexture("skeleton", "img/skeleton.png");
	}
	
	public int damage(int num, int type) {
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
		ALLoader.addSound(14, (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] {pos.x, pos.y, pos.z}).rewind());
		return amount;
	}
}
