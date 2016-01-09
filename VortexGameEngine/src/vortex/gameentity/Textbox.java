package vortex.gameentity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.RoundedRectangle;

import vortex.GameEntity;
import vortex.event.TextboxEvent;
import vortex.event.TextboxListener;
import vortex.input.KeyInput;
import vortex.utilities.GraphicText;

/**
 * An abstract class for creating Textbox objects. Textbox objects allow for dialog, choice options and various other things.
 * 
 * @author Daniel Stumpff
 */

public abstract class Textbox extends GameEntity{
	
	public static final int TOP_MARGIN = 0;
	public static final int BOTTOM_MARGIN = 1;
	public static final int LEFT_MARGIN = 2;
	public static final int RIGHT_MARGIN = 3;
	
	/**
	 * The list of TextboxListeners.
	 */
	protected List<TextboxListener> listeners = new ArrayList<TextboxListener>();
	/**
	 * Array for margins in this order: top, bottom, left, right
	 */
	protected int[] margins = {20, 20, 10, 10};
	/**
	 * The list of dialog for the box.
	 */
	protected ArrayList<String> text;
	/**
	 * The list of header's for each dialog.
	 */
	protected ArrayList<String> header;
	/**
	 * The current text to display.
	 */
	protected int curText = 0;
	/**
	 * The font for text.
	 */
	protected TrueTypeFont textFont;
	/**
	 * The box that contains the text.
	 */
	protected RoundedRectangle textContainer;
	/**
	 * The box that contains the header (usually the talker's name).
	 */
	protected RoundedRectangle headerContainer;
	/**
	 * The color of text in the textbox.
	 */
	protected Color textColor = Color.white;
	/**
	 * The color of the container if drawn.
	 */
	protected Color textContainerColor = new Color(Color.black.getRed(), Color.black.getGreen(), Color.black.getBlue(), 150);
	/**
	 * The color of the container for the header.
	 */
	protected Color headerContainerColor = new Color(Color.black.getRed(), Color.black.getGreen(), Color.black.getBlue(), 150);
	/**
	 * The color of the text for the header.
	 */
	protected Color headerColor = Color.white;
	/**
	 * Assigns focus to this textbox.
	 */
	protected boolean focused = false;
	/**
	 * True if textbox has started rendering.
	 */
	protected boolean start = false;
	/**
	 * Tells when the textbox has finished.
	 */
	protected boolean finished = false;
	/**
	 * True if the textbox uses a header.
	 */
	protected boolean hasHeader = false;
	/**
	 * True if textbox is paused.
	 */
	protected boolean paused = false;
	/**
	 * Hides the textbox, but does not stop it.
	 */
	protected boolean hidden = false;
	/**
	 * Makes textbox's x and y be synced with the camera (Means it will stay in the same spot in the viewport).
	 */
	protected boolean useCameraCoordinates = false;
	/**
	 * Internal timer for updating number of characters to draw.
	 */
	protected int textTimer = 0;
	/**
	 * Controls how fast text is drawn (higher = slower).
	 */
	protected int textSpeed = 3;
	/**
	 * Controls how many characters are drawn each time the timer is reached.
	 */
	protected int charsPerUpdate = 1;
	/**
	 * Each line of text and their draw point.
	 */
	protected ArrayList<GraphicText> graphicTextLines;
	
	/**
	 * The constructor.
	 * 
	 * @param x The x position of the textbox
	 * @param y The y position of the textbox
	 * @param width The width of the textbox
	 * @param height The height of the textbox
	 */
	public Textbox(float x, float y, float width, float height){
		super(x, y, width, height);
		textContainer = new RoundedRectangle(x, y, width, height, 10f);
		headerContainer = new RoundedRectangle(x, y - 40, 100, 35, 10f);
		graphicTextLines = new ArrayList<GraphicText>();
		textFont = new TrueTypeFont(new java.awt.Font("Calibri", java.awt.Font.PLAIN, 16), false);
		text = new ArrayList<String>();
		header = new ArrayList<String>();
		KeyInput.addInputCommand("Run Textbox Action", Input.KEY_ENTER, 0);
	}
	
	/**
	 * Loads dialog from a file. (Use the included stand-alone program to make textbox files).
	 * 
	 * @param path The path of the file.
	 * 
	 * @return True if successful load.
	 */
	public abstract boolean load(String path);
	
	/**
	 * Generates the lines of text for the textbox.
	 */
	protected abstract void generateTextLines();
	
	/**
	 * Tells that the textbox has stopped.
	 */
	protected synchronized void fireTextboxStopped(){
		TextboxEvent event = new TextboxEvent(this);
		Iterator<TextboxListener> iterator = listeners.iterator();
		
		while(iterator.hasNext()){
			iterator.next().textboxStopped(event);
		}
	}
	
	/**
	 * Set the Action key.
	 * 
	 * @param key The new key to assign
	 */
	public static void setTextboxActionKey(int key){
		KeyInput.removeInputCommand("Run Textbox Action");
		KeyInput.addInputCommand("Run Textbox Action", key, 0);
	}
	
	/**
	 * Set the scroll up key if the textbox supports it. Note that this is static and will apply to all current and future textboxes.
	 * 
	 * @param key The new key to assign
	 */
	public static void setTextboxScrollUpKey(int key){
		KeyInput.removeInputCommand("Confirm Dialogbox Key Up");
		KeyInput.addInputCommand("Confirm Dialogbox Key Up", key, 0);
	}
	
	/**
	 * Set the scroll down key if the textbox supports it. Note that this is static and will apply to all current and future textboxes.
	 * 
	 * @param key The new key to assign.
	 */
	public static void setTextboxScrollDownKey(int key){
		KeyInput.removeInputCommand("Confirm Dialogbox Key Down");
		KeyInput.addInputCommand("Confirm Dialogbox Key Down", key, 0);
	}
	
	/**
	 * Set the scroll left key if the textbox supports it. Note that this is static and will apply to all current and future textboxes.
	 * 
	 * @param key The new key to assign.
	 */
	public static void setTextboxScrollLeftKey(int key){
		KeyInput.removeInputCommand("Confirm Dialogbox Key Left");
		KeyInput.addInputCommand("Confirm Dialogbox Key Left", key, 0);
	}
	
	/**
	 * Set the scroll right key if the textbox supports it. Note that this is static and will apply to all current and future textboxes.
	 * 
	 * @param key The new key to assign.
	 */
	public static void setTextboxScrolRightKey(int key){
		KeyInput.removeInputCommand("Confirm Dialogbox Key Right");
		KeyInput.addInputCommand("Confirm Dialogbox Key Right", key, 0);
	}
	
	/**
	 * Initializes the textbox and reveals it on the screen.
	 */
	public void start(){
		if(!finished){
			generateTextLines();
			setFocused(true);
			start = true;
		}
	}
	
	/**
	 * Stops the textbox (this can not be resumed).
	 */
	public void stop(){
		if(!finished){
			graphicTextLines.clear();
			finished = true;
			fireTextboxStopped();
		}
	}
	
	/**
	 * Pauses the textbox (can be resumed).
	 */
	public void pause(){
		paused = true;
	}
	
	/**
	 * Resume the textbox if paused.
	 */
	public void resume(){
		paused = false;
	}
	
	/**
	 * Hides textbox but does not pause it.
	 */
	public void hide(){
		hidden = true;
	}
	
	/**
	 * Reveals textbox if not visible.
	 */
	public void show(){
		hidden = false;
	}
	
	/**
	 * Resets the textbox, this allows for it to be started again.
	 */
	public void reset(){
		finished = false;
		paused = false;
		hidden = false;
		start = false;
		curText = 0;
		graphicTextLines.clear();
	}
	
	/**
	 * Set if the textbox accepts key input.
	 * 
	 * @param focused True if accepts input
	 */
	public void setFocused(boolean focused){
		this.focused = focused;
	}
	
	/**
	 * Set the color of the text for the textbox.
	 * 
	 * @param textColor The color
	 */
	public void setTextColor(Color textColor){
		this.textColor = textColor;
	}
	
	/**
	 * Set the color of the header text.
	 * 
	 * @param headerColor The color
	 */
	public void setHeaderColor(Color headerColor){
		this.headerColor = headerColor;
	}
	
	/**
	 * Set the color of the text container.
	 * 
	 * @param containerColor The color
	 */
	public void setTextContainerColor(Color containerColor){
		textContainerColor = new Color(containerColor.getRed(), containerColor.getGreen(), containerColor.getBlue(), 150);
	}
	
	/**
	 * Set the color of the header container.
	 * 
	 * @param headerColor The color
	 */
	public void setHeaderContainerColor(Color headerColor){
		headerColor = new Color(headerColor.getRed(), headerColor.getGreen(), headerColor.getBlue(), 150);
	}
	
	/**
	 * Tells if x and y position are kept in sync with camera (The textbox will not move from it's current spot on the screen).
	 * 
	 * @param useCameraCoordinates True if use camera coordinates
	 */
	public void setUseCameraCoordinates(boolean useCameraCoordinates){
		this.useCameraCoordinates = useCameraCoordinates;
	}
	
	/**
	 * Get if textbox is focused.
	 * 
	 * @return True if focused
	 */
	public boolean isFocused(){
		return focused;
	}
	
	/**
	 * Get the color of the text.
	 * 
	 * @return Color of the text
	 */
	public Color getTextColor(){
		return textColor;
	}
	
	/**
	 * Get the color of the text container.
	 * 
	 * @return Color of the text container
	 */
	public Color getTextContainerColor(){
		return textContainerColor;
	}
	
	/**
	 * Get if textbox is using camera coordinates.
	 * 
	 * @return True if using camera coordinates.
	 */
	public boolean isUsingCameraCoordinates(){
		return useCameraCoordinates;
	}
	
	/**
	 * Get if textbox has started.
	 * 
	 * @return True if started
	 */
	public boolean hasStarted(){
		return start;
	}
	
	/**
	 * Get if the textbox has finished.
	 * 
	 * @return True if finished
	 */
	public boolean hasFinished(){
		return finished;
	}
	
	/**
	 * Get if the textbox is paused.
	 * 
	 * @return True if paused
	 */
	public boolean isPaused(){
		return paused;
	}
	
	/**
	 * Get if the textbox is hidden.
	 * 
	 * @return True if hidden
	 */
	public boolean isHidden(){
		return hidden;
	}
}
