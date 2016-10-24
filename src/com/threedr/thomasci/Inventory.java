package com.threedr.thomasci;

public class Inventory {
	private Item[] gear;
	private int coins;
	private boolean alchemy;
	private int health = 0, speed = 0, aspeed = 0;
	
	//0-7 armor
	//8-11 hotbar
	//12-21 inventory slots
	//22-26 alchemy slots
	//27-30 quiver slots
	public Inventory() {
		gear = new Item[31];
		coins = 0;
		//armour
		/*gear[0] = Item.newItem(0, "Def");
		((ItemWearable) gear[0]).setColour(0.45f, 0.25f, 0.0f);
		gear[1] = Item.newItem(1, "Def");
		((ItemWearable) gear[1]).setColour(0.95f, 0.95f, 0.95f);
		gear[2] = Item.newItem(8, "Def");
		gear[3] = Item.newItem(2, "Def");
		((ItemWearable) gear[3]).setColour(0.85f, 0.85f, 0.85f);
		gear[4] = Item.newItem(3, "Def");
		((ItemWearable) gear[4]).setColour(0.15f, 0.45f, 0.15f);
		gear[5] = Item.newItem(5, "Def");
		((ItemWearable) gear[5]).setColour(0.15f, 0.45f, 0.15f);*/
	}
	
	public void update() {
		for (int i = 0; i < gear.length; i++) {
			if (gear[i] != null) {
				if (gear[i].shouldDelete()) gear[i] = null;
			}
		}
	}
	
	public Item getItem(int slot) {
		return gear[slot];
	}
	
	public Item[] getItems() {
		return gear;
	}
	
	public void changeCoins(int count) {
		coins += count;
	}
	
	public void setCoins(int num) {
		coins = num;
	}
	
	public int getCoins() {
		return coins;
	}
	
	//this will return the slot the item is in
	public int hasItem(int type) {
		for (int i = 0; i < gear.length; i++) {
			if (gear[i] != null) {
				if (gear[i].getID() == type) {
					return i;
				}
			}
		}
		return -1;
	}
	
	public void setItem(Item i, int slot) {
		if (slot >= 0 && slot < 31) {
			if (slot <= 7) {
				if (i instanceof ItemWearable) {
					if (((ItemWearable) i).getSlot() == slot) {
						gear[slot] = i;
					}
				} else if (i == null) {
					gear[slot] = i;
				}
				health = 0;
				
			} else {
				gear[slot] = i;
			}
			alchemy = false;
			for (int k = 0; k < gear.length; k++) {
				if (gear[k] != null) {
					if (gear[k].getID() == 23) {
						alchemy = true;
						break;
					}
				}
			}
		}
		health = 0;
		aspeed = 0;
		speed = 0;
		for (int j = 0; j < 8; j++) {
			if (gear[j] != null) {
				if (gear[j].getEnchantments() != null) {
					health += gear[j].getEnchantments().getAmount(8);
					aspeed += gear[j].getEnchantments().getAmount(7);
					speed += gear[j].getEnchantments().getAmount(6);
				}
			}
		}
	}
	
	public Item putItem(Item i) {
		for (int k = 0; k < 14; k++) {
			if (gear[k + 8] == null) {
				gear[k + 8] = i;
				return null;
			}
		}
		return i;
	}
	
	public String write() {
		String inv = "";
		for (int i = 0; i < gear.length; i++) {
			System.out.println(i);
			if (gear[i] != null) {
				inv += gear[i].write();
			}
			if (i < gear.length - 1) inv += ",";
		}
		return inv;
	}
	
	public static Inventory createInventory(String idata) {
		Inventory inv = new Inventory();
		String[] dat = idata.split(",");
		for (int i = 0; i < dat.length; i++) {
			if (dat[i].length() != 0) {
				System.out.println(i);
				inv.setItem(Item.createItem(dat[i]), i);
			}
		}
		return inv;
	}
	
	public boolean hasAlchemy() {
		return alchemy;
	}
	
	public boolean hasQuiver() {
		if (gear[7] != null) {
			return gear[7].getID() == 33;
		}
		return false;
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public int getASpeed() {
		return aspeed;
	}
}
