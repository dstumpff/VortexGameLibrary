package vortex.sound;

import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Queue;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 * A static class for controlling background music. Allows easy loading, playing, looping, pausing and resuming of background music. Uses JLayer to allow .mp3 format.
 * 
 * @author Daniel Stumpff - Thanks to JLayer for it's mp3 compatible API.
 */
public class BackgroundMusic{
 	private static Queue<String> songQueue = new ArrayDeque<String>();
	
	public final static int NOT_STARTED = 0;
	public final static int LOADED = 1;
	public final static int PLAYING = 2;
	public final static int PAUSED = 3;
	public final static int FINISHED = 4;
	public final static int LOOP_CONTINUOUSLY = -1;
	private final static int PLAY_QUEUE = -2;
	
	/**
	 * Tells if music should stop playing current song.
	 */
	private static boolean stopPlaying = false;
	
	/**
	 * Stores last played song for looping purposes.
	 */
	private static String lastPlayedSong;
	
	/**
	 * The Player object to play the song - Thanks to JLayer for this object.
	 */
	private static Player thePlayer;
	/**
	 * Empty object to sync thread.
	 */
	private static final Object playerLock = new Object();
	/**
	 * The player's current status.
	 */
	private static int playerStatus = NOT_STARTED;
	
	/**
	 * Adds a song to the play queue.
	 * 
	 * @param path The path of the file
	 */
	public static void addSongToQueue(String path){
		songQueue.add(path);
	}
	
	/**
	 * Plays songs in the queue until it is empty or it is stopped.
	 * 
	 * @throws JavaLayerException
	 */
	public static void playAllInQueue() throws JavaLayerException{
		if(!songQueue.isEmpty())
			loadSong(songQueue.poll());
		
		synchronized(playerLock){
			switch(playerStatus){
				case LOADED:
					stopPlaying = false;
					final Runnable r = new Runnable(){
						public void run(){
							playInternal(PLAY_QUEUE);
						}
					};
					final Thread t = new Thread(r);
					t.setDaemon(true);
					t.setPriority(Thread.MAX_PRIORITY);
					playerStatus = PLAYING;
					t.start();
					break;
				case PAUSED:
					resume();
					break;
				default:
					break;			
			}
		}
	}
	
	/**
	 * Plays the loaded song or does nothing if song is not loaded.
	 * 
	 * @param flag Modifier to loop or not (use BackgroundMusic.LOOP_CONTINUOUSLY to loop or anything else to play once)
	 * @throws JavaLayerException
	 */
	public static void play(int flag) throws JavaLayerException{
		synchronized(playerLock){
			switch(playerStatus){
				case LOADED:
					stopPlaying = false;
					final Runnable r = new Runnable(){
						public void run(){
							playInternal(flag);
						}
					};
					final Thread t = new Thread(r);
					t.setDaemon(true);
					t.setPriority(Thread.MAX_PRIORITY);
					playerStatus = PLAYING;
					t.start();
					break;
				case PAUSED:
					resume();
					break;
				default:
					break;
			}
		}
	}
	
	/**
	 * Runs the player in a new thread so game can still run.
	 * 
	 * @param flag Given by the play method
	 */
	private static void playInternal(int flag){
		while(playerStatus != FINISHED){
			try{
				if(!thePlayer.play(1)){
					break;
				}
			}catch(final JavaLayerException e){
				break;
			}
			
			synchronized(playerLock){
				while(playerStatus == PAUSED){
					try{
						playerLock.wait();
					}catch(final InterruptedException e){
						break;
					}
				}
			}
		}
		if(flag == LOOP_CONTINUOUSLY && !stopPlaying){
			close();
			loadSong(lastPlayedSong);
			try {
				play(LOOP_CONTINUOUSLY);
			} catch (JavaLayerException e) {
				e.printStackTrace();
			}
		}
		else if(flag == PLAY_QUEUE && !stopPlaying){
			close();
			try{
				playAllInQueue();
			}catch(JavaLayerException e){
				e.printStackTrace();
			}
		}
		else{
			close();
		}
	}
	
	/**
	 * Pauses the song currently playing.
	 * 
	 * @return True if pause is successful
	 */
	public static boolean pause(){
		synchronized(playerLock){
			if(playerStatus == PLAYING){
				playerStatus = PAUSED;
			}
			return playerStatus == PAUSED;
		}
	}
	
	/**
	 * Resumes the song that is currently paused.
	 * 
	 * @return True if resume is successful
	 */
	public static boolean resume(){
		synchronized(playerLock){
			if(playerStatus == PAUSED){
				playerStatus = PLAYING;
				playerLock.notifyAll();
			}
			
			return playerStatus == PLAYING;
		}
	}
	
	/**
	 * Stops the current song (song cannot resume after this is called).
	 */
	public static void close(){
		synchronized(playerLock){
			playerStatus = FINISHED;
			stopPlaying = true;
		}
		try{
			thePlayer.close();
		}catch(final Exception e){
			
		}
	}
	
	/**
	 * Load a song to play.
	 * 
	 * @param path The path of the song
	 */
	public static void loadSong(String path){
		if(playerStatus == NOT_STARTED || playerStatus == FINISHED){
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream fis = loader.getResourceAsStream(path);
			lastPlayedSong = path;
			try {
				thePlayer = new Player(fis);
				playerStatus = LOADED;
			}catch(JavaLayerException e){
				e.printStackTrace();
			}
		}
	}
}
