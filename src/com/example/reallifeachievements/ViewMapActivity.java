package com.example.reallifeachievements;

import com.example.reallifeachievements.LocationHelper.LocationResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class ViewMapActivity extends Activity {
	
	private GoogleMap map;
	private MapHelper mapHelper;
	LocationHelper locationHelper;
	LocationResult locationResult = new LocationResult(){
	    @Override
	    public void locationUpdated(Location location){
	        drawLocation(location);
	    }		
	};		

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_map);
		// Show the Up button in the action bar.
		setupActionBar();
		
		initialize();
	}

	private void initialize() {
		map = getMap();
		mapHelper = new MapHelper(map);
		locationHelper = new LocationHelper();
	}
	
	private void drawLocation(final Location location)
	{
		mapHelper.drawLocationOnMainThread(location);				
	}

	private GoogleMap getMap() {
		return ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		int playServiceAvailability = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		
		if (playServiceAvailability != ConnectionResult.SUCCESS)
			GooglePlayServicesUtil.getErrorDialog(playServiceAvailability, this, 0);
		
		locationHelper.startTrackingPosition(this, locationResult);
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		
		locationHelper.stopTrackingPosition();
	}

}
