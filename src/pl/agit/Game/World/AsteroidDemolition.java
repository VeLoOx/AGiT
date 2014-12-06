package pl.agit.Game.World;

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
import pl.agit.Game.Sprites.Characters.Missile;
import pl.agit.Game.Sprites.Characters.SpaceShip;
import pl.agit.Game.World.GUIElements.GameStats;

public class AsteroidDemolition extends GameWorld implements GameConst {

	private GameStats gameStats;

	private int asteroidCount = 0;
	private int missileCount = 0;
	private int alienCount = 0;
	
	private boolean enableAlienGen=false;
	private boolean enableAsteroidGen=false;
	
	private boolean ALIEN_PART=false;

	private SpaceShip ship = new SpaceShip();
	
	private ArrayList<byte[][]> alienMapList = new ArrayList<byte[][]>();
	private int actualAlienMap = -1;
	
	private int actualAsteroidRound=0; //aktualna runda asteroid
	private long asteroidTimeRound = 10000; //czas trwania calej rundy
	private long lastAsteroidTime = 0; //czas rozpoczecia rundy
	
	private long asteroidSubTimeGeneration=1000; //czas regenerowania asteroid
	private long lastAsteroidSubTimeGeneration=0; //ostatni czas regenracji
	
	Image backgroundImage;
	ImageView backView;
	Group g = new Group();

	SoundManager sm = SoundManager.getSoundManager(1);
	ScriptManager scrm = ScriptManager.getScriptManager();
	ImageManager im = ImageManager.getImageManager();

	public AsteroidDemolition(int fps, String title) {
		super(fps, title);

		// GRAFIKA
	
		im.loadImage(GFX_MAINSPACE_NAME, GFX_MAINSPACE);

		// SKRYPTY
		try {
			scrm.addScript(GameConst.JS_ASTEROID_DEMOLITION_NAME,
					GameConst.JS_ASTEROID_DEMOLITION);
			scrm.addScript(GameConst.JS_ALIEN_MAP_NAME,
					GameConst.JS_ALIEN_MAP);
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
		final Timeline gameLoop = getGameLoop();
		
		primaryStage.setTitle(getWindowTitle()); // tytul okna
		primaryStage.setFullScreen(true);
		
				

		// tworzenie sceny
		setSceneElements(new Group());
		Scene sc = new Scene(getSceneElements());
		setGameScene(sc);
		getGameScene().getStylesheets().addAll(GameConst.CSS_PACKAGE_PATH);
		primaryStage.setScene(getGameScene());
		
		//dodanie statystyk
		gameStats = new GameStats(primaryStage.getWidth(),
				primaryStage.getHeight());
		getSceneElements().getChildren().add(gameStats.getStats());
		
		//doanie tla
		backView = new ImageView(im.getImage(GFX_MAINSPACE_NAME));
		g.getChildren().add(backView);
		Node node = g;
		node.toBack();
		getSceneElements().getChildren().add(0, node);

		//dodanie statku gracza
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

		//ustawienie sterowania				
		setupInput(primaryStage);

		//inicjalizacja listy map alienow
		try {
			alienMapList = (ArrayList<byte[][]>) scrm.getScript(JS_ALIEN_MAP_NAME).invokeFunction("returnAlienMapList");
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addScore(int val) {
		ship.addScore(val);
	}

	public void reductionMissile() {
		missileCount--;
	}

	private void setupInput(final Stage primaryStage) {

		/*okno - zamkniecie okna*/
		primaryStage.setOnCloseRequest(getCloseWindowEv()); 

		
		/*ruch lursora - poruszanie statkiem*/
		primaryStage.getScene().setOnMouseMoved(getMoveMouseEv());  
		/*strzelanie lewym*/
		primaryStage.getScene().setOnMousePressed(getMoveOrFire()); 
		/*strzelanie podczas poruszania*/
		primaryStage.getScene().setOnMouseDragged(getMovedragOrFireMouseEv());  
		/*klawisze*/																		
		primaryStage.getScene().setOnKeyPressed(getKeyboardEv(primaryStage));
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

				sm.shutdown();
				System.out.println("Stage is closing");
			}
		};
	}

	private EventHandler<KeyEvent> getKeyboardEv(final Stage pm) {
		return new EventHandler<KeyEvent>() {
			public void handle(KeyEvent ke) {
				if (ke.getCode() == KeyCode.ESCAPE) {
					sm.shutdown();
					System.out.println("Stage is closing");
					pm.close();
				}

				if (ke.getCode() == KeyCode.TAB) {
					// boolean vis = gameStats.getStats().isVisible();
					if (gameStats.getVBox().isVisible())
						gameStats.getVBox().setVisible(false);
					else
						gameStats.getVBox().setVisible(true);

				}
			}
		};
	}

	private void generateAlien() {

		AlienShip aCheck = new AlienShip(100, 100);// probny

		// zaladowanie lsosowego ukladu stakow obcych
		byte[][] alienMap1 = alienMapList.get(actualAlienMap);
		

		int maxCol = (int) (getGameScene().getWidth() / aCheck.getImageWidth()) - 1;
		int maxRow = (int) (getGameScene().getHeight() / aCheck
				.getImageHeight()) - 5;

		int startPozX = (int) ((int) (((maxCol - 10) / 2) + 1) * aCheck
				.getImageWidth());
		int startPozY = 20;
		int interPoz = 5;
		int index=0;
		double imgW = aCheck.getImageWidth();
		double imgH = aCheck.getImageHeight();
		
		for (int c = 0; c < alienMap1.length; c++) {
			List<Sprite> alTab = new ArrayList<Sprite>();
			
			for (int r = 0; r < alienMap1[c].length; r++) {
				//System.out.print(alienMap1[c][r] + " ");

				if (alienMap1[c][r] == 1) {
					AlienShip a = new AlienShip(startPozX
							+ (r * imgW) + interPoz,
							startPozY + (c * imgH)
									+ interPoz);// probny
					a.vX = 2;
					alTab.add(a);
					index++;
					
				}
				System.out.print(alienMap1[c][r]);
			}
			getSpriteManager().addSprites(alTab.toArray());
			for(int i=0;i<alTab.size();i++)
				getSceneElements().getChildren().add(alTab.get(i).getNode());
			System.out.println();
			index=0;
		}
					
	}

	private void aliensFiring(){
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
		if(System.currentTimeMillis()-lastAsteroidSubTimeGeneration<asteroidSubTimeGeneration) return;
		
		lastAsteroidSubTimeGeneration=System.currentTimeMillis();
		Random random = new Random();
		int anumb = random.nextInt(5) + 1;
		Scene scene = getGameScene();
		Double o = scene.getWidth();
		Sprite[] astTab = new Sprite[anumb];
		for (int i = 0; i < anumb; i++) {

			Asteroid ast = null;
			try {
				ast = (Asteroid) scrm.getScript(
						GameConst.JS_ASTEROID_DEMOLITION_NAME).invokeFunction(
						"generateAsteroid1", o);
				
				astTab[i]=ast;;

			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ScriptException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
		}
		
		//Sprite[] s = (Sprite[])astList.toArray() ;
		getSpriteManager().addSprites(astTab);
		//ast.node.toFront();
		
		for(int i=0;i<astTab.length;i++)
		getSceneElements().getChildren().addAll(astTab[i].getNode());

	}

	
	
	//ELEMENTY PETLI
	
	@Override
	protected void revideGame(){
		
		asteroidCount = SpriteManager.getCountAsteroids();
		alienCount = SpriteManager.getListObject(AlienShip.class).size();
		
		if(ALIEN_PART)
		if(enableAsteroidGen==false && alienCount==0) {
			actualAlienMap++;
			enableAlienGen=true; //pozwolenie na wygenerowanie obcych
			
			if(actualAlienMap>=alienMapList.size()){
				enableAlienGen=false;
				//enableAsteroidGen=true;
				ALIEN_PART=false;
				return;
			}
		
		}
		
		
		if(!ALIEN_PART)
		if(enableAlienGen==false){
			if(lastAsteroidTime==0){
				enableAsteroidGen=true;
				actualAsteroidRound++;
				lastAsteroidTime = System.currentTimeMillis();
				return;
			} else {
				if(System.currentTimeMillis()-lastAsteroidTime<=asteroidTimeRound){
					//actualAsteroidRound++;
					enableAsteroidGen=true;
					return;
				} else {
					ALIEN_PART=true;
					enableAsteroidGen=false;
					enableAlienGen=false;;
				}
			
			}
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

		if (sprite instanceof AlienMissile) {
			AlienMissile sphere = (AlienMissile) sprite;
			if (sphere.handleBoundsMeet(getGameScene().getWidth(),
					getGameScene().getHeight())) {
				((AlienMissile) sprite).handleDeath(this);
			}
		}

		sprite.update();
		
		
		
	}
	
	//odzywanie elemntow
	@Override
	protected void respawnElements() {
		
		// if (asteroidCount == 0)
		// generateAsteroids(); // dodawanie asteroid

		if (enableAlienGen){
			generateAlien();
			enableAlienGen=false;
		}
		
		if (enableAsteroidGen){
			generateAsteroids();
			enableAsteroidGen=false;
		}
		//alienCount=1;
		
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
					System.out.println("C ");
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
		gameStats.updateMissileCounter(missileCount);
		gameStats.updateSpaceEnergyCounter(ship.getEnergy());
		gameStats.updateSpaceScoreCounter(ship.getScore());
		gameStats.updateSpaceLevelCounter(ship.getLevel());

		// System.out.println("ALIEN="+SpriteManager.getListObject(AlienShip.class).size());

	}

	@Override
	protected void updateBehaviorSprites(){
		
		aliensFiring();
	}
}