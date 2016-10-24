package com.threedr.thomasci;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GUIWeapon extends GUIElement implements MouseListener {
	private Item item;
	private float off, swing, ready; //ready is used for the secondary action
	private boolean rmb;
	private int bowSound;
	
	public GUIWeapon(GUI g) {
		super(g);
		tex = TDR.getTexture("items", "img/items.png");
		swing = -31;
		gui.addListener(this);
		bowSound = -10;
	}
	
	public void setItem(Item i) {
		if (i != item) {
			item = i;
			swing = -31;
			off = 0;
		}
	}
	
	public void update() {
		if (!Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
			if (Mouse.isButtonDown(0)) {
				if (swing == -31) {
					off += 0.5f;
				}
			} else {
				if (off > 0) {
					Entity e = gui.getGame().getController().getEntity();
					ALLoader.addSound(9, (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] {e.getX(), e.getY(), e.getZ()}).rewind());
					swing = -off;
					off = 0;
				}
			}
			if (Mouse.isButtonDown(1)) {
				if (item != null) {
					if (item.getID() == 26) { //are we holding a bow
						if (ready == 0) {
							Entity e = gui.getGame().getController().getEntity();
							if (e instanceof EntityLiving) {
								int slot = ((EntityLiving) e).getInventory().hasItem(27);
								if (slot != -1) {
									ready++;
									if (bowSound == -10) {
										bowSound = ALLoader.addSound(7, (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] {e.getX(), e.getY(), e.getZ()}).rewind());
									}
								}
							}
						} else {
							ready++;
						}
					} else if (item.getID() == 24) {
						((ItemWand) item).charge();
					} else if (item.getID() == 25) {
						((ItemStaff) item).charge();
					}
				}
				rmb = true;
			} else {
				if (item != null) {
					if (ready >= 10) {
						if (item.getID() == 26) { //are we trying to fire a bow
							Entity e = gui.getGame().getController().getEntity();
							int slot = ((EntityLiving) e).getInventory().hasItem(27);
							if (slot != -1) {
								((EntityLiving) e).getInventory().getItem(slot).addCount(-1);
								Entity p = gui.getGame().getController().getEntity();
								float fx = p.pos.x - ((p.rad + 0.1f) * (float) (Math.sin(Math.toRadians(p.rot.y)) * Math.cos(Math.toRadians(p.rot.z))));
								float fz = p.pos.z + ((p.rad + 0.1f) * (float) (Math.cos(Math.toRadians(p.rot.y)) * Math.cos(Math.toRadians(p.rot.z))));
								gui.getGame().addEnt(new EntityProjectile(fx, p.pos.y - 1.25f, fz, p.rot.y, p.rot.z, ready / 100.0f * 2, 0, false, gui.getGame()));
								//play sound
								ALLoader.stopSound(bowSound);
								ALLoader.addSound(8, (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] {p.getX(), p.getY(), p.getZ()}).rewind());
								bowSound = -10;
							}
						}
					}
				}
				
				if (rmb) {
					if (item != null) {
						if (item.getID() == 24) {
							if (((ItemWand) item).getCharge() >= 120) {
								Entity e = gui.getGame().getController().getEntity();
								if (e instanceof EntityLiving) {
									Entity p = gui.getGame().getController().getEntity();
									float fx = p.pos.x - ((p.rad + 0.1f) * (float) (Math.sin(Math.toRadians(p.rot.y)) * Math.cos(Math.toRadians(p.rot.z))));
									float fz = p.pos.z + ((p.rad + 0.1f) * (float) (Math.cos(Math.toRadians(p.rot.y)) * Math.cos(Math.toRadians(p.rot.z))));
									gui.getGame().addEnt(new EntityProjectile(fx, p.pos.y - 1.0f, fz, p.rot.y, p.rot.z, 1.0f, ((ItemWand) item).getType() + 1, true, gui.getGame()).setCol(item.getCol()));
									((ItemWand) item).resetCharge();
									swing = 0;
								}
							}
							((ItemWand) item).resetCharge();
						} else if (item.getID() == 25) {
							if (((ItemStaff) item).getCharge() >= 240) {
								Entity e = gui.getGame().getController().getEntity();
								if (e instanceof EntityLiving) {
									Entity p = gui.getGame().getController().getEntity();
									float fx = p.pos.x - ((p.rad + 0.1f) * (float) (Math.sin(Math.toRadians(p.rot.y)) * Math.cos(Math.toRadians(p.rot.z))));
									float fz = p.pos.z + ((p.rad + 0.1f) * (float) (Math.cos(Math.toRadians(p.rot.y)) * Math.cos(Math.toRadians(p.rot.z))));
									gui.getGame().addEnt(new EntityProjectile(fx, p.pos.y - 1.0f, fz, p.rot.y, p.rot.z, 1.0f, ((ItemStaff) item).getType() + 1, false, gui.getGame()).setCol(item.getCol()));
									((ItemStaff) item).resetCharge();
									swing = 0;
								}
							}
							((ItemStaff) item).resetCharge();
						}
					}
				}
				ready = 0;
				rmb = false;
			}
		}
		Item i = ((EntityLiving) gui.getGame().getController().getEntity()).getInventory().getItem(gui.getGame().getController().getSlot() + 8);
		int max = 0;
		if (i != null) {
			max = i.getSwing();
		}
		float aspeed = ((EntityLiving) gui.getGame().getController().getEntity()).getInventory().getASpeed() / 2.0f;
		ArrayList<Effect> list = ((EntityLiving) gui.getGame().getController().getEntity()).getEffects();
		for (int j = 0; j < list.size(); j++) {
			if (list.get(j).getType() == 7) aspeed += list.get(j).getVal() / 2.0f;
		}
		if (off > max) off = max;
		if (off < 0) off = 0;
		if (swing > -31) swing += (4 + i.getSpeed() + aspeed);
		if (swing > 60) swing = -31;
		if (ready < 0) ready = 0;
		if (ready > 30) ready = 30;
	}
	
	public void draw() {
		if (item != null) {
			if (ready == 0) {
				if (swing == -31) {
					GL11.glTranslatef(off, gui.getGame().getController().getBob() * 10 - off, 0);
					GL11.glRotatef(off * 1.5f, 0, 0, 1);
				} else {
					float swing = -this.swing;
					if (-swing > 30) swing = -swing - 60;
					GL11.glTranslatef(swing, gui.getGame().getController().getBob() * 10 - swing, 0);
					GL11.glRotatef(swing * 2, 1, 0, 0);
					GL11.glRotatef(swing * 1.5f, 0, 0, 1);
				}
			} else {
				GL11.glTranslatef(-20, 15, 0);
				GL11.glRotatef(50.0f, 0, 1, 0);
				GL11.glRotatef(-25.0f, 0, 0, 1);
			}
			item.draw(true, (int) ready / 10);
		}
	}
	
	
	public void action(Object o) {
		if (off != 0) {
			if (o instanceof EntityLiving) {
				if (!Mouse.isButtonDown(0)) {
					EntityLiving p = ((EntityLiving) gui.getGame().getController().getEntity());
					Item i = p.getInventory().getItem(gui.getGame().getController().getSlot() + 8);
					if (i != null) {
						EntityLiving e = ((EntityLiving) o);
						float range = 0.75f + i.getReach();
						if (i != null) {
							if (Math.abs(Math.sqrt(Math.pow(e.getX() - p.getX(), 2) + Math.pow(e.getZ() - p.getZ(), 2))) <= range * 2) {
								((EntityLiving) o).damage((int) (off / 10 + 1 + i.getDamage()), 0);
								//ALLoader.addSound(3, (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] {p.getX(), p.getY(), p.getZ()}).rewind());
								if (i.hasEnchantments()) {
									i.getEnchantments().affect(p, (EntityLiving) o);
								}
							}
						}
					}
				}
			}
		}
	}
}
