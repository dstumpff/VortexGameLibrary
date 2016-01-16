package vortex.animation;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import vortex.GameEntity;
import vortex.utilities.ResourceLoader;

public class SpriteAnimation{
	public static final int LEFT_DOWN = 0;
	public static final int RIGHT_DOWN = 1;
	public static final int LEFT_UP = 2;
	public static final int RIGHT_UP = 3;
	
	protected ArrayList<Image> imgs;
	protected int curImg;
	protected boolean running;
	protected boolean looping;
	protected float x, y;
	protected int frameDelay = 1;
	protected int delayAmount = frameDelay;
	protected int rows, cols;
	protected boolean followParent;
	protected GameEntity parent;
	protected boolean reverse;
	
	public SpriteAnimation(float x, float y){
		this.x = x;
		this.y = y;
		curImg = 0;
		imgs = new ArrayList<Image>();
		running = false;
		looping = false;
		followParent = false;
		reverse = false;
	}
	
	public SpriteAnimation(float x, float y, GameEntity parent){
		this.x = x;
		this.y = y;
		curImg = 0;
		imgs = new ArrayList<Image>();
		running = false;
		looping = false;
		followParent = true;
		this.parent = parent;
		reverse = false;
	}
	
	public void playAnimation(){
		if(!running){
			running = true;
			curImg = 0;
			if(reverse){
				curImg = imgs.size() - 1;
			}
		}
	}
	
	public void stopAnimation(){
		if(running){
			running = false;
			curImg = 0;
		}
	}
	
	public void loadSpriteSheet(String path, int rows, int cols, int mode){
		this.rows = rows;
		this.cols = cols;
		Image fullImg = null;
		try{
			fullImg = ResourceLoader.loadImage(path);
			
			int imgWidth = fullImg.getWidth() / cols;
			int imgHeight = fullImg.getHeight() / rows;
			System.out.println(imgWidth + " " + imgHeight);
			
			for(int i = 0; i < rows; i++){
				for(int j = 0; j < cols; j++){
					if(mode == LEFT_DOWN){
						imgs.add(fullImg.getSubImage(imgWidth * j, imgHeight * i, imgWidth, imgHeight));
					}
					else if(mode == RIGHT_DOWN){
						imgs.add(fullImg.getSubImage(fullImg.getWidth() - (imgWidth * (j + 1)), imgHeight * i, imgWidth, imgHeight));
					}
					else if(mode == LEFT_UP){
						imgs.add(fullImg.getSubImage(imgWidth * j, fullImg.getHeight() - (imgHeight * (i + 1)), imgWidth, imgHeight));
					}
					else if(mode == RIGHT_UP){
						imgs.add(fullImg.getSubImage(fullImg.getWidth() - (imgWidth * (j + 1)), fullImg.getHeight() - (imgHeight * (i + 1)), imgWidth, imgHeight));
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("Loaded " + imgs.size() + " images");
	}
	
	public void update(GameContainer gc, int i){
		if(running){
			if(followParent && parent != null){
				x = parent.getX();
				y = parent.getY();
			}
			delayAmount--;
			if(delayAmount <= 0){
				delayAmount = frameDelay;
				if(!reverse){
					if(curImg + 1 < imgs.size()){
						curImg++;
					}
					else if(curImg + 1 >= imgs.size() && !looping){
						stopAnimation();
					}
					else if(curImg + 1 >= imgs.size() && looping){
						curImg = 0;
					}
				}
				else{
					if(curImg - 1 >= 0){
						curImg--;
					}
					else if(curImg - 1 < 0 && !looping){
						stopAnimation();
					}
					else if(curImg - 1 < 0 && looping){
						curImg = imgs.size() - 1;
					}
				}
			}
		}
	}
	
	public void drawAnimation(GameContainer gc, Graphics g){
		if(running){
			if(imgs != null){
				if(imgs.size() > curImg){
					if(imgs.get(curImg) != null){
						g.drawImage(imgs.get(curImg), x, y);
					}
				}
			}
		}
	}
	
	public void setX(float x){
		this.x = x;
	}
	
	public float getX(){
		return x;
	}
	
	public void setY(float y){
		this.y = y;
	}
	
	public float getY(){
		return y;
	}
	
	public void setReverse(boolean reverse){
		this.reverse = reverse;
	}
	
	public boolean isReversed(){
		return reverse;
	}
	
	public void setFrameDelay(int frames){
		frameDelay = frames;
	}
	
	public int getFrameDelay(){
		return frameDelay;
	}
	
	public int getCurFrame(){
		return curImg;
	}
	
	public Image getCurFrameImage(){
		return imgs.get(curImg);
	}
	
	public boolean isRunning(){
		return running;
	}
	
	public void setLooping(boolean looping){
		this.looping = looping;
	}
	
	public boolean isLooping(){
		return looping;
	}
}
