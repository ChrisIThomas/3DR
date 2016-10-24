package com.threedr.thomasci;

public class Enchantment {
	private final int type, damage; //damage is used to determine damage for damage enchantment and duration for magic effects
	private boolean defensive;
	
	//offensive:
	//0: standard damage
	//1-5: elemental damage (fire, cold, etc.)
	//6: life steal
	
	//defensive:
	//0: standard damage protection
	//1-5: elemental damage protection (fire, cold, etc.)
	//6: speed increase
	//7: attack speed
	//8: health increase
	
	public Enchantment(int typ, int dam, boolean def) {
		type = typ;
		damage = dam;
		defensive = def;
	}
	
	public int getType() {
		return type;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public boolean isDefensive() {
		return defensive;
	}
	
	public void setDefensive(boolean val) {
		defensive = val;
	}
	
	public void affect(EntityLiving owner, EntityLiving target) {	
		if (!defensive) {
			if (type == 0) {
				target.damage(damage, 0);
			} else if (type <= 5) {
				target.addEffect(new Effect(type - 1, 2));
			} else {
				if (type == 6) {
					int amount = target.damage(damage, 0);
					owner.heal(amount / 2);
				}
			}
		}
	}
	
	public static Enchantment randomEnchantment(boolean defensive) {
		Enchantment e = null;
		int type = (int) Math.floor(Math.random() * 6);
		
		if (defensive) {
			
		} else {
			e = new Enchantment(type, 0, false);
		}
		return e;
	}
	
	public String list() {
		if (defensive) {
			switch (type) {
				case 0:
					return "Damage Resistance " + damage;
				case 1:
					return "Fire Resistance " + damage;
				case 2:
					return "Cold Resistance " + damage;
				case 3:
					return "Arcane Resistance " + damage;
				case 4:
					return "Acid Resistance " + damage;
				case 5:
					return "Poison Resistance " + damage;
				case 6:
					return "Speed " + damage;
				case 7:
					return "Attack Speed " + damage;
				case 8:
					return "Health Buff " + damage;
			}
		} else {
			switch (type) {
				case 0:
					return "Damage " + damage;
				case 1:
					return "Fire " + damage;
				case 2:
					return "Cold " + damage;
				case 3:
					return "Arcane " + damage;
				case 4:
					return "Acid " + damage;
				case 5:
					return "Poison " + damage;
				case 6:
					return "Life Steal " + damage;
			}
		}
		return "ERR";
	}
	
	public String write() {
		return type + "#" + damage + "#" + defensive;
	}
}
