package com.example.sparktrials.models;

/**
 * Outlines the general elements that any trial for an experiment will have
 * This abstract class is to be extended by different variations on a trial
 * id is a unique identifier use to keep track of different trials
 * location is the location that this trial was taken. May sometimes be null
 * profile is the profile of the user who took this trial
 */
public abstract class Trial {
    Integer id;
    GeoLocation location;
    Profile profile;

    /**
     * This constructor creates a blank trial to be filled out
     * Initializes the parameters to null values
      */
    public Trial(){
        this.id = null;
        this.profile = null;
        this.location = null;
    }

    /**
     * This constructor initializes the parameters of the trial. Is called by the constructors
     * of the subclasses
     * @param id
     *    Sets the id of the trial. This values will be generated elsewhere and passed here
     * @param location
     *    Sets the location of the trial. Often will be null when no geolocation is required
     * @param profile
     *    Sets the profile of the trial to be the user who instantiated the trial
     */
    public Trial(Integer id, GeoLocation location, Profile profile){
        this.id = id;
        this.location = location;
        this.profile = profile;
    }

    /**
     * Gets the unique id of the trial
     * @return
     *    Returns the id of the trial
     */
    public Integer getId() {
        return id;
    }

    /**
     * Initializes the id of the trial. Does not allow overwrites
     * @param id
     *    The new id that the trial will take
     */
    public void setId(Integer id) {
        if (this.id == null){
            this.id = id;
        }
    }

    /**
     * Sets the location of the trial
     * @param location
     *    New location that the trial will take
     */
    public void setLocation(GeoLocation location){
        this.location = location;
    }

    /**
     * Gets the location of the trial
     * @return
     *    Returns the location that the trial has. Will often be null
     */
    public GeoLocation getLocation(){
        return this.location;
    }

    /**
     * Gets the profile of the user who made this trial
     * @return
     *    Returns the profile of this trial
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Sets the profile of the trial. Doesn't allow overwrites
     * @param profile
     *    The new profile of the trial
     */
    public void setProfile(Profile profile) {
        if (this.profile == null){
            this.profile = profile;
        }
    }

    /**
     * Overwrites the profile of the trial
     * @param profile
     *    The new profile that the trial will have
     */
    public void overwriteProfile(Profile profile) {
        this.profile = profile;

    }
}
