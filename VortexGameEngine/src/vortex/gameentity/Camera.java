package vortex.gameentity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;

import vortex.Game;
import vortex.GameEntity;

/**
 * This class defines a Camera that can be used for any GameEntity. Camera's are basically like a viewport.
 * They allow you to center on a certain point in 2D space and view all entities in a relative position to the Camera's point.
 * 
 * <p>The main Camera in any Game is set by Game.setCamera(). This gameCamera is the camera that will serve as the current viewport.</p>
 * 
 * @author Daniel Stumpff
 */
public class Camera extends GameEntity{
	/**
	 * Global coordinate.
	 */
	protected float globalX, globalY;
	/**
	 * Local offset.
	 */
	protected float localX, localY;
	/**
	 * The zoom of the camera (Not implemented yet).
	 */
	protected float magnification;
	/**
	 * Entity linked to this camera.
	 */
	protected GameEntity entity;
	/**
	 * Viewport of camera (default = window dimensions; Not adjustable yet).
	 */
	protected Point viewport;
	/**
	 * Whether camera will sync with horizontal movement (default = true).
	 */
	protected boolean syncHorizontal = true;
	/**
	 * Whether camera will sync with vertical movement (default = true).
	 */
	protected boolean syncVertical = true;
	/**
	 * Whether the camera is shaking or not.
	 */
	protected boolean shaking = false;
	/**
	 * The total amount to shake over the given time.
	 */
	protected float shakeX = 0, shakeY = 0;
	/**
	 * Used for controlling how much shaking of the camera happens.
	 */
	protected float shakeAmount = 0, curShakeAmount = 0;
	
	/**
	 * A constructor that takes an entity. The Camera's point will be located at the center of the GameEntity.
	 * 
	 * @param entity The GameEntity to base this Camera's position on
	 */
	public Camera(GameEntity entity){
		super();
		localX = 0;
		localY = 0;
		viewport = new Point(Game.getScreenWidth(), Game.getScreenHeight());
		globalX = Math.round((entity.getX() + localX + (entity.getWidth() / 2)));
		globalY = Math.round((entity.getY() + localY + (entity.getHeight() / 2)));
		magnification = 1.0f;
		this.entity = entity;
		if(entity != null){
			entity.attachCamera(this);
		}
	}
	
	/**
	 * A constructor with parameters. The Camera's point will be located at the center + offset of the GameEntity.
	 * 
	 * @param entity The GameEntity to base this Camera's position on
	 * @param offsetX The offset from the center in the x-axis
	 * @param offsetY The offset from the center in the y-axis
	 */
	public Camera(GameEntity entity, float offsetX, float offsetY){
		super();
		localX = offsetX;
		localY = offsetY;
		viewport = new Point(Game.getScreenWidth(), Game.getScreenHeight());
		globalX = Math.round((entity.getX() + localX + (entity.getWidth() / 2)));
		globalY = Math.round((entity.getY() + localY + (entity.getHeight() / 2)));
		magnification = 1.0f;
		this.entity = entity;
		if(entity != null){
			entity.attachCamera(this);
		}
	}
	
	/**
	 * A constructor with parameters. The Camera's point will be located at the center + offset of the GameEntity.
	 * 
	 * @param entity The GameEntity to base this Camera's position on.
	 * @param offsetX The offset from the center in the x-axis
	 * @param offsetY The offset from the center in the y-axis
	 * @param magnification The magnification of the Camera (not currently implemented)
	 */
	public Camera(GameEntity entity, float offsetX, float offsetY, float magnification){
		super();
		localX = offsetX;
		localY = offsetY;
		viewport = new Point(Game.getScreenWidth(), Game.getScreenHeight());
		globalX = Math.round((entity.getX() + localX + (entity.getWidth() / 2)));
		globalY = Math.round((entity.getY() + localY + (entity.getHeight() / 2)));
		this.magnification = magnification;
		this.entity = entity;
		if(entity != null){
			entity.attachCamera(this);
		}
	}
	
	@Override
	public void init(GameContainer gc) {
		
	}
	
	/**
	 * The update method for the Camera. Keeps the Camera in sync with the Entity.
	 * <p>Note: If you override this method you MUST call the super or the Camera may not keep in sync.
	 */
	@Override
	public void update(GameContainer gc, int i) {
		if(entity != null){
			if(syncHorizontal){
				globalX = Math.round((entity.getX() + localX + (entity.getWidth() / 2)));
				int viewportX = (int)viewport.getX() >> 1; //divide by 2 using bitwise operator for maximum efficiency
				if(globalX - (viewportX) < Game.getGameMap().getLeftBound()){
					globalX = Game.getGameMap().getLeftBound() + viewportX;
				}
				if(globalX + viewportX > Game.getGameMap().getRightBound()){
					globalX = Game.getGameMap().getRightBound() - viewportX;
				}
				
				if(shaking)
					globalX += (float)(Math.sin(curShakeAmount) * shakeX);
			}
			if(syncVertical){
				globalY = Math.round((entity.getY() + localY + (entity.getHeight() / 2)));
				int viewportY = (int)viewport.getY() >> 1; //divide by 2 using bitwise operator for maximum efficiency
				if(globalY - viewportY < Game.getGameMap().getUpperBound()){
					globalY = Game.getGameMap().getUpperBound() + viewportY; 
				}
				if(globalY + viewportY > Game.getGameMap().getLowerBound()){
					globalY = Game.getGameMap().getLowerBound() - viewportY;
				}
				
				if(shaking)
					globalY += (float)(Math.sin(curShakeAmount) * shakeY);
			}
			
			curShakeAmount += shakeAmount;
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		//Do nothing
	}
	
	/**
	 * Shakes the camera back and forth by the given x and y amount. This version of shakeCamera is very sporadic, it is suggested
	 * that you use shakeCamera(x, y, time) for smoother shaking.
	 * 
	 * @param x The x amount
	 * @param y The y amount
	 */
	public void shakeCamera(float x, float y){
		if(!shaking){
			shakeX = x;
			shakeY = y;
			shaking = true;
			shakeAmount = (float)Math.PI / 2;
			curShakeAmount = shakeAmount;
		}
	}
	
	/**
	 * Shakes the camera back and forth by the given x and y amount over the course of the given time. This version of shakeCamera is best for smoother movement.
	 * 
	 * @param x The x amount
	 * @param y The y amount
	 * @param time The time in frames
	 */
	public void shakeCamera(float x, float y, int time){
		if(!shaking && time > 0){
			shakeCamera(x, y);
			shakeAmount = (float)Math.PI / 2 / time;
			curShakeAmount = shakeAmount;
		}
	}
	
	/**
	 * Steadies the camera.
	 */
	public void steadyCamera(){
		shakeX = 0;
		shakeY = 0;
		shakeAmount = 0;
		curShakeAmount = 0;
		shaking = false;
	}
	
	/**
	 * Tells if the camera will sync with horizontal movement of it's associated entity. (default = true)
	 * 
	 * @param sync True or false
	 */
	public void syncHorizontalMovement(boolean sync){
		syncHorizontal = sync;
	}
	
	/**
	 * Tells if the camera will sync with vertical movement of it's associated entity. (default = true)
	 * @param sync True or false
	 */
	public void syncVerticalMovement(boolean sync){
		syncVertical = sync;
	}
	
	/**
	 * Set the Camera's localX or xOffset.
	 * 
	 * @param x The new localX or xOffset
	 */
	public void setLocalX(float x){
		localX = x;
	}
	
	/**
	 * Set the Camera's localY or yOffset.
	 * 
	 * @param y The new localY or yOffset.
	 */
	public void setLocalY(float y){
		localY = y;
	}
	
	/**
	 * Set the magnification of the Camera. (not currently implemented)
	 * 
	 * @param magnification The magnification.
	 */
	public void setMagnification(float magnification){
		this.magnification = magnification;
	}
	
	/**
	 * Get the Camera's globalX.
	 * 
	 * @return The globalX.
	 */
	public float getGlobalX(){
		return globalX;
	}
	
	/**
	 * Get the Camera's globalY.
	 * 
	 * @return The globalY.
	 */
	public float getGlobalY(){
		return globalY;
	}
	
	/**
	 * Get the Camera's localX or xOffset.
	 * 
	 * @return The localX or xOffset
	 */
	public float getLocalX(){
		return localX;
	}
	
	/**
	 * Get the Camera's localY or yOffset.
	 * 
	 * @return The localY or yOffset
	 */
	public float getLocalY(){
		return localY;
	}
	
	/**
	 * Get the current viewport size.
	 * 
	 * @return The viewport size as a Point.
	 */
	public Point getViewportSize(){
		return viewport;
	}
	
	/**
	 * Check if camera is synced with vertical movement.
	 * 
	 * @return True or false.
	 */
	public boolean isSyncedVertical(){
		return syncVertical;
	}
	
	/**
	 * Check if camera is synced with horizontal movement.
	 * 
	 * @return True or false
	 */
	public boolean isSyncedHorizontal(){
		return syncHorizontal;
	}
	
	/**
	 * Check if camera is currently shaking.
	 * 
	 * @return True or false
	 */
	public boolean isShaking(){
		return shaking;
	}
}
