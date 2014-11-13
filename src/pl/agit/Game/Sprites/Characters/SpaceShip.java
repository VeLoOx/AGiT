package pl.agit.Game.Sprites.Characters;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import pl.agit.Game.Sprites.Sprite;

public class SpaceShip extends Sprite{
	
	//milisekundy na klatke
	private final static float MILLIS_PER_FRAME = 3000;
	
	private final static float SPEED = 3.3f;
	
	private final static float MISSILE_SPEED = 5.3f;
	
	private double mpozX = 500;
	private double mpozY = 500;
	
	/**
     * A group contain all of the ship image view nodes.
     */
    private final Group shipBook = new Group();
    
    private KeyCode keyCode;
    
    public SpaceShip(){
    	
    	Image shipImage = new Image("/GameGfxFiles/ship.png");
    	ImageView shipView = new ImageView(shipImage);
    	shipBook.getChildren().addAll(shipView);
    	
    	shipBook.setTranslateX(100);
    	shipBook.setTranslateY(100);
    	node = shipBook;
    	
    }

	@Override
	public void update() {
		// TODO Auto-generated method stub
		//EventHandler<MouseEvent> ev = getMouseMoveEvent();
		//System.out.print("T");
		shipBook.setTranslateX(mpozX);
		shipBook.setTranslateY(mpozY);
		
	}
	
	public Missile fire(){
		
		Missile m = new Missile(10,mpozX+shipBook.getChildren().get(0).getBoundsInLocal().getWidth()/2,mpozY,0,4);
		
		return m;
		
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

	/*public static void main(String[] args){
		
		
		
	}*/

}
