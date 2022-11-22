/* Author: Caitlin Wong 
 * 
 */


package Assignment.components;

import java.io.File;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class UI extends Application {

	private BorderPane bPane = new BorderPane();
	private ScrollPane userPane = new ScrollPane();
	private ScrollPane mainPane = new ScrollPane();
	private GridPane controlPanel = new GridPane();
	private Button playButton = new Button("Play");
	private Button pauseButton = new Button("Pause");
	private Button prevButton = new Button("Prev");
	private Button skipButton = new Button("Skip");
	private Slider time = new Slider(); 
    private Slider volume = new Slider(0,100,100);
	private Label status = new Label("Song status: Playing");
	private String songPath = new String("C:\\Users\\caitl\\Documents\\UA\\Computer Science\\"
			+ "CS 335\\components\\src\\main\\java\\Assignment\\components\\STAR WALKIN'.mp3");
	private Media media = new Media(new File(songPath).toURI().toString());
	private MediaPlayer mediaPlayer = new MediaPlayer(media);

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
		
		
		mediaPlayer.play(); // starts playing song
		mediaPlayer.setVolume(0.2); // initial volume setting
		
		bPane.setLeft(userPane);
		bPane.setRight(mainPane);
		bPane.setBottom(controlPanel);
		bPane.setCenter(status);
		
		
		
		// Volume control
		volume.valueProperty().addListener(new InvalidationListener() { 
	        public void invalidated(Observable ov) 
	        { 
	            if (volume.isPressed()) { 
	               mediaPlayer.setVolume(volume.getValue()/100); // It would set the volume 
	                // as specified by user by pressing 
	            }
	        }
	    });
		
		
		
	
		
		
		// when button is pressed
		playButton.setOnAction(playButtonEvent);
		pauseButton.setOnAction(pauseButtonEvent);
		prevButton.setOnAction(prevButtonEvent);
		skipButton.setOnAction(skipButtonEvent);

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

	/*
	 * Sets up and places the buttons and sliders that control the songs
	 */
	private void songControl() {
		// Spacing for the buttons and sliders
		controlPanel.setGridLinesVisible(false);
		controlPanel.setHgap(10);
		controlPanel.setVgap(10);
		controlPanel.setPadding(new Insets(1, 12, 15, 12));
		controlPanel.setAlignment(Pos.BOTTOM_CENTER);
		
	    Label vol = new Label("Volume");
	    Label songLen = new Label("Song Len");
        
		

		// sets button size
		playButton.setPrefSize(100, 20);
		prevButton.setPrefSize(100, 20);
		skipButton.setPrefSize(100, 20);
		pauseButton.setPrefSize(100,20);
		
		
		
		
		//adds buttons and sliders to the gridPane
		controlPanel.add(prevButton, 0, 0);
		controlPanel.add(playButton, 1, 0);
		controlPanel.add(pauseButton, 2, 0);
		controlPanel.add(skipButton, 3, 0);
		controlPanel.add(time, 0, 1, 2, 1);
		controlPanel.add(volume, 3, 1, 1, 1);
		controlPanel.add(vol, 3, 2);
		controlPanel.add(songLen,0,2);
	}
	
	
	/*
	 * Handles events when the play button is pressed
	 */
	EventHandler<ActionEvent> playButtonEvent = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
        	status.setText("Song Status: Playing");
        	mediaPlayer.play();
        	
        }
	};

	/*
	 * Handles events when the previous button is pressed
	 */
	EventHandler<ActionEvent> prevButtonEvent = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
        	status.setText("Song Status: Previous");
        	mediaPlayer.pause();
        	
        }
	};

	
	/*
	 * handles events when the skip button is pressed
	 */
	EventHandler<ActionEvent> skipButtonEvent = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
        	status.setText("Song Status: Next Song");
        	
        	// next song in queue
        	String song = new String("C:\\Users\\caitl\\Documents\\UA\\Computer Science\\"
        			+ "CS 335\\components\\src\\main\\java\\Assignment\\components\\Forest.mp3");
        	Media newSong = new Media(new File(song).toURI().toString());
        	mediaPlayer = new MediaPlayer(newSong);
        	mediaPlayer.play();
        }
	};
	
	/*
	 * handles events when the pause button is pressed
	 */
	EventHandler<ActionEvent> pauseButtonEvent = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
        	status.setText("Song Status: Paused");
        	mediaPlayer.pause();
        }
	};
	
	
	
	
	
	

}