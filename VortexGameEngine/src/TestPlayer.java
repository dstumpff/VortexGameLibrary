import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Point;

import vortex.Game;
import vortex.gameentity.Pawn;
import vortex.input.KeyInput;
import vortex.input.MouseInput;
import vortex.input.MouseInputCommand;
import vortex.sound.SoundEffect;


public class TestPlayer extends Pawn {
	
	boolean left, right, up, down;
	
	public TestPlayer(){
		super();
	}
	
	public TestPlayer(Point startPoint, float width, float height){
		super(startPoint, width, height);
	}
	
	public TestPlayer(float x, float y, float width, float height){
		super(x, y, width, height);
	}
	
	public void update(GameContainer gc, int i){
		if(!TestGame.dialogMode){
			if(KeyInput.keyCommandExists("Move+X")){
				if(KeyInput.get("Move+X").getKeyDown()){
					System.out.println("Key Down");
					right = true;
				}
				else if(KeyInput.get("Move+X").getKeyUp()){
					System.out.println("Key Up");
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
				moveAtAngle(180);
			}
			if(right && !(up || down)){
				moveAtAngle(0);
			}
			if(up && !(left || right)){
				moveAtAngle(90);
				//Game.getGameCamera().shakeCamera(50, 0, 5);
			}
			if(down && !(left || right)){
				moveAtAngle(270);
			}
			if(left && up){
				moveAtAngle(135);
			}
			else if(left && down){
				moveAtAngle(225);
			}
			else if(right && up){
				moveAtAngle(45);
			}
			else if(right && down){
				moveAtAngle(315);
			}
			
			if(!(left || right || up || down)){
				stopMovement();
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
