package com.threedr.thomasci;

public class PNode {
	int g, h, f, px, py;
	
	public PNode(int g, int h, int px, int py) {
		this.g = g;
		this.h = h;
		f = g + h;
		this.px = px;
		this.py = py;
	}

	public void updateg(int g) {
		this.g = g;
		f = g + h;
	}
}
