package com.threedr.thomasci;

import org.lwjgl.util.vector.Vector2f;

public class Hallway {
	public Vector2f[] point;
	private int tiles;
	
	public Vector2f[] incArray(Vector2f[] arr) {
		Vector2f[] newArr = new Vector2f[arr.length + 1];
		for (int i = 0; i < arr.length; i++) {
			newArr[i] = arr[i];
		}
		return newArr;
	}
	
	public int calch(int x, int y, int ex, int ey) {
		int xd = ex - x;
		if (xd < 0) xd *= -1;
		int yd = ey - y;
		if (yd < 0) yd *= -1;
		return (xd + yd) * 10;
	}
	
	public int[][] getWalkable(int[][] dat, int sx, int sy, int ex, int ey) {
		int[][] temp = new int[dat.length][dat[0].length];
		for (int i = 0; i < dat.length; i++) {
			for (int j = 0; j < dat[i].length; j++) {
				if (dat[i][j] == 3 || dat[i][j] == 2 || dat[i][j] == 1) {
					temp[i][j] = 1;
				}
			}
		}
		temp[sx][sy] = 0;
		temp[ex][ey] = 0;
		return temp;
	}
	
	public void Path(int[][] dat, int sx, int sy, int ex, int ey) {
		int h = dat[0].length, w = dat.length;
		boolean done = false, cango = true;
		PNode[][] node = new PNode[w][h];
		int[][] walkable = getWalkable(dat, sx, sy, ex, ey);
		int[][] open = new int[w][h];
		int[][] closed = new int[w][h];

		open = new int[w][h];
		closed = new int[w][h];
		
		open[sx][sy] = 1;
		node[sx][sy] = new PNode(0, calch(sx, sy, ex, ey), sx, sy);
		
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
						if (closed[i + x][j + y] == 1 || walkable[i + x][j + y] == 1) {
							continue;
						} else if (open[i + x][j + y] == 0) {
							//if it isn't on the open list add it and make the parent the current square,
							//record h, g and f scores
							int g = 10;
							if ((i == -1 && j == -1) || (i == 1 && j == -1)
									|| (i == 1 && j == 1) || (i == -1 && j == 1)) g += 4;
							node[i + x][j + y] = new PNode(node[x][y].g + g, calch(i + x, j + y, ex, ey), x, y);
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
			if (x == ex && y == ey) {
				break;
			}
		}
		
		if (cango) {
			point = new Vector2f[2];
			point[0] = new Vector2f(ex, ey);
			int step = 1;
			done = false;
			while (!done) {
				point[step] = new Vector2f(node[(int) point[step - 1].x][(int) point[step - 1].y].px,
						node[(int) point[step - 1].x][(int) point[step - 1].y].py);
				if (point[step].x == sx && point[step].y == sy) break;
				point = incArray(point);
				step++;
			}
			tiles = step - 1;
		} else {
			System.out.println("ERROR! Could not find Point to door?");
			tiles = 0;
		}
	}
	
	public int[][] write(int[][] map) {
		if (point != null) {
			for (int i = 1; i < point.length - 1; i++) {
				if (point[i] != null)
					map[(int) point[i].x][(int) point[i].y] = 3;
			}
		}
		return map;
	}
	
	public int getTiles() {
		return tiles;
	}
}
