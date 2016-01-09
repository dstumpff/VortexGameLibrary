package vortex.gameentity.textbox;

import java.io.InputStream;
import java.util.Scanner;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import vortex.event.TextboxListener;
import vortex.gameentity.Textbox;
import vortex.input.KeyInput;
import vortex.utilities.GraphicText;
import vortex.Game;

/**
 * A Textbox specifically designed to handle dialog.
 * 
 * @author Daniel Stumpff
 *
 */
public class BasicDialogbox extends Textbox{
	/**
	 * Controls how many lines to fully draw.
	 */
	protected int curDrawnLines = 1;
	/**
	 * Controls how many chars to draw for the current line.
	 */
	protected int curDrawnChars = 0;
	/**
	 * Tells when the text is done drawing.
	 */
	protected boolean finishedDialog = false;
	/**
	 * Tells when all the dialog has completed.
	 */
	protected boolean finishedConversation = false;
	
	/**
	 * The constructor.
	 * 
	 * @param x The x position
	 * @param y The y position
	 * @param width The width
	 * @param height The height
	 */
	public BasicDialogbox(float x, float y, float width, float height) {
		super(x, y, width, height);
	}
	
	@Override
	public void init(GameContainer gc) {
		//Do nothing
	}
	
	@Override
	public void update(GameContainer gc, int i){
		if(start && !paused && !finished){
			if(finishedDialog && curText == text.size() - 1){
				finishedConversation = true;
			}
			if(!finishedDialog){
				if(textTimer >= textSpeed && curDrawnChars < graphicTextLines.get(curDrawnLines - 1).getText().length()){
					curDrawnChars++;
					if(curDrawnChars == graphicTextLines.get(curDrawnLines - 1).getText().length() && curDrawnLines < graphicTextLines.size()){
						curDrawnLines++;
						curDrawnChars = 0;
					}
					textTimer = 0;
				}
				if(curDrawnLines == graphicTextLines.size() && curDrawnChars == graphicTextLines.get(curDrawnLines - 1).getText().length()){
					finishedDialog = true;
				}
				
				textTimer++;
			}
			if(focused){
				if(KeyInput.get("Run Textbox Action").getKeyDown()){
					if(finishedDialog){
						if(curText + 1 < text.size()){
							curText++;
							textTimer = 0;
							curDrawnLines = 1;
							curDrawnChars = 0;
							graphicTextLines.clear();
							generateTextLines();
							finishedDialog = false;
						}
					}
					else if(!finishedDialog){
						curDrawnLines = graphicTextLines.size();
						curDrawnChars = graphicTextLines.get(curDrawnLines - 1).getText().length();
						finishedDialog = true;
					}
					if(finishedConversation){
						stop();
						finished = true;
					}
				}
			}
		}
		
		super.update(gc, i);
	}
	
	@Override
	public void render(GameContainer gc, Graphics g){
		if(useCameraCoordinates)
			g.translate(-Game.getGraphicsTranslationX(), -Game.getGraphicsTranslationY());
		
		if(start && !hidden && !finished){
			
			g.setColor(textContainerColor);
			g.fill(textContainer);
			
			g.setColor(textColor);
			Font defaultFont = g.getFont();
			g.setFont(textFont);
			for(int i = 0; i < graphicTextLines.size(); i++){
				GraphicText temp = graphicTextLines.get(i);
				if(i < curDrawnLines - 1){
					g.drawString(temp.getText(), temp.getX(), temp.getY());
				}
				else if(i == curDrawnLines - 1){
					g.drawString(temp.getText().substring(0, curDrawnChars), temp.getX(), temp.getY());
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
	
	public boolean load(String path){
		try{
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream is = loader.getResourceAsStream(path);
			Scanner in = new Scanner(is);
			if(in.hasNextLine()){
				if(in.nextLine().equals("1")){
					hasHeader = true;
				}
			}
			while(in.hasNextLine()){
				if(hasHeader){
					header.add(in.nextLine());
				}
				text.add(in.nextLine());
			}
			in.close();
		}catch(Exception e){
			return false;
		}
		
		return true;
	}
	
	protected void generateTextLines(){
		String word = "";
		String textLine = "";
		float x = getX() + margins[LEFT_MARGIN];
		float y = getY() + margins[TOP_MARGIN];
		int textWidth = 0;
		for(int i = 0; i < text.get(curText).length(); i++){
			word += text.get(curText).charAt(i);
			if(text.get(curText).charAt(i) == ' '){
				textWidth += textFont.getWidth(word);
				if(textWidth >= textContainer.getWidth() - margins[LEFT_MARGIN] - margins[RIGHT_MARGIN]){
					graphicTextLines.add(new GraphicText(x, y, textLine));
					y += textFont.getHeight();
					textLine = "";
					textWidth = textFont.getWidth(word);
				}
				
				textLine += word;
				word = "";
			}
		}
		
		textWidth += textFont.getWidth(word);
		if(textWidth >= textContainer.getWidth() - margins[LEFT_MARGIN] - margins[RIGHT_MARGIN]){
			graphicTextLines.add(new GraphicText(x, y, textLine));
			y += textFont.getHeight();
			textLine = "";
		}
		
		textLine += word;
		graphicTextLines.add(new GraphicText(x, y, textLine));
	}
	
	/**
	 * Add a TextboxListener to this textbox.
	 * 
	 * @param textboxListener The listener to add
	 */
	public synchronized void addTextboxListener(TextboxListener textboxListener){
		listeners.add(textboxListener);
	}
	
	/**
	 * Remove a TextboxListener from this textbox.
	 * 
	 * @param listener The listener to remove
	 */
	public synchronized void removeTextboxListener(TextboxListener listener){
		listeners.remove(listener);
	}
}
