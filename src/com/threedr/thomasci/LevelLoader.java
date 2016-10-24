package com.threedr.thomasci;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LevelLoader {
	public int[][][] loadTiles(String location) {
		int[][][] lev = new int[2][0][0];
		
		//System.out.println("Tile Map:");
		lev[0] = loadMap(location + "/tile.lev");
		lev[1] = loadMap(location + "/tiler.lev");
		/*if (lev[0].length != lev[1].length ||
				lev[0][0].length != lev[1][0].length ||
				lev[0].length != lev[2].length ||
				lev[0][0].length != lev[2][0].length ||
				lev[1].length != lev[2].length ||
				lev[1][0].length != lev[2][0].length) {
			System.out.println("Maps are not the same dimensions!");
			System.exit(0);
		} else {
			System.out.println("Maps loaded succesfully");
		}*/
		
		return lev;
	}
	public float[][][] loadHeight(String location) {
		float[][][] lev = new float[2][0][0];
		
		lev[0] = loadFMap(location + "/height.lev");
		lev[1] = loadFMap(location + "/heightr.lev");
		
		return lev;
	}
	
	/*public EDat[] loadEntities(String location) {
		EDat[] ents = new EDat[0];
		
		ents = loadEnts(location + "_e.txt");
		
		return ents;
	}*/
	
	private int[][] loadMap(String location) {
		File file = new File(location);
		int[][] lev = new int[0][0];
		
		if (file.exists()) {
			try {
				FileReader reader = new FileReader(file);
				BufferedReader in = new BufferedReader(reader);
				String line = "";
				int lnum = 0;
				
				while ((line = in.readLine()) != null) {
					lev = incArray(lev);
					String[] str = line.split(",");
					if (lnum == 0) lev = new int[1][str.length];
					for (int i = 0; i < str.length; i++) {
						lev[lnum][i] = Integer.parseInt(str[i]);
						//System.out.print(lev[lnum][i] + ",");
					}
					//System.out.println();
					lnum++;
				}
				
				in.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return lev;
	}
	
	private float[][] loadFMap(String location) {
		File file = new File(location);
		float[][] lev = new float[0][0];
		
		if (file.exists()) {
			try {
				FileReader reader = new FileReader(file);
				BufferedReader in = new BufferedReader(reader);
				String line = "";
				int lnum = 0;
				
				while ((line = in.readLine()) != null) {
					lev = incFArray(lev);
					String[] str = line.split(",");
					if (lnum == 0) lev = new float[1][str.length];
					for (int i = 0; i < str.length; i++) {
						lev[lnum][i] = Float.parseFloat(str[i]);
						//System.out.print(lev[lnum][i] + ",");
					}
					//System.out.println();
					lnum++;
				}
				
				in.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return lev;
	}
	
	public ArrayList<EDat> loadEnts(String location) {
		File file = new File(location + "/ent.lev");
		ArrayList<EDat> data = new ArrayList<EDat>();
		
		if (file.exists()) {
			try {
				EDat e;
				FileReader reader = new FileReader(file);
				BufferedReader in = new BufferedReader(reader);
				String line = "";
				
				while ((line = in.readLine()) != null) {
					String[] str = line.split(",");
					e = new EDat(str);
					data.add(e);
				}
				
				in.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
	
	public String[] loadData(String location) {
		File file = new File(location + "/dat.lev");
		String[] data = new String[0];
		
		if (file.exists()) {
			try {
				FileReader reader = new FileReader(file);
				BufferedReader in = new BufferedReader(reader);
				String line = "";
				
				while ((line = in.readLine()) != null) {
					data = incSArray(data);
					data[data.length - 1] = line;
				}
				
				in.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(data.length);
		return data;
	}
	
	/*private EDat[] loadEnts(String location) {
		File file = new File(location);
		EDat[] ent = new EDat[0];
		
		if (file.exists()) {
			try {
				FileReader reader = new FileReader(file);
				BufferedReader in = new BufferedReader(reader);
				String line = "";
				int lnum = 0;
				
				while ((line = in.readLine()) != null) {
					ent = incEArray(ent);
					String[] str = line.split(",");
					if (lnum == 0) ent = new EDat[1];
					for (int i = 0; i < str.length; i++) {
						ent[lnum] = new EDat(Integer.parseInt(str[0]),
								Integer.parseInt(str[1]),
								Integer.parseInt(str[2]),
								Integer.parseInt(str[3]));
					}
					lnum++;
				}
				
				in.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ent;
	}*/
	
	private int[][] incArray(int[][] arr) {
		if (arr.length > 0) {
			int[][] temp = new int[arr.length + 1][arr[0].length];
			
			for (int i = 0; i < arr.length; i++) {
				temp[i] = arr[i];
			}
			
			return temp;
		} else return new int[1][0];
	}
	
	private float[][] incFArray(float[][] arr) {
		if (arr.length > 0) {
			float[][] temp = new float[arr.length + 1][arr[0].length];
			
			for (int i = 0; i < arr.length; i++) {
				temp[i] = arr[i];
			}
			
			return temp;
		} else return new float[1][0];
	}
	
	private String[] incSArray(String[] arr) {
		if (arr.length > 0) {
			String[] temp = new String[arr.length + 1];
			
			for (int i = 0; i < arr.length; i++) {
				temp[i] = arr[i];
			}
			
			return temp;
		} else return new String[1];
	}
}
