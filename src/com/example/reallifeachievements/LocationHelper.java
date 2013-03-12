package com.example.reallifeachievements;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationHelper {

	private static final int TWO_MINUTES = 1000 * 60 * 2;
	
	Timer timer1;
    LocationManager locationManager;
    LocationResult locationResult;
    boolean gps_enabled=false;
    boolean network_enabled=false;
    
    
    public boolean startTrackingPosition(Context context, LocationResult result)
    {
        locationResult=result;
        if(locationManager==null)
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //exceptions will be thrown if provider is not permitted.
        try{gps_enabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);}catch(Exception ex){}
        try{network_enabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);}catch(Exception ex){}

        //don't start listeners if no provider is enabled
        if(!gps_enabled && !network_enabled)
            return false;

        if(gps_enabled)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, locationListenerGps);
        if(network_enabled)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 1, locationListenerNetwork);
        
        timer1=new Timer();
        timer1.schedule(new GetLastLocation(), 2000);
        return true;
    }

    public void stopTrackingPosition()
    {
    	locationManager.removeUpdates(locationListenerGps);
        locationManager.removeUpdates(locationListenerNetwork);
    }
    
    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.locationUpdated(location);                        
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.locationUpdated(location);                        
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };   
	

	/** Determines whether one Location reading is better than the current Location fix
	 * @param newLocation  The new Location that you want to evaluate
	 */
	protected boolean isBetterLocation(Location newLocation, Location currentLocation) {	    
		// Check whether the new location fix is newer or older
		long timeDelta = newLocation.getTime() - currentLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (newLocation.getAccuracy() - currentLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(newLocation.getProvider(),
				currentLocation.getProvider());

		// Determine location quality using a combination of timeliness and accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

	class GetLastLocation extends TimerTask {
        @Override
        public void run() {
             Location networkLocation=null, gpsLocation=null;
             if(gps_enabled)
                 gpsLocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
             if(network_enabled)
                 networkLocation=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

             //if there are both values use the latest one
             if(gpsLocation!=null && networkLocation!=null){
            	 if (isBetterLocation(gpsLocation, networkLocation))
                 //if(gpsLocation.getTime()>networkLocation.getTime())
                     locationResult.locationUpdated(gpsLocation);
                 else
                     locationResult.locationUpdated(networkLocation);
                 return;
             }

             if(gpsLocation!=null){
                 locationResult.locationUpdated(gpsLocation);
                 return;
             }
             if(networkLocation!=null){
                 locationResult.locationUpdated(networkLocation);
                 return;
             }
             //locationResult.gotLocation(null);
        }
    }

    public static abstract class LocationResult{
        public abstract void locationUpdated(Location location);
    }
}
