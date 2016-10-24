package com.threedr.thomasci;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class MenuFinish {
	private MenuButton finish;
	private boolean mdown;
	private Game game;
	
	public MenuFinish(Game g) {
		finish = new MenuButton(TDR.width / 2, TDR.height / 2 + 64, "Finish");
		game = g;
	}
	
	public void update() {
		int mx = Mouse.getX();
		int my = TDR.height - Mouse.getY();
		
		finish.setOver(false);
		
		if (finish.isOver(mx, my)) {
			finish.setOver(true);
		}
		
		if (Mouse.isButtonDown(0)) {
			if (!mdown) {
				if (finish.isOver()) {
					game.exitGame();
				}
			}
			mdown = true;
		} else {
			mdown = false;
		}
	}
	
	public void draw() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex3f(0, TDR.height, 0);
			GL11.glVertex3f(TDR.width, TDR.height, 0);
			GL11.glVertex3f(TDR.width, 0, 0);
			GL11.glVertex3f(0, 0, 0);
		GL11.glEnd();
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		finish.draw();
		GL11.glTranslatef(TDR.width / 2, TDR.height / 2 - 150, 0);
		Text.drawText("§0Well done, you have completed the campaign!", 2, true);
	}
}
