package pl.agit.Game;

import pl.agit.Game.World.AsteroidDemolition;
import pl.agit.Game.World.GameWorld;
import pl.agit.Game.World.GUIElements.MainMenu;
import javafx.application.Application;
import javafx.stage.Stage;

//glowna petla gry
public class MainLoop extends Application {

	GameWorld gameWorld = new AsteroidDemolition(60, "Asteroids - Game Loop");
	MainMenu mm = new MainMenu();

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		// ustawia aktorow, scene, tytul itp
		mm.initialize(primaryStage, gameWorld);

		// gameWorld.initialize(primaryStage);

		// petla glowna
		// gameWorld.beginGameLoop();

		// wyswietla okno
		primaryStage.show();

	}

}
