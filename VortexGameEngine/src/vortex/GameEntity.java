package vortex;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;

import vortex.collision.CollisionRectangle;
import vortex.collision.CollisionShape;
import vortex.collision.CollisionShapeConvex;
import vortex.gameentity.*;
import vortex.geom.Vector;
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
	protected Vector movementVector;
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
		movementVector = new Vector(0,0);
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
		movementVector = new Vector(0,0);
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
		movementVector = new Vector(0,0);
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
		boolean foundCollision = false;
		
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
		
		setX(getX() + movementVector.getX());
		setY(getY() + movementVector.getY());
		
		intersectingObjects.clear();
		if(collisionBox != null){
			movementVector = new Vector(0,0);
			for(int j = 0; j < collisionBox.size(); j++){
				collisionBox.get(j).syncCollision(this);
				ArrayList<CollisionShape> potentialObjects = new ArrayList<CollisionShape>();
				potentialObjects = Game.collisionQuadtree.retrieve(potentialObjects, collisionBox.get(j));
				//System.out.println(this.getClass().toString() + ": " + returnObjects.size());
				foundCollision = checkCollisions(collisionBox.get(j), potentialObjects);
			}
		}
		if(foundCollision){
			setX(getX() + movementVector.getX());
			setY(getY() + movementVector.getY());
		}
		
		if(theCamera != null){
			theCamera.update(gc, i);
		}
	}
	
	public boolean checkCollisions(CollisionShape shape, ArrayList<CollisionShape> potentialObjects){
		boolean foundCollision = false;
		for(int i = 0; i < potentialObjects.size(); i++){
			if(shape instanceof CollisionShapeConvex && potentialObjects.get(i) instanceof CollisionShapeConvex){
				CollisionShapeConvex s1 = (CollisionShapeConvex) shape;
				CollisionShapeConvex s2 = (CollisionShapeConvex) potentialObjects.get(i);
				Vector mtv = CollisionShapeConvex.intersectsConvex(s1, s2);
				if(mtv.getX() != 0 || mtv.getY() != 0){
					System.out.println("Collision Detected");
					System.out.println("MTV Magnitude: " + mtv.getMagnitude());
					System.out.println("MTV angle: " + Vector.angleFromOrigin(mtv));
					if(!shape.getRigid()){
						movementVector = Vector.add(movementVector, mtv);
					}
					foundCollision = true;
				}
			}
		}
		
		return foundCollision;
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
	
	public void setMovement(float magnitude, float angle){
		this.movementVector = Vector.createVectorWithAngle(magnitude, angle);
	}
	
	public void stopMovement(){
		this.movementVector = Vector.multiply(movementVector, 0);
	}
	
	
	/**
	 * Sets if this entity should move when it collides with another.
	 * 
	 * @param movable Whether it should move or not
	 */
	public void setRigid(boolean rigid){
		for(int i = 0; i < collisionBox.size(); i++){
			collisionBox.get(i).setRigid(rigid);
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
	
	public Vector getMovementVector(){
		return movementVector;
	}
	
	/**
	 * Get the total current speed of the GameEntity.
	 * @return The total current speed
	 */
	public float getCurSpeed(){
		return this.movementVector.getMagnitude();
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
