package com.threedr.thomasci;

import java.util.Arrays;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class EntityEnemy extends EntityLiving {
	protected Entity target;
	protected Vector2f[] point;
	protected boolean found;
	protected int deadAng;
	protected int blood = 25;
	protected int attack, dam;
	
	public EntityEnemy(float x, float z, float rot, Game g) {
		super(x, z, g);
		/*inventory.setItem(Item.newItem(0, "Def"), 0);
		inventory.setItem(Item.newItem(1, "Def"), 1);
		inventory.setItem(Item.newItem(3, "Def"), 3);
		inventory.setItem(Item.newItem(10, "Def"), 8);*/
		point = new Vector2f[0];
		found = false;
		target = game.getController().getEntity();
		this.rot.y = rot;
		maxSpeed = 0.05f;
		tex = TDR.getTexture("character", "img/character.png");
		dam = 1;
		dam += g.getLevelNum() / 2;
	}
	
	public void update() {
		if (game.getTile((int) (-pos.x / 2), (int) (-pos.z / 2)) == 1 || attacking) {
			if (vel.z > 0.05f) vel.z = 0.05f;
			if (vel.z < -0.05f) vel.z = -0.05f;
			if (vel.x > 0.05f) vel.x = 0.05f;
			if (vel.x < -0.05f) vel.x = -0.05f;
		}
		inventory.update();
	}
	
	protected boolean hitWall(float x, float y, float z) {
		if (game.getHeight((int) ((-x - 0.25f) / 2), (int) -z / 2) > -y + 0.3f) {
			return true;
		} else if (game.getHeight((int) ((-x + 0.25f) / 2), (int) -z / 2) > -y + 0.3f) {
			return true;
		}
		
		if (game.getHeight((int) -x / 2, (int) ((-z - 0.25f) / 2)) > -y + 0.3f) {
			return true;
		} else if (game.getHeight((int) -x / 2, (int) ((-z + 0.25f) / 2)) > -y + 0.3f) {
			return true;
		}
		
		
		if (game.getRoof((int) ((-x - 0.25f) / 2), (int) -z / 2) < -y + height) {
			return true;
		} else if (game.getRoof((int) ((-x + 0.25f) / 2), (int) -z / 2) < -y + height) {
			return true;
		}
		
		if (game.getRoof((int) -x / 2, (int) ((-z - 0.25f) / 2)) < -y + height) {
			return true;
		} else if (game.getRoof((int) -x / 2, (int) ((-z + 0.25f) / 2)) < -y + height) {
			return true;
		}
		return false;
	}
	
	//if fov is true then we will only see the point if it is in font of the enemy otherwise we will check if there is a valid path straight to the x and z position
	public boolean canSee(float x, float z, boolean fov) {
		float r = 0;
		boolean see = true;
		if (fov) {
			r = (float) Math.toDegrees(Math.atan2(pos.x - x, pos.z - z));
			r += 180;
			int ang = (int) (r - rot.y + 225);
			if (ang < 0) ang += 360;
			else if (ang > 360) ang -= 360;
			if (ang >= 135 && ang <= 315) see = false;
		}
		
		if (Math.abs(pos.x - x) >= 20) return false;
		if (Math.abs(pos.z - z) >= 20) return false;
		
		float cx = pos.x;
		float cy = pos.y;
		float cz = pos.z;
		float dist = 0.0f;
		r = (float) Math.toDegrees(Math.atan2(pos.x - x, pos.z - z));
		
		if (see) {
			while (dist < 20.0f) {
				dist += rad;
				cx -= rad * Math.sin(Math.toRadians(r));
				cz -= rad * Math.cos(Math.toRadians(r));
				
				if (hitWall(cx, cy, cz)) return false;
				if (game.collides(this, cx, cz, rad * 2)) return false;
				
				if ((int) cx == (int) x && (int) cz == (int) z) return true;
				
				cy = -game.getHeight((int) -cx / 2, (int) -cz / 2);
			}
		} else if (!see) {
			while (dist < 20.0f) {
				dist += rad;
				cx -= rad * Math.sin(Math.toRadians(r));
				cz -= rad * Math.cos(Math.toRadians(r));
				
				if (hitWall(cx, cy, cz)) return false;
				if (game.collides(this, cx, cz, rad * 2)) return false;
				
				if ((int) cx == (int) x && (int) cz == (int) z) {
					see = true;
					break;
				}
				
				cy = -game.getHeight((int) -cx / 2, (int) -cz / 2);
			}
			if (see) {
				if (((EntityLiving) game.getController().getEntity()).getVis() >= 75) {
					System.out.println("SEEN");
					return true;
				}
			}
		}
		
		return false;
	}
	
	public int calch(int x, int y, int ex, int ey) {
		int xd = ex - x;
		if (xd < 0) xd *= -1;
		int yd = ey - y;
		if (yd < 0) yd *= -1;
		return (xd + yd) * 10;
	}
	
	public Vector2f[] incArray(Vector2f[] arr) {
		Vector2f[] newArr = new Vector2f[arr.length + 1];
		for (int i = 0; i < arr.length; i++) {
			newArr[i] = arr[i];
		}
		return newArr;
	}
	
	public void pathTo(int ex, int ez) {
		int h = game.getDepth(), w = game.getWidth();
		int sx = (int) -pos.x / 2;
		int sz = (int) -pos.z / 2;
		boolean done = false, cango = true;
		PNode[][] node = new PNode[w][h];
		int[][] open = new int[w][h];
		int[][] closed = new int[w][h];

		open = new int[w][h];
		closed = new int[w][h];
		
		open[sx][sz] = 1;
		node[sx][sz] = new PNode(0, calch(sx, sz, ex, ez), sx, sz);
		
		while (!done) {
			//look for the lowest f score.
			int lf = 1000000, x = -1, y = -1;
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					if (open[i][j] == 1 && closed[i][j] == 0)
						if (node[i][j].f <= lf) {
							lf = node[i][j].f;
							x = i;
							y = j;
						}
				}
			}
			if (x == -1 || y == -1) {
				cango = false;
				break;
			}
			
			//switch it to the closed list
			closed[x][y] = 1;
			//for all surrounding squares
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					if (i != 0 || j != 0) {
						//If there is a wall blocking diagonal movement then go to next iteration
						if (i + x <= 0 || i + x >= w - 1 || j + y <= 0 || j + y >= h - 1) continue;
						if ((i == -1 && j == -1) ||
							(i == 1 && j == -1) ||
							(i == 1 && j == 1) ||
							(i == -1 && j == 1)) {
								continue;
							}
						//if it is not walkable or it is on the closed list ignore it.
						if (closed[i + x][j + y] == 1 || game.getHeight(i + x, j + y) > game.getHeight(x, y) + 0.3f) {
							continue;
						} else if (open[i + x][j + y] == 0) {
							//if it isn't on the open list add it and make the parent the current square,
							//record h, g and f scores
							int g = 10;
							if ((i == -1 && j == -1) || (i == 1 && j == -1)
									|| (i == 1 && j == 1) || (i == -1 && j == 1)) g += 4;
							node[i + x][j + y] = new PNode(node[x][y].g + g, calch(i + x, j + y, ex, ez), x, y);
							open[i + x][j + y] = 1;
						} else {
							//if it is on the open list then see if the Point is better by using g score,
							//if it is less it is better,
							//if so change the parent and update the f and g scores
							int g = 10;
							if ((i == -1 && j == -1) || (i == 1 && j == -1)
									|| (i == 1 && j == 1) || (i == -1 && j == 1)) g += 4;
							g += node[x][y].g;
							if (g < node[i + x][j + y].g) {
								node[i + x][j + y].updateg(g);
								node[i + x][j + y].px = x;
								node[i + x][j + y].py = y;
							}
						}
					}
				}
			}
			
			//stop when the target square is added to the closed list
			//or when there are no more open spots
			if (x == ex && y == ez) {
				break;
			}
		}
		
		if (cango) {
			point = new Vector2f[2];
			point[0] = new Vector2f(ex, ez);
			int step = 1;
			done = false;
			while (!done) {
				point[step] = new Vector2f(node[(int) point[step - 1].x][(int) point[step - 1].y].px,
						node[(int) point[step - 1].x][(int) point[step - 1].y].py);
				if (point[step].x == sx && point[step].y == sz) break;
				point = incArray(point);
				step++;
			}
		} else {
			point = new Vector2f[0];
			//System.out.println("COULD NOT FIND PATH TO TARGET");
		}
	}
	
	public void tick() {
		if (-pos.y > game.getHeight((int) -pos.x / 2, (int) ((-pos.z) / 2))) {
			vel.y += 0.025f;
		}
		if (-pos.y == game.getHeight((int) -pos.x / 2, (int) -pos.z / 2)) {
			vel.x *= 0.85f;
			vel.z *= 0.85f;
		}
		if (vel.x <= 0.0001f && vel.x >= -0.0001f) vel.x = 0;
		if (vel.z <= 0.0001f && vel.z >= -0.0001f) vel.z = 0;
		if (point.length != 0) {
			rot.y = (float) Math.toDegrees(Math.atan2(pos.x + point[point.length - 1].x * 2 + 1, pos.z + point[point.length - 1].y * 2 + 1));
		}
		
		//follow the player and track them down
		if (found) {
			
			/*if (canSee((int) -target.getX(), (int) -target.getZ())) {
				r = (float) Math.toDegrees(Math.atan2(pos.x + target.getX(), target.getZ() + pos.z));
				vel.z = -0.1f;
				if (point.length > 0) {
					point = new Vector2f[0];
				}
			} else if (point.length == 1) {
				found = false;
				vel.z = 0.0f;
			} else if (point.length == 0) {
				pathTo((int) (-target.getX() / 2), (int) (-target.getZ() / 2));
			} else {
				r = (float) Math.toDegrees(Math.atan2(pos.x + point[point.length - 1].x * 2 + 1, pos.z + point[point.length - 1].y * 2 + 1));
				vel.z = -0.1f;
				if (Math.round((-pos.x - 1) / 2) == point[point.length - 1].x && Math.round((-pos.z - 1) / 2) == point[point.length - 1].y) {
					point = Arrays.copyOfRange(point, 0, point.length - 1);
				}
			}*/
			if (canSee((int) target.getX(), (int) target.getZ(), false)) {
				//System.out.println(true);
				rot.y = (float) Math.toDegrees(Math.atan2(pos.x - target.getX(), pos.z - target.getZ()));
				vel.z += 0.01f;
				if (point.length > 0) {
					point = new Vector2f[0];
				}
				if (attack <= 0) {
					if (Math.sqrt(Math.pow(pos.x - target.getX(), 2) + Math.pow(pos.z - target.getZ(), 2)) <= 1) {
						attack = 60;
						if (target instanceof EntityLiving) {
							((EntityLiving) target).damage(dam, 0);
						}
						//vel.z = 0.0f;
					}
				}
			} else if (point.length == 0) {
				//System.out.println(false);
				pathTo((int) (-target.getX() / 2), (int) (-target.getZ() / 2));
			}
		} else {
			//vel.z = 0.0f;
		}
		
		/*if (Math.sqrt(Math.pow(pos.x - target.getX(), 2) + Math.pow(pos.z - target.getZ(), 2)) < 1) {
			vel.z = 0.0f;
			System.out.println(true);
		}*/
		
		//only begin looking if the entity is alive
		base: {
			if (getHealth() > 0) {
				if (target instanceof EntityLiving) {
					if (((EntityLiving) target).getHealth() < 0) {
						found = false;
						break base;
					}
				}
				//enable AI tracking
				if (!found) {
					if (canSee(target.getX(), target.getZ(), true)) { //if we can see the player then face them and head towards them
						found = true;
						rot.y = (float) Math.toDegrees(Math.atan2(pos.x - target.getX(), pos.z - target.getZ()));
						vel.z += 0.01f;
					}
				}
				
				if (vel.z > maxSpeed) vel.z = maxSpeed;
				if (vel.z < -maxSpeed) vel.z = -maxSpeed;
				if (rot.y < 0) rot.y += 360;
				if (rot.y >= 360) rot.y -= 360;
				
				//left and right movement
				pos.x += vel.x * Math.sin(Math.toRadians(rot.y + 90));
				pos.z -= vel.x * Math.cos(Math.toRadians(rot.y + 90));
				
				//forward and backward movement
				pos.x -= vel.z * Math.sin(Math.toRadians(rot.y));
				pos.z -= vel.z * Math.cos(Math.toRadians(rot.y));
				
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
				if (point.length != 0) {
					vel.z += 0.01f;
					if (Math.round((-pos.x - 1) / 2) == point[point.length - 1].x && Math.round((-pos.z - 1) / 2) == point[point.length - 1].y) {
						point = Arrays.copyOfRange(point, 0, point.length - 1);
						if (point.length == 0) {
							if (!canSee(target.getX(), target.getZ(), false)) {
								found = false;
							}
						}
					}
				}
			} else {
				found = false;
				if (effects.size() > 0) effects.clear();
			}
		}
		pos.y += vel.y;
		forces();
		
		Vector3f vec = game.collides(this, rad);
		if (vec != null) {
			if (pos.x != vec.x || pos.z != vec.z) {
				pos.x = vec.x;
				pos.z = vec.z;
				if (getHealth() <= 0) {
					if (Math.floor(Math.random() * 50) <= blood + 1) {
						if (blood > 0) blood--;
						game.addEnt(new EntityFloorDecoration(pos.x, pos.z, game, (float) Math.floor(Math.random() * 360), 7 + (int) (Math.round(Math.random() * 2))));
					}
				}
			}
		}
		
		if (effects.size() > 0) {
			for (int i = 0; i < effects.size(); i++) {
				effects.get(i).tick(this);
				if (effects.get(i).isFinished()) {
					effects.remove(i);
					i--;
				}
			}
		}
		
		if (vel.z != 0) anim += 0.125f;
		else anim = 0.0f;
		if (anim >= 4) anim = 0;
		if (getHealth() <= 0) anim = 0.0f;
		if (attack > 0) attack--;
	}
	
	public void draw(float lx, float lz) {
		//don't render this object if it is outside of the player's view
		if (Math.abs(pos.x - lx) > 15) return;
		if (Math.abs(pos.z - lz) > 15) return;
		
		float pw = 1.0f / tex.getWidth();
		float ph = 1.0f / tex.getHeight();
		
		float w = pw * 7;
		float h = ph * 12;
		
		float r = (float) Math.toDegrees(Math.atan2(pos.x - lx, pos.z - lz));
		//float r = (float) Math.toDegrees(Math.atan2(pos.x + point[point.length - 1].x * 2 + 1, pos.z + point[point.length - 1].y * 2 + 1));
		
		GL11.glPushMatrix();
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		if (getHealth() > 0) {
			GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
			GL11.glRotatef(r, 0, 1, 0);
		} else {
			GL11.glTranslatef(-pos.x, -pos.y + 0.0015f, -pos.z);
			GL11.glRotatef(rot.y, 0, 1, 0);
			GL11.glTranslatef(0.0f, 0.0f, 0.75f);
			GL11.glRotatef(-90, 1, 0, 0);
		}
		
		//figure out what side of the enemy is facing the player
		r += 180;
		int ang = (int) (r - rot.y + 225);
		//if (ang < 0) ang += 180;
		if (ang < 0) ang += 360;
		else if (ang > 360) ang -= 360;
		ang = ang / 90;
		if (getHealth() > 0) deadAng = ang;
		else ang = deadAng;
		int anim = (int) this.anim + ang * 4;
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(w * (anim + 1), h);
			GL11.glVertex3f(0.4375f, 0.0f, 0.0f);
			GL11.glTexCoord2f(w * (anim + 1), 0);
			GL11.glVertex3f(0.4375f, 1.5f, 0.0f);
			GL11.glTexCoord2f(w * anim, 0);
			GL11.glVertex3f(-0.4375f, 1.5f, 0.0f);
			GL11.glTexCoord2f(w * anim, h);
			GL11.glVertex3f(-0.4375f, 0.0f, 0.0f);
		GL11.glEnd();
		
		drawArmour(ang, (int) this.anim);
		
		if (tex.getHeight() > 12) {
			//HANDS
			GL11.glColor3f(1.0f, 1.0f, 1.0f);
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(w * (anim + 1), h * 2);
				GL11.glVertex3f(0.4375f, 0.0f, 0.0f);
				GL11.glTexCoord2f(w * (anim + 1), h);
				GL11.glVertex3f(0.4375f, 1.5f, 0.0f);
				GL11.glTexCoord2f(w * anim, h);
				GL11.glVertex3f(-0.4375f, 1.5f, 0.0f);
				GL11.glTexCoord2f(w * anim, h * 2);
				GL11.glVertex3f(-0.4375f, 0.0f, 0.0f);
			GL11.glEnd();
		}
		
		if (getHealth() > 0) {
			for (int i = 0; i < effects.size(); i++) {
				effects.get(i).draw();
			}
		}
		GL11.glPopMatrix();
	}
	
	public static Entity newEnemy(String[] data, Game g) {
		int type = Integer.parseInt(data[1]);
		switch (type) {
			case 0:
				return new EntityVermin(0, Float.parseFloat(data[2]), Float.parseFloat(data[3]), Float.parseFloat(data[4]), g);
			case 1:
				return new EntityVermin(1, Float.parseFloat(data[2]), Float.parseFloat(data[3]), Float.parseFloat(data[4]), g);
			case 2:
				return new EntityLeech(Float.parseFloat(data[2]), Float.parseFloat(data[3]), Float.parseFloat(data[4]), g);
			case 3:
				return new EntityVermin(2, Float.parseFloat(data[2]), Float.parseFloat(data[3]), Float.parseFloat(data[4]), g);
			case 4:
				return new EntityBat(Float.parseFloat(data[2]), Float.parseFloat(data[3]), Float.parseFloat(data[4]), g);
			case 5:
				return new EntityHuman(Float.parseFloat(data[2]), Float.parseFloat(data[3]), Float.parseFloat(data[4]), g);
			case 6:
				return new EntitySkeleton(Float.parseFloat(data[2]), Float.parseFloat(data[3]), Float.parseFloat(data[4]), g);
			case 7:
				return new EntityStatue(Float.parseFloat(data[2]), Float.parseFloat(data[3]), Float.parseFloat(data[4]), g);
			case 8:
				return new EntityGhost(Float.parseFloat(data[2]), Float.parseFloat(data[3]), Float.parseFloat(data[4]), g);
			case 9:
				return new EntityGhostKing(Float.parseFloat(data[2]), Float.parseFloat(data[3]), Float.parseFloat(data[4]), g);
			case 10:
				return new EntityDwarf(Float.parseFloat(data[2]), Float.parseFloat(data[3]), Float.parseFloat(data[4]), g);
		}
		return new EntityEnemy(Float.parseFloat(data[2]), Float.parseFloat(data[3]), Float.parseFloat(data[4]), g);
	}
}
