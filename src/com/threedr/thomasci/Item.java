package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Item {
	protected Texture tex;
	protected final int id, tloc;
	protected Vector3f col;
	private int swing, speed, damage, ewidth, eheight, money;
	protected int count;
	private float reach;
	protected int maxCount;
	protected String name = "", idName = "";
	private boolean delete;
	protected EnchantmentList enchantments;
	
	public Item(int id, int tl) {
		tex = TDR.getTexture("items", "img/items.png");
		this.id = id;
		tloc = tl;
		swing = 5;
		reach = 0;
		speed = 0;
		damage = 0;
		ewidth = 6;
		eheight = 5;
		money = 0;
		delete = false;
		enchantments = new EnchantmentList();
		col = new Vector3f(1.0f, 1.0f, 1.0f);
		count = 1;
		maxCount = 4;
	}
	
	public boolean hasEnchantments() {
		return enchantments.hasEnchantments();
	}
	
	public EnchantmentList getEnchantments() {
		return enchantments;
	}
	public void setEnchantments(EnchantmentList e) {
		enchantments = e;
	}
	
	public String listEnchantments() {
		return enchantments.list();
	}
	
	public void draw(boolean gui, int val) {
		float pw = 1.0f / tex.getWidth();
		float ph = 1.0f / tex.getHeight();
		
		float w = pw * 12;
		float h = ph * 12;
		float ty = tloc / 16;
		float tx = (tloc - ty * 16) * w;
		ty *= h;
		
		if (gui) {
			if (id == 26 || id == 28) {
				GL11.glTranslatef(-4.0f, 12.0f, 0.0f);
				GL11.glRotatef(-30, 0, 0, 1);
				if (id == 26) {
					ty += h * val;
				}
			}
		}
		
		if (col != null) GL11.glColor3f(col.x, col.y, col.z);
		else GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(tx, ty + h);
			GL11.glVertex2f(0.0f, 48.0f);
			GL11.glTexCoord2f(tx + w, ty + h);
			GL11.glVertex2f(48.0f, 48.0f);
			GL11.glTexCoord2f(tx + w, ty);
			GL11.glVertex2f(48.0f, 0.0f);
			GL11.glTexCoord2f(tx, ty);
			GL11.glVertex2f(0.0f, 0.0f);
		GL11.glEnd();
		
		if (id == 24) {
			tx = 0;
			ty = 2 * h;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(tx, ty + h);
				GL11.glVertex2f(0.0f, 48.0f);
				GL11.glTexCoord2f(tx + w, ty + h);
				GL11.glVertex2f(48.0f, 48.0f);
				GL11.glTexCoord2f(tx + w, ty);
				GL11.glVertex2f(48.0f, 0.0f);
				GL11.glTexCoord2f(tx, ty);
				GL11.glVertex2f(0.0f, 0.0f);
			GL11.glEnd();
		}
		if (count > 1) {
			if (!gui) {
				GL11.glTranslatef(40, 4, 0);
				Text.drawText("" + count, 1, true);
			}
		}
	}
	
	protected Item setName(String n) {
		name = n;
		return this;
	}
	
	protected Item setSwing(int s) {
		swing = s;
		return this;
	}
	
	protected Item setReach(float r) {
		reach = r;
		return this;
	}
	
	protected Item setSpeed(int s) {
		speed = s;
		return this;
	}
	
	protected Item setDamage(int d) {
		damage = d;
		return this;
	}
	
	protected Item setEWidth(int w) {
		ewidth = w;
		return this;
	}
	
	protected Item setEHeight(int h) {
		eheight = h;
		return this;
	}
	
	protected Item setIDName(String n) {
		idName = n;
		return this;
	}
	
	protected Item setMaxCount(int amount) {
		maxCount = amount;
		return this;
	}
	
	public void setMoney(int amount) {
		money = amount;
	}
	
	public void setCol(float r, float g, float b) {
		col = new Vector3f(r, g, b);
	}
	
	public void drawModel(int ang, int anim) {}
	
	public int getID() {
		return id;
	}
	
	public int getTLoc() {
		return tloc;
	}
	
	public String getName() {
		return name;
	}
	
	public String getIDName() {
		return idName;
	}
	
	public int getSwing() {
		return swing;
	}
	
	public int getSpeed() {
		if (speed < -3) return -3;
		return speed;
	}
	
	public int getDamage() {
		if (damage < 0) return 0;
		return damage;
	}
	
	public int getEWidth() {
		return ewidth;
	}
	
	public int getEHeight() {
		return eheight;
	}
	
	public float getReach() {
		return reach;
	}
	
	public int getMoney() {
		return money;
	}
	
	public Vector3f getCol() {
		return col;
	}
	
	public void setDelete(boolean del) {
		delete = del;
	}
	
	public boolean shouldDelete() {
		return delete;
	}
	
	public int getCount() {
		return count;
	}
	
	public int getMaxCount() {
		return maxCount;
	}
	
	//returns the number of items that did not fit into the stack
	//(e.g. if we tried to put 4 of an item into a stack of 2 (with a maximum of 4 in the stack) it would return 2, the number of items that did not make it into the stack)
	public int addCount(int amount) {
		if (count + amount > maxCount) {
			int c = -(maxCount - count - amount);
			count += maxCount - count;
			return c;
		}
		count += amount;
		if (count <= 0) setDelete(true);
		return 0;
	}
	
	public void setCount(int amount) {
		count = amount;
		if (count > maxCount) count = maxCount;
	}
	
	public static Item newItem(int id, String idName) {
		Item i = null;
		switch (id) {
			case 0:
				i = new ItemWearable(id, 0, 1).setName("Boots"); //boots
				break;
			case 1:
				i = new ItemWearable(id, 1, 2).setName("Pants"); //pants
				break;
			case 2:
				i = new ItemArmour(id, 2, 3).setName("Greaves"); //leg armor
				break;
			case 3:
				i = new ItemWearable(id, 3, 4).setName("Shirt"); //shirt
				break;
			case 4:
				i = new ItemWearable(id, 4, 5).setName("Vest"); //vest
				break;
			case 5:
				i = new ItemArmour(id, 5, 6).setName("Chestplate"); //chest armor
				break;
			case 6:
				i = new ItemWearable(id, 6, 7).setName("Hat"); //hat
				break;
			case 7:
				i = new ItemArmour(id, 7, 8).setName("Helmet"); //helmet
				break;
			case 8:
				i = new ItemArmour(id, 8, 9).setName("Full Helmet"); //full helmet
				break;
			case 9:
				i = new ItemWearable(id, 9, 10).setName("Belt"); //belt
				break;
			case 10:
				i = new ItemWeapon(id, 96).setName("Dagger").setSwing(15).setReach(-0.1f).setSpeed(1).setEWidth(4); //dagger
				break;
			case 11:
				i = new ItemWeapon(id, 97).setName("Sword").setSwing(30).setReach(0.3f).setSpeed(-2).setDamage(1); //sword
				break;
			case 12:
				i = new Item(id, 18).setName("Stone").setSpeed(1); //stone
				break;
			case 13:
				i = new Item(id, 19).setName("Bone"); //bone
				break;
			case 14:
				i = new Item(id, 20).setName("Red Mushroom").setEWidth(2); //red mushroom
				break;
			case 15:
				i = new Item(id, 21).setName("Brown Mushroom").setEWidth(2); //brown mushroom
				break;
			case 16:
				i = new Item(id, 22).setName("Yellow Mushroom").setEWidth(2); //yellow mushroom
				break;
			case 17:
				i = new Item(id, 23).setName("Moss"); //moss
				break;
			case 18:
				i = new Item(id, 24).setName("Leech").setEWidth(4); //leech
				break;
			case 19:
				i = new Item(id, 25).setName("Eye"); //eye
				break;
			case 20:
				i = new ItemPotion(id, 26, 0, 0).setName("Empty Vial"); //potions
				break;
			case 21:
				i = new Item(id, 28).setName("Venom"); //venom
				break;
			case 22:
				i = new Item(id, 29).setName("Key").setMaxCount(1); //key
				break;
			case 23:
				i = new Item(id, 30).setName("Mortar and Pestle"); //alchemy kit
				break;
			case 24:
				i = new ItemWand(id, 31, 0, 1).setName("Wand"); //wand
				break;
			case 25:
				i = new ItemStaff(id, 33, 0, 1).setName("Staff"); //staff
				break;
			case 26:
				i = new Item(id, 34).setName("Bow"); //bow
				break;
			case 27:
				i = new Item(id, 35).setName("Arrow").setMaxCount(8); //arrow
				break;
			case 28:
				i = new ItemWeapon(id, 98).setName("Spear").setSwing(15).setReach(0.5f).setSpeed(-2); //spear
				break;
			case 29:
				i = new ItemWeapon(id, 99).setName("Axe").setSwing(30).setReach(0.2f).setSpeed(-3).setDamage(3); //axe
				break;
			case 30:
				i = new Item(id, 38).setName("Coin").setEWidth(1).setEHeight(1); //coin
				break;
			case 31:
				i = new Item(id, 37).setName("Sack of Coins").setEWidth(5).setEHeight(4); //sack of coins
				break;
			case 32:
				i = new Item(id, 17).setName("Novo Root").setEWidth(2); //swapping root
				break;
			case 33:
				i = new ItemWearable(id, 16, 11).setName("Quiver"); //quiver
				break;
			case 34:
				i = new ItemWearable(id, 36, 12).setName("Ring"); //ring
				break;
			case 35:
				i = new ItemWearable(id, 39, 11).setName("Necklace"); //necklace
				break;
		}
		if (i != null) i.setIDName(idName);
		return i;
	}
	
	public static ItemPotion newPotion(int type, int val) {
		String name = "Empty Vial";
		switch (type) {
			case 1:
				name = "Vial of Water";
				break;
			case 2:
				name = "Healing Potion";
				break;
			case 3:
				name = "Speed Potion";
				break;
			case 4:
				name = "Defense Potion";
				break;
			case 5:
				name = "Fire Protection Potion";
				break;
			case 6:
				name = "Ice Protection Potion";
				break;
			case 7:
				name = "Magic Protection Potion";
				break;
			case 8:
				name = "Acid Protection Potion";
				break;
			case 9:
				name = "Poison Protection Potion";
				break;
			case 10:
				name = "Attack Potion";
				break;
			case 11:
				name = "Night Vision Potion";
				break;
			case 12:
				name = "X-Ray Potion";
				break;
			case 13:
				name = "Dark Vial";
				break;
			case 14:
				name = "Fire Vial";
				break;
			case 15:
				name = "Cold Vial";
				break;
			case 16:
				name = "Magic Vial";
				break;
			case 17:
				name = "Acid Vial";
				break;
			case 18:
				name = "Poison Vial";
				break;
		}
		return (ItemPotion) new ItemPotion(20, 26, type, val).setName(name);
	}
	
	//need to write: id, id name, color, count, enchantments
	public String write() {
		return id + ";" + idName + ";" + col.x + ";" + col.y + ";" + col.z + ";" + count + ";" + enchantments.write();
	}
	
	public static Item createItem(String idata) {
		String[] dat = idata.split(";");
		int length = 6;
		Item item = newItem(Integer.parseInt(dat[0]), dat[1]);
		if (item instanceof ItemWeapon) {
			((ItemWeapon) item).setMaterial(Material.getMaterial(Integer.parseInt(dat[2])));
			item.setCount(Integer.parseInt(dat[3]));
			length = 4;
		} else if (item instanceof ItemArmour) {
			((ItemArmour) item).setMaterial(Material.getMaterial(Integer.parseInt(dat[2])));
			item.setCount(Integer.parseInt(dat[3]));
			length = 4;
		} else if (item instanceof ItemWearable) {
			((ItemWearable) item).setCol(Float.parseFloat(dat[2]), Float.parseFloat(dat[3]), Float.parseFloat(dat[4]));
			item.setCount(Integer.parseInt(dat[5]));
			length = 4;
		} else if (item instanceof ItemWand) {
			((ItemWand) item).setType(Integer.parseInt(dat[6]));
			((ItemWand) item).setVal(Integer.parseInt(dat[7]));
			item.setCount(Integer.parseInt(dat[5]));
			length = 8;
		} else if (item instanceof ItemStaff) {
			((ItemStaff) item).setType(Integer.parseInt(dat[6]));
			((ItemStaff) item).setVal(Integer.parseInt(dat[7]));
			item.setCount(Integer.parseInt(dat[5]));
			length = 8;
		} else if (item instanceof ItemPotion) {
			((ItemPotion) item).setType(Integer.parseInt(dat[6]));
			((ItemPotion) item).setVal(Integer.parseInt(dat[7]));
			item.setCount(Integer.parseInt(dat[5]));
			length = 8;
		} else {
			item.setCount(Integer.parseInt(dat[5]));
		}
		if (dat.length + 1 > length) {
			item.setEnchantments(EnchantmentList.createEnchantments(dat[dat.length - 1]));
		}
		return item;
	}
}
