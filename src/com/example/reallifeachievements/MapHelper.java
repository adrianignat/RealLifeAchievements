package com.example.reallifeachievements;

import com.example.reallifeachievements.LocationHelper.LocationResult;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.location.Location;

public class MapHelper {	

	Context context;
	GoogleMap map;
	
	LocationResult locationResult = new LocationResult(){
	    @Override
	    public void gotLocation(Location location){
	        drawLocation(location);
	    }		
	};	
	
	public MapHelper(Context context, GoogleMap map) {
		this.context = context;
		this.map = map;
	}
	
	public void getUserLocation(){
		LocationHelper location = new LocationHelper();
		location.getLocation(context, locationResult);		
	}
	
	private void drawLocation(Location location) {
		LatLng userPosition = new LatLng(location.getLatitude(), location.getLongitude());
		
		map.addMarker(new MarkerOptions().position(userPosition)
				.title("You are here!"));
		
	}
}
