package vortex.gameentity.timer;

import java.util.Formatter;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import vortex.Game;
import vortex.gameentity.VortexTimer;

public class GameTimer extends VortexTimer{
	
	protected int fps;
	protected int fpsHolder;
	
	public GameTimer(float x, float y) {
		super(x, y);
	}

	@Override
	public void init(GameContainer gc) {
		
	}
	
	@Override
	public void update(GameContainer gc, int i){
		
		hour = (int)time / 3600;
		minute = (int)((time / 60) - (hour * 60));
		second = (int)(time % 60);
		
		if(start && time >= 0){
			
			if(time == 0 && fps == 0){
				fireTimerReachedZero();
				if(isRecurring()){
					reset();
				}
			}
			
			if(fps <= 0){
				time--;
				fps = fpsHolder;
			}
			
			fps--;
		}
	}
	
	public void render(GameContainer gc, Graphics g){
		if(isUsingCameraCoordinates()){
			g.translate(-Game.getGraphicsTranslationX(), -Game.getGraphicsTranslationY());
		}
		
		g.setColor(Color.white);
		
		builder = new StringBuilder();
		formatter = new Formatter(builder);
		formatter.format(formatString, hour, minute, second);
		
		g.drawString(formatter.toString(), getX(), getY());
		
		if(isUsingCameraCoordinates()){
			g.translate(Game.getGraphicsTranslationX(), Game.getGraphicsTranslationY());
		}
		
		super.render(gc, g);
	}
	
	@Override
	public void setTime(long time, int fps){
		super.setTime(time, fps);
		this.fps = fps;
		this.fpsHolder = fps;
	}
	
	@Override
	public void reset(){
		setTime(timeHolder, fpsHolder);
	}
}
