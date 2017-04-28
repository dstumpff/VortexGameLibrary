package vortex.collision;

import vortex.GameEntity;
import vortex.geom.Point;
import vortex.geom.Vector;

public class CollisionRectangle extends CollisionShapeConvex{
	
	public CollisionRectangle(float x, float y, float width, float height){
		points = new Point[4];
		points[0] = new Point(x, y);
		points[1] = new Point(x + width, y);
		points[2] = new Point(x + width, y + height);
		points[3] = new Point(x, y + height);
		axes = new Vector[2];
		calculateAxes();
	}
	
	protected void calculateAxes(){
		Vector edge1 = new Vector(getWidth(), 0);
		Vector normal1 = edge1.getNormal1();
		Vector edge2 = new Vector(0, getHeight());
		Vector normal2 = edge2.getNormal1();
		
		normal1 = normal1.normalize();
		normal2 = normal2.normalize();
		
		axes[0] = normal1;
		axes[1] = normal2;

	}
	
	public void syncCollision(GameEntity entity){
		float entityX = entity.getX();
		float entityY = entity.getY();
		float width = entity.getWidth();
		float height = entity.getHeight();
		points[0].setX(entityX);
		points[0].setY(entityY);
		
		points[1].setX(points[0].getX() + width);
		points[1].setY(points[0].getY());
		
		points[2].setX(points[0].getX() + width);
		points[2].setY(points[0].getY() + height);
		
		points[3].setX(points[0].getX());
		points[3].setY(points[0].getY() + height);
	}
	
	@Override
	public float getX() {
		return points[0].getX();
	}

	@Override
	public float getY() {
		return points[0].getY();
	}
	
	@Override
	public float getCenterX() {
		return (points[0].getX() + (getWidth() / 2));
	}

	@Override
	public float getCenterY() {
		return (points[0].getY() + (getHeight() / 2));
	}

	@Override
	public float getWidth() {
		return (points[1].getX() - points[0].getX());
	}

	@Override
	public float getHeight() {
		return (points[2].getY() - points[1].getY());
	}

}
