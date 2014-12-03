package pl.agit.Game.Sprites.Characters;

import pl.agit.Game.Sprites.Sprite;
import pl.agit.Game.World.AsteroidDemolition;
import pl.agit.Game.World.GameWorld;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class AlienMissile extends Sprite {
	private Circle bullet;
	
	private double damage;
	
	public AlienMissile(double radius,double x, double y, double vX, double vY){
		bullet = new Circle();
		bullet.setTranslateX(x);
		bullet.setTranslateY(y);
		bullet.setRadius(radius);
		bullet.setFill(Paint.valueOf("green"));
		
		this.vX = vX;
		this.vY = vY;
				
		node = bullet;	
		
		damage = 50;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		node.setTranslateY(node.getTranslateY()+vY);
		
	}
	
	public void handleDeath(GameWorld gm){
		super.handleDeath(gm);
		noImplode(gm);
				
	}
	
	public boolean handleBoundsMeet(double wx, double hy){
		if (this.node.getTranslateY() >hy ) {
              
          	  return true;
                  
        }
		return false;
    }
	
	public Circle getAsCircle() {
        return (Circle) node;
    }
	
	public void noImplode(final GameWorld gameWorld){
		isDead = true;
		gameWorld.getSceneElements().getChildren().remove(node);
	}
	
	public double getDamage(){
		return damage;
	}
	
	@Override
	public boolean collide(Sprite other, GameWorld gm) {
		
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
}
