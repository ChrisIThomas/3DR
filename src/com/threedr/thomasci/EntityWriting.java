package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class EntityWriting extends EntityWallObject {
	private String text;
	
	public EntityWriting(float x, float z, Game g, float rot, String text) {
		super(x, z, g);
		this.rot.y = rot;
		this.text = text;
		tex = TDR.getTexture("writing", "img/writing.png");
	}
	
	public String getText() {
		return text;
	}
	
	public void forces() {
		if (-pos.y <= game.getHeight((int) -pos.x / 2, (int) ((-pos.z) / 2))) {
			pos.y = -game.getHeight((int) -pos.x / 2, (int) ((-pos.z) / 2));
			vel.y = 0;
		}
	}
	
	public void draw(float lx, float lz) {
		//don't render this object if it is outside of the player's view
		if (Math.abs(pos.x - lx) > 15) return;
		if (Math.abs(pos.z - lz) > 15) return;
		
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
		
		//front
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex3f(-0.875f, -0.5f, 0.04f);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex3f(0.875f, -0.5f, 0.04f);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex3f(0.875f, 0.375f, 0.04f);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(-0.875f, 0.375f, 0.04f);
		GL11.glEnd();
		
		GL11.glPopMatrix();
	}
	
	public void drawBox(float lx, float lz) {
		GL11.glPushMatrix();
		
		GL11.glTranslatef(-pos.x, -pos.y + 1.0f, -pos.z);
		switch ((int) rot.y) {
			case 0:
				GL11.glTranslatef(0, 0, -1.039f);
				break;
			case 90:
				GL11.glTranslatef(-1.039f, 0, 0);
				break;
			case 180:
				GL11.glTranslatef(0, 0, 1.039f);
				break;
			case 270:
				GL11.glTranslatef(1.039f, 0, 0);
				break;
		}
		GL11.glRotatef(rot.y, 0, 1, 0);
		
		//front
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex3f(-0.875f, -0.5f, 0.04f);
		GL11.glVertex3f(0.875f, -0.5f, 0.04f);
		GL11.glVertex3f(0.875f, 0.375f, 0.04f);
		GL11.glVertex3f(-0.875f, 0.375f, 0.04f);
		GL11.glEnd();
		
		GL11.glPopMatrix();
	}
	
	public Vector3f collides(Entity e, float er) {return null;}
}
