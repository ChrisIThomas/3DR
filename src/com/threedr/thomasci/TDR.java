package com.threedr.thomasci;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class TDR {
	public static int width = 1920, height = 1080;
	public Boolean running;
	public static boolean funky = false, moreParticles = false;
	public static int sensitivity = 2;
	private static TexLoader texLoader;
	private static ALLoader alLoader;
	private Game game;
	private boolean press;
	private static boolean menu;
	private Menu mainMenu;
	
	public static void main(String args[]) {
		System.setProperty("org.lwjgl.librarypath",System.getProperty("user.dir") + "\\lib");
		TDR main = new TDR();
		main.setup();
		System.exit(0);
	}
	
	private void setup() {
		funky = false;
		try {
			//width = 800;
			//height = 600;
			//Display.setDisplayMode(new DisplayMode(width, height));
			/*DisplayMode modes[] = Display.getAvailableDisplayModes();
			for (int i = 0; i < modes.length; i++) {
				if (modes[i].getWidth() == width && modes[i].getHeight() == height && modes[i].getBitsPerPixel() == 32) {
					Display.setDisplayMode(modes[i]);
					Display.setFullscreen(true);
					break;
				}
			}*/
			/*Display.setDisplayMode(new DisplayMode(width, height));
			DisplayMode modes[] = Display.getAvailableDisplayModes();
			for (int i = 0; i < modes.length; i++) {
				if (modes[i].getWidth() == width && modes[i].getHeight() == height) {
					System.out.println("DM: " + modes[i].getWidth() + ", " + modes[i].getHeight() + ", " + modes[i].getBitsPerPixel() + ", " + modes[i].getFrequency());
					Display.setDisplayMode(modes[i]);
					Display.setFullscreen(true);
					break;
				}
			}*/
			Display.setDisplayMode(Display.getDesktopDisplayMode());
			Display.setFullscreen(true);
			width = Display.getDesktopDisplayMode().getWidth();
			height = Display.getDesktopDisplayMode().getHeight();
			Display.setTitle("3D Roguelike");
			Display.create();
			Keyboard.create();
			Mouse.create();
			AL.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		AL10.alGetError();
		
		// init OpenGL hewre
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		GLU.gluPerspective(90.0f, ((float)width) / ((float)height), 0.01f, 30.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		
		GL11.glClearDepth(1.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NEAREST);
		
		GL11.glFogf(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
		GL11.glHint(GL11.GL_FOG_HINT, GL11.GL_NICEST);
		GL11.glFogf(GL11.GL_FOG_START, 0.0f);
		GL11.glFogf(GL11.GL_FOG_END, 10.0f);
		GL11.glEnable(GL11.GL_FOG);
		
		loadOptions();
		
		texLoader = new TexLoader();
		
		alLoader = new ALLoader();
		if (alLoader.loadALData() == AL10.AL_FALSE) {
			System.out.println("Error loading data.");
		} else {
			alLoader.setListenerValues();
		}
		
		//game = new Game(-1);
		mainMenu = new Menu(this);
		
		running = true;
		menu = true;
		
		//AL10.alSourcePlay(ALLoader.source.get(1));
		
		run();
		
		//final cleanup
		alLoader.killALData();
		AL.destroy();
		Display.destroy();
	}
	
	public static Texture getTexture(String name, String loc) {
		return texLoader.getTexture(name, loc);
	}
	
	private void run() {
		while(!Display.isCloseRequested() && running) {
			update();
			tick();
			render();

			Display.update();
			Display.sync(60);
		}
		Display.destroy();
	}
	
	private void update() {
		if (sensitivity != 1 && sensitivity != 2 && sensitivity != 4) sensitivity = 2;
		if (game != null) {
			if (game.shouldFinish()) {
				game = null;
				menu = true;
			}
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			if (!press) {
				if (game != null) {
					if (!menu) {
						menu = true;
						mainMenu.setMenu(2);
					} else if (mainMenu.getMenu() == 2) {
						menu = false;
					} else {
						mainMenu.setMenu(2);
					}
				}
			}
			press = true;
		} else {
			press = false;
		}
		
		if (menu) {
			mainMenu.update();
			if (Mouse.isGrabbed()) Mouse.setGrabbed(false);
		} else if (game.inNextMenu() || game.inFinishMenu()) {
			game.update();
			if (Mouse.isGrabbed()) Mouse.setGrabbed(false);
		} else {
			game.update();
			
			if (Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
				if (Mouse.isGrabbed()) Mouse.setGrabbed(false);
			} else {
				if (!Mouse.isGrabbed()) Mouse.setGrabbed(true);
				Mouse.setCursorPosition(width / 2, height / 2);
			}
		}
		/*while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_F1) {
					try {
						Display.setFullscreen(!Display.isFullscreen());
					} catch (LWJGLException e) {
						e.printStackTrace();
					}
				}
			}
		}*/
	}
	
	private void tick() {
		alLoader.tick();
		if (!menu) {
			game.tick();
		}
	}
	
	private void render() {
		GL11.glDepthMask(true);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_EQUAL, 1f);
		
		if (menu) {
			if (mainMenu.getMenu() == 2 || mainMenu.getMenu() == 3) {
				setup3d();
				game.draw3d();
			}
			setup2d();
			mainMenu.draw();
		} else {
			setup3d();
			game.draw3d();
			
			setup2d();
			game.draw2d();
		}
		
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	private void setup3d() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();

		GLU.gluPerspective(90.0f, ((float)width) / ((float)height), 0.01f, 30.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		GL11.glViewport(0, 0, width, height);
		
		GL11.glClearDepth(1.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
	}
	
	private void setup2d() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		GL11.glOrtho(0, width, height, 0, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		//THIS LINE MAY CAUSE SOME ISSUES WITH TRANSPARENCY!!!!!
		GL11.glAlphaFunc(GL11.GL_LEQUAL, 1f);
	}
	
	public void startGame(String loc, boolean load) {
		Mouse.setGrabbed(true);
		menu = false;
		mainMenu.setMenu(0);
		game = new Game(loc, -1, load);
	}
	
	public void quit() {
		saveOptions();
		running = false;
	}
	
	public void stopGame() {
		game = null;
		menu = true;
		mainMenu.setMenu(0);
	}
	
	public void setMenu(boolean val) {
		menu = val;
	}
	
	public static boolean inMenu() {
		return menu;
	}
	
	public static void saveOptions() {
		FileWriter fw;
		try {
			fw = new FileWriter(new File("options.txt"));
			BufferedWriter writer = new BufferedWriter(fw);
			
			writer.write(funky + "," + moreParticles + "," + sensitivity);
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadOptions() {
		File file = new File("options.txt");
		
		if (file.exists()) {
			try {
				FileReader reader = new FileReader(file);
				BufferedReader in = new BufferedReader(reader);
				String line = "";
				line = in.readLine();
				String[] dat = line.split(",");
				
				if (dat.length >= 1) {
					funky = Boolean.parseBoolean(dat[0]);
					if (dat.length >= 2) {
						moreParticles = Boolean.parseBoolean(dat[1]);
						if (dat.length >= 3) {
							sensitivity = Integer.parseInt(dat[2]);
						}
					}
				}
				
				in.close();
				
				file.delete();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
