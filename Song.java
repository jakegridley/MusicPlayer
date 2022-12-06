package finalAssignment;

/*
* Author: Jake Gridley
* This class is the song class which contains data each individual song
*/
public class Song {

	private String name;
	private String artist;
	private String imageName;
	private String releaseDate;
	private String location;
	private String displayable;

	/*
	 * Creates a song object with a name, artist, releaseDate, imageName (url), and
	 * .mp3 location (url)
	 */
	public Song(String name, String artist, String releaseDate, String imageName, String location) {

		this.name = name;
		this.artist = artist;
		this.releaseDate = releaseDate;
		this.imageName = imageName;
		this.location = location;
		this.displayable = name + ", " + artist + ", " + releaseDate;

	}

	// returns the String which will be displayed on the UI for the song in the form "Title, Artist, releaseDate"
	public String getDisplayable() {
		return this.displayable;
	}

	// returns the name of song as a string
	public String getName() {
		return this.name;
	}

	// returns the artist name as a string
	public String getArtist() {
		return this.artist;
	}

	// returns the image name as a string
	public String getImageName() {
		return this.imageName;
	}

	// returns the releaseDate as a string 
	public String getReleaseDate() {
		return this.releaseDate;
	}

	// returns the url location of the mp3 as a string
	public String getLocation() {
		return this.location;
	}

	// overwrites toString() method with this returning displayable
	public String toString() {
		return this.displayable;
	}

}