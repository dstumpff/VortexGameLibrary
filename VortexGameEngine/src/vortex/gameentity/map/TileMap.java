package vortex.gameentity.map;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import vortex.Game;
import vortex.GameEntity;
import vortex.collision.*;
import vortex.gameentity.Map;

/**
 * This class serves as the base for all TileMap types. It inherits GameEntity and therefore
 * has all the properties of a GameEntity.
 * 
 * <p>Currently this object requires a third party application called  "Tiled". This free software can be found at <a href = "http://www.mapeditor.org">http://www.mapeditor.org.</a></p>
 * 
 * <p>Note: In order to properly load a .tmx file you must use the base64(gzip compressed) layer format. Also make sure to import the .tmx and all tileset images or the map will not load.</p>
 * 
 * @author Daniel Stumpff
 */
public abstract class TileMap extends Map{
	
	/**
	 * The TiledMap to use - Thanks to Slick2D for this object.
	 */
	protected TiledMap theTileMap;
	/**
	 * Tiles to render.
	 */
	protected int tilesToRenderX, tilesToRenderY;
	/**
	 * 
	 */
	protected int tilesToRenderStartX, tilesToRenderStartY;
	protected float renderX, renderY;
	
	/**
	 * The constructor. Takes the path for the .tmx and the x and y coordinates.
	 * 
	 * @param path The path of the .tmx file
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @throws SlickException
	 */
	public TileMap(String path, float x, float y) throws SlickException{
		super(x, y, 0, 0);
		theTileMap = new TiledMap(path);
		
		this.setX(x);
		this.setY(y);
		this.setWidth(theTileMap.getWidth() * theTileMap.getTileWidth());
		this.setHeight(theTileMap.getHeight() * theTileMap.getTileHeight());
	}
	
	public void update(GameContainer gc, int i){
		setX(getX() + vX);
		setY(getY() + vY);
		
		if(theCamera != null){
			theCamera.update(gc, i);
		}
		
		tilesToRenderStartX = (int)Math.floor((Game.getGameCamera().getGlobalX()
		- (Game.getGameCamera().getViewportSize().getX()/2))  / theTileMap.getTileWidth());
		renderX = (tilesToRenderStartX - 1) * theTileMap.getTileWidth();
		tilesToRenderStartY = (int)Math.floor((Game.getGameCamera().getGlobalY()
		- (Game.getGameCamera().getViewportSize().getY()/2)) / theTileMap.getTileHeight());
		renderY = (tilesToRenderStartY - 1) * theTileMap.getTileHeight();
		
		tilesToRenderX = (int)Math.ceil(Game.getGameCamera().getViewportSize().getX() / theTileMap.getTileWidth());
		tilesToRenderX += 3;
		tilesToRenderY = (int)Math.ceil(Game.getGameCamera().getViewportSize().getY() / theTileMap.getTileHeight());
		tilesToRenderY += 3;
	}
	
	public void render(GameContainer gc, Graphics g){
		super.render(gc, g);
	}
	
	/**
	 * Takes a 2D array to assign pathing to the map.
	 * 
	 * @param pathingMap The matrix to use for pathing
	 */
	public void setPathing(boolean[][] pathingMap){
		for(int i = 0; i < pathingMap.length; i++){
			for(int j = 0; j < pathingMap[i].length; j++){
				if(pathingMap[i][j]){
					collisionBox.add(new CollisionRectangle(theTileMap.getTileWidth() * j, theTileMap.getTileHeight() * i, theTileMap.getTileWidth(), theTileMap.getTileHeight()));
					Game.collisionShapes.add(collisionBox.get(collisionBox.size() - 1));
				}
			}
		}
		
		optimizePathing();
	}
	
	/**
	 * Optimizes the number of collision objects used for pathing. It assures correct pathing but will merge collision objects to decrease number of collision checks.
	 */
	protected void optimizePathing(){
		ArrayList<ArrayList<CollisionShape>> optimizableRects = new ArrayList<ArrayList<CollisionShape>>();
		
		//Optimize Horizontally
		for(int i = 0; i < collisionBox.size(); i++){
		    int curRectIndex = i;
			optimizableRects.add(new ArrayList<CollisionShape>());
			for(int j = i + 1; j < collisionBox.size(); j++){
				if(collisionBox.get(i).getX() + collisionBox.get(i).getWidth() == collisionBox.get(j).getX()
				&& collisionBox.get(i).getY()  == collisionBox.get(j).getY()){
					optimizableRects.get(optimizableRects.size() - 1).add(collisionBox.get(j));
					i = j;
				}
			}
		
			optimizableRects.get(optimizableRects.size() - 1).add(collisionBox.get(curRectIndex));
		}
		
		for(int i = 0; i < optimizableRects.size(); i++){
			Game.collisionShapes.removeAll(collisionBox);
			
			float newX = optimizableRects.get(i).get(optimizableRects.get(i).size() - 1).getX();
			float newY = optimizableRects.get(i).get(optimizableRects.get(i).size() - 1).getY();
			float newWidth = optimizableRects.get(i).get(optimizableRects.get(i).size() - 1).getWidth();
			float newHeight = optimizableRects.get(i).get(optimizableRects.get(i).size() - 1).getHeight();
			
			for(int j = 0; j < optimizableRects.get(i).size() - 1; j++){
				newWidth += optimizableRects.get(i).get(j).getWidth();
			}
			
			collisionBox.removeAll(optimizableRects.get(i));
			collisionBox.add(new CollisionRectangle(newX, newY, newWidth, newHeight));
			
			Game.collisionShapes.addAll(collisionBox);
			
			optimizableRects.get(i).clear();
		}
		
		optimizableRects.clear();
		
		//Optimize Vertically
		boolean foundOptimizableRect = true;
		while(foundOptimizableRect){
			foundOptimizableRect = false;
			for(int i = 0; i < collisionBox.size(); i++){
			    int curRectIndex = i;
				optimizableRects.add(new ArrayList<CollisionShape>());
				for(int j = i + 1; j < collisionBox.size(); j++){
					if(collisionBox.get(i).getX() == collisionBox.get(j).getX()
					&& collisionBox.get(i).getY() + collisionBox.get(i).getHeight() == collisionBox.get(j).getY()
					&& collisionBox.get(i).getWidth() == collisionBox.get(j).getWidth()){
						optimizableRects.get(optimizableRects.size() - 1).add(collisionBox.get(j));
						i = j;
						foundOptimizableRect = true;
					}
				}
				
				optimizableRects.get(optimizableRects.size() - 1).add(collisionBox.get(curRectIndex));
			}
		
		
			for(int i = 0; i < optimizableRects.size(); i++){
				Game.collisionShapes.removeAll(collisionBox);
				
				float newX = optimizableRects.get(i).get(optimizableRects.get(i).size() - 1).getX();
				float newY = optimizableRects.get(i).get(optimizableRects.get(i).size() - 1).getY();
				float newWidth = optimizableRects.get(i).get(optimizableRects.get(i).size() - 1).getWidth();
				float newHeight = optimizableRects.get(i).get(optimizableRects.get(i).size() - 1).getHeight();
				
				for(int j = 0; j < optimizableRects.get(i).size() - 1; j++){
					newHeight += optimizableRects.get(i).get(j).getHeight();
				}
				
				collisionBox.removeAll(optimizableRects.get(i));
				collisionBox.add(new CollisionRectangle(newX, newY, newWidth, newHeight));
				
				Game.collisionShapes.addAll(collisionBox);
			}
			
			optimizableRects.clear();
		}
	}
	
	/**
	 * Get the TiledMap object used to create the TileMap
	 * 
	 * @return The TiledMap object
	 */
	public TiledMap getTiledMap(){
		return theTileMap;
	}
}
