package pl.agit.Game.Sprites.Characters;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import javax.script.ScriptException;

import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import pl.agit.Game.Gamedef.GameConst;
import pl.agit.Game.Image.ImageManager;
import pl.agit.Game.Scripts.ScriptManager;
import pl.agit.Game.Sound.SoundManager;
import pl.agit.Game.Sprites.Sprite;
import pl.agit.Game.World.AsteroidDemolition;
import pl.agit.Game.World.GameWorld;

public class AlienShip extends Sprite implements GameConst {

	private double startEnergy = 100;
	private double energy = startEnergy;

	private final Group alienBook = new Group();
	private Circle sphere = new Circle();

	private SoundManager sm = SoundManager.getSoundManager(1);
	private ScriptManager scrm = ScriptManager.getScriptManager();
	private ImageManager im = ImageManager.getImageManager();

	private double scW; // maks szerokosc poruszania
	private double scH; // maks wysokosc poruszania

	private double startx; // pozycje poczatkowe
	private double starty;

	private Rectangle2D[] cellClips = new Rectangle2D[3]; // klatki spritow
	private Rectangle2D actualCell;
	public byte changedView = 0;
	
	//private ImageView alienView;
	
	public byte atack=0; //stan ataku 1=atak
	
	public long fireTime = 5000;
	public long lastFireTime=System.currentTimeMillis();

	private AlienShip() {
		// GRAFIKA
//		String dir = new File("").getAbsolutePath(); // znalezienie sciaki
//														// bezwzglednej do
//														// projektu
//		// System.out.println(dir + GameConst.GFX_ALIENSHIP1);
//		URL u = null;
//		try {
//			u = new File(dir + GameConst.GFX_ALIENSHIP1).toURI().toURL();
//		} catch (MalformedURLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		Image alienImage = new Image(u.toExternalForm());
//		alienView = new ImageView(alienImage);
		im.loadImage(GFX_ALIENSHIP1_NAME, GFX_ALIENSHIP1);

		cellClips[0] = new Rectangle2D(0 * 86, 0, 86, 100); // normlany
		cellClips[1] = new Rectangle2D(1 * 86, 0, 86, 100); // uszkodzony
		cellClips[2] = new Rectangle2D(2 * 86, 0, 86, 100); // atak speed

		actualCell = cellClips[0];
		
		
		im.getImage(GFX_ALIENSHIP1_NAME).setViewport(actualCell);
		alienBook.getChildren().add(im.getImage(GFX_ALIENSHIP1_NAME));

		sphere.setRadius(alienBook.getBoundsInLocal().getWidth() / 2);

		node = alienBook;
		
		node.setVisible(true);

		// SKRYPTY

		try {
			scrm.addScript(GameConst.JS_ALIENSHIP_NAME, GameConst.JS_ALIENSHIP);
		} catch (ScriptException | FileNotFoundException e) {
			System.out.println("blad skryptu");
			e.printStackTrace();
		}
	}

	public AlienShip(double startx, double starty) {
		this();
		this.startx = startx;
		this.starty = starty;
		alienBook.setTranslateX(startx);
		alienBook.setTranslateY(starty);

		scW = 1200;
		scH = 500;
	}

	@Override
	public void update() {
		//skrypt
		Object[] o = {this,startx,starty,scW};
		try {
			 scrm.getScript(GameConst.JS_ALIENSHIP_NAME).invokeFunction("behavior1",o);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
				
		if (atack!=0){
			refreshView(cellClips[2]);
			//System.out.println("ATACK!");
		}
		else {
			refreshView();
		}
		

	}
	
	public AlienMissile fire (){
		if(System.currentTimeMillis()-lastFireTime>fireTime){
			lastFireTime=System.currentTimeMillis();
			return new AlienMissile(10, node.getTranslateX()+alienBook.getBoundsInLocal().getWidth()/2, node.getTranslateY()+alienBook.getBoundsInLocal().getWidth()/2, 0, 5);
		}
		return null;
	}

	public void refreshView() {
		if(changedView==0) return;
		((ImageView) alienBook.getChildren().get(0)).setViewport(actualCell);
		changedView=0;
	}

	public void refreshView(Rectangle2D rec) {
		if(changedView==0) return;
		((ImageView) alienBook.getChildren().get(0)).setViewport(rec);
		changedView=1;
	}

	private void reduceEnergy(double dam) {
		energy = energy - dam;
		if (energy <= startEnergy / 2)
			actualCell = cellClips[1];
			changedView=1;
	}

	@Override
	public boolean collide(Sprite other, GameWorld gm) {

		if (other instanceof Missile) {
			boolean val = collide((Missile) other);
			if (val)
				// ((AsteroidDemolition) gm).addScore(50);
				reduceEnergy(other.getDamage());
			return val;
		}
		if (other instanceof SpaceShip) {
			return collide((SpaceShip) other);
		}
		return false;
	}

	private boolean collide(Missile other) {
		// //jesli ukryty to nie koliduje
		if (!node.isVisible() || !other.node.isVisible()) {
			return false;
		}

		// kolizje zalezne od rozmiaru
		Circle otherSphere = other.getAsCircle();
		Circle thisSphere = getAsCircle();
		double dx = otherSphere.getTranslateX() - thisSphere.getTranslateX();
		double dy = otherSphere.getTranslateY() - thisSphere.getTranslateY();
		double distance = Math.sqrt(dx * dx + dy * dy);
		double minDist = otherSphere.getRadius() + thisSphere.getRadius() + 3;

		return (distance < minDist);
	}

	private boolean collide(SpaceShip s) {
		return false;
	}

	public void handleDeath(GameWorld gm) {

		if (energy > 0)
			return;

		super.noImplode(gm);
		super.handleDeath(gm);

	}

	public Circle getAsCircle() {
		sphere.setTranslateX(node.getTranslateX()
				+ node.getBoundsInLocal().getWidth() / 2);
		sphere.setTranslateY(node.getTranslateY());
		return sphere;
	}

	public double getImageWidth() {
		return alienBook.getBoundsInLocal().getWidth();
	}

	public double getImageHeight() {
		return alienBook.getBoundsInLocal().getHeight();
	}

	public Rectangle2D[] getCellClips() {
		return cellClips;
	}

	public Rectangle2D getActualCell() {
		return actualCell;
	}
	

}
