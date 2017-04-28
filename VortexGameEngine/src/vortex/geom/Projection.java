package vortex.geom;

public class Projection {
	float min, max;
	float overlap = 0;
	
	public Projection(float min, float max){
		this.min = min;
		this.max = max;
	}
	
	public boolean overlap(Projection other){
		if(other.getMin() < getMax() && other.getMin() > getMin()){
			overlap = this.max - other.min;
			return true;
		}
		if(other.getMax() < getMax() && other.getMax() > getMin()){
			overlap = other.max - this.min;
			return true;
		}
		if(getMin() < other.getMax() && getMin() > other.getMin()){
			overlap = other.max - this.min;
			return true;
		}
		if(getMax() < other.getMax() && getMax() > other.getMin()){
			overlap = this.max - other.min;
			return true;
		}
		if(getMax() == other.getMax() && getMin() == other.getMin()){
			overlap = max - min;
			return true;
		}
		return false;
	}
	
	public float getOverlap(Projection p2){
		float start = Math.max(min, p2.min);
		float end = Math.min(max, p2.max);
		float d = end - start;
		if(d < 0){
			return 0;
		}
		else
			return d;
	}
	
	public float getMin(){
		return min;
	}
	
	public float getMax(){
		return max;
	}
}
