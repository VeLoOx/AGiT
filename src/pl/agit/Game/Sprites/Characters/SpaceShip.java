package pl.agit.Game.Sprites.Characters;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Circle;

import javax.script.ScriptException;

import pl.agit.Game.Gamedef.GameConst;
import pl.agit.Game.Scripts.ScriptManager;
import pl.agit.Game.Sound.SoundManager;
import pl.agit.Game.Sprites.Sprite;

public class SpaceShip extends Sprite implements GameConst {

	// milisekundy na klatke
	private final static float MILLIS_PER_FRAME = 3000;

	private final static float SPEED = 3.3f;

	private final static float MISSILE_SPEED = 5.3f;

	private double mpozX = 500;
	private double mpozY = 500;

	private double energy = 100;

	private final Group shipBook = new Group();
	double maxX, maxY;

	private KeyCode keyCode;

	private SoundManager sm = SoundManager.getSoundManager(1);
	ScriptManager scrm = ScriptManager.getScriptManager();

	public SpaceShip() {

		//GRAFIKA
		String dir = new File("").getAbsolutePath(); //znalezienie sciaki bezwzglednej do projektu
		System.out.println(dir+GameConst.GFX_MAINSHIP);
		URL u=null;
		try {
			u = new File(dir+GameConst.GFX_MAINSHIP).toURI().toURL();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Image shipImage = new Image(u.toExternalForm());
		ImageView shipView = new ImageView(shipImage);
		
		
		shipBook.getChildren().addAll(shipView);
		shipBook.setTranslateX(100);
		shipBook.setTranslateY(100);
		node = shipBook;
		
		//SKRYPTY
		
		try {
			scrm.addScript(GameConst.JS_MAINSHIP_NAME, GameConst.JS_MAINSHIP);
		} catch (ScriptException | FileNotFoundException e) {
			System.out.println("blad skryptu");
			e.printStackTrace();
		}
		
		
		//DZWIEKI
		sm.loadSoundEffects(GameConst.A_MAINSHIP_LASER_NAME,GameConst.A_MAINSHIP_LASER);

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		// EventHandler<MouseEvent> ev = getMouseMoveEvent();
		// System.out.print("T");
		if (mpozX > maxX - this.node.getBoundsInParent().getWidth() - 50)
			mpozX = maxX - this.node.getBoundsInParent().getWidth() - 50;
		shipBook.setTranslateX(mpozX);
		if (mpozY > maxY - this.node.getBoundsInParent().getHeight())
			mpozY = maxY - this.node.getBoundsInParent().getHeight();
		shipBook.setTranslateY(mpozY);

	}

	public void reducteEnergy(double dam) {
		//wywolanie skryptowe
		Object[] o = {energy,dam};
		
		try {
			energy = (double) scrm.getScript(GameConst.JS_MAINSHIP_NAME).invokeFunction("reduceEnergy", o);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public double getEnergy() {
		return energy;
	}

	public Missile fire() {

//		Missile m = new Missile(10, mpozX
//				+ shipBook.getChildren().get(0).getBoundsInLocal().getWidth()
//				/ 2, mpozY, 0, 4);
		
		Object[] o = {mpozX,mpozY,shipBook.getChildren().get(0).getBoundsInLocal().getWidth()};
		Missile m=null;
		
		try {
			m = (Missile) scrm.getScript(GameConst.JS_MAINSHIP_NAME).invokeFunction("fire", o);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sm.playSound("laser");

		return m;

	}

	public Circle getAsCircle() {
		Circle c = new Circle(shipBook.getBoundsInLocal().getWidth() / 2);
		return c;
	}

	@Override
	// obsluga kolizji ze scianami
	public boolean handleBoundsMeet(double wx, double hy) {
		maxX = wx;
		maxY = hy;
		return false;
	}

	public void stop() {
		// System.out.println("Stop");
		node.setTranslateX(mpozX - 20);
		node.setTranslateY(mpozY - 20);
	}

	public double getMpozX() {
		return mpozX;
	}

	public void setMpozX(double mpozX) {
		this.mpozX = mpozX;
	}

	public double getMpozY() {
		return mpozY;
	}

	public void setMpozY(double mpozY) {
		this.mpozY = mpozY;
	}

	// public boolean collide(Sprite s){
	// this.reducteEnergy(s.getDamage());
	// return false;
	// }
	// public void handleDeath(GameWorld gm){
	// //Sprite[] s = {this};
	// //gm.getSpriteManager().addSpritesToBeRemoved(s);
	// }

	/*
	 * public static void main(String[] args){
	 * 
	 * 
	 * 
	 * }
	 */

}
