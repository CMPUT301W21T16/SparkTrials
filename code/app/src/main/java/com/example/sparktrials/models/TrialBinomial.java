package com.example.sparktrials.models;

/**
 * A type of trial that takes either a single success or a single fail
 */
public class TrialBinomial extends Trial{
    Boolean pass;

    /**
     * This constructor initializes a blank trial with a given pass
     * @param pass
     *    Either a true or false, pass or fail
     */
    public TrialBinomial(Boolean pass){
        super();
        this.pass = pass;
    }

    /**
     * This constructor initializes a filled trial with a given pass
     * @param id
     *    The id of the trial. Generated elsewhere and passed in
     * @param location
     *    The location that the trial was held. Will often be null
     * @param profile
     *    The profile of the user that created the trial
     * @param pass
     *    Either a true or false boolean, pass or fail
     */
    public TrialBinomial(Integer id, GeoLocation location, Profile profile, Boolean pass){
        super(id, location, profile);
        this.pass = pass;

    }

    /**
     * Gets whether this trial is a pass or a fail
     * @return
     *    Returns a boolean value to signify pass or fail
     */
    public Boolean getPass() {
        return pass;
    }

    /**
     * Sets the pass attribute either to true (pass) or false (fail)
     * @param pass
     *    A boolean value representing either a pass or a fail that the pass attribute will take
     */
    public void setPass(Boolean pass) {
        this.pass = pass;
    }
}
