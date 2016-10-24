package com.threedr.thomasci;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GUI {
	private final Game game;
	private final GUIHotbar hotbar;
	private final GUIAlchemy alchemy;
	private final GUIItem[] items;
	private final GUIInventory inventory;
	private final GUIWeapon weapon;
	private final GUIButton btnInv;
	private final GUIHealthBar health;
	private final GUISight sight;
	private final GUICrosshair crosshair;
	private final GUIQuiver quiver;
	private GUIItem grabItem;
	private boolean mDown, inv;
	private float alpha;
	private String text; //remembers what text to display
	
	private ArrayList<MouseListener> mouseList;
	
	public GUI(Game g) {
		game = g;
		mouseList = new ArrayList<MouseListener>();
		hotbar = new GUIHotbar(this);
		alchemy = new GUIAlchemy(this);
		
		//first 8 slots - armor
		//next 4 slots - hot bar
		//next 10 slots - inventory
		//next 5 slots - alchemy
		items = new GUIItem[31];
		//Armour items
		int x = TDR.width / 2 - 300;
		items[0] = new GUIItem(this, null, x, 220);
		items[1] = new GUIItem(this, null, x, 160);
		items[2] = new GUIItem(this, null, x + 60, 160);
		items[3] = new GUIItem(this, null, x, 100);
		items[4] = new GUIItem(this, null, x + 60, 100);
		items[5] = new GUIItem(this, null, x, 40);
		items[6] = new GUIItem(this, null, x + 120, 160);
		items[7] = new GUIItem(this, null, x + 120, 100);
		//Hotbar items
		x = TDR.width / 2 - 122;
		for (int i = 0; i < 4; i++) {
			items[i + 8] = new GUIItem(this, null, x, 8);
			x += 60;
		}
		//Inventory items
		x = TDR.width / 2 - 108;
		for (int i = 0; i < 10; i++) {
			//items[i + 12] = new GUIItem(this, null, 256 + i * 60 - i / 5 * 300, 100 + i / 5 * 60);
			items[i + 12] = new GUIItem(this, null, x - i / 5 * 300, 100 + i / 5 * 60);
			x += 60;
		}
		//alchemy items
		/*items[22] = new GUIItem(this, null, 644, 92);
		items[23] = new GUIItem(this, null, 584, 160);
		items[24] = new GUIItem(this, null, 644, 160);
		items[25] = new GUIItem(this, null, 704, 160);
		items[26] = new GUIItem(this, null, 644, 228);*/
		x = TDR.width / 2 + 268;
		items[22] = new GUIItem(this, null, x, 92);
		items[23] = new GUIItem(this, null, x - 60, 160);
		items[24] = new GUIItem(this, null, x, 160);
		items[25] = new GUIItem(this, null, x + 60, 160);
		items[26] = new GUIItem(this, null, x, 228);

		x = TDR.width / 2 - 212;
		int y = TDR.height / 2 - 142;
		items[27] = new GUIItem(this, null, x, y);
		items[28] = new GUIItem(this, null, x + 60, y);
		items[29] = new GUIItem(this, null, x + 120, y);
		items[30] = new GUIItem(this, null, x + 180, y);
		
		grabItem = new GUIItem(this, null, 100, 100);
		
		weapon = new GUIWeapon(this);
		inventory = new GUIInventory(this);
		health = new GUIHealthBar(TDR.width / 2 - 194, 4, "", -1, this);
		sight = new GUISight(TDR.width / 2, TDR.height - 36, "", -1, this);
		inv = false;
		btnInv = new GUIButton(TDR.width / 2 + 138, 4, "inv", 0, this);
		alpha = 0.0f;
		text = "";
		crosshair = new GUICrosshair(this);
		quiver = new GUIQuiver(this);
	}
	
	public Game getGame() {
		return game;
	}
	
	public void update() {
		Entity e = game.getSelEnt();
		Entity p = game.getController().getEntity();
		int slot = game.getController().getSlot();
		hotbar.update();
		Inventory inv = ((EntityLiving) game.getController().getEntity()).getInventory();
		for (int i = 0; i < 31; i++) {
			items[i].setItem(inv.getItem(i));
		}
		
		//LEFT CLICK
		if (Mouse.isButtonDown(0)) {
			boolean useInv = false;
			if (Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
				//if we just clicked
				if (!mDown) {
					for (int i = 0; i < mouseList.size(); i++) {
						mouseList.get(i).action(e);
					}
					main: {
						if (btnInv.over(Mouse.getX(), TDR.height - Mouse.getY())) { //are we clicking a button
							if (grabItem.getItem() != null) {
								if (inv.putItem(grabItem.getItem()) == null) {
									grabItem.setItem(null);
								}
							} else {
								this.inv = !this.inv;
							}
							useInv = true;
						} else if (grabItem.getItem() == null && e != null) {
							if (e instanceof EntityItem) {
								if (Math.abs(Math.sqrt(Math.pow(e.getX() - p.getX(), 2) + Math.pow(e.getZ() - p.getZ(), 2))) <= 2.1) {
									int id = ((EntityItem) e).getItem().getID();
									if (id == 30) {
										inv.changeCoins(1);
									} else if (id == 31) {
										inv.changeCoins(((EntityItem) e).getItem().getMoney());
									} else {
										grabItem.setItem(((EntityItem) e).getItem());
										if (game.isDwarfAlive()) {
											if (inv.getCoins() >= grabItem.getItem().getMoney()) {
													inv.changeCoins(-grabItem.getItem().getMoney());
											} else {
												game.alertDwarf();
											}
										}
										grabItem.getItem().setMoney(0);
									}
									game.removeEnt(e);
									break main;
								}
							}
						}
						if (grabItem.getItem() == null) { //if we just clicked on an item
							for (int i = 0; i < items.length; i++) {
								if (!this.inv) {
									if (i < 8) continue;
									if (i > 11) break;
								}
								if (items[i].over(Mouse.getX(), TDR.height - Mouse.getY())) {
									if (items[i].getItem() != null) {
										grabItem.setItem(items[i].getItem());
										inv.setItem(null, i);
										if (i == 26) {
											inv.setItem(null, 22);
											for (int j = 0; j < 3; j++) {
												if (inv.getItem(23 + j) != null) {
													inv.getItem(23 + j).addCount(-1);
													if (inv.getItem(23 + j).getCount() <= 0) inv.setItem(null, 23 + j);
												}
											}
										}
										if (i >= 22 && i <= 26) setupPotion(inv);
									}
									useInv = true;
									break;
								}
							}
						} else { //if we have an item in our hand and we want to place it
							int loc = -1;
							for (int i = 0; i < items.length; i++) {
								if ((i >= 8 && i <= 11) || this.inv) {
									if (items[i].over(Mouse.getX(), TDR.height - Mouse.getY())) {
										loc = i;
										break;
									}
								}
							}
							
							if (loc != -1) {
								if (canPut(grabItem.getItem(), loc)) { //if we can put it in that slot, if we can't then don't do anything
									Item oldItem = items[loc].getItem();
									if (oldItem != null) {
										if (grabItem.getItem().getID() == oldItem.getID()) {
											int amount = items[loc].getItem().addCount(grabItem.getItem().getCount());
											if (amount != 0) {
												grabItem.getItem().setCount(amount);
											} else {
												grabItem.setItem(null);
											}
										} else {
											/*if (loc < 22) {
												inv.setItem(grabItem.getItem(), loc);
											} else {
												items[loc].setItem(grabItem.getItem());
											}*/
											inv.setItem(grabItem.getItem(), loc);
											if (loc >= 22 && loc <= 26) setupPotion(inv);
											grabItem.setItem(oldItem);
										}
									} else {
										/*if (loc < 22) {
											inv.setItem(grabItem.getItem(), loc);
										} else {
											items[loc].setItem(grabItem.getItem());
											setupPotion();
										}*/
										inv.setItem(grabItem.getItem(), loc);
										if (loc >= 22 && loc <= 26) setupPotion(inv);
										grabItem.setItem(null);
									}
								}
							} else { //throwing the item
								float nx = (float) (p.getX() - 0.3f * Math.sin(Math.toRadians(p.getRot())));
								float nz = (float) (p.getZ() + 0.3f * Math.cos(Math.toRadians(p.getRot())));
								
								EntityItem i = new EntityItem(nx, nz, game, grabItem.getItem());
								i.setRot(p.getRot());
								i.setPosY(p.getY());
								i.setVelZ(Mouse.getY() / 2500.0f);
								float vy = -Mouse.getY() / 2500.0f;
								if (vy < -0.25f) vy = -0.25f;
								i.setVelY(vy);
								game.addEnt(i);
								grabItem.setItem(null);
								useInv = true;
							}
							useInv = true;
						}
					}
				}
			}
			//trying to interact with the world
			if (!useInv) {
				if (!mDown) {
					if (!text.equals("")) {
						setText("");
					}
					if (e != null) {
						if (e instanceof EntityWriting) {
							if (text.equals("")) {
								if (Math.abs(Math.sqrt(Math.pow(e.getX() - p.getX(), 2) + Math.pow(e.getZ() - p.getZ(), 2))) <= 1) {
									setText(((EntityWriting) e).getText());
								}
							}
						} else {
							if (Math.abs(Math.sqrt(Math.pow(e.getX() - p.getX(), 2) + Math.pow(e.getZ() - p.getZ(), 2))) <= 2) {
								e.activate(items[game.getController().getSlot() + 8].getItem());
							}
						}
					}
				}
			}
			
			mDown = true;
		} else if (Mouse.isButtonDown(1)) {
			/*--------------\
			|  RIGHT CLICK  |
			\--------------*/
			if (!Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
				Item i = items[slot + 8].getItem();
				if (i != null) {
					if (i instanceof ItemPotion) {
						((ItemPotion) i).affect(p);
					}
				}
			}
		} else {
			//activate all listeners
			if (mDown) {
				for (int i = 0; i < mouseList.size(); i++) {
					mouseList.get(i).action(e);
				}
			}
			mDown = false;
		}
		if (!Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
			if (grabItem.getItem() != null) {
				float nx = (float) (p.getX() - 0.2f * Math.sin(Math.toRadians(p.getRot())));
				float nz = (float) (p.getZ() + 0.2f * Math.cos(Math.toRadians(p.getRot())));
				
				EntityItem i = new EntityItem(nx, nz, game, grabItem.getItem());
				i.setRot(p.getRot());
				i.setPosY(p.getY());
				i.setVelZ(0.05f);
				i.setVelY(-0.05f);
				game.addEnt(i);
				grabItem.setItem(null);
			}
		}
		
		if (grabItem.getItem() != null) {
			grabItem.setPos(Mouse.getX() - 24, TDR.height - Mouse.getY() - 24);
			if (btnInv.over(Mouse.getX(), TDR.height - Mouse.getY())) {
				this.inv = true;
			}
		}
		
		if (!text.equals("")) {
			if (e == null) {
				setText("");
			} else if (!(e instanceof EntityWriting)) {
				setText("");
			} else if (!((EntityWriting) e).getText().equals(text)) {
				setText("");
			} else if (Math.abs(Math.sqrt(Math.pow(e.getX() - p.getX(), 2) + Math.pow(e.getZ() - p.getZ(), 2))) > 1) {
				setText("");
			}
		}
		
		if (!inv.hasAlchemy()) {
			for (int i = 0; i < 4; i++) {
				if (inv.getItem(i + 22) != null) {
					dropItem(inv.getItem(i + 22), p);
					inv.setItem(null, i + 22);
				}
			}
			inv.setItem(null, 26);
		}
		if (!inv.hasQuiver()) {
			for (int i = 0; i < 4; i++) {
				if (inv.getItem(i + 27) != null) {
					dropItem(inv.getItem(i + 27), p);
					inv.setItem(null, i + 27);
				}
			}
		}
		
		sight.setSight((int) ((EntityLiving) p).getVis());
		
		weapon.setItem(items[slot + 8].getItem());
		weapon.update();
	}
	
	public void dropItem(Item item, Entity e) {
		float nx = (float) (e.getX() - 0.2f * Math.sin(Math.toRadians(e.getRot())));
		float nz = (float) (e.getZ() + 0.2f * Math.cos(Math.toRadians(e.getRot())));
		
		EntityItem i = new EntityItem(nx, nz, game, item);
		i.setRot(e.getRot());
		i.setPosY(e.getY());
		i.setVelZ(0.05f);
		i.setVelY(-0.05f);
		game.addEnt(i);
	}
	
	public void addListener(Object l) {
		if (l instanceof MouseListener) {
			mouseList.add((MouseListener) l);
		}
	}
	
	public void setText(String t) {
		text = t;
	}
	
	public void displayText() {
		if (!text.equals("")) {
			if (alpha < 0.6f) alpha += 0.025f;
			if (alpha > 0.6f) alpha = 0.6f;
		} else {
			if (alpha > 0.0f) alpha -= 0.025f;
			if (alpha < 0.0f) alpha = 0.0f;
		}
		if (alpha != 0.0f) {
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glColor4f(0.0f, 0.0f, 0.0f, alpha);
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2f(0, 0);
				GL11.glVertex2f(0, TDR.height);
				GL11.glVertex2f(TDR.width, TDR.height);
				GL11.glVertex2f(TDR.width, 0);
			GL11.glEnd();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glTranslatef(TDR.width / 2, TDR.height / 2, 0);
			Text.drawText("§0" + text, 2, true);
			GL11.glPopMatrix();
		}
	}
	
	public void draw() {
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		if (((EntityLiving) game.getController().getEntity()).getHealth() > 0) {
			GL11.glPushMatrix();
			crosshair.draw();
			hotbar.draw();
			health.draw();
			sight.draw();
			if (inv) {
				inventory.draw();
				if (((EntityLiving) game.getController().getEntity()).getInventory().hasQuiver()) quiver.draw();
				if (((EntityLiving) game.getController().getEntity()).getInventory().hasAlchemy()) alchemy.draw();
			}
			btnInv.draw();
			int mouse = -1;
			for (int i = 0; i < items.length; i++) {
				if ((i <= 7 || i >= 12) && !inv) continue;
				if (items[i].getItem() != null) {
					items[i].draw();
					if (mouse == -1) {
						if (Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
							if (items[i].over(Mouse.getX(), TDR.height - Mouse.getY())) {
								mouse = i;
							}
						}
					}
				}
			}
			GL11.glPopMatrix();
			if (mouse > -1 && grabItem.getItem() == null) {
				GL11.glPushMatrix();
				GL11.glTranslatef(Mouse.getX() + 16, TDR.height - Mouse.getY(), 0);
				Text.drawText("§0" + items[mouse].getItem().getName() + "\n" + items[mouse].getItem().listEnchantments(), 2, false);
				GL11.glPopMatrix();
			}
			Entity e = game.getSelEnt();
			
			//drawing the weapon
			GL11.glPushMatrix();
			int scale = 12;
			GL11.glScalef(scale, scale, 0);
			//GL11.glTranslatef(TDR.width / scale - 240 / scale, TDR.height / scale - 440 / scale, 0);
			GL11.glTranslatef(TDR.width / scale - 300 / scale, TDR.height / scale - 600 / scale, 0);
			GL11.glRotatef(30, 0, 1, 0);
			GL11.glRotatef(20, 0, 0, 1);
			weapon.draw();
			GL11.glPopMatrix();
			
			//draw number of coins
			if (inv) {
				GL11.glPushMatrix();
				GL11.glTranslatef(TDR.width / 2 + 128, TDR.height / 2 - 150, 0);
				Text.drawText("§4Coins:\n" + ((EntityLiving) game.getController().getEntity()).getInventory().getCoins(), 2, true);
				GL11.glPopMatrix();
			}
			EntityLiving p = (EntityLiving) game.getController().getEntity();
			
			if (grabItem.getItem() == null) {
				if (e != null) {
					if (e instanceof EntityItem) {
						if (Math.abs(Math.sqrt(Math.pow(e.getX() - p.getX(), 2) + Math.pow(e.getZ() - p.getZ(), 2))) <= 2.1) {
							GL11.glPushMatrix();
							GL11.glTranslatef(Mouse.getX() + 16, TDR.height - Mouse.getY(), 0);
							Text.drawText("§0" + ((EntityItem) e).getItem().getName(), 2, false);
							GL11.glPopMatrix();
						}
					}
				} else {
					if (health.over(Mouse.getX(), TDR.height - Mouse.getY())) {
						GL11.glPushMatrix();
						GL11.glTranslatef(Mouse.getX() + 16, TDR.height - Mouse.getY(), 0);
						Text.drawText("§0" + p.getHealth() + "/" + p.getMaxHealth(), 2, false);
						GL11.glPopMatrix();
					}
				}
			} else {
				GL11.glPushMatrix();
				grabItem.draw();
				GL11.glPopMatrix();
			}
			
			//draw messages
			displayText();
		} else {
			if (alpha < 0.75f) alpha += 0.0025f;
			if (alpha > 0.75f) alpha = 0.75f;
			if (alpha != 0.0f) {
				GL11.glPushMatrix();
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glColor4f(0.75f, 0.0f, 0.0f, alpha);
				GL11.glBegin(GL11.GL_QUADS);
					GL11.glVertex2f(0, 0);
					GL11.glVertex2f(0, TDR.height);
					GL11.glVertex2f(TDR.width, TDR.height);
					GL11.glVertex2f(TDR.width, 0);
				GL11.glEnd();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glTranslatef(TDR.width / 2, TDR.height / 2, 0);
				Text.drawText("§1You are Dead.", 6, true);
				GL11.glPopMatrix();
			}
		}
	}
	
	private boolean canPut(Item i, int loc) {
		if (loc != -1) {
			if (loc < 8) {
				if (!(i instanceof ItemWearable)) {
					return false;
				} else {
					if (((ItemWearable) i).getSlot() != loc) return false;
				}
			} else if (loc == 22) {
				base: {
					if (i instanceof ItemPotion) {
						int type = ((ItemPotion) i).getType();
						if (type == 1) {
							break base;
						}
					}
					return false;
				}
			} else if (loc == 26) {
				return false;
			} else if (loc > 26) {
				if (i.getID() != 27) return false;
			}
		}
		return true;
	}
	
	private void setupPotion(Inventory inv) {
		if (inv.getItem(22) != null) {
			int[] arr = new int[0];
			if (inv.getItem(23) != null) {
				arr = new int[] {inv.getItem(23).getID()};
			}
			if (inv.getItem(24) != null) {
				arr = incArray(arr);
				arr[arr.length - 1] = inv.getItem(24).getID();
			}
			if (inv.getItem(25) != null) {
				arr = incArray(arr);
				arr[arr.length - 1] = inv.getItem(25).getID();
			}
			
			int potion = Recipe.hasIngredients(arr);
			if (potion != -1) {
				inv.setItem(Item.newPotion(potion, 5), 26);
			} else {
				inv.setItem(null, 26);
			}
		} else {
			inv.setItem(null, 26);
		}
	}
	
	private int[] incArray(int[] arr) {
		if (arr.length > 0) {
			int[] temp = new int[arr.length + 1];
			
			for (int i = 0; i < arr.length; i++) {
				temp[i] = arr[i];
			}
			
			return temp;
		} else return new int[1];
	}
}
