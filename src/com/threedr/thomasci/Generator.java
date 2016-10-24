package com.threedr.thomasci;

public class Generator {
	private Room[] rooms;
	private Door[] doors;
	private Hallway[] hallways;
	private int[][] map;
	private int startx, starty, endx, endy, tiles;
	private static int width, height;
	public boolean connected, done;
	
	public Generator() {
		setup();
		width = 40;
		height = 20;
	}
	
	public void setup() {
		connected = false;
		rooms = new Room[6];
		doors = new Door[0];
		hallways = new Hallway[0];
		done = false;
	}
	
	public void generate() {
		boolean bool = true;
		while (bool) {
			setup();
			bool = false;
			
			clearRooms();
			//make 4-6 random rooms.
			int random = (int) Math.round(Math.random() * 2) + 4;
			for (int i = 0; i < random; i++) {
				rooms[i] = new Room(i);
			}
			
			for (int i = 0; i < rooms.length; i++) {
				if (rooms[i] != null) {
					rooms[i].randomPos();
				} else {
					break;
				}
			}
			
			//check if any rooms overlap
			for (int i = 0; i < rooms.length; i++) {
				if (rooms[i] != null) {
					for (int j = 0; j < rooms.length; j++) {
						if (i != j) {
							if (rooms[j] != null) {
								if (rooms[i].collides(rooms[j])) {
									bool = true;
									break;
								}
							} else {
								break;
							}
						}
					}
				} else {
					break;
				}
				if (bool) {
					break;
				}
			}
		}
		
		startx = rooms[0].getX() + 1;
		starty = rooms[0].getZ() + 1;
		
		for (int i = 5; i > 0; i--) {
			if (rooms[i] != null) {
				endx = rooms[i].getX() + (int) Math.floor(Math.random() * (rooms[i].getW() - 4)) + 2;
				endy = rooms[i].getZ() + (int) Math.floor(Math.random() * (rooms[i].getH() - 4)) + 2;
			}
		}
		
		map = new int[width][height];
		
		for (int i = 0; i < rooms.length; i++) {
			if (rooms[i] != null) {
				map = rooms[i].write(map);
			}
		}
		for (int i = 0; i < rooms.length; i++) {
			if (rooms[i] != null) {
				Door[] tds = rooms[i].getDoors();
				for (int j = 0; j < tds.length; j++) {
					if (tds[j] != null) {
						doors = addDoor(tds[j], doors);
					}
				}
			}
		}
		
		/*updateConnections();
		
		for (int i = 5; i > 0; i--) {
			if (rooms[i] != null) {
				Door[] ds = rooms[i].getDoors();
				for (int k = 0; k < ds.length; k++) {
					if (ds[k] != null) {
						if (ds[k].connected) {
							connected = true;
							break;
						}
					}
				}
				break;
			}
		}*/
		
		//add the hallways
		bool = true;
		int[][] tempmap = new int[map.length][map[0].length];
		int times = 10;
		while (bool) {
			for (int i = 0; i < doors.length; i++) {
				doors[i].hall = false;
				doors[i].connected = false;
			}
			tempmap = new int[map.length][map[0].length];
			for (int i = 0; i < tempmap.length; i++) {
				for (int j = 0; j < tempmap[i].length; j++) {
					tempmap[i][j] = map[i][j];
				}
			}
			hallways = new Hallway[0];
			tiles = 0;
			for (int i = 0; i < doors.length; i++) {
				if (doors[i] != null) {
					if (!doors[i].hall) {
						Hallway h = new Hallway();
						doors[i].hall = true;
						Door d = pickRandomDoor(doors[i].rid);
						if (d == null) {
							d = doors[(int) Math.floor(Math.random() * doors.length)];
						}
						d.hall = true;
						h.Path(tempmap, doors[i].x, doors[i].y, d.x, d.y);
						hallways = addHallway(h, hallways);
						doors[i].con = d;
					}
				}
			}
			
			for (int i = 0; i < hallways.length; i++) {
				tempmap = hallways[i].write(tempmap);
			}
			
			System.out.println("C: " + getTiles(tempmap));
			int[][] m = fillTile(tempmap, new int[width][height], startx, starty);
			int count = 0;
			for (int i = 0; i < m.length; i++) {
				for (int j = 0; j < m[i].length; j++) {
					if (m[i][j] == 1)
						count++;
				}
			}
			System.out.println("T: " + count);
			
			if (getTiles(tempmap) == count) {
				bool = false;
				done = true;
			}
			times--;
			if (times < 0) {
				done = false;
				break;
			}
		}
		map = tempmap;
		map = surroundFloors(map);
	}
	
	public int[][] getTile() {
		if (done) {
			return map;
		}
		return null;
	}
	
	public int[][] getTileR() {
		if (done) {
			int[][] map = new int[width][height];
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[i].length; j++) {
					map[i][j] = 2;
				}
			}
			return map;
		}
		return null;
	}
	
	public float[][] getHeight() {
		if (done) {
			float[][] height = new float[getMapWidth()][getMapHeight()];
			for (int i = 0; i < height.length; i++) {
				for (int j = 0; j < height[i].length; j++) {
					if (map[i][j] == 2) {
						height[i][j] = 3.0f;
					} else {
						height[i][j] = 1.0f;
					}
				}
			}
			
			for (int i = 0; i < rooms.length; i++) {
				if (rooms[i] != null) {
					for (int j = 0; j < rooms[i].getW(); j++) {
						for (int k = 0; k < rooms[i].getH(); k++) {
							if (j == 0) if (height[j + rooms[i].getX()][k + rooms[i].getZ()] == 3.0f) height[j + rooms[i].getX()][k + rooms[i].getZ()] = 4.0f;
							if (k == 0) if (height[j + rooms[i].getX()][k + rooms[i].getZ()] == 3.0f) height[j + rooms[i].getX()][k + rooms[i].getZ()] = 4.0f;
							if (j == rooms[i].getW() - 1) if (height[j + rooms[i].getX()][k + rooms[i].getZ()] == 3.0f) height[j + rooms[i].getX()][k + rooms[i].getZ()] = 4.0f;
							if (k == rooms[i].getH() - 1) if (height[j + rooms[i].getX()][k + rooms[i].getZ()] == 3.0f) height[j + rooms[i].getX()][k + rooms[i].getZ()] = 4.0f;
						}
					}
				}
			}
			return height;
		}
		return null;
	}
	
	public float[][] getRoof() {
		if (done) {
			float[][] roof = new float[width][height];
			for (int i = 0; i < roof.length; i++) {
				for (int j = 0; j < roof[i].length; j++) {
					if (map[i][j] == 3) roof[i][j] = 3.0f;
				}
			}
			for (int i = 0; i <rooms.length; i++) {
				if (rooms[i] != null) {
					for (int j = 0; j < rooms[i].getW(); j++) {
						for (int k = 0; k < rooms[i].getH(); k++) {
							if (j > 0 && k > 0 && j < rooms[i].getW() - 1 && k < rooms[i].getH() - 1)
								roof[j + rooms[i].getX()][k + rooms[i].getZ()] = 4.0f;
						}
					}
				}
			}
			return roof;
		}
		return null;
	}
	
	public Door[] addDoor(Door d, Door[] arr) {
		Door[] newArr = new Door[arr.length + 1];
		for (int i = 0; i < arr.length; i++) {
			newArr[i] = arr[i];
		}
		newArr[arr.length] = d;
		return newArr;
	}
	
	public Hallway[] addHallway(Hallway h, Hallway[] arr) {
		Hallway[] newArr = new Hallway[arr.length + 1];
		for (int i = 0; i < arr.length; i++) {
			newArr[i] = arr[i];
		}
		newArr[arr.length] = h;
		return newArr;
	}
	
	public Door pickRandomDoor(int id) {
		for (int i = 0; i < doors.length; i++) {
			if (id != doors[i].rid)
				if (!doors[i].hall) return doors[i];
		}
		return null;
	}
	
	public void clearRooms() {
		for (int i = 0; i < rooms.length; i++) {
			if (rooms[i] != null)
				rooms[i] = null;
			else 
				break;
		}
	}
	
	public int[][] surroundFloors(int[][] map) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (map[i][j] == 3) {
					if (map[i - 1][j] == 0) map[i - 1][j] = 2;
					if (map[i - 1][j - 1] == 0) map[i - 1][j - 1] = 2;
					if (map[i][j - 1] == 0) map[i][j - 1] = 2;
					if (map[i + 1][j - 1] == 0) map[i + 1][j - 1] = 2;
					if (map[i + 1][j] == 0) map[i + 1][j] = 2;
					if (map[i + 1][j + 1] == 0) map[i + 1][j + 1] = 2;
					if (map[i][j + 1] == 0) map[i][j + 1] = 2;
					if (map[i - 1][j + 1] == 0) map[i - 1][j + 1] = 2;
				}
			}
		}
		return map;
	}
	
	public Room getRoom(int id) {
		for (int i = 0; i < rooms.length; i++) {
			if (rooms[i].id == id) return rooms[i];
		}
		return null;
	}
	
	public void updateConnections() {
		int id = -1;
		for (int i = 5; i > 0; i--) {
			if (rooms[i] != null) id = rooms[i].id;
		}
		for (int i = 0; i < doors.length; i++) {
			if (doors[i] != null) {
				if (doors[i].connected) {
					if (doors[i].con != null) {
						doors[i].con.connected = true;
						if (doors[i].rid == id) {
							break;
						}
						Room rm = getRoom(doors[i].con.rid);
						rm.setConnected();
					}
				}
			}
		}
	}
	
	public int[][] fillTile(int[][] m, int[][] tmap, int sx, int sy) {
		if (m[sx][sy] == 3 && tmap[sx][sy] == 0) {
			tmap[sx][sy] = 1;
			fillTile(m, tmap, sx - 1, sy);
			fillTile(m, tmap, sx, sy - 1);
			fillTile(m, tmap, sx + 1, sy);
			fillTile(m, tmap, sx, sy + 1);
		}
		return tmap;
	}
	
	public int getStartX() {
		return startx;
	}
	
	public int getStartZ() {
		return starty;
	}
	
	public int getEndX() {
		return endx;
	}
	
	public int getEndZ() {
		return endy;
	}
	
	public int getTiles(int[][] m) {
		if (tiles == 0) {
			for (int i = 0; i < m.length; i++) {
				for (int j = 0; j < m[i].length; j++) {
					if (m[i][j] == 3) tiles++;
				}
			}
		}
		return tiles;
	}
	
	public static int getMapWidth() {
		return width;
	}
	
	public static int getMapHeight() {
		return height;
	}
}
