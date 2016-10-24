package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;

public class Letter {
	public static int getLX(char c) {
		switch (c) {
			case 'b': return 4;
			case 'c': return 8;
			case 'd': return 12;
			case 'e': return 16;
			case 'f': return 20;
			case 'g': return 24;
			case 'h': return 28;
			case 'i': return 32;
			case 'j': return 34;
			case 'k': return 36;
			case 'l': return 40;
			case 'm': return 42;
			case 'n': return 49;
			case 'o': return 53;
			case 'p': return 57;
			case 'q': return 61;
			case 'r': return 65;
			case 's': return 69;
			case 't': return 73;
			case 'u': return 76;
			case 'v': return 80;
			case 'w': return 85;
			case 'x': return 92;
			case 'y': return 97;
			case 'z': return 101;
			
			case 'B': return 5;
			case 'C': return 10;
			case 'D': return 15;
			case 'E': return 20;
			case 'F': return 24;
			case 'G': return 28;
			case 'H': return 33;
			case 'I': return 38;
			case 'J': return 41;
			case 'K': return 46;
			case 'L': return 51;
			case 'M': return 55;
			case 'N': return 62;
			case 'O': return 67;
			case 'P': return 72;
			case 'Q': return 77;
			case 'R': return 82;
			case 'S': return 87;
			case 'T': return 92;
			case 'U': return 97;
			case 'V': return 102;
			case 'W': return 107;
			case 'X': return 116;
			case 'Y': return 121;
			case 'Z': return 126;
			
			case '2': return 2;
			case '3': return 7;
			case '4': return 12;
			case '5': return 17;
			case '6': return 22;
			case '7': return 27;
			case '8': return 32;
			case '9': return 37;
			case '0': return 42;
			
			case '?': return 105;
			case '~': return 110;
			case '[': return 114;
			case ']': return 116;
			case '{': return 118;
			case '}': return 121;
			case '\\': return 124;
			case ' ': return 129;
			case '+': return 47;
			case '-': return 52;
			case '*': return 55;
			case '/': return 58;
			case '=': return 63;
			case '$': return 67;
			case '%': return 72;
			case '^': return 77;
			case '&': return 80;
			case '(': return 85;
			case ')': return 88;
			case '_': return 91;
			case ':': return 95;
			case ';': return 96;
			case '\'': return 98;
			case '.': return 100;
			case ',': return 101;
			case '!': return 103;
			case '@': return 104;
			case '#': return 110;
			case '"': return 116;
			case '<': return 120;
			case '>': return 124;
			case '|': return 128;
			case '`': return 129;
		}
		return 0;
	}
	
	public static int getLY(char c) {
		//ALL CHARACTERS NOT ON LINE 0
		switch (c) {
			case 'A': return 1;
			case 'B': return 1;
			case 'C': return 1;
			case 'D': return 1;
			case 'E': return 1;
			case 'F': return 1;
			case 'G': return 1;
			case 'H': return 1;
			case 'I': return 1;
			case 'J': return 1;
			case 'K': return 1;
			case 'L': return 1;
			case 'M': return 1;
			case 'N': return 1;
			case 'O': return 1;
			case 'P': return 1;
			case 'Q': return 1;
			case 'R': return 1;
			case 'S': return 1;
			case 'T': return 1;
			case 'U': return 1;
			case 'V': return 1;
			case 'W': return 1;
			case 'X': return 1;
			case 'Y': return 1;
			case 'Z': return 1;
			
			case '0': return 2;
			case '1': return 2;
			case '2': return 2;
			case '3': return 2;
			case '4': return 2;
			case '5': return 2;
			case '6': return 2;
			case '7': return 2;
			case '8': return 2;
			case '9': return 2;
			
			case '+': return 2;
			case '-': return 2;
			case '*': return 2;
			case '/': return 2;
			case '=': return 2;
			case '$': return 2;
			case '%': return 2;
			case '^': return 2;
			case '&': return 2;
			case '(': return 2;
			case ')': return 2;
			case '_': return 2;
			case ':': return 2;
			case ';': return 2;
			case '\'': return 2;
			case '.': return 2;
			case ',': return 2;
			case '!': return 2;
			case '@': return 2;
			case '#': return 2;
			case '"': return 2;
			case '<': return 2;
			case '>': return 2;
			case '|': return 2;
			case '`': return 2;
		}
		return 0;
	}

	public static int getSpacing(char c) {
		//this is assuming all letters are 5 pixels wide.
		//ALL CHARACTERS NOT INCLUDED HAVE NORMAL SPACING
		switch (c) {
			case 'a': return -1;
			case 'b': return -1;
			case 'c': return -1;
			case 'd': return -1;
			case 'e': return -1;
			case 'f': return -1;
			case 'g': return -1;
			case 'h': return -1;
			case 'i': return -3;
			case 'j': return -3;
			case 'k': return -1;
			case 'l': return -3;
			case 'm': return 2;
			case 'n': return -1;
			case 'o': return -1;
			case 'p': return -1;
			case 'q': return -1;
			case 'r': return -1;
			case 's': return -1;
			case 't': return -2;
			case 'u': return -1;
			case 'w': return 2;
			case 'y': return -1;
			case 'z': return -1;
			
			case 'E': return -1;
			case 'F': return -1;
			case 'I': return -2;
			case 'L': return -1;
			case 'M': return 2;
			case 'W': return 4;
			
			case '1': return -3;
			
			case '~': return -1;
			case '[': return -3;
			case ']': return -3;
			case '{': return -2;
			case '}': return -2;
			case ' ': return -3;
			
			case '-': return -2;
			case '*': return -2;
			case '=': return -1;
			case '^': return -2;
			case '(': return -2;
			case ')': return -2;
			case '_': return -1;
			case ':': return -4;
			case ';': return -3;
			case '\'': return -3;
			case '.': return -4;
			case ',': return -3;
			case '!': return -4;
			case '@': return 1;
			case '#': return 1;
			case '"': return -1;
			case '<': return -1;
			case '>': return -1;
			case '|': return -4;
			case '`': return -3;
		}
		return 0;
	}
	
	public static void colour(char c) {
		switch (c) {
			case '0': GL11.glColor3f(1.0f, 1.0f, 1.0f); break; //white
			case '1': GL11.glColor3f(0.0f, 0.0f, 0.0f); break; //black
			case '2': GL11.glColor3f(0.25f, 0.25f, 0.25f); break; //dark grey
			case '3': GL11.glColor3f(0.5f, 0.5f, 0.5f); break; //grey
			case '4': GL11.glColor3f(0.75f, 0.75f, 0.75f); break; //light grey
			case '5': GL11.glColor3f(0.75f, 0.0f, 0.0f); break; //red
			case '6': GL11.glColor3f(0.0f, 0.25f, 0.0f); break; //green
			case '7': GL11.glColor3f(0.0f, 0.0f, 0.5f); break; //blue
			case '8': GL11.glColor3f(0.75f, 0.75f, 0.0f); break; //yellow
			case '9': GL11.glColor3f(0.0f, 0.75f, 0.75f); break; //cyan
			case 'a': GL11.glColor3f(0.75f, 0.0f, 0.75f); break; //pink
		}
	}
}
