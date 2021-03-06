package pl.agit.Game.World;

import java.util.ArrayList;

import pl.agit.Game.Sprites.Sprite;
import pl.agit.Game.Sprites.SpriteManager;
import pl.agit.Game.World.GUIElements.MainMenu;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public abstract class GameWorld {
	// ################################# POLA
	/** Scena dla elementow */
	private Scene gameScene;

	/** Wyswietlane elementy gry */
	private Group sceneElements;

	/** linia czasu petli gry */
	private static Timeline gameLoop;
	private boolean pause = false;

	/** klatki na sekunde */
	private final int framesPerSecond;

	/** tytul okna gry */
	private final String windowTitle;

	private final SpriteManager spriteManager = new SpriteManager();

	private MainMenu mm = null;
	
	
	public GameWorld(final int fps, final String title) {

		framesPerSecond = fps;

		windowTitle = title;

		// create and set timeline for the game loop
		
		

		buildAndSetGameLoop();

	}

	// ################################# CZESC GLOWNA
	protected final void buildAndSetGameLoop() {

		final Duration oneFrameAmt = Duration
				.millis(1000 / getFramesPerSecond());

		final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,

		new EventHandler() {

			@Override
			public void handle(javafx.event.Event event) {
				revideGame();
				
				respawnElements();
				
				checkCollisions();

				updateSprites();

				cleanupSprites();
				
				updateBehaviorSprites();
				
				
			}

		}); // oneFrame
			// sets the game world's game loop (Timeline)

		setGameLoop(TimelineBuilder.create().cycleCount(Animation.INDEFINITE)
				.keyFrames(oneFrame).build());

	}

	public abstract void initialize(final Stage primaryStage, MainMenu main);

	public abstract void setupInput(final Stage pm);

	public abstract void cleanWorld();

	public void setMainMenu(MainMenu m) {
		mm = m;
	}

	public void returnToMenu() {
		getGameScene().setCursor(Cursor.DEFAULT);
		getGameScene().cursorProperty().set(Cursor.DEFAULT);
		
		mm.toMenu();
	}
	
	

	public void beginGameLoop() {
		getGameLoop().play();
		pause = false;

	}

	public void pauseGameLoop() {
		getGameLoop().pause();
		pause = true;
	}

	public boolean isPause() {
		return pause;
	}

	protected void updateSprites() {
		for (Object sprite : spriteManager.getAllSprites()) {
			handleUpdate((Sprite) sprite);
		}
	}

	protected void handleUpdate(Sprite sprite) {
	}

	// dodanie elemntow w trakcie gry
	protected void respawnElements() {

	}

	protected void revideGame() {

	}

	protected void updateBehaviorSprites() {

	}

	protected void checkCollisions() {
		// check other sprite's collisions
		spriteManager.resetCollisionsToCheck();
		// check each sprite against other sprite objects.
		for (Object spriteA : spriteManager.getCollisionsToCheck()) {
			for (Object spriteB : spriteManager.getAllSprites()) {
				if (handleCollision((Sprite) spriteA, (Sprite) spriteB)) {
					// The break helps optimize the collisions
					// The break statement means one object only hits another
					// object as opposed to one hitting many objects.
					// To be more accurate comment out the break statement.
					break;
					// return;
				}
			}
		}
	}

	protected boolean handleCollision(Sprite spriteA, Sprite spriteB) {
		return false;
	}

	/**
	 * Sprites to be cleaned up.
	 */
	protected void cleanupSprites() {
		spriteManager.cleanupSprites();
	}

	// ################################## GETTERY i SETTERY
	public Scene getGameScene() {
		return gameScene;
	}

	public void setGameScene(Scene gameScene) {
		this.gameScene = gameScene;
	}

	public Group getSceneElements() {
		// System.out.println("POBRANO");
		return sceneElements;
	}

	public void setSceneElements(Group sceneElements) {
		this.sceneElements = sceneElements;
		sceneElements.setCache(true);
		sceneElements.setCacheHint(CacheHint.SPEED);
	}

	public static Timeline getGameLoop() {
		return gameLoop;
	}

	public static void setGameLoop(Timeline gameLoop) {
		GameWorld.gameLoop = gameLoop;
	}

	public int getFramesPerSecond() {
		return framesPerSecond;
	}

	public String getWindowTitle() {
		return windowTitle;
	}

	public SpriteManager getSpriteManager() {
		return spriteManager;
	}

}
