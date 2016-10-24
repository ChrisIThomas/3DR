package com.threedr.thomasci;

public class EDat {
	private String[] data;
	
	public EDat(String[] d) {
		data = d;
	}
	
	public String[] getData() {
		return data;
	}
	
	public int getType() {
		return Integer.parseInt((String) data[0]);
	}
	
	public static Entity setupEnt(int type, String[] dat, Game g) {
		switch (type) {
			case 0:
				return EntityEnemy.newEnemy(dat, g);
				//return new EntityEnemy(Float.parseFloat(dat[1]), Float.parseFloat(dat[2]), g);
			case 1:
				return new EntityItem(Float.parseFloat(dat[1]), Float.parseFloat(dat[2]), g, Item.newItem(Integer.parseInt(dat[3]), dat[4]));
			case 2:
				return new EntityDoor(Float.parseFloat(dat[1]), Float.parseFloat(dat[2]), g, (int) Float.parseFloat(dat[3]), 1, dat[4]);
			case 3:
				return new EntityDoor(Float.parseFloat(dat[1]), Float.parseFloat(dat[2]), g, (int) Float.parseFloat(dat[3]), 0, dat[4]);
			case 4:
				return new EntityObstacle(Float.parseFloat(dat[1]), Float.parseFloat(dat[2]), g, dat[3]);
			case 5:
				return new EntityButton(Float.parseFloat(dat[1]), Float.parseFloat(dat[2]), g, (int) Float.parseFloat(dat[3]), dat[4]);
			case 6:
				return new EntityActivatorObject(Float.parseFloat(dat[1]), Float.parseFloat(dat[2]), g, dat[3], Boolean.parseBoolean(dat[4]));
			case 7:
				return new EntityLButton(Float.parseFloat(dat[1]), Float.parseFloat(dat[2]), g, (int) Float.parseFloat(dat[3]), dat[4]);
			case 8:
				return new EntityCommander(Float.parseFloat(dat[1]), Float.parseFloat(dat[2]), g, Integer.parseInt(dat[3]), dat[4]);
			case 9:
				return new EntityWriting(Float.parseFloat(dat[1]), Float.parseFloat(dat[2]), g, Float.parseFloat(dat[3]), dat[4]);
			case 10:
				return new EntityDecoration(Float.parseFloat(dat[1]), Float.parseFloat(dat[2]), g, Integer.parseInt(dat[4]));
			case 11:
				return new EntityFloorDecoration(Float.parseFloat(dat[1]), Float.parseFloat(dat[2]), g, Float.parseFloat(dat[3]), Integer.parseInt(dat[4]));
			case 12:
				return new EntityWallDecoration(Float.parseFloat(dat[1]), Float.parseFloat(dat[2]), g, Float.parseFloat(dat[3]), Integer.parseInt(dat[4]));
			case 13:
				return new EntityData(dat);
			case 14:
				return new EntityPressurePlate(Float.parseFloat(dat[1]), Float.parseFloat(dat[2]), g, (int) Float.parseFloat(dat[3]), dat[4]);
			case 15:
				return new EntityTrap(Float.parseFloat(dat[1]), Float.parseFloat(dat[2]), g, dat[3]);
			case 16:
				return new EntityWallTrap(Float.parseFloat(dat[1]), Float.parseFloat(dat[2]), g, dat[3], Float.parseFloat(dat[4]));
			case 17:
				return new EntityShooterTrap(Float.parseFloat(dat[1]), Float.parseFloat(dat[2]), g, dat[3], Float.parseFloat(dat[4]), Integer.parseInt(dat[5]));
			case 18:
				return new EntityParticleEmitter(Float.parseFloat(dat[1]), Float.parseFloat(dat[2]), g);
			case 19:
				return new EntityExit(Float.parseFloat(dat[1]), Float.parseFloat(dat[2]), Float.parseFloat(dat[3]), g);
		}
		return null;
	}
}
