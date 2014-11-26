package pl.agit.Game.World.GUIElements;

import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GameStats {

	private Label asteroidCounter;
	private Label asteroidCounterName;

	private Label missileCounter;
	private Label missileCounterName;

	private Label mousePos;
	private Label mousePosName;
	
	private Label spaceEnergyCounter;
	private Label spaceEnergyCounterName;
	
	private Label spaceScoreCounter;
	private Label spaceScoreCounterName;
	
	private Label spaceLevelCounter;
	private Label spaceLevelCounterName;


	private double targetX;
	private double targetY;
	
	private VBox stats = new VBox();

	public GameStats(double sizeX, double sizeY) {
		asteroidCounter = new Label("0");
		asteroidCounterName = new Label("Asteroids COUNTER:");

		missileCounter = new Label("0");
		missileCounterName = new Label("Missile COUNTER:");

		mousePos = new Label("X = 0 -- Y = 0");
		mousePosName = new Label("Mouse Pos:");
		
		spaceEnergyCounter = new Label("0");
		spaceEnergyCounterName = new Label("Ship ENERGY:");
		
		spaceScoreCounter = new Label("0");
		spaceScoreCounterName = new Label("Ship SCORE:");
		
		spaceLevelCounter = new Label("0");
		spaceLevelCounterName = new Label("Ship LEVEL:");

		asteroidCounter.getStyleClass().add(".ac {-fx-font-size: 30pt;}");

		targetX = 10;
		targetY = sizeY - 50;

	}

	public void updateAsteroidCounter(int val) {

		asteroidCounter.setText(Integer.toString(val));
	}

	public void updateMousePos(double valx, double valy) {

		mousePos.setText("X = " + Double.toString(valx) + " -- Y = "
				+ Double.toString(valy));
	}

	public void updateMissileCounter(int val) {
		missileCounter.setText(Integer.toString(val));
	}
	
	public void updateSpaceEnergyCounter(double val) {

		spaceEnergyCounter.setText(Double.toString(val));
	}
	
	public void updateSpaceScoreCounter(double val) {

		spaceScoreCounter.setText(Double.toString(val));
	}
	
	public void updateSpaceLevelCounter(double val) {

		spaceLevelCounter.setText(Double.toString(val));
	}
	
	public VBox getVBox(){
		return stats;
	}

	public VBox getStats() {

		

		// stats.setBorder(new Border(new BorderStroke));
		// stats.setSpacing(5);
		// stats.setBackground(new Background(new BackgroundFill));
		stats.setTranslateX(20);
		stats.setTranslateY(20);

		HBox hstatus = new HBox();
		hstatus.getStyleClass().add("hbox");
		hstatus.getChildren().add(asteroidCounterName);
		hstatus.getChildren().add(asteroidCounter);
		hstatus.getChildren().add(missileCounterName);
		hstatus.getChildren().add(missileCounter);
		hstatus.getChildren().add(mousePosName);
		hstatus.getChildren().add(mousePos);
		hstatus.getChildren().add(spaceEnergyCounterName);
		hstatus.getChildren().add(spaceEnergyCounter);
		hstatus.getChildren().add(spaceScoreCounterName);
		hstatus.getChildren().add(spaceScoreCounter);
		hstatus.getChildren().add(spaceLevelCounterName);
		hstatus.getChildren().add(spaceLevelCounter);

		stats.getChildren().add(hstatus);

		return stats;

	}

}
