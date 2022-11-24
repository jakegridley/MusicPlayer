/* Author: Caitlin Wong 
 * 
 */


package Assignment.components;

import java.io.File;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class UI extends Application {

	private BorderPane bPane = new BorderPane();
	
	private ScrollPane userPane = new ScrollPane();
	private VBox userLib = new VBox();
	private ScrollPane mainPane = new ScrollPane();
	private VBox mainLib = new VBox();
	
	private GridPane controlPanel = new GridPane();
	private Button playButton = new Button("Play");
	private Button pauseButton = new Button("Pause");
	private Button prevButton = new Button("Prev");
	private Button skipButton = new Button("Skip");
	private Slider time = new Slider(); 
    private Slider volume = new Slider(0,100,100);
    
    private GridPane menuBar = new GridPane();
    private ChoiceBox<String> userList = new ChoiceBox<String>();
    
    
	private Label status = new Label("Song status: Playing \n Star Walkin by Lil Nas X");
	private String songPath = new String("C:\\Users\\caitl\\Documents\\UA\\Computer Science\\"
			+ "CS 335\\components\\src\\main\\java\\Assignment\\components\\STAR WALKIN'.mp3");
	private Media media = new Media(new File(songPath).toURI().toString());
	private MediaPlayer mediaPlayer = new MediaPlayer(media);

	
	
	@SuppressWarnings("exports")
	@Override
	public void start(Stage s) {
		// set title for the stage
		s.setTitle("Music Player");
		
		setUp();
		songControl();	
		menuControl();
		
		
		mediaPlayer.play(); // starts playing song
		mediaPlayer.setVolume(0.2); // initial volume setting
		
		
		
		
		
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
	
	private void setUp() { 
		// sets Scroll Pane size
		mainPane.setPrefSize(200, 400);
		userPane.setPrefSize(200, 400);
		userLib.setPadding(new Insets(10));
		mainLib.setPadding(new Insets(10));

		// create a label
		Label userLabel = new Label("User's Library");
		Label mainLibraryLabel = new Label("Main Library");

		// adds labels to scroll panes
		userLib.getChildren().add(userLabel);
		userPane.setContent(userLabel);
		userPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);

		mainLib.getChildren().add(mainLibraryLabel);
		mainPane.setContent(mainLibraryLabel);
		mainPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		
		
		// adds different panes to main pane
		bPane.setLeft(userPane);
		bPane.setRight(mainPane);
		bPane.setBottom(controlPanel);
		bPane.setCenter(status);
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
	    //Duration length = media.getDuration();
	    //double timeX = length.toSeconds();
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
		
		
		//controlPanel.setStyle("-fx-background-color: #336699;");
	}
	
	
	private void menuControl() { 
		bPane.setTop(userList);
		String one = "BTS";
		String two = "TXT";
		String three = "GOT7";
		userList.setValue(one);
		userList.setValue(two);
		userList.setValue(three);
		
		
		
	}
	
	/*
	 * Handles events when the play button is pressed
	 */
	EventHandler<ActionEvent> playButtonEvent = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
        	status.setText("Song Status: Playing ");
        	mediaPlayer.play();
        	
        }
	};

	/*
	 * Handles events when the previous button is pressed
	 */
	EventHandler<ActionEvent> prevButtonEvent = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
        	status.setText("Song Status: Previous \n Star Walkin by Lil Nas X");
        	mediaPlayer.stop();
        	String song = new String("C:\\Users\\caitl\\Documents\\UA\\Computer Science\\"
        			+ "CS 335\\components\\src\\main\\java\\Assignment\\components\\STAR WALKIN'.mp3");
        	Media media = new Media(new File(song).toURI().toString());
        	mediaPlayer = new MediaPlayer(media);
        	mediaPlayer.play();
        	
        }
	};

	
	/*
	 * handles events when the skip button is pressed
	 */
	EventHandler<ActionEvent> skipButtonEvent = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
        	status.setText("Song Status: Next Song \n Forrest, by Twenty One Pilots");
        	mediaPlayer.stop();
        	
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