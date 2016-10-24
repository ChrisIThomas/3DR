package com.threedr.thomasci;

import org.lwjgl.util.vector.Vector3f;

public class EntityParticleEmitter extends Entity {
	private int emit;
	
	public EntityParticleEmitter(float x, float z, Game g) {
		super(x, z, g);
	}
	
	public void tick() {
		float r = (float) Math.random() * 0.5f - 0.25f;
		game.addParticle(new Particle(new Vector3f((float) Math.random() * 2 - 1 + pos.x, pos.y, (float) Math.random() * 2 - 1 + pos.z),
				new Vector3f((float) Math.random() * 0.02f - 0.01f, -0.025f - (float) Math.random() * 0.05f, (float) Math.random() * 0.02f - 0.01f),
				new Vector3f(0.5f + r, 0.0f, 0.75f + r), false, game));
		if (TDR.moreParticles) {
			if (emit <= 0) {
				game.addParticle(new Particle(new Vector3f((float) Math.random() * 2 - 1 + pos.x, pos.y, (float) Math.random() * 2 - 1 + pos.z),
						new Vector3f(0.0f, 0.0f, 0.0f),
						new Vector3f(0.5f + r, 0.0f, 0.75f + r), false, game));
				emit = 1;
			} else {
				emit--;
			}
		}
	}
}
