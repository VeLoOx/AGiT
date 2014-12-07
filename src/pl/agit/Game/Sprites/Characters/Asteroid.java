package pl.agit.Game.Sprites.Characters;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import javax.script.ScriptException;

import javafx.animation.FadeTransitionBuilder;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;
import pl.agit.Game.Gamedef.GameConst;
import pl.agit.Game.Image.ImageManager;
import pl.agit.Game.Scripts.ScriptManager;
import pl.agit.Game.Sprites.Sprite;
import pl.agit.Game.World.AsteroidDemolition;
import pl.agit.Game.World.GameWorld;
import javafx.util.Duration;

public class Asteroid extends Sprite implements GameConst {

	private Circle asteroid;
	Circle sphere;
	private final Group astBook = new Group();
	private double radius;
	
	ImageManager im = ImageManager.getImageManager();
	ScriptManager sm = ScriptManager.getScriptManager();

	private Asteroid(double radius) {

		sphere = new Circle();

					
		
		ImageView astView = new ImageView(im.getImage(GFX_ASTEROID_NAME));
		astView.setFitHeight(radius*2);
		astView.setFitWidth(radius*2);
		astBook.getChildren().add(astView);

		this.radius = radius;
		sphere.setRadius(radius);

		node = astBook;
		
		
	}

	public Asteroid(double radius, RadialGradient color) {
		this(radius);

		//sphere.setFill(color);

	}

	public Asteroid(double radius, LinearGradient color) {
		this(radius);

		sphere.setFill(color);

	}

	public Asteroid(double radius, String color) {
		this(radius);
		sphere.setFill(Color.valueOf(color));
		// node = sphere;
	}

	public Node getAsNode() {
		return node;
	}

	public double getRadius() {
		return radius;
	}

	@Override
	public void update() {
		
		// TODO Auto-generated method stub
		node.setTranslateX(node.getTranslateX() + vX);
		node.setTranslateY(node.getTranslateY() + vY);

	}

	@Override
	public double getDamage() {
		return 10;
	}

	@Override
	public boolean collide(Sprite other, GameWorld gm) {
		if (other instanceof Asteroid) {
			return collide((Asteroid) other);
		}
		if (other instanceof Missile) {
			boolean val = collide((Missile) other);
			if (val){
				((AsteroidDemolition) gm).addScore(50);
				generateShatter(gm, getNode().getTranslateX(), getNode().getTranslateY(), vY);
			}
			return val;
		}
		if (other instanceof SpaceShip) {
			return collide((SpaceShip) other);
		}
		return false;
	}

	private boolean collide(SpaceShip other) {

		// //jesli ukryty to nie koliduje
		if (!node.isVisible() || !other.node.isVisible()) {
			return false;
		}

		// kolizje zalezne od rozmiaru
		Circle otherSphere = new Circle();
		otherSphere.setRadius(48);
		otherSphere.setTranslateX(other.getMpozX() + 40);
		otherSphere.setTranslateY(other.getMpozY() + 60);
		otherSphere.setFill(Color.valueOf("red"));

		Circle thisSphere = getAsCircle();
		double dx = otherSphere.getTranslateX() - thisSphere.getTranslateX();
		double dy = otherSphere.getTranslateY() - thisSphere.getTranslateY();
		double distance = Math.sqrt(dx * dx + dy * dy);
		double minDist = otherSphere.getRadius() + thisSphere.getRadius() + 3;

		boolean val = (distance < minDist);

		return val;
	}

	private boolean collide(Asteroid other) {

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

	@Override
	// obsluga kolizji ze scianami
	public boolean handleBoundsMeet(double wx, double hy) {
		if (this.node.getTranslateY() > hy
				- this.node.getBoundsInParent().getHeight()) {
			isDead = true;
			return true;

		}
		return false;
	}

	public Circle getAsCircle() {
		sphere.setTranslateX(node.getTranslateX()
				+ node.getBoundsInLocal().getWidth() / 2);
		sphere.setTranslateY(node.getTranslateY());
		return sphere;
	}

	public void handleDeath(GameWorld gm) {
		// System.out.print("T");

		noImplode(gm);

		super.handleDeath(gm);
	}

	// wybuch
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void implode(final GameWorld gameWorld) {
		vX = vY = 0;
		FadeTransitionBuilder.create().node(node)
				.duration(Duration.millis(300)).fromValue(node.getOpacity())
				.toValue(0).onFinished(new EventHandler() {
					// @Override
					public void handle(Event arg0) {
						isDead = true;
						gameWorld.getSceneElements().getChildren().remove(node);
					}

				}).build().play();
	}
	
	//generuje od³amki
	private void generateShatter(GameWorld g, double pozx, double pozy, double vY){
		if(radius < 15) return;
		
		Object[] o = {this.getNode().getTranslateX(),this.getNode().getTranslateY(),vY,radius};
		Asteroid[] aT = null;
		try {
			aT = (Asteroid[]) sm.getScript(GameConst.JS_ASTEROID_NAME).invokeFunction("generateShatter", o);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		((AsteroidDemolition)g).addToRespawnSprite(aT);
		
		/*Random r = new Random();
		int newMaxRadius = (int) (radius/2);
		int newMinRadius = (int) (radius/3);
		
		int shatterNumb = r.nextInt(3)+1;
		for(int i=0;i<shatterNumb;i++){
			
			double rad = r.nextInt(newMaxRadius-newMinRadius)+newMinRadius;;
					
			Asteroid a = new Asteroid(rad,"red");
			int direct=1; //1=na prawo  -1 = na lewo
			int interPozX=r.nextInt(100);
			if(r.nextBoolean()) direct = -1;
			interPozX = interPozX*direct;
			a.getNode().setTranslateX(pozx+interPozX);
			a.getNode().setTranslateY(pozy+r.nextInt(10));
			a.vY = vY+r.nextInt(4)+1;
			
			((AsteroidDemolition)g).addToRespawnSprite(a);
			
		}*/
	}

}
