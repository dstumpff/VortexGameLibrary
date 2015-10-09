package vortex;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * This class serves as the driver of the Game class. This is the class that will be inherited to 
 * create your game. Contains a static GameDriver object called gameDriver that will be used as the child for the Game class.
 * <p>Example of how to create GameDriver using TestGame as inheriting class:</p>
 *
 * <p>public class TestGame extends GameDriver{ <br>
 *    &nbsp public static void main(String[] args){ <br>
 *     &nbsp &nbsp gameDriver = new TestGame(); <br>
 *     &nbsp &nbsp Game.initGame('title'); <br>
 *     &nbsp &nbsp Game.setChild(gameDriver); <br>
 *     &nbsp &nbsp Game.start(); <br>
 *     &nbsp } <br>
 *     }</p>
 * <p>Note: This is just a template. You will likely need to call some other functions in the Game class to set the size of the window and other properties.
 * Also, you will be prompted to override the 3 abstract functions: <br>
 * {@code public abstract void init(GameContainer gc);} <br>
 * {@code public abstract void render(GameContainer gc, Graphics g);} <br>
 * {@code public abstract void update(GameContainer gc, int i);} <br>
 * These allow you initialize things before the game loop starts as well as render and update them.</p>
 * 
 * @author Daniel Stumpff
 *
 */

public abstract class GameDriver{
	
	/**
	 * The static GameDriver object used.
	 */
	public static GameDriver gameDriver;
	
	/**
	 * Called once on startup. Allows you to initialize any necessary objects and variables.
	 * 
	 * @param gc The GameContainer used by this class
	 */
	public abstract void init(GameContainer gc);
	
	/**
	 * 
	 * Called continuously while game is running. Updates objects each frame.
	 * 
	 * @param gc The GameContainer used by this class
	 * @param i The amount of time that's passed since last update in milliseconds
	 */
	public abstract void update(GameContainer gc, int i);
	
	/**
	 * Called continuously while game is running. Draws object to the game window.
	 * 
	 * @param gc The GameContainer used by this class
	 * @param g The Graphics used by this class
	 */
	public abstract void render(GameContainer gc, Graphics g);
	
	/**
	 * Called when the mouse is moved.
	 * 
	 * @param oldx The position of the mouse's previous x.
	 * @param oldy The position of the mouse's previous y.
	 * @param newx The new position of the mouse's x.
	 * @param newy The new position of the mouse's y.
	 */
	public void mouseMoved(int oldx, int oldy, int newx, int newy){
		//Do nothing
	}
	
	/**
	 * Called when the mouse wheel is moved.
	 * 
	 * @param change The amount the wheel moved
	 */
	public void mouseWheelMoved(int change){
		//Do nothing
	}
}
