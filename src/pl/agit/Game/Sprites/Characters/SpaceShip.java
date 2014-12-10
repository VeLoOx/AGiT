package pl.agit.Game.Sprites.Characters;

import java.io.FileNotFoundException;

import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import javax.script.ScriptException;

import pl.agit.Game.Gamedef.GameConst;
import pl.agit.Game.Image.ImageManager;
import pl.agit.Game.Scripts.ScriptManager;
import pl.agit.Game.Sound.SoundManager;
import pl.agit.Game.Sprites.Sprite;

public class SpaceShip extends Sprite implements GameConst {

	private int LEVEL_UPGRADE_JUMP = 2; // wspolczynnik zmiany poziomu

	private double mpozX = 500;
	private double mpozY = 500;

	private double energy = 100;
	private int score = 0;
	private int level = 0;
	private int previousLevelScore = 0;

	private final Group shipBook = new Group();
	double maxX, maxY;

	private SoundManager sm = SoundManager.getSoundManager(1);
	private ScriptManager scrm = ScriptManager.getScriptManager();
	private ImageManager im = ImageManager.getImageManager();

	public SpaceShip() {

		
		im.loadImage(GFX_MAINSHIP_NAME, GFX_MAINSHIP);
		ImageView shipView = new ImageView(im.getImage(GFX_MAINSHIP_NAME));
		

		shipBook.getChildren().addAll(shipView);
		shipBook.setTranslateX(100);
		shipBook.setTranslateY(100);
		shipBook.setCache(true);
		shipBook.setCacheHint(CacheHint.SPEED);
		node = shipBook;

		// SKRYPTY

		try {
			scrm.addScript(GameConst.JS_MAINSHIP_NAME, GameConst.JS_MAINSHIP);
		} catch (ScriptException | FileNotFoundException e) {
			System.out.println("blad skryptu");
			e.printStackTrace();
		}

		// DZWIEKI
		sm.loadSoundEffects(GameConst.A_MAINSHIP_LASER_NAME,
				GameConst.A_MAINSHIP_LASER);

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		// EventHandler<MouseEvent> ev = getMouseMoveEvent();
		// System.out.print("T");
		if (mpozX > maxX - this.node.getBoundsInParent().getWidth())
			mpozX = maxX - this.node.getBoundsInParent().getWidth();
		shipBook.setTranslateX(mpozX);
		if (mpozY > maxY - this.node.getBoundsInParent().getHeight())
			mpozY = maxY - this.node.getBoundsInParent().getHeight();
		shipBook.setTranslateY(mpozY);

	}
	
	public void parkSpaceShip(){
		shipBook.setTranslateX(550);
		shipBook.setTranslateY(550);
	}

	private void levelUpdate(int sc) {
		// upgrade levela
		if (sc == 100) {
			previousLevelScore = sc;
			level++;
		} else

		if (sc == (previousLevelScore * LEVEL_UPGRADE_JUMP)) {
			previousLevelScore = sc;
			level++;
		}
	}

	public void reduceEnergy(double dam) {
		// wywolanie skryptowe redulcji energii
		Object[] o = { energy, dam };

		try {
			energy = (double) scrm.getScript(GameConst.JS_MAINSHIP_NAME)
					.invokeFunction("reduceEnergy", o);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(energy<=0) isDead=true;
	}

	public void addScore(int val) {
		score = score + val;
		levelUpdate(score);
	}

	public double getEnergy() {
		return energy;
	}

	public double getScore() {
		return score;
	}

	public int getLevel() {
		return level;
	}

	public Missile fire() {

		Object[] o = { mpozX, mpozY,
				shipBook.getChildren().get(0).getBoundsInLocal().getWidth() };
		Missile m = null;

		try {
			m = (Missile) scrm.getScript(GameConst.JS_MAINSHIP_NAME)
					.invokeFunction("fire", o);
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

	// @Override
	// public boolean collide(Sprite other, GameWorld gm) {
	//
	// if (other instanceof AlienMissile) {
	// boolean val = collide((AlienMissile) other);
	// if (val)
	// // ((AsteroidDemolition) gm).addScore(50);
	// reduceEnergy(other.getDamage());
	// return val;
	// }
	//
	// return false;
	// }
	//
	// private boolean collide(AlienMissile other) {
	// // //jesli ukryty to nie koliduje
	// if (!node.isVisible() || !other.node.isVisible()) {
	// return false;
	// }
	//
	// // kolizje zalezne od rozmiaru
	// Circle otherSphere = other.getAsCircle();
	// Circle thisSphere = getAsCircle();
	// double dx = otherSphere.getTranslateX() - thisSphere.getTranslateX();
	// double dy = otherSphere.getTranslateY() - thisSphere.getTranslateY();
	// double distance = Math.sqrt(dx * dx + dy * dy);
	// double minDist = otherSphere.getRadius() + thisSphere.getRadius() + 3;
	//
	// return (distance < minDist);
	// }

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

	/*
	 * public static void main(String[] args){
	 * 
	 * 
	 * 
	 * }
	 */

}
