package vortex.gameentity.timer;

import java.util.Formatter;
import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import vortex.gameentity.VortexTimer;

public class RealTimer extends VortexTimer{
	private Timer t;
	
	public RealTimer(float x, float y){
		super(x, y);
		t = new Timer();
	}
	
	@Override
	public void setTime(long time, int fps){
		super.setTime(time, fps);
	}
	
	public void reset(){
		t.cancel();
		setTime(timeHolder, 0);
	}
	
	@Override
	public void init(GameContainer gc) {
		// TODO Auto-generated method stub
		
	}
	
	public void update(GameContainer gc, int i){
		if(pause){
			try {
				t.wait(i);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			hour = (int)time / 3600;
			minute = (int)((time / 60) - (hour * 60));
			second = (int)(time % 60);
		}
		
		super.update(gc, i);
	}
	
	public void render(GameContainer gc, Graphics g){
		
		g.setColor(Color.white);
		
		builder = new StringBuilder();
		formatter = new Formatter(builder);
		
		formatter.format(formatString, hour, minute, second);
		g.drawString(formatter.toString(), getX(), getY());
		
		super.render(gc, g);
	}
	
	public void start(){
		t.schedule(new TimerTask(){
			
			@Override
			public void run(){
				if(time <= 0){
					fireTimerReachedZero();
					if(isRecurring()){
						time = timeHolder + 1;
					}
				}
				time--;
			}

		}, 1000, 1000);
		
		start = true;
	}
}
