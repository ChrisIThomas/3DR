package com.threedr.thomasci;

import java.nio.FloatBuffer;
import java.util.Arrays;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class EntityGhost extends EntityEnemy {
	public EntityGhost(float x, float z, float rot, Game g) {
		super(x, z, rot, g);
		tex = TDR.getTexture("ghost", "img/ghost.png");
		bloodCol = new Vector3f(0.9f, 0.9f, 0.9f);
		dam += 2;
		maxHealth = 15;
		maxHealth += g.getLevelNum() / 3;
		health = maxHealth;
	}
	
	public void tick() {
		if (-pos.y > game.getHeight((int) -pos.x / 2, (int) ((-pos.z) / 2))) {
			vel.y += 0.025f;
		}
		if (-pos.y == game.getHeight((int) -pos.x / 2, (int) -pos.z / 2)) {
			vel.x *= 0.85f;
			vel.z *= 0.85f;
		}
		if (vel.x <= 0.0001f && vel.x >= -0.0001f) vel.x = 0;
		if (vel.z <= 0.0001f && vel.z >= -0.0001f) vel.z = 0;
		if (point.length != 0) {
			rot.y = (float) Math.toDegrees(Math.atan2(pos.x + point[point.length - 1].x * 2 + 1, pos.z + point[point.length - 1].y * 2 + 1));
		}
		
		//follow the player and track them down
		if (found) {
			
			/*if (canSee((int) -target.getX(), (int) -target.getZ())) {
				r = (float) Math.toDegrees(Math.atan2(pos.x + target.getX(), target.getZ() + pos.z));
				vel.z = -0.1f;
				if (point.length > 0) {
					point = new Vector2f[0];
				}
			} else if (point.length == 1) {
				found = false;
				vel.z = 0.0f;
			} else if (point.length == 0) {
				pathTo((int) (-target.getX() / 2), (int) (-target.getZ() / 2));
			} else {
				r = (float) Math.toDegrees(Math.atan2(pos.x + point[point.length - 1].x * 2 + 1, pos.z + point[point.length - 1].y * 2 + 1));
				vel.z = -0.1f;
				if (Math.round((-pos.x - 1) / 2) == point[point.length - 1].x && Math.round((-pos.z - 1) / 2) == point[point.length - 1].y) {
					point = Arrays.copyOfRange(point, 0, point.length - 1);
				}
			}*/
			if (canSee((int) target.getX(), (int) target.getZ(), false)) {
				//System.out.println(true);
				rot.y = (float) Math.toDegrees(Math.atan2(pos.x - target.getX(), pos.z - target.getZ()));
				vel.z+= 0.01f;
				if (point.length > 0) {
					point = new Vector2f[0];
				}
				if (attack <= 0) {
					if (Math.sqrt(Math.pow(pos.x - target.getX(), 2) + Math.pow(pos.z - target.getZ(), 2)) <= 1) {
						attack = 60;
						if (target instanceof EntityLiving) {
							((EntityLiving) target).damage(1, 0);
						}
						vel.z = 0.0f;
					}
				}
			} else if (point.length == 0) {
				//System.out.println(false);
				pathTo((int) (-target.getX() / 2), (int) (-target.getZ() / 2));
			}
		} else {
			vel.z = 0.0f;
		}
		
		/*if (Math.sqrt(Math.pow(pos.x - target.getX(), 2) + Math.pow(pos.z - target.getZ(), 2)) < 1) {
			vel.z = 0.0f;
			System.out.println(true);
		}*/
		
		//only begin looking if the entity is alive
		base: {
			if (getHealth() > 0) {
				if (target instanceof EntityLiving) {
					if (((EntityLiving) target).getHealth() < 0) {
						found = false;
						break base;
					}
				}
				//enable AI tracking
				if (!found) {
					if (canSee(target.getX(), target.getZ(), true)) { //if we can see the player then face them and head towards them
						found = true;
						rot.y = (float) Math.toDegrees(Math.atan2(pos.x - target.getX(), pos.z - target.getZ()));
						vel.z += 0.01f;
					}
				}
				
				if (vel.z > maxSpeed) vel.z = maxSpeed;
				if (rot.y < 0) rot.y += 360;
				if (rot.y >= 360) rot.y -= 360;
				
				//left and right movement
				pos.x += vel.x * Math.sin(Math.toRadians(rot.y + 90));
				pos.z -= vel.x * Math.cos(Math.toRadians(rot.y + 90));
				
				//forward and backward movement
				pos.x -= vel.z * Math.sin(Math.toRadians(rot.y));
				pos.z -= vel.z * Math.cos(Math.toRadians(rot.y));
				
				if (rot.z < -90.0f) rot.z = -90;
				if (rot.z > 90.0f) rot.z = 90;
				if (pos.x > 0) {
					pos.x = 0;
				}
				if (pos.z > 0) {
					pos.z = 0;
				}
				if (pos.x < -game.getWidth() * 2) {
					pos.x = -game.getWidth() * 2;
				}
				if (pos.z < -game.getDepth() * 2) {
					pos.z = -game.getDepth() * 2;
				}
				if (point.length != 0) {
					vel.z = 0.05f;
					if (Math.round((-pos.x - 1) / 2) == point[point.length - 1].x && Math.round((-pos.z - 1) / 2) == point[point.length - 1].y) {
						point = Arrays.copyOfRange(point, 0, point.length - 1);
						if (point.length == 0) {
							if (!canSee(target.getX(), target.getZ(), false)) {
								found = false;
							}
						}
					}
				}
			} else {
				found = false;
				if (effects.size() > 0) effects.clear();
			}
		}
		pos.y += vel.y;
		forces();
		
		Vector3f vec = game.collides(this, rad);
		if (vec != null) {
			if (pos.x != vec.x || pos.z != vec.z) {
				pos.x = vec.x;
				pos.z = vec.z;
				if (getHealth() <= 0) {
					if (Math.floor(Math.random() * 50) <= blood + 1) {
						if (blood > 0) blood--;
						game.addEnt(new EntityFloorDecoration(pos.x, pos.z, game, (float) Math.floor(Math.random() * 360), 7 + (int) (Math.round(Math.random() * 2))));
					}
				}
			}
		}
		
		if (effects.size() > 0) {
			for (int i = 0; i < effects.size(); i++) {
				effects.get(i).tick(this);
				if (effects.get(i).isFinished()) {
					effects.remove(i);
					i--;
				}
			}
		}
		
		anim += 0.125f;
		if (anim >= 4) anim = 0;
		if (getHealth() <= 0) anim = 0.0f;
		if (attack > 0) attack--;
	}
	
	public int damage(int num, int type) {
		ALLoader.addSound(15, (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] {pos.x, pos.y, pos.z}).rewind());
		int amount = super.damage(num, type);
		if (health > 0) {
			if (canSee(target.getX(), target.getZ(), false)) { //if we can see the player then face them and head towards them
				found = true;
				rot.y = (float) Math.toDegrees(Math.atan2(pos.x - target.getX(), pos.z - target.getZ()));
				vel.z += 0.01f;
				attack = 60;
			}
		}
		return amount;
	}
}
