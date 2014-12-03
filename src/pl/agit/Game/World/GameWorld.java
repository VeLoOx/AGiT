package pl.agit.Game.World;

import pl.agit.Game.Sprites.Sprite;
import pl.agit.Game.Sprites.SpriteManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

	/** klatki na sekunde */
	private final int framesPerSecond;

	/** tytul okna gry */
	private final String windowTitle;

	private final SpriteManager spriteManager = new SpriteManager();

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
				checkCollisions();
				
				updateSprites();

				cleanupSprites();

				respawnElements();
			}

		}); // oneFrame
			// sets the game world's game loop (Timeline)

		setGameLoop(TimelineBuilder.create().cycleCount(Animation.INDEFINITE)
				.keyFrames(oneFrame).build());

	}

	public abstract void initialize(final Stage primaryStage);

	
	public void beginGameLoop() {
		getGameLoop().play();
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
					//return;
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
		//System.out.println("POBRANO");
		return sceneElements;
	}

	public void setSceneElements(Group sceneElements) {
		this.sceneElements = sceneElements;
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
