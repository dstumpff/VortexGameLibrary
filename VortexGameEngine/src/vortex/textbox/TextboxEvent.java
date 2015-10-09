package vortex.textbox;

import java.util.EventObject;

import vortex.Textbox;

/**
 * An event object for Textboxes.
 * 
 * @author Daniel Stumpff
 *
 */
public class TextboxEvent extends EventObject{
	
	/**
	 * The constructor (Only the Textbox should create this event).
	 * 
	 * @param source The source object
	 */
	public TextboxEvent(Object source) {
		super(source);
	}
	
	/**
	 * Get the associated Textbox for this event.
	 * 
	 * @return The associated Textbox
	 */
	public Textbox getTextbox(){
		return (Textbox)getSource();
	}
}
