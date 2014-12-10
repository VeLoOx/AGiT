package pl.agit.Game.World;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.script.ScriptException;

import com.sun.javafx.font.FontConstants;
import com.sun.javafx.runtime.*;
import com.sun.scenario.effect.impl.prism.PrImage;

import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.objects.NativeArray;
import pl.agit.Game.Gamedef.GameConst;
import pl.agit.Game.Image.ImageManager;
import pl.agit.Game.Scripts.ScriptManager;
import pl.agit.Game.Sound.SoundManager;
import pl.agit.Game.Sprites.Sprite;
import pl.agit.Game.Sprites.SpriteManager;
import pl.agit.Game.Sprites.Characters.AlienMissile;
import pl.agit.Game.Sprites.Characters.AlienShip;
import pl.agit.Game.Sprites.Characters.Asteroid;
import pl.agit.Game.Sprites.Characters.BigMissile;
import pl.agit.Game.Sprites.Characters.Missile;
import pl.agit.Game.Sprites.Characters.SpaceShip;
import pl.agit.Game.World.GUIElements.GameStats;
import pl.agit.Game.World.GUIElements.MainMenu;

public class AsteroidDemolition extends GameWorld implements GameConst {

	private ArrayList<Sprite> spriteToRespawnList = new ArrayList<>();
	private ArrayList<Asteroid> asteroids = new ArrayList<>();
	
	// private ArrayList<Node> nodeToRespawnList = new ArrayList<>();

	private GameStats gameStats;
	private SpaceShip ship;

	private int asteroidCount = 0;
	private int missileCount = 0;
	private int alienCount = 0;

	private boolean enableAlienGen = false;
	private boolean enableAsteroidGen = false;

	private boolean ALIEN_PART = false;
	private boolean DEAD_STATE = false;
	private boolean WIN_STATE = false;
	private boolean FIRSTROUND_STATE = true;

	private List<Integer> stageSequenceList = new ArrayList<>();
	private ArrayList<byte[][]> alienMapList = new ArrayList<byte[][]>();
	private int actualAlienMap = -1;

	private int actualAsteroidRound = 0; // aktualna runda asteroid

	// TIMERY
	private long asteroidTimeRound = 5000; // czas trwania calej rundy
	private long lastAsteroidTime = 0; // czas rozpoczecia rundy
	private long timeDead = 0;
	private long asteroidSubTimeGeneration = 1000; // czas regenerowania//
													// asteroid
	private long lastAsteroidSubTimeGeneration = 0; // ostatni czas regenracji
	private long lastRespaw = 0;
	private long parkShipTime = 0;
	private long waitParkTime = 4000;

	Image backgroundImage;
	ImageView backView;
	Group g = new Group();

	SoundManager sm = SoundManager.getSoundManager(1);
	ScriptManager scrm = ScriptManager.getScriptManager();
	ImageManager im = ImageManager.getImageManager();

	private AlienShip aCheck = new AlienShip(100, 100);// probny
	private double imgW = aCheck.getImageWidth();
	private double imgH = aCheck.getImageHeight();

	private int maxCol = 0;
	private int maxRow = 0;

	private int stageIndex = 0;

	public AsteroidDemolition(int fps, String title) {
		super(fps, title);

				
	}

	@Override
	public void initialize(final Stage primaryStage, MainMenu m) {
		final Timeline gameLoop = getGameLoop();
		
		primaryStage.setFullScreenExitKeyCombination(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));

		setMainMenu(m); // odwolanie do glownego menu
		
		Robot r;
		try {
			r = new Robot();
			r.mouseMove(550, 550);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		stageIndex = 0;
		asteroidCount = 0;
		missileCount = 0;
		alienCount = 0;
		
		enableAlienGen = false;
		enableAsteroidGen = false;

		ALIEN_PART = false;
		DEAD_STATE = false;
		WIN_STATE = false;
		FIRSTROUND_STATE = true;
		actualAlienMap = -1;
		actualAsteroidRound = 0;
		
		// tworzenie sceny
		setSceneElements(new Group());
		
		//Scene sc = new Scene(getSceneElements());
		//setGameScene(sc);
		setGameScene(m.getScene());
		getGameScene().setRoot(getSceneElements());
		getGameScene().getStylesheets().addAll(GameConst.CSS_PACKAGE_PATH);
		getGameScene().setCursor(Cursor.NONE);
		//getSceneElements().setCursor(Cursor.NONE);

		// dodanie statystyk
		gameStats = new GameStats(primaryStage.getWidth(),
				primaryStage.getHeight());
		getSceneElements().getChildren().add(gameStats.getStats());
		getSceneElements().getChildren().add(gameStats.getGamePanel());
		getSceneElements().getChildren().add(gameStats.getTimeText());

		// doanie tla
		backView = new ImageView(im.getImage(GFX_MAINSPACE_NAME));
		g.getChildren().add(backView);
		Node node = g;
		node.toBack();
		getSceneElements().getChildren().add(0, node);

		// dodanie statku gracza
		ship = new SpaceShip();
		Sprite[] s = { ship };
		getSpriteManager().addSprites(s);
		getSceneElements().getChildren().add(ship.node);

		try {
			alienMapList = (ArrayList<byte[][]>) scrm.getScript(
					GameConst.JS_ASTEROID_DEMOLITION_NAME).invokeFunction(
					"returnAlienMapList");
			System.out.println(alienMapList.size());
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		// inicjalizacja listy sekwencji map
		try {
			stageSequenceList = (List<Integer>) scrm.getScript(JS_ASTEROID_DEMOLITION_NAME).invokeFunction("gameSequenceList");
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ScriptException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (stageSequenceList.get(stageIndex) == 1)
			ALIEN_PART = true;
		else
			ALIEN_PART = false;
		stageIndex++;

		parkShipTime = System.currentTimeMillis();
		
		
		

	}

	public void addScore(int val) {
		ship.addScore(val);
	}

	private void prepareDeadState() {
		timeDead = System.currentTimeMillis();
		DEAD_STATE = true;
		System.out.println("JESTES MARTWY");

		String text = "YOU ARE DEAD";
		final Text text1 = new Text(600 - (text.length() * 90) / 2, 300, text);
		text1.setFill(Color.RED);
		text1.setFont(Font.font(java.awt.Font.SERIF, 90));

		getSceneElements().getChildren().add(text1);

	}

	private void useDeadState() {
		if (System.currentTimeMillis() - timeDead >= 2000) {
			getGameLoop().pause();
			returnToMenu();
		}
	}

	@Override
	public void setupInput(final Stage primaryStage) {

		/* okno - zamkniecie okna */
		primaryStage.setOnCloseRequest(getCloseWindowEv());

		/* ruch lursora - poruszanie statkiem */
		primaryStage.getScene().setOnMouseMoved(getMoveMouseEv());
		/* strzelanie lewym */
		primaryStage.getScene().setOnMousePressed(getMoveOrFire());
		/* strzelanie podczas poruszania */
		primaryStage.getScene().setOnMouseDragged(getMovedragOrFireMouseEv());
		/* klawisze */
		primaryStage.getScene().setOnKeyPressed(getKeyboardEv(primaryStage));
	}

	public void resetWorld(){
		try{
		getSceneElements().getChildren().clear();
		getSpriteManager().clearAllStuff();
		spriteToRespawnList.clear();
		} catch(Exception e){
			return;
		}
	}
	
	public void cleanWorld() {
		sm.shutdown();

		if (getSceneElements() != null)
			getSceneElements().getChildren().clear();

		getSpriteManager().clearAllStuff();
	}

	// zdarzenia sterowania

	private EventHandler<MouseEvent> getMoveOrFire() {
		// strzelanie lewym przyciskiem myszy
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
				
				if (event.getButton() == MouseButton.SECONDARY) {
					BigMissile m1 = ship.fire2(true);
					BigMissile m2 = ship.fire2(false);
					Sprite[] s = { m1,m2 };
					getSpriteManager().addSprites(s);
					// dodanie pocisku do listy spritow
					getSceneElements().getChildren().add(m1.node);
					getSceneElements().getChildren().add(m2.node);
					missileCount++;
				}
			}
		};

	}

	private EventHandler<MouseEvent> getMoveMouseEv() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				
				ship.setMpozX(event.getX());
				ship.setMpozY(event.getY());
				gameStats.updateMousePos(event.getX(), event.getY());
			}
		};
	}

	private EventHandler<MouseEvent> getMovedragOrFireMouseEv() {
		// strzelanie podczas poruszania (strzelanie nie zaimplementowane)
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ship.setMpozX(event.getX());
				ship.setMpozY(event.getY());
				gameStats.updateMousePos(event.getX(), event.getY());
			}
		};
	}

	private EventHandler<WindowEvent> getCloseWindowEv() {
		// zamkniecie okna krzyzykiem
		return new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				getGameScene().setCursor(Cursor.DEFAULT);
				sm.shutdown();
				System.out.println("Stage is closing");
			}
		};
	}

	private EventHandler<KeyEvent> getKeyboardEv(final Stage pm) {
		return new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				if (ke.getCode() == KeyCode.ESCAPE) {
					getGameScene().setCursor(Cursor.DEFAULT);
					getGameLoop().pause();
					returnToMenu();
					
				}

				if (ke.getCode() == KeyCode.TAB) {
					getGameScene().setCursor(Cursor.DEFAULT);
					// boolean vis = gameStats.getStats().isVisible();
					if (gameStats.getVBox().isVisible())
						gameStats.getVBox().setVisible(false);
					else
						gameStats.getVBox().setVisible(true);

				}

				if (ke.getCode() == KeyCode.SPACE) {
					if (!isPause())
						pauseGameLoop();
					else
						beginGameLoop();
				}
			}
		};
	}

	private void generateAlien() {
		System.out.println("Generate map " + actualAlienMap + " time"
				+ System.currentTimeMillis());
		// zaladowanie mapy
		byte[][] alienMap1 = alienMapList.get(actualAlienMap);

		maxRow = (int) (getGameScene().getHeight() / imgH) - 5;
		maxCol = (int) (getGameScene().getWidth() / imgW) - 1;

		int startPozX = (int) ((int) (((maxCol - 10) / 2) + 1) * imgW);
		int startPozY = 20;
		int interPoz = 5;

		for (int c = 0; c < alienMap1.length; c++) {
			List<Sprite> alTab = new ArrayList<Sprite>();

			for (int r = 0; r < alienMap1[c].length; r++) {
				// System.out.print(alienMap1[c][r] + " ");

				if (alienMap1[c][r] == 1) {
					AlienShip a = new AlienShip(startPozX + (r * imgW)
							+ interPoz, startPozY + (c * imgH) + interPoz);
					a.vX = 2;
					alTab.add(a);

				}
				// System.out.print(alienMap1[c][r]);
			}
			addToRespawnSprite(alTab);

		}
		useRespawnLists();
	}

	private void aliensFiring() {
		List<Object> l = SpriteManager.getListObject(AlienShip.class);
		Iterator<Object> i = l.iterator();
		while (i.hasNext()) {
			AlienShip a = (AlienShip) i.next();

			AlienMissile am = a.fire();
			if (am == null)
				continue;

			Sprite[] o = { am };
			getSpriteManager().addSprites(o);
			getSceneElements().getChildren().add(am.node);
		}
	}

	// generowanie asteroid
	private void generateAsteroids() {

		if (System.currentTimeMillis() - lastAsteroidSubTimeGeneration < asteroidSubTimeGeneration) {
			// System.out.println("return from generating");
			return;
		}

		lastAsteroidSubTimeGeneration = System.currentTimeMillis();
		// Random random = new Random();
		int anumb = 4;

		Double o = 1200d;
		Sprite[] astTab = new Sprite[anumb];
		Asteroid ast = null;

		for (int i = 0; i < anumb; i++) {

			try {
				ast = (Asteroid) scrm.getScript(
						GameConst.JS_ASTEROID_DEMOLITION_NAME).invokeFunction(
						"generateAsteroid1", o);

				astTab[i] = ast;

			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			} catch (ScriptException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}

		}

		addToRespawnSprite(astTab);

	}

	// -------------------------------- ELEMENTY PETLI

	@Override
	protected void revideGame() {

		if (DEAD_STATE == true) {
			useDeadState();
			return;
		}

		if (WIN_STATE == true) {
			useDeadState();
			return;
		}

		if (ship.isDead) {

			prepareDeadState();

		}

		if (ALIEN_PART) {
			alienCount = SpriteManager.getListObject(AlienShip.class).size();

			if (FIRSTROUND_STATE) {
				enableAlienGen = true;
				enableAsteroidGen = false;
				actualAlienMap++;
				FIRSTROUND_STATE = false;
				return;
			}

			if (alienCount != 0)
				return;
			else {

				if (stageSequenceList.size() == stageIndex) {
					WIN_STATE = true;
					return;
				}

				if (stageSequenceList.get(stageIndex) == 1)
					ALIEN_PART = true;
				else
					ALIEN_PART = false;

				if (ALIEN_PART) {
					stageIndex++;
					enableAlienGen = true;
					enableAsteroidGen = false;
					actualAlienMap++;
					parkShipTime=System.currentTimeMillis();
				}

			}
		}

		if (!ALIEN_PART) {
			asteroidCount = SpriteManager.getCountAsteroids();

			if (FIRSTROUND_STATE) {
				FIRSTROUND_STATE = false;
				enableAsteroidGen = true;
				enableAlienGen = false;
				actualAsteroidRound++;
				lastAsteroidTime = System.currentTimeMillis();
				return;
			}

			if (System.currentTimeMillis() - lastAsteroidTime < asteroidTimeRound) {
				enableAsteroidGen = true;
				return;
			} else {

				if (stageSequenceList.size() == stageIndex) {
					WIN_STATE = true;
					return;
				}
				
				if(asteroidCount!=0) return;
				
				if (stageSequenceList.get(stageIndex) == 1)
					ALIEN_PART = true;
				else
					ALIEN_PART = false;

				if (!ALIEN_PART) {
					stageIndex++;
					enableAsteroidGen = true;
					enableAlienGen = false;
					actualAsteroidRound++;
					lastAsteroidTime = System.currentTimeMillis();
					return;
				}
			}
		}

	}

	// uaktualnianie aktorow
	@Override
	protected void handleUpdate(Sprite sprite) {
		gameStats.getTimeText().setVisible(false);
		
		long time = System.currentTimeMillis() - parkShipTime;
		
		if ( time < waitParkTime) {
			gameStats.getTimeText().setVisible(true);
			gameStats.updateTime((waitParkTime/1000)-time/1000);
			ship.parkSpaceShip();
			try {
				Robot r = new Robot();
				r.mouseMove(550, 550);
				
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}

		if (sprite instanceof SpaceShip) {
			
			SpaceShip sphere = (SpaceShip) sprite;
			if (sphere.handleBoundsMeet(getGameScene().getWidth(),
					getGameScene().getHeight() - 60)) {

			}
		}

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

		if (sprite instanceof AlienMissile) {
			AlienMissile sphere = (AlienMissile) sprite;
			if (sphere.handleBoundsMeet(getGameScene().getWidth(),
					getGameScene().getHeight())) {
				((AlienMissile) sprite).handleDeath(this);
			}
		}

		sprite.update();

	}

	// odzywanie elemntow
	@Override
	protected void respawnElements() {
		useRespawnLists();

		if (enableAlienGen) {
			generateAlien();
			enableAlienGen = false;
		}

		if (enableAsteroidGen) {
			// Iterator<Sprite> i = spriteToRespawnList.iterator();
			generateAsteroids();
			enableAsteroidGen = false;

		}

	}

	// kolizje dwoch obiektow
	@Override
	protected boolean handleCollision(Sprite spriteA, Sprite spriteB) {
		if (spriteA != spriteB) {

			if (spriteA.collide(spriteB, this)) {

				spriteA.handleDeath(this);

				if (spriteB != ship) {

					spriteB.handleDeath(this);
				}
				if (spriteB == ship) {
					// System.out.println("C ");
					ship.reduceEnergy(spriteA.getDamage());
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
		gameStats.updateMissileCounter(getSpriteManager().getListObject(Missile.class).size()+getSpriteManager().getListObject(BigMissile.class).size());
		gameStats.updateSpaceEnergyCounter(ship.getEnergy());
		gameStats.updateSpaceScoreCounter(ship.getScore());
		gameStats.updateSpaceLevelCounter(ship.getLevel());

		// System.out.println("ALIEN="+SpriteManager.getListObject(AlienShip.class).size());

	}

	@Override
	protected void updateBehaviorSprites() {

		aliensFiring();
	}

	//-----INNE
	public void addToRespawnSprite(Sprite s) {
		spriteToRespawnList.add(s);
		// nodeToRespawnList.add(s.getNode());
	}

	public void addToRespawnSprite(Sprite[] s) {
		for (int i = 0; i < s.length; i++) {
			spriteToRespawnList.add(s[i]);
			// nodeToRespawnList.add(s[i].getNode());
		}
	}

	public void addToRespawnSprite(List<Sprite> lista) {
		for (Sprite s : lista) {
			spriteToRespawnList.add(s);
			// nodeToRespawnList.add(s.getNode());
		}
	}

	private void useRespawnLists() {

		for (int i = 0; i < 2; i++) {
			try {
				getSpriteManager().addSprite(spriteToRespawnList.get(i));
				getSceneElements().getChildren().add(
						spriteToRespawnList.get(i).getNode());

				spriteToRespawnList.remove(i);
				// nodeToRespawnList.remove(i);

			} catch (IndexOutOfBoundsException e) {
				return;
			}
		}

	}
}