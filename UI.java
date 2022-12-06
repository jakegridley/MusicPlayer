/* Author: Caitlin Wong, Jake Gridley, Gang Shi, Ariel Garcia
 * 
 */
package finalAssignment;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
 * JavaFX App
 */
public class UI extends Application {
	
	

	private BorderPane bPane = new BorderPane();
	private VBox userLib = new VBox();
	private VBox mainLib = new VBox();


	private ListView<Song> mainListView = new ListView<Song>();
	private ListView<Song> userListView = new ListView<Song>();
	
	// Song controls
	private GridPane controlPanel = new GridPane();
	private Button playButton = new Button("Play");
	private Button pauseButton = new Button("Pause");
	private Button prevButton = new Button("Prev");
	private Button skipButton = new Button("Skip");
	
	private Button runUserPlayListButton = new Button("Play User's Library");
	private Button playSelectedButton = new Button("Play Selected");
	private Button addSelectedButton = new Button("Add Selected");
	private Song currentSong;
	
	
    private Slider volume = new Slider(0,100,100);
	private Slider seekBar = new Slider();
	private Label vol = new Label("Volume");
    private Label songLen = new Label("Seek Bar");

	
	private Label status = new Label("Song status: Not Playing");
	private Label currentSelectedSong = new Label("No Song Selected");
	
	
	//trying to get image displayed in center
	private VBox songData = new VBox();
	private Image curImage;
	private ImageView imView = new ImageView();
	
	private Label sortBy = new Label("Sort By:");
    private Label colorScheme = new Label("Color Scheme:");
    private Label searchLabel = new Label("Search For:");

    private GridPane menuBar = new GridPane();
    private TextField searchBar = new TextField("");
    private ComboBox<String> themeList = new ComboBox<String>();
    private ComboBox<String> sortOrder = new ComboBox<String>();
    
    
    // Library of all songs
    private Library mainLibSongs = new Library();
    private Library userLibSongs = new Library("User's Library");


	//private Label status = new Label("Song status: Playing \n Star Walkin by Lil Nas X");
	private String songPath = new String("C:\\Users\\caitl\\Documents\\UA\\Computer Science\\CS 335"
			+ "\\finalAssignment\\src\\Songs\\STAR WALKIN'.mp3");
	private Media media = new Media(new File(songPath).toURI().toString());
	private MediaPlayer mediaPlayer = new MediaPlayer(media);
	
	private int Index = 0;



	@SuppressWarnings("exports")
	@Override
	public void start(Stage s) {
		// set title for the stage
		s.setTitle("Music Player");

		setUp();
		songControl();	
		menuControl();
		seekBarControls();
		volumeControl();
		
		
		// when button is pressed
		playButton.setOnAction(playButtonEvent);
		pauseButton.setOnAction(pauseButtonEvent);
		prevButton.setOnAction(prevButtonEvent);
		skipButton.setOnAction(skipButtonEvent);
		themeList.setOnAction(themeColorEvent);
		sortOrder.setOnAction(sortOrderEvent);
		searchBar.setOnAction(searchBarEvent);
		playSelectedButton.setOnAction(playSelectedButtonEvent);
		addSelectedButton.setOnAction(addSelectedButtonEvent);
		runUserPlayListButton.setOnAction(runPlayListButtonEvent);
		
		
		// create a scene
		Scene sc = new Scene(bPane, 800, 600);
		s.setScene(sc);
		s.show();
	}
	public static void main(String args[]) {
		// launch the application
		launch(args);
	}
	
	
	
	private void seekBarControls() {
		seekBar.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov)
            {
                if (seekBar.isPressed()) { 
                    mediaPlayer.seek(mediaPlayer.getMedia().getDuration().multiply(seekBar.getValue() / 100));
                }
            }
        });
		
		mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov)
            {
                updatesValues();
            }
        });
		
	}
	
	
	private void volumeControl() {
		volume.valueProperty().addListener(new InvalidationListener() { 
	        public void invalidated(Observable ov) 
	        { 
	            if (volume.isPressed()) { 
	               mediaPlayer.setVolume(volume.getValue()/100); 
	            }
	        }
	    });
	}
	
	
	
	protected void updatesValues()
    {
        Platform.runLater(new Runnable() {
            public void run()
            {
                // Updates to the new time value
                seekBar.setValue(mediaPlayer.getCurrentTime().toMillis()/mediaPlayer.getTotalDuration().toMillis()* 100);
            }
        });
    }
	
	
	private void setUp() { 
		// sets Scroll Pane size
		userLib.setPadding(new Insets(10));
		userLib.setPrefSize(225, 800);
		mainLib.setPadding(new Insets(10));
		mainLib.setPrefSize(225, 800);
		// create a label
		Label userLabel = new Label("User's Library is empty");
		Label mainLibraryLabel = new Label("Main Library");


		// adds labels to scroll panes
		userLib.getChildren().add(userLabel);
		mainLib.getChildren().add(mainLibraryLabel);
		
		
		mainLib.getChildren().add(mainListView);
		
		//song data and image with status
		songData.getChildren().add(status);
		imView.setImage(curImage);
		imView.setFitWidth(300);
		imView.setPreserveRatio(true);
		imView.setSmooth(true);
		imView.setCache(true);
		songData.getChildren().add(imView);
		userLib.getChildren().add(userListView);

		// adds different panes to main pane
		bPane.setLeft(userLib);
		bPane.setRight(mainLib);
		bPane.setBottom(controlPanel);
		bPane.setCenter(songData);

	}	
	
	
	private void runPlaylist() {
		ArrayList<Song> songs = userLibSongs.getSongs();
        Song firstSong = songs.get(Index);
        playSong(firstSong);
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                Index = (Index + 1) % userLibSongs.getSongs().size();
                runPlaylist();
            }
        });

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
		
		// sets button size
		playButton.setPrefSize(100, 20);
		prevButton.setPrefSize(100, 20);
		skipButton.setPrefSize(100, 20);
		pauseButton.setPrefSize(100,20);


		currentSelectedSong.setPrefSize(150, 20);

		//adds buttons and sliders to the gridPane
		controlPanel.add(prevButton, 0, 0);
		controlPanel.add(playButton, 1, 0);
		controlPanel.add(pauseButton, 2, 0);
		controlPanel.add(skipButton, 3, 0);
		controlPanel.add(seekBar, 0, 1, 2, 1);
		controlPanel.add(volume, 3, 1, 1, 1);
		controlPanel.add(vol, 3, 2);
		controlPanel.add(songLen,0,2);
		controlPanel.add(playSelectedButton, 12, 2);
		controlPanel.add(addSelectedButton, 12, 1);
		controlPanel.add(currentSelectedSong, 12, 0);
		controlPanel.add(runUserPlayListButton, 2, 1);
		
	}


	private void menuControl() { 
		bPane.setTop(menuBar);
		menuBar.setGridLinesVisible(false);
		menuBar.setHgap(30);
		menuBar.setVgap(10);
		menuBar.setPadding(new Insets(1, 12, 15, 12));
		String st[] = { "Red", "Yellow", "Green", "Blue" };
		themeList.setItems(FXCollections.observableArrayList(st));
		themeList.setPrefSize(100, 20);
		searchBar.setPrefSize(200, 20);
		
		String l[] = {"Title", "Artist", "Release Date"};
		sortOrder.setItems(FXCollections.observableArrayList(l));
		
		menuBar.add(colorScheme, 0, 0);
		menuBar.add(themeList, 1, 0);
		menuBar.add(sortBy, 2, 0);
		menuBar.add(sortOrder, 3, 0);
		menuBar.add(searchLabel, 4, 0);
		menuBar.add(searchBar, 5, 0);
	}
	
	
	private void updateUserLib() {
		ArrayList<Song> songs = userLibSongs.getSongs();
		userListView = new ListView<Song>();
		for (Song s : songs) {
			userListView.getItems().add(s);
		}
		Label label = new Label(userLibSongs.getName());
		userLib.getChildren().clear();
		userLib.getChildren().add(label);
		userLib.getChildren().add(userListView);
		userListView.setOnMouseClicked(new EventHandler<MouseEvent>() {

	        @Override
	        public void handle(MouseEvent event) {
	            currentSong = userListView.getSelectionModel().getSelectedItem();
	            currentSelectedSong.setText(currentSong.toString());
	        }
	    });
		
	}

	/*
	 * Handles events when the play button is pressed
	 */
	EventHandler<ActionEvent> playButtonEvent = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
        	if (mediaPlayer == null) {
                System.out.println("select a song");
            } else {
            	status.setText(currentSong.toString());
                mediaPlayer.play();
                seekBarControls();
            	volumeControl();
            }
        }
	};
	
	
	
	/*
	 * Handles events when the previous button is pressed
	 */
	EventHandler<ActionEvent> prevButtonEvent = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
        	Index = (Index - 1 + userLibSongs.getSongs().size()) % userLibSongs.getSongs().size();
            runPlaylist();
        }
	};
	
	
	EventHandler<ActionEvent> runPlayListButtonEvent = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e) {
			Index = 0;
			runPlaylist();
		}
	};
	
	/*
	 * handles events when the skip button is pressed
	 */
	EventHandler<ActionEvent> skipButtonEvent = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
        	mediaPlayer.stop();
            Index = (Index + 1) % userLibSongs.getSongs().size();
            runPlaylist();
        }
	};
	
	
	private void playSong(Song s) {
        if (mediaPlayer != null) {mediaPlayer.stop();}
        String loc = s.getLocation();
        System.out.println("Location: " + loc);
        if (loc != null) {
            Media newSong = new Media(loc);
            status.setText("Playing: " + s);
            mediaPlayer = new MediaPlayer(newSong);
            mediaPlayer.play();
        }
        String imagLoc = s.getImageName();
        if (imagLoc != null) {
            curImage = new Image(imagLoc,300,300,false,false);
            songData.getChildren().clear();
            imView = new ImageView(curImage);
            songData.getChildren().add(status);
            songData.getChildren().add(imView);
        }
        seekBarControls();
        volumeControl();
    }

	
	
	EventHandler<ActionEvent> addSelectedButtonEvent = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e) {
			if (!userLibSongs.contains(currentSong)) {
				userLibSongs.addSong(currentSong);
				System.out.println("Song -" + currentSong + "- added");
			} else {
				System.out.println("Song - " + currentSong + "- not added (already exists in library)");
			}
			updateUserLib();
		}
	};

	
	private void updateMainLib() {
        mainLib.getChildren().clear();
        mainListView = new ListView<Song>();
        ArrayList<Song> mainSongs = mainLibSongs.getSongs();
        for (Song s1 : mainSongs) {
            mainListView.getItems().add(s1);
        }
        mainLib.getChildren().add(mainListView);
        mainListView.setOnMouseClicked(new EventHandler<MouseEvent>() {

	        @Override
	        public void handle(MouseEvent event) {
	            currentSong = mainListView.getSelectionModel().getSelectedItem();
	            currentSelectedSong.setText(currentSong.toString());
	        }
	    });
	}
        
	
	EventHandler<ActionEvent> playSelectedButtonEvent = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e) {
			playSong(currentSong);
		}
	};
	
	
	  EventHandler<ActionEvent> searchBarEvent = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				String toSearch = searchBar.getText();
				mainLibSongs.clear();
				TestClassSongSearch.search(mainLibSongs, toSearch);
				updateMainLib();
				seekBarControls();
				volumeControl();
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
	
	
	EventHandler<ActionEvent> sortOrderEvent = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e) {
			if (sortOrder.getValue() == "Title") {
				userLibSongs.sortByName();
				updateUserLib();
			} else if (sortOrder.getValue() == "Artist") {
				userLibSongs.sortByArtist();
				updateUserLib();
			} else if (sortOrder.getValue() == "Release Date") {
				userLibSongs.sortByDate();
				updateUserLib();
			}
		}
	};
	
	
	EventHandler<ActionEvent> themeColorEvent = new EventHandler<ActionEvent>() {
	      public void handle(ActionEvent e)
	      {
	          if(themeList.getValue().contentEquals("Red")) {
	        	  clearColor();
	        	  redTheme();
	          }
	          else if (themeList.getValue().contentEquals("Yellow")) { 
	        	  clearColor();
	        	  yellowTheme();
	        	  
	          }else if (themeList.getValue().contentEquals("Green")) { 
	        	  clearColor();
	        	  greenTheme();
	        	  }
	          else { 
	        	  clearColor();
	        	  blueTheme();
	        	  
	          }
	      }
	  };
	  
	  
	  /*
	   * Resets the background color
	   */
	  private void clearColor() {
		  themeList.setStyle(null);
		  sortOrder.setStyle(null);
		  searchBar.setStyle(null);
		  userLib.setStyle(null);
		  mainLib.setStyle(null);
		  controlPanel.setStyle(null);
		  playButton.setStyle(null);
		  pauseButton.setStyle(null);
		  prevButton.setStyle(null);
		  skipButton.setStyle(null);
		  runUserPlayListButton.setStyle(null);
		  playSelectedButton.setStyle(null);
		  addSelectedButton.setStyle(null);
		  menuBar.setStyle(null);
		  bPane.setStyle(null);
	  }
	  
	  private void redTheme() {
		  menuBar.setStyle("-fx-background-color: #dc143c;"); 
    	  searchBar.setStyle("-fx-background-color: #db7093"); 
    	  themeList.setStyle("-fx-background-color: pink"); 
    	  sortOrder.setStyle("-fx-background-color: pink");
    	  bPane.setStyle("-fx-background-color: tomato"); 
    	  controlPanel.setStyle("-fx-background-color: crimson"); 
    	  playButton.setStyle("-fx-background-color: lightpink");
    	  //songLen.setTextFill(Color.rgb(255,255,255));
	  }
	  
	  private void yellowTheme() {
		  menuBar.setStyle("-fx-background-color: #ffff00"); //yellow
    	  searchBar.setStyle("-fx-background-color:  #f5deb3"); //wheat
    	  themeList.setStyle("-fx-background-color: #daa520"); //goldenRod
    	  bPane.setStyle("-fx-background-color: #f0e68c"); //khaki
    	  controlPanel.setStyle("-fx-background-color: #ffd700"); //gold
	  }
	  
	  private void greenTheme() {
		  menuBar.setStyle("-fx-background-color: #008000"); //Green
    	  searchBar.setStyle("-fx-background-color: #66cdaa"); //Medium Aquamarine
    	  themeList.setStyle("-fx-background-color: #00fa9a"); //Medium Spring Green
    	  bPane.setStyle("-fx-background-color: #8fbc8f"); //dark Sea Green
    	  controlPanel.setStyle("-fx-background-color: #90ee90"); //light Green
	  }
	  
	  private void blueTheme() {
		  menuBar.setStyle("-fx-background-color: #191970"); //midnight Blue
    	  searchBar.setStyle("-fx-background-color:  #4682b4"); //steel Blue
    	  themeList.setStyle("-fx-background-color: #6495ed"); //cornflower Blue
    	  bPane.setStyle("-fx-background-color: #1e90ff"); //dodger Blue
    	  controlPanel.setStyle("-fx-background-color: #add8e6"); //light Blue
		  
	  }
}
