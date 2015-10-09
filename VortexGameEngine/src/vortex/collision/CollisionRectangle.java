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
		
		if(a.x + a.width + a.vX >= b.x + b.vX && a.x + a.vX <= b.x + b.vX){
			if(a.y + a.height + a.vY >= b.y + b.vY && a.y <= b.y + b.height && a.x + a.width > b.x){
				return 0; //Top-Left corner
			}
			if(a.y + a.vY <= b.y + b.height + b.vY && a.x + a.width > b.x){
				return 3; //Bottom-Left corner
			}
			return 1; //Left
		}
		
		if(a.x + a.vX <= b.x + b.width + b.vX && a.x + a.width + a.vX >= b.x + b.width + b.vX){
			if(a.y + a.height + a.vY >= b.y + b.vY && a.y + a.vY < b.y + b.height - b.vY && a.x < b.x + b.width){
				return 0; //Top-Right corner
			}
			if(a.y <= b.y + b.height && a.x < b.x + b.width){
				return 3; //Bottom-Right corner
			}
			return 2; //Right
		}
		
		
		/*if(a.y + a.height + vY > b.y){
			return 0;
		}
		
		if(a.y + vY < b.y + b.height){
			return 3;
		}*/
		
		//Need to find a way to check which side is hit.
		
		return -1;
	}
	
	@Override
	public int intersectsRect(CollisionRectangle other) {
		if (this.x + vX <= other.x + other.width + other.vX &&
		   this.x + this.width + vX >= other.x + other.vX &&
		   this.y + vY <= other.y + other.height + other.vY &&
		   this.height + this.y + vY >= other.y + other.vY) {
			
		    //System.out.println("Collided");
		    int wall = checkSide(this, other);
		   // System.out.println(wall);
		    return wall;
		}
		
		//System.out.println("NOT COLLIDED " + this.x + " " + this.y + " " + other.x + " " + other.y);
		return -1;
		
		/*for(int i = 0; i < collisionLines.length; i++){
			for(int j = 0; j < other.collisionLines.length; j++){
				if(collisionLines[i].collisionLine.intersects(other.collisionLines[j].collisionLine)){
					if(j == 0){
						if(y < other.y && y + height > other.y && x + width - Math.abs(vX) - Math.abs(other.vX) <= other.x){
							return 1;
						}
						if(y < other.y && y + height > other.y && x + Math.abs(vX) + Math.abs(other.vX) >= other.x + other.width){
							return 2;
						}
						if(y + height == other.y && x + width == other.x || y + height == other.y && x == other.x + other.width){
							return -1;
						}
					}
					if(j == 1){
						if(x <= other.x && x + width > other.x && y + Math.abs(vY) + Math.abs(other.vY) >= other.y + other.height){
							return 3;
						}
						
						if(y == other.y + other.height && x + width == other.x){
							return -1;
						}
					}
					if(j == 2){
						if(x < other.x + other.width && x + width >= other.x + other.width && y + Math.abs(vY) + Math.abs(other.vY) >= other.y + other.height){
							return 3;
						}
						if(x == other.x + other.width && y == other.y + other.height){
							return -1;
						}
						
					}
					
					return j;
				}
			}
		}
		
		return -1;*/
	}

}
