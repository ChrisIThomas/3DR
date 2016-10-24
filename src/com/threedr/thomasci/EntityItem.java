package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class EntityItem extends Entity {
	protected final Item item;
	
	public EntityItem(float x, float z, Game g, Item i) {
		super(x, z, g);
		item = i;
		tex = TDR.getTexture("itemsModel", "img/itemsModel.png");
		height = 0.25f;
	}
	
	public void forces() {
		boolean smash = false;
		if (vel.z > 0.2f || vel.z < -0.2f) {
			if (item instanceof ItemPotion) {
				smash = true;
			}
		}
		if (game.getHeight((int) ((-pos.x - 0.25f) / 2), (int) -pos.z / 2) > -pos.y + 0.3f) {
			pos.x = Math.round(pos.x) - 0.25f;
			if (smash) destroy(false);
		} else if (game.getHeight((int) ((-pos.x + 0.25f) / 2), (int) -pos.z / 2) > -pos.y + 0.3f) {
			pos.x = Math.round(pos.x) + 0.25f;
			if (smash) destroy(false);
		}
		
		if (game.getHeight((int) -pos.x / 2, (int) ((-pos.z - 0.25f) / 2)) > -pos.y + 0.3f) {
			pos.z = Math.round(pos.z) - 0.25f;
			if (smash) destroy(false);
		} else if (game.getHeight((int) -pos.x / 2, (int) ((-pos.z + 0.25f) / 2)) > -pos.y + 0.3f) {
			pos.z = Math.round(pos.z) + 0.25f;
			if (smash) destroy(false);
		}
		
		
		if (game.getRoof((int) ((-pos.x) / 2), (int) -pos.z / 2) < -pos.y + height) {
			pos.y = game.getRoof((int) -pos.x / 2, (int) ((-pos.z) / 2)) - height;
			vel.y = 0;
		}
		
		if (game.getRoof((int) ((-pos.x - 0.25f) / 2), (int) -pos.z / 2) < -pos.y + height) {
			pos.x = Math.round(pos.x) - 0.25f;
			if (smash) destroy(false);
		} else if (game.getRoof((int) ((-pos.x + 0.25f) / 2), (int) -pos.z / 2) < -pos.y + height) {
			pos.x = Math.round(pos.x) + 0.25f;
			if (smash) destroy(false);
		}
		
		if (game.getRoof((int) -pos.x / 2, (int) ((-pos.z - 0.25f) / 2)) < -pos.y + height) {
			pos.z = Math.round(pos.z) - 0.25f;
			if (smash) destroy(false);
		} else if (game.getRoof((int) -pos.x / 2, (int) ((-pos.z + 0.25f) / 2)) < -pos.y + height) {
			pos.z = Math.round(pos.z) + 0.25f;
			if (smash) destroy(false);
		}
		
		if (vel.y > 2.0f) vel.y = 2.0f;
		if (vel.y < -2.0f) vel.y = -2.0f;
		
		if (-pos.y <= game.getHeight((int) -pos.x / 2, (int) ((-pos.z) / 2))) {
			pos.y = -game.getHeight((int) -pos.x / 2, (int) ((-pos.z) / 2));
			if (vel.y != 0.0f) {
				if (smash) destroy(false);
			}
			vel.y = 0;
		}
	}
	
	public void tick() {
		if (rot.y < 0) rot.y += 360;
		if (rot.y >= 360) rot.y -= 360;
		if (-pos.y > game.getHeight((int) -pos.x / 2, (int) ((-pos.z) / 2))) {
			vel.y += 0.025f;
		}
		if (-pos.y == game.getHeight((int) -pos.x / 2, (int) ((-pos.z) / 2))) {
			vel.x *= 0.85f;
			vel.z *= 0.85f;
			if (item.getID() == 20) {
				if (game.getTile((int) (-pos.x / 2), (int) -pos.z / 2) == 1) {
					((ItemPotion) item).setType(1);
				}
			}
		}
		if (vel.x <= 0.0001f && vel.x >= -0.0001f) vel.x = 0;
		if (vel.z <= 0.0001f && vel.z >= -0.0001f) vel.z = 0;
		//left and right movement
		pos.x += vel.x * Math.sin(Math.toRadians(rot.y + 90));
		pos.z -= vel.x * Math.cos(Math.toRadians(rot.y + 90));
		
		pos.y += vel.y;
		
		//forward and backward movement
		pos.x -= vel.z * Math.sin(Math.toRadians(rot.y));
		pos.z += vel.z * Math.cos(Math.toRadians(rot.y));
		
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
		
		if (item != null) {
			if (item.getMoney() == 0) {
				Vector3f vec = game.collides(this, rad);
				if (vec != null) {
					pos.x = vec.x;
					pos.z = vec.z;
				}
			}
		}
		
		forces();
	}
	
	public Item getItem() {
		return item;
	}
	
	public void setVelZ(float v) {
		vel.z = v;
	}
	
	public void setVelY(float v) {
		vel.y = v;
	}
	
	public void setRot(float r) {
		rot.y = r;
	}
	
	public void setPosY(float y) {
		pos.y = y;
	}
	
	public void draw(float lx, float lz) {
		//don't render this object if it is outside of the player's view
		if (Math.abs(pos.x - lx) > 15) return;
		if (Math.abs(pos.z - lz) > 15) return;
		
		float pw = 1.0f / tex.getWidth();
		float ph = 1.0f / tex.getHeight();
		
		float w = pw * 12;
		float h = ph * 5;
		float ty = item.getTLoc() / 16;
		float tx = (item.getTLoc() - ty * 16) * w;
		ty *= w;
		
		float r = (float) Math.toDegrees(Math.atan2(pos.x - lx, pos.z - lz));
		
		GL11.glPushMatrix();
		if (item instanceof ItemWearable) {
			((ItemWearable) item).colour();
		} else if (item instanceof ItemWeapon) {
			((ItemWeapon) item).colourHand();
		} else if (item.getCol() != null) {
			if (item.getID() != 24 && item.getID() != 25) {
				Vector3f col = item.getCol();
				GL11.glColor3f(col.x, col.y, col.z);
			}
		} else {
			GL11.glColor3f(1.0f, 1.0f, 1.0f);
		}
		GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
		GL11.glRotatef(r, 0.0f, 1.0f, 0.0f);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(tx + w, ty + h);
			GL11.glVertex3f(0.75f, 0.0f, 0.0f);
			GL11.glTexCoord2f(tx + w, ty);
			GL11.glVertex3f(0.75f, 0.625f, 0.0f);
			GL11.glTexCoord2f(tx, ty);
			GL11.glVertex3f(-0.75f, 0.625f, 0.0f);
			GL11.glTexCoord2f(tx, ty + h);
			GL11.glVertex3f(-0.75f, 0.0f, 0.0f);
		GL11.glEnd();
		
		if (item instanceof ItemPotion) {
			if (((ItemPotion) item).getType() != 0) {
				((ItemPotion) item).colour();
				tx += w;
				GL11.glBegin(GL11.GL_QUADS);
					GL11.glTexCoord2f(tx + w, ty + h);
					GL11.glVertex3f(0.75f, 0.0f, 0.0f);
					GL11.glTexCoord2f(tx + w, ty);
					GL11.glVertex3f(0.75f, 0.625f, 0.0f);
					GL11.glTexCoord2f(tx, ty);
					GL11.glVertex3f(-0.75f, 0.625f, 0.0f);
					GL11.glTexCoord2f(tx, ty + h);
					GL11.glVertex3f(-0.75f, 0.0f, 0.0f);
				GL11.glEnd();
			}
		} else if (item instanceof ItemWeapon) {
			((ItemWeapon) item).colour();
			ty += h;
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(tx + w, ty + h);
				GL11.glVertex3f(0.75f, 0.0f, 0.0f);
				GL11.glTexCoord2f(tx + w, ty);
				GL11.glVertex3f(0.75f, 0.625f, 0.0f);
				GL11.glTexCoord2f(tx, ty);
				GL11.glVertex3f(-0.75f, 0.625f, 0.0f);
				GL11.glTexCoord2f(tx, ty + h);
				GL11.glVertex3f(-0.75f, 0.0f, 0.0f);
			GL11.glEnd();
		}
		
		if (item.getMoney() > 0) {
			if (item.getID() != 31) {
				GL11.glTranslatef(0.0f, 1.05f, 0.0f);
				GL11.glScalef(0.005f, 0.005f, 0.005f);
				GL11.glRotatef(180, 0, 1, 0);
				GL11.glRotatef(180, 0, 0, 1);
				Text.drawText("§8" + item.getMoney(), 10, true);
			}
		}
		if (item.hasEnchantments()) {
			tx = 15 * w;
			ty = 0;
			GL11.glColor3f(1.0f, 1.0f, 1.0f);
			GL11.glTranslatef(0.0f, 0.625f, 0.0f);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(tx + w, ty + h);
				GL11.glVertex3f(0.75f, 0.0f, 0.0f);
				GL11.glTexCoord2f(tx + w, ty);
				GL11.glVertex3f(0.75f, 0.625f, 0.0f);
				GL11.glTexCoord2f(tx, ty);
				GL11.glVertex3f(-0.75f, 0.625f, 0.0f);
				GL11.glTexCoord2f(tx, ty + h);
				GL11.glVertex3f(-0.75f, 0.0f, 0.0f);
			GL11.glEnd();
		}
		
		GL11.glPopMatrix();
	}
	
	public void drawBox(float lx, float lz) {
		float r = (float) Math.toDegrees(Math.atan2(pos.x - lx, pos.z - lz));
		float w = item.getEWidth() * 0.125f;
		float h = item.getEHeight() * 0.125f;
		
		GL11.glPushMatrix();
		GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
		GL11.glRotatef(r, 0.0f, 1.0f, 0.0f);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex3f(w, 0.0f, 0.0f);
			GL11.glVertex3f(w, h, 0.0f);
			GL11.glVertex3f(-w, h, 0.0f);
			GL11.glVertex3f(-w, 0.0f, 0.0f);
		GL11.glEnd();
		GL11.glPopMatrix();
	}
}
