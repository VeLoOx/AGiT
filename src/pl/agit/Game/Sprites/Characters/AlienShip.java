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
import pl.agit.Game.Scripts.ScriptManager;
import pl.agit.Game.Sound.SoundManager;
import pl.agit.Game.Sprites.Sprite;
import pl.agit.Game.World.AsteroidDemolition;
import pl.agit.Game.World.GameWorld;

public class AlienShip extends Sprite {

	private double startEnergy = 100;
	private double energy = startEnergy;

	private final Group alienBook = new Group();
	private Circle sphere = new Circle();

	private SoundManager sm = SoundManager.getSoundManager(1);
	private ScriptManager scrm = ScriptManager.getScriptManager();

	public double scW; // maks szerokosc poruszania
	private double scH; // maks wysokosc poruszania

	public double startx; // pozycje poczatkowe
	public double starty;

	private Rectangle2D[] cellClips = new Rectangle2D[3]; // klatki spritow
	Rectangle2D actualCell;
	
	ImageView alienView;
	
	

	private AlienShip() {
		// GRAFIKA
		String dir = new File("").getAbsolutePath(); // znalezienie sciaki
														// bezwzglednej do
														// projektu
		// System.out.println(dir + GameConst.GFX_ALIENSHIP1);
		URL u = null;
		try {
			u = new File(dir + GameConst.GFX_ALIENSHIP1).toURI().toURL();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Image alienImage = new Image(u.toExternalForm());
		alienView = new ImageView(alienImage);

		cellClips[0] = new Rectangle2D(0 * 86, 0, 86, 100); // normlany
		cellClips[1] = new Rectangle2D(1 * 86, 0, 86, 100); // uszkodzony
		cellClips[2] = new Rectangle2D(2 * 86, 0, 86, 100); // atak speed

		actualCell = cellClips[0];
		
		
		alienView.setViewport(actualCell);
		alienBook.getChildren().add(alienView);

		sphere.setRadius(alienBook.getBoundsInLocal().getWidth() / 2);

		node = alienBook;

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
		Object[] o = {this};
		try {
			 scrm.getScript(GameConst.JS_ALIENSHIP_NAME).invokeFunction("behavior1",o);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
//		boolean atack = false;
//
//		if (!atack)
//			refreshView(cellClips[2]);
//		else {
//			refreshView();
//		}
//		
//		
//
//		// TODO Auto-generated method stub
//		node.setTranslateX(node.getTranslateX() + vX);
//		node.setTranslateY(node.getTranslateY() + vY);
//
//		if (node.getTranslateX() >= scW || node.getTranslateX() <= 20)
//			vX = -vX;
//
//		int howLong = 300; // jak g³êboko ma zaatakowac
//
//		if (!atack) {
//			Random r = new Random();
//			if (r.nextInt(1000) == 1) {
//				atack = true;
//				vY = 10;
//			}
//		}
//
//		if (node.getTranslateY() >= starty + howLong)
//			vY = -vY;
//
//		if (node.getTranslateY() <= starty) {
//			node.setTranslateY(starty);
//			refreshView();
//			atack = false;
//		}

	}

	public void refreshView() {
		((ImageView) alienBook.getChildren().get(0)).setViewport(actualCell);
	}

	public void refreshView(Rectangle2D rec) {
		((ImageView) alienBook.getChildren().get(0)).setViewport(rec);
	}

	private void reduceEnergy(double dam) {
		energy = energy - dam;
		if (energy <= startEnergy / 2)
			actualCell = cellClips[1];
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
