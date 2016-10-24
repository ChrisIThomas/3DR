package com.threedr.thomasci;

import java.util.ArrayList;

public class EnchantmentList {
	private ArrayList<Enchantment> enchantments;
	
	public EnchantmentList() {
		enchantments = new ArrayList<Enchantment>();
	}
	
	public boolean hasEnchantments() {
		return enchantments.size() > 0;
	}
	
	public EnchantmentList addEnchantment(Enchantment e) {
		enchantments.add(e);
		return this;
	}
	
	public void affect(EntityLiving owner, EntityLiving target) {
		for (int i = 0; i < enchantments.size(); i++) {
			enchantments.get(i).affect(owner, target);
		}
	}
	
	public int getAmount(int type) {
		int amount = 0;
		for (int i = 0; i < enchantments.size(); i++) {
			if (enchantments.get(i).getType() == type) {
				amount += enchantments.get(i).getDamage();
			}
		}
		return amount;
	}
	
	public String list() {
		String dat = "";
		for (int i = 0; i < enchantments.size(); i++) {
			dat += enchantments.get(i).list();
			if (i < enchantments.size() - 1) dat += "\n";
		}
		return dat;
	}
	
	public String write() {
		String dat = "";
		for (int i = 0; i < enchantments.size(); i++) {
			dat += enchantments.get(i).write();
			if (i < enchantments.size() - 1) dat += "!";
		}
		return dat;
	}
	
	public static EnchantmentList createEnchantments(String edata) {
		EnchantmentList list = new EnchantmentList();
		String[] enchantments = edata.split("!");
		for (int i = 0; i < enchantments.length; i++) {
			String[] dat = enchantments[i].split("#");
			if (dat.length >= 3)
			list.addEnchantment(new Enchantment((int) Float.parseFloat(dat[0]), (int) Float.parseFloat(dat[1]), Boolean.parseBoolean(dat[2])));
		}
		return list;
	}
}
