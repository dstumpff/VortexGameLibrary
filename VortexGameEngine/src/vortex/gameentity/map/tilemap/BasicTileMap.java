package vortex.gameentity.map.tilemap;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import vortex.Game;
import vortex.algorithm.pathfinding.PathingNode;
import vortex.collision.CollisionRectangle;
import vortex.collision.CollisionShape;
import vortex.gameentity.map.TileMap;

/**
 * Implements the TileMap class. This is the simplest form of TileMap.
 * 
 * <p>Currently this object requires a third party application called  "Tiled". This free software can be found at <a href = "http://www.mapeditor.org">http://www.mapeditor.org.</a></p>
 * 
 * <p>Note: In order to properly load a .tmx file you must use the base64(gzip compressed) layer format. Also make sure to import the .tmx and all tileset images or the map will not load.</p>
 * 
 * @author Daniel Stumpff
 */
public class BasicTileMap extends TileMap{
	
	protected int[] collisionTileIds;
	protected PathingNode[][] pathingMap;
	
	/**
	 * The constructor. Takes the path for the .tmx and the x and y coordinates.
	 * 
	 * @param path The path of the .tmx file
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @throws SlickException
	 */
	public BasicTileMap(String path) throws SlickException {
		super(path, 0, 0);
		pathingMap = new PathingNode[theTileMap.getWidth()][theTileMap.getHeight()];
	}

	@Override
	public void init(GameContainer gc) {
		// TODO Auto-generated method stub
		
	}
	
	public void update(GameContainer gc, int i){
		
		if(collisionBox != null){
			for(int j = 0; j < collisionBox.size(); j++){
				//collisionBox.get(j).syncCollision(this);
				ArrayList<CollisionShape> potentialObjects = new ArrayList<CollisionShape>();
				potentialObjects = Game.collisionQuadtree.retrieve(potentialObjects, collisionBox.get(j));
				//System.out.println(this.getClass().toString() + ": " + returnObjects.size());
				//checkCollisions(collisionBox.get(j), potentialObjects);
			}
		}
		
		super.update(gc, i);
	}

	@Override
	public void render(GameContainer gc, Graphics g){
		if(theTileMap != null){
			theTileMap.render((int)renderX, (int)renderY, tilesToRenderStartX-1, tilesToRenderStartY-1, tilesToRenderX, tilesToRenderY);
		}
		
		super.render(gc, g);
	}
	
	/**
	 * Assigns Collision to the TileMap
	 * 
	 */
	public void setCollision(){
		int r = theTileMap.getHeight();
		int c = theTileMap.getWidth();
		for(int i = 0; i < r; i++){
			for(int j = 0; j < c; j++){
				pathingMap[i][j] = new PathingNode(true);
				for(int k = 0; k < collisionTileIds.length; k++){
					if(theTileMap.getTileId(j, i, 0) == collisionTileIds[k]){
						CollisionRectangle collision = new CollisionRectangle(theTileMap.getTileWidth() * j, theTileMap.getTileHeight() * i, theTileMap.getTileWidth(), theTileMap.getTileHeight());
						collision.setRigid(true);
						this.collisionBox.add(collision);
						Game.collisionShapes.add(collision);
						pathingMap[i][j].setPathable(false);
						break;
					}
				}
			}
		}
	}
	
	public void assignCollisionTiles(int[] tileIds){
		this.collisionTileIds = tileIds;
	}
	
	public PathingNode[][] getPathingMap(){
		return pathingMap;
	}
}
