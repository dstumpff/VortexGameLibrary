package vortex.gameentity;

import org.newdawn.slick.GameContainer;

import vortex.GameEntity;

public abstract class Map extends GameEntity{
	
	public Map(float x, float y, float width, float height){
		super(x, y, width, height);
	}
	
	public Map(float width, float height){
		super(0, 0, width, height);
	}
	
	public void update(GameContainer gc, int i){
		setX(getX() + movementVector.getX());
		setY(getY() + movementVector.getY());
		
		if(getCamera() != null){
			getCamera().update(gc, i);
		}
	}
	
	public float getLeftBound(){
		return startPoint.getX();
	}
	
	public float getRightBound(){
		return startPoint.getX() + width;
	}
	
	public float getUpperBound(){
		return startPoint.getY();
	}
	
	public float getLowerBound(){
		return startPoint.getY() + height;
	}
}
