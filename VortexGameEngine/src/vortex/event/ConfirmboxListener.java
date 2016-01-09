package vortex.event;

/**
 * A listener that fires when it's associated Confirmbox performs an action.
 * 
 * @author Daniel Stumpff
 *
 */
public interface ConfirmboxListener extends TextboxListener{
	/**
	 * Runs when source object performs a selection command.
	 * 
	 * @param e The event object
	 */
	public void selectionCommandPerformed(ConfirmboxEvent e);
	/**
	 * Runs when the source object performs a scroll. Use the static variables in the Confirmbox object to determine which scroll was performed.
	 * 
	 * @param e The event object.
	 */
	public void scrollCommandPerformed(ConfirmboxEvent e);
}
