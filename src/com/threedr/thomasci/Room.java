package com.threedr.thomasci;

public class Room {
	private int x, y, w, h;
	private Door[] doors;
	public final int id;
	
	public Room(int id) {
		x = (int) Math.floor(Math.random() * 31) + 2;
		y = (int) Math.floor(Math.random() * 15) + 2;
		w = (int) Math.floor(Math.random() * 6) + 5;
		h = (int) Math.floor(Math.random() * 4) + 5;
		if (x + w >= 31) x = 32 - w - 2;
		if (y + h >= 15) y = 16 - h - 2;
		doors = new Door[4];
		this.id = id;
		int random = (int) Math.floor(Math.random() * 2) * 2 + 2;
		for (int i = 0; i < random; i++) {
			do {
				int rndmposx = (int) Math.round(Math.random() * (w - 1));
				int rndmposy = (int) Math.round(Math.random() * (h - 1));
				if (Math.floor(Math.random() * 2) == 0) {
					if (rndmposx == 0) rndmposx = 1;
					if (rndmposx == w - 1) rndmposx = w - 2;
					int y = 0;
					if (rndmposy >= h / 2) y = h - 1;
					doors[i] = new Door(rndmposx, y, id);
				} else {
					if (rndmposy == 0) rndmposy = 1;
					if (rndmposy == h - 1) rndmposy = h - 2;
					int x = 0;
					if (rndmposx >= w / 2) x = w - 1;
					doors[i] = new Door(x, rndmposy, id);
				}
			} while (doorOverlap());
			if (id == 0) doors[i].connected = true;
		}
	}
	
	public void setConnected() {
		for (int i = 0; i < doors.length; i++) {
			if (doors[i] != null) {
				doors[i].connected = true;
			}
		}
	}
	
	public boolean collides(Room rm) {
		if ((x - 1 >= rm.x && x - 1 <= rm.x + w + 1) || (x + w + 1 >= rm.x - 1 && x + w + 1 <= rm.x + w + 1)) {
			if ((y - 1 >= rm.y && y - 1 <= rm.y + h + 1) || (y + h + 1 >= rm.y - 1 && y + h + 1 <= rm.y + h + 1)) {
				return true;
			}
		}
		return false;
	}
	
	public void randomPos() {
		x = (int) Math.floor(Math.random() * Generator.getMapWidth() - 1) + 3;
		y = (int) Math.floor(Math.random() * Generator.getMapHeight() - 1) + 3;
		if (x <= 1) x = 2;
		if (y <= 1) y = 2;
		if (x + w >= 38) x = 40 - w - 3;
		if (y + h >= 18) y = 20 - h - 3;
	}
	
	public int[][] write(int[][] map) {
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if (i == 0 || j == 0 || i == w - 1 || j == h - 1)
					map[x + i][y + j] = 2;
				else
					map[x + i][y + j] = 3;
			}
		}
		for (int i = 0; i < doors.length; i++) {
			if (doors[i] != null) {
				map[doors[i].x + x][doors[i].y + y] = 3;
			}
		}
		return map;
	}
	
	public Door[] getDoors() {
		Door[] ds = new Door[5];
		for (int i = 0; i < doors.length; i++) {
			if (doors[i] != null) {
				ds[i] = doors[i];
				ds[i].x += x;
				ds[i].y += y;
			}
		}
		return ds;
	}
	
	public int getX() {
		return x;
	}
	
	public int getZ() {
		return y;
	}
	
	public int getW() {
		return w;
	}
	
	public int getH() {
		return h;
	}
	
	public boolean doorOverlap() {
		for (int i = 0; i < doors.length; i++) {
			if (doors[i] != null) {
				for (int j = 0; j < doors.length; j++) {
					if(doors[j] != null) {
						if (j != i) {
							if (doors[i].x == doors[j].x && doors[i].y != doors[j].y)
								return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public int getTiles() {
		int count = (w - 2) * (h - 2);
		for (int i = 0; i < doors.length; i++) {
			if (doors[i] != null) count++;
			else break;
		}
		return count;
	}
}
