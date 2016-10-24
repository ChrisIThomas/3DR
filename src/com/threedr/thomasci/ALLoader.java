package com.threedr.thomasci;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;
import org.lwjgl.util.vector.Vector3f;

public class ALLoader {
	public static IntBuffer buffer, source;
	//public FloatBuffer sourceVel;
	public static FloatBuffer listenerPos;
	//public FloatBuffer listenerVel;
	public FloatBuffer listenerOri;
	//public FloatBuffer sourcePos;
	private static int nextSource;
	private static final int chanels = 32;
	
	private int numSounds = 18;
	
	//sounds are:
	//0: woodDoor
	//1: metalDoor
	//2: stoneSlide
	//3: swordClang
	//4: stepStone
	//5: stepDirt
	//6: stepWater
	//7: drawBack
	//8: arrowFire
	//9: swoosh
	//10: teleport
	//11: flap
	//12: squeak
	//13: squelch
	//14: bones
	//15: ghost
	//16: statue
	//17: grunt
	
	public ALLoader() {
		buffer = BufferUtils.createIntBuffer(numSounds);
		
		source = BufferUtils.createIntBuffer(chanels);
		
		//sourcePos = (FloatBuffer) BufferUtils.createFloatBuffer(3 * numSounds);
		
		//sourceVel = (FloatBuffer) BufferUtils.createFloatBuffer(3 * numSounds);
		
		listenerPos = (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] {0.0f, 0.0f, 0.0f}).rewind();
		
		//listenerVel = (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] {0.0f, 0.0f, 0.0f}).rewind();
		
		listenerOri = (FloatBuffer) BufferUtils.createFloatBuffer(6).put(new float[] {0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f}).rewind();
	}
	
	public int loadALData() {
		AL10.alGenBuffers(buffer);
		AL10.alDistanceModel(AL10.AL_INVERSE_DISTANCE);
		
		if (AL10.alGetError() != AL10.AL_NO_ERROR)
			return AL10.AL_FALSE;
		
		WaveData waveFile = null;
		try {
			waveFile = WaveData.create(new BufferedInputStream(new FileInputStream("snd/woodDoor.wav")));
			AL10.alBufferData(buffer.get(0), waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();
			
			waveFile = WaveData.create(new BufferedInputStream(new FileInputStream("snd/metalDoor.wav")));
			AL10.alBufferData(buffer.get(1), waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();
			
			waveFile = WaveData.create(new BufferedInputStream(new FileInputStream("snd/stoneSlide.wav")));
			AL10.alBufferData(buffer.get(2), waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();
			
			waveFile = WaveData.create(new BufferedInputStream(new FileInputStream("snd/swordClang.wav")));
			AL10.alBufferData(buffer.get(3), waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();
			
			waveFile = WaveData.create(new BufferedInputStream(new FileInputStream("snd/stepStone.wav")));
			AL10.alBufferData(buffer.get(4), waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();
			
			waveFile = WaveData.create(new BufferedInputStream(new FileInputStream("snd/stepDirt.wav")));
			AL10.alBufferData(buffer.get(5), waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();
			
			waveFile = WaveData.create(new BufferedInputStream(new FileInputStream("snd/stepWater.wav")));
			AL10.alBufferData(buffer.get(6), waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();
			
			waveFile = WaveData.create(new BufferedInputStream(new FileInputStream("snd/drawBack.wav")));
			AL10.alBufferData(buffer.get(7), waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();
			
			waveFile = WaveData.create(new BufferedInputStream(new FileInputStream("snd/arrowFire.wav")));
			AL10.alBufferData(buffer.get(8), waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();
			
			waveFile = WaveData.create(new BufferedInputStream(new FileInputStream("snd/swoosh.wav")));
			AL10.alBufferData(buffer.get(9), waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();
			
			waveFile = WaveData.create(new BufferedInputStream(new FileInputStream("snd/teleport.wav")));
			AL10.alBufferData(buffer.get(10), waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();
			
			waveFile = WaveData.create(new BufferedInputStream(new FileInputStream("snd/flap.wav")));
			AL10.alBufferData(buffer.get(11), waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();
			
			waveFile = WaveData.create(new BufferedInputStream(new FileInputStream("snd/squeak.wav")));
			AL10.alBufferData(buffer.get(12), waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();
			
			waveFile = WaveData.create(new BufferedInputStream(new FileInputStream("snd/squelch.wav")));
			AL10.alBufferData(buffer.get(13), waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();
			
			waveFile = WaveData.create(new BufferedInputStream(new FileInputStream("snd/bones.wav")));
			AL10.alBufferData(buffer.get(14), waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();
			
			waveFile = WaveData.create(new BufferedInputStream(new FileInputStream("snd/ghost.wav")));
			AL10.alBufferData(buffer.get(15), waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();
			
			waveFile = WaveData.create(new BufferedInputStream(new FileInputStream("snd/statue.wav")));
			AL10.alBufferData(buffer.get(16), waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();
			
			waveFile = WaveData.create(new BufferedInputStream(new FileInputStream("snd/grunt.wav")));
			AL10.alBufferData(buffer.get(17), waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		
		AL10.alGenSources(source);
		
		if (AL10.alGetError() != AL10.AL_NO_ERROR)
			return AL10.AL_FALSE;
		
		/*AL10.alSourcei(source.get(0), AL10.AL_BUFFER, buffer.get(0));
		AL10.alSourcef(source.get(0), AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(source.get(0), AL10.AL_GAIN, 1.0f);
		AL10.alSource(source.get(0), AL10.AL_POSITION, sourcePos);
		AL10.alSource(source.get(0), AL10.AL_VELOCITY, sourceVel);
		//AL10.alSourcei(source.get(0), AL10.AL_LOOPING, AL10.AL_TRUE);
		
		AL10.alSourcei(source.get(1), AL10.AL_BUFFER, buffer.get(1));
		AL10.alSourcef(source.get(1), AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(source.get(1), AL10.AL_GAIN, 1.0f);
		AL10.alSource(source.get(1), AL10.AL_POSITION, sourcePos);
		AL10.alSource(source.get(1), AL10.AL_VELOCITY, sourceVel);
		
		AL10.alSourcei(source.get(2), AL10.AL_BUFFER, buffer.get(2));
		AL10.alSourcef(source.get(2), AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(source.get(2), AL10.AL_GAIN, 1.0f);
		AL10.alSource(source.get(2), AL10.AL_POSITION, sourcePos);
		AL10.alSource(source.get(2), AL10.AL_VELOCITY, sourceVel);*/
		
		
		if (AL10.alGetError() == AL10.AL_NO_ERROR)
			return AL10.AL_TRUE;
		
		return AL10.AL_FALSE;
	}
	
	public void setListenerValues() {
		AL10.alListener(AL10.AL_POSITION, listenerPos);
		//AL10.alListener(AL10.AL_VELOCITY, listenerVel);
		AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
	}
	
	public void killALData() {
		int position = source.position();
		source.position(0).limit(position);
		AL10.alDeleteSources(source);
		AL10.alDeleteBuffers(buffer);
	}
	
	public void tick() {
		for (int i = 0; i < source.position(); i++) {
			if (AL10.alGetSourcei(source.get(i), AL10.AL_SOURCE_STATE) != AL10.AL_PLAYING) {
				nextSource = i;
			}
		}
	}
	
	public static void setListenPos(Vector3f vec) {
		listenerPos.put(0, vec.x);
		listenerPos.put(1, vec.y);
		listenerPos.put(2, vec.z);
		AL10.alListener(AL10.AL_POSITION, listenerPos);
	}
	
	public static void setListenOri(float rot) {
		AL10.alListener(AL10.AL_ORIENTATION, (FloatBuffer) BufferUtils.createFloatBuffer(6).put(new float[] {0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f}).rewind());
	}
	
	public static int addSound(int type, FloatBuffer pos) {
		//if (AL10.alGetSourcei(source.get(0), AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING) return;
		//int position = source.position();
		//AL10.alSourceRewind(source.get(position));
		//position = 0;
		int position = nextSource;
		while (AL10.alGetSourcei(source.get(nextSource), AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING) {
			nextSource++;
			if (nextSource >= chanels) {
				nextSource = 0;
			}
			if (nextSource == position) return -1;
		}
		position = nextSource;
		//source.limit(position + 1);
		AL10.alGenSources(source);
		
		if (AL10.alGetError() != AL10.AL_NO_ERROR) {}
		
		
		AL10.alSourcei(source.get(position), AL10.AL_BUFFER, buffer.get(type));
		AL10.alSourcef(source.get(position), AL10.AL_REFERENCE_DISTANCE, 1.0f);
		AL10.alSourcef(source.get(position), AL10.AL_MAX_DISTANCE, 4.0f);
		AL10.alSourcef(source.get(position), AL10.AL_PITCH, 0.75f + (float) Math.random() * 0.25f);
		AL10.alSourcef(source.get(position), AL10.AL_GAIN, 1.0f);
		AL10.alSource(source.get(position), AL10.AL_POSITION, pos);
		
		AL10.alSourcePlay(source.get(position));
		
		if (AL10.alGetError() != AL10.AL_NO_ERROR) {}
		
		//source.position(position+1);
		return position;
	}
	
	public static void stopSound(int pos) {
		if (pos < 0 || pos >= chanels) return;
		if (AL10.alGetSourcei(source.get(pos), AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING) {
			AL10.alSourceStop(source.get(pos));
		}
	}
}
