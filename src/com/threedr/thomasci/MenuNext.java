package com.threedr.thomasci;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class MenuNext {
	private MenuButton nextLev, quit;
	private boolean mdown;
	private final Game game;
	
	public MenuNext(Game g) {
		nextLev = new MenuButton(TDR.width / 2 - 136, TDR.height / 2 - 32, "Continue");
		quit = new MenuButton(TDR.width / 2 + 136, TDR.height / 2 - 32, "Save and Quit");
		game = g;
		mdown = true;
	}
	
	public void update() {
		int mx = Mouse.getX();
		int my = TDR.height - Mouse.getY();
		
		nextLev.setOver(false);
		quit.setOver(false);
		
		if (nextLev.isOver(mx, my)) {
			nextLev.setOver(true);
		} else if (quit.isOver(mx, my)) {
			quit.setOver(true);
		}
		
		if (Mouse.isButtonDown(0)) {
			if (!mdown) {
				if (nextLev.isOver()) {
					game.nextLevel();
				} else if (quit.isOver()) {
					game.saveGame();
				}
			}
			mdown = true;
		} else {
			mdown = false;
		}
	}
	
	public void draw() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.75f);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex3f(0, TDR.height, 0);
			GL11.glVertex3f(TDR.width, TDR.height, 0);
			GL11.glVertex3f(TDR.width, 0, 0);
			GL11.glVertex3f(0, 0, 0);
		GL11.glEnd();
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		nextLev.draw();
		quit.draw();
		GL11.glTranslatef(TDR.width / 2, TDR.height / 2 - 150, 0);
		Text.drawText("§0You have completed the level, would you like to...", 2, true);
	}
}
