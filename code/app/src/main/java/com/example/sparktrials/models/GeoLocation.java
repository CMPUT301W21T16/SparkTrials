package com.example.sparktrials.models;

import java.util.ArrayList;

public class GeoLocation {
    Float lat;
    Float lon;
    
    public GeoLocation(Float lat, Float lon){
        this.lat = lat;
        this.lon = lon;
    }
    
    public ArrayList<Float> getCoords(){
        ArrayList<Float> coords = new ArrayList<>();
        coords.add(this.lat);
        coords.add(this.lon);
        
        return coords;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLon() {
        return lon;
    }

    public void setLon(Float lon) {
        this.lon = lon;
    }
}
