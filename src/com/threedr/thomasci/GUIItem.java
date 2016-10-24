package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class GUIItem extends GUIElement {
	private Item item;
	private Vector2f pos, start;
	
	public GUIItem(GUI g, Item i, int x, int y) {
		super(g);
		tex = TDR.getTexture("items", "img/items.png");
		item = i;
		pos = new Vector2f(x, y);
		start = new Vector2f(x, y);
	}
	
	public void setItem(Item i) {
		item = i;
	}
	
	public void draw() {
		if (item != null) {
			GL11.glPushMatrix();
			GL11.glTranslatef(pos.x, pos.y, 0);
			item.draw(false, 0);
			GL11.glPopMatrix();
		}
	}
	
	public float getSX() {
		return start.x;
	}
	
	public float getSY() {
		return start.y;
	}
	
	public Item getItem() {
		return item;
	}
	
	public void setPos(int x, int y) {
		pos.x = x;
		pos.y = y;
	}
	
	public boolean over(int x, int y) {
		if (x >= pos.x && x < pos.x + 48) {
			if (y >= pos.y && y <= pos.y + 48) {
				return true;
			}
		}
		return false;
	}
}
