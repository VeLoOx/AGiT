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

	private double targetX;
	private double targetY;

	public GameStats(int sizeX, int sizeY) {
		asteroidCounter = new Label("0");
		asteroidCounterName = new Label("Asteroids COUNTER:");

		missileCounter = new Label("0");
		missileCounterName = new Label("Missile COUNTER:");

		mousePos = new Label("X = 0 -- Y = 0");
		mousePosName = new Label("Mouse Pos:");

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

	public VBox getStats() {

		VBox stats = new VBox();

		// stats.setBorder(new Border(new BorderStroke));
		// stats.setSpacing(5);
		// stats.setBackground(new Background(new BackgroundFill));
		stats.setTranslateX(targetX);
		stats.setTranslateY(targetY);

		HBox hstatus = new HBox();
		hstatus.getStyleClass().add("hbox");
		hstatus.getChildren().add(asteroidCounterName);
		hstatus.getChildren().add(asteroidCounter);
		hstatus.getChildren().add(missileCounterName);
		hstatus.getChildren().add(missileCounter);
		hstatus.getChildren().add(mousePosName);
		hstatus.getChildren().add(mousePos);

		stats.getChildren().add(hstatus);

		return stats;

	}

}
