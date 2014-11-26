package pl.agit.Game.Sprites.Characters;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

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
import pl.agit.Game.Sprites.Sprite;
import pl.agit.Game.World.AsteroidDemolition;
import pl.agit.Game.World.GameWorld;
import javafx.util.Duration;

public class Asteroid extends Sprite {
	
	private Circle asteroid;
	Circle sphere;
	private final Group astBook = new Group();
	private double radius;
	
	private Asteroid(double radius) {
		
		sphere = new Circle();
		//sphere.setCenterX(radius);
		//sphere.setCenterY(radius);
		
		
		String dir = new File("").getAbsolutePath(); //znalezienie sciaki bezwzglednej do projektu
		//System.out.println(dir+GameConst.GFX_ASTEROID);
		URL u=null;
		try {
			u = new File(dir+GameConst.GFX_ASTEROID).toURI().toURL();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Image astImage = new Image(u.toExternalForm(),radius*2,radius*2,false,false);
		ImageView astView = new ImageView(astImage);
		
		//astView.setScaleX(1/(radius));
		//astView.setScaleY(1/(radius));
		astBook.getChildren().addAll(astView);
		
		this.radius = radius;
		sphere.setRadius(radius);
		
		
		
		node = astBook;	
	}
	
	public Asteroid(double radius, RadialGradient color){
		this(radius);
		
		sphere.setFill(color);
		
		//node = sphere;	
	}
	
	
	
	public Asteroid(double radius, LinearGradient color){
		this(radius);
		
		sphere.setFill(color);
		
		//node = sphere;	
	}
	
	public Asteroid(double radius, String color){
		this(radius);
		sphere.setFill(Color.valueOf(color));
		//node = sphere;
	}
	

	
	public Node getAsNode(){
		return node;
	}
	public double getRadius(){
		return radius;
	}

	@Override
	public void update() {
		//System.out.print("A");
		// TODO Auto-generated method stub
		node.setTranslateX(node.getTranslateX() + vX);
        node.setTranslateY(node.getTranslateY() + vY);
        
        System.out.println("NODE: X="+node.getTranslateX()+"  Y: "+node.getTranslateY() + "WIDHT = "+node.getBoundsInLocal().getWidth());
        Circle c = getAsCircle();
        System.out.println("CIRC: X="+c.getTranslateX()+"  Y: "+c.getTranslateY() + "WIDHT = "+c.getBoundsInLocal().getWidth());
        
	}
	
	@Override
	public double getDamage(){
		return 10;
	}
	
	@Override
    public boolean collide(Sprite other,GameWorld gm) {
        if (other instanceof Asteroid) {
            return collide((Asteroid)other);
        }
        if (other instanceof Missile) {
           boolean val =  collide((Missile)other);
           if(val) ((AsteroidDemolition)gm).addScore(50);
           return val;
        }
        if (other instanceof SpaceShip) {
            return collide((SpaceShip)other);
        }
       return false;
    }
	
	private boolean collide(SpaceShip other) {
		 
        // //jesli ukryty to nie koliduje
        if (!node.isVisible() ||
            !other.node.isVisible() ) {
            return false;
        }
 
        // kolizje zalezne od rozmiaru
        Circle otherSphere = new Circle();
        otherSphere.setRadius(48);
        otherSphere.setTranslateX(other.getMpozX()+40);
        otherSphere.setTranslateY(other.getMpozY()+60);
        otherSphere.setFill(Color.valueOf("red"));
        
        Circle thisSphere =  getAsCircle();
        double dx = otherSphere.getTranslateX() - thisSphere.getTranslateX();
        double dy = otherSphere.getTranslateY() - thisSphere.getTranslateY();
        double distance = Math.sqrt( dx * dx + dy * dy );
        double minDist  = otherSphere.getRadius() + thisSphere.getRadius() + 3;
 
        boolean val = (distance < minDist);
        
        //if(val) isDead=true;
        
        return val;
    }

		
	private boolean collide(Asteroid other) {
		 
        // //jesli ukryty to nie koliduje
        if (!node.isVisible() ||
            !other.node.isVisible() ) {
            return false;
        }
 
        // kolizje zalezne od rozmiaru
        Circle otherSphere = other.getAsCircle();
        Circle thisSphere =  getAsCircle();
        double dx = otherSphere.getTranslateX() - thisSphere.getTranslateX();
        double dy = otherSphere.getTranslateY() - thisSphere.getTranslateY();
        double distance = Math.sqrt( dx * dx + dy * dy );
        double minDist  = otherSphere.getRadius() + thisSphere.getRadius() + 3;
 
        return (distance < minDist);
    }
	
	private boolean collide(Missile other) {
		 
        // //jesli ukryty to nie koliduje
        if (!node.isVisible() ||
            !other.node.isVisible() ) {
            return false;
        }
 
        // kolizje zalezne od rozmiaru
        Circle otherSphere = other.getAsCircle();
        Circle thisSphere =  getAsCircle();
        double dx = otherSphere.getTranslateX() - thisSphere.getTranslateX();
        double dy = otherSphere.getTranslateY() - thisSphere.getTranslateY();
        double distance = Math.sqrt( dx * dx + dy * dy );
        double minDist  = otherSphere.getRadius() + thisSphere.getRadius() + 3;
 
        return (distance < minDist);
    }
	
	@Override
	//obsluga kolizji ze scianami
	public boolean handleBoundsMeet(double wx, double hy){
		if (this.node.getTranslateY() > hy-this.node.getBoundsInParent().getHeight() ) {
              isDead=true;
          	  return true;
                  
        }
		return false;
    }
	
	public Circle getAsCircle() {
		sphere.setTranslateX(node.getTranslateX()+node.getBoundsInLocal().getWidth()/2);
		sphere.setTranslateY(node.getTranslateY());
        return sphere;
    }
	
	public void handleDeath(GameWorld gm){
		//System.out.print("T");
		
		noImplode(gm);
		
		super.handleDeath(gm);
	}
	
	
	
		//wybuch
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void implode(final GameWorld gameWorld) {
        vX = vY = 0;
        FadeTransitionBuilder.create()
            .node(node)
            .duration(Duration.millis(300))
            .fromValue(node.getOpacity())
            .toValue(0)
            .onFinished(new EventHandler() {
                //@Override
                public void handle(Event arg0) {
                    isDead = true;
                    gameWorld.getSceneElements().getChildren().remove(node);
                }

				            })
            .build()
            .play();
    }

}
