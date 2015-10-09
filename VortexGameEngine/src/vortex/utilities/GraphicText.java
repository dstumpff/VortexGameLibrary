package vortex.utilities;

/**
 * Stores a string with x and y coordinates. Mainly used for textboxes.
 * 
 * @author Daniel Stumpff
 *
 */
public class GraphicText{
	/**
	 * A coordinate of the string.
	 */
	protected float x, y;
	/**
	 * The string.
	 */
	protected String text;
	
	/**
	 * The constructor
	 * 
	 * @param x The x position
	 * @param y The y position
	 * @param text The string
	 */
	public GraphicText(float x, float y, String text){
		this.x = x;
		this.y = y;
		this.text = text;
	}
	
	/**
	 * Get the x position.
	 * 
	 * @return The x position
	 */
	public float getX(){
		return x;
	}
	
	/**
	 * Get the y position.
	 * 
	 * @return The y position
	 */
	public float getY(){
		return y;
	}
	
	/**
	 * Get the string.
	 * 
	 * @return The string
	 */
	public String getText(){
		return text;
	}
}
