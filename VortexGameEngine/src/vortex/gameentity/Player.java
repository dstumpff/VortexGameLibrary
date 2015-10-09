package vortex.gameentity;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;


/**
 * An extension of the Pawn class. Provides extra functionality.
 * 
 * @author Daniel Stumpff
 *
 */
public class Player extends Pawn{
	
	/**
	 * The optional image to draw.
	 */
	protected Image playerImage;
	
	/**
	 * The default constructor. Sets the spawn point to the center of the screen.
	 */
	public Player(){
		super();
	}
	
	/**
	 * A constructor with parameters.
	 * 
	 * @param startPoint The x and y coordinates to spawn at
	 * @param width The width of the entity
	 * @param height The height of the entity.
	 */
	public Player(Point startPoint, float width, float height){
		super(startPoint, width, height);
	}
	
	/**
	 * A constructor with parameters.
	 * 
	 * @param x The x coordinate to spawn at
	 * @param y The y coordinate to spawn at
	 * @param width The width of the entity
	 * @param height The height of the entity
	 */
	public Player(float x, float y, float width, float height){
		super(x, y, width, height);
	}
	
	public void init(GameContainer gc){
		super.init(gc);
	}
	
	public void update(GameContainer gc, int i){	
		super.update(gc, i);
	}
	
	public void render(GameContainer gc, Graphics g){
		if(playerImage == null){
			super.render(gc, g);
		}
		else{
			g.drawImage(playerImage, getX(), getY());
		}
	}
	
	/**
	 * Set the image to render.
	 * 
	 * @param playerImage The image to render
	 */
	public void setImage(Image playerImage){
		this.playerImage = playerImage;
	}
	
	/**
	 * Set the image to render.
	 * 
	 * @param path The path to the image to render
	 */
	public void setImage(String path){
		try {
			playerImage = new Image(path);
		}catch(SlickException e) {
			Logger.getLogger(Player.class.getName()).log(Level.WARNING, "Could not load Image: " + path, e);
		}
	}
}
