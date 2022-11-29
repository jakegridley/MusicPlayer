package MusicPlayer.cs335;

public class Song {

	private String name;
	private String artist;
	private String genre;
	private String imageName;
	private String releaseDate;
	private String location;
	
	
	public Song(String name, String artist, String genre, String releaseDate,
			String imageName, String location) {
		
		this.name = name;
		this.artist = artist;
		this.genre = genre;
		this.releaseDate = releaseDate;
		this.imageName = imageName;
		this.location = location;
	
	}
	
	public String getName() {
		return this.name;
	}

	public String getArtist() {
		return this.artist;
	}
	
	public String getGenre() {
		return this.genre;
	}
	
	public String getImageName() {
		return this.imageName;
	}
	
	
	public String getReleaseDate() {
		return this.releaseDate;
	}
	
	public String getLocation() {
		return this.location;
	}

	
	
}
