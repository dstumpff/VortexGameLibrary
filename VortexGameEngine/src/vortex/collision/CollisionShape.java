package vortex.collision;

import vortex.GameEntity;
import vortex.geom.Point;
import vortex.geom.Projection;
import vortex.geom.Vector;

public abstract class CollisionShape {
	
	protected Vector[] axes;
	protected Point[] points;
	protected boolean collisionChecked = false;
	protected boolean rigid = false;
	
	public void setRigid(boolean rigid){
		this.rigid = rigid;
	}
	
	public boolean getRigid(){
		return rigid;
	}
	public abstract float getX();
	public abstract float getY();
	public abstract float getCenterX();
	public abstract float getCenterY();
	public abstract float getWidth();
	public abstract float getHeight();
	
	public Point[] getPoints(){
		return points;
	}
	
	public abstract void syncCollision(GameEntity entity);
	
	
	protected abstract void calculateAxes();
	protected abstract Projection project(Vector axis);
}
