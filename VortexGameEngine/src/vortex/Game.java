package vortex;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.input.Cursor;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.ImageData;

import vortex.collision.CollisionShape;
import vortex.gameentity.Camera;
import vortex.gameentity.Map;
import vortex.input.*;
import vortex.utilities.Quadtree;

/**
 * 
 * 
 * This is the base Game class. It is a wrapper for Slick2D's BasicGame object.
 *  It is static so simply refer to the class itself to call functions.
 * 
 * Ex: To create a game window simply type {@code Game.initGame(Title)}.
 *
 * @author Daniel Stumpff - Thanks to all the developers of Slick2D
 */

public class Game extends BasicGame{
	/**
	 * The AppGameContainer useful for adjusting properties of the window.
	 */
	protected static AppGameContainer appgc;
	/**
	 * The child GameDriver. Used to control what is rendered and updated.
	 */
	protected static GameDriver child;
	/**
	 * Static object for handling key commands.
	 */
	protected static KeyInput keyInput;
	/**
	 * Static object for handling mouse commands.
	 */
	protected static MouseInput mouseInput;
	/**
	 * The Camera used for the game.
	 */
	protected static Camera gameCamera;
	/**
	 * Whether the game camera is panning or not.
	 */
	protected static boolean cameraIsPanning = false;
	/**
	 * Stores the x value of the game camera before panning.
	 */
	protected static float cameraOldX; 
	/**
	 * Stores the y value of the game camera before panning.
	 */
	protected static float cameraOldY;
	/**
	 * Stores the local x value of the game camera before panning.
	 */
	protected static float cameraOldLocalX;
	/**
	 * Stores the local y value of the game camera before panning.
	 */
	protected static float cameraOldLocalY;
	/**
	 * Stores the cameraVX to use while panning.
	 */
	protected static float cameraVX;
	/**
	 * Stores the cameraVY to use while panning.
	 */
	protected static float cameraVY;
	/**
	 * Stores the camera that is being panned to.
	 */
	protected static Camera cameraPanningTo;
	/**
	 * Stores the number of times to move the camera while panning.
	 */
	protected static int panCounter = 1;
	/**
	 * List of all collision bounds.
	 */
	public static ArrayList<CollisionShape> collisionShapes = new ArrayList<CollisionShape>();
	/**
	 * Collision Quadtree to help collision performance.
	 */
	public static Quadtree collisionQuadtree;
	/**
	 * The amount to translate the graphics object in the plane.
	 */
	protected static float graphicsTranslationX, graphicsTranslationY = 0;
	/**
	 * Boolean to sync game camera for the first frame. Needed due to nature of game loop.
	 */
	protected boolean syncCameraForFirstFrame = false;
	/**
	 * The map currently being used.
	 */
	protected static Map gameMap;
	/**
	 * The constructor for Game.
	 * 
	 * @param title The title for the game window.
	 */
	public Game(String title) {
		super(title);
	}
	
	/**
	 * Sets the GameDriver to be used by this class.
	 * 
	 * @param driver The GameDriver object
	 */
	public static void setDriver(GameDriver driver){
		Game.child = driver;
	}
	
	/**
	 * Called once on startup. Allows you to initialize any necessary objects and variables.
	 * 
	 * @param gc The GameContainer used by this class
	 */
	public void init(GameContainer gc){
		keyInput = new KeyInput();
		mouseInput = new MouseInput();
		if(child != null)
			child.init(gc);
	}
	
	/**
	 * 
	 * Called continuously while game is running. Updates objects each frame.
	 * 
	 * @param gc The GameContainer used by this class
	 * @param i The amount of time that's passed since last update in milliseconds
	 */
	public void update(GameContainer gc, int i){
		collisionQuadtree = new Quadtree(0, new Rectangle(0, 0, getScreenWidth(), getScreenHeight()));
		
		for(int j = 0; j < collisionShapes.size(); j++){
			collisionQuadtree.insert(collisionShapes.get(j));
		}
		
		if(child != null)
			child.update(gc, i);
		
		if(cameraIsPanning && panCounter > 0){
			panCounter--;
			gameCamera.setLocalX(gameCamera.getLocalX() + cameraVX);
			gameCamera.setLocalY(gameCamera.getLocalY() + cameraVY);	
			
			if(panCounter == 0){
				cameraIsPanning = false;
				gameCamera.setLocalX(cameraOldLocalX);
				gameCamera.setLocalY(cameraOldLocalY);
				gameCamera.setX(cameraOldX);
				gameCamera.setY(cameraOldY);
				gameCamera = cameraPanningTo;
				cameraPanningTo = null;
			}
		}
		
		if(gameCamera != null){
			graphicsTranslationX = (Game.gameCamera.getViewportSize().getX() / 2) - Game.gameCamera.getGlobalX();
			graphicsTranslationY = (Game.gameCamera.getViewportSize().getY() / 2) - Game.gameCamera.getGlobalY();
		}
	}
	
	/**
	 * Called continuously while game is running. Draws objects to the game window.
	 * 
	 * @param gc The GameContainer used by this class
	 * @param g The Graphics object used by this class
	 */
	public void render(GameContainer gc, Graphics g){
		if(!syncCameraForFirstFrame){
			if(gameCamera != null){
				graphicsTranslationX = (Game.gameCamera.getViewportSize().getX() / 2) - Game.gameCamera.getGlobalX();
				graphicsTranslationY = (Game.gameCamera.getViewportSize().getY() / 2) - Game.gameCamera.getGlobalY();
			}
			syncCameraForFirstFrame = true;
		}
		
		g.translate(graphicsTranslationX, graphicsTranslationY);
		
		if(child != null)
			child.render(gc, g);
	}
	
	/**
	 * Pans gameCamera to the desired camera and set's it as the new gameCamera.
	 * 
	 * @param camera - The camera to pan and set as gameCamera
	 * @param speed - The speed to pan at
	 */
	public static void panToCamera(Camera camera, float speed){
		if(!cameraIsPanning){
			float displacementX = camera.getGlobalX() - gameCamera.getGlobalX();
			float displacementY = camera.getGlobalY() - gameCamera.getGlobalY();
			
			float angle = (float)Math.atan2(displacementY, displacementX);
			
			cameraVX = (float)Math.cos(angle) * speed;
			cameraVY = (float)Math.sin(angle) * speed;
			cameraOldX = gameCamera.getX();
			cameraOldY = gameCamera.getY();
			cameraOldLocalX = gameCamera.getLocalX();
			cameraOldLocalY = gameCamera.getLocalY();
			
			cameraPanningTo = camera;
			
			
			panCounter = (int)(Math.abs(displacementX / cameraVX));
			/*
			 * Check special case if displacementX = 0. In this case check if 
			 * displacementY > 0. Since we are using vector math, the result of
			 * panCounter will still be correct.
			 */
			if(panCounter == 0){
				panCounter = (int)Math.abs(displacementY / cameraVY);
			}
			/*
			 * Check special case if displacementX and displacementY == 0.
			 * In this case we just set panCounter to 1. The reality is that 
			 * camera is so close to gameCamera that it would take less than 1
			 * frame to pan. Setting panCounter to 1 just makes life easier as it
			 * will be corrected in the update method.
			 */
			if(panCounter == 0){
				panCounter++;
			}
			
			cameraIsPanning = true;
		}
	}
	
	/**
	 * Polls a key on press and checks if a command is registered to it.
	 * 
	 * @param key The integer value of the key pressed
	 * @param c The character value of the key pressed
	 */
	@Override
	public void keyPressed(int key, char c){
		if(keyInput != null){
			keyInput.keyPressed(key, c);
		}
	}
	
	/**
	 * Polls a key on release and checks if a command is registered to it.
	 * 
	 * @param key The integer value of the key released
	 * @param c The character value of the key released
	 */
	@Override
	public void keyReleased(int key, char c){
		if(keyInput != null){
			keyInput.keyReleased(key, c);
		}
	}
	
	/**
	 * Called when the mouse is moved.
	 * 
	 * @param oldx The position of the mouse's previous x.
	 * @param oldy The position of the mouse's previous y.
	 * @param newx The new position of the mouse's x.
	 * @param newy The new position of the mouse's y.
	 */
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy){
		if(child != null){
			child.mouseMoved(oldx, oldy, newx, newy);
		}
	}
	
	/**
	 * Called when a button on the mouse is pressed.
	 * 
	 * @param button the mouse button pressed.
	 * @param x The x position pressed at.
	 * @param y the y position pressed at.
	 */
	@Override
	public void mousePressed(int button, int x, int y){
		if(mouseInput != null){
			mouseInput.mousePressed(button, x, y);
		}
	}
	
	/**
	 * Called when a button on the mouse is released.
	 * 
	 * @param button the mouse button released.
	 * @param x The x position released at.
	 * @param y the y position released at.
	 */
	@Override
	public void mouseReleased(int button, int x, int y){
		if(mouseInput != null){
			mouseInput.mouseReleased(button, x, y);
		}
	}
	
	/**
	 * Called when the mouse wheel is moved.
	 * 
	 * @param change The amount the wheel moved
	 */
	@Override
	public void mouseWheelMoved(int change){
		if(child != null){
			child.mouseWheelMoved(change);
		}
	}
	
	/**
	 * Creates the AppGameContainer to be used for the game.
	 * 
	 * @param title The title for the game window
	 */
	public static void initGame(String title){
		try
		{
			appgc = new AppGameContainer(new Game(title));
		}
		catch (SlickException e)
		{
			Logger.getLogger(Game.class.getName()).log(Level.SEVERE, "Could Not Initialize Game", e);
		}
	}
	
	/**
	 * Destroys the AppGameContainer.
	 */
	public static void destroy(){
		if(appgc == null){
			return;
		}
		
		appgc.destroy();
	}
	
	/**
	 * Get the height of the standard screen resolution.
	 * 
	 * @return the screen height
	 */
	public static int getScreenHeight(){
		if(appgc == null){
			return -1;
		}
		
		return appgc.getHeight();
	}
	
	/**
	 * Get the width of the standard screen resolution.
	 * 
	 * @return the screen width
	 */
	public static int getScreenWidth(){
		if(appgc == null){
			return -1;
		}
		
		return appgc.getWidth();
	}
	
	/**
	 * Check if game currently has focus.
	 * 
	 * @return If game has focus
	 */
	public static boolean hasFocus(){
		if(appgc == null){
			return false;
		}
		
		return appgc.hasFocus();
	}
	
	/**
	 * Check if the display is in fullscreen mode.
	 * 
	 * @return If the display is in fullscreen mode
	 */
	public static boolean isFullscreen(){
		if(appgc == null){
			return false;
		}
		
		return appgc.isFullscreen();
	}
	
	/**
	 * Check if the mouse cursor is currently grabbed.
	 * 
	 * @return If the mouse is currently grabbed
	 */
	public static boolean isMouseGrabbed(){
		if(appgc == null){
			return false;
		}
		
		return appgc.isMouseGrabbed();
	}
	
	/**
	 * Check if game is updating only when visible to the user (default = true).
	 * 
	 * @return If updating only when frame is visible
	 */
	public static boolean isUpdatingOnlyWhenVisible(){
		if(appgc == null){
			return false;
		}
		
		return appgc.isUpdatingOnlyWhenVisible();
	}
	
	/**
	 * Reinitialize the game and the context in which it's being rendered.
	 */
	public static void reinit(){
		if(appgc == null){
			return;
		}
		
		try {
			appgc.reinit();
		} catch (SlickException e) {
			Logger.getLogger(Game.class.getName()).log(Level.WARNING, "Could Not Reinitialize Game", e);
		}
	}
	
	/**
	 * Set the default mouse cursor.
	 */
	public static void setDefaultMouseCursor(){
		if(appgc == null){
			return;
		}
		
		appgc.setDefaultMouseCursor();
	}
	
	/**
	 * Sets the game window size.
	 * 
	 * @param width The width of the game window
	 * @param height The height of the game window
	 */
	public static void setGameWindowSize(int width, int height){
		if(appgc == null){
			return;
		}
		
		try{
			appgc.setDisplayMode(width, height, false);
		}catch(SlickException e){
			Logger.getLogger(Game.class.getName()).log(Level.SEVERE, "Could Not Set Game Window Size", e);
		}
	}
	
	/**
	 * Indicate whether we want to be in fullscreen mode.
	 * 
	 * @param fullscreen Whether we want fullscreen
	 */
	public static void setFullscreen(boolean fullscreen){
		if(appgc == null){
			return;
		}
		
		try{
			appgc.setFullscreen(fullscreen);
		}catch(SlickException e){
			Logger.getLogger(Game.class.getName()).log(Level.WARNING, "Could Not Set Fullscreen", e);
		}
	}
	
	/**
	 * Set the icon to be displayed if possible in this type of container.
	 * 
	 * @param ref The icon to be displayed
	 */
	public static void setIcon(String ref){
		if(appgc == null){
			return;
		}
		
		try{
			appgc.setIcon(ref);
		}catch(SlickException e) {
			Logger.getLogger(Game.class.getName()).log(Level.WARNING, "Could Not Set Icon " + ref, e);
		}
	}
	
	/**
	 * Sets the icons to be used for this application.
	 * 
	 * @param refs The icons to be used
	 */
	public static void setIcons(String[] refs){
		if(appgc == null){
			return;
		}
		
		try{
			appgc.setIcons(refs);
		}catch(SlickException e){
			Logger.getLogger(Game.class.getName()).log(Level.WARNING, "Could Not Set Icon(s) " + refs.toString(), e);
		}
	}
	
	/**
	 * Set the mouse cursor to be displayed - this is a hardware cursor and hence shouldn't have any impact of FPS.
	 * 
	 * @param cursor The cursor to be used
	 * @param hotSpotX The x offset of the cursor
	 * @param hotSpotY The y offset of the cursor
	 */
	public static void setMouseCursor(Cursor cursor, int hotSpotX, int hotSpotY){
		if(appgc == null){
			return;
		}
		
		try {
			appgc.setMouseCursor(cursor, hotSpotX, hotSpotY);
		}catch(SlickException e) {
			Logger.getLogger(Game.class.getName()).log(Level.WARNING, "Could Not Set Mouse Cursor " + cursor.toString(), e);
		}
	}
	
	/**
	 * Set the mouse cursor to be displayed - this is a hardware cursor and hence shouldn't have any impact of FPS.
	 * 
	 * @param data The cursor to be used
	 * @param hotSpotX The x offset of the cursor
	 * @param hotSpotY The y offset of the cursor
	 */
	public static void setMouseCursor(ImageData data, int hotSpotX, int hotSpotY){
		if(appgc == null){
			return;
		}
		
		try{
			appgc.setMouseCursor(data, hotSpotX, hotSpotY);
		}catch(SlickException e){
			Logger.getLogger(Game.class.getName()).log(Level.WARNING, "Could Not Set Mouse Cursor " + data.toString(), e);
		}
	}
	
	/**
	 * Set the mouse cursor to be displayed - this is a hardware cursor and hence shouldn't have any impact of FPS.
	 * 
	 * @param image The cursor to be used
	 * @param hotSpotX The x offset of the cursor
	 * @param hotSpotY The y offset of the cursor
	 */
	public static void setMouseCursor(Image image, int hotSpotX, int hotSpotY){
		if(appgc == null){
			return;
		}
		
		try{
			appgc.setMouseCursor(image, hotSpotX, hotSpotY);
		}catch(SlickException e){
			Logger.getLogger(Game.class.getName()).log(Level.WARNING, "Could Not Set Mouse Cursor " + image.toString(), e);
		}
	}
	
	/**
	 * Set the mouse cursor to be displayed - this is a hardware cursor and hence shouldn't have any impact of FPS.
	 * 
	 * @param ref The cursor to be used
	 * @param hotSpotX The x offset of the cursor
	 * @param hotSpotY The y offset of the cursor
	 */
	public static void setMouseCursor(String ref, int hotSpotX, int hotSpotY){
		if(appgc == null){
			return;
		}
		
		try{
			appgc.setMouseCursor(ref, hotSpotX, hotSpotY);
		}catch(SlickException e){
			Logger.getLogger(Game.class.getName()).log(Level.WARNING, "Could Not Set Mouse Cursor " + ref, e);
		}
	}
	
	/**
	 * Indicate whether the mouse cursor should be grabbed or not.
	 * 
	 * @param grabbed Whether cursor should be grabbed
	 */
	public static void setMouseGrabbed(boolean grabbed){
		if(appgc == null){
			return;
		}
		
		appgc.setMouseGrabbed(grabbed);
	}
	
	/**
	 * Set the title of the window.
	 * 
	 * @param title The title of the window
	 */
	public static void setTitle(String title){
		if(appgc == null){
			return;
		}
		
		appgc.setTitle(title);
	}
	
	/**
	 * Assign a camera to the game camera.
	 * 
	 * @param gameCamera the camera to assign
	 */
	public static void setGameCamera(Camera gameCamera){
		Game.gameCamera = gameCamera;
	}
	
	public static void setGameMap(Map gameMap){
		Game.gameMap = gameMap;
	}
	
	/**
	 * Indicate if the display should update only when the game is visible (the default is true).
	 * 
	 * @param updateOnlyWhenVisible - the boolean
	 */
	public static void setUpdateOnlyWhenVisible(boolean updateOnlyWhenVisible){
		if(appgc == null){
			return;
		}
		
		appgc.setUpdateOnlyWhenVisible(updateOnlyWhenVisible);
	}
	
	/**
	 * Start running the game.
	 */
	public static void start(){
		if(appgc == null){
			return;
		}
		
		try {
			appgc.start();
		}catch(SlickException e) {
			Logger.getLogger(Game.class.getName()).log(Level.WARNING, "Could Not Start Game Loop", e);
		}
	}
	
	/**
	 * Check if the display created supported alpha in the back buffer.
	 * 
	 * @return If the display supports alpha in the back buffer
	 */
	public static boolean supportsAlphaInBackBuffer(){
		if(appgc == null){
			return false;
		}
		
		return appgc.supportsAlphaInBackBuffer();
	}
	
	/**
	 * Get the AppGameContainer associated with this Game.
	 * 
	 * @return The AppGameContainer
	 */
	public static AppGameContainer getAppGameContainer(){
		return appgc;
	}
	
	/**
	 * Get the amount of graphics translation in the x-axis.
	 * 
	 * @return The amount of graphics translation
	 */
	public static float getGraphicsTranslationX(){
		return graphicsTranslationX;
	}
	
	/**
	 * Get the amount of graphics translation in the y-axis.
	 *
	 * @return The amount of graphics translation
	 */
	public static float getGraphicsTranslationY(){
		return graphicsTranslationY;
	}
	
	/**
	 * Get the game camera.
	 * 
	 * @return The game camera
	 */
	public static Camera getGameCamera(){
		return gameCamera;
	}
	
	public static Map getGameMap(){
		return gameMap;
	}
	
	public static boolean isCameraPanning(){
		return cameraIsPanning;
	}
}
