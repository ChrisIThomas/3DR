package com.threedr.thomasci;

import java.io.File;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class Menu {
	private float qscale, qscaledir;
	private MenuButton select, load, options, quit;
	private MenuButton advBegin, advBack;
	private MenuButton gameBack, gameQuit;
	private MenuButton sureYes, sureNo;
	private MenuButton optionBack, optionParticle, optionFunky, optionSens;
	private MenuFileList adventures;
	private final TDR tdr;
	private boolean mdown;
	private Texture tex;
	private int menu, curmenu;
	//0 = main menu
	//1 = adventure select
	//2 = in game menu
	//3 = are you sure you want to quit
	
	public Menu(TDR t) {
		qscale = 1.75f;
		qscaledir = 0.05f;
		
		select = new MenuButton(TDR.width / 2, 256, "Choose an Adventure");
		load = new MenuButton(TDR.width / 2, 352, "Load");
		if (!(new File("sav/game.sav").exists())) load.setActive(false);
		options = new MenuButton(TDR.width / 2, 448, "Options");
		quit = new MenuButton(TDR.width / 2, 544, "Quit");
		
		advBegin = new MenuButton(TDR.width / 2 - 136, TDR.height - 64, "Begin");
		advBack = new MenuButton(TDR.width / 2 + 136, TDR.height - 64, "Back");
		
		gameBack = new MenuButton(TDR.width / 2, 384, "Back");
		gameQuit = new MenuButton(TDR.width / 2, 512, "Quit");
		
		sureYes = new MenuButton(TDR.width / 2 - 136, TDR.height / 2 - 32, "Yes");
		sureNo = new MenuButton(TDR.width / 2 + 136, TDR.height / 2 - 32, "No");
		
		optionBack = new MenuButton(TDR.width / 2, TDR.height - 64, "Back");
		optionParticle = new MenuButton(TDR.width / 2 - 136, TDR.height / 2 - 32, "Particles");
		optionFunky = new MenuButton(TDR.width / 2 + 136, TDR.height / 2 - 32, "Funky");
		optionSens = new MenuButton(TDR.width / 2, TDR.height / 2 + 48, "Sensitivity: High");
		
		if (TDR.moreParticles) {
			optionParticle.setText("More Particles");
		} else {
			optionParticle.setText("Less Particles");
		}
		if (TDR.funky) {
			optionFunky.setText("Funky");
		} else {
			optionFunky.setText("Not Funky");
		}
		switch (TDR.sensitivity) {
			case 2:
				optionSens.setText("Sensitivity: Med");
				break;
			case 4:
				optionSens.setText("Sensitivity: Low");
				break;
		}
		
		adventures = new MenuFileList(TDR.width / 2 - TDR.width / 2 + 100, 180);
		
		tex = TDR.getTexture("back", "img/back.png");
		tdr = t;
		menu = 0;
		curmenu = menu;
	}
	
	public void update() {
		if (qscale > 2.0f) qscaledir = -0.015f;
		else if (qscale < 1.75f) qscaledir = 0.015f;
		
		int mx = Mouse.getX();
		int my = TDR.height - Mouse.getY();
		
		select.setOver(false);
		load.setOver(false);
		options.setOver(false);
		quit.setOver(false);
		advBegin.setOver(false);
		advBack.setOver(false);
		gameBack.setOver(false);
		gameQuit.setOver(false);
		sureYes.setOver(false);
		sureNo.setOver(false);
		optionBack.setOver(false);
		optionParticle.setOver(false);
		optionFunky.setOver(false);
		optionSens.setOver(false);
		
		if (curmenu != menu) {
			if (menu == 0) {
				if (!(new File("sav/game.sav").exists())) {
					load.setActive(false);
				} else {
					load.setActive(true);
				}
			}
			curmenu = menu;
		}
		
		if (menu == 0) {
			if (select.isOver(mx, my)) {
				select.setOver(true);
			} else if (load.isOver(mx, my)) {
				if (load.isActive()) load.setOver(true);
			} else if (options.isOver(mx, my)) {
				if (options.isActive()) options.setOver(true);
			} else if (quit.isOver(mx, my)) {
				quit.setOver(true);
			}
		} else if (menu == 1) {
			if (advBegin.isOver(mx, my)) {
				advBegin.setOver(true);
			} else if (advBack.isOver(mx, my)) {
				advBack.setOver(true);
			}
		} else if (menu == 2) {
			if (gameBack.isOver(mx, my)) {
				gameBack.setOver(true);
			} else if (gameQuit.isOver(mx, my)) {
				gameQuit.setOver(true);
			}
		} else if (menu == 3) {
			if (sureYes.isOver(mx, my)) {
				sureYes.setOver(true);
			} else if (sureNo.isOver(mx, my)) {
				sureNo.setOver(true);
			}
		} else if (menu == 4) {
			if (optionBack.isOver(mx, my)) {
				optionBack.setOver(true);
			} else if (optionParticle.isOver(mx, my)) {
				optionParticle.setOver(true);
			} else if (optionFunky.isOver(mx, my)) {
				optionFunky.setOver(true);
			} else if (optionSens.isOver(mx, my)) {
				optionSens.setOver(true);
			}
		}
		
		if (Mouse.isButtonDown(0)) {
			if (!mdown) {
				if (menu == 0) {
					if (select.isOver()) {
						menu = 1;
					} else if (load.isOver()) {
						tdr.startGame("", true);
					} else if (options.isOver()) {
						menu = 4;
						TDR.saveOptions();
					} else if (quit.isOver()) {
						tdr.quit();
					}
				} else if (menu == 1) {
					if (adventures.isOver(mx, my)) {
						adventures.click(mx, my);
						adventures.setOver(true);
					} else if (advBegin.isOver()) {
						tdr.startGame(adventures.getSelectedLoc(), false);
					} else if (advBack.isOver()) {
						menu = 0;
					}
				} else if (menu == 2) {
					if (gameBack.isOver()) {
						tdr.setMenu(false);
					} else if (gameQuit.isOver()) {
						menu = 3;
					}
				} else if (menu == 3) {
					if (sureYes.isOver()) {
						menu = 0;
						tdr.stopGame();
					} else if (sureNo.isOver()) {
						menu = 2;
					}
				} else if (menu == 4) {
					if (optionBack.isOver()) {
						menu = 0;
						TDR.saveOptions();
					} else if (optionParticle.isOver()) {
						TDR.moreParticles = !TDR.moreParticles;
						if (TDR.moreParticles) {
							optionParticle.setText("More Particles");
						} else {
							optionParticle.setText("Less Particles");
						}
					} else if (optionFunky.isOver()) {
						TDR.funky = !TDR.funky;
						if (TDR.funky) {
							optionFunky.setText("Funky");
						} else {
							optionFunky.setText("Not Funky");
						}
					} else if (optionSens.isOver()) {
						
						switch (TDR.sensitivity) {
							case 1:
								TDR.sensitivity = 2;
								optionSens.setText("Sensitivity: Med");
								break;
							case 2:
								TDR.sensitivity = 4;
								optionSens.setText("Sensitivity: Low");
								break;
							case 4:
								TDR.sensitivity = 1;
								optionSens.setText("Sensitivity: High");
								break;
						}
					}
				}
			} else {
				if (menu == 1) {
					if (adventures.isOver()) {
						adventures.drag(-Mouse.getDY());
					}
				}
			}
			mdown = true;
		} else {
			mdown = false;
			adventures.setOver(false);
		}
	}
	
	public void draw() {
		qscale += qscaledir;
		
		if (menu != 2 && menu != 3) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTex());
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glTexCoord2f(0, TDR.height / 64.0f);
				GL11.glVertex3f(0, TDR.height, 0);
				GL11.glTexCoord2f(TDR.width / 64.0f, TDR.height / 64.0f);
				GL11.glVertex3f(TDR.width, TDR.height, 0);
				GL11.glTexCoord2f(TDR.width / 64.0f, 0);
				GL11.glVertex3f(TDR.width, 0, 0);
				GL11.glTexCoord2f(0, 0);
				GL11.glVertex3f(0, 0, 0);
			GL11.glEnd();
		} else {
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
		}
		
		if (menu == 0) {
			select.draw();
			load.draw();
			options.draw();
			quit.draw();
		} else if (menu == 1) {
			adventures.draw();
			advBegin.draw();
			advBack.draw();
		} else if (menu == 2) {
			gameBack.draw();
			gameQuit.draw();
			GL11.glTranslatef(TDR.width / 2, 100, 0);
			Text.drawText("§0GAME PAUSED", 6, true);
		} else if (menu == 4) {
			optionBack.draw();
			optionParticle.draw();
			optionFunky.draw();
			optionSens.draw();
		}
		if (menu == 3) {
			sureYes.draw();
			sureNo.draw();
			GL11.glTranslatef(TDR.width / 2, TDR.height / 2 - 150, 0);
			Text.drawText("§0Are you sure you want to quit, your game will not be saved\nand you will have to start all over again.", 2, true);
		}
		
		if (menu != 2 && menu != 3) {
			GL11.glTranslatef(TDR.width / 2, 100, 0);
			Text.drawText("§0Dungeons", 6, true);
		}
	}
	
	public void setMenu(int num) {
		menu = num;
		if (menu == 0) {
			TDR.saveOptions();
			if (!(new File("sav/game.sav").exists())) {
				load.setActive(false);
			} else {
				load.setActive(true);
			}
		}
	}
	
	public int getMenu() {
		return menu;
	}
}
