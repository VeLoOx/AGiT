package pl.agit.Game.World.GUIElements;

import java.awt.Event;

import pl.agit.Game.World.GameWorld;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu {
	private Scene menuScene;
	private Group menuGroup;
	private VBox menuVbox;
	private MainMenu mm;

	private Button startB;
	private Button resumeB;
	private Button exitB;

	private GameWorld gm = null;
	private Stage myStage;

	public MainMenu() {
		menuGroup = new Group();
		menuVbox = new VBox();

		startB = new Button("Start");
		startB.setPrefSize(200, 200);
		startB.setOnAction(startClick());
		
		resumeB = new Button("Resume");
		resumeB.setPrefSize(200, 200);
		resumeB.setOnAction(resumeClick());
		
		exitB = new Button("Exit");
		exitB.setPrefSize(200, 200);
		exitB.setOnAction(exitClick());

		menuVbox.getChildren().add(startB);
		menuVbox.getChildren().add(resumeB);
		menuVbox.getChildren().add(exitB);
		menuVbox.setTranslateX(100);
		menuVbox.setTranslateY(50);

		menuGroup.getChildren().add(menuVbox);

		menuScene = new Scene(menuGroup);

	}

	public void initialize(Stage primaryStage, GameWorld g) {
		primaryStage.setScene(menuScene);

		
		myStage = primaryStage;
		myStage.setFullScreen(true);
		myStage.setFullScreenExitKeyCombination(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
		
		gm = g;

		mm = this;

	}

	private EventHandler<ActionEvent> startClick() {

		return new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg) {
				// TODO Auto-generated method stub

				gm.initialize(myStage, mm);
				myStage.setScene(gm.getGameScene());
				myStage.setFullScreen(true);
				gm.setupInput(myStage);

				// myStage.
				gm.beginGameLoop();
			}

		};
	}

	private EventHandler<ActionEvent> resumeClick() {

		return new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg) {
				// TODO Auto-generated method stub

				// gm.initialize(myStage);
				myStage.setScene(gm.getGameScene());
				myStage.setFullScreen(true);
				gm.setupInput(myStage);

				// myStage.
				gm.beginGameLoop();
			}

		};
	}

	private EventHandler<ActionEvent> exitClick() {

		return new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg) {
				// TODO Auto-generated method stub

				gm.getGameLoop().stop();
				gm.cleanWorld();
				myStage.close();
				
			}

		};
	}

	public void toMenu() {
		myStage.setScene(menuScene);
		
		menuScene.setCursor(Cursor.DEFAULT);
		myStage.setFullScreen(true);
		
	}

}
