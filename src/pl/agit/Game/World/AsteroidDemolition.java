package pl.agit.Game.World;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.script.ScriptException;

import com.sun.scenario.effect.impl.prism.PrImage;

import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import pl.agit.Game.Gamedef.GameConst;
import pl.agit.Game.Scripts.ScriptManager;
import pl.agit.Game.Sound.SoundManager;
import pl.agit.Game.Sprites.Sprite;
import pl.agit.Game.Sprites.SpriteManager;
import pl.agit.Game.Sprites.Characters.Asteroid;
import pl.agit.Game.Sprites.Characters.Missile;
import pl.agit.Game.Sprites.Characters.SpaceShip;
import pl.agit.Game.World.GUIElements.GameStats;

public class AsteroidDemolition extends GameWorld {

	private GameStats gameStats;

	private int asteroidCount = 0;
	private int missileCount = 0;
	Image backgroundImage;
	ImageView backView;
	Group g = new Group();

	private SpaceShip ship = new SpaceShip();

	SoundManager sm = SoundManager.getSoundManager(1);
	ScriptManager scrm = ScriptManager.getScriptManager();

	public AsteroidDemolition(int fps, String title) {
		super(fps, title);

		// GRAFIKA
		String dir = new File("").getAbsolutePath(); // znalezienie sciaki  bezwzglednej do  projektu

		URL u = null;
		try {
			u = new File(dir + GameConst.GFX_MAINSPACE).toURI().toURL();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		backgroundImage = new Image(u.toExternalForm());
		backView = new ImageView(backgroundImage);
		
		
		//SKRYPTY
		try {
			scrm.addScript(GameConst.JS_ASTEROID_DEMOLITION_NAME, GameConst.JS_ASTEROID_DEMOLITION);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(final Stage primaryStage) {
		// backView.setD
		primaryStage.setTitle(getWindowTitle()); // tytul okna
		primaryStage.setFullScreen(true);
		g.getChildren().add(backView);
		Node node = g;
		node.toBack();
		gameStats = new GameStats(primaryStage.getWidth(), primaryStage.getHeight());

		// tworzenie sceny
		setSceneElements(new Group());
		Scene sc = new Scene(getSceneElements());
		setGameScene(sc);
		
		
		getGameScene().getStylesheets().addAll(GameConst.CSS_PACKAGE_PATH);
		primaryStage.setScene(getGameScene());

		Sprite[] s = { ship };
		getSpriteManager().addSprites(s);
		getSceneElements().getChildren().add(0, node);
		getSceneElements().getChildren().add(ship.node);

		// tworzenie asteroids
		// generateManySpheres(150);
		generateAsteroids();

		// Display the number of spheres visible.
		// Create a button to add more spheres.
		// Create a button to freeze the game loop.
		final Timeline gameLoop = getGameLoop();

		getSceneElements().getChildren().add(gameStats.getStats());

		setupInput(primaryStage);

	}

	public void addScore(int val){
		ship.addScore(val);
	}
 	public void reductionMissile() {
		missileCount--;
	}

	private void setupInput(final Stage primaryStage) {
		
		//okno
		primaryStage.setOnCloseRequest(getCloseWindowEv()); //zamkniecie krzyzykiem
		
		//mysz
		primaryStage.getScene().setOnMouseMoved(getMoveMouseEv()); //poruszanie statkiem poprzez ruch kursora
		primaryStage.getScene().setOnMousePressed(getMoveOrFire()); //strzelanie lewym
		primaryStage.getScene().setOnMouseDragged(getMovedragOrFireMouseEv()); //strzelanie padczas poruszania
		primaryStage.getScene().setOnKeyPressed(getKeyboardEv(primaryStage));
	}
	
	
	//zdarzenia sterowania
	
	private EventHandler<MouseEvent> getMoveOrFire(){
		//strzelanie lewym przyciskiem myszy
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY) {
					Missile m = ship.fire();
					Sprite[] s = { m };
					getSpriteManager().addSprites(s);
					// dodanie pocisku do listy spritow
					getSceneElements().getChildren().add(m.node);
					missileCount++;
				}
			}
		};
		
	}

	private EventHandler<MouseEvent> getMoveMouseEv(){
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ship.setMpozX(event.getX());
				ship.setMpozY(event.getY());
				gameStats.updateMousePos(event.getX(), event.getY());
			}
		};
	}
	
	private EventHandler<MouseEvent> getMovedragOrFireMouseEv(){
		//strzelanie podczas poruszania (strzelanie nie zaimplementowane)
		return  new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ship.setMpozX(event.getX());
				ship.setMpozY(event.getY());
				gameStats.updateMousePos(event.getX(), event.getY());
			}
		};
	}
	
	private EventHandler<WindowEvent> getCloseWindowEv(){
		//zamkniecie okna krzyzykiem
		return new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				
				sm.shutdown();
				System.out.println("Stage is closing");
			}
		};
	}
	
	private EventHandler<KeyEvent> getKeyboardEv(final Stage pm){
		return new EventHandler<KeyEvent>(){
			public void handle(KeyEvent ke){
				if(ke.getCode()==KeyCode.ESCAPE){
					sm.shutdown();
					System.out.println("Stage is closing");
					pm.close();
				}
				
				if(ke.getCode()==KeyCode.TAB){
					//boolean vis = gameStats.getStats().isVisible();
					if(gameStats.getVBox().isVisible()) gameStats.getVBox().setVisible(false); else gameStats.getVBox().setVisible(true); 
					
				}
			}
		};
	}
	
	// generowanie asteroid
	private void generateAsteroids() {
		Random random = new Random();
		int anumb = random.nextInt(9) + 1;
		Scene scene = getGameScene();
		Double o = scene.getWidth(); 
		for (int i = 0; i < anumb; i++) {
						
			Asteroid ast=null;
			try {
				ast = (Asteroid) scrm.getScript(GameConst.JS_ASTEROID_DEMOLITION_NAME).invokeFunction("generateAsteroid1", o);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ScriptException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Sprite[] s = { ast };
			getSpriteManager().addSprites(s);
			ast.node.toFront();
			getSceneElements().getChildren().add(ast.node);
		}

	}

	
	// uaktualnianie aktorow
	@Override
	protected void handleUpdate(Sprite sprite) {

		if (sprite instanceof Asteroid) {
			Asteroid sphere = (Asteroid) sprite;

			if (sphere.handleBoundsMeet(getGameScene().getWidth(),
					getGameScene().getHeight() - 60))
				((Asteroid) sprite).handleDeath(this);

		}

		if (sprite instanceof Missile) {
			Missile sphere = (Missile) sprite;
			if (sphere.handleBoundsMeet(getGameScene().getWidth(),
					getGameScene().getHeight()))

				((Missile) sprite).handleDeath(this);

		}
		if (sprite instanceof SpaceShip) {
			SpaceShip sphere = (SpaceShip) sprite;
			if (sphere.handleBoundsMeet(getGameScene().getWidth(),
					getGameScene().getHeight() - 60)) {

			}
		}

		sprite.update();
	}

	@Override
	protected void respawnElements() {
		asteroidCount = SpriteManager.getCountAsteroids();
		if (asteroidCount == 0)
			generateAsteroids(); // dodawanie asteroid

	}

	// kolizje dwoch obiektow
	@Override
	protected boolean handleCollision(Sprite spriteA, Sprite spriteB) {
		if (spriteA != spriteB) {

			if (spriteA.collide(spriteB,this)) {
												
				spriteA.handleDeath(this);
				
				if (spriteB != ship) {
					
					spriteB.handleDeath(this);
				}
				if (spriteB == ship) {
					System.out.println("C ");
					ship.reducteEnergy(spriteA.getDamage());
				}
				
				
			}
		}

		return false;
	}

	// usuwanie zdechlych rzeczy z planszy
	@Override
	protected void cleanupSprites() {
		// removes from the scene and backend store
		super.cleanupSprites();

		gameStats.updateAsteroidCounter(asteroidCount);
		gameStats.updateMissileCounter(missileCount);
		gameStats.updateSpaceEnergyCounter(ship.getEnergy());
		gameStats.updateSpaceScoreCounter(ship.getScore());
		gameStats.updateSpaceLevelCounter(ship.getLevel());

	}

	private class Delta {
		double x;
		double y;
	};
	
	
	
	
	//-------nieuzywane
	// nie uzywana
		private void generateManySpheres(int numSpheres) {
			Random rnd = new Random();
			Scene gameSurface = getGameScene();
			for (int i = 0; i < numSpheres; i++) {
				Color c = Color.rgb(rnd.nextInt(255), rnd.nextInt(255),
						rnd.nextInt(255));
				Asteroid b = new Asteroid(
						rnd.nextInt(15) + 5,
						LinearGradient
								.valueOf("linear-gradient(from 0% 0% to 100% 100%, red  0% , blue 30%,  black 100%)"));
				Circle circle = b.getAsCircle();
				// random 0 to 2 + (.0 to 1) * random (1 or -1)
				b.vX = (rnd.nextInt(2) + rnd.nextDouble())
						* (rnd.nextBoolean() ? 1 : -1);
				b.vY = (rnd.nextInt(2) + rnd.nextDouble())
						* (rnd.nextBoolean() ? 1 : -1);

				// random x between 0 to width of scene
				double newX = rnd.nextInt((int) gameSurface.getWidth());

				// check for the right of the width newX is greater than width
				// minus radius times 2(width of sprite)
				if (newX > (gameSurface.getWidth() - (circle.getRadius() * 2))) {
					newX = gameSurface.getWidth() - (circle.getRadius() * 2);
				}

				// check for the bottom of screen the height newY is greater than
				// height
				// minus radius times 2(height of sprite)
				double newY = rnd.nextInt((int) gameSurface.getHeight());
				if (newY > (gameSurface.getHeight() - (circle.getRadius() * 2))) {
					newY = gameSurface.getHeight() - (circle.getRadius() * 2);
				}

				circle.setTranslateX(newX);
				circle.setTranslateY(newY);
				circle.setVisible(true);
				circle.setId(b.toString());

				// add to actors in play (sprite objects)
				Sprite[] s = { b };
				getSpriteManager().addSprites(s);

				// add sprite's
				getSceneElements().getChildren().add(0, b.node);

			}
		}

}
