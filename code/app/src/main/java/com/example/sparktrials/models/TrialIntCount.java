package com.example.sparktrials.models;

/**
 * This class extends the basic trial class. This type of trial has a set count
 */
public class TrialIntCount extends Trial {
    Integer count;

    /**
     * Initializes a blank trial with a set count
     * @param count
     *    The count that this trial will hold
     */
    public TrialIntCount(Integer count){
        if (count>=0) {
            this.count = count;
        } else {
            this.count = 0;
        }
    }
    /**
     * This constructor initializes a filled trial with a given count
     * @param id
     *    The id of the trial. Generated elsewhere and passed in
     * @param location
     *    The location that the trial was held. Will often be null
     * @param profile
     *    The profile of the user that created the trial
     * @param count
     *    Non-negative integer
     */
    public TrialIntCount(Integer id, GeoLocation location, Profile profile, Integer count){
        super(id, location, profile);
        if (count>=0) {
            this.count = count;
        } else {
            this.count = 0;
        }
    }

    /**
     * Retrieves the count that this trial holds
     * @return
     *    Returns the count of this trial
     */
    public Integer getValue() {
        return count;
    }

    /**
     * Updates the count of this trial
     * @param count
     *    The new count that the count attribute will hold
     */
    public void setCount(Integer count) {
        if (count>0){
            this.count = count;
        }
    }
}
