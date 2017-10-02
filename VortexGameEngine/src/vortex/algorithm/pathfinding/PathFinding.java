package vortex.algorithm.pathfinding;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Stack;

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
	protected static Node[][][][] nodeMap;
	protected static boolean nodeMapCreated = false;
	
	public static void setup(int row, int col) {
		nodeMap = new Node[row][col][row][col];
		nodeMapCreated = true;
	}
	
	/**
	 * Finds the paths from the Specified Node to all other nodes using Dijkstra's Algorithm.
	 */
	protected static void runDijkstra(BasicTileMap tileMap, int sRow, int sCol){
		if(!nodeMapCreated) {
			return;
		}
		long startNano = System.nanoTime();
		long startMilli = System.currentTimeMillis();
		int row = tileMap.getTiledMap().getHeight();
		int col = tileMap.getTiledMap().getWidth();
		
		if(sRow >= row || sCol >= col || sRow < 0 || sCol < 0) {
			return;
		}
		//if(nodeMap == null) {
		//	nodeMap = new Node[99][99][99][99];
		//}
		
		boolean checkedAll = false;
		Node[][] nodes = new Node[row][col];
		PriorityQueue<Node> orderedDistances = new PriorityQueue<Node>(2, new NodeComparator());
		PathingNode[][] pathingMap = tileMap.getPathingMap();
		
		if(pathingMap[sRow][sCol].pathable == false){
			return;
		}
		
		System.out.println("Started");
		
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
		
		nodeMap[sRow][sCol] = nodes;
	}
	
	public static void runAllPairShortestPath(BasicTileMap tileMap) {
		int row = tileMap.getTiledMap().getHeight();
		int col = tileMap.getTiledMap().getWidth();
		int threadCount = (int) Math.max(Math.ceil((double)row/5), Math.ceil((double)col/5));
		System.out.println(threadCount);
		if(!nodeMapCreated) {
			setup(tileMap.getTiledMap().getHeight(), tileMap.getTiledMap().getWidth());
		}
		Thread[] threads = new Thread[threadCount];
		for(int i = 0; i < threadCount; i++) {
			threads[i] = new Thread(new PathFindingWorkerThread(tileMap, 5 * i, 5 * (i+1), 5 * i, 5 * (i+1)));
			threads[i].start();
		}
		for(int i = 0; i < threadCount; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static Node[] findPath(int sRow, int sCol, int eRow, int eCol){
		Node curNode = nodeMap[sRow][sCol][eRow][eCol];
		Stack<Node> path = new Stack<Node>();
		while(curNode.from != null){
			path.push(curNode);
			curNode = curNode.from;
		}
		
		Node[] pathList = new Node[path.size()];
		int count = 0;
		while(!path.isEmpty()) {
			curNode = path.pop();
			pathList[count] = curNode;
			count++;
			System.out.println("r: " + curNode.row + ", c: " + curNode.col);
		}
		
		return pathList;
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
	
	public static void reset() {
		nodeMap = null;
		nodeMapCreated = false;
	}
}
