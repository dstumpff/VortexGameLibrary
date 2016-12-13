package vortex.collision;

import org.newdawn.slick.geom.Rectangle;

import vortex.GameEntity;

/**
 * Implementation of CollisionShape for a Rectangular shape.
 * 
 * @author Daniel Stumpff
 *
 */
public class CollisionRectangle extends CollisionShape{
	
	/**
	 * Constructor with parameters.
	 * 
	 * @param x The x position
	 * @param y The y position
	 * @param width The width
	 * @param height The height
	 */
	public CollisionRectangle(float x, float y, float width, float height){
		super(x, y, width, height);
	}
	
	/**
	 * Constructor with parameters.
	 * 
	 * @param x The x position
	 * @param y The y position
	 */
	public CollisionRectangle(float x, float y) {
		super(x, y);
	}
	
	/**
	 * Constructor with parameters.
	 * 
	 * @param rect - The rectangle to base this shape off
	 */
	public CollisionRectangle(Rectangle rect){
		super(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}
	
	/**
	 * Constructor with parameters.
	 * 
	 * @param linkedEntity The entity to link
	 * @param x The x position
	 * @param y The y position
	 * @param width The width
	 * @param height The height
	 */
	public CollisionRectangle(GameEntity linkedEntity, float x, float y, float width, float height){
		super(linkedEntity, x, y, width, height);
	}
	
	/**
	 * Constructor with parameters.
	 * 
	 * @param linkedEntity - The entity to link
	 * @param x The x position
	 * @param y The y position
	 */
	public CollisionRectangle(GameEntity linkedEntity, float x, float y){
		super(linkedEntity, x, y);
	}
	
	protected void initCollisionLines(){
		//Do nothing
	}
	
	public void updateCollisionLines(){
		//Do nothing
	}
	
	public int checkSide(CollisionRectangle a, CollisionRectangle b){
		
		//Checks if position + movement will move into rectangle. This means that 
		//the actually rectangle will never move into the colliding rectangle.
		//This gives a nice smooth collision.
		
		if( (a.x + a.width)  + a.vX >= b.x){
			if(a.x + a.width < b.x){
				return 0; //Hit left side
			}
		}
		
		if(a.x + a.vX <= b.x + b.width){
			if(a.x > b.x + b.width){
				return 1; // Hit right side
			}
		}
		
		if( (a.y + a.height) + a.vY >= b.y){
			if(a.y + a.height < b.y){
				return 2; //Hit top side
			}
		}
		
		if(a.y + a.vY <= b.y + b.height){
			if(a.y > b.y + b.height){
				return 3; //Hit bottom side
			}
		}
		
		return -1;
	}
	
	@Override
	public int intersectsRect(CollisionRectangle other) {
		if (this.x + vX <= other.x + other.width + other.vX &&
		   this.x + this.width + vX >= other.x + other.vX &&
		   this.y + vY <= other.y + other.height + other.vY &&
		   this.height + this.y + vY >= other.y + other.vY) {
			
		    int wall = checkSide(this, other);
		    return wall;
		}
		
		return -1;
		
	}

}
