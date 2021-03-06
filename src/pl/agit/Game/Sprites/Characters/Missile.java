package pl.agit.Game.Sprites.Characters;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import pl.agit.Game.Sprites.Sprite;
import pl.agit.Game.World.AsteroidDemolition;
import pl.agit.Game.World.GameWorld;

public class Missile extends Sprite {
	
	Circle bullet;
	
	private double damage;
	
public Missile(double radius,double x, double y, double vX, double vY) {
		
		bullet = new Circle();
		bullet.setTranslateX(x);
		bullet.setTranslateY(y);
		bullet.setRadius(radius);
		bullet.setFill(Paint.valueOf("orange"));
		
		this.vX = vX;
		this.vY = vY;
				
		node = bullet;	
		
		damage = 50;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		node.setTranslateY(node.getTranslateY()-vY);
		//node.setTranslateX(node.getTranslateX()+vX);
		
	}
	
	public void handleDeath(GameWorld gm){
		
		
		super.handleDeath(gm);
		noImplode(gm);
		//((AsteroidDemolition)gm).reductionMissile();
		//((AsteroidDemolition)gm).addScore(100);
		
		
	}
	
	public boolean handleBoundsMeet(double wx, double hy){
		//if (this.node.getTranslateX() > this.node.getBoundsInParent().getWidth()) vX = -vX;
		//if (this.node.getTranslateX() < 0) vX = -vX;
		
		if (this.node.getTranslateY() <-this.node.getBoundsInParent().getHeight() ) {
              
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

}
