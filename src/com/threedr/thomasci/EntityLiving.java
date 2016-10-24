package com.threedr.thomasci;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class EntityLiving extends Entity {
	protected Inventory inventory;
	protected Controller controller;
	protected int slot;
	protected float anim, maxSpeed, visibility;
	protected int health, maxHealth;
	protected boolean attacking;
	protected Vector3f bloodCol;
	
	public EntityLiving(float x, float z, Game g) {
		super(x, z, g);
		tex = TDR.getTexture("character", "img/character.png");
		inventory = new Inventory();
		slot = 0;
		attacking = false;
		health = 10;
		maxHealth = 10;
		maxSpeed = 0.1f;
		visibility = 0;
		bloodCol = new Vector3f(0.5f, 0, 0);
	}
	
	public void setController(Controller c) {
		controller = c;
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
		
		
		if (game.getRoof((int) ((-pos.x) / 2), (int) -pos.z / 2) < -pos.y + height) {
			pos.y = game.getRoof((int) -pos.x / 2, (int) ((-pos.z) / 2)) - height;
			vel.y = 0;
		}
		
		if (game.getRoof((int) ((-pos.x - 0.25f) / 2), (int) -pos.z / 2) < -pos.y + height) {
			pos.x = Math.round(pos.x) - 0.25f;
		} else if (game.getRoof((int) ((-pos.x + 0.25f) / 2), (int) -pos.z / 2) < -pos.y + height) {
			pos.x = Math.round(pos.x) + 0.25f;
		}
		
		if (game.getRoof((int) -pos.x / 2, (int) ((-pos.z - 0.25f) / 2)) < -pos.y + height) {
			pos.z = Math.round(pos.z) - 0.25f;
		} else if (game.getRoof((int) -pos.x / 2, (int) ((-pos.z + 0.25f) / 2)) < -pos.y + height) {
			pos.z = Math.round(pos.z) + 0.25f;
		}
		
		if (vel.y > 2.0f) vel.y = 2.0f;
		if (vel.y < -2.0f) vel.y = -2.0f;
		
		if (-pos.y <= game.getHeight((int) -pos.x / 2, (int) ((-pos.z) / 2))) {
			pos.y = -game.getHeight((int) -pos.x / 2, (int) ((-pos.z) / 2));
			vel.y = 0;
			if (game.getTile((int) -pos.x / 2, (int) ((-pos.z) / 2)) == -1) {
				health = 0;
			}
		}
	}
	
	public void update() {
		if (controller != null) {
			Vector3f con = controller.getDir();
			vel.x += con.x;
			vel.z += con.z;
			if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				if (vel.x > maxSpeed) vel.x = maxSpeed;
				if (vel.x < -maxSpeed) vel.x = -maxSpeed;
				if (vel.z > maxSpeed) vel.z = maxSpeed;
				if (vel.z < -maxSpeed) vel.z = -maxSpeed;
			} else {
				if (vel.x > maxSpeed) vel.x = maxSpeed / 2;
				if (vel.x < -maxSpeed) vel.x = -maxSpeed / 2;
				if (vel.z > maxSpeed) vel.z = maxSpeed / 2;
				if (vel.z < -maxSpeed) vel.z = -maxSpeed / 2;
			}
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
		}
		
		if (game.getTile((int) (-pos.x / 2), (int) -pos.z / 2) == 1 || attacking) {
			if (vel.z > 0.05f) vel.z = 0.05f;
			if (vel.z < -0.05f) vel.z = -0.05f;
			if (vel.x > 0.05f) vel.x = 0.05f;
			if (vel.x < -0.05f) vel.x = -0.05f;
		}
		if (vel.z > maxSpeed) vel.z = maxSpeed;
		if (vel.z < -maxSpeed) vel.z = -maxSpeed;
		if (vel.x > maxSpeed) vel.x = maxSpeed;
		if (vel.x < -maxSpeed) vel.x = -maxSpeed;
		inventory.update();
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
		forces();
		
		if (controller != null) {
			Vector3f vec = game.collides(this, rad);
			if (vec != null) {
				pos.x = vec.x;
				pos.z = vec.z;
			}
		}
		
		if (effects.size() > 0) {
			for (int i = 0; i < effects.size(); i++) {
				effects.get(i).tick(this);
				if (effects.get(i).isFinished()) {
					//if (effects.get(i).getType() == 1) maxSpeed = 0.1f;
					effects.remove(i);
					i--;
				}
			}
		}
	}
	
	public void drawArmour(int ang, int anim) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		boolean leg = false;
		if (inventory.getItem(0) != null) {
			inventory.getItem(0).drawModel(ang, anim);
		}
		if (inventory.getItem(1) != null) {
			inventory.getItem(1).drawModel(ang, anim);
		}
		if (inventory.getItem(2) != null) {
			if (inventory.getItem(2).getID() == 2) {
				inventory.getItem(2).drawModel(ang, anim);
				leg = true;
			}
		}
		if (inventory.getItem(3) != null) {
			inventory.getItem(3).drawModel(ang, anim);
		}
		if (inventory.getItem(4) != null) {
			inventory.getItem(4).drawModel(ang, anim);
		}
		if (inventory.getItem(5) != null) {
			inventory.getItem(5).drawModel(ang, anim);
		}
		if (inventory.getItem(2) != null) {
			if (!leg) inventory.getItem(2).drawModel(ang, anim);
		}
	}
	
	public void drawBox(float lx, float lz) {
		if (health > 0) {
			float r = (float) Math.toDegrees(Math.atan2(pos.x - lx, pos.z - lz));
			
			GL11.glPushMatrix();
			GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
			GL11.glRotatef(r, 0, 1, 0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex3f(0.4375f, 0.0f, 0.0f);
				GL11.glVertex3f(0.4375f, 1.5f, 0.0f);
				GL11.glVertex3f(-0.4375f, 1.5f, 0.0f);
				GL11.glVertex3f(-0.4375f, 0.0f, 0.0f);
			GL11.glEnd();
			GL11.glPopMatrix();
		}
	}
	
	public Vector3f collides(Entity e, float er) {
		if (rad <= 0) return null;
		if (health <= 0) return null;
		float dirx = 0, dirz = 0;
		float ex = e.getX();
		float ez = e.getZ();
		if ((ex - er >= pos.x - rad && ex - er <= pos.x + rad) &&
				(ez >= pos.z - rad && ez <= pos.z + rad)) {
			dirx = 1;
		} else if ((ex + er >= pos.x - rad && ex + er <= pos.x + rad) &&
				(ez >= pos.z - rad && ez <= pos.z + rad)) {
			dirx = -1;
		}
		if ((ez - er >= pos.z - rad && ez - er <= pos.z + rad) &&
				(ex >= pos.x - rad && ex <= pos.x + rad)) {
			dirz = 1;
		} else if ((ez + er >= pos.z - rad && ez + er <= pos.z + rad) &&
				(ex >= pos.x - rad && ex <= pos.x + rad)) {
			dirz = -1;
		}
		if (dirx != 0 || dirz != 0) {
			if (e.pos.y >= pos.y - height && e.pos.y <= pos.y) {
				float x = ex, z = ez;
				if (dirx != 0)
					x = pos.x + (rad + er) * dirx;
				if (dirz != 0)
					z = pos.z + (rad + er) * dirz;
				return new Vector3f(x, 0, z);
			}
		}
		return null;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public int damage(int num, int type) {
		int amount = 0;
		switch (type) {
			case 0: //standard damage
				//adjust damage depending on Armour and Enchantments.
				for (int i = 0; i < 8; i++) {
					Item item = inventory.getItem(i);
					if (item != null) {
						if (item.getEnchantments() != null) {
							amount += item.getEnchantments().getAmount(0);
						}
						if (item instanceof ItemArmour) {
							amount += ((ItemArmour) item).getResistance();
						}
					}
				}
				for (int i = 0; i < effects.size(); i++) {
					if (effects.get(i).getType() == 6) amount += effects.get(i).getVal();
				}
				num -= amount;
				if (num < 1) num = 1;
				health -= num;
				amount = num;
				break;
			case 1: //fire damage
				//adjust damage depending on Armour and Enchantments
				for (int i = 0; i < 8; i++) {
					Item item = inventory.getItem(i);
					if (item != null) {
						if (item.getEnchantments() != null) {
							amount += item.getEnchantments().getAmount(1);
						}
						//increase Damage if armour is worn (essentially you are being cooked in an oven if you are on fire and sitting in a metal box)
						if (item instanceof ItemArmour) {
							amount -= ((ItemArmour) item).getResistance();
						}
					}
				}
				num -= amount;
				if (num < 1) num = 1;
				health -= num;
				amount = num;
				break;
			case 2: //cold damage (doesn't deal damage just slows you)
				maxSpeed = 0.01f;
				break;
			case 3: //arcane damage
				//adjust depending on magic resistance
				for (int i = 0; i < 8; i++) {
					Item item = inventory.getItem(i);
					if (item != null) {
						if (item.getEnchantments() != null) {
							amount += item.getEnchantments().getAmount(3);
						}
					}
				}
				num -= amount;
				if (num < 1) num = 1;
				health -= num;
				amount = num;
				break;
			case 4: //acid damage
				//adjust depending on armor strength and Enchantments
				for (int i = 0; i < 8; i++) {
					Item item = inventory.getItem(i);
					if (item != null) {
						if (item.getEnchantments() != null) {
							amount += item.getEnchantments().getAmount(4);
						}
						if (item instanceof ItemArmour) {
							amount += ((ItemArmour) item).getResistance();
						}
					}
				}
				num -= amount;
				if (num < 0) num = 0;
				health -= num;
				amount = num;
				break;
			case 5: //poison damage
				//adjust depending on poison resistance
				for (int i = 0; i < 8; i++) {
					Item item = inventory.getItem(i);
					if (item != null) {
						if (item.getEnchantments() != null) {
							amount += item.getEnchantments().getAmount(5);
						}
					}
				}
				num -= amount;
				if (num < 1) num = 1;
				health -= num;
				amount = num;
				break;
		}
		for (int i = 0; i < 10; i++) {
			game.addParticle(new Particle(new Vector3f(pos.x, pos.y - 1.0f, pos.z),
					new Vector3f((float) Math.random() * 0.2f - 0.1f, (float) Math.random() * -0.1f, (float) Math.random() * 0.2f - 0.1f),
					bloodCol, true, game));
		}
		return amount;
	}
	
	public void heal(int amount) {
		health += amount;
		if (health > maxHealth) health = maxHealth;
	}
	
	public void setMaxSpeed(float val) {
		maxSpeed = val;
		if (maxSpeed > 0.2f) maxSpeed = 0.2f;
	}
	
	public void addMaxSpeed(float val) {
		maxSpeed += val;
		if (maxSpeed > 0.2f) maxSpeed = 0.2f;
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getMaxHealth() {
		return maxHealth;
	}
	
	public float getVis() {
		return visibility;
	}
}
