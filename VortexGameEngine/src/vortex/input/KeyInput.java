package vortex.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;


/**
 * Handles registering commands for keyboard input. Assigning commands will allow you to organize what key input does.
 * 
 * @author Daniel Stumpff
 *
 */
public class KeyInput {
	
	/**
	 * A HashMap of all the registered commands.
	 */
	private static HashMap<Integer, LinkedList<KeyInputCommand>> commands = new HashMap<Integer, LinkedList<KeyInputCommand>>();
	/**
	 * A HashMap that stores all commands and their associated key. Used for looking up with command name, instead of key.
	 */
	private static HashMap<String, Integer> nameToKey = new HashMap<String, Integer>();
	
	/**
	 * Add a new key command;
	 * 
	 * @param name The name of the command
	 * @param key The key associated with the command
	 * @param value The value of the command
	 */
	public static boolean addInputCommand(String name, int key, float value){
		if(!nameToKey.containsKey(name)){
			if(commands.containsKey(key)){
				LinkedList<KeyInputCommand> list = commands.get(key);
				list.add(new KeyInputCommand(name, value));
				System.out.println("Inserting: " + name + " into the map.");
			}
			else{
				LinkedList<KeyInputCommand> newList = new LinkedList<KeyInputCommand>();
				newList.add(new KeyInputCommand(name, value));
				commands.put(key, newList);
				System.out.println("Inserting: " + name + " into the map.");
			}
			
			nameToKey.put(name, key);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Remove a key command
	 * 
	 * @param name The name of the command to remove
	 */
	public static void removeInputCommand(String name){
		if(nameToKey.containsKey(name)){
			int key = nameToKey.get(name);
			
			LinkedList<KeyInputCommand> list = commands.get(key);
			
			if(list != null){
				Iterator<KeyInputCommand> it = list.iterator();
				while(it.hasNext()){
					KeyInputCommand command = it.next();
					if(command.getName().equals(name)){
						list.remove(command);
						break;
					}
				}
			}
		}
	}
	
	public static KeyInputCommand get(String name){
		if(nameToKey.containsKey(name)){
			int key = nameToKey.get(name);
			
			LinkedList<KeyInputCommand> list = commands.get(key);
			
			if(list != null){
				Iterator<KeyInputCommand> it = list.iterator();
				while(it.hasNext()){
					KeyInputCommand command = it.next();
					if(command.getName().equals(name)){
						return command;
					}
				}
			}
		}
		return null;
	}
	
	public static boolean keyCommandExists(String name){
		if(nameToKey.get(name) == null){
			return false;
		}
		
		return true;
	}
	
	/**
	 * Polls a key on press and checks if a command is registered to it.
	 * 
	 * @param key The integer value of the key pressed
	 * @param c The character value of the key pressed
	 */
	public void keyPressed(int key, char c){
		LinkedList<KeyInputCommand> list = commands.get(key);
		
		if(list != null){
			Iterator<KeyInputCommand> it = list.iterator();
			while(it.hasNext()){
				KeyInputCommand command = it.next();
				command.setKeyDown(true);
				command.setKeyHeld(true);
			}
		}
	}
	
	/**
	 * Polls a key on release and checks if a command is registered to it.
	 * 
	 * @param key The integer value of the key released
	 * @param c The character value of the key released
	 */
	public void keyReleased(int key, char c){
		
		LinkedList<KeyInputCommand> list = commands.get(key);
		
		if(list != null){
			Iterator<KeyInputCommand> it = list.iterator();
			while(it.hasNext()){
				KeyInputCommand command = it.next();
				command.setKeyUp(true);
			}
		}
	}
}
