package vortex.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * Handles registering commands for mouse input. Assigning commands will allow you to organize what mouse button does.
 * 
 * @author Daniel Stumpff
 *
 */
public class MouseInput {
	/**
	 * A HashMap of all the registered commands.
	 */
	private static HashMap<Integer, LinkedList<MouseInputCommand>> commands = new HashMap<Integer, LinkedList<MouseInputCommand>>();
	/**
	 * A HashMap that stores all commands and their associated button. Used for looking up with command name, instead of button.
	 */
	private static HashMap<String, Integer> nameToButton = new HashMap<String, Integer>();;
	
	/**
	 * Add a new mouse command to the HashMap.
	 * 
	 * @param name The name of the command
	 * @param button The button associated with the command
	 * @param value The value of the command
	 */
	public static boolean addMouseInputCommand(String name, int button, float value){
		if(!nameToButton.containsKey(name)){
			if(commands.containsKey(button)){
				LinkedList<MouseInputCommand> list = commands.get(button);
				list.add(new MouseInputCommand(name, value));
			}
			else{
				LinkedList<MouseInputCommand> newList = new LinkedList<MouseInputCommand>();
				newList.add(new MouseInputCommand(name, value));
				commands.put(button, newList);
			}
			
			nameToButton.put(name, button);
			return true;
		}
		
		return false;
	}
	
	public static void removeMouseInputCommand(String name){
		if(nameToButton.containsKey(name)){
			int button = nameToButton.get(name);
		
		
			LinkedList<MouseInputCommand> list = commands.get(button);
			
			if(list != null){
				Iterator<MouseInputCommand> it = list.iterator();
				while(it.hasNext()){
					MouseInputCommand command = it.next();
					if(command.getName().equals(name)){
						list.remove(command);
						break;
					}
				}
			}
		}
	}

	public static MouseInputCommand get(String name){
		if(nameToButton.containsKey(name)){
			int button = nameToButton.get(name);
			
			LinkedList<MouseInputCommand> list = commands.get(button);
			
			if(list != null){
				Iterator<MouseInputCommand> it = list.iterator();
				while(it.hasNext()){
					MouseInputCommand command = it.next();
					if(command.getName().equals(name)){
						return command;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Polls a button on press and checks if a command is registered to it.
	 * 
	 * @param button The mouse button pressed
	 * @param x The x position pressed at
	 * @param y The y position pressed at
	 */
	public void mousePressed(int button, int x, int y){
		LinkedList<MouseInputCommand> list = commands.get(button);
		
		if(list != null){
			Iterator<MouseInputCommand> it = list.iterator();
			while(it.hasNext()){
				MouseInputCommand command = it.next();
				command.setButtonDown(true);
				command.setButtonHeld(true);
				command.setX(x);
				command.setY(y);
			}
		}
	}
	
	/**
	 * Polls a button on release and checks if a command is registered to it.
	 * 
	 * @param button The mouse button released
	 * @param x The x position released at
	 * @param y The y position released at
	 */
	public void mouseReleased(int button, int x, int y){
		LinkedList<MouseInputCommand> list = commands.get(button);
		
		if(list != null){
			Iterator<MouseInputCommand> it = list.iterator();
			while(it.hasNext()){
				MouseInputCommand command = it.next();
				command.setButtonUp(true);
				command.setX(x);
				command.setY(y);
			}
		}
	}
}
