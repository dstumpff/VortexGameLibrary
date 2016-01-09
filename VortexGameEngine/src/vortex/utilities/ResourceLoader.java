package vortex.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import vortex.exception.TextboxException;
import vortex.gameentity.Textbox;
import vortex.gameentity.map.TileMap;

/**
 * A utility class that allows for easy loading of various types of resources. Such as an Image, File, etc...
 * 
 * @author Daniel Stumpff
 *
 */
public class ResourceLoader {
	
	/**
	 * Loads an image given a path.
	 * 
	 * @param path The path of the image to load (Can be relative or absolute)
	 * @return The Image object created
	 * @throws SlickException
	 */
	public static Image loadImage(String path) throws SlickException{
		return new Image(path);
	}
	
	/**
	 * Loads a file given a path.
	 * 
	 * @param path The path of the file to open (Can be relative or absolute)
	 * @return The Scanner for the file opened
	 * @throws Exception
	 */
	public static Scanner openFile(String path) throws FileNotFoundException{
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		FileInputStream is = (FileInputStream)loader.getResourceAsStream(path);
		if(is == null){
			throw new FileNotFoundException();
		}
		
		Scanner file = new Scanner(is);
		
		return file;
	}
	
	public static Textbox createTextbox(String path) throws TextboxException{
		String ext = "";
		int i = path.lastIndexOf(".");
		i++;
		
		if(i == -1){
			throw new TextboxException("File is not a .tbx file");
		}
		
		ext = path.substring(i, path.length());
		
		if(!ext.equals("txb")){
			throw new TextboxException("File is not a .tbx file");
		}
		
		return null;
	}
	
	public static TileMap loadTileMap(String path){
		return null;
	}
}
