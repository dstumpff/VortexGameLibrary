package vortex.input;

/**
 * Contains values for mouse button commands. Button commands are used in the MouseInput class to link a command with input from the mouse.
 * 
 * @author Daniel Stumpff
 *
 */
public class MouseInputCommand {
	/**
	 * The name of the command.
	 */
	private String name;
	
	private boolean buttonDown;
	private boolean buttonHeld;
	private boolean buttonUp;
	/**
	 * The value of the command
	 */
	private float value;
	private int x, y;
	
	/**
	 * The constructor.
	 * 
	 * @param button The button of the command
	 * @param value The value of the command
	 */
	public MouseInputCommand(String name, float value){
		this.name = name;
		this.value = value;
		buttonDown = false;
		buttonHeld = false;
		buttonUp = false;
		x = y = 0;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public void setButtonDown(boolean buttonDown){
		this.buttonDown = buttonDown;
	}
	
	public void setButtonHeld(boolean buttonHeld){
		this.buttonHeld = buttonHeld;
	}
	
	public void setButtonUp(boolean buttonUp){
		buttonDown = buttonHeld = false;
		this.buttonUp = buttonUp;
	}
	
	public boolean getButtonDown(){
		boolean temp = buttonDown;
		buttonDown = false;
		return temp;
	}
	
	public boolean getButtonHeld(){
		return buttonHeld;
	}
	
	public boolean getButtonUp(){
		boolean temp = buttonUp;
		buttonUp = false;
		return temp;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public String getName(){
		return name;
	}
	
	public float getValue(){
		return value;
	}
}
