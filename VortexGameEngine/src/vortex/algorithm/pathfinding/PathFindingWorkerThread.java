package vortex.algorithm.pathfinding;

import vortex.gameentity.map.tilemap.BasicTileMap;

public class PathFindingWorkerThread implements Runnable{
	private int sRow, eRow;
	private int sCol, eCol;
	private BasicTileMap tileMap;
	
	public PathFindingWorkerThread(BasicTileMap tileMap, int sRow, int eRow, int sCol, int eCol) {
		this.sRow = sRow;
		this.eRow = eRow;
		this.sCol = sCol;
		this.eCol = eCol;
		this.tileMap = tileMap;
	}
	
	@Override
	public void run() {
		for(int i = sRow; i < eRow; i++) {
			for(int j = sCol; j < eCol; j++) {
				PathFinding.runDijkstra(tileMap, i, j);
			}
		}
	}

}
