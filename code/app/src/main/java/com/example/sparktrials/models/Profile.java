package com.example.sparktrials.models;

/**
 * This class keeps track of each user profile with an id, their username and contact info
 */
public class Profile {
    String id;
    String username;
    String contact;

    /**
     * This constructor creates a default Profile with no attributes
     */
    public Profile(){
        this.id = null;
        this.username = null;
        this.contact = null;
    }

    /**
     * This constructor initializes a unique profile
     * @param id
     *    A unique id used to identify this profile. Generated elsewhere and passed up
     *    Default username will be based on their id
     */
    public Profile(String id){
        this.id = id;
        this.username = "user"+id;
    }

    /**
     * This constructor fills out a profile
     * TO BE USED WHEN DOWNLOADING A PRE-EXISTING profile
     * @param id
     *    A unique id used to identify this profile
     * @param username
     *    The username of the person
     * @param contact
     *    The contact info of the person
     */
    public Profile(String id, String username, String contact){
        this.id = id;
        this.username = username;
        this.contact = contact;
    }

    /**
     * This method returns the profile's id
     * @return
     *    Returns the id of this profile
     */
    public String getId() {
        return id;
    }

    /**
     * This method initializes the id of the user
     * id should not overwrite an existing id
     * @param id
     *    New id that this profile will take
     */
    public void setId(String id) {
        if (this.id == null){
            this.id = id;
        }
    }

    /**
     * This method overwrites the id of the user
     * Irreversible
     * @param id
     *    New id that this profile will take
     */
    public void overwriteId(String id) {
        this.id = id;
    }

    /**
     * This method retrieves the name of this profile
     * @return
     *    Returns the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * This method sets the username of the profile
     * @param username
     *    The new username that this profile will take
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * This method retrieves the contact info from this profile
     * @return
     *    Returns the contact info from the profile
     */
    public String getContact() {
        return contact;
    }

    /**
     * Overriding equals() method from object class for Profiles
     * @param o
     *  The object to compare with
     * @return
     *  True if the Profiles have the same ID
     *  False otherwise
     */
    @Override
    public boolean equals(Object o){
        boolean retVal = false;

        if(o instanceof Profile){
            Profile profile = (Profile) o;
            retVal = profile.getId().equals(this.id);
        }

        return retVal;
    }

    /**
     * This method sets the contact info of the profile
     * @param contact
     *    The new contact that this profile will take
     */
    public void setContact(String contact) {
        this.contact = contact;
    }
}