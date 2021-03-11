package com.example.sparktrials.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * An experiment class that has an owner, multiple specs that define the experiment, and trials
 * owner is the user who created the experiment
 * trials is the array of trials that have been committed to the experiment
 * title is the title of the experiment
 * desc is the description of the experiment
 * region is the location that the experiment is held at
 * minNTrials is the minimum number of trials an experimenter must complete before committing
 * open is a boolean value, that states whether the experiment is accepting new trials or not
 */
public class Experiment {
    private Profile owner;
    private ArrayList<Trial> trials;
    private String title;
    private String desc;
    private GeoLocation region;
    private Integer minNTrials;
    private Boolean open;
    private Date date;

    /**
     * Initiates an empty experiment that will be filled out later
     * Sets experiment attribute to some default values
     */
    public Experiment(){
        this.owner = null;
        this.trials = new ArrayList<>();
        this.title = "N/A";
        this.desc = "N/A";
        this.region = null;
        this.minNTrials = 0;
        this.open = true;
        this.date = new Date();
    }

    /**
     * Initiates a new experiment with the given values
     * @param owner
     *    The user who created this experiment
     * @param title
     *    The title of the experiment (name)
     * @param desc
     *    The description for the experiment
     * @param region
     *    The location that the experiment is being held at
     * @param minNTrials
     *    The minimum number of trials that a user has to commit before their trials are counted
     */
    public Experiment(Profile owner, String title, String desc, GeoLocation region, Integer minNTrials){
        this.owner = owner;
        this.trials = new ArrayList<>();
        this.title = title;
        this.desc = desc;
        this.region = region;
        this.minNTrials = minNTrials;
        this.open = true;
        this.date = new Date();
    }

    /**
     * Recreates an experiment
     * ONLY USE WHEN DOWNLOADING A PRE-EXISTING EXPERIMENT
     * @param owner
     *    The user who created this experiment
     * @param trials
     *    A list of all the trials in the experiment
     * @param title
     *    The title of th experiment (name)
     * @param desc
     *    The description for the experiment
     * @param region
     *    The location that the experiment is being held at
     * @param minNTrials
     *    The minimum number of trials that a user has to commit before their trials are counted
     * @param open
     *    The boolean value of whether the experiment is "open" for more trials or not
     * @param date
     *    The date that the experiment was created on
     */
    public Experiment(Profile owner, ArrayList<Trial> trials, String title, String desc,
                      GeoLocation region, Integer minNTrials, Boolean open, Date date){
        this.owner = owner;
        this.trials = trials;
        this.title = title;
        this.desc = desc;
        this.region = region;
        this.minNTrials = minNTrials;
        this.open = open;
        this.date = date;
    }

    /**
     * Fetches every trial that's ever been uploaded to the experiment
     * @return
     *    Returns an array list of all the trials in the experiment
     */
    public ArrayList<Trial> getAllTrials() {
        return trials;
    }

    /**
     * For each experimenter that has submitted more than the minimum number of trials,
     * retrieve their trials
     * @return
     *    returns a list of "valid" trials
     */
    public ArrayList<Trial> getValidTrials() {
        //create a hash map that, for each user, will have an array list populated with
        //that users trials
        HashMap<Integer, ArrayList<Trial>> user_trials = new HashMap<>();
        Trial trial;
        Integer trial_id;
        for (int i=0; i < this.trials.size(); i++){
            trial = this.trials.get(i);
            trial_id = trial.getId();
            if (user_trials.get(trial_id) == null){
                ArrayList<Trial> arr_list = new ArrayList<>();
                user_trials.put(trial_id, arr_list);
            }
            user_trials.get(trial_id).add(trial);
        }
        //for each user in the hash map, check if their trials should be included
        //be checking if there are enough of them (enough = more than the min)
        ArrayList<Trial> valid_trials = new ArrayList<>();
        for (int i : user_trials.keySet()){
            ArrayList<Trial> trials_of_user = user_trials.get(i);
            if (trials_of_user.size()>=this.minNTrials){
                for (int j=0; j<trials_of_user.size(); j++){
                    valid_trials.add(trials_of_user.get(j));
                }
            }
        }

        return valid_trials;
    }

    /**
     * Gets a specific trial, if not found, return null
     * @param id
     *    Takes in an id of a trial to find it
     * @return
     *    Returns the requested trial, or a null value
     */
    public Trial getTrial(Integer id) {
        for (int i=0; i < this.trials.size(); i++){
            if (this.trials.get(i).getId()==id){
                return this.trials.get(i);
            }
        }

        return null;
    }

    /**
     * Uploads a trial to the experiment
     * @param trial
     *    The trial to be added to the experiment
     */
    public void addTrial(Trial trial){
        if (this.open){
            this.trials.add(trial);
        }
    }

    /**
     * Retrieves every single trial from a specified user
     * @param id
     *    The id of the user that you want the trials from
     * @return
     *    Returns a list of trials, where each trials is from the specified user
     */
    public ArrayList<Trial> getUserTrials(String id){
        ArrayList<Trial> chosenTrials = new ArrayList<>();
        for (int i=0; i < this.trials.size(); i++){
            if (this.trials.get(i).getProfile().getId()==id){
                chosenTrials.add(this.trials.get(i));
            }
        }
        return chosenTrials;
    }
    /**
     * Uploads an entire list of trials into the experiment
     * @param trials
     *   The array list containing the trials desired to be uploaded
     */
    public void addTrials(ArrayList<Trial> trials) {
        if (this.open) {
            for (int i = 0; i < trials.size(); i++) {
                this.trials.add(trials.get(i));
            }
        }
    }

    /**
     * Gets the title of the experiment
     * @return
     *    returns a string of the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the experiment
     * @param title
     *    A string containing the new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the description of the experiment
     * @return
     *    Returns the string containing the experiment's description
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Sets the description of the experiment
     * @param desc
     *    A string containing the new description
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Gets the region (geo-location) of the experiment
     * @return
     *    Returns the GeoLocation of the experiment
     */
    public GeoLocation getRegion() {
        return region;
    }

    /**
     * Sets the region of the experiment
     * @param region
     *    The new region for the experiment
     */
    public void setRegion(GeoLocation region) {
        this.region = region;
    }

    /**
     * Gets the minimum number of trials needed for a user's trials to get counted
     * @return
     *    Returns an integer for the number of trials needed
     */
    public Integer getMinNTrials() {
        return minNTrials;
    }

    /**
     * Sets the minimum number of trials needed for a user's trials to get counted
     * @param minNTrials
     *    The new minimum
     */
    public void setMinNTrials(Integer minNTrials) {
        this.minNTrials = minNTrials;
    }

    /**
     * Gets the owner of the experiment
     * @return
     */
    public Profile getOwner() {
        return owner;
    }

    /**
     * Sets a new owner for the experiment. You really should not use this to *change* the owner
     * @param owner
     */
    public void setOwner(Profile owner) {
        this.owner = owner;
    }

    /**
     * Gets whether the experiment is open for new trials or not
     * @return
     *    Returns a boolean - true=open, false=closed
     */
    public Boolean getOpen() {
        return open;
    }

    /**
     * Changes the open status of the experiment
     * @param open
     */
    public void setOpen(Boolean open) {
        this.open = open;
    }

    /**
     * Gets the start date of the experiment
     * @return
     *    Returns the date that the experiment started
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets a new start date of the experiment. You really should not use this to *change* the date
     * @param date
     *    The new start date that the experiment will take
     */
    public void setDate(Date date) {
        this.date = date;
    }
}
