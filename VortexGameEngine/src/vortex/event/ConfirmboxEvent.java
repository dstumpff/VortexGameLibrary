package vortex.event;

/**
 * An event object for the Confirmbox object.
 * 
 * @author Daniel Stumpff
 *
 */
public class ConfirmboxEvent extends TextboxEvent{
	
	public static final int NULL_COMMAND = -1;
	public static final int SCROLL_DOWN = 0;
	public static final int SCROLL_UP = 1;
	public static final int SCROLL_RIGHT = 2;
	public static final int SCROLL_LEFT = 3;
	
	/**
	 * The selected command (The command highlighted when performed)
	 */
	protected int selectionCommand;
	/**
	 * The scroll command.
	 */
	protected int scrollCommand;
	
	/**
	 * The constructor (Only the Confirmbox should create this event).
	 * 
	 * @param source The source object
	 * @param selectionCommand The selection command
	 * @param scrollCommand The scroll command
	 */
	public ConfirmboxEvent(Object source, int selectionCommand, int scrollCommand) {
		super(source);
		this.selectionCommand = selectionCommand;
		this.scrollCommand = scrollCommand;
	}
	
	/**
	 * Returns the selection command performed.
	 * 
	 * @return The selection command
	 */
	public int getSelectionCommand(){
		return selectionCommand;
	}
	
	/**
	 * Returns the scroll command performed.
	 * 
	 * @return The scroll command
	 */
	public int getScrollCommand(){
		return scrollCommand;
	}

}
