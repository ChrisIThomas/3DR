package com.threedr.thomasci;

public class Door {
	public int x, y, rid;
	public boolean connected, hall;
	public Door con;
	
	public Door(int _x, int _y, int i) {
		x = _x;
		y = _y;
		rid = i;
		hall = false;
	}
}
