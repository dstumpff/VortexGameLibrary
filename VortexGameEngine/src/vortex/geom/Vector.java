package vortex.geom;

public class Vector {
	float x, y;
	
	
	public Vector(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public static Vector createVectorWithAngle(float magnitude, float angle){
		float x = (float)Math.cos(Math.toRadians(angle)) * magnitude;
		float y = -(float)Math.sin(Math.toRadians(angle)) * magnitude;
		if(angle == 90 || angle == 270){
			x = 0f;
		}
		if(angle == 180){
			y = 0f;
		}
		return new Vector(x, y);
	}
	
	public static Vector add(Vector v1, Vector v2){
		return new Vector(v1.x + v2.x, v1.y + v2.y);
	}
	
	public static Vector subtract(Vector v1, Vector v2){
		return new Vector(v1.x - v2.x, v1.y - v2.y);
	}
	
	public static Vector multiply(Vector v1, float constant){
		return new Vector(v1.x * constant, v1.y * constant);
	}
	
	public static Vector divide(Vector v1, float constant){
		if(constant == 0){
			return v1;
		}
		return new Vector(v1.x / constant, v1.y / constant);
	}
	
	public static float dotProduct(Vector v1, Vector v2){
		return ((v1.x * v2.x) + (v1.y * v2.y));
	}
	
	public static float crossProduct(Vector v1, Vector v2){
		return (v1.x * v2.y) - (v1.y * v2.x);
	}
	
	public static float angleBetween(Vector v1, Vector v2){
		return (float) Math.acos(dotProduct(v1, v2) / v1.getMagnitude() * v2.getMagnitude());
	}
	
	public static float angleFromOrigin(Vector v1){
		Vector origin = new Vector(2,0);
		float dotProduct = dotProduct(v1, origin);
		float angle = (float) Math.acos(dotProduct / (v1.getMagnitude() * origin.getMagnitude()));
		if(v1.y < 0){
			angle = -angle;
		}
		return angle;
	}
	
	public Vector normalize(){
		float magnitude = getMagnitude();
		float normalX = this.x / magnitude;
		float normalY = this.y / magnitude;
		return new Vector (normalX, normalY);
	}
	
	public void negate(){
		x = -x;
		y = -y;
	}
	
	public float getMagnitude(){
		return (float) Math.sqrt((x*x) + (y*y));
	}
	
	public Vector getNormal1(){
		return new Vector(-this.y, this.x);
	}
	
	public Vector getNormal2(){
		return new Vector(this.y, -this.x);
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
}
