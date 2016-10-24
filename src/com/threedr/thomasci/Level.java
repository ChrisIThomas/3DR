package com.threedr.thomasci;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

public class Level {
	private final int[][] tile, tiler;
	private final float[][] height, roof;
	private final Texture tex;
	private final Generator generator;
	private final LevelLoader levLoader;
	private String[] data;
	private float water;
	private int dir = 1;
	private float anim;
	
	public Level(String name) {
		tex = TDR.getTexture("tiles", "img/tiles.png");
		generator = new Generator();
		while (!generator.done) { 
			generator.generate();
		}
		levLoader = new LevelLoader();
		int[][][] map = levLoader.loadTiles(name);
		float[][][] hmap = levLoader.loadHeight(name);
		tile = map[0];
		tiler = map[1];
		height = hmap[0];
		roof = hmap[1];
		
		data = levLoader.loadData(name);
		
		/*tile = generator.getTile();
		tiler = generator.getTileR();
		
		height = generator.getHeight();
		roof = generator.getRoof();*/
	}
	
	public String[] getData() {
		return data;
	}
	
	public ArrayList<EDat> getEnts(String location) {
		return levLoader.loadEnts(location);
	}
	
	public float getHeight(int x, int z) {
		if (x < 0) x = 0;
		else if (x >= height.length) x = height.length - 1;
		if (z < 0) z = 0;
		else if (z >= height[0].length) z = height[0].length - 1;
		return height[x][z];
	}
	
	public float getTile(int x, int z) {
		if (x < 0) x = 0;
		else if (x >= tile.length) x = tile.length - 1;
		if (z < 0) z = 0;
		else if (z >= tile[0].length) z = tile[0].length - 1;
		return tile[x][z];
	}
	
	public float getRoof(int x, int z) {
		if (x < 0) x = 0;
		else if (x >= roof.length) x = roof.length - 1;
		if (z < 0) z = 0;
		else if (z >= roof[0].length) z = roof[0].length - 1;
		return roof[x][z];
	}
	
	public int getWidth() {
		return height.length;
	}
	
	public int getDepth() {
		return height[0].length;
	}
	
	public int getTile(int id) {
		switch (id) {
			case 1:
				return 220 + (int) anim;
		}
		return id;
	}
	
	public void draw(boolean drawWater, float px, float pz) {
		px /= -2;
		pz /= -2;
		
		anim += 0.075f;
		if (anim >= 4) anim = 0;
		water += (float) dir / 1000;
		if (water >= 0.1f) dir = -1;
		if (water <= 0.025f) dir = 1;
		
		float pw = 1.0f / tex.getWidth();
		float ph = 1.0f / tex.getHeight();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		for (int i = 0; i < height.length; i++) {
			for (int j = 0; j < height[i].length; j++) {
				//don't render this section if it is outside of the player's view
				if (Math.abs(i - px) > 8) continue;
				if (Math.abs(j - pz) > 8) continue;
				if (!drawWater)
					if (tile[i][j] == 1) break;
				int tloc = getTile(tile[i][j]);
				float w = pw * 16;
				float h = ph * 16;
				int ty = 0;
				int tx = 0;
				if (tloc != -1) {
					ty = tloc / 16;
					tx = tloc - ty * 16;
					//ty *= ;
					float add = tile[i][j] == 1 ? ((float) Math.ceil(height[i][j]) - height[i][j] - 0.25f + water) : 0;
					
					//FLOOR
					GL11.glBegin(GL11.GL_QUADS);
						GL11.glTexCoord2f(tx * w, ty * h);
						GL11.glVertex3f(i * 2, height[i][j] + add, j * 2);
						GL11.glTexCoord2f(tx * w, (ty + 1) * h);
						GL11.glVertex3f(i * 2, height[i][j] + add, j * 2 + 2);
						GL11.glTexCoord2f((tx + 1) * w, (ty + 1) * h);
						GL11.glVertex3f(i * 2 + 2, height[i][j] + add, j * 2 + 2);
						GL11.glTexCoord2f((tx + 1) * w, ty * h);
						GL11.glVertex3f(i * 2 + 2, height[i][j] + add, j * 2);
					GL11.glEnd();
					
					//WALLS
					if (tile[i][j] != 1) {
						float hd = 0, th = 0, vd = 0;
						if (i < height.length - 1) {
							if (height[i + 1][j] < height[i][j]) {
								boolean darken = getTile(tile[i + 1][j]) == -1;
								boolean end = false;
								hd = height[i][j] - height[i + 1][j];
								if (tloc == 3) {
									if (hd > 0.5) {
										ty = (tloc - 1) / 8;
										tx = (tloc - 1) - ty * 8;
										ty *= 8;
									}
								}
								int k = 0;
								do {
									end = hd - k * 2 <= 2;
									vd = hd - k * 2;
									if (vd > 2) vd = 2;
									th = vd * 8 * ph;
									GL11.glBegin(GL11.GL_QUADS);
										GL11.glTexCoord2f((tx + 1) * w, ty * h);
										GL11.glVertex3f(i * 2 + 2, height[i][j] - k * 2, j * 2);
										GL11.glTexCoord2f(tx * w, ty * h);
										GL11.glVertex3f(i * 2 + 2, height[i][j] - k * 2, j * 2 + 2);
										if (darken) if (end) GL11.glColor3f(0.0f, 0.0f, 0.0f);
										GL11.glTexCoord2f(tx * w, ty * h + th);
										GL11.glVertex3f(i * 2 + 2, height[i][j] - vd - k * 2, j * 2 + 2);
										GL11.glTexCoord2f((tx + 1) * w, ty * h + th);
										GL11.glVertex3f(i * 2 + 2, height[i][j] - vd - k * 2, j * 2);
									GL11.glEnd();
									k++;
								} while (k < hd / 2);
								if (darken) GL11.glColor3f(1.0f, 1.0f, 1.0f);
							}
						}
						if (j < height[i].length - 1) {
							if (height[i][j + 1] < height[i][j]) {
								boolean darken = getTile(tile[i][j + 1]) == -1;
								boolean end = false;
								hd = height[i][j] - height[i][j + 1];
								int k = 0;
								if (tloc == 3) {
									if (hd > 0.5) {
										ty = (tloc - 1) / 8;
										tx = (tloc - 1) - ty * 8;
									} else {
										ty = tloc / 8;
										tx = tloc - ty * 8;
									}
									ty *= 8;
								}
								do {
									end = hd - k * 2 <= 2;
									vd = hd - k * 2;
									if (vd > 2) vd = 2;
									th = vd * 8 * ph;
									GL11.glBegin(GL11.GL_QUADS);
										GL11.glTexCoord2f(tx * w, ty * h);
										GL11.glVertex3f(i * 2, height[i][j] - k * 2, j * 2 + 2);
										if (darken) if (end) GL11.glColor3f(0.0f, 0.0f, 0.0f);
										GL11.glTexCoord2f(tx * w, ty * h + th);
										GL11.glVertex3f(i * 2, height[i][j] - vd - k * 2, j * 2 + 2);
										GL11.glTexCoord2f((tx + 1) * w, ty * h + th);
										GL11.glVertex3f(i * 2 + 2, height[i][j] - vd - k * 2, j * 2 + 2);
										if (darken) if (end) GL11.glColor3f(1.0f, 1.0f, 1.0f);
										GL11.glTexCoord2f((tx + 1) * w, ty * h);
										GL11.glVertex3f(i * 2 + 2, height[i][j] - k * 2, j * 2 + 2);
									GL11.glEnd();
									k++;
								} while (k < hd / 2);
							}
						}
						if (i > 0) {
							if (height[i - 1][j] < height[i][j]) {
								boolean darken = getTile(tile[i - 1][j]) == -1;
								boolean end = false;
								hd = height[i][j] - height[i - 1][j];
								int k = 0;
								if (tloc == 3) {
									if (hd > 0.5) {
										ty = (tloc - 1) / 8;
										tx = (tloc - 1) - ty * 8;
									} else {
										ty = tloc / 8;
										tx = tloc - ty * 8;
									}
									ty *= 8;
								}
								do {
									end = hd - k * 2 <= 2;
									vd = hd - k * 2;
									if (vd > 2) vd = 2;
									th = vd * 8 * ph;
									GL11.glBegin(GL11.GL_QUADS);
										GL11.glTexCoord2f(tx * w, ty * h);
										GL11.glVertex3f(i * 2, height[i][j] - k * 2, j * 2);
										if (darken) if (end) GL11.glColor3f(0.0f, 0.0f, 0.0f);
										GL11.glTexCoord2f(tx * w, ty * h + th);
										GL11.glVertex3f(i * 2, height[i][j] - vd - k * 2, j * 2);
										GL11.glTexCoord2f((tx + 1) * w, ty * h + th);
										GL11.glVertex3f(i * 2, height[i][j] - vd - k * 2, j * 2 + 2);
										if (darken) if (end) GL11.glColor3f(1.0f, 1.0f, 1.0f);
										GL11.glTexCoord2f((tx + 1) * w, ty * h);
										GL11.glVertex3f(i * 2, height[i][j] - k * 2, j * 2 + 2);
									GL11.glEnd();
									k++;
								} while (k < hd / 2);
							}
						}
						if (j > 0) {
							if (height[i][j - 1] < height[i][j]) {
								boolean darken = getTile(tile[i][j - 1]) == -1;
								boolean end = false;
								hd = height[i][j] - height[i][j - 1];
								int k = 0;
								if (tloc == 3) {
									if (hd > 0.5) {
										ty = (tloc - 1) / 8;
										tx = (tloc - 1) - ty * 8;
									} else {
										ty = tloc / 8;
										tx = tloc - ty * 8;
									}
									ty *= 8;
								}
								do {
									end = hd - k * 2 <= 2;
									vd = hd - k * 2;
									if (vd > 2) vd = 2;
									th = vd * 8 * ph;
									GL11.glBegin(GL11.GL_QUADS);
										GL11.glTexCoord2f((tx + 1) * w, ty * h);
										GL11.glVertex3f(i * 2, height[i][j] - k * 2, j * 2);
										GL11.glTexCoord2f(tx * w, ty * h);
										GL11.glVertex3f(i * 2 + 2, height[i][j] - k * 2, j * 2);
										if (darken) if (end) GL11.glColor3f(0.0f, 0.0f, 0.0f);
										GL11.glTexCoord2f(tx * w, ty * h + th);
										GL11.glVertex3f(i * 2 + 2, height[i][j] - vd - k * 2, j * 2);
										GL11.glTexCoord2f((tx + 1) * w, ty * h + th);
										GL11.glVertex3f(i * 2, height[i][j] - vd - k * 2, j * 2);
									GL11.glEnd();
									k++;
								} while (k < hd / 2);
								if (darken) GL11.glColor3f(1.0f, 1.0f, 1.0f);
							}
						}
					}
				}
				
				
				
				if (tiler[i][j] != -1) {
					ty = tiler[i][j] / 8;
					tx = tiler[i][j] - ty * 8;
					
					//ROOF
					if (tiler[i][j] != -1 && roof[i][j] != 0) {
						GL11.glBegin(GL11.GL_QUADS);
							GL11.glTexCoord2f(tx * w, ty * h);
							GL11.glVertex3f(i * 2, roof[i][j], j * 2);
							GL11.glTexCoord2f(tx * w, (ty + 1) * h);
							GL11.glVertex3f(i * 2 + 2, roof[i][j], j * 2);
							GL11.glTexCoord2f((tx + 1) * w, (ty + 1) * h);
							GL11.glVertex3f(i * 2 + 2, roof[i][j], j * 2 + 2);
							GL11.glTexCoord2f((tx + 1) * w, ty * h);
							GL11.glVertex3f(i * 2, roof[i][j], j * 2 + 2);
						GL11.glEnd();
						//ROOF WALLS
						float hd = 0, th = 0, vd = 0;
						if (i < roof.length - 1) {
							if (roof[i + 1][j] > roof[i][j] && roof[i + 1][j] != 0.0f) {
								hd = roof[i + 1][j] - roof[i][j];
								int k = 0;
								do {
									vd = hd - k * 2;
									th = vd * 8 * ph;
									GL11.glBegin(GL11.GL_QUADS);
										GL11.glTexCoord2f((tx + 1) * w, ty * h + th);
										GL11.glVertex3f(i * 2 + 2, roof[i][j], j * 2);
										GL11.glTexCoord2f((tx + 1) * w, ty * h);
										GL11.glVertex3f(i * 2 + 2, roof[i][j] + vd, j * 2);
										GL11.glTexCoord2f(tx * w, ty * h);
										GL11.glVertex3f(i * 2 + 2, roof[i][j] + vd, j * 2 + 2);
										GL11.glTexCoord2f(tx * w, ty * h + th);
										GL11.glVertex3f(i * 2 + 2, roof[i][j], j * 2 + 2);
									GL11.glEnd();
									k++;
								} while (k < hd / 2);
							}
						}
						if (j < roof[i].length - 1) {
							if (roof[i][j + 1] > roof[i][j] && roof[i][j + 1] != 0.0f) {
								hd = roof[i][j + 1] - roof[i][j];
								int k = 0;
								do {
									vd = hd - k * 2;
									th = vd * 8 * ph;
									GL11.glBegin(GL11.GL_QUADS);
										GL11.glTexCoord2f(tx * w, ty * h + th);
										GL11.glVertex3f(i * 2, roof[i][j], j * 2 + 2);
										GL11.glTexCoord2f((tx + 1) * w, ty * h + th);
										GL11.glVertex3f(i * 2 + 2, roof[i][j], j * 2 + 2);
										GL11.glTexCoord2f((tx + 1) * w, ty * h);
										GL11.glVertex3f(i * 2 + 2, roof[i][j] + vd, j * 2 + 2);
										GL11.glTexCoord2f(tx * w, ty * h);
										GL11.glVertex3f(i * 2, roof[i][j] + vd, j * 2 + 2);
									GL11.glEnd();
									k++;
								} while (k < hd / 2);
							}
						}
						if (i > 0) {
							if (roof[i - 1][j] > roof[i][j] && roof[i - 1][j] != 0.0f) {
								hd = roof[i - 1][j] - roof[i][j];
								int k = 0;
								do {
									vd = hd - k * 2;
									th = vd * 8 * ph;
									GL11.glBegin(GL11.GL_QUADS);
										GL11.glTexCoord2f(tx * w, ty * h + th);
										GL11.glVertex3f(i * 2, roof[i][j], j * 2);
										GL11.glTexCoord2f((tx + 1) * w, ty * h + th);
										GL11.glVertex3f(i * 2, roof[i][j], j * 2 + 2);
										GL11.glTexCoord2f((tx + 1) * w, ty * h);
										GL11.glVertex3f(i * 2, roof[i][j] + vd, j * 2 + 2);
										GL11.glTexCoord2f(tx * w, ty * h);
										GL11.glVertex3f(i * 2, roof[i][j] + vd, j * 2);
									GL11.glEnd();
									k++;
								} while (k < hd / 2);
							}
						}
						if (j > 0) {
							if (roof[i][j - 1] > roof[i][j] && roof[i][j - 1] != 0.0f) {
								hd = roof[i][j - 1] - roof[i][j];
								int k = 0;
								do {
									vd = hd - k * 2;
									th = vd * 8 * ph;
									GL11.glBegin(GL11.GL_QUADS);
										GL11.glTexCoord2f((tx + 1) * w, ty * h + th);
										GL11.glVertex3f(i * 2, roof[i][j], j * 2);
										GL11.glTexCoord2f((tx + 1) * w, ty * h);
										GL11.glVertex3f(i * 2, roof[i][j] + vd, j * 2);
										GL11.glTexCoord2f(tx * w, ty * h);
										GL11.glVertex3f(i * 2 + 2, roof[i][j] + vd, j * 2);
										GL11.glTexCoord2f(tx * w, ty * h + th);
										GL11.glVertex3f(i * 2 + 2, roof[i][j], j * 2);
									GL11.glEnd();
									k++;
								} while (k < hd / 2);
							}
						}
					}
				}
				
			}
		}
	}
	
	public int getStartX() {
		return generator.getStartX();
	}
	
	public int getStartZ() {
		return generator.getStartZ();
	}
	
	public int getEndX() {
		return generator.getEndX();
	}
	
	public int getEndZ() {
		return generator.getEndZ();
	}
}
