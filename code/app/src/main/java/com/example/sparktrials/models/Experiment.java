package com.example.sparktrials.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

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
    private String id;
    private String type;
    private Profile owner;
    private ArrayList<Trial> trials;
    private String title;
    private String desc;
    private GeoLocation region;
    private Boolean reqLocation;
    private Integer minNTrials;
    private Boolean open;
    private Date date;
    private ArrayList<String> blacklist;

    /**
     * Initiates an empty experiment that will be filled out later
     * Sets experiment attribute to some default values
     */
    public Experiment(String id){
        this.id = id;
        this.type = null;
        this.owner = null;
        this.trials = new ArrayList<>();
        this.title = "N/A";
        this.desc = "N/A";
        this.region = null;
        this.reqLocation = false;
        this.minNTrials = 0;
        this.open = true;
        this.date = new Date();
        this.blacklist = new ArrayList<>();
    }

    /**
     * Initiates a new experiment with the given values
     * USED FOR CREATING NEW EXPERIMENTS
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
    public Experiment(String id, String type, Profile owner, String title, String desc, GeoLocation region, Boolean reqLocation, Integer minNTrials){
        this.id = id;
        this.type = type.toLowerCase();
        this.owner = owner;
        this.trials = new ArrayList<>();
        this.title = title;
        this.desc = desc;
        this.region = region;
        this.reqLocation = reqLocation;
        this.minNTrials = minNTrials;
        this.open = true;
        this.date = new Date();
        this.blacklist = new ArrayList<>();
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
    public Experiment(String id, String type, Profile owner, ArrayList<Trial> trials, String title, String desc,
                      GeoLocation region, Boolean reqLocation, Integer minNTrials, Boolean open, Date date, ArrayList<String> blacklist){
        this.id = id;
        this.type = type.toLowerCase();
        this.owner = owner;
        this.trials = trials;
        this.title = title;
        this.desc = desc;
        this.region = region;
        this.reqLocation = reqLocation;
        this.minNTrials = minNTrials;
        this.open = open;
        this.date = date;
        this.blacklist = blacklist;
    }

    public Experiment() {
        this.date = new Date();

    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Fetches the id of the experiment
     * @return
     *    The integer id that the experiment holds
     */
    public String getId() {
        return id;
    }

    /**
     * Fetches the type of the experiment
     * @return
     *    The type of the experiment as a string.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the experiment to a new value
     * SHOULD NOT BE USED UNLESS CHANGING FROM NULL!
     * @param type
     *    The type that the experiment will be
     *    Should be either binomial, count, intergcount, or measure
     */
    public void setType(String type) {
        if (this.type==null) {
            this.type = type.toLowerCase();
        }
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
     * Replaces the list of trials with a new list of trials
     * Should ONLY be used to initialize an experiment, NEVER to replace trials
     * @param trials
     */
    public void setTrials(ArrayList<Trial> trials){
        if (this.trials.size() == 0) {
            this.trials = trials;
        }
    }

    /**
     * For each experimenter that has submitted more than the minimum number of trials,
     * retrieve their trials
     * @return
     *    returns a list of "valid" trials
     */
    public ArrayList<Trial> getValidTrials() {
        ArrayList<Trial> valid_trials = new ArrayList<>();
        //create a hash map that, for each user, will have an array list populated with
        //that users trials
        HashMap<String, ArrayList<Trial>> user_trials = new HashMap<>();
        Trial trial;
        String user_id;
        for (int i=0; i < this.trials.size(); i++){
            trial = this.trials.get(i);
            if (this.blacklist.contains(trial.getProfile().getId())){
                break;
            }
            user_id = trial.getProfile().getId();
            if (user_trials.get(user_id) == null){
                ArrayList<Trial> arr_list = new ArrayList<>();
                user_trials.put(user_id, arr_list);
            }

            user_trials.get(user_id).add(trial);
        }

        //for each user in the hash map, check if their trials should be included
        //be checking if there are enough of them (enough = more than the min)
        for (String i : user_trials.keySet()){
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
    public Trial getTrial(String id) {
        for (int i=0; i < this.trials.size(); i++){
            if (this.trials.get(i).getId().equals(id)){
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
     * Deletes a trial from the trial list
     * @param trial
     *    The trial to delete
     */
    public void delTrial(Trial trial){
        this.trials.remove(trial);
    }

    /**
     * Deletes a trial from the trial list
     * @param id
     *    The id of the trial to delete
     */
    public void delTrial(Integer id){
        Trial trial = this.trials.get(id);
        this.delTrial(trial);

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
            if (this.trials.get(i).getProfile().getId().equals(id)){
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
     * Gets whether the experiment requires locations or not
     * @return
     *    Returns a boolean true=requires locations, false=doesn't require locations
     */
    public Boolean getReqLocation() {
        return reqLocation;
    }

    /**
     * Sets whether the experiment is required or not
     * @param reqLocation
     *    A boolean true/false
     */
    public void setReqLocation(Boolean reqLocation) {
        this.reqLocation = reqLocation;
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
        return this.date;
    }

    public String getDay(){
        String pattern = "EEE MMM MM HH:mm:ss z yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        String strDate = df.format(date);
        strDate = strDate.substring(4,10);
        return strDate;

    }
    /**
     * Sets a new start date of the experiment. You really should not use this to *change* the date
     * @param date
     *    The new start date that the experiment will take
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * This method gets the blacklist that a profile has
     * @return
     *    A list containing the ids of every experiment that the user is subscribed to
     */
    public ArrayList<String> getBlacklist() {
        return this.blacklist;
    }

    /**
     * Replace the blacklist list
     * Should not really be used except to create a Profile
     * @param blacklist
     *    A list of profile ids
     */
    public void setBlacklist(ArrayList<String> blacklist) {
        this.blacklist = blacklist;
    }

    /**
     * Adds an profile id to the list of blacklist
     * Only add it if it doesn't already exist inside it
     * @param proId
     *    And profile's id
     */
    public void addToBlacklist(String proId) {
        if (!this.isBlacklisted(proId)) {
            this.blacklist.add(proId);
        }
    }

    /**
     * Delete an profile id in the list of blacklist
     * @param proId
     *    The profile id to delete
     */
    public void delFromBlacklist(String proId) {
        this.blacklist.remove(proId);
    }

    /**
     * Adds a list of profile ids to the list of blacklist
     * @param proIds
     *    A list of profile ids
     */
    public void addManyBlacklist(ArrayList<String> proIds){
        for (int i=0; i<proIds.size(); i++){
            this.addToBlacklist(proIds.get(i));
        }
    }

    /**
     * Delete a group of profile ids from the last of blacklist
     * @param proIds
     *   A list of profile ids
     */
    public void delManyBlacklist(ArrayList<String> proIds){
        this.blacklist.removeAll(proIds);
    }

    /**
     * Checks if an profile's id is in this profile's subscribed profiles
     * @param proId
     *    The profile id to check for
     * @return
     *    returns true if profile id is in blacklist
     */
    public boolean isBlacklisted(String proId) {
        return this.blacklist.contains(proId);
    }

    /**
     * Sorts the trials of an experiment in ascending order this comes in handy for calculating descriptive stats
     * @return
     * A sorted list of trials
     */
    public ArrayList<Double> trialsValuesSorted(){
        ArrayList <Double>  values = new ArrayList<>();
        for(int i = 0; i<getValidTrials().size(); i++){
            values.add( (Double) ( getValidTrials().get(i).getValue()));
        }
        Collections.sort(values);
        return values;
    }

    public ArrayList<String> daysOfTrials(){

        ArrayList<String> days = new ArrayList<>();
        for (int i = 0 ; i < getValidTrials().size(); i++){


            this.getDay();
            if (!days.contains(getValidTrials().get(i).getDay())){
                 days.add(getValidTrials().get(i).getDay());

            }
        }

        return  days;
    }

    /**
     * Removes duplicate values of trials necessary for calculating frequencies at which trials occur
     * @return
     * A sorted list with no duplicates
     */
    public Double[] removeDupes(){
        Double[] cleanArray ;
        HashSet<Double> noDupes = new HashSet<Double>(trialsValuesSorted());
        cleanArray = new Double[noDupes.size()];
        noDupes.toArray(cleanArray);
        return cleanArray;
    }

    /**
     * Calculates the x-axis values for the histogram/plot in the stats tab
     * @return
     * A sorted list of type string
     */
    public String [] getXaxis(){
        int size = removeDupes().length;
        String[] str = new String[size];
        for (int i=0 ; i<size; i++){
            str[i] = removeDupes()[i].toString();
        }
        return str ;
    }

    /**
     * Calculates the so called frequencies or Y values for the histogram
     * @return
     * An integer list of frequencies which matches the the index of the getXAxis list
     */
    public int [] frequencies(){
        int []frequencies = new int [getXaxis().length];
        for (int i = 0 ; i < getXaxis().length ; i++){
            for (int j = 0; j< trialsValuesSorted().size(); j++){
                if (Double.parseDouble(getXaxis()[i]) == trialsValuesSorted().get(j)){
                    frequencies[i]+=1;
                }
            }
        }
        return frequencies;
    }



    /**
     * Calculates the Median value for the experiment
     * @return
     * Median of type string
     */
    public String getMedian(){
        double median =0;
        if (trialsValuesSorted().isEmpty() ){
            return "N/A";
        }
        int num = trialsValuesSorted().size();
        if (num  % 2 == 0){
            median = ((double)(trialsValuesSorted().get(num/2) + trialsValuesSorted().get(num/2 - 1)));
        }else{
            median = ((double) trialsValuesSorted().get(num/2));
        }
        return ""+ median;
    }

    /**
     * Calculates the Q1 (quartile 1) for the experiment
     * @return
     * Q1 of type string
     */
    public String getQ1(){
        double quartile;
        if (trialsValuesSorted().isEmpty() ){
            return "N/A";
        }
        int num = trialsValuesSorted().size();
        int length = num +1;
        float newArraySize = (length * ((float) (1) * 25 / 100)) - 1;
        if (newArraySize % 1 == 0) {
            quartile =  trialsValuesSorted().get((int) newArraySize);
        } else {
            int newArraySize1 = (int) (newArraySize);
            quartile = (trialsValuesSorted().get(newArraySize1) + trialsValuesSorted().get(newArraySize1+1)) / 2;
        }
        return String.format("%.2f", quartile);
    }

    /**
     * Calculates the Q3 (quartile 3) for the experiment
     * @return
     * Q3 of type string
     */
    public String getQ3(){
        if (trialsValuesSorted().isEmpty() ){
            return "N/A";
        }
        double quartile;
        int num = trialsValuesSorted().size();
        int length = num +1;
        float newArraySize = (length * ((float) (3) * 25 / 100)) - 1;
        if (newArraySize % 1 == 0) {
            quartile =  trialsValuesSorted().get((int) newArraySize);
        } else {
            int newArraySize1 = (int) (newArraySize);
            quartile = (trialsValuesSorted().get(newArraySize1) + trialsValuesSorted().get(newArraySize1+1)) / 2;
        }
        return String.format("%.2f", quartile);
    }

    /**
     * Finds the total number of trials in an experiment
     * @return
     * Number of trials of type string
     */
    public String getNumTrials(){
        return "" +getValidTrials().size();
    }

    /**
     * Calculates the standard deviation for the experiment
     * @return
     * Standard deviation of type string
     */
    public String getStd(){
        if (trialsValuesSorted().isEmpty() ){
            return "N/A";
        }
        int sum =0 ;
        double mean;
        int num = getValidTrials().size();
        double std= 0;
        for (int i=0 ; i<num;  i++){
            sum+= getValidTrials().get(i).getValue();
        }
        mean = ((double) sum) / ((double) num);
        for (int i = 0 ; i<num; i++){
            std+= Math.pow(getValidTrials().get(i).getValue()- mean, 2);
        }
        std = Math.sqrt(std/num);
        return String.format("%.2f", std);
    }

    /**
     * Calculates the mean (average) for an experiment
     * @return
     * Mean of type String
     */
    public String getMean(){
        if (trialsValuesSorted().isEmpty() ){
            return "N/A";
        }
        int sum =0 ;
        double mean;
        int num = getValidTrials().size();
        for (int i=0 ; i<num;  i++){
            sum+= getValidTrials().get(i).getValue();
        }
        mean = ((double) sum) / ((double) num);
        return String.format("%.2f", mean);
    }
}
