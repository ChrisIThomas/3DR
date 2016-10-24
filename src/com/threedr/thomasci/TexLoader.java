package com.threedr.thomasci;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public class TexLoader {
	HashMap<String, Texture> textures = new HashMap<String, Texture>();
	
	//loads ALL textures (done at the start so we don't need to load any textures mid-game)
	public TexLoader() {
		getTexture("shootTrap", "img/shootTrap.png");
		getTexture("wallTrap", "img/wallTrap.png");
		getTexture("quiver", "img/quiver.png");
		getTexture("crosshair", "img/crosshair.png");
		getTexture("effect", "img/effect.png");
		getTexture("hbutton", "img/hbutton.png");
		getTexture("decor", "img/decor.png");
		getTexture("doors", "img/doors.png");
		getTexture("floorDecor", "img/floorDecor.png");
		getTexture("itemsModel", "img/itemsModel.png");
		getTexture("ladder", "img/ladder.png");
		getTexture("button", "img/button.png");
		getTexture("tiles", "img/tiles.png");
		getTexture("pressure", "img/pressure.png");
		getTexture("arrow", "img/arrow.png");
		getTexture("magic", "img/magic.png");
		getTexture("floorTrap", "img/floorTrap.png");
		getTexture("wallDecor", "img/wallDecor.png");
		getTexture("writing", "img/writing.png");
		getTexture("alchemy", "img/alchemy.png");
		getTexture("icons", "img/icons.png");
		getTexture("healthbar", "img/health.png");
		getTexture("hotbar", "img/hotbar.png");
		getTexture("inventory", "img/inventory.png");
		getTexture("sight", "img/sight.png");
		getTexture("items", "img/items.png");
		getTexture("back", "img/back.png");
		getTexture("menuButton", "img/menuButton.png");
		getTexture("font", "img/font.png");
		
		//chracters
		getTexture("character", "img/character.png");
		getTexture("dwarf", "img/dwarf.png");
		
		//enemies
		getTexture("rat", "img/rat.png");
		getTexture("spider", "img/spider.png");
		getTexture("leech", "img/leech.png");
		getTexture("crawler", "img/crawler.png");
		getTexture("bat", "img/bat.png");
		getTexture("skeleton", "img/skeleton.png");
		getTexture("statue", "img/statue.png");
		getTexture("ghost", "img/ghost.png");
		getTexture("king", "img/king.png");
	}
	
	public Texture getTexture(String name, String filename) {
		if (textures.containsKey(name)) {
			return textures.get(name);
		} else {
			Texture tex = new Texture(name, loadTex(filename));
			Vector2f vec = getSize(filename);
			tex.setSize((int) vec.x, (int) vec.y);
			textures.put(name, tex);
			return tex;
		}
	}
	
	public int loadTex(String filename) {
		IntBuffer iB = BufferUtils.createIntBuffer(1);
		GL11.glGenTextures(iB);
		iB.rewind();
		
		BufferedImage bimg;
		
		bimg = loadImage(filename);
		
		ByteBuffer data = convertImageData(bimg);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, iB.get(0));
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D,0,GL11.GL_RGBA,bimg.getWidth(),bimg.getHeight(),0,GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE,data);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		if (!filename.equals("img/back.png")) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		}
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		
		iB.rewind();
		return iB.get(0);
	}
	
	public Vector2f getSize(String filename) {
		File file = new File(filename);
		Image img = new ImageIcon(filename).getImage();
		try {
			img = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Vector2f vec = new Vector2f(img.getWidth(null), img.getHeight(null));
		return vec;
	}
	
	public BufferedImage loadImage(String path) {
		File fi = new File(path);
		Image img = new ImageIcon(path).getImage();
		try {
			img = ImageIO.read(fi);
		} catch (IOException e) {
			System.out.println(fi);
			e.printStackTrace();
		}
		BufferedImage bimg = (BufferedImage) img;
		//removeCol(bimg, Color.PINK);
		return bimg;
	}
	
	@SuppressWarnings("rawtypes")
	private ByteBuffer convertImageData(BufferedImage bufferedImage) {
		ByteBuffer imageBuffer;
		WritableRaster raster;
		BufferedImage texImage;

		ColorModel glAlphaColorModel = new ComponentColorModel(ColorSpace
				.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8, 8 },
				true, false, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);

		raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
				bufferedImage.getWidth(), bufferedImage.getHeight(), 4, null);
		texImage = new BufferedImage(glAlphaColorModel, raster, true,
				new Hashtable());

		// copy the source image into the produced image
		Graphics g = texImage.getGraphics();
		g.setColor(new Color(0f, 0f, 0f, 0f));
		g.fillRect(0, 0, 256, 256);
		g.drawImage(bufferedImage, 0, 0, null);

		// build a byte buffer from the temporary image
		// that be used by OpenGL to produce a texture.
		byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer())
				.getData();
		
		
		imageBuffer = ByteBuffer.allocateDirect(data.length);
		imageBuffer.order(ByteOrder.nativeOrder());
		imageBuffer.put(data, 0, data.length);
		imageBuffer.flip();

		return imageBuffer;
	}
}
