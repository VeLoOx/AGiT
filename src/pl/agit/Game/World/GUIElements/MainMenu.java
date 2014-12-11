package pl.agit.Game.World.GUIElements;

import java.awt.Event;

import pl.agit.Game.Gamedef.GameConst;
import pl.agit.Game.World.AsteroidDemolition;
import pl.agit.Game.World.GameWorld;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
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
		
		Rectangle backg = new Rectangle(1500,1500);
		menuGroup.getChildren().add(backg);
		
		Label titleGame = new Label("SPACE War");

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
		
		menuGroup.getChildren().add(titleGame);
		titleGame.getStyleClass().add("menuBigText");
		titleGame.setTranslateX(500);
		titleGame.setTranslateY(300);

		menuGroup.getChildren().add(menuVbox);

		menuScene = new Scene(menuGroup);
		menuScene.getStylesheets().addAll(GameConst.CSS_PACKAGE_PATH);
		backg.getStyleClass().add("background");
		
		startB.getStyleClass().add("glass-grey");
		resumeB.getStyleClass().add("glass-grey");
		exitB.getStyleClass().add("glass-grey");

	}

	public void initialize(Stage primaryStage, GameWorld g) {
		primaryStage.setScene(menuScene);

		
		myStage = primaryStage;
		myStage.setFullScreen(true);
		myStage.setFullScreenExitKeyCombination(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
		
		gm = g;

		mm = this;

	}
	
	public Group getMainGroup(){
		
		menuGroup.getChildren().removeAll();
		return menuGroup;
	}
	
	public Scene getScene(){
		return menuScene;
	}

	private EventHandler<ActionEvent> startClick() {

		return new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg) {
				// TODO Auto-generated method stub
				((AsteroidDemolition) gm).resetWorld();
				gm.initialize(myStage, mm);
				//myStage.setScene(gm.getGameScene());
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
				//myStage.setScene(gm.getGameScene());
				//myStage.setFullScreen(true);
				menuScene.setRoot(gm.getSceneElements());
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
		
		menuScene.setCursor(Cursor.DEFAULT);
		//myStage.setScene(menuScene);
		//myStage.getScene().setCursor(Cursor.DEFAULT);
		//myStage.setFullScreen(true);
		//myStage.getScene().setCursor(Cursor.DEFAULT);
		menuScene.setRoot(menuGroup);
		
	}

}
