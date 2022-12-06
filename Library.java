package finalAssignment;

import java.util.ArrayList;
import java.util.Comparator;

/*
 * Author: Jake Gridley
 * This library class represents a library of songs
 */
public class Library {

	private String name;
	private ArrayList<Song> songs;
	private String sortby;
	
	// If the construtor is not given a name, it is the main library and will be created to 
	// originally be sorted by name
	public Library() {
		this.songs = new ArrayList<Song>();
		this.name = "Main Library";
		this.sortby = "Name";
	}
	
	// Creates a library object with a given name
	public Library(String name) {
		this.name = name;
		this.songs = new ArrayList<Song>();
		this.sortby = "Name";
	}
	
	// returns the ArrayList of songs that this data structure is built with
	public ArrayList<Song> getSongs() {
		return this.songs;
	}
	
	// sets the value that the library is to be sorted by ("Name", "Artist", "Date")
	public void setSortby(String s) {
		this.sortby = s;
	}
	
	// returns the name of the song
	public String getName() {
		return this.name;
	}
	
	// adds a song in correctly sorted order
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
	
	// sorts the Library by name (Title)
	public void sortByName() {
		this.sortby = "Name";
		songs.sort(Comparator.comparing(Song::getName));
	}
	
	// sorts the Library by artist
	public void sortByArtist() {
		this.sortby = "Artist";
		songs.sort(Comparator.comparing(Song::getArtist));
	}
	
	// sorts the library by date
	public void sortByDate() {
		this.sortby = "Date";
		songs.sort(Comparator.comparing(Song::getReleaseDate));
	}
	
	// checks if the library contains a certain song s
	public boolean contains(Song s) {
		if (songs.contains(s)) {
			return true;
		} else {
			return false;
		}
	}
	
	// clears the library's arrayList of songs
	public void clear() {
		songs.clear();
	}
	
}
