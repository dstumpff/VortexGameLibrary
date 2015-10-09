package vortex.collision;

import org.newdawn.slick.geom.Line;

import vortex.GameEntity;

class AngledLine{
	Line collisionLine;
	float angle;
	
	public AngledLine(float sx, float sy, float ex, float ey){
		collisionLine = new Line(sx, sy, ex, ey);
	}
}

/**
 * Base collision object. All collision objects are implemented from this class. Any entity created that needs collision should use one of these objects or collision will not work.
 * 
 * @author Daniel Stumpff
 *
 */
public abstract class CollisionShape{
	/**
	 * The linked GameEntity - not all collision needs one, but is useful in certain cases.
	 */
	protected GameEntity linkedEntity;
	/**
	 * The x position of the collision shape.
	 */
	protected float x; 
	/**
	 * The y position of the collision shape.
	 */
	protected float y; 
	/**
	 * The width of the collision shape.
	 */
	protected float width;
	/**
	 * The height of the collision shape.
	 */
	protected float height;
	/**
	 * Movement modifier for the collision shape.
	 */
	protected float vX, vY;
	/**
	 * Used to check for collision
	 */
	protected AngledLine[] collisionLines;
	
	protected boolean collisionCheckedThisFrame = false;
	/**
	 * Whether collision shape is movable when intersecting with another.
	 */
	protected boolean movable = false;
	
	/**
	 * Constructor with parameters.
	 *  
	 * @param x The x position
	 * @param y The y position
	 * @param width The width
	 * @param height The height
	 */
	public CollisionShape(float x, float y, float width, float height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		initCollisionLines();
		vX = vY = 0;
	}
	
	/**
	 * Constructor with parameters.
	 * 
	 * @param x The x position
	 * @param y The y position
	 */
	public CollisionShape(float x, float y){
		this.x = x;
		this.y = y;
		width = 0;
		height = 0;
		initCollisionLines();
		vX = vY = 0;
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
	public CollisionShape(GameEntity linkedEntity, float x, float y, float width, float height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		initCollisionLines();
		vX = vY = 0;
		this.linkedEntity = linkedEntity;
	}
	
	/**
	 * Constructor with parameters.
	 * 
	 * @param linkedEntity - The entity to link
	 * @param x The x position
	 * @param y The y position
	 */
	public CollisionShape(GameEntity linkedEntity, float x, float y){
		this.x = x;
		this.y = y;
		width = 0;
		height = 0;
		initCollisionLines();
		vX = vY = 0;
		this.linkedEntity = linkedEntity;
	}
	
	/**
	 * Initializes the needed collision lines for the shape.
	 */
	protected abstract void initCollisionLines();
	/**
	 * Updates the collision lines to keep in sync with movement.
	 */
	public abstract void updateCollisionLines();
	/**
	 * Runs algorithm for rectangle intersection.
	 * 
	 * @param other The CollisionRectangle to check with this shape
	 * @return Modifier to know where it hit on the other shape
	 */
	public abstract int intersectsRect(CollisionRectangle other);
	
	/**
	 * Sets the x position.
	 * 
	 * @param x New x position
	 */
	public void setX(float x){
		this.x = x;
	}
	
	/**
	 * Sets the y position.
	 * 
	 * @param y New y position
	 */
	public void setY(float y){
		this.y = y;
	}
	
	/**
	 * Sets the width.
	 * 
	 * @param width The new width
	 */
	public void setWidth(float width){
		this.width = width;
	}
	
	/**
	 * Sets the height.
	 * 
	 * @param height The new height
	 */
	public void setHeight(float height){
		this.height = height;
	}
	
	/**
	 * Sets the x movement (default = 0).
	 * 
	 * @param vX The new x movement
	 */
	public void setVX(float vX){
		this.vX = vX;
	}
	
	/**
	 * Sets the y movement (default = 0).
	 * 
	 * @param vY The new y movement
	 */
	public void setVY(float vY){
		this.vY = vY;
	}
	
	public void setCollisionCheckedThisFrame(boolean checked){
		collisionCheckedThisFrame = checked;
	}
	
	/**
	 * Set the entity to link with this collision.
	 * 
	 * @param entity The entity to link
	 */
	public void setLinkedEntity(GameEntity entity){
		this.linkedEntity = entity;
	}
	
	/**
	 * Set if collision can move upon intersection.
	 * 
	 * @param movable Can move or not
	 */
	public void setMovable(boolean movable){
		this.movable = movable;
	}
	
	/**
	 * @return The current x position
	 */
	public float getX(){
		return x;
	}
	
	/**
	 * @return The current y position
	 */
	public float getY(){
		return y;
	}
	/**
	 * 
	 * @return The current width
	 */
	public float getWidth(){
		return width;
	}
	
	/**
	 * 
	 * @return The current height
	 */
	public float getHeight(){
		return height;
	}
	
	/**
	 * 
	 * @return The current vX
	 */
	public float getVX(){
		return vX;
	}
	
	/**
	 * 
	 * @return The current vY
	 */
	public float getVY(){
		return vY;
	}
	
	public boolean isCollisionCheckedThisFrame(){
		return collisionCheckedThisFrame;
	}
	
	/**
	 * 
	 * @return The linked entity
	 */
	public GameEntity getLinkedEntity(){
		return linkedEntity;
	}
	
	/**
	 * @return If movable or not
	 */
	public boolean getMovable(){
		return movable;
	}
}
