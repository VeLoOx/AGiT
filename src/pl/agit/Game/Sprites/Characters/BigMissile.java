package pl.agit.Game.Sprites.Characters;

import javafx.scene.paint.Paint;
import pl.agit.Game.World.GameWorld;

public class BigMissile extends Missile {
	
	
	

	public BigMissile(double radius, double x, double y, double vX, double vY) {
		super(radius, x, y, vX, vY);
		
		bullet.setFill(Paint.valueOf("red"));
		// TODO Auto-generated constructor stub
	}
	
public void handleDeath(GameWorld gm){
	if (this.node.getTranslateY() <-this.node.getBoundsInParent().getHeight() ){
		
		super.handleDeath(gm);
		noImplode(gm);
	}
		//noImplode(gm);
		//((AsteroidDemolition)gm).reductionMissile();
		//((AsteroidDemolition)gm).addScore(100);
		
		
	}

public boolean handleBoundsMeet(double wx, double hy){
	if (this.node.getTranslateX() > wx) vX = -vX;
	if (this.node.getTranslateX() < 0) vX = -vX;
	
	if (this.node.getTranslateY() <-this.node.getBoundsInParent().getHeight() ) {
          
      	  return true;
              
    }
	return false;
}

@Override
public void update() {
	// TODO Auto-generated method stub
	node.setTranslateY(node.getTranslateY()-vY);
	node.setTranslateX(node.getTranslateX()+vX);
	
}

}
