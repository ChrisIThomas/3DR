package com.threedr.thomasci;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Game {
	private final ArrayList<Entity> entities;
	private final ArrayList<EntityDecoration> decorations;
	private final ArrayList<Activator> activators;
	private final ArrayList<Particle> particles;
	private Entity selEnt;
	private Controller controller;
	private Level level;
	private final GUI gui;
	private int levelNum;
	private final String folder;
	private final GameSaver saver;
	private final MenuNext menu0;
	private final MenuFinish menu1;
	private boolean nextMenu, finishMenu, finished;
	
	public Game(String loc, int lev, boolean load) {
		levelNum = 1;
		if (lev != -1) levelNum = lev;
		entities = new ArrayList<Entity>();
		decorations = new ArrayList<EntityDecoration>();
		activators = new ArrayList<Activator>();
		particles = new ArrayList<Particle>();
		selEnt = null;
		saver = new GameSaver();
		Object[] dat = saver.loadGame();
		
		if (!load) {
			dat = new Object[0];
			folder = loc;
		} else {
			folder = dat[0].toString();
			levelNum = Integer.parseInt(dat[1].toString());
		}
		loadLevel(getLevName(), dat);
		
		gui = new GUI(this);
		menu0= new MenuNext(this);
		menu1 = new MenuFinish(this);
		nextMenu = false;
		finishMenu = false;
	}
	
	public void loadLevel(String lname, Object[] pdat) {
		entities.clear();
		decorations.clear();
		activators.clear();
		particles.clear();
		
		level = new Level(lname);
		
		String[] data = level.getData();
		
		String[] loc = data[0].split(";");
		if (controller == null) {
			EntityPlayer player = new EntityPlayer(-Integer.parseInt(loc[0]), -Integer.parseInt(loc[1]), this);
			if (pdat.length != 0) {
				//set the player's health and money
				player.setHealth(Integer.parseInt(pdat[2].toString()), Integer.parseInt(pdat[3].toString()));
				Inventory inv = Inventory.createInventory(pdat[5].toString());
				player.setInventory(inv);
				player.setMoney(Integer.parseInt(pdat[4].toString()));
			}
			controller = new Controller(player);
			player.setController(controller);
			entities.add(player);
		} else {
			controller.getEntity().setPos(Integer.parseInt(loc[0]), 0, Integer.parseInt(loc[1]));
			controller.getEntity().resetVel();
			entities.add(controller.getEntity());
		}
		//add the spawn ladder
		entities.add(new EntityLadder(-Integer.parseInt(loc[0]), -Integer.parseInt(loc[1]), this, true));
		//add the exit ladder
		loc = data[1].split(";");
		entities.add(new EntityLadder(-Integer.parseInt(loc[0]), -Integer.parseInt(loc[1]), this, false));
		
		ArrayList<EDat> ed = level.getEnts(lname);
		for (int i = 0; i < ed.size(); i++) {
			Entity e = EDat.setupEnt(ed.get(i).getType(), ed.get(i).getData(), this);
			if (e instanceof EntityDecoration) {
				decorations.add((EntityDecoration) e);
			} else {
				entities.add(e);
			}
		}
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) instanceof EntityActivatorObject) {
				activators.add(new Activator(((EntityActivatorObject) entities.get(i)).getName(), ((EntityActivatorObject) entities.get(i)).isOnce()));
				entities.remove(i);
				i--;
			}
		}
		
		//setup entity data
		String name = "";
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) instanceof EntityData) {
				name = ((EntityData) entities.get(i)).getName();
				for (int j = 0; j < entities.size(); j++) {
					if (entities.get(j) instanceof EntityItem) {
						if (((EntityItem) entities.get(j)).getItem().getIDName().equals(name)) {
							Object o = ((EntityData) entities.get(i)).getData();
							if (o instanceof Enchantment) {
								Enchantment e = (Enchantment) o;
								if (((EntityItem) entities.get(j)).getItem() instanceof ItemWearable) e.setDefensive(true);
								((EntityItem) entities.get(j)).getItem().getEnchantments().addEnchantment(e);
							} else if (o instanceof Material) {
								Item item = ((EntityItem) entities.get(j)).getItem();
								if (item instanceof ItemWeapon) {
									((ItemWeapon) item).setMaterial((Material) o);
								} else if (item instanceof ItemArmour) {
									((ItemArmour) item).setMaterial((Material) o);
								}
							} else if (o instanceof Vector3f) {
								Vector3f col = (Vector3f) o;
								((EntityItem) entities.get(j)).getItem().setCol(col.x, col.y, col.z);
							} else if (o instanceof Vector2f) {
								Vector2f dat = (Vector2f) o;
								if (((EntityItem) entities.get(j)).getItem() instanceof ItemStaff) {
									((ItemStaff) ((EntityItem) entities.get(j)).getItem()).setType((int) dat.x);
									((ItemStaff) ((EntityItem) entities.get(j)).getItem()).setVal((int) dat.y);
								} else if (((EntityItem) entities.get(j)).getItem() instanceof ItemWand) {
									((ItemWand) ((EntityItem) entities.get(j)).getItem()).setType((int) dat.x);
									((ItemWand) ((EntityItem) entities.get(j)).getItem()).setVal((int) dat.y);
								} else {
									((ItemPotion) ((EntityItem) entities.get(j)).getItem()).setType((int) dat.x);
									((ItemPotion) ((EntityItem) entities.get(j)).getItem()).setVal((int) dat.y);
								}
							} else if (o instanceof Integer) {
								int type = ((EntityData) entities.get(i)).getType();
								switch (type) {
									case 3:
										((EntityItem) entities.get(j)).getItem().setMoney((Integer) o);
										break;
									case 4:
										((EntityItem) entities.get(j)).getItem().setCount((Integer) o);
										break;
								}
							}
						}
					} else if (entities.get(j) instanceof EntityDoor) {
						if (((EntityDoor) entities.get(j)).getActivName().equals(name)) {
							Object o = ((EntityData) entities.get(i)).getData();
							if (o instanceof Vector3f) {
								Vector3f col = (Vector3f) o;
								((EntityDoor) entities.get(j)).setCol(col.x, col.y, col.z);
							}
						}
					}
				}
				entities.remove(i);
				i--;
			}
		}
		
		//setup the entities (check for activators, etc.
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).setup();
		}
		
		//add random things to the level (chance to add money to level)
		for (int i = 0; i < 100; i++) {
			float x = 0;
			float z = 0;
			while (true) {
				x = (float) Math.random() * level.getWidth() * 2;
				z = (float) Math.random() * level.getDepth() * 2;
				int tile = (int) level.getTile((int) x / 2, (int) z / 2);
				if (tile == 0 || tile == 3 || tile == 4 || tile == 7) {
					if (Math.random() <= 0.1) entities.add(new EntityItem(-x, -z, this, Item.newItem(30, "Def")));
					else {
						if (tile == 3) decorations.add(new EntityDecoration(-x, -z, this, (int) Math.floor(Math.random() * 2)));
						else if (tile == 0) decorations.add(new EntityDecoration(-x, -z, this, (int) Math.floor(Math.random() * 2) + 2));
						else if (tile == 4) decorations.add(new EntityDecoration(-x, -z, this, (int) Math.floor(Math.random() * 2) + 4));
						else if (tile == 7) {
							decorations.add(new EntityDecoration(-x, -z, this, (int) Math.floor(Math.random() * 2) + 6));
							i++;
						}
					}
					break;
				}
			}
		}
	}
	
	public void finishLevel() {
		nextMenu = true;
	}
	
	public void finishGame() {
		finishMenu = true;
	}
	
	public void nextLevel() {
		levelNum++;
		nextMenu = false;
		loadLevel(getLevName(), new Object[0]);
	}
	
	public void saveGame() {
		saver.saveGame(folder, levelNum + 1, (EntityPlayer) controller.getEntity());
		quit();
	}
	
	public void exitGame() {
		quit();
	}
	
	public float getHeight(int x, int z) {
		return level.getHeight(x, z);
	}
	
	public float getRoof(int x, int z) {
		float r = level.getRoof(x, z);
		if (r <= 0) r = 1000;
		return r;
	}
	
	public float getTile(int x, int z) {
		return level.getTile(x, z);
	}
	
	public int getWidth() {
		return level.getWidth();
	}
	
	public int getDepth() {
		return level.getDepth();
	}
	
	public Controller getController() {
		return controller;
	}
	
	public void update() {
		if (finishMenu) {
			menu1.update();
		} else if (nextMenu) {
			menu0.update();
		} else {
			controller.update();
			if (((EntityLiving) controller.getEntity()).getHealth() > 0)
				gui.update();
			for (int i = 0; i < entities.size(); i++) {
				if (entities.get(i).shouldDestroy()) {
					if (entities.get(i).shouldDrop()) {
						if (entities.get(i) instanceof EntityProjectile) {
							if (((EntityProjectile) entities.get(i)).getType() == 0) {
								Entity e = entities.get(i);
								entities.add(new EntityItem(e.getX(), e.getZ(), this, Item.newItem(27, "Def")));
							}
						}
					}
					entities.remove(i);
					i--;
				}
				entities.get(i).update();
			}
		}
	}
	
	public void tick() {
		if (!nextMenu) {
			for (int i = 0; i < entities.size(); i++) {
				entities.get(i).tick();
			}
			for (int i = 0; i < decorations.size(); i++) {
				decorations.get(i).tick();
			}
			for (int i = 0; i < particles.size(); i++) {
				particles.get(i).tick();
				if (particles.get(i).shouldDestroy()) {
					particles.remove(i);
					i--;
				}
			}
			ALLoader.setListenPos(controller.getEntity().getPos());
			//ALLoader.setListenOri(controller.getEntity().getRot());
		}
	}
	
	public Vector3f collides(Entity e, float r) {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) == e) continue;
			if (entities.get(i) instanceof EntityLiving ||
					entities.get(i) instanceof EntityObstacle||
					entities.get(i) instanceof EntityCommander ||
					entities.get(i) instanceof EntityPressurePlate) {
				Vector3f vec = entities.get(i).collides(e, r);
				if (vec != null) {
					if (e instanceof EntityProjectile) {
						if (entities.get(i) instanceof EntityLiving) {
							if (((EntityProjectile) e).getType() == 0) {
								((EntityLiving) entities.get(i)).damage(((EntityProjectile) e).getDamage(), ((EntityProjectile) e).getType());
							} else {
								((EntityLiving) entities.get(i)).addEffect(new Effect(((EntityProjectile) e).getType() - 1, ((EntityProjectile) e).getDamage()));
							}
						}
					} else if (e instanceof EntityItem) {
						if (((EntityItem) e).getItem() instanceof ItemPotion) {
							if (e.getVel().getZ() > 0.2f || e.getVel().getZ() < -0.2f) {
								if (entities.get(i) instanceof EntityLiving) {
									((ItemPotion) ((EntityItem) e).getItem()).affect(entities.get(i));
									e.destroy(true);
								}
							}
						}
					}
					return vec;
				}
			}
		}
		return null;
	}
	
	public boolean collides(Entity e, float x, float z, float r) {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) == e) continue;
			if (entities.get(i) instanceof EntityObstacle) {
				if (entities.get(i).collides(x, z, r)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void draw3d() {
		selEnt = pick3d();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		controller.look(!TDR.inMenu() && !nextMenu);
		float lx = controller.getEntity().getX();
		float lz = controller.getEntity().getZ();
		level.draw(true, lx, lz);
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).draw(lx, lz);
		}
		for (int i = 0; i < decorations.size(); i++) {
			decorations.get(i).draw(lx, lz);
		}
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		for (int i = 0; i < particles.size(); i++) {
			particles.get(i).draw(lx, lz);
		}
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public void draw2d() {
		if (finishMenu) {
			menu1.draw();
		} else if (nextMenu) {
			menu0.draw();
		} else {
			gui.draw();
		}
	}
	
	public Entity getSelEnt() {
		return selEnt;
	}
	
	private Entity pick3d() {
		GL11.glPushMatrix();
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_FOG);
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glLoadIdentity();
		controller.look(true);
		
		GL11.glColor3f(0.0f, 0.0f, 0.0f);
		level.draw(false, controller.getEntity().getX(), controller.getEntity().getZ());
		
		ArrayList<Vector3f> col = new ArrayList<Vector3f>();
		float r = 0, g = 0, b = 0;
		float v = 1.0f / 255.0f;
		for (int i = 0; i < entities.size(); i++) {
			r++;
			if (r >= 255) g++;
			if (g >= 255) b++;
			
			GL11.glColor3f(r * v, g * v, b * v);
			entities.get(i).drawBox(controller.getEntity().getX(), controller.getEntity().getZ());
			col.add(new Vector3f(r, g, b));
			
			if (r >= 255) r = 0;
			if (g >= 255) g = 0;
		}
		
		ByteBuffer pixels = BufferUtils.createByteBuffer(16);
		GL11.glReadPixels(Mouse.getX(), Mouse.getY(), 1, 1, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, pixels);
		int num = -1;
		if ((pixels.get(0) & 0xff) != 0 || (pixels.get(1) & 0xff) != 0 || (pixels.get(2) & 0xff) != 0) {
			for (int i = 0; i < entities.size(); i++) {
				if (col.get(i).x == (pixels.get(0) & 0xff) && col.get(i).y == (pixels.get(1) & 0xff) && col.get(i).z == (pixels.get(2) & 0xff)) {
					num = i;
					break;
				}
			}
		}
		/*if (pixels.get(0) != 0 || pixels.get(1) != 0 || pixels.get(2) != 0) {
			for (int i = 0; i < col.size(); i++) {
				if ((int) col.get(i).x == pixels.get(0) && (int) col.get(i).y == pixels.get(1) && (int) col.get(i).z == pixels.get(2)) {
					num = i;
					break;
				}
			}
		}*/
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_FOG);
		
		GL11.glPopMatrix();
		if (num != -1) return entities.get(num);
		return null;
	}
	
	public void removeEnt(Entity e) {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) == e) {
				entities.remove(i);
			}
		}
	}
	
	public void addEnt(Entity e) {
		entities.add(e);
	}
	
	public Activator getActivator(String name) {
		if (name.equals("")) return null;
		for (int i = 0; i < activators.size(); i++) {
			if (activators.get(i).getName().equals(name)) {
				return activators.get(i);
			}
		}
		return null;
	}
	
	private String getLevName() {
		String levName = "lev/" + folder + "/L" + (levelNum<10?"0":"") + levelNum;
		return levName;
	}
	
	public void addParticle(Particle p) {
		if (TDR.funky) p.setCol(new Vector3f((float) Math.random(), (float) Math.random(), (float) Math.random()));
		particles.add(p);
	}
	
	public boolean inNextMenu() {
		return nextMenu;
	}
	
	public boolean inFinishMenu() {
		return finishMenu;
	}
	
	private void quit() {
		finished = true;
	}
	
	public boolean shouldFinish() {
		return finished;
	}
	
	public void alertDwarf() {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) instanceof EntityDwarf) {
				((EntityDwarf) entities.get(i)).alert();
			}
		}
	}
	
	public boolean isDwarfAlive() {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) instanceof EntityDwarf) {
				if (((EntityDwarf) entities.get(i)).getHealth() > 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void alertStatues() {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) instanceof EntityStatue) {
				((EntityStatue) entities.get(i)).alert();
			}
		}
	}
	
	public int getLevelNum() {
		return levelNum;
	}
}
