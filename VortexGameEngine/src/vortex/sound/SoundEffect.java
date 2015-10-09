package vortex.sound;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.*;

/**
 * A class for loading, playing, looping, pausing, and resuming sound effects. Uses the native java sound library and can therefore only support
 * those formats (.mp3 is not a supported format). It is recommended to use .wav format.
 * 
 * @author Daniel Stumpff
 */
public class SoundEffect {
	/**
	 * The sound clip.
	 */
	private Clip clip;
	/**
	 * The stream.
	 */
	private AudioInputStream ais;
	/**
	 * The current position of the clip. Used for pause and resume methods.
	 */
	private long curPos = 0;
	/**
	 * True if the clip is paused.
	 */
	private boolean isPaused = false;
	
	/**
	 * The constructor that takes the path of the sound effect to load.
	 * 
	 * @param path The path of the sound
	 */
	public SoundEffect(String path){
		try{
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream is = loader.getResourceAsStream(path);
			ais = AudioSystem.getAudioInputStream(is);
			clip = AudioSystem.getClip();
			clip.open(ais);
		}catch(IOException e){
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Begins playing the sound from the beginning of the stream.
	 */
	public void playSoundEffect(){
		if(!clip.isRunning()){
			clip.setFramePosition(0);
			clip.start();
		}
	}
	
	/**
	 * Will loop the sound until the stop method is called.
	 */
	public void loopSoundEffect(){
		clip.setFramePosition(0);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	/**
	 * Pause the sound effect.
	 */
	public void pauseSoundEffect(){
		if(!isPaused && clip.isRunning()){
			curPos = clip.getMicrosecondPosition();
			isPaused = true;
			clip.stop();
		}
	}
	
	/**
	 * Resume the sound effect if currently paused.
	 */
	public void resumeSoundEffect(){
		if(isPaused && !clip.isRunning()){
			clip.setMicrosecondPosition(curPos);
			isPaused = false;
			clip.start();
		}
	}
	
	/**
	 * Stop the sound, but leave the stream open for future playing.
	 */
	public void stopSoundEffect(){
		clip.stop();
	}
	
	/**
	 * Closes the stream.
	 */
	public void close(){
		clip.close();
	}
}
