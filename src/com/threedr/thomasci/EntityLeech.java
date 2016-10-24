package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class EntityLeech extends EntityEnemy {
	
	public EntityLeech(float x, float z, float rot, Game g) {
		super(x, z, rot, g);
		height = 0.625f;
		health = 1;
		maxHealth = 1;
		tex = TDR.getTexture("leech", "img/leech.png");
	}
	
	public boolean canSee(float x, float z, boolean fov) {
		float r = 0;
		//leeches do not need to determine if the point is in their line of sight, they use their water senses!
		
		if (Math.abs(pos.x - x) >= 20) return false;
		if (Math.abs(pos.z - z) >= 20) return false;
		
		float cx = pos.x;
		float cy = pos.y;
		float cz = pos.z;
		float dist = 0.0f;
		r = (float) Math.toDegrees(Math.atan2(pos.x - x, pos.z - z));
		
		while (dist < 20.0f) {
			dist += rad * 2;
			cx -= rad * Math.sin(Math.toRadians(r));
			cz -= rad * Math.cos(Math.toRadians(r));
			
			if (hitWall(cx, cy, cz)) return false;
			if (game.getTile((int) -cx / 2, (int) -cz / 2) != 1) return false;
			
			if ((int) cx == (int) x && (int) cz == (int) z) return true;
			
			cy = -game.getHeight((int) -cx / 2, (int) -cz / 2);
		}
		
		return false;
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
						//if it is not walkable or it is on the closed list or it is not in the water ignore it.
						if (closed[i + x][j + y] == 1 || game.getHeight(i + x, j + y) > game.getHeight(x, y) + 0.3f || game.getTile(i + x, j + y) != 1) {
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
		}
	}
	
	public void drawBox(float lx, float lz) {
		if (health > 0) {
			float r = (float) Math.toDegrees(Math.atan2(pos.x - lx, pos.z - lz));
			
			GL11.glPushMatrix();
			GL11.glTranslatef(-pos.x, -pos.y, -pos.z);
			GL11.glRotatef(r, 0, 1, 0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex3f(0.4375f, 0.0f, 0.0f);
				GL11.glVertex3f(0.4375f, 0.625f, 0.0f);
				GL11.glVertex3f(-0.4375f, 0.625f, 0.0f);
				GL11.glVertex3f(-0.4375f, 0.0f, 0.0f);
			GL11.glEnd();
			GL11.glPopMatrix();
		}
	}
	
	public int damage(int num, int type) {
		int oldHealth = health;
		int amount = super.damage(num, type);
		if (health <= 0 && oldHealth > 0) {
			game.addEnt(new EntityItem(pos.x, pos.z, game, Item.newItem(18, "Def")));
		}
		return amount;
	}
}
