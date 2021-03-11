package com.example.sparktrials.models;

import java.util.ArrayList;

/**
 * This is a class that contains information to record a point on the surface of the Earth
 * Lat is the latitude of the point
 * Lon is the longitude of the point
 */
public class GeoLocation {
    Float lat;
    Float lon;

    /**
     * Constructor that initiates the point on Earth
     * Ensures that coordinates are valid
     * @param lat
     * @param lon
     */
    public GeoLocation(Float lat, Float lon){
        if (lat<-90){
            this.lat = (float)-90.0;
        } else if (lat>90){
            this.lat = (float)90.0;
        } else {
            this.lat = lat;
        }

        if (lon<-180){
            this.lat = (float)-180.0;
        } else if (lon>180){
            this.lat = (float)180.0;
        } else {
            this.lat = lat;
        }
    }

    /**
     * This method returns the lat/lon as a list of "coords"
     * @return
     *    Returns an array list containing the lat lon as a coord
     */
    public ArrayList<Float> getCoords(){
        ArrayList<Float> coords = new ArrayList<>();
        coords.add(this.lat);
        coords.add(this.lon);
        
        return coords;
    }

    /**
     *This returns the latitude of the point on Earth
     * @return
     *    Returns the latitude
     */
    public Float getLat() {
        return lat;
    }

    /**
     * This edits the latitude of this geolocation. Adjusts for invalid entry
     * @param lat
     *    Overrides the previously held lat value
     */
    public void setLat(Float lat) {
        if (lat<-90){
            this.lat = (float)-90.0;
        } else if (lat>90){
            this.lat = (float)90.0;
        } else {
            this.lat = lat;
        }
    }

    /**
     * This returns the longitude of the point on Earth
     * @return
     *    Returns the longitude
     */
    public Float getLon() {
        return lon;
    }

    /**
     * This edits the latitude of this geolocation. Adjusts for invalid entry
     * @param lon
     */
    public void setLon(Float lon) {
        if (lon<-180){
            this.lat = (float)-180.0;
        } else if (lon>180){
            this.lat = (float)180.0;
        } else {
            this.lat = lat;
        }
    }
}
