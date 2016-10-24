package com.threedr.thomasci;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class EntityPlayer extends EntityLiving {
	private float step;
	
	public EntityPlayer(float x, float z, Game g) {
		super(x, z, g);
		/*Item i = Item.newItem(11, "aaa");
		((ItemWeapon) i).setMaterial(Material.adamantine);
		i.setEnchantments(new EnchantmentList().addEnchantment(new Enchantment(0, 50, false)));
		inventory.setItem(i, 8);
		i = Item.newItem(0, "aaa");
		i.setEnchantments(new EnchantmentList().addEnchantment(new Enchantment(6, 1000, true)));
		inventory.setItem(i, 11);
		
		inventory.setItem(Item.newItem(23, "Def"), 12);
		inventory.setItem(Item.newItem(33, "Def"), 13);
		inventory.setCoins(100000);*/
	}
	
	public void update() {
		if (controller != null) {
			Vector3f con = controller.getDir();
			vel.x += con.x;
			vel.z += con.z;
			
			maxSpeed = 0.1f;
			if (effects.size() > 0) {
				for (int i = 0; i < effects.size(); i++) {
					if (effects.get(i).getType() == 1) maxSpeed -= effects.get(i).getVal() * 0.005f;
					else if (effects.get(i).getType() == 5) maxSpeed += effects.get(i).getVal() * 0.005f;
				}
			}
			maxSpeed += inventory.getSpeed() * 0.005f;
			
			if (Mouse.isButtonDown(1)) {
				if (inventory.getItem(controller.getSlot() + 8) != null) {
					if (inventory.getItem(controller.getSlot() + 8).getID() == 24) {
						maxSpeed /= 2;
					} else if (inventory.getItem(controller.getSlot() + 8).getID() == 25) {
						maxSpeed /= 4;
					}
				}
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || attacking) maxSpeed /= 2;
			if (game.getTile((int) (-pos.x / 2), (int) -pos.z / 2) == 1) maxSpeed /= 2;
			if (vel.x > maxSpeed) vel.x = maxSpeed;
			if (vel.x < -maxSpeed) vel.x = -maxSpeed;
			if (vel.z > maxSpeed) vel.z = maxSpeed;
			if (vel.z < -maxSpeed) vel.z = -maxSpeed;
			
			visibility = 0;
			if (vel.z != 0) visibility = Math.abs(vel.z) * 1000.0f;
			if (Math.abs(vel.x) * 1000.0f > visibility) visibility = Math.abs(vel.x) * 1000;
			
			con = controller.getRot();
			rot.y += con.y;
			rot.z += con.z;
			
			slot = controller.getSlot();
			if ((Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) && !Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
				if (inventory.getItem(slot + 8) != null) {
					base: {
						if (Mouse.isButtonDown(1)) {
							if (inventory.getItem(slot + 8).getID() != 26) {
								break base;
							}
						}
						attacking = true;
					}
				}
			} else {
				attacking = false;
			}
			maxHealth = 10 + inventory.getHealth();
			if (health > maxHealth) health = maxHealth;
		}
		
		/*if (game.getTile((int) (-pos.x / 2), (int) -pos.z / 2) == 1 || attacking) {
			if (vel.z > 0.05f) vel.z = 0.05f;
			if (vel.z < -0.05f) vel.z = -0.05f;
			if (vel.x > 0.05f) vel.x = 0.05f;
			if (vel.x < -0.05f) vel.x = -0.05f;
		}*/

		if (-pos.y == game.getHeight((int) -pos.x / 2, (int) -pos.z / 2)) {
			if (vel.z > 0 || vel.x > 0) {
				if (vel.z > vel.x) {
					step += vel.z / 2;
				} else {
					step += vel.x / 2;
				}
			} else if (vel.z < 0 || vel.x < 0) {
				if (vel.z < vel.x) {
					step += vel.z / -2;
				} else {
					step += vel.x / -2;
				}
			}
		}
		if (step >= 1.0f) {
			int type = 4;
			if (game.getTile((int) (-pos.x / 2), (int) -pos.z / 2) == 0 || game.getTile((int) (-pos.x / 2), (int) -pos.z / 2) == 4)
				type = 5;
			if (game.getTile((int) (-pos.x / 2), (int) -pos.z / 2) == 1)
				type = 6;
			ALLoader.addSound(type, (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] {pos.x, pos.y, pos.z}).rewind());
			step -= 1.0f;
		}
		
		inventory.update();
	}
	
	public int damage(int num, int type) {
		ALLoader.addSound(17, (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] {pos.x, pos.y, pos.z}).rewind());
		return super.damage(num, type);
	}
	
	public void draw(float lx, float lz) {
		//System.out.println(visibility);
		/*float pw = 1.0f / tex.getWidth();
		float ph = 1.0f / tex.getHeight();
		
		float w = pw * 7;
		float h = ph * 12;
		
		GL11.glPushMatrix();
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(w, h);
			GL11.glVertex3f(0.4375f, 0.0f, 0.0f);
			GL11.glTexCoord2f(w, 0);
			GL11.glVertex3f(0.4375f, 1.5f, 0.0f);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(-0.4375f, 1.5f, 0.0f);
			GL11.glTexCoord2f(0, h);
			GL11.glVertex3f(-0.4375f, 0.0f, 0.0f);
		GL11.glEnd();
		GL11.glPopMatrix();*/
	}
	
	public void setHealth(int hamount, int mamount) {
		health = hamount;
		maxHealth = mamount;
	}
	
	public void setInventory(Inventory inv) {
		inventory = inv;
	}
	
	public void setMoney(int amount) {
		inventory.setCoins(amount);
	}
}
