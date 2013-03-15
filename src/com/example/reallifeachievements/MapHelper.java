package com.example.reallifeachievements;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;

public class MapHelper {
	
	private GoogleMap map;
	private Handler mainThreadHandler;
	
	private Marker previousUserLocationMarker;
	private Circle previousUserLocationCircle;
	
	public MapHelper(GoogleMap map) {		
		this.map = map;
		mainThreadHandler = new Handler(Looper.getMainLooper());
	}		
	
	public void drawLocationOnMainThread(final Location location) {
		
		mainThreadHandler.post(new Runnable(){
			@Override
			public void run() {
				drawLocation(location);
			}
		});				    
	}
	
	private void drawLocation(Location location){	
		
		if (previousUserLocationMarker != null)
			previousUserLocationMarker.remove();
		
		LatLng userPosition = new LatLng(location.getLatitude(), location.getLongitude());				  
		
		previousUserLocationMarker = map.addMarker(new MarkerOptions().position(userPosition)
				.title("You are here!"));
		

		if (previousUserLocationCircle != null)
			previousUserLocationCircle.remove();
		
		CircleOptions circleOptions = new CircleOptions()
		    .center(userPosition)
		    .radius(50)
		    .strokeWidth(4);

		
		previousUserLocationCircle = map.addCircle(circleOptions);
	}
	
	public void zoomIntoPositionOnMainThread(final LatLng position){
		mainThreadHandler.post(new Runnable(){
			@Override
			public void run() {
				zoomIntoPosition(position);
			}
		});
	}
	
	private void zoomIntoPosition(LatLng position)
	{
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

		//Zoom in, animating the camera.
		map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
	}
	
	public void moveToPositionOnMainThread(final LatLng position){
		mainThreadHandler.post(new Runnable(){
			@Override
			public void run() {
				moveToPosition(position);
			}
		});
	}
	
	private void moveToPosition(LatLng position){
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
	}
}
