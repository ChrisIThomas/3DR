package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Particle {
	private Vector3f pos, vel, col;
	private boolean grav, destroy;
	private Game game;
	private int life = 100;
	private float rotz;
	
	public Particle(Vector3f pos, Vector3f vel, Vector3f col, boolean grav, Game game) {
		this.pos = pos;
		this.vel = vel;
		this.col = col;
		this.grav = grav;
		this.game = game;
	}
	
	public void forces() {
		if (game.getHeight((int) ((-pos.x - 0.25f) / 2), (int) -pos.z / 2) > -pos.y + 0.3f) {
			pos.x = Math.round(pos.x) - 0.25f;
		} else if (game.getHeight((int) ((-pos.x + 0.25f) / 2), (int) -pos.z / 2) > -pos.y + 0.3f) {
			pos.x = Math.round(pos.x) + 0.25f;
		}
		
		if (game.getHeight((int) -pos.x / 2, (int) ((-pos.z - 0.25f) / 2)) > -pos.y + 0.3f) {
			pos.z = Math.round(pos.z) - 0.25f;
		} else if (game.getHeight((int) -pos.x / 2, (int) ((-pos.z + 0.25f) / 2)) > -pos.y + 0.3f) {
			pos.z = Math.round(pos.z) + 0.25f;
		}
		
		
		if (game.getRoof((int) ((-pos.x) / 2), (int) -pos.z / 2) < -pos.y + 0.1f) {
			pos.y = -game.getRoof((int) -pos.x / 2, (int) ((-pos.z) / 2)) - 0.1f;
			vel.y = 0;
		}
		
		if (game.getRoof((int) ((-pos.x - 0.25f) / 2), (int) -pos.z / 2) < -pos.y + 0.1f) {
			pos.x = Math.round(pos.x) - 0.25f;
		} else if (game.getRoof((int) ((-pos.x + 0.25f) / 2), (int) -pos.z / 2) < -pos.y + 0.1f) {
			pos.x = Math.round(pos.x) + 0.25f;
		}
		
		if (game.getRoof((int) -pos.x / 2, (int) ((-pos.z - 0.25f) / 2)) < -pos.y + 0.1f) {
			pos.z = Math.round(pos.z) - 0.25f;
		} else if (game.getRoof((int) -pos.x / 2, (int) ((-pos.z + 0.25f) / 2)) < -pos.y + 0.1f) {
			pos.z = Math.round(pos.z) + 0.25f;
		}
		
		if (vel.y > 2.0f) vel.y = 2.0f;
		if (vel.y < -2.0f) vel.y = -2.0f;
		
		if (-pos.y <= game.getHeight((int) -pos.x / 2, (int) ((-pos.z) / 2))) {
			pos.y = -game.getHeight((int) -pos.x / 2, (int) ((-pos.z) / 2));
			vel.y = 0;
		}
	}
	
	public void tick() {
		if (grav) {
			if (-pos.y > game.getHeight((int) -pos.x / 2, (int) ((-pos.z) / 2))) {
				vel.y += 0.025f;
			}
		}
		if (-pos.y == game.getHeight((int) -pos.x / 2, (int) ((-pos.z) / 2))) {
			vel.x *= 0.85f;
			vel.z *= 0.85f;
		}
		if (vel.x <= 0.0001f && vel.x >= -0.0001f) vel.x = 0;
		if (vel.z <= 0.0001f && vel.z >= -0.0001f) vel.z = 0;
		pos.x += vel.x;
		pos.z -= vel.z;
		pos.y += vel.y;
		
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
		forces();
		life -= 2;
		if (life <= 0) destroy = true;
		rotz += (vel.x + vel.y + vel.z) * 50;
	}
	
	public void draw(float lx, float lz) {
		//don't render this object if it is outside of the player's view
		if (Math.abs(pos.x - lx) > 15) return;
		if (Math.abs(pos.z - lz) > 15) return;
		
		float r = (float) Math.toDegrees(Math.atan2(pos.x - lx, pos.z - lz));
		
		GL11.glPushMatrix();
		GL11.glColor3f(col.x, col.y, col.z);
		GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
		GL11.glScalef(life / 100.0f, life / 100.0f, life / 100.0f);
		GL11.glRotatef(r, 0, 1, 0);
		GL11.glRotatef(rotz, 0, 0, 1);
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex3f(0.0625f, -0.0625f, 0.0f);
			GL11.glVertex3f(0.0625f, 0.0625f, 0.0f);
			GL11.glVertex3f(-0.0625f, 0.0625f, 0.0f);
			GL11.glVertex3f(-0.0625f, -0.0625f, 0.0f);
		GL11.glEnd();
		GL11.glPopMatrix();
	}
	
	public boolean shouldDestroy() {
		return destroy;
	}
	
	public void setCol(Vector3f c) {
		col = c;
	}
}
