package vortex.gameentity;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;

import vortex.GameEntity;
import vortex.event.TimerEvent;
import vortex.event.TimerListener;

public abstract class VortexTimer extends GameEntity{
	protected long time;
	protected long timeHolder;
	protected boolean start, pause;
	protected StringBuilder builder;
	protected Formatter formatter;
	protected String formatString;
	protected int hour, minute, second;
	protected boolean useCameraCoordinates;
	protected boolean recurring;
	
	protected ArrayList<TimerListener> listeners = new ArrayList<TimerListener>();
	
	public VortexTimer(float x, float y){
		super(x, y, 0, 0);
		start = pause = false;
		hour = minute = second = 0;
		builder = new StringBuilder();
		formatter = new Formatter(builder);
		useCameraCoordinates = true;
		recurring = false;
		formatString = "%02d:%02d:%02d";
		formatter.format(formatString, hour, minute, second);
	}
	
	public void start(){
		start = true;
	}
	
	public void reset(){
		setTime(timeHolder, 0);
	}
	
	public void pause(){
		pause = true;
	}
	
	public void resume(){
		pause = false;
	}
	
	public synchronized void fireTimerReachedZero(){
		TimerEvent event = new TimerEvent(this);
		
		Iterator<TimerListener> it = listeners.iterator();
		
		while(it.hasNext()){
			it.next().timerReachedZero(event);
		}
	}
	
	public void addTimerListener(TimerListener timerListener){
		listeners.add(timerListener);
	}
	
	public void setFormat(String formatString){
		this.formatString = formatString;
	}
	
	public void setTime(long time, int fps){
		this.time = time;
		this.timeHolder = time;
	}
	
	
	public void setRecurring(boolean recurring){
		this.recurring = recurring;
	}
	
	public void setUseCameraCoordinates(boolean useCameraCoordinates){
		this.useCameraCoordinates = useCameraCoordinates;
	}
	
	public int getHour(){
		return hour;
	}
	
	public int getMinute(){
		return minute;
	}
	
	public int getSecond(){
		return second;
	}
	
	public boolean isUsingCameraCoordinates(){
		return useCameraCoordinates;
	}
	
	public boolean isRecurring(){
		return recurring;
	}
}
