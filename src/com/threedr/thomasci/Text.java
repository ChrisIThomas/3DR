package com.threedr.thomasci;

import org.lwjgl.opengl.GL11;

public class Text {
	private static Texture tex;
	
	static {
		tex = TDR.getTexture("font", "img/font.png");
	}
	
	private static float[] incArray(float[] arr) {
		float[] newArr = new float[arr.length + 1];
		for (int i = 0; i < arr.length; i++) {
			newArr[i] = arr[i];
		}
		return newArr;
	}
	
	public static void drawText(String text, float scale, boolean center) {
		float pw = 1.0f / tex.getWidth();
		float th = 1.0f / tex.getHeight() * 9;
		float x = 0.0f;
		float y = 0.0f;
		float w = 0.0f;
		float textOff = 0.0f;
		char c = ' ';
		GL11.glPushMatrix();
		int numLine = 0;
		float[] wo = new float[1];
		if (center) {
			float womax = 0.0f;
			for (int i = 0; i < text.length(); i++) {
				c = text.charAt(i);
				if (c == '§') {
					i++;
				} else if (c == '\n') {
					if (wo[numLine] > womax) womax = wo[numLine];
					numLine++;
					wo = incArray(wo);
					wo[numLine] = 0.0f;
				} else {
					wo[numLine] += (6 + Letter.getSpacing(c)) * scale;
				}	
			}
			if (wo[numLine] > womax) womax = wo[numLine];
			GL11.glTranslatef(-womax / 2, -9 * scale / 2, 0);
		}
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		numLine = 0;
		for (int i = 0; i < text.length(); i++) {
			c = text.charAt(i);
			if (c == '§') {
				i++;
				Letter.colour(text.charAt(i));
			} else if (c == '\n') {
				if (center) {
					GL11.glTranslatef(-wo[numLine] / 2, 10 * scale, 0);
					numLine++;
					GL11.glTranslatef(-wo[numLine] / 2, 0, 0);
				} else {
					GL11.glTranslatef(-textOff, 10 * scale, 0);
					textOff = 0;
				}
			} else {
				w = 5 + Letter.getSpacing(c);
				x = Letter.getLX(c) * pw;
				y = Letter.getLY(c) * th;
				GL11.glBegin(GL11.GL_QUADS);
					GL11.glTexCoord2f(x, y + th);
					GL11.glVertex2f(0.0f, 9 * scale);
					GL11.glTexCoord2f(x + w * pw, y + th);
					GL11.glVertex2f(w * scale, 9 * scale);
					GL11.glTexCoord2f(x + w * pw, y);
					GL11.glVertex2f(w * scale, 0.0f);
					GL11.glTexCoord2f(x, y);
					GL11.glVertex2f(0.0f, 0.0f);
				GL11.glEnd();
				GL11.glTranslatef((w + 1) * scale, 0, 0);
				textOff += (w + 1) * scale;
			}
		}
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glPopMatrix();
	}
	
	public static void drawTextBox(String text, float scale, float width) {
		float pw = 1.0f / tex.getWidth();
		float th = 1.0f / tex.getHeight() * 9;
		float x = 0.0f;
		float y = 0.0f;
		float w = 0.0f;
		char c = ' ';
		
		GL11.glPushMatrix();
		float wo = 0;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
		for (int i = 0; i < text.length(); i++) {
			c = text.charAt(i);
			if (c == ' ') {
				float wordWidth = 0.0f;
				for (int j = i + 1; j < text.length(); j++) {
					if (text.charAt(j) == ' ') {
						wordWidth += 3 * scale;
						break;
					} else {
						wordWidth += (6 + Letter.getSpacing(text.charAt(j))) * scale;
					}
				}
				if (wo + wordWidth + 8 >= width) {
					GL11.glTranslatef(-wo, 10 * scale, 0);
					wo = 0;
				} else {
					GL11.glTranslatef(3 * scale, 0, 0);
					wo += 3 * scale;
				}
			} else if (c == '§') {
				i++;
				Letter.colour(text.charAt(i));
			} else if (c == '\n') {
				GL11.glTranslatef(-wo, 10 * scale, 0);
				wo = 0;
			} else {
				if (wo >= width) {
					GL11.glTranslatef(-wo, 10 * scale, 0);
					wo = 0;
				}
				w = 5 + Letter.getSpacing(c);
				x = Letter.getLX(c) * pw;
				y = Letter.getLY(c) * th;
				GL11.glBegin(GL11.GL_QUADS);
					GL11.glTexCoord2f(x, y + th);
					GL11.glVertex2f(0.0f, 9 * scale);
					GL11.glTexCoord2f(x + w * pw, y + th);
					GL11.glVertex2f(w * scale, 9 * scale);
					GL11.glTexCoord2f(x + w * pw, y);
					GL11.glVertex2f(w * scale, 0.0f);
					GL11.glTexCoord2f(x, y);
					GL11.glVertex2f(0.0f, 0.0f);
				GL11.glEnd();
				GL11.glTranslatef((w + 1) * scale, 0, 0);
				wo += (w + 1) * scale;
			}
		}
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		GL11.glPopMatrix();
	}
}
