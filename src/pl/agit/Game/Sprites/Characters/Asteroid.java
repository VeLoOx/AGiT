package pl.agit.Game.Sprites.Characters;

import javafx.animation.FadeTransitionBuilder;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;
import pl.agit.Game.Sprites.Sprite;
import pl.agit.Game.World.GameWorld;
import javafx.util.Duration;

public class Asteroid extends Sprite {
	
	private Circle asteroid;
	Circle sphere;
	
	private Asteroid(double radius) {
		
		sphere = new Circle();
		sphere.setCenterX(radius);
		sphere.setCenterY(radius);
		sphere.setRadius(radius);
		
		
		
		node = sphere;	
	}
	
	public Asteroid(double radius, RadialGradient color){
		this(radius);
		
		sphere.setFill(color);
		
		node = sphere;	
	}
	
	public Asteroid(double radius, LinearGradient color){
		this(radius);
		
		sphere.setFill(color);
		
		node = sphere;	
	}
	
	public Circle createAsteroid(double radius, LinearGradient color){
		Circle sphere = new Circle();
		sphere.setCenterX(radius);
		sphere.setCenterY(radius);
		sphere.setRadius(radius);
		
		//sphere.setFill(LinearGradient.valueOf("linear-gradient(from 0% 0% to 100% 100%, red  100% , blue 1%,  black 10%)"));
		
		return sphere;
	}

	@Override
	public void update() {
		//System.out.print("A");
		// TODO Auto-generated method stub
		node.setTranslateX(node.getTranslateX() + vX);
        node.setTranslateY(node.getTranslateY() + vY);
	}
	
	@Override
    public boolean collide(Sprite other) {
        if (other instanceof Asteroid) {
            return collide((Asteroid)other);
        }
       return false;
    }
	
	private boolean collide(Asteroid other) {
		 
        // //jesli ukryty to nie koliduje
        if (!node.isVisible() ||
            !other.node.isVisible() ||
            this == other) {
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
	
	public Circle getAsCircle() {
        return (Circle) node;
    }
	
	public void noImplode(final GameWorld gameWorld){
		isDead = true;
		gameWorld.getSceneElements().getChildren().remove(node);
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
