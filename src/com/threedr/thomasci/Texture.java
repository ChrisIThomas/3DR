package com.threedr.thomasci;

public class Texture {
	private int tex, width, height;
	private String name;
	
	public Texture(String name, int id) {
		tex = id;
		this.name = name;
	}
	
	public void setSize(int w, int h) {
		width = w;
		height = h;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getTex() {
		return tex;
	}
	
	public String getName() {
		return name;
	}
}
