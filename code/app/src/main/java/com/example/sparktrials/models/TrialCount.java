package com.example.sparktrials.models;

/**
 * This class extends the basic trial class. This type of trial will keep a count that
 * starts at zero and can be incremented
 */
public class TrialCount extends Trial{
    /**
     * Initializes a blank trial and sets count to 0
     */
    public TrialCount(){
        super();
        this.value = 0.0;
    }
    /**
     * This constructor initializes a filled trial. Initializes count to 0 always
     * @param id
     *    The id of the trial. Generated elsewhere and passed in
     * @param location
     *    The location that the trial was held. Will often be null
     * @param profile
     *    The profile of the user that created the trial
     */
    public TrialCount (String id, GeoLocation location, Profile profile){
        super(id, location, profile);
        this.value = 0.0;
    }

    /**
     * Increments the count attribute
     */
    public void addCount() {
        this.value += 1.0;
    }


    /**
     * Completely sets the count to a specified amount. Should not be used often - counts should be incremented
     * @param count
     *    The new count value that the trial will hold
     */
    public void setCount(Integer count) {
        this.value = (double)count;
    }
}
