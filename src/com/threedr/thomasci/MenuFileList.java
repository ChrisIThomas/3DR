package com.threedr.thomasci;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class MenuFileList {
	private Vector2f pos;
	private String[][] data;
	private int selected, size;
	private float drag;
	private boolean over;
	private Texture tex;
	
	public MenuFileList(int x, int y) {
		data = new String[0][0];
		pos = new Vector2f(x, y);
		File file = new File("lev");
		File[] dat = file.listFiles();
		for (int i = 0; i < dat.length; i++) {
			if (dat[i].isDirectory()) {
				File f = new File("lev/" + dat[i].getName() + "/info.txt");
				
				if (f.exists()) {
					try {
						FileReader reader = new FileReader(f);
						BufferedReader in = new BufferedReader(reader);
						String newName = in.readLine();
						String newInfo = in.readLine();
						
						in.close();
						data = incArray(data);
						data[data.length - 1][0] = dat[i].getName();
						data[data.length - 1][1] = newName;
						data[data.length - 1][2] = newInfo;
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		size = (int) ((TDR.height - pos.y - 128) / 64);
		tex = TDR.getTexture("back", "img/back.png");
	}
	
	private String[][] incArray(String[][] arr) {
		if (arr.length > 0) {
			String[][] temp = new String[arr.length + 1][3];
			
			for (int i = 0; i < arr.length; i++) {
				temp[i] = arr[i];
			}
			
			return temp;
		} else return new String[1][3];
	}
	
	public boolean isOver(int x, int y) {
		if (x >= pos.x - 8 && x <= pos.x + 256) {
			if (y >= pos.y - 23 && y <= pos.y + 41 + (data.length - 1) * 64) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isOver() {
		return over;
	}
	
	public void setOver(boolean val) {
		over = val;
	}
	
	public void click(int x, int y) {
		y -= pos.y;
		if (y >= 0 && y <= size * 64) {
			y += -drag;
			if (y / 64 < data.length) selected = y / 64;
		}
	}
	
	public void drag(int dy) {
		if (data.length * 64 > 256) {
			drag += dy;
			if (drag < -data.length * 64 + size * 64) drag = -data.length * 64 + size * 64;
			if (drag > 0) drag = 0;
		}
	}
	
	public String getSelectedLoc() {
		return data[selected][0];
	}
	
	public void draw() {
		GL11.glPushMatrix();
		float w = TDR.width - 464;
		
		GL11.glTranslatef(pos.x, pos.y, 0);
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glColor3f(0.0f, 0.0f, 0.0f);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex3f(-8, 64 * size, 0.0f);
			GL11.glVertex3f(256, 64 * size, 0.0f);
			GL11.glVertex3f(256, 0, 0.0f);
			GL11.glVertex3f(-8, 0, 0.0f);
		GL11.glEnd();
		
		GL11.glColor3f(0.1f, 0.1f, 0.1f);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex3f(256, 64 * size, 0.0f);
			GL11.glVertex3f(w + 256, 64 * size, 0.0f);
			GL11.glVertex3f(w + 256, 0, 0.0f);
			GL11.glVertex3f(256, 0, 0.0f);
		GL11.glEnd();
		
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glTranslatef(0, drag, 0);
		for (int i = 0; i < data.length; i++) {
			if (i * 64 >= -(drag - size * 64)) break;
			if (i * 64 <= -drag - 64) {
				GL11.glTranslatef(0, 64, 0);
				continue;
			}
			if (i == selected) {
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glColor3f(0.1f, 0.1f, 0.1f);
				GL11.glBegin(GL11.GL_QUADS);
					GL11.glVertex3f(-8, 64, 0.0f);
					GL11.glVertex3f(256, 64, 0.0f);
					GL11.glVertex3f(256, 0, 0.0f);
					GL11.glVertex3f(-8, 0, 0.0f);
				GL11.glEnd();
				GL11.glColor3f(1.0f, 1.0f, 1.0f);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
			
			GL11.glTranslatef(0, 23, 0); //odd values to center the text manually
			Text.drawText("§0" + data[i][1], 2, false);
			GL11.glTranslatef(0, 41, 0);
		}
		
		GL11.glPopMatrix();
		
		GL11.glTranslatef(272, 23, 0);
		Text.drawTextBox("§0" + data[selected][2], 2, w - 8);
		
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		
		GL11.glTranslatef(pos.x, pos.y, 0);
		
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f((pos.x - 8) / 64, pos.y / 64);
			GL11.glVertex3f(-8, 0, 0);
			GL11.glTexCoord2f((pos.x + w + 256) / 64.0f, pos.y / 64);
			GL11.glVertex3f(w + 256, 0, 0);
			GL11.glTexCoord2f((pos.x + w + 256) / 64.0f, (pos.y - 64) / 64);
			GL11.glVertex3f(w + 256, -64, 0);
			GL11.glTexCoord2f((pos.x - 8) / 64, (pos.y - 64) / 64);
			GL11.glVertex3f(-8, -64, 0);
		GL11.glEnd();

		GL11.glTranslatef(0, size * 64, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f((pos.x - 8) / 64, (pos.y + 64)  / 64);
			GL11.glVertex3f(-8, 64, 0);
			GL11.glTexCoord2f((pos.x + w + 256) / 64.0f, (pos.y + 64) / 64);
			GL11.glVertex3f(w + 256, 64, 0);
			GL11.glTexCoord2f((pos.x + w + 256) / 64.0f, pos.y / 64);
			GL11.glVertex3f(w + 256, 0, 0);
			GL11.glTexCoord2f((pos.x - 8) / 64, pos.y / 64);
			GL11.glVertex3f(-8, 0, 0);
		GL11.glEnd();
		
		GL11.glPopMatrix();
	}
}
