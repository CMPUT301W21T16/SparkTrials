package com.example.sparktrials.models;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GeoMap implements OnMapReadyCallback {

    private GoogleMap map;

    private GeoLocation geoLocation;

    private boolean isEditable;
    private boolean markerSet;

    public GeoMap(boolean isEditable) {
        this.isEditable = isEditable;
        geoLocation = new GeoLocation();
        markerSet = false;
    }

    public GeoMap(Experiment experiment, boolean isEditable) {
        this.isEditable = isEditable;
        this.geoLocation = experiment.getRegion();
        markerSet = true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;


        if (isEditable) {
            map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng center) {
                    map.clear();

                    geoLocation.setLat(center.latitude);
                    geoLocation.setLon(center.longitude);

                    map.addMarker(new MarkerOptions()
                            .position(center));

                    map.moveCamera(CameraUpdateFactory.newLatLng(center));

                    markerSet = true;
                }
            });
        } else {
            LatLng center = new LatLng(geoLocation.getLat(), geoLocation.getLon());

            map.addMarker(new MarkerOptions()
                    .position(center));

            displayCircle(center, geoLocation.getRadius());

            float zoomLevel = 13.0f;
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomLevel));
        }
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public boolean isMarkerSet() {
        return markerSet;
    }

    private void displayCircle(LatLng center, double radius) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(center);
        circleOptions.radius(radius);
        circleOptions.strokeWidth(5);
        circleOptions.strokeColor(Color.BLUE);
        circleOptions.fillColor(Color.argb(50, 0, 0, 120));

        System.out.println(radius);

        map.addCircle(circleOptions);
    }

}
