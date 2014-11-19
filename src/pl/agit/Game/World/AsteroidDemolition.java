package pl.agit.Game.World;

import java.io.File;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import pl.agit.Game.Sprites.Sprite;
import pl.agit.Game.Sprites.SpriteManager;
import pl.agit.Game.Sprites.Characters.Asteroid;
import pl.agit.Game.Sprites.Characters.Missile;
import pl.agit.Game.Sprites.Characters.SpaceShip;
import pl.agit.Game.World.GUIElements.GameStats;

public class AsteroidDemolition extends GameWorld {
	
    
    private GameStats gameStats;
    
    private int asteroidCount = 0;
    private int missileCount = 0;
    Image backgroundImage = new Image("/GameGfxFiles/space2.jpg");
    ImageView backView = new ImageView(backgroundImage);
    Group g = new Group();
    
    private SpaceShip ship = new SpaceShip();
 
    public AsteroidDemolition(int fps, String title){
        super(fps, title);
    }
    
    
    @Override
    public void initialize(final Stage primaryStage) {
      //backView.setD
        primaryStage.setTitle(getWindowTitle()); //tytul okna
        g.getChildren().add(backView);
        Node node = g;
        node.toBack();
        gameStats = new GameStats(1050, 650);
 
        // tworzenie sceny
        setSceneElements(new Group());
        Scene sc = new Scene(getSceneElements(), 1050, 650);
        setGameScene(sc);
        String cssPath ="/pl/agit/css/scene.css";
       // getSceneElements().getChildren().add(0,backView);
        getGameScene().getStylesheets().addAll(cssPath);
        primaryStage.setScene(getGameScene());
        
        Sprite[] s = {ship};
        getSpriteManager().addSprites(s);
        getSceneElements().getChildren().add(0,node);
        getSceneElements().getChildren().add(ship.node);
       
        // tworzenie asteroids
        //generateManySpheres(150);
        generateAsteroids();
  
        // Display the number of spheres visible.
        // Create a button to add more spheres.
        // Create a button to freeze the game loop.
        final Timeline gameLoop = getGameLoop();
        
       
        getSceneElements().getChildren().add(gameStats.getStats());
        
        
        
        setupInput(primaryStage);
        
        String ssound = "D:\\Studia\\przedmioty2_2\\AGiT\\proj\\JavaProject\\bin\\GameGfxFiles\\m1.mp3";
	    Media sound = new Media(new File("D:\\Studia\\przedmioty2_2\\AGiT\\proj\\JavaProject\\bin\\GameGfxFiles\\m1.mp3").toURI().toString());
	    MediaPlayer mediaPlayer = new MediaPlayer(sound);
	    mediaPlayer.play();
        
    }
    
    
    
    public void reductionMissile(){
    	missileCount--;
    }
    
    private void setupInput(final Stage primaryStage){
    	
    	
    	final Delta d = new Delta();
    	double dy;
    	
    	//samo poruszanie
    	EventHandler moveMouseEv = new EventHandler<MouseEvent>(){
			@Override public void handle(MouseEvent event) {
				ship.setMpozX(event.getX());
				ship.setMpozY(event.getY());
				
				gameStats.updateMousePos(event.getX(), event.getY());
			}
			
		};
		
		//nacisniecie przycisku myszy
		EventHandler moveOrFireMouseEv = new EventHandler<MouseEvent>(){
			@Override public void handle(MouseEvent event) {
				if(event.getButton()==MouseButton.PRIMARY){
					
					Missile m = ship.fire();
					
					Sprite[] s = {m};
		            getSpriteManager().addSprites(s);
		            
		            // dodanie pocisku do listy spritow 
		            getSceneElements().getChildren().add( m.node);
					
				d.x = primaryStage.getX() - event.getScreenX();
				d.y = primaryStage.getY() - event.getScreenY();
				missileCount++;
				
				}
			}
			
		};
		
		//strzelanie i poruszanie
		EventHandler movedragOrFireMouseEv = new EventHandler<MouseEvent>(){
			@Override public void handle(MouseEvent event) {
				ship.setMpozX(event.getX());
				ship.setMpozY(event.getY());
				
				gameStats.updateMousePos(event.getX(), event.getY());}
			
			
		};
		
		primaryStage.getScene().setOnMouseMoved(moveMouseEv);
		//primaryStage.getScene().setOnMouseClicked(moveMouseEv);
		primaryStage.getScene().setOnMousePressed(moveOrFireMouseEv);
	    primaryStage.getScene().setOnMouseDragged(movedragOrFireMouseEv);
    	
    }
 
    //generowanie asteroid
    private void generateAsteroids(){
    	Random random = new Random();
    	int anumb = random.nextInt(9)+1;
    	asteroidCount = asteroidCount+anumb;
    	Scene scene= getGameScene();
    	for(int i=0;i<anumb;i++){
    		
    		//Asteroid ast = new Asteroid(7,LinearGradient.valueOf("linear-gradient(from 0% 0% to 100% 100%, red  70% , blue 5%,  black 10%)"));
    		Asteroid ast = new Asteroid(10,new RadialGradient(0,.1,3,3,4,false,CycleMethod.REFLECT,new Stop(0, Color.RED),new Stop(1, Color.BLACK)));
    		Circle circle = ast.getAsCircle();
    		ast.vX=0;
    		ast.vY=random.nextDouble()+2;
    		double newX = random.nextInt((int) (scene.getWidth()-circle.getRadius()));
    		double newY = 100;
    		
    		circle.setTranslateX(newX);
            circle.setTranslateY(newY);
            circle.setVisible(true);
            circle.setId(ast.toString());
            
            Sprite[] s = {ast};
            getSpriteManager().addSprites(s);
            ast.node.toFront();
            // add sprite's 
            getSceneElements().getChildren().add(ast.node);
    	}
    	
    }
    
    private void generateManySpheres(int numSpheres) {
        Random rnd = new Random();
        Scene gameSurface = getGameScene();
        for (int i=0; i<numSpheres; i++) {
            Color c = Color.rgb(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
            Asteroid b = new Asteroid(rnd.nextInt(15) + 5, LinearGradient.valueOf("linear-gradient(from 0% 0% to 100% 100%, red  0% , blue 30%,  black 100%)"));
            Circle circle = b.getAsCircle();
            // random 0 to 2 + (.0 to 1) * random (1 or -1)
            b.vX = (rnd.nextInt(2) + rnd.nextDouble()) * (rnd.nextBoolean() ? 1 : -1);
            b.vY = (rnd.nextInt(2) + rnd.nextDouble()) * (rnd.nextBoolean() ? 1 : -1);

            // random x between 0 to width of scene
            double newX = rnd.nextInt((int) gameSurface.getWidth());
            
            // check for the right of the width newX is greater than width 
            // minus radius times 2(width of sprite)
            if (newX > (gameSurface.getWidth() - (circle.getRadius() * 2))) {
                newX = gameSurface.getWidth() - (circle.getRadius()  * 2);
            }
            
            // check for the bottom of screen the height newY is greater than height 
            // minus radius times 2(height of sprite)
            double newY = rnd.nextInt((int) gameSurface.getHeight());
            if (newY > (gameSurface.getHeight() - (circle.getRadius() * 2))) {
                newY = gameSurface.getHeight() - (circle.getRadius() * 2);
            }
            
            circle.setTranslateX(newX);
            circle.setTranslateY(newY);
            circle.setVisible(true);
            circle.setId(b.toString());
            
            // add to actors in play (sprite objects)
            Sprite[] s = {b};
            getSpriteManager().addSprites(s);
            
            // add sprite's 
            getSceneElements().getChildren().add(0, b.node);
            
        }
    }
 
    //uaktualnianie aktorow
    @Override
    protected void handleUpdate(Sprite sprite) {
    	
    	//int asterDel=0;
        if (sprite instanceof Asteroid) {
            Asteroid sphere = (Asteroid) sprite;
            
                 if(sphere.handleBoundsMeet(getGameScene().getWidth(), getGameScene().getHeight()-60))        	  
                	 ((Asteroid) sprite).handleDeath(this);
              	
        }
        
        if(sprite instanceof Missile){
        	 Missile sphere = (Missile) sprite;
        	 if(sphere.handleBoundsMeet(getGameScene().getWidth(), getGameScene().getHeight()))        	  
            	 
              	((Missile) sprite).handleDeath(this);
            
            }
       if(sprite instanceof SpaceShip){
    	   SpaceShip sphere = (SpaceShip) sprite;
    	   if(sphere.handleBoundsMeet(getGameScene().getWidth(), getGameScene().getHeight()-60)){
    		   //sphere.stop();
    		   //return;
    	   }
       }
       
       sprite.update();
    }
    
    @Override
    protected void respawnElements(){
    	 asteroidCount = SpriteManager.getCountAsteroids();
    	 if(asteroidCount==0) generateAsteroids(); //dodawanie asteroid
    	 
    }
 
    //kolizje dwoch obiektow
    @Override
    protected boolean handleCollision(Sprite spriteA, Sprite spriteB) {
    	if (spriteA != spriteB) {
    		
    		        if (spriteA.collide(spriteB)) {
    		           //if (spriteA != ship) {
    		                spriteA.handleDeath(this);
    		            //}
    		            if (spriteB != ship ) {
    		                spriteB.handleDeath(this);
    		           }
    		            if (spriteB == ship ) {
    		                ship.reducteEnergy(spriteA.getDamage());
    		           }
    		        }
    		    }
 	
    	return false;
    }
 
   //usuwanie zdechlych rzeczy z planszy
    @Override
    protected void cleanupSprites() {
        // removes from the scene and backend store
        super.cleanupSprites();
     
        gameStats.updateAsteroidCounter(asteroidCount);
        gameStats.updateMissileCounter( missileCount);
        gameStats.updateSpaceEnergyCounter(ship.getEnergy());
        
    }

    private class Delta {double x; double y;};
}
