package vortex.textbox;

/**
 * A listener that fires when it's associated Textbox performs a selection command.
 * 
 * @author Daniel Stumpff
 *
 */
public interface TextboxListener {
	/**
	 * Fires when the textbox has stopped.
	 * 
	 * @param e The event
	 */
	public void textboxStopped(TextboxEvent e);
}
