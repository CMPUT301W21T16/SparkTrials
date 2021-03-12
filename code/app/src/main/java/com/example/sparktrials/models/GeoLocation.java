package com.example.sparktrials.models;

import java.util.ArrayList;

/**
 * This is a class that contains information to record a point on the surface of the Earth
 * Lat is the latitude of the point
 * Lon is the longitude of the point
 */
public class GeoLocation {
    Double lat;
    Double lon;

    /**
     * Constructor that initiates the point on Earth
     * Ensures that coordinates are valid
     * @param lat
     * @param lon
     */
    public GeoLocation(Double lat, Double lon){
        if (lat<-90){
            this.lat = -90.0;
        } else if (lat>90){
            this.lat = 90.0;
        } else {
            this.lat = lat;
        }

        if (lon<-180){
            this.lon = -180.0;
        } else if (lon>180){
            this.lon = 180.0;
        } else {
            this.lon = lon;
        }
    }

    /**
     * This method returns the lat/lon as a list of "coords"
     * @return
     *    Returns an array list containing the lat lon as a coord
     */
    public ArrayList<Double> getCoords(){
        ArrayList<Double> coords = new ArrayList<>();
        coords.add(this.lat);
        coords.add(this.lon);
        
        return coords;
    }

    /**
     *This returns the latitude of the point on Earth
     * @return
     *    Returns the latitude
     */
    public Double getLat() {
        return lat;
    }

    /**
     * This edits the latitude of this geolocation. Adjusts for invalid entry
     * @param lat
     *    Overrides the previously held lat value
     */
    public void setLat(Double lat) {
        if (lat<-90){
            this.lat = -90.0;
        } else if (lat>90){
            this.lat = 90.0;
        } else {
            this.lat = lat;
        }
    }

    /**
     * This returns the longitude of the point on Earth
     * @return
     *    Returns the longitude
     */
    public Double getLon() {
        return lon;
    }

    /**
     * This edits the latitude of this geolocation. Adjusts for invalid entry
     * @param lon
     */
    public void setLon(Double lon) {
        if (lon<-180){
            this.lon = -180.0;
        } else if (lon>180){
            this.lon = 180.0;
        } else {
            this.lon = lon;
        }
    }
}
