package vortex.gameentity.map;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TileSet;
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
		TileSet theTileSet = theTileMap.getTileSet(0);
		SpriteSheet sheet = theTileSet.tiles;
	}
	
	public void update(GameContainer gc, int i){
		setX(getX() + movementVector.getX());
		setY(getY() + movementVector.getY());
		
		if(getCamera() != null){
			getCamera().update(gc, i);
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
		g.setColor(Color.white);
		for(int i = 0; i < theTileMap.getHeight(); i++){
			for(int j = 0; j < theTileMap.getWidth(); j++){
				g.drawRect(theTileMap.getTileWidth() * j, theTileMap.getTileHeight() * i, theTileMap.getTileWidth(), theTileMap.getTileHeight());
			}
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
