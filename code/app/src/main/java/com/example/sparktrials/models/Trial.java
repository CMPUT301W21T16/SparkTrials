package com.example.sparktrials.models;

public abstract class Trial {
    Integer id;
    GeoLocation location;
    Profile profile;

    public Trial(){
        this.id = null;
        this.profile = null;
        this.location = null;
    }

    public Trial(Integer id, GeoLocation location, Profile profile){
        this.id = id;
        this.location = location;
        this.profile = profile;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLocation(GeoLocation location){
        this.location = location;
    }

    public GeoLocation getLocation(){
        return this.location;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
