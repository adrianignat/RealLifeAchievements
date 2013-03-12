package com.example.reallifeachievements;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;

public class MapHelper {
	
	private GoogleMap map;		
	
	public MapHelper(GoogleMap map) {		
		this.map = map;
	}		
	
	public void drawLocationOnMainThread(final Location location) {
		
		Handler mainThreadHandler = new Handler(Looper.getMainLooper());
		
		mainThreadHandler.post(new Runnable(){
			@Override
			public void run() {
				drawLocation(location);
			}
		});				    
	}
	
	private void drawLocation(Location location){
		LatLng userPosition = new LatLng(location.getLatitude(), location.getLongitude());
		
		map.addMarker(new MarkerOptions().position(userPosition)
				.title("You are here!"));
		
		zoomIntoPosition(userPosition);
	}
	
	public void zoomIntoPosition(LatLng position)
	{
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

		//Zoom in, animating the camera.
		map.animateCamera(CameraUpdateFactory.zoomTo(20), 2000, null);
	}
}
