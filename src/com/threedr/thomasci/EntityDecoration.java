package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;

public class EntityDecoration extends Entity {
	protected int type;
	
	public EntityDecoration(float x, float z, Game g, int type) {
		super(x, z, g);
		this.type = type;
		tex = TDR.getTexture("decor", "img/decor.png");
	}
	
	public void tick() {
		forces();
	}
	
	public void draw(float lx, float lz) {
		//don't render this object if it is outside of the player's view
		if (Math.abs(pos.x - lx) > 15) return;
		if (Math.abs(pos.z - lz) > 15) return;
		
		float pw = 1.0f / tex.getWidth();
		float ph = 1.0f / tex.getHeight();
		
		float w = pw * 6;
		float h = ph * 3;
		float ty = type / 4;
		float tx = (type - ty * 4) * w;
		ty *= w;
		
		float r = (float) Math.toDegrees(Math.atan2(pos.x - lx, pos.z - lz));
		
		GL11.glPushMatrix();
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
		GL11.glRotatef(r, 0.0f, 1.0f, 0.0f);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(tx + w, ty + h);
			GL11.glVertex3f(0.375f, 0.0f, 0.0f);
			GL11.glTexCoord2f(tx + w, ty);
			GL11.glVertex3f(0.375f, 0.375f, 0.0f);
			GL11.glTexCoord2f(tx, ty);
			GL11.glVertex3f(-0.375f, 0.375f, 0.0f);
			GL11.glTexCoord2f(tx, ty + h);
			GL11.glVertex3f(-0.375f, 0.0f, 0.0f);
		GL11.glEnd();
		
		GL11.glPopMatrix();
	}
}
