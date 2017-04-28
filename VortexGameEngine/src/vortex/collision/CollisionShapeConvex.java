package vortex.collision;

import vortex.geom.Projection;
import vortex.geom.Vector;

public abstract class CollisionShapeConvex extends CollisionShape{
	
	public static Vector intersectsConvex(CollisionShapeConvex r1, CollisionShapeConvex r2){
		if(r1.equals(r2)){
			return new Vector(0,0);
		}
		
		float overlap = Integer.MAX_VALUE;
		Vector smallest = null;
		for(int i = 0; i < r1.axes.length; i++){
			Projection p1 = r1.project(r1.axes[i]);
			Projection p2 = r2.project(r1.axes[i]);
			
			if(!p1.overlap(p2)){
				return new Vector(0,0);
			}
			else{
				float o = p1.getOverlap(p2);
				if( o < overlap){
					overlap = o;
					smallest = r1.axes[i];
				}
			}
		}
		
		for(int i = 0; i < r2.axes.length; i++){
			Projection p1 = r1.project(r2.axes[i]);
			Projection p2 = r2.project(r2.axes[i]);
			
			if(!p1.overlap(p2)){
				return new Vector(0,0);
			}
			else{
				float o = p1.getOverlap(p2);
				if( o < overlap){
					overlap = o;
					smallest = r1.axes[i];
				}
			}
		}
		
		smallest = Vector.multiply(smallest, overlap);
		Vector centerR1 = new Vector(r1.getCenterX(), r1.getCenterY());
		Vector centerR2 = new Vector(r2.getCenterX(), r2.getCenterY());
		Vector r1tor2 = Vector.subtract(centerR1, centerR2);
		
		if(Vector.dotProduct(r1tor2, smallest) < 0){
			smallest.negate();
		}
		
		return smallest;
	}
	
	protected Projection project(Vector axis){
		float min = Vector.dotProduct(axis, new Vector(points[0].getX(), points[0].getY()));
		float max = min;
		
		for(int i = 0; i < points.length; i++){
			float p = Vector.dotProduct(axis, new Vector(points[i].getX(), points[i].getY()));
			if(p < min){
				min = p;
			}
			else if(p > max){
				max = p;
			}
		}
		
		return new Projection(min, max);
	}
}
