package com.example.sparktrials.models;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GeoMap implements OnMapReadyCallback {

    GoogleMap map;
    GeoLocation geoLocation;
    boolean isEditable;

    public GeoMap(boolean isEditable) {
        this.isEditable = isEditable;
        geoLocation = new GeoLocation();
    }

    public GeoMap(Experiment experiment, boolean isEditable) {
        this.isEditable = isEditable;
        this.geoLocation = experiment.getRegion();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;


        if (isEditable) {
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {

                    map.clear();

                    geoLocation.setLat(latLng.latitude);
                    geoLocation.setLon(latLng.longitude);

                    map.addMarker(new MarkerOptions()
                            .position(latLng));

                    map.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                }

            });
        } else {
            LatLng latLng = new LatLng(geoLocation.getLat(), geoLocation.getLon());

            map.addMarker(new MarkerOptions()
                    .position(latLng));

            float zoomLevel = 13.0f;
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
        }
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }


}
