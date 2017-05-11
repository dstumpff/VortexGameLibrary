import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Point;

import vortex.Game;
import vortex.animation.SpriteAnimation;
import vortex.gameentity.Pawn;
import vortex.input.KeyInput;
import vortex.input.MouseInput;
import vortex.input.MouseInputCommand;
import vortex.sound.SoundEffect;


public class TestPlayer extends Pawn {
	
	boolean left, right, up, down;
	float movementMagnitude = 5;
	
	public TestPlayer(){
		super();
		setupAnimations();
	}
	
	public TestPlayer(Point startPoint, float width, float height){
		super(startPoint, width, height);
		setupAnimations();
	}
	
	public TestPlayer(float x, float y, float width, float height){
		super(x, y, width, height);
		setupAnimations();
	}
	
	public void setupAnimations(){
		/*SpriteAnimation anim;
		anim = new SpriteAnimation(getX(), getY(), this);
		anim.loadSpriteSheet("runningcatscaled.png", 4, 2, SpriteAnimation.LEFT_DOWN);
		anim.setFrameDelay(5);
		anim.setLooping(true);
		addAnimation(anim);
		
		anim = new SpriteAnimation(getX(), getY(), this);
		anim.loadSpriteSheet("runningcatscaledleft.png", 4, 2, SpriteAnimation.RIGHT_DOWN);
		anim.setFrameDelay(5);
		anim.setLooping(true);
		addAnimation(anim);*/
	}
	
	public void update(GameContainer gc, int i){
		if(!TestGame.dialogMode){
			if(KeyInput.keyCommandExists("Move+X")){
				if(KeyInput.get("Move+X").getKeyDown()){
					right = true;
				}
				else if(KeyInput.get("Move+X").getKeyUp()){
					right = false;
				}
			}
			if(KeyInput.keyCommandExists("Move-X")){
				if(KeyInput.get("Move-X").getKeyDown()){
					left = true;
				}
				else if(KeyInput.get("Move-X").getKeyUp()){
					left = false;
				}
			}
			
			if(KeyInput.keyCommandExists("Move+Y")){
				if(KeyInput.get("Move+Y").getKeyDown()){
					down = true;
				}
				else if(KeyInput.get("Move+Y").getKeyUp()){
					down = false;
				}
			}
			
			if(KeyInput.keyCommandExists("Move-Y")){
				if(KeyInput.get("Move-Y").getKeyDown()){
					up = true;
				}
				else if(KeyInput.get("Move-Y").getKeyUp()){
					up = false;
				}
			}
			
			if(left && !(up || down)){
				setMovement(movementMagnitude, 180);
				runAnimation(1);
			}
			if(right && !(up || down)){
				setMovement(movementMagnitude, 0);
				runAnimation(0);
			}
			if(up && !(left || right)){
				setMovement(movementMagnitude, 90);
				//Game.getGameCamera().shakeCamera(50, 0, 5);
			}
			if(down && !(left || right)){
				setMovement(movementMagnitude, 270);
			}
			if(left && up){
				setMovement(movementMagnitude, 135);
			}
			else if(left && down){
				setMovement(movementMagnitude, 225);
			}
			else if(right && up){
				setMovement(movementMagnitude, 45);
			}
			else if(right && down){
				setMovement(movementMagnitude, 315);
			}
			
			if(!(left || right || up || down)){
				stopMovement();
				stopAnimation();
				//Game.getGameCamera().steadyCamera();
			}
			
			MouseInputCommand command = MouseInput.get("Attack");
			if(command.getButtonDown()){
				System.out.println("Pressed at: (" + command.getX() + ", " + command.getY() + ")");
			}
			
		}
		
		super.update(gc, i);
	}
}
