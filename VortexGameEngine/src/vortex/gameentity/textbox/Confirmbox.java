package vortex.gameentity.textbox;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.RoundedRectangle;

import vortex.Game;
import vortex.event.ConfirmboxEvent;
import vortex.event.ConfirmboxListener;
import vortex.event.TextboxListener;
import vortex.gameentity.Textbox;
import vortex.input.KeyInput;
import vortex.utilities.GraphicText;

/**
 * A Textbox specifically designed for handling choices (ie. Yes, no, attack, hide).
 * 
 * @author Daniel Stumpff
 *
 */
public class Confirmbox extends Textbox{
	/**
	 * The selection confirmed on key press.
	 */
	protected int selectionCommand = 0;
	/**
	 * The box for highlighting the current selection.
	 */
	protected RoundedRectangle selectionBox;
	/**
	 * True if the selectionBox (The box that shows the highlighted command) is shown.
	 */
	protected boolean selectionBoxActive = true;
	/**
	 * True if drawing column lines.
	 */
	protected boolean drawColumnLines = false;
	/**
	 * The color for the highlight box.
	 */
	protected Color selectionBoxColor = new Color(Color.white.getRed(), Color.white.getGreen(), Color.white.getBlue(), 50);
	/**
	 * The current command selected.
	 */
	protected int curSelection = 0;
	/**
	 * The number of columns for the box (default = 1)
	 */
	protected int cols = 1;
	/**
	 * The width of each column (default = width of longest string)
	 */
	protected float columnWidth;
	
	/**
	 * The constructor
	 * 
	 * @param x The x position
	 * @param y The y position
	 * @param width The width
	 * @param height The height
	 */
	public Confirmbox(float x, float y, float width, float height) {
		super(x, y, width, height);
		selectionBox = new RoundedRectangle(x, y, width - margins[LEFT_MARGIN] - margins[RIGHT_MARGIN], textFont.getHeight(), 5f);
		columnWidth = selectionBox.getWidth();
		KeyInput.addInputCommand("Confirm Dialogbox Key Up", Input.KEY_UP, 0);
		KeyInput.addInputCommand("Confirm Dialogbox Key Down", Input.KEY_DOWN, 0);
		KeyInput.addInputCommand("Confirm Dialogbox Key Left", Input.KEY_LEFT, 0);
		KeyInput.addInputCommand("Confirm Dialogbox Key Right", Input.KEY_RIGHT, 0);
	}
	
	@Override
	public void init(GameContainer gc) {
		//Do nothing
	}
	
	public void update(GameContainer gc, int i){
		if(start && !paused  && !finished){
			if(focused){
				selectionBox.setX(graphicTextLines.get(curSelection).getX());
				selectionBox.setY(graphicTextLines.get(curSelection).getY());
				
				if(KeyInput.get("Confirm Dialogbox Key Up").getKeyDown()){
					if(curSelection - cols >= 0){
						curSelection -= cols;
						fireScrollCommandPerformed(ConfirmboxEvent.SCROLL_UP);
					}
					//KeyInput.get("Confirm Dialogbox Key Up").setPressed(false);
				}
				else if(KeyInput.get("Confirm Dialogbox Key Down").getKeyDown()){
					if(curSelection + cols < graphicTextLines.size()){
						curSelection += cols;
						fireScrollCommandPerformed(ConfirmboxEvent.SCROLL_DOWN);
					}
					//KeyInput.get("Confirm Dialogbox Key Down").setPressed(false);
				}
				else if(KeyInput.get("Confirm Dialogbox Key Right").getKeyDown()){
					if(curSelection + 1 < graphicTextLines.size() && curSelection % cols != cols - 1){
						curSelection++;
						fireScrollCommandPerformed(ConfirmboxEvent.SCROLL_RIGHT);
					}
					//KeyInput.get("Confirm Dialogbox Key Right").setPressed(false);
				}
				else if(KeyInput.get("Confirm Dialogbox Key Left").getKeyDown()){
					if(curSelection - 1 >= 0 && curSelection % cols != 0){
						curSelection--;
						fireScrollCommandPerformed(ConfirmboxEvent.SCROLL_LEFT);
					}
					//KeyInput.get("Confirm Dialogbox Key Left").setPressed(false);
				}
				if(KeyInput.get("Run Textbox Action").getKeyDown()){
					selectionCommand = curSelection;
					//KeyInput.setKeyUnpressed(Input.KEY_ENTER);
					fireSelectionCommandPerformed();
					//KeyInputget("Run Textbox Action").setPressed(false);
				}
			}
		}
		
		super.update(gc, i);
	}
	
	public void render(GameContainer gc, Graphics g){
		if(useCameraCoordinates)
			g.translate(-Game.getGraphicsTranslationX(), -Game.getGraphicsTranslationY());
		
		if(start && !hidden && !finished){
			Font defaultFont = g.getFont();
			g.setFont(textFont);
			g.setColor(textContainerColor);
			g.fill(textContainer);
			
			g.setColor(textColor);
			for(int i = 0; i < graphicTextLines.size(); i++){
				GraphicText temp = graphicTextLines.get(i);
				g.drawString(temp.getText(), temp.getX() + 10, temp.getY());
			}
			
			if(selectionBoxActive){
				g.setColor(selectionBoxColor);
				g.fill(selectionBox);
			}
			
			if(drawColumnLines){
				for(int i = 0; i < cols - 1; i++){
					g.setColor(Color.black);
					g.drawLine(textContainer.getX() +  margins[LEFT_MARGIN] + (selectionBox.getWidth() * (i + 1)), textContainer.getY(), 
							   textContainer.getX() +  margins[LEFT_MARGIN] + (selectionBox.getWidth() * (i + 1)), textContainer.getY() + textContainer.getHeight() - 2);
				}
			}
			
			if(hasHeader){
				g.setColor(headerContainerColor);
				g.fill(headerContainer);
				g.setColor(headerColor);
				g.drawString(header.get(curText), headerContainer.getX() + margins[LEFT_MARGIN], headerContainer.getY() + (headerContainer.getHeight() / 2) - (textFont.getHeight(header.get(curText)) / 2));
			}
			g.setFont(defaultFont);
		}
		
		if(useCameraCoordinates)
			g.translate(Game.getGraphicsTranslationX(), Game.getGraphicsTranslationY());
		
		super.render(gc, g);
	}

	@Override
	public boolean load(String path) {
		try{
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream is = loader.getResourceAsStream(path);
			Scanner in = new Scanner(is);
			if(in.hasNextLine()){
				if(in.nextLine().equals("1")){
					hasHeader = true;
					header.add(in.nextLine());
				}
			}
			while(in.hasNextLine()){
				text.add(in.nextLine());
			}
			in.close();
		}catch(Exception e){
			return false;
		}
		return true;
	}

	@Override
	protected void generateTextLines() {
		float x = getX() + margins[LEFT_MARGIN];
		float y = getY() + margins[TOP_MARGIN];
		for(int i = 1; i <= text.size(); i++){
			graphicTextLines.add(new GraphicText(x, y, text.get(i-1)));
			x += columnWidth;
			if(i % cols == 0){
				y += textFont.getHeight();
				x = getX() + margins[LEFT_MARGIN];
			}
		}
	}
	
	/**
	 * Add a ConfirmboxListener to the textbox.
	 * 
	 * @param listener The listener to add
	 */
	public synchronized void addConfirmboxListener(ConfirmboxListener listener){
		listeners.add(listener);
	}
	
	/**
	 * Remove a ConfirmboxListener from the textbox.
	 * 
	 * @param listener The listener to remove
	 */
	public synchronized void removeConfirmboxListener(ConfirmboxListener listener){
		listeners.remove(listener);
	}
	
	/**
	 * Fires when a selection command is performed.
	 */
	protected synchronized void fireSelectionCommandPerformed(){
		ConfirmboxEvent event = new ConfirmboxEvent(this, selectionCommand, ConfirmboxEvent.NULL_COMMAND);
		Iterator<TextboxListener> iterator = listeners.iterator();
		
		while(iterator.hasNext()){
			((ConfirmboxListener) iterator.next()).selectionCommandPerformed(event);
		}
	}
	
	/**
	 * Fires when a scroll command is performed.
	 * 
	 * @param dir The scroll direction
	 */
	protected synchronized void fireScrollCommandPerformed(int dir){
		ConfirmboxEvent event = new ConfirmboxEvent(this, selectionCommand, dir);
		Iterator<TextboxListener> iterator = listeners.iterator();
		
		while(iterator.hasNext()){
			((ConfirmboxListener) iterator.next()).scrollCommandPerformed(event);
		}
	}
	
	/**
	 * Fits the size of the box to the info contained.
	 */
	public void pack(){
		float containerWidth = 0, containerHeight = 0;
		containerHeight = textFont.getHeight() * (((text.size() - 1) / cols) + 1) + margins[TOP_MARGIN] + margins[BOTTOM_MARGIN];
		containerWidth = columnWidth * cols + margins[LEFT_MARGIN] + margins[RIGHT_MARGIN];
		textContainer.setWidth(containerWidth);
		textContainer.setHeight(containerHeight);
	}
	
	/**
	 * Set the color of the selection box.
	 * 
	 * @param selectionBoxColor The color
	 */
	public void setSelectionBoxColor(Color selectionBoxColor){
		this.selectionBoxColor = new Color(selectionBoxColor.getRed(), selectionBoxColor.getGreen(), selectionBoxColor.getBlue(), 50);
	}
	
	/**
	 * Set the current selection of the textbox.
	 * 
	 * @param curSelection The new selection index
	 * 
	 * @return True if successfully set the current selection
	 */
	public boolean setCurrentSelection(int curSelection){
		if(curSelection >= 0 && curSelection < text.size()){
			this.curSelection = curSelection;
			return true;
		}
		return false;
	}
	
	/**
	 * Set if the selectionbox is drawn.
	 * 
	 * @param selectionBoxActive True if drawn
	 */
	public void setSelectionBoxActive(boolean selectionBoxActive){
		this.selectionBoxActive = selectionBoxActive;
	}
	
	/**
	 * Set the columns for the textbox (default = 1).
	 * 
	 * @param cols The new number of columns
	 */
	public void setColumns(int cols){
		computeColumnWidth();
		this.cols = cols;
	}
	
	/**
	 * Set the column width manually.
	 * 
	 * @param width The width
	 */
	public void setColumnWidth(float width){
		columnWidth = width;
	}
	
	/**
	 * Computes the column width based off the largest string.
	 */
	public void computeColumnWidth(){
		columnWidth = 0;
		for(int i = 0; i < text.size(); i++){
			if(columnWidth < textFont.getWidth(text.get(i))){
				columnWidth = textFont.getWidth(text.get(i));
			}
		}
		
		columnWidth += 20;
		selectionBox.setWidth(columnWidth);
	}
	
	/**
	 * Set if column lines are drawn.
	 * 
	 * @param draw If column lines are drawn
	 */
	public void drawColumnLines(boolean draw){
		drawColumnLines = draw;
	}
	
	/**
	 * Get if the selection box is being drawn.
	 * 
	 * @return True if being drawn
	 */
	public boolean isSelectionBoxActive(){
		return selectionBoxActive;
	}
	
	/**
	 * Get the current selection.
	 * 
	 * @return The current selection
	 */
	public int getCurrentSelection(){
		return curSelection;
	}
	
	/**
	 * Get the number of columns in the textbox.
	 * 
	 * @return The number of columns
	 */
	public int getColumns(){
		return cols;
	}
	
	/**
	 * Get if column lines are drawn.
	 * 
	 * @return True if column lines are drawn
	 */
	public boolean areColumnLinesDrawn(){
		return drawColumnLines;
	}
}