package vortex;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;

import vortex.collision.CollisionRectangle;
import vortex.collision.CollisionShape;
import vortex.gameentity.*;
import vortex.Game;

/**
 * This class creates the base for all entities in the game. Anything that can be drawn or updated is considered an entity.
 * This excludes key and mouse input however. All entities have the ability of being drawn to the screen and updated. This does 
 * not mean that all do by default though. For instance a camera does not draw anything to the screen by default, but a Pawn or Map does.
 * 
 * <p>If you wish to create your own entity it is best to inherit from one of the already defined entities, or in the very least, inherit this class directly.</p>
 * 
 * @author Daniel Stumpff
 *
 */
public abstract class GameEntity{
	
	/**
	 * The optional Camera for this entity.
	 */
	private Camera theCamera;
	/**
	 * The start position of the entity.
	 */
	protected Point startPoint;
	/**
	 * The width of the entity.
	 */
	protected float width;
	/**
	 * the height of the entity.
	 */
	protected float height;
	/**
	 * Movement modifier for the entity.
	 */
	protected float vX, vY;
	/**
	 * The combined speed of vX and vY.
	 */
	protected float speed = 0;
	/**
	 * The list of collision bounds for the entity.
	 */
	protected ArrayList<CollisionShape> collisionBox;
	/**
	 * Boolean to draw the collision bounds.
	 */
	protected boolean viewCollision = false;
	/**
	 * The list of entities that collided with this entity in the most recent frame.
	 */
	protected ArrayList<CollisionShape> intersectingObjects = new ArrayList<CollisionShape>();
	/**
	 * True if entity is in the screen and therefore should be rendered.
	 */
	protected boolean visible = true;
	
	/**
	 * The default constructor. Sets the spawn point to the center of the screen.
	 */
	public GameEntity(){
		startPoint = new Point(Game.getScreenWidth() / 2, Game.getScreenHeight() / 2);
		width = 0;
		height = 0;
		vX = vY = 0;
		collisionBox = new ArrayList<CollisionShape>();
	}
	
	/**
	 * A constructor with parameters.
	 * 
	 * @param startPoint The x and y coordinates to spawn at
	 * @param width The width of the entity
	 * @param height The height of the entity.
	 */
	public GameEntity(Point startPoint, float width, float height){
		this.startPoint = startPoint;
		this.width = width;
		this.height = height;
		vX = vY = 0;
		collisionBox = new ArrayList<CollisionShape>();
	}
	
	/**
	 * A constructor with parameters.
	 * 
	 * @param x The x coordinate to spawn at
	 * @param y The y coordinate to spawn at
	 * @param width The width of the entity
	 * @param height The height of the entity
	 */
	public GameEntity(float x, float y, float width, float height){
		startPoint = new Point(x, y);
		this.width = width;
		this.height = height;
		vX = vY = 0;
		collisionBox = new ArrayList<CollisionShape>();
	}
	
	/**
	 * An abstract method that is used to initialize any needed information for the entity.
	 * 
	 * @param gc The GameContainer for the game
	 */
	public abstract void init(GameContainer gc);
	
	/**
	 * Called Continuously while game is running. Handles all updates to objects.
	 * 
	 * @param gc The GameContainer for the game
	 * @param i The amount of time that's passed since last update in milliseconds
	 */
	public void update(GameContainer gc, int i){
		visible = true;
		
		int halfViewportX = (int)Game.getGameCamera().getViewportSize().getX() >> 1;
		int halfViewportY = (int)Game.getGameCamera().getViewportSize().getY() >> 1;
		
		if(getX() + getWidth() < Game.getGameCamera().getGlobalX() - halfViewportX){
			visible = false;
		}
		else if(getX() > Game.getGameCamera().getGlobalX() + halfViewportX){
			visible = false;
		}
		if(getY() + getHeight() < Game.getGameCamera().getGlobalY() - halfViewportY){
			visible = false;
		}
		else if(getY() > Game.getGameCamera().getGlobalY() + halfViewportY){
			visible = false;
		}
			
		intersectingObjects.clear();
		if(collisionBox != null){
			for(int j = 0; j < collisionBox.size(); j++){
				collisionBox.get(j).setCollisionCheckedThisFrame(false);
				collisionBox.get(j).setX(startPoint.getX());
				collisionBox.get(j).setY(startPoint.getY());
				collisionBox.get(j).setHeight(height);
				collisionBox.get(j).setWidth(width);
				collisionBox.get(j).setVX(vX);
				collisionBox.get(j).setVY(vY);
				collisionBox.get(j).updateCollisionLines();
				ArrayList<CollisionShape> returnObjects = new ArrayList<CollisionShape>();
				Game.collisionQuadtree.retrieve(returnObjects, collisionBox.get(j));
				//System.out.println(this.getClass().toString() + ": " + returnObjects.size());
				checkCollisions(collisionBox.get(j), returnObjects);
			}
		}
		
		setX(getX() + vX);
		setY(getY() + vY);
		
		if(theCamera != null){
			theCamera.update(gc, i);
		}
	}
	
	/**
	 * Called continuously while game is running. Used to draw any graphics to the game window.
	 * 
	 * @param gc The GameContainer for the game
	 * @param g The Graphics object to draw to
	 */
	public void render(GameContainer gc, Graphics g){
		if(collisionBox != null && viewCollision){
			g.setColor(Color.white);
			for(int i = 0; i < collisionBox.size(); i++){
				g.drawRect(collisionBox.get(i).getX(), collisionBox.get(i).getY(), collisionBox.get(i).getWidth(), collisionBox.get(i).getHeight());
			}
		}
	}
	
	/**
	 * Checks collision for all potential collision bounds.
	 * 
	 * @param box One of the collision bounds for this entity
	 * @param potentialObjects A list of potential collisions
	 */
	protected void checkCollisions(CollisionShape box, ArrayList<CollisionShape> potentialObjects){
		for(int i = 0; i < potentialObjects.size(); i++){
			if(potentialObjects.get(i) instanceof CollisionRectangle && potentialObjects.get(i) != box){
				if(!potentialObjects.get(i).isCollisionCheckedThisFrame()){
					RectToRectCollision((CollisionRectangle)box, (CollisionRectangle)potentialObjects.get(i));
				}
			}
		}
	}
	
	/**
	 * Runs the algorithm for rect to rect collision.
	 * 
	 * @param box The first rectangle
	 * @param other The second rectangle 
	 */
	protected void RectToRectCollision(CollisionRectangle box, CollisionRectangle other){
		int wallHit = box.intersectsRect(other);
		
		box.setCollisionCheckedThisFrame(true);
		other.setCollisionCheckedThisFrame(true);
		
		if(wallHit != -1){
			intersectingObjects.add(other);
		}
		
		if(wallHit == 0){
			if(box.getMovable()){
				System.out.println("Here 1");
				setX(other.getX() - getWidth() - 1);
				vX = 0;
			}
		}
		else if(wallHit == 1){
			if(box.getMovable()){
				System.out.println("Here 2");
				setX(other.getX() + other.getWidth() + 1);
				vX = 0;
			}
		}
		else if(wallHit == 2){
			if(box.getMovable()){
				System.out.println("Here 3");
				setY(other.getY() - getHeight() - 1);
				vY = 0;
			}
		}
		else if(wallHit == 3){
			if(box.getMovable()){
				System.out.println("Here 3");
				setY(other.getY() + other.getHeight() + 1);
				vY = 0;
			}
		}
	}
	
	public void moveAtAngle(float angle){
		setVX((float)Math.cos(Math.toRadians(angle)) * speed);
		setVY(-(float)Math.sin(Math.toRadians(angle)) * speed);
	}
	
	public void stopMovement(){
		vX = 0;
		vY = 0;
	}
	
	/**
	 * Set the amount to move in the x-axis each frame (default = 0).
	 * 
	 * @param vX The value to move
	 */
	public void setVX(float vX){
		this.vX = vX;
	}
	
	/**
	 * Set the amount to move in the y-axis each frame (default = 0).
	 * @param vY The value to move
	 */
	public void setVY(float vY){
		this.vY = vY;
	}
	
	public void setSpeed(float speed){
		this.speed = speed;
	}
	
	/**
	 * Sets if this entity should move when it collides with another.
	 * 
	 * @param movable Whether it should move or not
	 */
	public void setMovableCollision(boolean movable){
		for(int i = 0; i < collisionBox.size(); i++){
			collisionBox.get(i).setMovable(movable);
		}
	}
	
	/**
	 * Attaches a Camera to this GameEntity (this sets the Camera to use when referring to this GameEntity's Camera).
	 * 
	 * @param camera The Camera to attach to this GameEntity
	 * @return If the Camera was attached successfully
	 */
	public boolean attachCamera(Camera camera){
		if(camera != null){
			theCamera = camera;
			return true;
		}
		
		return false;
	}
	
	/**
	 * Attaches a Camera to the center of this GameEntity.
	 */
	public void useDefaultCamera(){
		theCamera = new Camera(this);
	}
	
	/**
	 * Set the x position of the GameEntity.
	 * 
	 * @param x The new x position
	 */
	public void setX(float x){
		startPoint.setX(x);
	}
	
	/**
	 * Set the y position of the GameEntity.
	 * 
	 * @param y The new y position
	 */
	public void setY(float y){
		startPoint.setY(y);
	}
	
	/**
	 * Set the width of the GameEntity.
	 * 
	 * @param width The new width of the GameEntity
	 */
	public void setWidth(float width){
		this.width = width;
	}
	
	/**
	 * Set the height of the GameEntity.
	 * @param height The new height of the GameEntity
	 */
	public void setHeight(float height){
		this.height = height;
	}
	
	/**
	 * Sets if you can see the entity's collision bounds.
	 * 
	 * @param viewCollision Whether you can see the collison bounds
	 */
	public void setViewCollision(boolean viewCollision){
		this.viewCollision = viewCollision;
	}
	
	/**
	 * Get the x position of the GameEntity.
	 * @return The x position
	 */
	public float getX(){
		return startPoint.getX();
	}
	
	/**
	 * Get the y position of the GameEntity.
	 * @return The y position
	 */
	public float getY(){
		return startPoint.getY();
	}
	
	/**
	 * Get the vX of the GameEntity.
	 * @return The vX
	 */
	public float getVX(){
		return vX;
	}
	
	/**
	 * Get the vY of the GameEntity.
	 * @return The vY
	 */
	public float getVY(){
		return vY;
	}
	
	/**
	 * Get the total current speed of the GameEntity.
	 * @return The total current speed
	 */
	public float getCurSpeed(){
		return (float)Math.sqrt((vX * vX) + (vY * vY));
	}
	
	/**
	 * Get the total set speed of this GameEntity.
	 * @return The set speed
	 */
	public float getSpeed(){
		return speed;
	}
	
	/**
	 * Get the width of the GameEntity.
	 * @return The width
	 */
	public float getWidth(){
		return width;
	}
	
	/**
	 * Get the height of the GameEntity.
	 * @return The height
	 */
	public float getHeight(){
		return height;
	}
	
	/**
	 * @return If you can see the collision bounds
	 */
	public boolean isCollisionViewable(){
		return viewCollision;
	}
	
	/**
	 * Get the Camera used by this GameEntity.
	 * @return The Camera used
	 */
	public Camera getCamera(){
		return theCamera;
	}
	
	/**
	 * Get a specific collision bound from the list.
	 * 
	 * @param index must be greater than -1 and less than the size of the list
	 * @return The collision bound at index
	 */
	public CollisionShape getCollisionBox(int index){
		if(index < collisionBox.size() && index >= 0)
			return collisionBox.get(index);
		
		return null;
	}
	
	public boolean isVisible(){
		return visible;
	}
}
