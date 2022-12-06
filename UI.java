/* Author: Caitlin Wong, Jake Gridley, Gang Shi, Ariel Garcia
 * 
 */
package finalAssignment;

import java.io.File;

import java.util.ArrayList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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

	// UI Layout
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

	// Song & Playlist Buttons
	private Button runUserPlayListButton = new Button("Play User's Library");
	private Button playSelectedButton = new Button("Play Selected");
	private Button addSelectedButton = new Button("Add Selected");
	private Song currentSong;

	// Sliders
	private Slider volume = new Slider(0, 100, 100);
	private Slider seekBar = new Slider();
	private Label vol = new Label("Volume");
	private Label songLen = new Label("Seek Bar");

	// Status Labels
	private Label status = new Label("Song Status: Not Playing");
	private Label currentSelectedSong = new Label("No Song Selected");

	// Center Panel
	private VBox songData = new VBox();
	private Image curImage;
	private ImageView imView = new ImageView();

	// Menu Bar
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

	private String songPath = new String("C:\\Users\\caitl\\Documents\\UA\\Computer Science\\CS 335"
			+ "\\finalAssignment\\src\\Songs\\STAR WALKIN'.mp3");
	private Media media = new Media(new File(songPath).toURI().toString());
	private MediaPlayer mediaPlayer = new MediaPlayer(media);

	private int Index = 0;

	@SuppressWarnings("exports")
	@Override
	public void start(Stage s) {
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

	/*
	 * Controls the seek bar
	 */
	private void seekBarControls() {
		// updates the seek bar when it is pressed
		seekBar.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable ov) {
				if (seekBar.isPressed()) {
					mediaPlayer.seek(mediaPlayer.getMedia().getDuration().multiply(seekBar.getValue() / 100));
				}
			}
		});

		// allows the seek bar to continuousy track the song
		mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable ov) {
				updatesValues();
			}
		});

	}

	/*
	 * Controls the volume
	 */
	private void volumeControl() {
		// updates the volume when the slider is pressed
		volume.valueProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable ov) {
				if (volume.isPressed()) {
					mediaPlayer.setVolume(volume.getValue() / 100);
				}
			}
		});
	}

	private void updatesValues() {
		Platform.runLater(new Runnable() {
			public void run() {
				// Updates to the new time value
				seekBar.setValue(
						mediaPlayer.getCurrentTime().toMillis() / mediaPlayer.getTotalDuration().toMillis() * 100);
			}
		});
	}

	private void setUp() {
		userLib.setPadding(new Insets(10));
		userLib.setPrefSize(225, 800);
		mainLib.setPadding(new Insets(10));
		mainLib.setPrefSize(225, 800);
		Label userLabel = new Label("User's Library is empty");
		Label mainLibraryLabel = new Label("Main Library");

		// adds labels to libraries
		userLib.getChildren().add(userLabel);
		mainLib.getChildren().add(mainLibraryLabel);

		// displays Library
		mainLib.getChildren().add(mainListView);

		// song data and image with status
		songData.getChildren().add(status);
		imView.setImage(curImage);
		imView.setFitWidth(300);
		imView.setPreserveRatio(true);
		imView.setSmooth(true);
		imView.setCache(true);
		songData.getChildren().add(imView);
		songData.setAlignment(Pos.TOP_CENTER);
		userLib.getChildren().add(userListView);

		// adds different panes to main pane
		bPane.setLeft(userLib);
		bPane.setRight(mainLib);
		bPane.setBottom(controlPanel);
		bPane.setCenter(songData);
		bPane.setTop(menuBar);

	}

	/*
	 * Loops through user's library and plays the song in that library It restarts
	 * the loop if it reaches the end of the list
	 */
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

		playButton.setPrefSize(100, 20);
		prevButton.setPrefSize(100, 20);
		skipButton.setPrefSize(100, 20);
		pauseButton.setPrefSize(100, 20);
		currentSelectedSong.setPrefSize(150, 20);

		// adds buttons and sliders to the gridPane
		controlPanel.add(prevButton, 0, 0);
		controlPanel.add(playButton, 1, 0);
		controlPanel.add(pauseButton, 2, 0);
		controlPanel.add(skipButton, 3, 0);
		controlPanel.add(seekBar, 0, 1, 2, 1);
		controlPanel.add(volume, 3, 1, 1, 1);
		controlPanel.add(vol, 3, 2);
		controlPanel.add(songLen, 0, 2);
		controlPanel.add(playSelectedButton, 12, 2);
		controlPanel.add(addSelectedButton, 12, 1);
		controlPanel.add(currentSelectedSong, 12, 0);
		controlPanel.add(runUserPlayListButton, 2, 1);
	}

	/*
	 * Top Bar control panel
	 */
	private void menuControl() {
		menuBar.setGridLinesVisible(false);
		menuBar.setHgap(30);
		menuBar.setVgap(10);
		menuBar.setPadding(new Insets(1, 12, 15, 12));

		// Theme drop down menu
		String themes[] = { "Red", "Yellow", "Green", "Blue", "Default" };
		themeList.setItems(FXCollections.observableArrayList(themes));
		themeList.setPrefSize(100, 20);
		searchBar.setPrefSize(200, 20);

		// Sorting Drop down menu
		String sorting[] = { "Title", "Artist", "Release Date" };
		sortOrder.setItems(FXCollections.observableArrayList(sorting));

		// adds elements to grid pane
		menuBar.add(colorScheme, 0, 0);
		menuBar.add(themeList, 1, 0);
		menuBar.add(sortBy, 2, 0);
		menuBar.add(sortOrder, 3, 0);
		menuBar.add(searchLabel, 4, 0);
		menuBar.add(searchBar, 5, 0);
	}

	/*
	 * Updates the User's library when the add selected button is pressed
	 */
	private void updateUserLib() {
		ArrayList<Song> songs = userLibSongs.getSongs();
		userListView = new ListView<Song>();
		for (Song s : songs) {
			userListView.getItems().add(s);
		}
		Label label = new Label(userLibSongs.getName());
		Label songCount = new Label("Total Song Count: " + userLibSongs.getSongs().size());
		userLib.getChildren().clear();
		userLib.getChildren().add(label);
		userLib.getChildren().add(songCount);
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

	/*
	 * Handles events when the run playlist button is pressed
	 */
	EventHandler<ActionEvent> runPlayListButtonEvent = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e) {
			Index = 0;
			runPlaylist();
		}
	};

	/*
	 * Handles events when the skip button is pressed
	 */
	EventHandler<ActionEvent> skipButtonEvent = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e) {
			mediaPlayer.stop();
			Index = (Index + 1) % userLibSongs.getSongs().size();
			runPlaylist();
		}
	};

	/*
	 * Plays a song
	 */
	private void playSong(Song s) {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}
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
			curImage = new Image(imagLoc, 300, 300, false, false);
			songData.getChildren().clear();
			imView = new ImageView(curImage);
			songData.getChildren().add(status);
			songData.getChildren().add(imView);
		}
		seekBarControls();
		volumeControl();
	}

	/*
	 * Handles events when the add selected button is pressed
	 */
	EventHandler<ActionEvent> addSelectedButtonEvent = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e) {
			if (!userLibSongs.contains(currentSong)) {
				userLibSongs.addSong(currentSong);
				System.out.println("Song - " + currentSong + " - added");
			} else {
				System.out.println("Song - " + currentSong + " - not added (already exists in library)");
			}
			updateUserLib();
		}
	};

	/*
	 * Updates the main library when the search bar is used
	 */
	private void updateMainLib() {
		mainLib.getChildren().clear();
		mainListView = new ListView<Song>();
		ArrayList<Song> mainSongs = mainLibSongs.getSongs();
		for (Song s1 : mainSongs) {
			mainListView.getItems().add(s1);
		}
		Label label = new Label(mainLibSongs.getName());
		Label searchResult = new Label("Search Results Number: " + mainSongs.size());
		mainLib.getChildren().add(label);
		mainLib.getChildren().add(searchResult);
		mainLib.getChildren().add(mainListView);
		mainListView.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				currentSong = mainListView.getSelectionModel().getSelectedItem();
				currentSelectedSong.setText(currentSong.toString());
			}
		});
	}

	/*
	 * Handles events when the play selected button is pressed
	 */
	EventHandler<ActionEvent> playSelectedButtonEvent = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e) {
			playSong(currentSong);
		}
	};

	/*
	 * Handles events when the search bar is used
	 */
	EventHandler<ActionEvent> searchBarEvent = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e) {
			String toSearch = searchBar.getText();
			mainLibSongs.clear();

			// Calls the api to find the song
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

	/*
	 * Handles events when a sort function is chosen
	 */
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

	/*
	 * Handles events when a theme is chosen
	 */
	EventHandler<ActionEvent> themeColorEvent = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e) {
			if (themeList.getValue().contentEquals("Red")) {
				clearColor();
				redTheme();
				textColor();
			} else if (themeList.getValue().contentEquals("Yellow")) {
				clearColor();
				yellowTheme();

			} else if (themeList.getValue().contentEquals("Green")) {
				clearColor();
				greenTheme();
				textColor();
			} else if (themeList.getValue().contentEquals("Blue")) {
				clearColor();
				blueTheme();
				textColor();

			} else {
				clearColor();
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
		colorScheme.setStyle(null);
		sortBy.setStyle(null);
		currentSelectedSong.setStyle(null);
		searchLabel.setStyle(null);
		vol.setStyle(null);
		songLen.setStyle(null);
	}

	/*
	 * Changes the text color
	 */
	private void textColor() {
		colorScheme.setStyle("-fx-text-fill: white");
		sortBy.setStyle("-fx-text-fill: white");
		currentSelectedSong.setStyle("-fx-text-fill: white");
		searchLabel.setStyle("-fx-text-fill: white");
		vol.setStyle("-fx-text-fill: white");
		songLen.setStyle("-fx-text-fill: white");
	}

	/*
	 * Changes the layout's background to red
	 */
	private void redTheme() {
		menuBar.setStyle("-fx-background-color: crimson");
		bPane.setStyle("-fx-background-color: palevioletred");
		controlPanel.setStyle("-fx-background-color: crimson");

		searchBar.setStyle("-fx-background-color: pink");
		themeList.setStyle("-fx-background-color: pink");
		sortOrder.setStyle("-fx-background-color: pink");

		playButton.setStyle("-fx-background-color: lightpink");
		pauseButton.setStyle("-fx-background-color: lightpink");
		prevButton.setStyle("-fx-background-color: lightpink");
		skipButton.setStyle("-fx-background-color: lightpink");

		runUserPlayListButton.setStyle("-fx-background-color: lightpink");
		playSelectedButton.setStyle("-fx-background-color: lightpink");
		addSelectedButton.setStyle("-fx-background-color: lightpink");

		mainLib.setStyle("-fx-background-color: palevioletred");
		userLib.setStyle("-fx-background-color: palevioletred");
	}

	/*
	 * Changes the layout's background to yellow
	 */
	private void yellowTheme() {
		menuBar.setStyle("-fx-background-color: goldenrod");
		bPane.setStyle("-fx-background-color: gold");
		controlPanel.setStyle("-fx-background-color: goldenrod");

		searchBar.setStyle("-fx-background-color: yellow");
		themeList.setStyle("-fx-background-color: yellow");
		sortOrder.setStyle("-fx-background-color: yellow");

		playButton.setStyle("-fx-background-color: yellow");
		pauseButton.setStyle("-fx-background-color: yellow");
		prevButton.setStyle("-fx-background-color: yellow");
		skipButton.setStyle("-fx-background-color: yellow");

		runUserPlayListButton.setStyle("-fx-background-color: yellow");
		playSelectedButton.setStyle("-fx-background-color: yellow");
		addSelectedButton.setStyle("-fx-background-color: yellow");

		mainLib.setStyle("-fx-background-color: gold");
		userLib.setStyle("-fx-background-color: gold");
	}

	/*
	 * Changes the layout's background to blue
	 */
	private void greenTheme() {
		menuBar.setStyle("-fx-background-color: green");
		bPane.setStyle("-fx-background-color: darkseagreen");
		controlPanel.setStyle("-fx-background-color: green");

		searchBar.setStyle("-fx-background-color: palegreen");
		themeList.setStyle("-fx-background-color: palegreen");
		sortOrder.setStyle("-fx-background-color: palegreen");

		playButton.setStyle("-fx-background-color: palegreen");
		pauseButton.setStyle("-fx-background-color: palegreen");
		prevButton.setStyle("-fx-background-color: palegreen");
		skipButton.setStyle("-fx-background-color: palegreen");

		runUserPlayListButton.setStyle("-fx-background-color: palegreen");
		playSelectedButton.setStyle("-fx-background-color: palegreen");
		addSelectedButton.setStyle("-fx-background-color: palegreen");

		mainLib.setStyle("-fx-background-color: darkseagreen");
		userLib.setStyle("-fx-background-color: darkseagreen");
	}

	/*
	 * Changes the layout's background to blue
	 */
	private void blueTheme() {
		menuBar.setStyle("-fx-background-color: dodgerblue");
		bPane.setStyle("-fx-background-color: lightskyblue");
		controlPanel.setStyle("-fx-background-color: dodgerblue");

		searchBar.setStyle("-fx-background-color: skyblue");
		themeList.setStyle("-fx-background-color: skyblue");
		sortOrder.setStyle("-fx-background-color: skyblue");

		playButton.setStyle("-fx-background-color: lightblue");
		pauseButton.setStyle("-fx-background-color: lightblue");
		prevButton.setStyle("-fx-background-color: lightblue");
		skipButton.setStyle("-fx-background-color: lightblue");

		runUserPlayListButton.setStyle("-fx-background-color: lightblue");
		playSelectedButton.setStyle("-fx-background-color: lightblue");
		addSelectedButton.setStyle("-fx-background-color: lightblue");

		mainLib.setStyle("-fx-background-color: lightskyblue");
		userLib.setStyle("-fx-background-color: lightskyblue");
	}
}