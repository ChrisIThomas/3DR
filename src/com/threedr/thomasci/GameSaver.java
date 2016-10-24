package com.threedr.thomasci;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GameSaver {
	
	//what needs to be saved: level name, level number, inventory, health and maximum health
	//I don't need to save position because i am only saving at the end of the level and even if you continue you end up in the same spot.
	public void saveGame(String lname, int lnum, EntityPlayer player) {
		FileWriter fw;
		try {
			fw = new FileWriter(new File("sav/game.sav"));
			BufferedWriter writer = new BufferedWriter(fw);
			
			writer.write(lname + "," + lnum + "\n");
			writer.write(player.getHealth() + "," + player.getMaxHealth() + "," + player.getInventory().getCoins() + "\n");
			writer.write(player.getInventory().write());
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Object[] loadGame() {
		File file = new File("sav/game.sav");
		Object[] dat = new Object[6];
		
		if (file.exists()) {
			try {
				FileReader reader = new FileReader(file);
				BufferedReader in = new BufferedReader(reader);
				String line = "";
				String[] info;
				
				line = in.readLine();
				info = line.split(",");
				dat[0] = info[0];
				dat[1] = info[1];
				
				line = in.readLine();
				info = line.split(",");
				dat[2] = Integer.parseInt(info[0]);
				dat[3] = Integer.parseInt(info[1]);
				dat[4] = Integer.parseInt(info[2]);
				
				dat[5] = in.readLine();
				
				in.close();
				
				file.delete();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			return new Object[0];
		}
		return dat;
	}
}
