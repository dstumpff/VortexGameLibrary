package vortex.algorithm.pathfinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

import vortex.gameentity.map.tilemap.BasicTileMap;

class Node{
	private static int INFINITY = Integer.MAX_VALUE;
	
	int row, col = -1;
	int distance = INFINITY;
	Node from = null;
	
	public Node(int row, int col){
		this.row = row;
		this.col = col;
	}
}

class NodeComparator implements Comparator<Node>{

	@Override
	public int compare(Node n1, Node n2) {
		if(n1.distance < n2.distance){
			return -1;
		}
		if(n1.distance > n2.distance){
			return 1;
		}
		return 0;
	}
	
}

public class PathFinding {
	protected static Node[][] nodeMap;
	
	/**
	 * Finds the paths from the Specified Node to all other nodes using Dijkstra's Algorithm.
	 */
	public static void runDijkstra(BasicTileMap tileMap, int sRow, int sCol){
		long startNano = System.nanoTime();
		long startMilli = System.currentTimeMillis();
		int row = tileMap.getTiledMap().getHeight();
		int col = tileMap.getTiledMap().getWidth();
		
		boolean checkedAll = false;
		Node[][] nodes = new Node[row][col];
		PriorityQueue<Node> orderedDistances = new PriorityQueue<Node>(2, new NodeComparator());
		PathingNode[][] pathingMap = tileMap.getPathingMap();
		
		if(pathingMap[sRow][sCol].pathable = false){
			return;
		}
		
		for(int i = 0; i < nodes.length; i++){
			for(int j = 0; j < nodes[0].length; j++){
				nodes[i][j] = new Node(i, j);
				if(pathingMap[i][j].pathable == true){
					orderedDistances.add(nodes[i][j]);
				}
			}
		}
		
		orderedDistances.remove(nodes[sRow][sCol]);
		nodes[sRow][sCol].distance = 0;
		orderedDistances.add(nodes[sRow][sCol]);
		
		Node curNode = orderedDistances.poll();
		for(int i = 0; i < row && !checkedAll; i++){
			for(int j = 0; j < col && !checkedAll; j++){
				Node[] neighbors = findNeighbors(tileMap, pathingMap, curNode.row, curNode.col);
				for(int k = 0; k < neighbors.length; k++){
					if(nodes[neighbors[k].row][neighbors[k].col].distance > curNode.distance + pathingMap[neighbors[k].row][neighbors[k].col].cost){
						orderedDistances.remove(nodes[neighbors[k].row][neighbors[k].col]);
						nodes[neighbors[k].row][neighbors[k].col].distance = curNode.distance + pathingMap[neighbors[k].row][neighbors[k].col].cost;
						nodes[neighbors[k].row][neighbors[k].col].from = curNode;
						orderedDistances.add(nodes[neighbors[k].row][neighbors[k].col]);
					}
				}
				if(!orderedDistances.isEmpty()){
					curNode = orderedDistances.poll();
				}
				else{
					checkedAll = true;
					break;
				}
			}
		}
		
		long endNano = System.nanoTime();
		long endMilli = System.currentTimeMillis();
		System.out.println("Runtime: Nano: " + (endNano - startNano) + ", Milli: " + (endMilli - startMilli));
		
		
		nodeMap = nodes;
	}
	
	public static void findPath(int r, int c){
		Node curNode = nodeMap[r][c];
		while(curNode.from != null){
			System.out.println("r: " + curNode.row + ", c: " + curNode.col);
			curNode = curNode.from;
		}
	}
	
	private static Node[] findNeighbors(BasicTileMap tileMap, PathingNode[][] pathingMap, int row, int col){
		ArrayList<Node> neighbors = new ArrayList<Node>();
		if(row - 1 >= 0){
			if(pathingMap[row-1][col].pathable == true){
				neighbors.add(new Node(row-1,col));
			}
		}
		if(row + 1 < tileMap.getTiledMap().getHeight()){
			if(pathingMap[row+1][col].pathable == true){
				neighbors.add(new Node(row+1,col));
			}
		}
		if(col - 1 >= 0){
			if(pathingMap[row][col-1].pathable == true){
				neighbors.add(new Node(row,col-1));
			}
		}
		if(col + 1 < tileMap.getTiledMap().getWidth()){
			if(pathingMap[row][col+1].pathable == true){
				neighbors.add(new Node(row,col+1));
			}
		}
		
		return neighbors.toArray(new Node[0]);
	}
}
