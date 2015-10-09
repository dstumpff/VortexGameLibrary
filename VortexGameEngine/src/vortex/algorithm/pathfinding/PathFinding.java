package vortex.algorithm.pathfinding;

import vortex.gameentity.map.TileMap;

public class PathFinding {
	int[][] mapMatrix;
	
	public PathFinding(int row, int col){
		
	}
	
	public PathFinding(TileMap map){
		mapMatrix = new int[map.getTiledMap().getHeight()][map.getTiledMap().getWidth()];
		
	}
	
	/**
	 * Find a path using Dijkstra's algorithm. This algorithm is guaranteed to find an optimal
	 * solution, if a solution exists.
	 * 
	 */
	public void runDijkstra(){
		
	}
	
	/**
	 * Finds a path using the A* algorithm.
	 * 
	 * <p>The A* algorithm (pronounced A-Star) is a variant of dijkstra's algorithm that uses 
	 * a heuristic to check fewer nodes then dijkstra's. Assuming this heuristic is admissable,
	 * then it is guaranteed to also be an optimal solution. I have included this one and dijkstra's
	 * because this algorithm is not always guaranteed to be an optimal solution, but it is more
	 * performant.</p>
	 */
	public void runAStar(){
		
	}
}
