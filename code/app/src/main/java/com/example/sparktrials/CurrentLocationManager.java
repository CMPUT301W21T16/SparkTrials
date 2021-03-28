package com.example.sparktrials;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.example.sparktrials.models.GeoLocation;

import static android.content.Context.LOCATION_SERVICE;

public class CurrentLocationManager implements LocationListener {
    Context context;

    LocationManager locationManager;

    MutableLiveData<GeoLocation> currentLocation;

    public CurrentLocationManager(Context context) {
        this.context = context;
        currentLocation = new MutableLiveData<>();
    }

    /**
     * Gets the updated location of the device, and causes onLocationChanged() to be called as
     * a callback function.
     */
    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public MutableLiveData<GeoLocation> getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5, this);
        }catch (Exception e){
            e.printStackTrace();
        }
        return currentLocation;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        // Update the currentLocation of the user
        GeoLocation cLoc = new GeoLocation(location.getLatitude(), location.getLongitude());

        currentLocation.setValue(cLoc);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {}

    @Override
    public void onProviderDisabled(@NonNull String provider) {}
}
