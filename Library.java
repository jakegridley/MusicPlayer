package finalAssignment;

import java.util.ArrayList;
import java.util.Comparator;

public class Library {

	private String name;
	private ArrayList<Song> songs;
	private String sortby;
	
	
	public Library() {
		
		this.songs = new ArrayList<Song>();
		this.name = "Main Library";
		this.sortby = "Name";
		
	}
	
	public Library(String name) {
		
		this.name = name;
		this.songs = new ArrayList<Song>();
		this.sortby = "Name";
		
	}
	
	public ArrayList<Song> getSongs() {
		return this.songs;
	}
	
	public void setSortby(String s) {
		this.sortby = s;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void addSong(Song s) {
		songs.add(s);
		
		if (this.sortby.equals("Name")) {
			this.sortByName();
		} else if (this.sortby.equals("Artist")) {
			this.sortByArtist();
		} else if (this.sortby.equals("Date")) {
			this.sortByDate();
		} else {
			System.out.println("INCORRECT SORTBY VALUE\n *INSTEAD SORTED BY NAME");
			this.sortByName();
		}
		
		
	}
	
	
	public void clear() {
		songs = new ArrayList<Song>();
	}
	
	public void sortByName() {
		songs.sort(Comparator.comparing(Song::getName));
	}
	
	public void sortByArtist() {
		songs.sort(Comparator.comparing(Song::getArtist));
	}
	
	public void sortByDate() {
		songs.sort(Comparator.comparing(Song::getReleaseDate));
	}

	public boolean contains(Song currentSong) {
		return songs.contains(currentSong);
	}
	
	
	
	
}