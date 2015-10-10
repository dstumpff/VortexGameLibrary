package vortex.gameentity.map;

import org.newdawn.slick.GameContainer;

import vortex.gameentity.Map;

public class BasicMap extends Map{
	public BasicMap(float x, float y, float width, float height){
		super(x, y, width, height);
	}
	
	public BasicMap(float width, float height){
		super(0, 0, width, height);
	}

	@Override
	public void init(GameContainer gc) {
		// TODO Auto-generated method stub
		
	}
}
