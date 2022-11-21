/* Author: Caitlin Wong 
 * 
 */
package Assignment.components;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class UI extends Application {

	private BorderPane bPane = new BorderPane();
	private ScrollPane userPane = new ScrollPane();
	private ScrollPane mainPane = new ScrollPane();
	private HBox controlPanel = new HBox();
	private Button playButton = new Button("Play");
	private Button prevButton = new Button("Prev");
	private Button skipButton = new Button("Skip");
	private Label status = new Label("Song status: Playing");

	@Override
	public void start(Stage s) {
		// set title for the stage
		s.setTitle("Music Player");

		// sets Scroll Pane size
		mainPane.setPrefSize(200, 400);
		userPane.setPrefSize(200, 400);

		// create a label
		Label userLabel = new Label("User's Library");
		Label mainLibraryLabel = new Label("Main Library");
		

		userPane.setContent(userLabel);
		userPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		
		mainPane.setContent(mainLibraryLabel);
		mainPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		
		
		songControl();	
		
		
		bPane.setLeft(userPane);
		bPane.setRight(mainPane);
		bPane.setBottom(controlPanel);
		bPane.setCenter(status);
		EventHandler<ActionEvent> playButtonHandler = new playButtonHandler();
		EventHandler<ActionEvent> prevButtonHandler = new prevButtonHandler();
		EventHandler<ActionEvent> skipButtonHandler = new skipButtonHandler();

		// when button is pressed
		playButton.setOnAction(playButtonHandler);
		prevButton.setOnAction(prevButtonHandler);
		skipButton.setOnAction(skipButtonHandler);

		// create a scene
		Scene sc = new Scene(bPane, 800, 600);

		// set the scene
		s.setScene(sc);

		s.show();
	}

	public static void main(String args[]) {
		// launch the application
		launch(args);
	}

	
	private void songControl() {
		controlPanel.setPadding(new Insets(15, 12, 15, 12));
		controlPanel.setSpacing(10);

		// sets button size
		playButton.setPrefSize(100, 20);
		prevButton.setPrefSize(100, 20);
		skipButton.setPrefSize(100, 20);
		
		controlPanel.setAlignment(Pos.BOTTOM_CENTER);
		controlPanel.getChildren().addAll(prevButton, playButton, skipButton);
	}
	
	/*
	 * Handles events when the play button is pressed
	 */
	private class playButtonHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			status.setText("Song Status: Stopped");
		}
	};

	/*
	 * Handles events when the previous button is pressed
	 */
	private class prevButtonHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			status.setText("Song Status: Previous");
		}
	};

	
	/*
	 * handles events when the skip button is pressed
	 */
	private class skipButtonHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			status.setText("Song Status: Next");
		}
	};
}