/* 
 * Author: Ariel Garcia & Gang Shi
 */

package finalAssignment;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;


public class TestClassSongSearch {
	
	public static String startConnection() {

		 HttpResponse<JsonNode> response = Unirest.post("https://accounts.spotify.com/api/token")
		 .header("Content-Type", "application/x-www-form-urlencoded")
		 .header("Authorization", "Basic ZGJiMzFmNDU3ZjU2NGVjY2FlZTIwMmRiMGY1ZjMzODE6MDM2NzczOTJkMDVjNGI2Yjg1M2JhZWE0NTQ0N2UyY2U=")
		 .header("Cookie", "__Host-device_id=AQACoYU4dKunHBGQylIO1PhD_yFKP11nrlsQ5UEdUGYawqmoCs5oXxOpm0JwM7QZ5K8V3WAZOua_SUTnWLPp9vmnU3ZiZMDUB8s; sp_tr=false")
		 .field("grant_type", "client_credentials").asJson();
		 
		 String authToken = "Bearer\n" + response.getBody().getObject().getString("access_token");
		 
		 return authToken;
	}
	
	public static void search(Library libToUpdate, String song) {
		 
		String authToken = startConnection();
		
		String searchLink = "https://api.spotify.com/v1/search?q=track:" + song + "&type=track&include_external=audio&limit=20";

		 
		 HttpResponse<JsonNode> response = Unirest.get(searchLink)
	     .header("Authorization", authToken)
	     .header("Content-Type", "application/json")
	     .asJson();
		 JSONObject toPrint = (JSONObject) response.getBody().getObject().get("tracks");	
		 JSONArray items = (JSONArray) toPrint.get("items");
		 for (Object o : items) {
			 JSONObject jsonLineItem = (JSONObject) o;
			 String key = jsonLineItem.getString("href");
			 authToken = startConnection();
			 HttpResponse<JsonNode> responseSong = Unirest.get(key)
				     .header("Authorization", authToken)
				     .header("Content-Type", "application/json")
				     .asJson();
			 
			 String name;
			 String artist = null;
			 String imageLink;
			 String releaseDate;
			 String mp3Link;
			 
			 ///// SCRIPT FOR GETTING ARTIST /////
			 JSONArray getArtists = (JSONArray) responseSong.getBody().getObject().get("artists");
			for (Object oo : getArtists) {
				 JSONObject jsonLineItem2 = (JSONObject) oo;
				 String key2 = jsonLineItem2.getString("name");
				 artist = key2;
				 break;
			 }
			
			///// Script for getting Song Name/////
			name = song;
			
			///// Script for getting Image on Album /////
			JSONObject getAlbumInfo = (JSONObject) responseSong.getBody().getObject().get("album");
			JSONArray getImages = (JSONArray) getAlbumInfo.getJSONArray("images");
			JSONObject image = (JSONObject) getImages.get(1);
			imageLink = (String) image.get("url");
			
			///// Script for getting release Date string /////
			releaseDate = (String) getAlbumInfo.get("release_date");
			///// Script for getting mp3Link /////
			mp3Link = (String) responseSong.getBody().getObject().get("preview_url");
			
			///// Add song to Main Library /////
			Song songToAdd = new Song(name, artist, releaseDate, imageLink, mp3Link);
			libToUpdate.addSong(songToAdd);
		 }
	} 
}

