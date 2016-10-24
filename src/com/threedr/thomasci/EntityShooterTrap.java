package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class EntityShooterTrap extends EntityTrap {
	private boolean fired;
	private int type;
	
	public EntityShooterTrap(float x, float z, Game g, String activ, float rot, int type) {
		super(x, z, g, activ);
		tex = TDR.getTexture("shootTrap", "img/shootTrap.png");
		fired = false;
		this.type = type;
		this.rot.y = rot;
	}
	
	public void tick() {
		if (activator != null) {
			if (!fired && activator.isActive()) {
				System.out.println("ACTIVATED");
				float fx = 0.0f;
				float fz = 0.0f;
				switch ((int) rot.y) {
					case 0:
						fz = 0.5f;
						break;
					case 90:
						fx = 0.5f;
						break;
					case 180:
						fz = -0.5f;
						break;
					case 270:
						fx = -0.5f;
						break;
				}
				game.addEnt(new EntityProjectile(fx + pos.x, pos.y - 1.0f, fz + pos.z, rot.y + 180 * (float) Math.cos(Math.toRadians(rot.y)), 0, 0.1f, type, false, game));
			}
			fired = activator.isActive();
		}
		
		forces();
	}
	
	public void draw(float lx, float lz) {
		GL11.glPushMatrix();
		
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glTranslatef(-pos.x, -pos.y + 1.0f, -pos.z);
		switch ((int) rot.y) {
			case 0:
				GL11.glTranslatef(0, 0, -1.037f);
				break;
			case 90:
				GL11.glTranslatef(-1.037f, 0, 0);
				break;
			case 180:
				GL11.glTranslatef(0, 0, 1.037f);
				break;
			case 270:
				GL11.glTranslatef(1.037f, 0, 0);
				break;
		}
		GL11.glRotatef(rot.y, 0, 1, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex3f(-1.0f, -1.0f, 0.04f);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex3f(1.0f, -1.0f, 0.04f);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex3f(1.0f, 1.0f, 0.04f);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(-1.0f, 1.0f, 0.04f);
		GL11.glEnd();
		
		GL11.glPopMatrix();
	}
	
	public Vector3f collides(Entity e, float er) {
		return null;
	}
}
