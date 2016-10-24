package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class EntityProjectile extends Entity {
	private int type, damage;
	private Vector3f col;
	private final boolean half;
	
	public EntityProjectile(float x, float y, float z, float r, float rz, float speed, int type, boolean half, Game g) {
		super(x, z, g);
		if (type == 0) {
			tex = TDR.getTexture("arrow", "img/arrow.png");
		} else {
			tex = TDR.getTexture("magic", "img/magic.png");
		}
		this.pos.y = y;
		this.pos.y += 0.75f * Math.sin(Math.toRadians(rz));
		rot.y = r;
		rot.z = rz;
		this.vel.z = speed;
		this.type = type;
		switch (type) {
			case 0: //arrow
				damage = 5;
				break;
			case 1: //damage magic
				damage = 4;
				break;
			case 2: //fire
				damage = 2;
				break;
			case 3: //cold
				damage = 0;
				break;
			case 4: //arcane
				damage = 8;
				break;
			case 5: //acid
				damage = 1;
				break;
		}
		this.half = half;
		col = new Vector3f(1.0f, 1.0f, 1.0f);
	}
	
	public EntityProjectile setCol(Vector3f c) {
		col = c;
		return this;
	}
	
	public void forces() {
		if (game.getHeight((int) ((-pos.x - 0.25f) / 2), (int) -pos.z / 2) > -pos.y) {
			pos.x = Math.round(pos.x) - 0.25f;
			destroy(type == 0);
		} else if (game.getHeight((int) ((-pos.x + 0.25f) / 2), (int) -pos.z / 2) > -pos.y) {
			pos.x = Math.round(pos.x) + 0.25f;
			destroy(type == 0);
		}
		
		if (game.getHeight((int) -pos.x / 2, (int) ((-pos.z - 0.25f) / 2)) > -pos.y) {
			pos.z = Math.round(pos.z) - 0.25f;
			destroy(type == 0);
		} else if (game.getHeight((int) -pos.x / 2, (int) ((-pos.z + 0.25f) / 2)) > -pos.y) {
			pos.z = Math.round(pos.z) + 0.25f;
			destroy(type == 0);
		}
		
		if (game.getRoof((int) ((-pos.x) / 2), (int) -pos.z / 2) < -pos.y) {
			pos.y = game.getRoof((int) -pos.x / 2, (int) ((-pos.z) / 2));
			vel.y = 0;
			destroy(type == 0);
		}
		
		if (vel.y > 2.0f) vel.y = 2.0f;
		if (vel.y < -2.0f) vel.y = -2.0f;
		
		if (-pos.y <= game.getHeight((int) -pos.x / 2, (int) -pos.z / 2)) {
			pos.y = -game.getHeight((int) -pos.x / 2, (int) -pos.z / 2);
			destroy(type == 0);
			vel.y = 0;
		}
	}
	
	public void tick() {
		if (rot.y < 0) rot.y += 360;
		if (rot.y >= 360) rot.y -= 360;
		if (-pos.y > game.getHeight((int) -pos.x / 2, (int) ((-pos.z) / 2))) {
			if (type == 0) vel.y += 0.1f - vel.z / 10;
		}
		rot.z += vel.y;
		
		if (rot.z < -90.0f) rot.z = -90;
		if (rot.z > 90.0f) rot.z = 90;
		if (pos.x > 0) {
			pos.x = 0;
			destroy(false);
		}
		if (pos.z > 0) {
			pos.z = 0;
			destroy(false);
		}
		if (pos.x < -game.getWidth() * 2) {
			pos.x = -game.getWidth() * 2;
			destroy(false);
		}
		if (pos.z < -game.getDepth() * 2) {
			pos.z = -game.getDepth() * 2;
			destroy(false);
		}
		
		//collisions
		float dist = rad * 2;
		float total = vel.z;
		while (total > 0.0f) {
			total -= dist;
			if (total < 0.0f) dist += total;
			//forward and backward movement
			pos.x -= dist * Math.sin(Math.toRadians(rot.y)) * Math.cos(Math.toRadians(rot.z));
			pos.z += dist * Math.cos(Math.toRadians(rot.y)) * Math.cos(Math.toRadians(rot.z));
			
			pos.y += dist * Math.sin(Math.toRadians(rot.z));
			
			Vector3f vec = game.collides(this, rad);
			if (vec != null) {
				destroy(false);
				break;
			}
		}
		if (type != 0) {
			game.addParticle(new Particle(new Vector3f(pos.x, pos.y, pos.z),
					new Vector3f((float) Math.random() * 0.05f - 0.025f, (float) Math.random() * 0.05f - 0.025f, (float) Math.random() * 0.05f - 0.025f),
					col, false, game));
		}
		
		forces();
	}
	
	public int getType() {
		return type - 1 < 0 ? 0 : type - 1;
	}
	
	public void draw(float lx, float lz) {
		GL11.glPushMatrix();
		rot.x += vel.z * 1000;
		//don't render this object if it is outside of the player's view
		//if (Math.abs(pos.x - lx) > 15) return;
		//if (Math.abs(pos.z - lz) > 15) return;
		
		if (type == 0) {
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
			GL11.glRotatef(-rot.y, 0.0f, 1.0f, 0.0f);
			GL11.glRotatef(-rot.z, 1.0f, 0.0f, 0.0f);
			GL11.glRotatef(-rot.x, 0.0f, 0.0f, 1.0f);
			GL11.glRotatef(45, 0.0f, 0.0f, 1.0f);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex3f(0.0f, -0.0375f, 0.5f);
				GL11.glTexCoord2f(1, 0);
				GL11.glVertex3f(0.0f, 0.0375f, 0.5f);
				GL11.glTexCoord2f(0.25f, 0);
				GL11.glVertex3f(0.0f, 0.0375f, -0.25f);
				GL11.glTexCoord2f(0.25f, 1);
				GL11.glVertex3f(0.0f, -0.0375f, -0.25f);
			GL11.glEnd();
			
			GL11.glBegin(GL11.GL_TRIANGLES);
				GL11.glTexCoord2f(0.25f, 0);
				GL11.glVertex3f(0.0f, 0.0625f, -0.25f);
				GL11.glTexCoord2f(0, 0);
				GL11.glVertex3f(0.0f, 0.0f, -0.5f);
				GL11.glTexCoord2f(0.25f, 1);
				GL11.glVertex3f(0.0f, -0.0625f, -0.25f);
			GL11.glEnd();
			
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex3f(-0.0375f, 0.0f, 0.5f);
				GL11.glTexCoord2f(1, 0);
				GL11.glVertex3f(0.0375f, 0.0f, 0.5f);
				GL11.glTexCoord2f(0.25f, 0);
				GL11.glVertex3f(0.0375f, 0.0f, -0.25f);
				GL11.glTexCoord2f(0.25f, 1);
				GL11.glVertex3f(-0.0375f, 0.0f, -0.25f);
			GL11.glEnd();
		
			GL11.glBegin(GL11.GL_TRIANGLES);
				GL11.glTexCoord2f(0.25f, 0);
				GL11.glVertex3f(0.0625f, 0.0f, -0.25f);
				GL11.glTexCoord2f(0, 0);
				GL11.glVertex3f(0.0f, 0.0f, -0.5f);
				GL11.glTexCoord2f(0.25f, 1);
				GL11.glVertex3f(-0.0625f, 0.0f, -0.25f);
			GL11.glEnd();
			GL11.glEnable(GL11.GL_CULL_FACE);
		} else {
			float r = (float) Math.toDegrees(Math.atan2(pos.x - lx, pos.z - lz));
			
			GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
			GL11.glRotatef(r, 0.0f, 1.0f, 0.0f);
			if (half) GL11.glScalef(0.5f, 0.5f, 0.5f);
			GL11.glColor3f(col.x, col.y, col.z);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex3f(0.25f, -0.25f, 0.0f);
				GL11.glTexCoord2f(1, 0);
				GL11.glVertex3f(0.25f, 0.25f, 0.0f);
				GL11.glTexCoord2f(0, 0);
				GL11.glVertex3f(-0.25f, 0.25f, 0.0f);
				GL11.glTexCoord2f(0, 1);
				GL11.glVertex3f(-0.25f, -0.25f, 0.0f);
			GL11.glEnd();
		}
		
		GL11.glPopMatrix();
	}
	
	public int getDamage() {
		if (half) return damage / 2;
		return damage;
	}
	
	public Vector3f collides(Entity e, float er) {
		return null;
	}
}
