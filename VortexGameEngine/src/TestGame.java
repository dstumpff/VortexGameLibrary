import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javazoom.jl.decoder.JavaLayerException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import vortex.Game;
import vortex.GameDriver;
import vortex.GameEntity;
import vortex.Textbox;
import vortex.exception.TextboxException;
import vortex.gameentity.Camera;
import vortex.gameentity.Map;
import vortex.gameentity.Player;
import vortex.gameentity.map.tilemap.BasicTileMap;
import vortex.input.KeyInput;
import vortex.input.MouseInput;
import vortex.sound.*;
import vortex.textbox.BasicDialogbox;
import vortex.textbox.Confirmbox;
import vortex.textbox.ConfirmboxEvent;
import vortex.textbox.ConfirmboxListener;
import vortex.textbox.TextboxEvent;
import vortex.textbox.TextboxListener;
import vortex.utilities.ResourceLoader;
import static vortex.sound.BackgroundMusic.*;
import vortex.algorithm.pathfinding.*;

public class TestGame extends GameDriver{

	ArrayList<GameEntity> gameEntities = new ArrayList<GameEntity>();
	
	Map theTileMap;
	TestPlayer player1;
	Player player2;
	PathFinding pathFinder;
	Player player3;
	Camera pawnCamera;
	public static boolean dialogMode = false;
	Game game;
	boolean[][] tileMapPathing = {{true, true, true, false, false, false, false, false, false, true, false, false, true},
								  {false, true, true, false, false, false, false, false, false, true, false, false, true },
								  {false, false, true, false, false, false, false, false, false, true, false, false, true},
								  {false, false, false, false, false, false, false, false, false, true, true, true, true},
								  {false, false, false, false, false, false, false, false, false, false, false, true, true},
								  {false, false, false, false, false, false, false, false, false, false, false, false, false},
								  {false, false, true, true, true, false, false, false, false, false, false, false, false},
								  {true, true, true, true, true, false, false, false, false, false, false, false, false},
								  {true, true, true, true, false, false, false, false, false, false, true, true, true},
								  {true, true, true, true, false, false, false, false, false, false, true, true, true}};

	ArrayList<Rectangle> quadtreeNodes = new ArrayList<Rectangle>();
	Confirmbox testBox;
	BasicDialogbox  testBox2;
	
	public static void main(String[] args){
		gameDriver = new TestGame();
		Game.initGame("Test Game");
		Game.setGameWindowSize(650, 500);
		Game.getAppGameContainer().setShowFPS(true);
		//Game.getAppGameContainer().setVSync(true);
		Game.getAppGameContainer().setTargetFrameRate(60);
		Game.setChild(gameDriver);
		Game.start();
	}
	
	@Override
	public void init(GameContainer gc) {
		
		try {
			Scanner test = ResourceLoader.openFile("Test");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			ResourceLoader.createTextbox("test");
		} catch (TextboxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try{
			theTileMap = new BasicTileMap("res/testMap.tmx");
			//((BasicTileMap)(theTileMap)).setPathing(tileMapPathing);
			theTileMap.useDefaultCamera();
			System.out.println("LEFT: " + theTileMap.getLeftBound() + " RIGHT: " + theTileMap.getRightBound() + " UPPER: " + theTileMap.getUpperBound() + " LOWER: " + theTileMap.getLowerBound());
			theTileMap.setViewCollision(true);
			Game.setGameMap(theTileMap);
		}catch(SlickException e){
			e.printStackTrace();
		}
		player1 = new TestPlayer(100,200,50,50);
		player1.setSpeed(5f);
		player1.setShape("Rectangle");
		player1.setMovableCollision(true);
		player1.setViewCollision(false);
		pawnCamera = new Camera(player1, 100, 50);
		player1.attachCamera(pawnCamera);
		
		//player1.getCamera().syncVerticalMovement(false);
		
		player2 = new Player(300, 400, 50, 50);
		player2.setShape("Rectangle");
		player2.setMovableCollision(false);
		player2.useDefaultCamera();
		
		//player3 = new Player(350, 600, 100, 100);
		//player3.useDefaultCamera();
		Game.setGameCamera(player1.getCamera());
		testBox2 = new BasicDialogbox(150, 100, 300, 300);
		testBox2.load("testDialog.txt");
		testBox2.setUseCameraCoordinates(true);
		testBox2.addTextboxListener(new TextboxListener(){

			@Override
			public void textboxStopped(TextboxEvent e) {
				testBox.stop();
			}
			
		});
			
		//pathFinder.run
		testBox = new Confirmbox(75, 300, 500, 200);
		testBox.load("testConfirmDialog.txt");
		testBox.setColumns(2);
		testBox.drawColumnLines(true);
		Textbox.setTextboxScrollUpKey(Input.KEY_W);
		Textbox.setTextboxScrollDownKey(Input.KEY_S);
		Textbox.setTextboxScrollLeftKey(Input.KEY_A);
		Textbox.setTextboxScrolRightKey(Input.KEY_D);
		testBox.pack();
		testBox.setUseCameraCoordinates(true);
		Textbox.setTextboxActionKey(Input.KEY_SPACE);
		testBox.addConfirmboxListener(new ConfirmboxListener(){

			@Override
			public void textboxStopped(TextboxEvent e) {
				
			}

			@Override
			public void selectionCommandPerformed(ConfirmboxEvent e) {
				System.out.println(e.getSelectionCommand());
				Textbox.setTextboxActionKey(Input.KEY_ENTER);
				testBox.pause();
				testBox.setSelectionBoxActive(false);
				testBox.setFocused(false);
				testBox2.setFocused(true);
				if(!testBox2.hasStarted())
					testBox2.start();
				else{
					testBox2.resume();
					testBox2.show();
				}
			}
			
			public void scrollCommandPerformed(ConfirmboxEvent e){
				System.out.println(e.getScrollCommand());
				System.out.println(e.getSelectionCommand());
			}
			
		});
		
		gameEntities.add(theTileMap);
		gameEntities.add(player1);
		gameEntities.add(player2);
		//gameEntities.add(player3);
		gameEntities.add(testBox);
		gameEntities.add(testBox2);
		
		KeyInput.addInputCommand("Move+X", Input.KEY_RIGHT, 0);
		KeyInput.addInputCommand("Move-X", Input.KEY_LEFT, 0);
		KeyInput.addInputCommand("Move+Y", Input.KEY_DOWN, 0);
		KeyInput.addInputCommand("Move-Y", Input.KEY_UP, 0);
		KeyInput.addInputCommand("PauseTextbox", Input.KEY_P, 0);
		KeyInput.addInputCommand("ResumeTextbox", Input.KEY_R, 0);
		KeyInput.addInputCommand("StopTextbox", Input.KEY_ESCAPE, 0);
		KeyInput.addInputCommand("Start Dialog", Input.KEY_ENTER, 0);
		
		MouseInput.addMouseInputCommand("Attack", Input.MOUSE_LEFT_BUTTON, 0);
		
		BackgroundMusic.addSongToQueue("01.-title.mp3");
		BackgroundMusic.addSongToQueue("04.-speeder-bikes.mp3");
		BackgroundMusic.addSongToQueue("01-hell-march-2.mp3");
		
		try{
			BackgroundMusic.playAllInQueue();
		}catch(JavaLayerException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(GameContainer gc, int i) {
		
		//player2.setVX(2f);
		for(int j = 0; j < gameEntities.size(); j++){
			gameEntities.get(j).update(gc, i);
		}
		
		/*if(KeyInput.get("PauseTextbox").isPressed()){
			
			if(Game.getGameCamera() == player1.getCamera()){
				Game.panToCamera(theTileMap.getCamera(), 10);
			}
			else if(Game.getGameCamera() == theTileMap.getCamera()){
				Game.panToCamera(player1.getCamera(), 10);
			}
			KeyInput.get("PauseTextbox").setPressed(false);
		}
		
		if(KeyInput.get("PauseTextbox").isPressed()){
			testBox.pause();
			testBox.hide();
		}
		if(KeyInput.get("ResumeTextbox").isPressed()){
			testBox.show();
			testBox.resume();
		}
		if(KeyInput.get("StopTextbox").isPressed()){
			testBox.stop();
			dialogMode = false;
		}
		if(KeyInput.get("Start Dialog").isPressed()){
			if(!testBox.hasStarted() && !testBox2.hasStarted()){
				System.out.println("HERE");
				testBox.start();
				testBox.setFocused(true);
				dialogMode = true;
			}
		}*/
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		for(int i = 0; i < gameEntities.size(); i++){
			if(gameEntities.get(i).isVisible()){
				gameEntities.get(i).render(gc, g);
			}
		}
	}
}
