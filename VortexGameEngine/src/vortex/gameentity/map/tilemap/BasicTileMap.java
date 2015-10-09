package vortex.gameentity.map.tilemap;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import vortex.Game;
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
	}

	@Override
	public void init(GameContainer gc) {
		// TODO Auto-generated method stub
		
	}
	
	public void update(GameContainer gc, int i){
		
		if(collisionBox != null){
			for(int j = 0; j < collisionBox.size(); j++){
				//collisionBox.get(j).setX(startPoint.getX());
				//collisionBox.get(j).setY(startPoint.getY());
				collisionBox.get(j).setVX(vX);
				collisionBox.get(j).setVY(vY);
				collisionBox.get(j).updateCollisionLines();
				ArrayList<CollisionShape> returnObjects = new ArrayList<CollisionShape>();
				Game.collisionQuadtree.retrieve(returnObjects, collisionBox.get(j));
				checkCollisions(collisionBox.get(j), returnObjects);
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
}
