package vortex.algorithm.pathfinding;

public class PathingNode {
	int cost;
	boolean pathable;
	
	public PathingNode(boolean pathable){
		cost = 1;
		this.pathable = pathable;
	}
	
	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public boolean isPathable() {
		return pathable;
	}

	public void setPathable(boolean pathable) {
		this.pathable = pathable;
	}
	
}
