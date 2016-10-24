package com.threedr.thomasci;

public class Recipe {
	private final int[] items;
	private final int product;
	
	public Recipe(int[] data, int type) {
		items = data;
		product = type;
	}
	
	public int[] getIngredients() {
		int[] data = new int[items.length];
		for (int i = 0; i < data.length; i++) {
			data[i] = items[i];
		}
		return data;
	}
	
	public int getProduct() {
		return product;
	}
	
	public static int hasIngredients(int[] ing) {
		for (int k = 0; k < recipes.length; k++) {
			int[] data = recipes[k].getIngredients();
			int count = 0;
			for (int i = 0; i < ing.length; i++) {
				for (int j = 0; j < data.length; j++) {
					if (ing[i] == data[j]) {
						data[j] = -1;
						count++;
						break;
					}
				}
			}
			if (count == data.length) return recipes[k].getProduct();
		}
		return -1;
	}
	
	private static final Recipe[] recipes;
	
	static {
		recipes = new Recipe[10];
		recipes[0] = new Recipe(new int[] {13, 18, 32}, 2); //health potion
		recipes[1] = new Recipe(new int[] {13, 16, 17}, 3); //speed potion
		recipes[2] = new Recipe(new int[] {13, 15, 32}, 4); //defense potion
		recipes[3] = new Recipe(new int[] {13, 14, 32}, 10); //attack speed potion
		recipes[4] = new Recipe(new int[] {13, 18, 17}, 13); //damage potion
		recipes[5] = new Recipe(new int[] {13, 14}, 14); //fire potion
		recipes[6] = new Recipe(new int[] {13, 16}, 15); //cold potion
		recipes[7] = new Recipe(new int[] {13, 19}, 16); //magic potion
		recipes[8] = new Recipe(new int[] {13, 15, 17}, 17); //acid vial
		recipes[9] = new Recipe(new int[] {13, 21}, 18); //poison vial
	}
}
