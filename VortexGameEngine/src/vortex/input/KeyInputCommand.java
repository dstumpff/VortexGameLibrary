package vortex.input;

/**
 * Contains values for key commands. Key commands are used in the KeyInput class to link a command with input from the keyboard.
 * 
 * @author Daniel Stumpff
 *
 */
public class KeyInputCommand {
	/**
	 * The name of the command.
	 */
	private String name;
	/**
	 * The value for the command.
	 */
	private float value;

	private boolean keyDown;
	private boolean keyUp;
	private boolean keyHeld;
	
	/**
	 * The constructor.
	 * 
	 * @param key The key of the command
	 * @param value The value of the command
	 */
	public KeyInputCommand(String name, float value){
		this.name = name;
		this.value = value;
		keyDown = false;
		keyHeld = false;
		keyUp = false;
	}
	
	public void setKeyDown(boolean keyDown){
		this.keyDown = keyDown;
	}
	
	public void setKeyHeld(boolean keyHeld){
		this.keyHeld = keyHeld;
	}
	
	public void setKeyUp(boolean keyUp){
		keyDown = false;
		keyHeld = false;
		this.keyUp = keyUp;
	}
	
	public boolean getKeyDown(){
		boolean temp = keyDown;
		keyDown = false;
		return temp;
	}
	
	public boolean getKeyHeld(){
		return keyHeld;
	}
	
	public boolean getKeyUp(){
		boolean temp = keyUp;
		keyUp = false;
		return temp;
	}
	
	/**
	 * 
	 * @return The value of the command
	 */
	public float getValue(){
		return value;
	}
	
	/**
	 * 
	 * @return The key of the command
	 */
	public String getName(){
		return name;
	}
}
